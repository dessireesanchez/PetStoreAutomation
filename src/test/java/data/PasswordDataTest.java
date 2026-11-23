package data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordDataTest {

    @AfterEach
    void clearSecrets() {
        ScenarioSecrets.clear();
    }

    @ParameterizedTest
    @ValueSource(ints = {8, 12, 20})
    void validPasswordsSatisfyTheDocumentedPolicy(int length) {
        String password = PasswordData.valid(length);
        assertShape(password, length, true, true, true, true);
    }

    @ParameterizedTest
    @CsvSource({
            "longitud 7, 7, true, true, true, true",
            "longitud 21, 21, true, true, true, true",
            "sin mayúscula, 12, false, true, true, true",
            "sin minúscula, 12, true, false, true, true",
            "sin número, 12, true, true, false, true",
            "sin carácter especial, 12, true, true, true, false"
    })
    void invalidPasswordsViolateOnlyTheNamedCondition(String rule, int length,
            boolean upper, boolean lower, boolean number, boolean special) {
        assertShape(PasswordData.invalid(rule), length, upper, lower, number, special);
    }

    @Test
    void wrongPasswordDiffersFromTheRegisteredCredential() {
        String original = PasswordData.valid(12);
        String different = PasswordData.differentFrom(original);
        assertThat(different.equals(original)).isFalse();
        assertShape(different, 12, true, true, true, true);
    }

    @Test
    void runtimeSecretsAreRedactedFromDiagnostics() {
        String secret = UUID.randomUUID().toString();
        ScenarioSecrets.register(secret);
        String redacted = ScenarioSecrets.redact("valor=" + secret);
        assertThat(redacted.equals("valor=[REDACTADO]")).isTrue();
    }

    @Test
    void generatedCredentialsAreRedactedAndExcludedFromFixtureText() {
        RegistrationData account = RegistrationData.valid();
        String password = account.value("New password");
        assertThat(ScenarioSecrets.redact("valor=" + password).equals("valor=[REDACTADO]"))
                .isTrue();
        assertThat(account.toString().contains(password)).isFalse();
    }

    @Test
    void secretsAreNotRetainedBetweenScenarios() {
        String secret = UUID.randomUUID().toString();
        ScenarioSecrets.register(secret);
        ScenarioSecrets.clear();
        assertThat(ScenarioSecrets.containsSecret(secret)).isFalse();
    }

    @Test
    void longerSecretsAreRedactedBeforeTheirPrefixes() {
        String secret = UUID.randomUUID().toString();
        ScenarioSecrets.register(secret.substring(0, 8));
        ScenarioSecrets.register(secret);
        assertThat(ScenarioSecrets.redact(secret).equals("[REDACTADO]")).isTrue();
    }

    private void assertShape(String password, int length, boolean upper,
            boolean lower, boolean number, boolean special) {
        assertThat(password.length()).isEqualTo(length);
        assertThat(password.chars().anyMatch(Character::isUpperCase)).isEqualTo(upper);
        assertThat(password.chars().anyMatch(Character::isLowerCase)).isEqualTo(lower);
        assertThat(password.chars().anyMatch(Character::isDigit)).isEqualTo(number);
        assertThat(password.chars().anyMatch(c -> !Character.isLetterOrDigit(c)))
                .isEqualTo(special);
    }
}
