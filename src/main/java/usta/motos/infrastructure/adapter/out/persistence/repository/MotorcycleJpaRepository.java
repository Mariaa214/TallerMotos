package usta.motos.infrastructure.adapter.out.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import usta.motos.infrastructure.adapter.out.persistence.entity.MotorcycleEntity;

import java.util.Optional;

// Repositorio Panache para las motocicletas (tabla motorcycles)
@ApplicationScoped
public class MotorcycleJpaRepository implements PanacheRepository<MotorcycleEntity> {

    public Optional<MotorcycleEntity> findByPlate(String plate) {

        return find("plate", plate).firstResultOptional();
    }
}