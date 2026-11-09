package usta.motos.application.port.in;

import usta.motos.domain.model.Motorcycle;
import java.util.List;
import java.util.Optional;

public interface MotorcycleUseCase {
    List<Motorcycle> getAllMotorcycles();
    Motorcycle getMotorcycleById(Long id);
    void saveMotorcycle(Motorcycle motorcycle);
    void updateMotorcycle(Motorcycle motorcycle);
    void deleteMotorcycle(Long id);
}