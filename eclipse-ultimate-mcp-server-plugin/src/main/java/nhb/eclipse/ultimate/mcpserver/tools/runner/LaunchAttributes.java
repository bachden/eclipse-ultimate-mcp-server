package nhb.eclipse.ultimate.mcpserver.tools.runner;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Applies arbitrary JSON attributes onto a launch configuration working copy.
 */
final class LaunchAttributes {

    private LaunchAttributes() {
    }

    static Map<String, Object> apply(ILaunchConfigurationWorkingCopy wc, JsonObject attributes) {
        Map<String, Object> applied = new LinkedHashMap<>();
        for (Map.Entry<String, JsonElement> entry : attributes.entrySet()) {
            String key = entry.getKey();
            Object value = toAttributeValue(key, entry.getValue());
            applied.put(key, value);
            if (value == null) {
                wc.removeAttribute(key);
            } else if (value instanceof Boolean booleanValue) {
                wc.setAttribute(key, booleanValue);
            } else if (value instanceof Integer integerValue) {
                wc.setAttribute(key, integerValue);
            } else if (value instanceof String stringValue) {
                wc.setAttribute(key, stringValue);
            } else if (value instanceof List<?> listValue) {
                @SuppressWarnings("unchecked")
                List<String> strings = (List<String>) listValue;
                wc.setAttribute(key, strings);
            } else if (value instanceof Map<?, ?> mapValue) {
                @SuppressWarnings("unchecked")
                Map<String, String> strings = (Map<String, String>) mapValue;
                wc.setAttribute(key, strings);
            }
        }
        return applied;
    }

    private static Object toAttributeValue(String key, JsonElement value) {
        if (value == null || value.isJsonNull()) {
            return null;
        }
        if (value.isJsonPrimitive()) {
            JsonPrimitive primitive = value.getAsJsonPrimitive();
            if (primitive.isBoolean()) {
                return primitive.getAsBoolean();
            }
            if (primitive.isNumber()) {
                return primitive.getAsInt();
            }
            return primitive.getAsString();
        }
        if (value.isJsonArray()) {
            List<String> list = new ArrayList<>();
            for (JsonElement element : value.getAsJsonArray()) {
                if (!element.isJsonPrimitive()) {
                    throw unsupported(key, "array items must be primitive values");
                }
                list.add(element.getAsString());
            }
            return list;
        }
        if (value.isJsonObject()) {
            Map<String, String> map = new LinkedHashMap<>();
            for (Map.Entry<String, JsonElement> item : value.getAsJsonObject().entrySet()) {
                if (item.getValue() == null || !item.getValue().isJsonPrimitive()) {
                    throw unsupported(key, "object values must be primitive values");
                }
                map.put(item.getKey(), item.getValue().getAsString());
            }
            return map;
        }
        throw unsupported(key, "unsupported JSON value");
    }

    private static IllegalArgumentException unsupported(String key, String detail) {
        return new IllegalArgumentException("Invalid launch attribute '" + key + "': " + detail);
    }
}
