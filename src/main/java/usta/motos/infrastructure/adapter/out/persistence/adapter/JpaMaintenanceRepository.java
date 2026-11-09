package usta.motos.infrastructure.adapter.out.persistence.adapter;

import usta.motos.infrastructure.adapter.out.persistence.repository.MaintenanceJpaRepository;
import usta.motos.infrastructure.adapter.out.persistence.repository.MotorcycleJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import usta.motos.application.port.out.MaintenanceRepository;
import usta.motos.domain.model.Maintenance;
import usta.motos.infrastructure.adapter.out.persistence.entity.MaintenanceEntity;
import usta.motos.infrastructure.adapter.out.persistence.entity.MotorcycleEntity;
import usta.motos.infrastructure.adapter.out.persistence.mapper.MaintenanceEntityMapper;

import java.util.List;
import java.util.Optional;

// Adaptador JPA del puerto MaintenanceRepository: guarda/lee mantenimientos en MySQL
@ApplicationScoped
public class JpaMaintenanceRepository implements MaintenanceRepository {

    @Inject
    MaintenanceJpaRepository repository;

    @Inject
    MotorcycleJpaRepository motorcycleJpaRepository;

    @Override
    @Transactional
    public Maintenance save(Maintenance maintenance) {

        MotorcycleEntity motorcycleEntity = motorcycleJpaRepository.findById(maintenance.getMotorcycle().getId());

        MaintenanceEntity entityToSave = MaintenanceEntityMapper.toEntity(maintenance, motorcycleEntity);
        MaintenanceEntity saved = repository.getEntityManager().merge(entityToSave);
        return MaintenanceEntityMapper.toDomain(saved);
    }

    @Override
    public List<Maintenance> findAll() {
        return repository.listAll().stream().map(MaintenanceEntityMapper::toDomain).toList();
    }

    @Override
    public List<Maintenance> findAllWithMotorcycle() {
        List<MaintenanceEntity> entities = repository.findAllWithMotorcycle();
        return MaintenanceEntityMapper.toDomainList(entities);
    }

    @Override
    public List<Maintenance> findByMotorcyclePlate(String plate) {
        return repository.findByMotorcyclePlate(plate).stream().map(MaintenanceEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Maintenance> findById(Long id) {
        return repository.findByIdOptional(id).map(MaintenanceEntityMapper::toDomain);
    }

    @Override
    @Transactional
    public void update(Maintenance maintenance) {

        MaintenanceEntity existingEntity = repository.findById(maintenance.getId());

        if (existingEntity != null) {

            MotorcycleEntity motorcycleEntity = motorcycleJpaRepository.findById(maintenance.getMotorcycle().getId());

            existingEntity.setMaintenanceDate(maintenance.getMaintenanceDate());
            existingEntity.setDescription(maintenance.getDescription());
            existingEntity.setCost(maintenance.getCost());
            existingEntity.setMechanicName(maintenance.getMechanicName());
            existingEntity.setMotorcycle(motorcycleEntity);

            repository.getEntityManager().merge(existingEntity);
        }
    }


    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
