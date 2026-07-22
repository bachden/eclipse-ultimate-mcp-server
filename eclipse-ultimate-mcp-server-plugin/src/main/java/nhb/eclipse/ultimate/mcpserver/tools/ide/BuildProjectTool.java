package nhb.eclipse.ultimate.mcpserver.tools.ide;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import nhb.eclipse.ultimate.mcpserver.mcp.McpTool;
import nhb.eclipse.ultimate.mcpserver.tools.Schemas;

/** Runs an Eclipse full or incremental build for one workspace project. */
public class BuildProjectTool implements McpTool {

    @Override
    public String name() {
        return "build_project";
    }

    @Override
    public String description() {
        return "Build one open Eclipse project and wait for its configured builders to finish. "
                + "Supports incremental (default) and full builds, and returns problem marker counts.";
    }

    @Override
    public JsonObject inputSchema() {
        JsonObject schema = Schemas.object();
        Schemas.prop(schema, "projectName", "string", "The open Eclipse project to build");
        Schemas.prop(schema, "buildKind", "string", "Build kind: incremental (default) or full");
        return Schemas.required(schema, "projectName");
    }

    @Override
    public String execute(JsonObject arguments) throws Exception {
        String projectName = Schemas.requireString(arguments, "projectName").trim();
        String buildKind = Schemas.optString(arguments, "buildKind", "incremental").trim().toLowerCase();
        int kind = switch (buildKind) {
        case "incremental" -> IncrementalProjectBuilder.INCREMENTAL_BUILD;
        case "full" -> IncrementalProjectBuilder.FULL_BUILD;
        default -> throw new IllegalArgumentException("buildKind must be incremental or full");
        };

        IProject project = Workspace.project(projectName);
        if (!project.isAccessible()) {
            throw new IllegalArgumentException("Project is not open and accessible: " + projectName);
        }

        long started = System.nanoTime();
        project.build(kind, new NullProgressMonitor());
        long durationMillis = (System.nanoTime() - started) / 1_000_000;

        int errors = 0;
        int warnings = 0;
        int infos = 0;
        for (IMarker marker : project.findMarkers(IMarker.PROBLEM, true, IProject.DEPTH_INFINITE)) {
            switch (marker.getAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO)) {
            case IMarker.SEVERITY_ERROR -> errors++;
            case IMarker.SEVERITY_WARNING -> warnings++;
            default -> infos++;
            }
        }

        JsonObject markers = new JsonObject();
        markers.addProperty("errors", errors);
        markers.addProperty("warnings", warnings);
        markers.addProperty("infos", infos);

        JsonObject result = new JsonObject();
        result.addProperty("projectName", projectName);
        result.addProperty("buildKind", buildKind);
        result.addProperty("durationMillis", durationMillis);
        result.addProperty("completed", true);
        result.add("problemMarkers", markers);
        return new GsonBuilder().setPrettyPrinting().create().toJson(result);
    }
}
