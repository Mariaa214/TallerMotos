package usta.motos.application.port.out;

import usta.motos.domain.model.Motorcycle;

import java.util.List;
import java.util.Optional;


public interface MotorcycleRepository {

    Motorcycle save(Motorcycle motorcycle);

    List<Motorcycle> findAll();

    Optional<Motorcycle> findByPlate(String plate);

    Motorcycle findById(Long id);

    void update(Motorcycle motorcycle);

    void delete(Long id);
}