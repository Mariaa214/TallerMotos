package usta.motos.domain.model;

import java.time.LocalDate;
import usta.motos.domain.exception.BusinessRuleException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Maintenance {

    private Long id;
    private LocalDate maintenanceDate;
    private String description;
    private double cost;
    private String mechanicName;
    private Motorcycle motorcycle;

    public void validate() {
        if (maintenanceDate == null) {
            throw new BusinessRuleException("La fecha del mantenimiento es obligatoria");
        }
        if (maintenanceDate.isAfter(LocalDate.now())) {
            throw new BusinessRuleException("La fecha del mantenimiento no puede ser futura");
        }
        if (description == null || description.trim().length() < 5) {
            throw new BusinessRuleException("La descripción debe tener al menos 5 caracteres");
        }
        description = description.trim();
        if (description.length() > 500) {
            throw new BusinessRuleException("La descripción no puede superar 500 caracteres");
        }
        if (cost <= 0) {
            throw new BusinessRuleException("El costo del mantenimiento debe ser mayor que cero");
        }
        if (mechanicName == null || mechanicName.trim().length() < 3) {
            throw new BusinessRuleException("El nombre del mecánico es obligatorio (mínimo 3 letras)");
        }
        mechanicName = mechanicName.trim();
        if (motorcycle == null || motorcycle.getId() == null) {
            throw new BusinessRuleException("Debe seleccionar la motocicleta del mantenimiento");
        }
    }
}
