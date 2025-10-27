$data = @{
    username = "testuser"
    email = "test@example.com"
    password = "password123"
    firstName = "Test"
    lastName = "User"
}

$jsonBody = $data | ConvertTo-Json
$headers = @{
    "Content-Type" = "application/json"
}

Write-Host "Sending request to http://localhost:8000/api/users/register"
Write-Host "Request body: $jsonBody"

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8000/api/users/register" `
        -Method Post `
        -Headers $headers `
        -Body $jsonBody `
        -UseBasicParsing

    Write-Host "`nResponse Status Code: $($response.StatusCode)"
    Write-Host "Response Content:"
    $response.Content
} catch {
    Write-Host "`nError occurred:"
    Write-Host "Status Code: $($_.Exception.Response.StatusCode.value__)"
    Write-Host "Status Description: $($_.Exception.Response.StatusDescription)"
    Write-Host "Error Message: $($_.Exception.Message)"
}