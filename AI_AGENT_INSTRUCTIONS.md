# Eclipse Ultimate MCP - AI Agent Instructions

> Import this file as project instructions for an AI coding agent connected to an Eclipse Ultimate MCP Server.

## Objective

Operate the live Eclipse workspace through Eclipse APIs. Prefer semantic project, JDT, m2e, EGit, launch, and debugger operations over raw filesystem or process manipulation. Keep changes scoped, observable, and reversible.

See [TOOLS.md](TOOLS.md) for the complete tool catalogue and exact input contracts.

## Core Rules

1. Use Eclipse Ultimate MCP tools first for anything inside the Eclipse workspace.
2. Use shell commands only when no MCP tool provides the capability, the MCP server is unavailable, or the target is outside the Eclipse workspace. State the reason before falling back.
3. Treat Eclipse project names and project-relative paths as the primary resource identity. Do not assume a workspace resource's physical location.
4. When the user says "this file" or "this class", call `get_currently_opened_file` before inspecting or editing.
5. When an absolute path is required, call `resolve_workspace_file`. This resolves linked files and files below linked folders to their real backing location.
6. A success message is not sufficient evidence for a write. Read the affected resource or configuration back and verify the intended state.
7. Preserve unrelated workspace, file, build-path, POM, launch, Git, and debug state.

## Discovery Workflow

Use the narrowest useful sequence:

1. `list_projects` to establish the available project names and open/closed state.
2. `get_project_details` for natures, builders, references, locations, source roots, and compiler settings.
3. `get_project_layout` or `find_files` to locate resources without scanning unrelated projects.
4. `get_source`, `get_class_outline`, `get_method_source`, and `find_references` for Java-aware inspection.
5. `read_project_resource` for exact text or non-Java files.
6. `get_project_dependencies` and Maven tools for classpath/dependency questions.

Do not infer a project name from a repository directory when Eclipse can report it directly.

## Editing Workflow

Before editing:

- Read the current file or Java element.
- Check `get_compilation_errors` when the change touches Java.
- Identify whether a semantic JDT operation exists.

Choose the highest-level applicable operation:

- Use JDT refactoring tools for Java type/package moves, renames, and method signature changes.
- Use `create_java_class` for structured class creation.
- Use Java build-path tools for source/test/output folder changes.
- Use m2e dependency tools for POM dependency changes.
- Use `replace_string` for a unique, exact local replacement.
- Use `insert_into_file` for insertion or replacement by a known line range.
- Use `apply_patch` for contextual multi-hunk edits to one file.
- Use `replace_file_content` only when replacing the complete file is intentional.
- Use plain resource move/rename tools only when Java-aware refactoring is not required.

After editing:

1. Read the changed resource back.
2. Run `organize_imports` and `format_code` for changed Java files where appropriate.
3. Recheck `get_compilation_errors` on affected files or projects.
4. Use `build_project` for a synchronous full or incremental Eclipse build when builder output matters.
5. Run focused tests before broader tests.
6. Inspect `git_diff` to confirm scope.

Use `clean_projects` only when a clean build is needed. Omit `projectNames` only when cleaning the entire workspace is intentional.

When direct changes are authorized, apply them directly. Use `show_proposed_changes` only when the user explicitly requests a preview or manual comparison.

Whenever a new file is created through Eclipse, open it immediately with `open_file`.

## Java and JDT

- Prefer `get_source` and JDT outlines over text search for type and member discovery.
- Use `find_references` before a behavioral refactor when callers determine the blast radius.
- `organize_imports`, `quick_fix`, and `clean_up` operate through Eclipse/JDT; read the file back after application.
- `configure_java_source_folder` must preserve other classpath entries. Test sources may require a separate output folder under the project's current JDT policy.
- `remove_java_source_folder` removes the build-path entry, not the folder contents.
- Do not place tests under `src/main/java`; use the project's configured test source root, normally `src/test/java`.

## Maven and m2e

- Use `get_maven_dependency_graph` for the dependency-mediated model rather than reconstructing it from POM text.
- Use `resolve_maven_dependency` to identify the selected artifact and a matching workspace project.
- Prefer `open_maven_dependency_project` when source is available in a closed workspace project. Avoid extracting large JARs just to inspect code.
- Use `configure_maven_dependency` and `remove_maven_dependency` for direct dependencies and `dependencyManagement` entries.
- Use `refresh_maven_project` after external POM edits or when the m2e model/classpath is stale.
- Read `pom.xml` after dependency edits and verify both semantics and formatting.

## Launch Configurations

- Call `list_launch_configurations`, then `get_launch_configuration`, before creating or changing a configuration.
- Preserve attribute types. Arrays and string-valued objects must remain arrays/maps, not serialized strings.
- `update_launch_configuration` changes only supplied attributes; pass `null` to remove one.
- Read the configuration back after every update. Treat a verification error as a failed write.
- Use `duplicate_launch_configuration` when a complete copy is required; verify type and attributes on the copy.
- Use `refresh_launch_configurations` only for configuration files changed outside Eclipse's launch APIs.

## Tests, Builds, and Updates

- Use `find_test_classes` before choosing a test scope.
- Prefer `run_class_tests` for a focused Java test and `run_all_tests` only when project-wide coverage is appropriate.
- Use the JUnit runner detected by Eclipse rather than assuming JUnit 3, 4, 5, or 6.
- Use saved launch configurations for established project builds when available.
- For this plugin repository, build only with Maven/Tycho. Do not depend on machine-local target platforms.
- After plugin code changes, run the complete Tycho update-site build and require `BUILD SUCCESS`.
- After publishing an update site, call `check_for_updates`. A plugin update requires an Eclipse restart before runtime testing.

## Git

- Start with `git_status` and inspect `git_diff` before staging.
- Stage only intended paths with `git_add`.
- Commit with a focused message after build/test verification.
- Do not reset, delete branches, stash, or check out over user changes without explicit authorization.
- Eclipse Ultimate currently provides local EGit/JGit operations but no push tool. Use the repository's approved external Git workflow only when pushing is requested.

## Debugging

- Call `list_active_launches` before addressing a launch by index.
- Call `get_stack_trace` before stepping or evaluating expressions.
- Use `evaluate_expression` only against a suspended frame and the correct Java project.
- Breakpoint, resume, step, stop, and expression tools mutate live debug/application state. Keep those actions aligned with the user's explicit debugging goal.

## Failure and Staleness Handling

- On an edit failure, reread the resource before retrying; the editor buffer may differ from disk.
- If a tool reports success but readback differs, report the mismatch and investigate before making further dependent changes.
- For launch configuration files changed externally, call `refresh_launch_configurations` and then read them again.
- For POM files changed externally, call `refresh_maven_project` and inspect the effective dependency graph.
- Keep temporary probes isolated, document their names, and remove them after verification.
- Never overwrite or revert unrelated user changes.

## Security

- Treat generated bearer tokens in `.mcp.json` and `.codex/config.toml` as secrets.
- Keep the server bound to loopback unless remote exposure is deliberate.
- Require appropriate approval for file deletion, Git mutation, launches, debugger state changes, and update installation.
- Do not expose credentials from launch attributes, Mylyn repositories, environment variables, or generated MCP configuration.

## Completion Checklist

A task is complete only when:

- the requested behavior is implemented;
- affected resources were read back;
- Java diagnostics are clean enough for the project baseline;
- focused tests and the required build succeeded;
- the Git diff contains only intended changes;
- temporary probes are removed;
- update checks and restart-dependent runtime tests were performed when plugin code changed.
