package data;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public final class ScenarioSecrets {
    private static final ThreadLocal<Set<String>> VALUES = ThreadLocal.withInitial(HashSet::new);

    private ScenarioSecrets() { }

    public static void register(String value) {
        if (value != null && !value.isEmpty()) {
            VALUES.get().add(value);
        }
    }

    public static String redact(String text) {
        if (text == null) {
            return null;
        }
        String result = text;
        for (String value : VALUES.get().stream()
                .sorted(Comparator.comparingInt(String::length).reversed()).toList()) {
            result = result.replace(value, "[REDACTADO]");
        }
        return result;
    }

    public static boolean containsSecret(String text) {
        return text != null && VALUES.get().stream().anyMatch(text::contains);
    }

    public static boolean isSecretValue(String value) {
        return VALUES.get().contains(value);
    }

    public static void clear() {
        VALUES.remove();
    }
}
