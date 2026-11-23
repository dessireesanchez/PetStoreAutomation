package page;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class RegistrationErrorReasonTest {

    @ParameterizedTest
    @CsvSource({
            "User ID duplicado,User ID is already in use,true",
            "User ID duplicado,El usuario ya está registrado,true",
            "Email duplicado,This email is already registered,true",
            "confirmación distinta,Passwords do not match,true",
            "User ID duplicado,No se pudo completar,false",
            "Email duplicado,No se pudo completar,false",
            "confirmación distinta,No se pudo completar,false",
            "Email duplicado,User ID is already in use,false",
            "confirmación distinta,Password confirmation is required,false",
            "longitud 7,Password must have at least 8 characters,true",
            "longitud 21,Password must have at most 20 characters,true",
            "sin mayúscula,Password must contain an uppercase letter,true",
            "sin minúscula,Password must contain a lowercase letter,true",
            "sin número,Password must contain a digit,true",
            "sin carácter especial,Password must contain a special character,true",
            "sin número,Phone number is required,false"
    })
    void requiresTheDocumentedReasonInsteadOfAnyError(String reason, String message, boolean expected) {
        assertThat(RegistrationErrorReason.matches(message, reason)).isEqualTo(expected);
    }
}
