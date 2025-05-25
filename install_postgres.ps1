# Download PostgreSQL installer
$url = "https://get.enterprisedb.com/postgresql/postgresql-15.5-1-windows-x64.exe"
$output = "postgresql-installer.exe"
Write-Host "Downloading PostgreSQL installer..."
Invoke-WebRequest -Uri $url -OutFile $output

# Install PostgreSQL silently
Write-Host "Installing PostgreSQL..."
Start-Process -FilePath $output -ArgumentList "--unattendedmodeui minimal --mode unattended --superpassword postgres --serverport 5432" -Wait

# Clean up installer
Remove-Item $output

Write-Host "PostgreSQL installation complete!" 