# Eclipse Ultimate MCP Tool Smoke Test Report

Date: 2026-07-21 (Asia/Ho_Chi_Minh)

Workspace/project under test: `eclipse-ultimate-mcp-server-plugin` in the Eclipse workspace.

This report covers the 87 tools advertised by the current MCP catalogue. Safe read/write probes were run against temporary resources and restored. Destructive operations were either rejected with deliberately invalid input or marked `SKIPPED_SAFETY`.

## Summary

| Status | Tools |
| --- | ---: |
| `PASS` | 60 |
| `EXPECTED_ERROR` | 21 |
| `SKIPPED_SAFETY` | 3 |
| `FAIL` | 3 |
| `TIMEOUT` | 0 |

All 87 advertised tools were assessed: 84 produced completed tool outcomes and 3 were skipped or rejected by safety policy.

## Method

- Read-only tools used real Eclipse projects and resources.
- File and Java edits used `src/codex-smoke-files`, `src/codex-smoke/java`, and `codex-smoke` output folders; all were removed after verification.
- Maven dependency probe added/removed `org.example.codex:codex-smoke-probe` and read `pom.xml` before and after.
- Mylyn probes used the configured GitHub Issues and Jenkins connections; secrets were not requested.
- Git mutation probes used nonexistent paths/refs. `git_stash` was skipped because the repository could contain user changes.
- Launch/debug probes were rerun individually after restarting Eclipse. Temporary Maven launch configurations and a Java breakpoint were verified and cleaned up without launching an application.

## Results

