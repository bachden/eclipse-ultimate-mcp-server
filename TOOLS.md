# Eclipse Ultimate MCP Tool Reference

This document lists the tools advertised by the Eclipse Ultimate MCP Server runtime. It was generated from the live `tools/list` catalogue and currently covers **89 tools**.

## Availability

- The workspace, Java, file, Git, launch, and debug groups are available when their required Eclipse platform bundles are installed.
- Maven tools require m2e. POM editing tools additionally require the m2e UI editing bundle.
- Mylyn tools are registered only when the corresponding Mylyn task/build bundles are installed.
- The authoritative runtime contract is always the MCP `tools/list` response. This file documents the catalogue available in the build used to generate it.

## Conventions

- `projectName` is the Eclipse workspace project name, not an arbitrary filesystem directory.
- Project file paths are relative to the project root unless a tool explicitly says otherwise.
- Use `resolve_workspace_file` when the physical path matters; linked files and folders may live outside the project directory.
- Use `get_currently_opened_file` first when a user says "this file" or "this class".
- Read operations may return structured JSON encoded as MCP text content.

## Tool Index

- [Workspace and IDE](#workspace-and-ide) (15)
- [Java and JDT](#java-and-jdt) (21)
- [File and resource editing](#file-and-resource-editing) (10)
- [Maven and m2e](#maven-and-m2e) (6)
- [Mylyn](#mylyn) (5)
- [Git and EGit](#git-and-egit) (13)
- [Launch and debug](#launch-and-debug) (19)

## Workspace and IDE

### `build_project`

Build one open Eclipse project and wait for its configured builders to finish. Supports incremental (default) and full builds, and returns problem marker counts.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__build_project(args: {
  // Build kind: incremental (default) or full
  buildKind?: string;
  // The open Eclipse project to build
  projectName: string;
}): Promise<CallToolResult>; };
```

### `clean_projects`

Clean configured builders for selected open Eclipse projects and wait for completion. Omit projectNames to clean every open project in the workspace.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__clean_projects(args: {
  // Optional project names to clean; omit to clean every open project in the workspace
  projectNames?: Array<string>;
}): Promise<CallToolResult>; };
```

### `check_for_updates`

Check p2 repositories for updates (same as Help > Check for Updates), and apply them if found. Optionally pass siteFilter to only check repositories whose name or URL contains that text (e.g. this plugin's own dist/ site), instead of every configured repository. Blocks until the check (and install, if updates exist) finishes. Never restarts automatically  -  shows a confirm dialog so the user chooses when to restart.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__check_for_updates(args: {
  // Optional substring to filter which repositories are checked, matched against each repository's display name (nickname) or URL. Omit to check every configured repository.
  siteFilter?: string;
}): Promise<CallToolResult>; };
```

### `find_files`

Find files in a project whose relative path contains or ends with the given pattern (simple substring/suffix match, e.g. '.java' or 'PgSessionRepo').

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__find_files(args: {
  // Maximum number of results to return (default 200)
  maxResults?: number;
  // Substring/suffix to match against the file's relative path
  pattern: string;
  // The project to search in
  projectName: string;
}): Promise<CallToolResult>; };
```

### `file_search`

Search for a plain-text substring across all files in a project (case-insensitive). Returns matching file paths with line numbers and the matching line text.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__file_search(args: {
  // Maximum number of matches to return (default 200)
  maxResults?: number;
  // The project to search in
  projectName: string;
  // Plain-text substring to search for
  query: string;
}): Promise<CallToolResult>; };
```

### `file_search_regexp`

Search for a Java regular expression across all files in a project. Returns matching file paths with line numbers and the matching line text.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__file_search_regexp(args: {
  // Maximum number of matches to return (default 200)
  maxResults?: number;
  // Java regular expression to search for (applied per line)
  pattern: string;
  // The project to search in
  projectName: string;
}): Promise<CallToolResult>; };
```

### `get_console_output`

Get the currently buffered stdout/stderr of a launched process, given its launch label as shown by list_active_launches. Returns the most recently launched matching process's output.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_console_output(args: {
  // Label of the launch to read output from (substring match against the launch configuration name)
  launchLabel: string;
}): Promise<CallToolResult>; };
```

### `get_currently_opened_file`

Get the workspace-relative path of the file currently active in the Eclipse editor. Call this first whenever the user refers to "this file" or "this class".

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_currently_opened_file(args: { [key: string]: unknown; }): Promise<CallToolResult>; };
```

### `get_project_details`

Get detailed metadata for one Eclipse project: state, location, charset, natures, builders, project references, and Java source/compiler settings when applicable.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_project_details(args: {
  // The name of the project to inspect
  projectName: string;
}): Promise<CallToolResult>; };
```

### `get_project_layout`

Get the file and folder structure of a project in a hierarchical format. Use scopePath to limit to a subdirectory and/or maxDepth to limit tree depth.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_project_layout(args: {
  // Optional maximum depth of the directory tree to display
  maxDepth?: number;
  // The name of the project to analyze
  projectName: string;
  // Optional path relative to the project root to limit the listing
  scopePath?: string;
}): Promise<CallToolResult>; };
```

### `list_projects`

