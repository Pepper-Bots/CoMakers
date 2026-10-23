$ErrorActionPreference = 'Stop'
$taskRoot = Split-Path -Parent $PSScriptRoot
$taskEnvPath = Join-Path $taskRoot '.env'
if (Test-Path -LiteralPath $taskEnvPath) {
    Write-Host '.env already exists; preserved without changes.'
    exit 0
}
function New-LocalSecret {
    $bytes = New-Object byte[] 32
    $random = [System.Security.Cryptography.RandomNumberGenerator]::Create()
    try { $random.GetBytes($bytes) } finally { $random.Dispose() }
    return [Convert]::ToBase64String($bytes)
}
$lines = @(
    '# Local Docker credentials. Do not commit or share.'
    "DB_PASSWORD=$(New-LocalSecret)"
    "MYSQL_ROOT_PASSWORD=$(New-LocalSecret)"
    "JWT_SECRET=$(New-LocalSecret)"
    'DB_PORT=3307'
    'API_PORT=8081'
)
# CreateNew prevents overwriting an existing file even if two processes run.
$stream = [System.IO.File]::Open($taskEnvPath, [System.IO.FileMode]::CreateNew)
$writer = [System.IO.StreamWriter]::new($stream, [System.Text.UTF8Encoding]::new($false))
try { $writer.Write(($lines -join "`n") + "`n") } finally { $writer.Dispose() }
Write-Host '.env created with random local credentials. Values were not printed.'
