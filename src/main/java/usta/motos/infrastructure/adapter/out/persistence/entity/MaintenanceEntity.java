package usta.motos.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

// Entidad JPA que representa la tabla "maintenances" (mantenimientos)
@Entity
@Table(name = "maintenances")
@Getter
@Setter
public class MaintenanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "maintenance_date", nullable = false)
    private LocalDate maintenanceDate;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private double cost;

    @Column(name = "mechanic_name", nullable = false)
    private String mechanicName;

    @ManyToOne
    @JoinColumn(name = "motorcycle_id", nullable = false)
    private MotorcycleEntity motorcycle;
}