List all projects in the Eclipse workspace with their open/closed state and detected natures (Java, etc).

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__list_projects(args: { [key: string]: unknown; }): Promise<CallToolResult>; };
```

### `open_file`

Open a file in the Eclipse editor, given a project and a path relative to its root.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__open_file(args: {
  // Path to the file, relative to the project root
  filePath: string;
  // The project containing the file
  projectName: string;
}): Promise<CallToolResult>; };
```

### `read_project_resource`

Read the text content of a file inside a project, given a path relative to the project root.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__read_project_resource(args: {
  // The name of the project containing the file
  projectName: string;
  // Path to the file, relative to the project root
  resourcePath: string;
}): Promise<CallToolResult>; };
```

### `resolve_workspace_file`

Resolve a project-relative Eclipse workspace file to its absolute backing path. Uses the resource location URI so linked files and files under linked folders resolve to their actual target.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__resolve_workspace_file(args: {
  // Path to the file, relative to the project root
  filePath: string;
  // The Eclipse project containing the workspace file
  projectName: string;
}): Promise<CallToolResult>; };
```

### `show_proposed_changes`

Open Eclipse's Compare editor for a file, with the current workspace file on the editable left and proposed content on the read-only right. The user can copy selected changes and save them.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__show_proposed_changes(args: {
  // Path to the file, relative to the project root
  filePath: string;
  // The project containing the file
  projectName: string;
  // Complete proposed content for the file
  proposedContent: string;
  // Optional title for the Compare editor
  title?: string;
}): Promise<CallToolResult>; };
```


## Java and JDT

### `clean_up`

Run Eclipse JDT Clean Up using the active project/workspace Clean Up profile. Supply filePath for one Java file, or omit it to clean every source compilation unit in the Java project.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__clean_up(args: {
  // Optional path to one .java file; omit to clean the complete Java project
  filePath?: string;
  // The Java project to clean
  projectName: string;
}): Promise<CallToolResult>; };
```

### `configure_java_output_folder`

Set a Java project's default compiler output folder while preserving per-source output overrides.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__configure_java_output_folder(args: {
  // Create the output folder when it does not exist (default false)
  createIfMissing?: boolean;
  // Project-relative compiler output folder, such as target/classes
  outputFolder: string;
  // The Java project to configure
  projectName: string;
}): Promise<CallToolResult>; };
```

### `configure_java_source_folder`

Add or update a Java source folder in the project build path. Supports test-source marking, a separate output folder, linked folders and optional folder creation without changing other classpath entries.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__configure_java_source_folder(args: {
  // Create the source folder when it does not exist (default false)
  createIfMissing?: boolean;
  // Optional project-relative output folder for this source entry
  outputFolder?: string;
  // The Java project to configure
  projectName: string;
  // Project-relative source folder, such as src/test/java
  sourceFolder: string;
  // Mark this entry as test code; omitted preserves an existing value and defaults false for a new entry
  testSource?: boolean;
}): Promise<CallToolResult>; };
```

### `create_java_class`

Create and format a complete Java class in a source folder from structured metadata. One call can define imports, annotations, inheritance, fields, constructors, and implemented method bodies. The generated source is syntax-checked before it is written and opens in the Eclipse editor.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__create_java_class(args: {
  // Class annotations as Java source
  annotations?: Array<string>;
  // Simple class name without .java
  className: string;
  // Constructors to create
  constructors?: Array<{
  // Constructor annotations
  annotations?: Array<string>;
  // Constructor body statements without outer braces
  body: string;
  // Optional complete constructor Javadoc comment
  javadoc?: string;
  // Constructor modifiers; defaults to [public]
  modifiers?: Array<string>;
  // Parameters in declaration order
  parameters?: Array<{
  // Parameter annotations
  annotations?: Array<string>;
  // Parameter modifiers, e.g. final
  modifiers?: Array<string>;
  // Parameter name
  name: string;
  // Parameter Java type, including ... for varargs
  type: string;
}>;
  // Thrown exception type names
  throws?: Array<string>;
}>;
  // Optional superclass source name
  extendsType?: string;
  // Fields to create
  fields?: Array<{
  // Field annotations
  annotations?: Array<string>;
  // Optional Java initializer expression
  initializer?: string;
  // Optional complete field Javadoc comment
  javadoc?: string;
  // Field modifiers; defaults to [private]
  modifiers?: Array<string>;
  // Field name
  name: string;
  // Field Java type
  type: string;
}>;
  // Implemented interface source names
  implementsTypes?: Array<string>;
  // Explicit imports, with optional leading static
  imports?: Array<string>;
  // Optional complete class Javadoc comment
  javadoc?: string;
  // Methods to create, including their implementations
  methods?: Array<{
  // Method annotations
  annotations?: Array<string>;
  // Method body statements without outer braces; omit only for abstract/native methods
  body?: string;
  // Optional complete method Javadoc comment
  javadoc?: string;
  // Method modifiers; defaults to [public]
  modifiers?: Array<string>;
  // Method name
  name: string;
  // Parameters in declaration order
  parameters?: Array<{
  // Parameter annotations
  annotations?: Array<string>;
  // Parameter modifiers, e.g. final
  modifiers?: Array<string>;
  // Parameter name
  name: string;
  // Parameter Java type, including ... for varargs
  type: string;
}>;
  // Method return type
  returnType: string;
  // Thrown exception type names
  throws?: Array<string>;
  // Optional complete method type parameter clause
  typeParameters?: string;
}>;
  // Class modifiers; defaults to [public]
  modifiers?: Array<string>;
  // Open the generated class in Eclipse (default true)
  openInEditor?: boolean;
  // Java package name; use an empty string for the default package
  packageName: string;
  // The Java project in which to create the class
  projectName: string;
  // Optional project-relative source folder; defaults to src/main/java or the first non-test source root
  sourceFolder?: string;
  // Optional complete type parameter clause, e.g. <T extends Foo>
  typeParameters?: string;
}): Promise<CallToolResult>; };
```

### `find_references`

Find all references to a Java type, or to a specific method on that type, across the whole workspace using the JDT search engine.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__find_references(args: {
  // Fully-qualified type name, e.g. com.example.Foo
  fqName: string;
  // Maximum number of results to return (default 200)
  maxResults?: number;
  // Optional method name to search references of, instead of the type itself
  methodName?: string;
  // The Java project the type belongs to
  projectName: string;
}): Promise<CallToolResult>; };
```

