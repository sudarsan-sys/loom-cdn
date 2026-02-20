# 1. Configuration
$url = "http://localhost:8080/api/v1/cdn/upload"

# ⚠️ IMPORTANT: Paste the FULL path to a real image on your computer below
# Example: "C:\Users\G SUDARSAN\Downloads\test-image.jpg"
$filePath = "D:\OneDrive\Pictures\Screenshots\prof.png"

$concurrentUsers = 10

Write-Host "🚀 Starting Load Test with $concurrentUsers concurrent users..."

# 2. Loop to create background jobs
1..$concurrentUsers | ForEach-Object {
    Start-Job -ScriptBlock {
        param($u, $f, $id)
        
        # Verify file exists inside the background job
        if (Test-Path $f) {
            # ✅ FIX: We use 'curl.exe' to bypass the PowerShell alias
            # We also added 2>&1 to capture any errors
            $response = & curl.exe -X POST -F "file=@$f" $u 2>&1
            return "User $id : Request Sent"
        } else {
            return "User $id : ❌ File not found at $f"
        }
    } -ArgumentList $url, $filePath, $_
}

# 3. Wait for all 'users' to finish
Write-Host "⏳ Waiting for responses..."
Get-Job | Wait-Job | Out-Null

# 4. Print results
Write-Host "✅ Test Complete. Results:"
Get-Job | Receive-Job
Remove-Job *