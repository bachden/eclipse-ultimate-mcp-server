package nhb.eclipse.ultimate.mcpserver.tools.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import com.google.gson.JsonObject;

import nhb.eclipse.ultimate.mcpserver.mcp.McpTool;
import nhb.eclipse.ultimate.mcpserver.tools.Schemas;

/**
 * Updates selected attributes on an existing saved launch configuration.
 */
public class UpdateLaunchConfigurationTool implements McpTool {

    @Override
    public String name() {
        return "update_launch_configuration";
    }

    @Override
    public String description() {
        return "Update attributes on an existing saved launch configuration by name. Only the given attribute "
                + "keys are changed; everything else is preserved. Pass null to remove an attribute. Primitive "
                + "values, arrays and string-valued objects are persisted with their Eclipse launch attribute types.";
    }

    @Override
    public JsonObject inputSchema() {
        JsonObject schema = Schemas.object();
        Schemas.prop(schema, "name", "string", "Exact name of the launch configuration to update");
        JsonObject attributes = new JsonObject();
        attributes.addProperty("type", "object");
        attributes.addProperty("description",
                "Attribute key -> value map to merge in. Values may be strings, integers, booleans, arrays of "
                        + "primitive values, string-valued objects, or null to remove that attribute.");
        schema.getAsJsonObject("properties").add("attributes", attributes);
        return Schemas.required(schema, "name", "attributes");
    }

    @Override
    public String execute(JsonObject arguments) throws Exception {
        String name = Schemas.requireString(arguments, "name");
        if (!arguments.has("attributes") || !arguments.get("attributes").isJsonObject()) {
            throw new IllegalArgumentException("Missing required argument: attributes");
        }
        JsonObject attributes = arguments.getAsJsonObject("attributes");

        ILaunchConfiguration config = LaunchConfigurationLookup.find(name);
        ILaunchConfigurationWorkingCopy wc = config.getWorkingCopy();
        Map<String, Object> expected = LaunchAttributes.apply(wc, attributes);
        ILaunchConfiguration saved = wc.doSave();
        verifyPersisted(saved, expected);
        return "Updated and verified launch configuration \"" + saved.getName() + "\" (" + expected.size()
                + " attribute(s))";
    }

    private static void verifyPersisted(ILaunchConfiguration saved, Map<String, Object> expected) throws Exception {
        Map<String, Object> persisted = LaunchConfigurationLookup.find(saved.getName()).getAttributes();
        List<String> mismatches = new ArrayList<>();
        for (Map.Entry<String, Object> entry : expected.entrySet()) {
            String key = entry.getKey();
            Object expectedValue = entry.getValue();
            if (expectedValue == null) {
                if (persisted.containsKey(key)) {
                    mismatches.add(key + " was not removed");
                }
            } else if (!expectedValue.equals(persisted.get(key))) {
                mismatches.add(key + " expected " + expectedValue + " but found " + persisted.get(key));
            }
        }
        if (!mismatches.isEmpty()) {
            throw new IllegalStateException(
                    "Launch configuration save verification failed: " + String.join("; ", mismatches));
        }
    }
}
