package usta.motos.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import usta.motos.application.port.in.MaintenanceUseCase;
import usta.motos.application.port.out.MaintenanceRepository;
import usta.motos.application.port.out.MotorcycleRepository;
import usta.motos.domain.exception.BusinessRuleException;
import usta.motos.domain.model.Maintenance;

import java.util.List;

@ApplicationScoped
public class RegisterMaintenanceService implements MaintenanceUseCase {

    private final MaintenanceRepository maintenanceRepository;
    private final MotorcycleRepository motorcycleRepository;

    public RegisterMaintenanceService(MaintenanceRepository maintenanceRepository,
                                      MotorcycleRepository motorcycleRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.motorcycleRepository = motorcycleRepository;
    }

    @Override
    public Maintenance registerMaintenance(Maintenance maintenance) {
        maintenance.validate();
        checkMotorcycleExists(maintenance);
        return maintenanceRepository.save(maintenance);
    }

    @Override
    @Transactional
    public List<Maintenance> getAllMaintenances() {
        return maintenanceRepository.findAllWithMotorcycle();
    }

    @Override
    public List<Maintenance> getMaintenanceHistoryByPlate(String plate) {
        return maintenanceRepository.findByMotorcyclePlate(plate.trim().toUpperCase());
    }

    @Override
    public Maintenance getMaintenanceById(Long id) {
        return maintenanceRepository.findById(id).orElse(null);
    }

    @Override
    public void updateMaintenance(Maintenance maintenance) {
        if (maintenance.getId() == null || maintenanceRepository.findById(maintenance.getId()).isEmpty()) {
            throw new BusinessRuleException("El mantenimiento que intenta actualizar no existe");
        }
        maintenance.validate();
        checkMotorcycleExists(maintenance);
        maintenanceRepository.update(maintenance);
    }

    @Override
    public void deleteMaintenance(Long id) {
        if (maintenanceRepository.findById(id).isEmpty()) {
            throw new BusinessRuleException("El mantenimiento que intenta eliminar no existe");
        }
        maintenanceRepository.delete(id);
    }

    @Override
    @Transactional
    public List<Maintenance> getAllMaintenancesWithMotorcycle() {
        return maintenanceRepository.findAllWithMotorcycle();
    }


    private void checkMotorcycleExists(Maintenance maintenance) {
        if (motorcycleRepository.findById(maintenance.getMotorcycle().getId()) == null) {
            throw new BusinessRuleException("La motocicleta seleccionada no existe");
        }
    }
}
