package usta.motos.application.port.out;

import usta.motos.domain.model.Maintenance;

import java.util.List;
import java.util.Optional;

public interface MaintenanceRepository {

    Maintenance save(Maintenance maintenance);

    List<Maintenance> findAll();

    List<Maintenance> findAllWithMotorcycle();

    List<Maintenance> findByMotorcyclePlate(String plate);

    Optional<Maintenance> findById(Long id);

    void update(Maintenance maintenance);

    void delete(Long id);
}