### `find_test_classes`

Find candidate JUnit test classes in a project, matched by naming convention (ends with 'Test' or 'Tests', or starts with 'Test').

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__find_test_classes(args: {
  // The project to search in
  projectName: string;
}): Promise<CallToolResult>; };
```

### `format_code`

Format an entire Java file using Eclipse's code formatter (Ctrl+Shift+F) and save it to disk.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__format_code(args: {
  // Path to the .java file, relative to the project root
  filePath: string;
  // The project containing the file
  projectName: string;
}): Promise<CallToolResult>; };
```

### `get_class_outline`

Get a Java type's outline: fields and method signatures (no bodies), given its fully-qualified name. Cheaper than get_source when you just need the shape of a class.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_class_outline(args: {
  // Fully-qualified type name, e.g. com.example.Foo
  fqName: string;
  // The Java project the type belongs to
  projectName: string;
}): Promise<CallToolResult>; };
```

### `get_compilation_errors`

Get Java compilation problem markers (errors and warnings) for a project, optionally filtered to a single file (relative path).

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_compilation_errors(args: {
  // Optional path relative to the project root to filter to a single file
  filePath?: string;
  // The project to check
  projectName: string;
}): Promise<CallToolResult>; };
```

### `get_method_source`

Get the verbatim source of a single method on a type, given the type's fully-qualified name and the method name. If the method is overloaded, optionally disambiguate with parameterCount.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_method_source(args: {
  // Fully-qualified type name, e.g. com.example.Foo
  fqName: string;
  // Method name to look up
  methodName: string;
  // Optional parameter count to disambiguate overloads
  parameterCount?: number;
  // The Java project the type belongs to
  projectName: string;
}): Promise<CallToolResult>; };
```

### `get_project_dependencies`

List a Java project's classpath entries: referenced projects, library jars and classpath containers (e.g. Maven dependencies, JRE).

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_project_dependencies(args: {
  // The Java project to inspect
  projectName: string;
}): Promise<CallToolResult>; };
```

### `get_source`

Get the verbatim source of a Java type (class/interface/enum/record), given its fully-qualified name and the project it lives in. Output is line-numbered like `cat -n`.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_source(args: {
  // Fully-qualified type name, e.g. com.example.Foo
  fqName: string;
  // The Java project the type belongs to
  projectName: string;
}): Promise<CallToolResult>; };
```

### `organize_imports`

Organize a Java file's imports (Ctrl+Shift+O): removes unused imports, adds resolvable missing imports, sorts them, and commits the result through Eclipse's shared text buffer.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__organize_imports(args: {
  // Path to the .java file, relative to the project root
  filePath: string;
  // The project containing the file
  projectName: string;
}): Promise<CallToolResult>; };
```

### `quick_fix`

List headlessly applicable Eclipse JDT quick fixes for a Java file, or apply exactly one by proposalTitle/proposalIndex. Filter by line, problemId, or problemMessage to disambiguate.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__quick_fix(args: {
  // Path to the .java file, relative to the project root
  filePath: string;
  // Optional 1-based problem line
  line?: number;
  // Optional Eclipse JDT problem id
  problemId?: number;
  // Optional case-insensitive substring of the problem message
  problemMessage?: string;
  // The project containing the Java file
  projectName: string;
  // Optional zero-based proposal index within one matched problem to apply
  proposalIndex?: number;
  // Optional exact proposal title to apply; omit both proposal selectors to only list fixes
  proposalTitle?: string;
}): Promise<CallToolResult>; };
```

### `refactor_change_method_signature`

Change a Java method or constructor signature and update declarations and call sites across the workspace. A single call can rename the method, change return type/visibility, add, remove, rename, retype or reorder parameters, and replace the throws list.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__refactor_change_method_signature(args: {
  // Current parameter types used to identify the overload, in declaration order
  currentParameterTypes: Array<string>;
  // Fully-qualified declaring type, e.g. com.example.Service
  declaringType: string;
  // Mark the generated delegate deprecated (default false; requires keepDelegate)
  deprecateDelegate?: boolean;
  // Keep a delegating method with the old signature when JDT supports it (default false)
  keepDelegate?: boolean;
  // Current method name. Use the simple type name or <init> for a constructor
  methodName: string;
  // Optional new method name
  newName?: string;
  // Optional new return type; not valid for constructors
  newReturnType?: string;
  // Optional final parameter list. Existing parameters use sourceIndex; omitted indexes are deleted
  parameters?: Array<{
  // Java expression inserted at existing call sites for a new parameter
  defaultValue?: string;
  // Final parameter name
  name: string;
  // Zero-based index of an existing parameter; omit to add a new parameter
  sourceIndex?: number;
  // Final Java type, including [] or ... when applicable
  type: string;
}>;
  // The Java project containing the declaring type
  projectName: string;
  // Optional final throws list using fully-qualified exception type names
  thrownExceptions?: Array<string>;
  // Optional final visibility
  visibility?: "public" | "protected" | "package" | "private";
}): Promise<CallToolResult>; };
```

