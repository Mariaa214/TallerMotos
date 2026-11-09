package usta.motos.infrastructure.adapter.out.inmemory;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import usta.motos.application.port.out.MotorcycleRepository;
import usta.motos.domain.model.Motorcycle;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
@IfBuildProfile("test")
public class InMemoryMotorcycleRepository implements MotorcycleRepository {

    private final Map<Long, Motorcycle> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public Motorcycle save(Motorcycle motorcycle) {
        if (motorcycle.getId() == null) {
            motorcycle.setId(sequence.incrementAndGet());
        }
        store.put(motorcycle.getId(), motorcycle);
        return motorcycle;
    }

    @Override
    public List<Motorcycle> findAll() {
        return store.values().stream().sorted(Comparator.comparing(Motorcycle::getId)).toList();
    }

    @Override
    public Optional<Motorcycle> findByPlate(String plate) {
        return store.values().stream()
                .filter(m -> m.getPlate() != null && m.getPlate().equalsIgnoreCase(plate))
                .findFirst();
    }

    @Override
    public Motorcycle findById(Long id) {
        return store.get(id);
    }

    @Override
    public void update(Motorcycle motorcycle) {
        if (motorcycle.getId() != null && store.containsKey(motorcycle.getId())) {
            store.put(motorcycle.getId(), motorcycle);
        }
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }
}