package usta.motos.infrastructure.adapter.out.persistence.adapter;

import usta.motos.infrastructure.adapter.out.persistence.repository.ClientJpaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import usta.motos.application.port.out.ClientRepository;
import usta.motos.domain.model.Client;
import usta.motos.infrastructure.adapter.out.persistence.entity.ClientEntity;
import usta.motos.infrastructure.adapter.out.persistence.mapper.ClientEntityMapper;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class JpaClientRepository implements ClientRepository {

    @Inject
    ClientJpaRepository repository;

    @Override
    @Transactional
    public Client save(Client client) {
        ClientEntity saved = repository.getEntityManager().merge(ClientEntityMapper.toEntity(client));
        return ClientEntityMapper.toDomain(saved);
    }

    @Override
    public List<Client> findAll() {
        return repository.listAll().stream().map(ClientEntityMapper::toDomain).toList();
    }

    @Override
    public Client findById(Long id) {
        ClientEntity entity = repository.findById(id);
        return entity != null ? ClientEntityMapper.toDomain(entity) : null;
    }

    @Override
    public Optional<Client> findByIdentification(String identification) {
        return repository.findByIdentification(identification).map(ClientEntityMapper::toDomain);
    }

    @Override
    @Transactional
    public void update(Client client) {
        repository.getEntityManager().merge(ClientEntityMapper.toEntity(client));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