### `refactor_move_java_type`

Move a Java type (class/interface/enum/record) to a different package in the same project and update every reference across the workspace. The destination package must already exist.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__refactor_move_java_type(args: {
  // Current fully-qualified type name, e.g. com.example.Foo
  fqName: string;
  // The Java project the type belongs to
  projectName: string;
  // Destination package name, e.g. com.example.util
  targetPackage: string;
}): Promise<CallToolResult>; };
```

### `refactor_rename_java_type`

Rename a Java type (class/interface/enum/record) and update every reference across the workspace, including the file name. Uses the same refactoring engine as Eclipse's Rename wizard.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__refactor_rename_java_type(args: {
  // Current fully-qualified type name, e.g. com.example.Foo
  fqName: string;
  // New simple type name (no package)
  newName: string;
  // The Java project the type belongs to
  projectName: string;
}): Promise<CallToolResult>; };
```

### `refactor_rename_package`

Rename a package (and its folder) and update every reference across the workspace, using the same refactoring engine as Eclipse's Rename wizard.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__refactor_rename_package(args: {
  // New package name, e.g. com.example.newname
  newName: string;
  // Current package name, e.g. com.example.old
  packageName: string;
  // The Java project the package belongs to
  projectName: string;
}): Promise<CallToolResult>; };
```

### `remove_java_source_folder`

Remove a project-relative source folder from the Java build path without deleting the folder or its files.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__remove_java_source_folder(args: {
  // Fail when the folder is not currently a source entry (default false)
  failIfMissing?: boolean;
  // The Java project to configure
  projectName: string;
  // Project-relative source folder to remove
  sourceFolder: string;
}): Promise<CallToolResult>; };
```

### `run_all_tests`

Run every JUnit test in a project with the compatible runner detected by Eclipse JDT, and return the captured console output. Blocks until the run finishes or times out.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__run_all_tests(args: {
  // The project whose tests to run
  projectName: string;
  // Max time to wait for the run to finish (default 300)
  timeoutSeconds?: number;
}): Promise<CallToolResult>; };
```

### `run_class_tests`

Run every test method in a single JUnit test class with the JUnit 3/4/5/6 runner detected by Eclipse JDT, and return the captured console output. Blocks until the run finishes or times out.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__run_class_tests(args: {
  // Fully-qualified test class name, e.g. com.example.FooTest
  fqTestClassName: string;
  // The project the test class belongs to
  projectName: string;
  // Max time to wait for the run to finish (default 120)
  timeoutSeconds?: number;
}): Promise<CallToolResult>; };
```


## File and resource editing

### `apply_patch`

Apply a unified diff patch (standard @@ hunk format) to a single file. Reads and commits through Eclipse's shared text buffer, validates hunk counts/context, and fails if the patch makes no change.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__apply_patch(args: {
  // Path to the file, relative to the project root
  filePath: string;
  // Unified diff content with @@ hunk headers
  patch: string;
  // The project containing the file
  projectName: string;
}): Promise<CallToolResult>; };
```

### `create_directories`

Create a folder in a project, along with any missing parent folders. No-op if it already exists.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__create_directories(args: {
  // Path of the folder, relative to the project root
  folderPath: string;
  // The project to create the folder in
  projectName: string;
}): Promise<CallToolResult>; };
```

### `create_file`

Create a new file with the given content in a project. Fails if the file already exists. Any missing parent folders are created automatically.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__create_file(args: {
  // The content to write to the file
  content: string;
  // Path of the new file, relative to the project root
  filePath: string;
  // The project to create the file in
  projectName: string;
}): Promise<CallToolResult>; };
```

### `delete_file`

Delete a file or folder (recursively) from a project.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__delete_file(args: {
  // The project containing the resource
  projectName: string;
  // Path of the file or folder, relative to the project root
  resourcePath: string;
}): Promise<CallToolResult>; };
```

### `delete_lines_in_file`

Delete an inclusive range of 1-based lines from a file.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__delete_lines_in_file(args: {
  // 1-based last line to delete (inclusive)
  endLine: number;
  // Path to the file, relative to the project root
  filePath: string;
  // The project containing the file
  projectName: string;
  // 1-based first line to delete (inclusive)
  startLine: number;
}): Promise<CallToolResult>; };
```

### `insert_into_file`

Insert text before a given 1-based line number, or replace an inclusive line range when replaceEndLine is provided. Line 1 is the top; one past the last line appends.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__insert_into_file(args: {
  // Path to the file, relative to the project root
  filePath: string;
  // 1-based insertion line or first line to replace
  line: number;
  // The project containing the file
  projectName: string;
  // Optional 1-based last line to replace (inclusive); must be at or after line
  replaceEndLine?: number;
  // Text to insert or use as the replacement; include a line delimiter when needed
  text: string;
}): Promise<CallToolResult>; };
```