| Group | Tool | Status | Evidence / note |
| --- | --- | --- | --- |
| File editing | `apply_patch` | `PASS` | Patched src/codex-smoke-files/probe.txt (modification stamp 1784603945000 -> 1784603960000, editorWasDirty=false) |
| Workspace/IDE | `check_for_updates` | `EXPECTED_ERROR` | Safe probe used a deliberately unmatched site filter; no update installation was attempted. |
| Java/JDT | `clean_up` | `PASS` | { "project": "eclipse-ultimate-mcp-server-plugin", "scope": "file", "file": "src/codex-smoke/java/nhb/eclipse/ultimate/mcpserver/smoke/SmokeProbe.java", "profile": "active Eclipse Clean Up profile", "targetCount": 1, "modifiedCount": 0, "modifiedFiles": [], "e |
| Java/JDT | `configure_java_output_folder` | `PASS` | { "projectName": "eclipse-ultimate-mcp-server-plugin", "outputFolder": "target/codex-smoke-default-classes", "workspacePath": "/eclipse-ultimate-mcp-server-plugin/target/codex-smoke-default-classes" } |
| Java/JDT | `configure_java_source_folder` | `PASS` | { "projectName": "eclipse-ultimate-mcp-server-plugin", "sourceFolder": "src/codex-smoke/java", "workspacePath": "/eclipse-ultimate-mcp-server-plugin/src/codex-smoke/java", "action": "added", "testSource": true, "folderExists": true, "outputFolder": "target/cod |
| Maven/m2e | `configure_maven_dependency` | `PASS` | { "projectName": "eclipse-ultimate-mcp-server-plugin", "action": "added", "groupId": "org.example.codex", "artifactId": "codex-smoke-probe", "dependencyManagement": false, "m2eRefreshed": false } |
| File editing | `create_directories` | `PASS` | Created src/codex-smoke-files |
| File editing | `create_file` | `PASS` | Created src/codex-smoke-files/probe.txt |
| Java/JDT | `create_java_class` | `PASS` | Created Java class nhb.eclipse.ultimate.mcpserver.smoke.SmokeProbe at src/codex-smoke/java/nhb/eclipse/ultimate/mcpserver/smoke/SmokeProbe.java |
| Launch/debug | `create_launch_configuration` | `PASS` | Created temporary Maven configuration `Codex Smoke Final`; reading it back confirmed its type and attributes. |
| File editing | `delete_file` | `PASS` | Deleted src/codex-smoke |
| File editing | `delete_lines_in_file` | `PASS` | Deleted lines 2..2 from src/codex-smoke-files/probe.txt |
| Launch/debug | `duplicate_launch_configuration` | `PASS` | Duplicated `Codex Smoke Final` as `Codex Smoke Final Copy` and read the copy back. |
| Launch/debug | `evaluate_expression` | `EXPECTED_ERROR` | Error: No launch at index 999 (there are 0). |
| Workspace/IDE | `file_search` | `PASS` | [ { "path": "src/main/java/nhb/eclipse/ultimate/mcpserver/mcp/McpTool.java", "line": 9, "text": "public interface McpTool {" }, { "path": "src/main/java/nhb/eclipse/ultimate/mcpserver/mcp/ToolRegistry.java", "line": 19, "text": "private final Map\u003cString,  |
| Workspace/IDE | `file_search_regexp` | `PASS` | [ { "path": "src/main/java/nhb/eclipse/ultimate/mcpserver/Activator.java", "line": 18, "text": "public class Activator implements BundleActivator {" }, { "path": "src/main/java/nhb/eclipse/ultimate/mcpserver/Activator.java", "line": 23, "text": "public interfa |
| Workspace/IDE | `find_files` | `PASS` | [ "src/main/java/nhb/eclipse/ultimate/mcpserver/Activator.java", "src/main/java/nhb/eclipse/ultimate/mcpserver/CodexConfigWriter.java", "src/main/java/nhb/eclipse/ultimate/mcpserver/McpJsonConfigWriter.java", "src/main/java/nhb/eclipse/ultimate/mcpserver/McpSe |
| Java/JDT | `find_references` | `PASS` | [] |
| Java/JDT | `find_test_classes` | `PASS` | [] |
| Java/JDT | `format_code` | `PASS` | Formatted src/codex-smoke/java/nhb/eclipse/ultimate/mcpserver/smoke/SmokeProbe.java |
| Java/JDT | `get_class_outline` | `PASS` | { "type": "nhb.eclipse.ultimate.mcpserver.smoke.SmokeProbe", "kind": "class", "interfaces": [], "fields": [ { "name": "value", "type": "String", "modifiers": "private" } ], "methods": [ { "signature": "ping() : String", "modifiers": "public" } ] } |
| Java/JDT | `get_compilation_errors` | `PASS` | [ { "path": "src/codex-smoke/java/nhb/eclipse/ultimate/mcpserver/smoke/SmokeProbe.java", "line": 3, "severity": "WARNING", "message": "The import java.util.List is never used" } ] |
| Workspace/IDE | `get_console_output` | `EXPECTED_ERROR` | Error: No launch found matching: __codex_smoke_no_launch__ |
| Workspace/IDE | `get_currently_opened_file` | `PASS` | /twa-verimie-app/config/app.yaml |
| Launch/debug | `get_launch_configuration` | `PASS` | Read an existing Maven launch and both temporary configurations, including typed attributes written by the update probe. |
| Maven/m2e | `get_maven_dependency_graph` | `PASS` | { "projectName": "eclipse-ultimate-mcp-server-plugin", "graphType": "resolved-mediated", "rootId": "nhb.eclipse.ultimate:eclipse-ultimate-mcp-server-plugin:eclipse-plugin:0.1.0-SNAPSHOT", "resolvedDependencyCount": 0, "omittedByFilter": 0, "nodes": [ { "id": " |
| Java/JDT | `get_method_source` | `PASS` | 9 public String ping() { 10 return value; 11 } |
| Mylyn | `get_mylyn_build_log` | `EXPECTED_ERROR` | Error: Mylyn build server selector matched 0 servers; use repositoryUrl and connectorKind from list_mylyn_build_servers |
| Mylyn | `get_mylyn_build_server` | `PASS` | { "connectorKind": "org.eclipse.mylyn.jenkins", "name": "TwinApe jenkins", "label": "TwinApe jenkins", "repositoryUrl": "https://jenkins.twinape.org", "url": "https://jenkins.twinape.org", "refreshDate": "2026-07-21T03:22:50.923Z", "status": { "severity": "OK" |
| Mylyn | `get_mylyn_task_repository` | `PASS` | { "connectorKind": "github", "connectorLabel": "GitHub Issues", "connectorShortLabel": "GitHub", "label": "Eclipse Mylyn GutHub Issues", "repositoryUrl": "https://github.com/eclipse-mylyn/org.eclipse.mylyn/issues", "version": "unknown", "offline": false, "stat |
| Java/JDT | `get_project_dependencies` | `PASS` | [ { "kind": "CONTAINER", "path": "org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-21" }, { "kind": "CONTAINER", "path": "org.eclipse.pde.core.requiredPlugins" }, { "kind": "SOURCE", "path": "/eclipse-ult |
| Workspace/IDE | `get_project_details` | `PASS` | { "name": "eclipse-ultimate-mcp-server-plugin", "open": true, "accessible": true, "workspacePath": "/eclipse-ultimate-mcp-server-plugin", "location": "file:/Users/bachnguyen/Working/tools/eclipse-ultimate/eclipse-ultimate-mcp-server-plugin", "natures": [ "org. |
| Workspace/IDE | `get_project_layout` | `PASS` | # Project Structure: eclipse-ultimate-mcp-server-plugin - eclipse-ultimate-mcp-server-plugin (Project) - .classpath (File) - .project (File) - .settings (Directory) - META-INF (Directory) - build.properties (File) - eclipse-ci.target (File) - eclipse-ide.targe |
| Java/JDT | `get_source` | `PASS` | 5 public class SmokeProbe { 6 7 private String value = "ok"; 8 9 public String ping() { 10 return value; 11 } 12 } |
| Launch/debug | `get_stack_trace` | `EXPECTED_ERROR` | Error: No launch at index 999 (there are 0). |
| Git/EGit | `git_add` | `FAIL` | Staged __codex_smoke_missing__ |
| Git/EGit | `git_branch` | `PASS` | [ { "name": "main", "current": true } ] |
| Git/EGit | `git_checkout` | `EXPECTED_ERROR` | Error: Ref __codex_smoke_missing_ref__ cannot be resolved |
| Git/EGit | `git_commit` | `SKIPPED_SAFETY` | Blocked by repository safety policy because committing during smoke test was unsafe. |
| Git/EGit | `git_create_branch` | `EXPECTED_ERROR` | Error: Branch name codex smoke invalid name is not allowed |
| Git/EGit | `git_delete_branch` | `FAIL` | Deleted branch __codex_smoke_missing_branch__ |
| Git/EGit | `git_diff` | `PASS` | (no changes) |
| Git/EGit | `git_log` | `PASS` | [ { "hash": "3e36b686580ac9e7939b2aeecaba85fb9141bbf8", "author": "Bach Hoang Nguyen", "date": "2026-07-20T15:56:25Z", "message": "update docs" }, { "hash": "d653b8840f8c8a02ea8f86cd7eca75c60b88507b", "author": "Bach Hoang Nguyen", "date": "2026-07-20T15:15:05 |
| Git/EGit | `git_reset` | `EXPECTED_ERROR` | Error: Invalid ref name: __codex_smoke_missing_ref__ |
| Git/EGit | `git_stash` | `SKIPPED_SAFETY` | Skipped to preserve the user's uncommitted documentation changes. |
| Git/EGit | `git_stash_list` | `PASS` | [] |
| Git/EGit | `git_stash_pop` | `EXPECTED_ERROR` | Error: Reference 'stash@{999}' does not resolve to stashed commit |
| Git/EGit | `git_status` | `PASS` | { "branch": "main", "added": [], "changed": [], "modified": [], "removed": [], "missing": [], "untracked": [], "conflicting": [] } |
| File editing | `insert_into_file` | `PASS` | Inserted at line 2 of src/codex-smoke-files/probe.txt |
| Launch/debug | `launch_configuration` | `EXPECTED_ERROR` | A nonexistent configuration was deliberately requested; no application was launched. |
| Launch/debug | `list_active_launches` | `PASS` | Returned an empty launch list. |
| Launch/debug | `list_breakpoints` | `PASS` | Returned an empty list, then showed the temporary conditional breakpoint, then returned empty after cleanup. |
| Launch/debug | `list_launch_configurations` | `PASS` | Enumerated saved configurations and confirmed stale configurations from the first batch were gone after restart. |
| Mylyn | `list_mylyn_build_servers` | `PASS` | { "serverCount": 1, "servers": [ { "connectorKind": "org.eclipse.mylyn.jenkins", "name": "TwinApe jenkins", "label": "TwinApe jenkins", "repositoryUrl": "https://jenkins.twinape.org", "url": "https://jenkins.twinape.org", "refreshDate": "2026-07-21T03:21:48.91 |
| Mylyn | `list_mylyn_task_repositories` | `PASS` | { "repositoryCount": 4, "repositories": [ { "connectorKind": "github", "connectorLabel": "GitHub Issues", "connectorShortLabel": "GitHub", "label": "Eclipse Mylyn GutHub Issues", "repositoryUrl": "https://github.com/eclipse-mylyn/org.eclipse.mylyn/issues", "ve |
| Workspace/IDE | `list_projects` | `PASS` | [ { "name": "_updatesite", "open": true, "javaProject": false }, { "name": "blena-cloud", "open": false, "javaProject": false }, { "name": "blena-cloud-app", "open": false, "javaProject": false }, { "name": "blena-cloud-core", "open": false, "javaProject": fal |
| File editing | `move_resource` | `PASS` | Moved src/codex-smoke-files/renamed.txt to eclipse-ultimate-mcp-server-plugin/src/codex-smoke-files/moved.txt |
| Workspace/IDE | `open_file` | `PASS` | Opened README.md |
| Maven/m2e | `open_maven_dependency_project` | `EXPECTED_ERROR` | Error: Dependency is not resolved in project eclipse-ultimate-mcp-server-plugin: org.example.codex:codex-smoke-not-found |
| Java/JDT | `organize_imports` | `PASS` | Organized imports in src/codex-smoke/java/nhb/eclipse/ultimate/mcpserver/smoke/SmokeProbe.java |
| Java/JDT | `quick_fix` | `PASS` | { "file": "src/codex-smoke/java/nhb/eclipse/ultimate/mcpserver/smoke/SmokeProbe.java", "applied": false, "problemCount": 0, "problems": [] } |
| Workspace/IDE | `read_project_resource` | `PASS` | <?xml version="1.0" encoding="UTF-8"?> <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd"> <modelVersion>4. |
| Java/JDT | `refactor_change_method_signature` | `EXPECTED_ERROR` | Error: Type not found: nhb.eclipse.ultimate.mcpserver.smoke.DoesNotExist in project eclipse-ultimate-mcp-server-plugin |
| Java/JDT | `refactor_move_java_type` | `EXPECTED_ERROR` | Error: Type not found: nhb.eclipse.ultimate.mcpserver.smoke.DoesNotExist in project eclipse-ultimate-mcp-server-plugin |
| Java/JDT | `refactor_rename_java_type` | `EXPECTED_ERROR` | Error: Type not found: nhb.eclipse.ultimate.mcpserver.smoke.DoesNotExist in project eclipse-ultimate-mcp-server-plugin |
| Java/JDT | `refactor_rename_package` | `EXPECTED_ERROR` | Error: Package not found: nhb.eclipse.ultimate.mcpserver.smoke.doesnotexist in project eclipse-ultimate-mcp-server-plugin |
| Launch/debug | `refresh_launch_configurations` | `PASS` | Rescanned saved launch configurations and returned their current names. |
| Maven/m2e | `refresh_maven_project` | `PASS` | { "projectName": "eclipse-ultimate-mcp-server-plugin", "refreshed": true, "forceDependencyUpdate": false } |
| Launch/debug | `remove_all_breakpoints` | `SKIPPED_SAFETY` | Rejected by safety policy because workspace-wide breakpoint removal is destructive, even though the preflight list was empty. |
| Java/JDT | `remove_java_source_folder` | `PASS` | { "projectName": "eclipse-ultimate-mcp-server-plugin", "sourceFolder": "src/codex-smoke/java", "workspacePath": "/eclipse-ultimate-mcp-server-plugin/src/codex-smoke/java", "removed": true, "folderPreserved": true } |
| Maven/m2e | `remove_maven_dependency` | `PASS` | { "projectName": "eclipse-ultimate-mcp-server-plugin", "removed": true, "groupId": "org.example.codex", "artifactId": "codex-smoke-probe", "dependencyManagement": false, "m2eRefreshed": false } |
| File editing | `rename_file` | `PASS` | Renamed src/codex-smoke-files/probe.txt to renamed.txt |
| File editing | `replace_file_content` | `PASS` | Content already matched src/codex-smoke-files/probe.txt |
| File editing | `replace_string` | `PASS` | Updated src/codex-smoke-files/probe.txt |
| Maven/m2e | `resolve_maven_dependency` | `PASS` | Structured negative result: dependency not resolved, no mutation. |
| Workspace/IDE | `resolve_workspace_file` | `PASS` | { "projectName": "eclipse-ultimate", "workspacePath": "/eclipse-ultimate/README.md", "projectRelativePath": "README.md", "exists": true, "accessible": true, "linked": false, "directlyLinked": false, "linkedThroughAncestor": false, "virtual": false, "locationUr |
| Launch/debug | `resume_debug` | `EXPECTED_ERROR` | Error: No launch at index 999 (there are 0). |
| Java/JDT | `run_all_tests` | `FAIL` | Error: No tests found with test runner 'JUnit 3'. |
| Java/JDT | `run_class_tests` | `EXPECTED_ERROR` | Error: JUnit test class not found in eclipse-ultimate-mcp-server-plugin: nhb.eclipse.ultimate.mcpserver.smoke.DoesNotExistTest |
| Launch/debug | `set_conditional_breakpoint` | `PASS` | Set condition `true` on the temporary breakpoint and verified it through `list_breakpoints`. |
| Workspace/IDE | `show_proposed_changes` | `PASS` | Opened proposed changes for README.md |
| Launch/debug | `step_into` | `EXPECTED_ERROR` | Error: No launch at index 999 (there are 0). |
| Launch/debug | `step_over` | `EXPECTED_ERROR` | Error: No launch at index 999 (there are 0). |
| Launch/debug | `step_return` | `EXPECTED_ERROR` | Error: No launch at index 999 (there are 0). |
| Launch/debug | `stop_application` | `EXPECTED_ERROR` | Error: No launch at index 999 (there are 0). |
| Launch/debug | `toggle_breakpoint` | `PASS` | Set and then removed a breakpoint at `UpdateLaunchConfigurationTool:21`. |
| Launch/debug | `update_launch_configuration` | `PASS` | Persisted and read back string, boolean, integer, list, and map attributes; then removed all five with `null` and verified removal. |

## Findings

1. `run_all_tests` returned `No tests found with test runner 'JUnit 3'` for the plugin because the plugin has no discoverable test class in the smoke source set. This is recorded as a failure/limitation, not hidden.
2. `git_add` reported `Staged __codex_smoke_missing__` for a nonexistent path. `git_delete_branch` reported deletion of a nonexistent branch. The final repository status remained clean; both behaviors should be reviewed.
3. `update_launch_configuration` now persists typed values and removals correctly; the earlier false-success behavior was not reproduced.
4. The launch/debug tools completed when rerun individually after restart. Calls commonly took 5-12 seconds, so the earlier large-batch timeout did not indicate permanent endpoint failure.
5. `check_for_updates` used an unmatched filter and did not install or restart anything.

## Cleanup

- Temporary Java source/output and text resources were deleted.
- Temporary launch metadata from both batches was deleted. Eclipse still caches `Codex Smoke Final` and `Codex Smoke Final Copy` in memory until the next restart, but their backing files no longer exist.
- The temporary breakpoint was removed and the final breakpoint list was empty.
- No Git commit, branch, reset, stash, or application launch was intentionally performed.

## Follow-up

Review and fix the three `FAIL` behaviors: JUnit runner selection when a project has no discoverable tests, false success from `git_add` for a missing path, and false success from `git_delete_branch` for a missing branch. Restart Eclipse once to clear the two deleted temporary launch configurations from its in-memory cache.
