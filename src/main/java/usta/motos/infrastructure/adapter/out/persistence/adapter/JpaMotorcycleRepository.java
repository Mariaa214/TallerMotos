package usta.motos.infrastructure.adapter.out.persistence.adapter;

import usta.motos.infrastructure.adapter.out.persistence.repository.MotorcycleJpaRepository;
import usta.motos.infrastructure.adapter.out.persistence.repository.ClientJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import usta.motos.application.port.out.MotorcycleRepository;
import usta.motos.domain.model.Motorcycle;
import usta.motos.infrastructure.adapter.out.persistence.entity.ClientEntity;
import usta.motos.infrastructure.adapter.out.persistence.entity.MotorcycleEntity;
import usta.motos.infrastructure.adapter.out.persistence.mapper.MotorcycleEntityMapper;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class JpaMotorcycleRepository implements MotorcycleRepository {

    @Inject
    MotorcycleJpaRepository repository;

    @Inject
    ClientJpaRepository clientJpaRepository;

    @Override
    @Transactional
    public Motorcycle save(Motorcycle motorcycle) {

        ClientEntity clientEntity = clientJpaRepository.findById(motorcycle.getClient().getId());

        MotorcycleEntity entityToSave = MotorcycleEntityMapper.toEntity(motorcycle, clientEntity);
        MotorcycleEntity saved = repository.getEntityManager().merge(entityToSave);
        return MotorcycleEntityMapper.toDomain(saved);
    }

    @Override
    public List<Motorcycle> findAll() {
        return repository.listAll().stream().map(MotorcycleEntityMapper::toDomain).toList();
    }

    @Override
    public Optional<Motorcycle> findByPlate(String plate) {
        return repository.findByPlate(plate).map(MotorcycleEntityMapper::toDomain);
    }

    @Override
    public Motorcycle findById(Long id) {
        MotorcycleEntity entity = repository.findById(id);
        return entity != null ? MotorcycleEntityMapper.toDomain(entity) : null;
    }

    @Override
    @Transactional
    public void update(Motorcycle motorcycle) {

        MotorcycleEntity existingEntity = repository.findById(motorcycle.getId());

        if (existingEntity != null) {

            ClientEntity clientEntity = clientJpaRepository.findById(motorcycle.getClient().getId());

            existingEntity.setPlate(motorcycle.getPlate());
            existingEntity.setBrand(motorcycle.getBrand());
            existingEntity.setModelLine(motorcycle.getModelLine());
            existingEntity.setClient(clientEntity);

            repository.getEntityManager().merge(existingEntity);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}