### `move_resource`

Move a file or folder to a new path (optionally in a different project). Plain resource move, not Java-aware  -  use refactor_move_java_type to move a .java file and update references.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__move_resource(args: {
  // The project currently containing the resource
  projectName: string;
  // Current path, relative to the project root
  resourcePath: string;
  // Destination path, relative to the destination project root
  targetPath: string;
  // Destination project (defaults to the same project)
  targetProjectName?: string;
}): Promise<CallToolResult>; };
```

### `rename_file`

Rename a file or folder in place (same parent directory). This is a plain resource rename, not a Java-aware refactor  -  use refactor_rename_java_type to rename a .java file and update references.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__rename_file(args: {
  // New simple name (no path segments)
  newName: string;
  // The project containing the resource
  projectName: string;
  // Current path of the file or folder, relative to the project root
  resourcePath: string;
}): Promise<CallToolResult>; };
```

### `replace_file_content`

Overwrite a file's entire content through Eclipse's shared text buffer. The file must already exist; use create_file for new files.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__replace_file_content(args: {
  // New content for the file
  content: string;
  // Path to the file, relative to the project root
  filePath: string;
  // The project containing the file
  projectName: string;
}): Promise<CallToolResult>; };
```

### `replace_string`

Replace an exact string match in a file. Fails if oldString is not found, or if it matches more than once and replaceAll is not set.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__replace_string(args: {
  // Path to the file, relative to the project root
  filePath: string;
  // Replacement text
  newString: string;
  // Exact text to find
  oldString: string;
  // The project containing the file
  projectName: string;
  // Replace every occurrence instead of requiring a unique match
  replaceAll?: boolean;
}): Promise<CallToolResult>; };
```


## Maven and m2e

### `configure_maven_dependency`

Add or update a direct Maven dependency using m2e's DOM POM editor, preserving surrounding XML formatting and comments. Supports dependencyManagement, scope, type, classifier, optional and exclusions.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__configure_maven_dependency(args: {
  // Dependency artifactId
  artifactId: string;
  // Optional dependency classifier
  classifier?: string;
  // Edit dependencyManagement instead of direct dependencies (default false)
  dependencyManagement?: boolean;
  // Optional final exclusion list; an empty array removes all exclusions
  exclusions?: Array<{
  // Dependency artifactId to exclude
  artifactId: string;
  // Dependency groupId to exclude
  groupId: string;
}>;
  // Force dependency and snapshot updates during refresh (default false)
  forceDependencyUpdate?: boolean;
  // Dependency groupId
  groupId: string;
  // Optional dependency flag; false removes the optional element
  optional?: boolean;
  // The open m2e project whose pom.xml to edit
  projectName: string;
  // Refresh the m2e project configuration after saving (default true)
  refreshProject?: boolean;
  // Optional dependency scope
  scope?: string;
  // Optional dependency type; defaults to jar
  type?: string;
  // Optional version; omit to preserve, or use an empty string to remove for managed versions
  version?: string;
}): Promise<CallToolResult>; };
```

### `get_maven_dependency_graph`

Get the resolved, dependency-mediated Maven graph from m2e. Includes dependency trails, resolved artifact paths, and matching workspace projects when available.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_maven_dependency_graph(args: {
  // Include optional dependencies (default true)
  includeOptional?: boolean;
  // Maximum dependency depth to return (default 100)
  maxDepth?: number;
  // The open m2e Maven project to inspect
  projectName: string;
  // Optional resolved scope filter: compile, provided, runtime, test, system or import
  scope?: string;
}): Promise<CallToolResult>; };
```

### `open_maven_dependency_project`

Open the existing workspace project that m2e resolves for a dependency. Does not import projects or extract JARs; fails when the dependency has no workspace project match.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__open_maven_dependency_project(args: {
  // Dependency artifactId
  artifactId: string;
  // Optional classifier; omit to select the main artifact
  classifier?: string;
  // Dependency groupId
  groupId: string;
  // The open m2e project whose resolved dependencies to search
  projectName: string;
  // Optional artifact type, such as jar or test-jar
  type?: string;
  // Optional selected version; omit to use m2e's resolved version
  version?: string;
}): Promise<CallToolResult>; };
```

### `refresh_maven_project`

Refresh a Maven project's effective model, classpath and m2e configuration after pom.xml changes.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__refresh_maven_project(args: {
  // Force dependency and snapshot updates during refresh (default false)
  forceDependencyUpdate?: boolean;
  // The open m2e project to refresh
  projectName: string;
}): Promise<CallToolResult>; };
```

### `remove_maven_dependency`

