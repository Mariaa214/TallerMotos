package usta.motos.infrastructure.adapter.out.persistence.mapper;

import usta.motos.domain.model.Motorcycle;
import usta.motos.infrastructure.adapter.out.persistence.entity.ClientEntity;
import usta.motos.infrastructure.adapter.out.persistence.entity.MotorcycleEntity;

import java.util.List;
import java.util.stream.Collectors;

// Convierte entre la motocicleta del dominio (Motorcycle) y la entidad JPA (MotorcycleEntity)
public final class MotorcycleEntityMapper {

    private MotorcycleEntityMapper() {
    }

    public static MotorcycleEntity toEntity(Motorcycle motorcycle, ClientEntity clientEntity) {
        if (motorcycle == null) {
            return null;
        }
        MotorcycleEntity entity = new MotorcycleEntity();
        entity.setId(motorcycle.getId());
        entity.setPlate(motorcycle.getPlate());
        entity.setBrand(motorcycle.getBrand());
        entity.setModelLine(motorcycle.getModelLine());
        entity.setClient(clientEntity);
        return entity;
    }


    public static Motorcycle toDomain(MotorcycleEntity entity) {
        if (entity == null) {
            return null;
        }
        return Motorcycle.builder()
                .id(entity.getId())
                .plate(entity.getPlate())
                .brand(entity.getBrand())
                .modelLine(entity.getModelLine())
                .client(ClientEntityMapper.toDomain(entity.getClient()))
                .build();
    }

    public static List<Motorcycle> toDomainList(List<MotorcycleEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(MotorcycleEntityMapper::toDomain).collect(Collectors.toList());
    }
}