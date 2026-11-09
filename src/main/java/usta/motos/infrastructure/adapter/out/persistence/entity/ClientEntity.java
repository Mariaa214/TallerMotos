package usta.motos.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

// Entidad JPA que representa la tabla "clients" (clientes)
@Entity
@Table(name = "clients")
@Getter
@Setter
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String identification;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    private String phone;
}