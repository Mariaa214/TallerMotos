package usta.motos.infrastructure.adapter.out.inmemory;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import usta.motos.application.port.out.ClientRepository;
import usta.motos.domain.model.Client;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
@IfBuildProfile("test")
public class InMemoryClientRepository implements ClientRepository {

    private final Map<Long, Client> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public Client save(Client client) {
        if (client.getId() == null) {
            client.setId(sequence.incrementAndGet());
        }
        store.put(client.getId(), client);
        return client;
    }

    @Override
    public List<Client> findAll() {
        return store.values().stream().sorted(Comparator.comparing(Client::getId)).toList();
    }

    @Override
    public Client findById(Long id) {
        return store.get(id);
    }

    @Override
    public Optional<Client> findByIdentification(String identification) {
        return store.values().stream().filter(c -> identification.equals(c.getIdentification())).findFirst();
    }

    @Override
    public void update(Client client) {
        if (client.getId() != null && store.containsKey(client.getId())) {
            store.put(client.getId(), client);
        }
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }
}