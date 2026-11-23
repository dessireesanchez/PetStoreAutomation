package data;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public record RegistrationData(Map<String, String> values) {

    public RegistrationData {
        values = Collections.unmodifiableMap(new LinkedHashMap<>(values));
    }

    public static RegistrationData valid() {
        String userId = "U" + RandomStringUtils.randomAlphanumeric(9);
        String password = PasswordData.valid(12);
        Map<String, String> fields = new LinkedHashMap<>();
        fields.put("User ID", userId);
        fields.put("New password", password);
        fields.put("Repeat password", password);
        fields.put("First name", "Carlos");
        fields.put("Last name", "Tester");
        fields.put("Email", "qa_user_" + System.currentTimeMillis()
                + "_" + userId + "@testpetstore.com");
        fields.put("Phone", "+123456789");
        fields.put("Address 1", "Av. Principal 123");
        fields.put("City", "Bogotá");
        fields.put("State", "Cundinamarca");
        fields.put("Zip", "110111");
        fields.put("Country", "Colombia");
        return new RegistrationData(fields);
    }

    public RegistrationData with(String field, String value) {
        if (!values.containsKey(field)) {
            throw new IllegalArgumentException("Campo de datos desconocido: " + field);
        }
        Map<String, String> changed = new LinkedHashMap<>(values);
        changed.put(field, Objects.requireNonNull(value));
        return new RegistrationData(changed);
    }

    public String value(String field) {
        return Objects.requireNonNull(values.get(field), "Falta el campo: " + field);
    }

    @Override
    public String toString() {
        return "RegistrationData[User ID=" + value("User ID")
                + ", Email=" + value("Email") + ", passwords=[REDACTADO]]";
    }
}
