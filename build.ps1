$project = $PSScriptRoot
$jpackage = "C:\Users\Mert\.jdks\openjdk-26.0.2\bin\jpackage.exe"

$jarDir = "$project\out\artifacts\FalconEYE_jar"
$jar = "FalconEYE.jar"
$icon = "$project\falconeye_icon.ico"
$dest = "$project\dist"

Write-Host "=== Building JAR ===" -ForegroundColor Cyan

# IntelliJ artifact'i elle build etmek yerine burada sadece mevcut JAR'ı kullanıyoruz.
# JAR'ı IntelliJ'den Build Artifacts ile güncelledikten sonra devam eder.

Write-Host "=== Cleaning old EXE ===" -ForegroundColor Cyan

if (Test-Path "$dest\FalconEYE_v1.0") {
    Remove-Item "$dest\FalconEYE_v1.0" -Recurse -Force
}

Write-Host "=== Creating EXE ===" -ForegroundColor Cyan

& $jpackage `
    --type app-image `
    --name FalconEYE_v1.0 `
    --input $jarDir `
    --main-jar $jar `
    --main-class App.Main `
    --icon $icon `
    --vendor "Mert Kavilcioglu" `
    --description "FalconEYE Airborne Fire Control Radar Simulation" `
    --app-version "1.0.0" `
    --dest $dest

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "=== BUILD SUCCESS ===" -ForegroundColor Green
    Write-Host "EXE: $dest\FalconEYE_v1.0\FalconEYE_v1.0.exe"
}
else {
    Write-Host ""
    Write-Host "=== BUILD FAILED ===" -ForegroundColor Red
}