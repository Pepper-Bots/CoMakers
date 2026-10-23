param([string]$BaseUrl = 'http://localhost:8081')
$ErrorActionPreference = 'Stop'
$BaseUrl = $BaseUrl.TrimEnd('/')
function Invoke-Expected {
    param([string]$Method, [string]$Path, [int]$Expected, [object]$Body = $null, [hashtable]$Headers = @{})
    $params = @{ Uri = "$BaseUrl$Path"; Method = $Method; Headers = $Headers; UseBasicParsing = $true }
    if ($null -ne $Body) {
        $params.ContentType = 'application/json'
        $params.Body = $Body | ConvertTo-Json -Compress
    }
    $content = ''
    try {
        $response = Invoke-WebRequest @params
        $status = [int]$response.StatusCode
        $content = $response.Content
    } catch {
        if ($null -eq $_.Exception.Response) { throw "Cannot reach $BaseUrl. Start the API first." }
        $status = [int]$_.Exception.Response.StatusCode
    }
    if ($status -ne $Expected) { throw "$Method $Path expected $Expected but received $status." }
    Write-Host "$Method $Path -> $status OK"
    if ($content) { return ($content | ConvertFrom-Json) }
}
$email = "onboarding-$([Guid]::NewGuid().ToString('N'))@example.com"
$password = "Local-$([Guid]::NewGuid().ToString('N'))!"
$health = Invoke-Expected GET '/actuator/health' 200
if ($health.status -ne 'UP') { throw 'Health is not UP.' }
$account = @{ displayName = 'Onboarding test'; email = $email; password = $password }
$user = Invoke-Expected POST '/api/users/register' 201 $account
if (-not $user.id -or $user.email -ne $email) { throw 'Registration response mismatch.' }
if ($user.PSObject.Properties['passwordHash'] -or $user.PSObject.Properties['password']) { throw 'Credential leaked.' }
$null = Invoke-Expected POST '/api/users/register' 409 $account
$null = Invoke-Expected POST '/api/users/register' 400 @{ displayName = 'Test'; email = 'invalid'; password = 'abc' }
$token = Invoke-Expected POST '/api/auth/login' 200 @{ email = $email; password = $password }
if (-not $token.accessToken -or $token.tokenType -ne 'Bearer') { throw 'Missing Bearer token.' }
$profile = Invoke-Expected GET '/api/auth/me' 200 -Headers @{ Authorization = "Bearer $($token.accessToken)" }
if ($profile.id -ne $user.id -or $profile.email -ne $email) { throw 'Authenticated identity mismatch.' }
$null = Invoke-Expected GET '/api/auth/me' 401
$null = Invoke-Expected GET '/api/auth/me' 401 -Headers @{ Authorization = 'Bearer invalid-token' }
$null = Invoke-Expected POST '/api/auth/login' 401 @{ email = $email; password = 'wrong-password' }
Write-Host "Smoke test passed. One test account remains in this database: $email"
