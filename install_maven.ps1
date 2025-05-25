# Create Maven directory
$mavenDir = "C:\Program Files\Apache\maven"
New-Item -ItemType Directory -Force -Path $mavenDir

# Download Maven
$url = "https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
$output = "maven.zip"
Write-Host "Downloading Maven..."
Invoke-WebRequest -Uri $url -OutFile $output

# Extract Maven
Write-Host "Extracting Maven..."
Expand-Archive -Path $output -DestinationPath $mavenDir -Force

# Add Maven to PATH
$mavenBin = "$mavenDir\apache-maven-3.9.6\bin"
$currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
if (-not $currentPath.Contains($mavenBin)) {
    [Environment]::SetEnvironmentVariable("Path", "$currentPath;$mavenBin", "Machine")
}

# Clean up
Remove-Item $output

Write-Host "Maven installation complete!" 