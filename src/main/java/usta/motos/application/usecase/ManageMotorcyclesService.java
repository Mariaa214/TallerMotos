package usta.motos.application.usecase;

import jakarta.enterprise.context.ApplicationScoped;
import usta.motos.application.port.in.MotorcycleUseCase;
import usta.motos.application.port.out.MaintenanceRepository;
import usta.motos.application.port.out.MotorcycleRepository;
import usta.motos.domain.exception.BusinessRuleException;
import usta.motos.domain.model.Motorcycle;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ManageMotorcyclesService implements MotorcycleUseCase {

    private final MotorcycleRepository motorcycleRepository;
    private final MaintenanceRepository maintenanceRepository;

    public ManageMotorcyclesService(MotorcycleRepository motorcycleRepository,
                                    MaintenanceRepository maintenanceRepository) {
        this.motorcycleRepository = motorcycleRepository;
        this.maintenanceRepository = maintenanceRepository;
    }

    @Override
    public List<Motorcycle> getAllMotorcycles() {
        return motorcycleRepository.findAll();
    }

    @Override
    public Motorcycle getMotorcycleById(Long id) {
        return motorcycleRepository.findById(id);
    }

    @Override
    public void saveMotorcycle(Motorcycle motorcycle) {
        motorcycle.validate();
        checkUniquePlate(motorcycle);
        motorcycleRepository.save(motorcycle);
    }

    @Override
    public void updateMotorcycle(Motorcycle motorcycle) {
        if (motorcycle.getId() == null || motorcycleRepository.findById(motorcycle.getId()) == null) {
            throw new BusinessRuleException("La moto que intenta actualizar no existe");
        }
        motorcycle.validate();
        checkUniquePlate(motorcycle);
        motorcycleRepository.update(motorcycle);
    }

    @Override
    public void deleteMotorcycle(Long id) {
        Motorcycle motorcycle = motorcycleRepository.findById(id);
        if (motorcycle == null) {
            throw new BusinessRuleException("La moto que intenta eliminar no existe");
        }

        if (!maintenanceRepository.findByMotorcyclePlate(motorcycle.getPlate()).isEmpty()) {
            throw new BusinessRuleException("No se puede eliminar la moto " + motorcycle.getPlate()
                    + " porque tiene mantenimientos registrados");
        }
        motorcycleRepository.delete(id);
    }


    private void checkUniquePlate(Motorcycle motorcycle) {
        Optional<Motorcycle> existing = motorcycleRepository.findByPlate(motorcycle.getPlate());
        if (existing.isPresent() && !existing.get().getId().equals(motorcycle.getId())) {
            throw new BusinessRuleException("Ya existe una moto con la placa " + motorcycle.getPlate());
        }
    }
}