Remove a matching direct Maven dependency through m2e's DOM POM editor. Preserves comments and formatting outside the removed dependency.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__remove_maven_dependency(args: {
  // Dependency artifactId
  artifactId: string;
  // Optional dependency classifier
  classifier?: string;
  // Remove from dependencyManagement instead of direct dependencies (default false)
  dependencyManagement?: boolean;
  // Fail when no matching dependency exists (default true)
  failIfMissing?: boolean;
  // Force dependency and snapshot updates during refresh (default false)
  forceDependencyUpdate?: boolean;
  // Dependency groupId
  groupId: string;
  // The open m2e project whose pom.xml to edit
  projectName: string;
  // Refresh the m2e project configuration after saving (default true)
  refreshProject?: boolean;
  // Optional dependency type; defaults to jar
  type?: string;
}): Promise<CallToolResult>; };
```

### `resolve_maven_dependency`

Resolve a Maven dependency using m2e's current project model. Reports the selected artifact and the workspace project that substitutes for it, so source lookup can avoid JAR extraction.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__resolve_maven_dependency(args: {
  // Dependency artifactId
  artifactId: string;
  // Optional classifier; omit to select the main artifact
  classifier?: string;
  // Dependency groupId
  groupId: string;
  // The open m2e project whose resolved dependencies to search
  projectName: string;
  // Optional artifact type, such as jar or test-jar
  type?: string;
  // Optional selected version; omit to use m2e's resolved version
  version?: string;
}): Promise<CallToolResult>; };
```


## Mylyn

### `get_mylyn_build_log`

Read console output for an exact Mylyn build number, or the current last build when buildNumber is omitted. Cached metadata is used when possible; with a plan selector the connector can load builds that are not cached. Output is bounded by maxChars.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_mylyn_build_log(args: {
  // Optional exact build number; omit for the current last matching build. A plan selector is required when the requested build is not already cached
  buildNumber?: number;
  // Optional exact connector kind used to disambiguate servers sharing a URL
  connectorKind?: string;
  // Maximum log characters to return (default 100000, range 1000-1000000)
  maxChars?: number;
  // Optional exact Mylyn build plan id or URL
  planId?: string;
  // Optional case-insensitive substring of the build plan name
  planName?: string;
  // Exact repository URL returned by list_mylyn_build_servers
  repositoryUrl: string;
  // Return the end of a truncated log instead of the beginning (default true)
  tail?: boolean;
}): Promise<CallToolResult>; };
```

### `get_mylyn_build_server`

Get safe connection metadata, plans and cached builds for one Mylyn Builds server such as Jenkins. Set refresh=true to connect to the server and refresh its configuration.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_mylyn_build_server(args: {
  // Optional exact connector kind used to disambiguate servers sharing a URL
  connectorKind?: string;
  // Maximum number of cached builds to return (default 20, maximum 200; use 0 to omit)
  maxBuilds?: number;
  // Maximum number of plans to return across the plan hierarchy (default 100, maximum 1000)
  maxPlans?: number;
  // Connect to the build server and refresh its configuration before returning plans (default false)
  refresh?: boolean;
  // Exact repository URL returned by list_mylyn_build_servers
  repositoryUrl: string;
}): Promise<CallToolResult>; };
```

### `get_mylyn_task_repository`

Get safe connection metadata, status and connector capabilities for one configured Mylyn Task Repository. Passwords, tokens and other secret properties are redacted.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_mylyn_task_repository(args: {
  // Exact connector kind returned by list_mylyn_task_repositories
  connectorKind: string;
  // Exact repository URL returned by list_mylyn_task_repositories
  repositoryUrl: string;
}): Promise<CallToolResult>; };
```

### `list_mylyn_build_servers`

List build servers configured in Mylyn Builds (including Jenkins), optionally filtered by connector kind and server name using case-insensitive substring or regex matching. Returns connection status and cached plan/build counts; credentials are never returned.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__list_mylyn_build_servers(args: {
  // Optional exact connector kind, as returned by this tool
  connectorKind?: string;
  // Optional case-insensitive substring matched against the build server name
  nameIlike?: string;
  // Optional Java regular expression searched within the build server name; mutually exclusive with nameIlike
  nameRegex?: string;
}): Promise<CallToolResult>; };
```

### `list_mylyn_task_repositories`

List Mylyn Task Repositories configured in Eclipse, optionally filtered by connector kind and repository name using case-insensitive substring or regex matching. Credentials and other secrets are never returned.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__list_mylyn_task_repositories(args: {
  // Optional exact connector kind, as returned by this tool
  connectorKind?: string;
  // Optional case-insensitive substring matched against the task repository name
  nameIlike?: string;
  // Optional Java regular expression searched within the task repository name; mutually exclusive with nameIlike
  nameRegex?: string;
}): Promise<CallToolResult>; };
```


## Git and EGit

### `git_add`

Stage one or more paths in a project's Git repository (git add). Pass '.' to stage everything.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_add(args: {
  // Path (relative to the repository root) to stage; '.' stages everything
  path: string;
  // The project whose repository to stage in
  projectName: string;
}): Promise<CallToolResult>; };
```

### `git_branch`

List local branches in a project's Git repository, marking the currently checked-out one.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_branch(args: {
  // The project whose repository to inspect
  projectName: string;
}): Promise<CallToolResult>; };
```

### `git_checkout`

Check out a branch, tag or commit in a project's Git repository. Set createBranch=true to create and check out a new branch in one step.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_checkout(args: {
  // Create ref as a new branch instead of checking out an existing one
  createBranch?: boolean;
  // The project whose repository to check out in
  projectName: string;
  // Branch name, tag or commit to check out
  ref: string;
}): Promise<CallToolResult>; };
```

### `git_commit`

