# MediQueue

MediQueue is a Java console application organized around three design patterns:

- Abstract Factory for hospital-specific queue, scheduling, and notification products.
- Facade for coordinating patient registration and queue-related services.
- Observer for notifying patients, doctors, and administrators about queue changes.

## Project Layout

Java source files are under `src/`. Design documentation assets belong under `docs/`.

## Run

Compile from the project root with:

```text
javac -d out $(find src -name "*.java")
```

On Windows PowerShell, use:

```powershell
Get-ChildItem -Recurse src -Filter *.java | ForEach-Object FullName | javac -d out
```
