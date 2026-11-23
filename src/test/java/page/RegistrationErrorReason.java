package page;

import java.text.Normalizer;
import java.util.Locale;

public final class RegistrationErrorReason {
    private RegistrationErrorReason() { }

    public static boolean matches(String message, String reason) {
        String text = Normalizer.normalize(message, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "").toLowerCase(Locale.ROOT);
        boolean password = any(text, "password", "contrasena", "clave");
        boolean duplicate = any(text, "already", "taken", "in use", "exists", "duplicate",
                "ya ", "duplicad", "registrad", "existe", "en uso")
                && !any(text, "not registered", "not found", "no existe", "no esta registrado");
        return switch (reason) {
            case "User ID duplicado" -> duplicate && any(text, "user id", "userid", "username", "usuario");
            case "Email duplicado" -> duplicate && any(text, "email", "e-mail", "correo");
            case "confirmación distinta" -> password && any(text, "not match", "mismatch", "must match",
                    "different", "no coincid", "deben coincidir", "distint", "diferent", "no son igual");
            case "longitud 7" -> password && lengthMessage(text, "8");
            case "longitud 21" -> password && lengthMessage(text, "20");
            case "sin mayúscula" -> password && any(text, "uppercase", "upper case", "mayuscul");
            case "sin minúscula" -> password && any(text, "lowercase", "lower case", "minuscul");
            case "sin número" -> password && any(text, "digit", "number", "numero");
            case "sin carácter especial" -> password && any(text, "special", "especial", "symbol", "simbol");
            default -> throw new IllegalArgumentException("Motivo desconocido: " + reason);
        };
    }

    private static boolean lengthMessage(String text, String limit) {
        return text.matches("(?s).*\\b" + limit + "\\b.*")
                && any(text, "length", "longitud", "character", "caracter", "least", "most", "min", "max");
    }

    private static boolean any(String text, String... fragments) {
        for (String fragment : fragments) {
            if (text.contains(fragment)) {
                return true;
            }
        }
        return false;
    }
}
