package usta.motos.infrastructure.adapter.out.persistence.mapper;

import usta.motos.domain.model.Client;
import usta.motos.infrastructure.adapter.out.persistence.entity.ClientEntity;

import java.util.List;
import java.util.stream.Collectors;


public final class ClientEntityMapper {

    private ClientEntityMapper() {
    }

    public static ClientEntity toEntity(Client client) {
        if (client == null) {
            return null;
        }
        ClientEntity entity = new ClientEntity();
        entity.setId(client.getId());
        entity.setIdentification(client.getIdentification());
        entity.setFullName(client.getFullName());
        entity.setPhone(client.getPhone());
        return entity;
    }

    public static Client toDomain(ClientEntity entity) {
        if (entity == null) {
            return null;
        }
        return Client.builder()
                .id(entity.getId())
                .identification(entity.getIdentification())
                .fullName(entity.getFullName())
                .phone(entity.getPhone())
                .build();
    }

    public static List<Client> toDomainList(List<ClientEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(ClientEntityMapper::toDomain).collect(Collectors.toList());
    }
}