# PowerShell script to stop PostgreSQL and Redis infrastructure

Write-Host "Stopping RestBook infrastructure..." -ForegroundColor Yellow

# Stop and remove containers
docker-compose down

Write-Host "Infrastructure stopped successfully!" -ForegroundColor Green
