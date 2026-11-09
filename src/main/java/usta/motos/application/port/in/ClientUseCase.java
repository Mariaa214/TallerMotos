package usta.motos.application.port.in;

import usta.motos.domain.model.Client;
import java.util.List;

public interface ClientUseCase {
    Client registerClient(Client client);
    List<Client> getAllClients();
    Client getClientById(Long id);
    void saveClient(Client client);
    void updateClient(Client client);
    void deleteClient(Long id);
}