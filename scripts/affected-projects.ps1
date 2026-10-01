<#
.SYNOPSIS
    Calcula los modulos del monorepo afectados por un conjunto de cambios.

.DESCRIPTION
    Equivalente PowerShell de scripts/affected-projects.sh, para los equipos
    que trabajan en Windows sin Git Bash.

    git diff  ->  modulos cambiados  ->  Maven -am/-amd  ->  proyectos afectados

.PARAMETER Base
    Referencia de comparacion. Por defecto origin/main.

.PARAMETER Format
    list (por defecto) | maven

.EXAMPLE
    .\scripts\affected-projects.ps1 -Format maven
    .\mvnw (.\scripts\affected-projects.ps1 -Format maven).Split(' ') verify
#>
param(
    [string] $Base = "origin/main",
    [ValidateSet("list", "maven")]
    [string] $Format = "list"
)

$ErrorActionPreference = "Stop"
$repoRoot = Split-Path -Parent $PSScriptRoot
Set-Location $repoRoot

$allModules = @()
foreach ($pom in (Get-ChildItem -Path "apps", "libs" -Filter "pom.xml" -Depth 1 -File)) {
    $relative = Resolve-Path -Relative $pom.Directory.FullName
    $allModules += ($relative -replace '^\.\\', '' -replace '\\', '/')
}

git rev-parse --verify --quiet $Base | Out-Null
$baseExists = $?

if (-not $baseExists) {
    Write-Warning "La referencia '$Base' no existe; se construye todo."
    $changedModules = $allModules
}
else {
    $changedFiles = git diff --name-only "$Base...HEAD"
    $globalPattern = '^(pom\.xml|apps/pom\.xml|libs/pom\.xml|\.mvn/|mvnw|mvnw\.cmd|scripts/|\.github/workflows/)'

    if ($changedFiles | Where-Object { $_ -match $globalPattern }) {
        $changedModules = $allModules
    }
    else {
        $changedModules = @()
        foreach ($file in $changedFiles) {
            foreach ($module in $allModules) {
                if ($file.StartsWith("$module/") -and ($changedModules -notcontains $module)) {
                    $changedModules += $module
                }
            }
        }
    }
}

if ($changedModules.Count -eq 0) {
    if ($Format -eq "maven") { "" }
    return
}

switch ($Format) {
    "list"  { $changedModules }
    "maven" { "-pl " + ($changedModules -join ",") + " -am -amd" }
}
