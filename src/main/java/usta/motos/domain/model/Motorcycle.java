package usta.motos.domain.model;

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
public class Motorcycle {

    private Long id;
    private String plate;
    private String brand;
    private String modelLine;
    private Client client;


    private static final String PLATE_PATTERN = "[A-Z]{3}\\d{2}[A-Z]?";


    public void validate() {
        if (plate == null || plate.isBlank()) {
            throw new BusinessRuleException("La placa es obligatoria");
        }
        plate = plate.trim().toUpperCase().replace(" ", "").replace("-", "");
        if (!plate.matches(PLATE_PATTERN)) {
            throw new BusinessRuleException("La placa " + plate + " no es válida. Formato esperado: ABC12 o ABC12D");
        }
        if (brand == null || brand.isBlank()) {
            throw new BusinessRuleException("La marca es obligatoria");
        }
        brand = brand.trim();
        if (modelLine == null || modelLine.isBlank()) {
            throw new BusinessRuleException("La línea o modelo es obligatoria");
        }
        modelLine = modelLine.trim();
        if (client == null || client.getId() == null) {
            throw new BusinessRuleException("La moto debe tener un cliente (propietario)");
        }
    }
}
