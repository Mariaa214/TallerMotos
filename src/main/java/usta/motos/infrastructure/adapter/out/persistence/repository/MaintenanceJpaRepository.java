package usta.motos.infrastructure.adapter.out.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import usta.motos.infrastructure.adapter.out.persistence.entity.MaintenanceEntity;

import java.util.List;

@ApplicationScoped
public class MaintenanceJpaRepository implements PanacheRepository<MaintenanceEntity> {

    public List<MaintenanceEntity> findAllWithMotorcycle() {
        return find("SELECT m FROM MaintenanceEntity m LEFT JOIN FETCH m.motorcycle").list();
    }

    public List<MaintenanceEntity> findByMotorcyclePlate(String plate) {
        return find("SELECT m FROM MaintenanceEntity m JOIN FETCH m.motorcycle moto WHERE moto.plate = ?1", plate).list();
    }
}