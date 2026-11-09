package usta.motos.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import usta.motos.application.port.in.ClientUseCase;
import usta.motos.application.port.out.ClientRepository;
import usta.motos.application.port.out.MotorcycleRepository;
import usta.motos.domain.exception.BusinessRuleException;
import usta.motos.domain.model.Client;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ManageClientService implements ClientUseCase {

    @Inject
    ClientRepository clientRepository;

    @Inject
    MotorcycleRepository motorcycleRepository;

    @Override
    public Client registerClient(Client client) {
        client.validate();
        checkUniqueIdentification(client);
        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public void saveClient(Client client) {
        registerClient(client);
    }

    @Override
    public void updateClient(Client client) {
        if (client.getId() == null || clientRepository.findById(client.getId()) == null) {
            throw new BusinessRuleException("El cliente que intenta actualizar no existe");
        }
        client.validate();
        checkUniqueIdentification(client);
        clientRepository.update(client);
    }

    @Override
    public void deleteClient(Long id) {
        boolean hasMotorcycles = motorcycleRepository.findAll().stream()
                .anyMatch(m -> m.getClient() != null && id.equals(m.getClient().getId()));
        if (hasMotorcycles) {
            throw new BusinessRuleException("No se puede eliminar el cliente porque tiene motos asociadas");
        }
        clientRepository.delete(id);
    }

    private void checkUniqueIdentification(Client client) {
        Optional<Client> existing = clientRepository.findByIdentification(client.getIdentification());
        if (existing.isPresent() && !existing.get().getId().equals(client.getId())) {
            throw new BusinessRuleException("Ya existe un cliente con la identificación " + client.getIdentification());
        }
    }
}
