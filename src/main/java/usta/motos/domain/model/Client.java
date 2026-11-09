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
public class Client {

    private Long id;
    private String identification;
    private String fullName;
    private String phone;


    public void validate() {
        if (identification == null || identification.isBlank()) {
            throw new BusinessRuleException("La identificación del cliente es obligatoria");
        }
        identification = identification.trim();
        if (!identification.matches("\\d{6,12}")) {
            throw new BusinessRuleException("La identificación debe tener solo números (entre 6 y 12 dígitos)");
        }
        if (fullName == null || fullName.trim().length() < 3) {
            throw new BusinessRuleException("El nombre del cliente debe tener al menos 3 letras");
        }
        fullName = fullName.trim();
        if (phone == null || !phone.trim().matches("\\d{7,10}")) {
            throw new BusinessRuleException("El teléfono debe tener solo números (entre 7 y 10 dígitos)");
        }
        phone = phone.trim();
    }
}
