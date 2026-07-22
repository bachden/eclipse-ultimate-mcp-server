package nhb.eclipse.ultimate.mcpserver.tools.mylyn;

import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.google.gson.JsonObject;

import nhb.eclipse.ultimate.mcpserver.tools.Schemas;

/** Optional name matching shared by Mylyn list tools. */
final class MylynNameFilter {

    private final String ilike;
    private final Pattern regex;

    private MylynNameFilter(String ilike, Pattern regex) {
        this.ilike = ilike;
        this.regex = regex;
    }

    static void addSchema(JsonObject schema, String subject) {
        Schemas.prop(schema, "nameIlike", "string",
                "Optional case-insensitive substring matched against the " + subject + " name");
        Schemas.prop(schema, "nameRegex", "string", "Optional Java regular expression searched within the " + subject
                + " name; mutually exclusive with nameIlike");
    }

    static MylynNameFilter from(JsonObject arguments) {
        String ilike = Schemas.optString(arguments, "nameIlike", "").trim();
        String regexText = Schemas.optString(arguments, "nameRegex", "").trim();
        if (!ilike.isEmpty() && !regexText.isEmpty()) {
            throw new IllegalArgumentException("nameIlike and nameRegex are mutually exclusive");
        }

        Pattern regex = null;
        if (!regexText.isEmpty()) {
            try {
                regex = Pattern.compile(regexText);
            } catch (PatternSyntaxException e) {
                throw new IllegalArgumentException("Invalid nameRegex: " + e.getDescription(), e);
            }
        }
        return new MylynNameFilter(ilike.toLowerCase(Locale.ROOT), regex);
    }

    boolean matches(String name) {
        String value = name == null ? "" : name;
        if (!ilike.isEmpty()) {
            return value.toLowerCase(Locale.ROOT).contains(ilike);
        }
        return regex == null || regex.matcher(value).find();
    }
}
