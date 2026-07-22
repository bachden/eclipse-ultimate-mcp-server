package nhb.eclipse.ultimate.mcpserver.tools.ide;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import nhb.eclipse.ultimate.mcpserver.mcp.McpTool;
import nhb.eclipse.ultimate.mcpserver.tools.Schemas;

/** Cleans selected Eclipse projects, or every open project in the workspace. */
public class CleanProjectsTool implements McpTool {

    @Override
    public String name() {
        return "clean_projects";
    }

    @Override
    public String description() {
        return "Clean configured builders for selected open Eclipse projects and wait for completion. "
                + "Omit projectNames to clean every open project in the workspace.";
    }

    @Override
    public JsonObject inputSchema() {
        JsonObject schema = Schemas.object();
        JsonObject projectNames = new JsonObject();
        projectNames.addProperty("type", "array");
        projectNames.addProperty("description",
                "Optional project names to clean; omit to clean every open project in the workspace");
        JsonObject items = new JsonObject();
        items.addProperty("type", "string");
        projectNames.add("items", items);
        schema.getAsJsonObject("properties").add("projectNames", projectNames);
        return schema;
    }

    @Override
    public String execute(JsonObject arguments) throws Exception {
        List<IProject> projects = selectedProjects(arguments);
        long started = System.nanoTime();
        NullProgressMonitor monitor = new NullProgressMonitor();
        for (IProject project : projects) {
            project.build(IncrementalProjectBuilder.CLEAN_BUILD, monitor);
        }
        long durationMillis = (System.nanoTime() - started) / 1_000_000;

        JsonArray names = new JsonArray();
        for (IProject project : projects) {
            names.add(project.getName());
        }

        JsonObject result = new JsonObject();
        result.addProperty("scope", hasProjectNames(arguments) ? "selected" : "workspace");
        result.addProperty("projectCount", projects.size());
        result.add("projects", names);
        result.addProperty("durationMillis", durationMillis);
        result.addProperty("completed", true);
        return new GsonBuilder().setPrettyPrinting().create().toJson(result);
    }

    private List<IProject> selectedProjects(JsonObject arguments) {
        if (!hasProjectNames(arguments)) {
            List<IProject> projects = new ArrayList<>();
            for (IProject project : Workspace.root().getProjects()) {
                if (project.isAccessible()) {
                    projects.add(project);
                }
            }
            return projects;
        }

        JsonElement value = arguments.get("projectNames");
        if (!value.isJsonArray()) {
            throw new IllegalArgumentException("projectNames must be an array of project names");
        }

        Set<String> names = new LinkedHashSet<>();
        for (JsonElement element : value.getAsJsonArray()) {
            if (!element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
                throw new IllegalArgumentException("projectNames must contain only strings");
            }
            String name = element.getAsString().trim();
            if (name.isEmpty()) {
                throw new IllegalArgumentException("projectNames must not contain blank names");
            }
            names.add(name);
        }
        if (names.isEmpty()) {
            throw new IllegalArgumentException("projectNames must not be empty; omit it to clean the workspace");
        }

        List<IProject> projects = new ArrayList<>();
        for (String name : names) {
            IProject project = Workspace.project(name);
            if (!project.isAccessible()) {
                throw new IllegalArgumentException("Project is not open and accessible: " + name);
            }
            projects.add(project);
        }
        return projects;
    }

    private boolean hasProjectNames(JsonObject arguments) {
        return arguments != null && arguments.has("projectNames") && !arguments.get("projectNames").isJsonNull();
    }
}