Commit currently staged changes in a project's Git repository. Fails with JGit's usual error if nothing is staged.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_commit(args: {
  // Amend the previous commit instead of creating a new one
  amend?: boolean;
  // Commit message
  message: string;
  // The project whose repository to commit in
  projectName: string;
}): Promise<CallToolResult>; };
```

### `git_create_branch`

Create a new local branch from the given start point (default HEAD). Does not check it out; use git_checkout for that.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_create_branch(args: {
  // Name for the new branch
  branchName: string;
  // The project whose repository to branch
  projectName: string;
  // Ref/commit to branch from (default HEAD)
  startPoint?: string;
}): Promise<CallToolResult>; };
```

### `git_delete_branch`

Delete a local branch. Set force=true to delete even if it is not fully merged.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_delete_branch(args: {
  // Branch to delete
  branchName: string;
  // Delete even if not fully merged
  force?: boolean;
  // The project whose repository to modify
  projectName: string;
}): Promise<CallToolResult>; };
```

### `git_diff`

Get a unified diff for a project's Git repository. By default diffs the working tree against the index (unstaged changes); set staged=true to diff the index against HEAD instead.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_diff(args: {
  // Optional path (relative to the repository root) to limit the diff to
  path?: string;
  // The project whose repository to diff
  projectName: string;
  // Diff the index against HEAD instead of the working tree against the index
  staged?: boolean;
}): Promise<CallToolResult>; };
```

### `git_log`

List recent commits reachable from HEAD: hash, author, date and message summary.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_log(args: {
  // Maximum number of commits to return (default 20)
  maxCount?: number;
  // The project whose repository to inspect
  projectName: string;
}): Promise<CallToolResult>; };
```

### `git_reset`

Reset the current branch to a ref. mode is SOFT (move HEAD only), MIXED (default; also reset the index) or HARD (also overwrite the working tree  -  discards uncommitted changes).

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_reset(args: {
  // SOFT, MIXED or HARD (default MIXED)
  mode?: string;
  // The project whose repository to reset
  projectName: string;
  // Ref/commit to reset to (default HEAD)
  ref?: string;
}): Promise<CallToolResult>; };
```

### `git_stash`

Stash uncommitted changes in a project's Git repository (git stash push). Set includeUntracked to also stash untracked files.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_stash(args: {
  // Also stash untracked files
  includeUntracked?: boolean;
  // Optional stash message
  message?: string;
  // The project whose repository to stash in
  projectName: string;
}): Promise<CallToolResult>; };
```

### `git_stash_list`

List all stash entries in a project's Git repository, most recent first.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_stash_list(args: {
  // The project whose repository to inspect
  projectName: string;
}): Promise<CallToolResult>; };
```

### `git_stash_pop`

Apply and drop a stash entry (git stash pop). Defaults to the most recent (index 0).

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_stash_pop(args: {
  // The project whose repository to pop from
  projectName: string;
  // Stash index to pop (default 0, the most recent)
  stashRef?: number;
}): Promise<CallToolResult>; };
```

### `git_status`

Get the working tree and index status of a project's Git repository: staged, modified, untracked, conflicting and missing files, plus the current branch.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__git_status(args: {
  // The project whose repository to inspect
  projectName: string;
}): Promise<CallToolResult>; };
```


## Launch and debug

### `create_launch_configuration`

Create a new saved launch configuration of any type by id (Java Application, JUnit, Maven Build, Program/External Tools, Eclipse Application, ...), setting arbitrary attributes on it. See get_launch_configuration on a similar existing config to learn the attribute keys/types a given launch type actually uses.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__create_launch_configuration(args: {
  // Attribute key -> value map. Values may be strings, numbers, booleans, or arrays of strings.
  attributes?: { [key: string]: unknown; };
  // Name for the new launch configuration
  name: string;
  // Launch configuration type id, e.g. org.eclipse.jdt.launching.localJavaApplication, org.eclipse.jdt.junit.launchconfig, org.eclipse.ui.externaltools.ProgramLaunchConfigurationType
  typeId: string;
}): Promise<CallToolResult>; };
```

### `duplicate_launch_configuration`

Duplicate a saved Eclipse launch configuration, preserving its launch type, attributes, mapped resources and prototype metadata.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__duplicate_launch_configuration(args: {
  // Generate a numbered unique name when newName is already used (default false)
  generateUniqueName?: boolean;
  // Name for the duplicate
  newName: string;
  // Exact name of the launch configuration to duplicate
  sourceName: string;
}): Promise<CallToolResult>; };
```

### `evaluate_expression`

Evaluate a Java expression in the context of a suspended stack frame, given launchIndex, threadIndex, frameIndex and a projectName to resolve types against. Blocks until the evaluation completes.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__evaluate_expression(args: {
  // Java expression to evaluate
  expression: string;
  // Index of the stack frame (0 = top)
  frameIndex: number;
  // Index of the launch, from list_active_launches
  launchIndex: number;
  // Java project used to resolve types for the expression
  projectName: string;
  // Index of the thread within the launch's debug target
  threadIndex: number;
}): Promise<CallToolResult>; };
```

### `get_launch_configuration`

Get full details of a single saved launch configuration by name: its type id and every attribute (project, main class, VM/program arguments, working directory, etc  -  whatever that launch type stores). Use list_launch_configurations first to find the exact name.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_launch_configuration(args: {
  // Exact name of the launch configuration
  name: string;
}): Promise<CallToolResult>; };
```

