package usta.motos.application.port.out;

import usta.motos.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Client save(Client client);
    List<Client> findAll();
    Client findById(Long id);
    Optional<Client> findByIdentification(String identification);
    void update(Client client);
    void delete(Long id);
}
