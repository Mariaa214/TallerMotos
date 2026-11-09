package usta.motos.infrastructure.adapter.out.inmemory;

import io.quarkus.arc.profile.IfBuildProfile;
import jakarta.enterprise.context.ApplicationScoped;
import usta.motos.application.port.out.MaintenanceRepository;
import usta.motos.domain.model.Maintenance;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
@IfBuildProfile("test")
public class InMemoryMaintenanceRepository implements MaintenanceRepository {

    private final Map<Long, Maintenance> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public Maintenance save(Maintenance maintenance) {
        if (maintenance.getId() == null) {
            maintenance.setId(sequence.incrementAndGet());
        }
        store.put(maintenance.getId(), maintenance);
        return maintenance;
    }

    @Override
    public List<Maintenance> findAll() {
        return store.values().stream().sorted(Comparator.comparing(Maintenance::getId)).toList();
    }

    @Override
    public List<Maintenance> findByMotorcyclePlate(String plate) {
        return store.values().stream()
                .filter(m -> m.getMotorcycle() != null &&
                        m.getMotorcycle().getPlate() != null &&
                        m.getMotorcycle().getPlate().equalsIgnoreCase(plate))
                .sorted(Comparator.comparing(Maintenance::getId))
                .toList();
    }

    @Override
    public Optional<Maintenance> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void update(Maintenance maintenance) {
        if (maintenance.getId() != null && store.containsKey(maintenance.getId())) {
            store.put(maintenance.getId(), maintenance);
        }
    }

    @Override
    public List<Maintenance> findAllWithMotorcycle() {
        return findAll();
    }

    @Override
    public void delete(Long id) {
        store.remove(id);
    }
}