### `get_stack_trace`

Get the stack trace of a suspended thread, given launchIndex and threadIndex. Each frame includes its type/method/line and local variables with their current values.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__get_stack_trace(args: {
  // Include local variables per frame (default true)
  includeVariables?: boolean;
  // Index of the launch, from list_active_launches
  launchIndex: number;
  // Index of the thread within the launch's debug target
  threadIndex: number;
}): Promise<CallToolResult>; };
```

### `launch_configuration`

Launch a saved launch configuration by name (see list_launch_configurations), in run or debug mode. Returns immediately after the launch is scheduled  -  poll list_active_launches for status.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__launch_configuration(args: {
  // 'run' or 'debug' (default 'run')
  mode?: string;
  // Exact name of the launch configuration
  name: string;
}): Promise<CallToolResult>; };
```

### `list_active_launches`

List all currently tracked launches (running or recently finished) with their index, launch config name, mode (run/debug) and termination state. The index is used by other runner tools.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__list_active_launches(args: { [key: string]: unknown; }): Promise<CallToolResult>; };
```

### `list_breakpoints`

List all breakpoints currently set in the workspace, showing type, location, enabled state and any condition.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__list_breakpoints(args: { [key: string]: unknown; }): Promise<CallToolResult>; };
```

### `list_launch_configurations`

List all saved launch configurations in the workspace: name, type, and for Java applications the project and main class. Use the name with launch_configuration.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__list_launch_configurations(args: { [key: string]: unknown; }): Promise<CallToolResult>; };
```

### `refresh_launch_configurations`

Force the workspace to rescan for launch configuration changes made directly on disk (outside create_launch_configuration/update_launch_configuration), then return the up-to-date count and names. Use this if a .launch file was added/edited by another process and isn't showing up yet.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__refresh_launch_configurations(args: { [key: string]: unknown; }): Promise<CallToolResult>; };
```

### `remove_all_breakpoints`

Remove every breakpoint currently set in the workspace.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__remove_all_breakpoints(args: { [key: string]: unknown; }): Promise<CallToolResult>; };
```

### `resume_debug`

Resume a suspended thread, given launchIndex (from list_active_launches) and threadIndex.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__resume_debug(args: {
  // Index of the launch, from list_active_launches
  launchIndex: number;
  // Index of the thread within the launch's debug target
  threadIndex: number;
}): Promise<CallToolResult>; };
```

### `set_conditional_breakpoint`

Set or update the condition on an existing Java line breakpoint at the given type + line. The breakpoint must already exist (see toggle_breakpoint).

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__set_conditional_breakpoint(args: {
  // Java boolean expression; breakpoint fires when true
  condition: string;
  // Fully-qualified type name the breakpoint is set on
  fqName: string;
  // 1-based line number of the breakpoint
  line: number;
}): Promise<CallToolResult>; };
```

### `step_into`

Step into the next executable line of a suspended thread, given launchIndex and threadIndex.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__step_into(args: {
  // Index of the launch, from list_active_launches
  launchIndex: number;
  // Index of the thread within the launch's debug target
  threadIndex: number;
}): Promise<CallToolResult>; };
```

### `step_over`

Step over the next executable line of a suspended thread, given launchIndex and threadIndex.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__step_over(args: {
  // Index of the launch, from list_active_launches
  launchIndex: number;
  // Index of the thread within the launch's debug target
  threadIndex: number;
}): Promise<CallToolResult>; };
```

### `step_return`

Step out of the current method (step return) on a suspended thread, given launchIndex and threadIndex.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__step_return(args: {
  // Index of the launch, from list_active_launches
  launchIndex: number;
  // Index of the thread within the launch's debug target
  threadIndex: number;
}): Promise<CallToolResult>; };
```

### `stop_application`

Terminate a launch given its index from list_active_launches.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__stop_application(args: {
  // Index of the launch, from list_active_launches
  launchIndex: number;
}): Promise<CallToolResult>; };
```

### `toggle_breakpoint`

Set or remove a Java line breakpoint, given the project, fully-qualified type name and line number. If a breakpoint already exists at that exact location it is removed instead of duplicated.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__toggle_breakpoint(args: {
  // Fully-qualified type name, e.g. com.example.Foo
  fqName: string;
  // 1-based line number
  line: number;
  // The Java project the type belongs to
  projectName: string;
}): Promise<CallToolResult>; };
```

### `update_launch_configuration`

Update attributes on an existing saved launch configuration by name. Only the given attribute keys are changed; everything else is preserved. Pass null to remove an attribute. Primitive values, arrays and string-valued objects are persisted with their Eclipse launch attribute types.

Input contract:

```ts
declare const tools: { mcp__eclipse_ultimate__update_launch_configuration(args: {
  // Attribute key -> value map to merge in. Values may be strings, integers, booleans, arrays of primitive values, string-valued objects, or null to remove that attribute.
  attributes: { [key: string]: unknown; };
  // Exact name of the launch configuration to update
  name: string;
}): Promise<CallToolResult>; };
```

## Maintenance

Regenerate or review this file whenever a tool is added, removed, renamed, or its input schema changes. Compare it against the server's live `tools/list` response before publishing.
