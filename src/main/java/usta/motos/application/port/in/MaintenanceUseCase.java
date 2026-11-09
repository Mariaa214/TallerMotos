package usta.motos.application.port.in;

import usta.motos.domain.model.Maintenance;
import java.util.List;

public interface MaintenanceUseCase {
    Maintenance registerMaintenance(Maintenance maintenance);
    List<Maintenance> getAllMaintenances();
    List<Maintenance> getAllMaintenancesWithMotorcycle();
    List<Maintenance> getMaintenanceHistoryByPlate(String plate);
    Maintenance getMaintenanceById(Long id);
    void updateMaintenance(Maintenance maintenance);
    void deleteMaintenance(Long id);


}