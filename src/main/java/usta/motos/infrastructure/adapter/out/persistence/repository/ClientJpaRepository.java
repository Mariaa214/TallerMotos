package usta.motos.infrastructure.adapter.out.persistence.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import usta.motos.infrastructure.adapter.out.persistence.entity.ClientEntity;

import java.util.Optional;

// Repositorio Panache para los clientes (tabla clients)
@ApplicationScoped
public class ClientJpaRepository implements PanacheRepository<ClientEntity> {

    public Optional<ClientEntity> findByIdentification(String identification) {
        return find("identification", identification).firstResultOptional();
    }
}
