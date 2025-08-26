# PowerShell script to start PostgreSQL and Redis infrastructure

Write-Host "Starting RestBook infrastructure..." -ForegroundColor Green

# Check if Docker is running
try {
    docker version | Out-Null
    Write-Host "Docker is running" -ForegroundColor Green
} catch {
    Write-Host "Error: Docker is not running. Please start Docker Desktop first." -ForegroundColor Red
    exit 1
}

# Stop and remove existing containers
Write-Host "Stopping existing containers..." -ForegroundColor Yellow
docker-compose down

# Start the infrastructure
Write-Host "Starting PostgreSQL and Redis..." -ForegroundColor Yellow
docker-compose up -d

# Wait for services to be ready
Write-Host "Waiting for services to be ready..." -ForegroundColor Yellow
Start-Sleep -Seconds 10

# Check if services are healthy
Write-Host "Checking service health..." -ForegroundColor Yellow

$postgresHealthy = $false
$redisHealthy = $false

for ($i = 1; $i -le 30; $i++) {
    $postgresStatus = docker inspect restbook-postgres --format='{{.State.Health.Status}}' 2>$null
    $redisStatus = docker inspect restbook-redis --format='{{.State.Health.Status}}' 2>$null
    
    if ($postgresStatus -eq "healthy") {
        $postgresHealthy = $true
        Write-Host "PostgreSQL is healthy" -ForegroundColor Green
    }
    
    if ($redisStatus -eq "healthy") {
        $redisHealthy = $true
        Write-Host "Redis is healthy" -ForegroundColor Green
    }
    
    if ($postgresHealthy -and $redisHealthy) {
        break
    }
    
    Write-Host "Waiting for services... ($i/30)" -ForegroundColor Yellow
    Start-Sleep -Seconds 2
}

if ($postgresHealthy -and $redisHealthy) {
    Write-Host "All services are ready!" -ForegroundColor Green
    Write-Host "PostgreSQL: localhost:5432" -ForegroundColor Cyan
    Write-Host "Redis: localhost:6379" -ForegroundColor Cyan
    Write-Host "You can now start the Spring Boot application." -ForegroundColor Green
} else {
    Write-Host "Error: Services failed to start properly." -ForegroundColor Red
    Write-Host "PostgreSQL healthy: $postgresHealthy" -ForegroundColor Red
    Write-Host "Redis healthy: $redisHealthy" -ForegroundColor Red
    exit 1
}
