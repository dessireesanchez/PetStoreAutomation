package data;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class PasswordData {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!+@";
    private static final List<String> ALL = List.of(UPPER, LOWER, DIGITS, SPECIAL);

    private PasswordData() { }

    public static String valid(int length) {
        if (length < 8 || length > 20) {
            throw new IllegalArgumentException("La contraseña válida de prueba debe tener 8 a 20 caracteres");
        }
        return generate(length, ALL);
    }

    public static String invalid(String rule) {
        return switch (rule) {
            case "longitud 7" -> generate(7, ALL);
            case "longitud 21" -> generate(21, ALL);
            case "sin mayúscula" -> generate(12, List.of(LOWER, DIGITS, SPECIAL));
            case "sin minúscula" -> generate(12, List.of(UPPER, DIGITS, SPECIAL));
            case "sin número" -> generate(12, List.of(UPPER, LOWER, SPECIAL));
            case "sin carácter especial" -> generate(12, List.of(UPPER, LOWER, DIGITS));
            default -> throw new IllegalArgumentException("Regla de contraseña desconocida: " + rule);
        };
    }

    public static String differentFrom(String original) {
        String candidate;
        do {
            candidate = valid(12);
        } while (candidate.equals(original));
        return candidate;
    }

    private static String generate(int length, List<String> groups) {
        List<Character> characters = new ArrayList<>();
        String alphabet = String.join("", groups);
        for (String group : groups) {
            characters.add(group.charAt(RANDOM.nextInt(group.length())));
        }
        while (characters.size() < length) {
            characters.add(alphabet.charAt(RANDOM.nextInt(alphabet.length())));
        }
        Collections.shuffle(characters, RANDOM);
        StringBuilder password = new StringBuilder();
        characters.forEach(password::append);
        String generated = password.toString();
        ScenarioSecrets.register(generated);
        return generated;
    }
}
