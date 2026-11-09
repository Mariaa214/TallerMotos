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

// Entidad JPA que representa la tabla "motorcycles" (motocicletas)
@Entity
@Table(name = "motorcycles")
@Getter
@Setter
public class MotorcycleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String plate;

    @Column(nullable = false)
    private String brand;

    @Column(name = "model_line", nullable = false)
    private String modelLine;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;
}