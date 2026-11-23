package data;

import org.apache.commons.lang3.RandomStringUtils;

public final class RegistrationValueResolver {
    private final String timestamp = Long.toString(System.currentTimeMillis());
    private String password;

    public String resolve(String field, String value) {
        if ("User ID".equals(field) && "U + 9 caracteres alfanuméricos generados".equals(value)) {
            return "U" + RandomStringUtils.randomAlphanumeric(9);
        }
        if ("Email".equals(field)) {
            return value.replace("{timestamp}", timestamp);
        }
        if (("New password".equals(field) || "Repeat password".equals(field))
                && "contraseña generada del escenario".equals(value)) {
            if (password == null) {
                password = PasswordData.valid(12);
            }
            return password;
        }
        return value;
    }
}
