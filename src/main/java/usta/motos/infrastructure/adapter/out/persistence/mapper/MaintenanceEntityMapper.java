package usta.motos.infrastructure.adapter.out.persistence.mapper;

import usta.motos.domain.model.Maintenance;
import usta.motos.infrastructure.adapter.out.persistence.entity.MaintenanceEntity;
import usta.motos.infrastructure.adapter.out.persistence.entity.MotorcycleEntity;

import java.util.List;
import java.util.stream.Collectors;

// Convierte entre el mantenimiento del dominio (Maintenance) y la entidad JPA (MaintenanceEntity)
public final class MaintenanceEntityMapper {

    private MaintenanceEntityMapper() {
    }


    public static MaintenanceEntity toEntity(Maintenance maintenance, MotorcycleEntity motorcycleEntity) {
        if (maintenance == null) {
            return null;
        }
        MaintenanceEntity entity = new MaintenanceEntity();
        entity.setId(maintenance.getId());
        entity.setMaintenanceDate(maintenance.getMaintenanceDate());
        entity.setDescription(maintenance.getDescription());
        entity.setCost(maintenance.getCost());
        entity.setMechanicName(maintenance.getMechanicName());
        entity.setMotorcycle(motorcycleEntity);
        return entity;
    }


    public static Maintenance toDomain(MaintenanceEntity entity) {
        if (entity == null) {
            return null;
        }
        return Maintenance.builder()
                .id(entity.getId())
                .maintenanceDate(entity.getMaintenanceDate())
                .description(entity.getDescription())
                .cost(entity.getCost())
                .mechanicName(entity.getMechanicName())
                .motorcycle(MotorcycleEntityMapper.toDomain(entity.getMotorcycle()))
                .build();
    }

    public static List<Maintenance> toDomainList(List<MaintenanceEntity> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(MaintenanceEntityMapper::toDomain).collect(Collectors.toList());
    }
}