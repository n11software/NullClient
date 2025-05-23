@echo off
setlocal enabledelayedexpansion

set "LIBSREF=libsref"
set "OUTDIR=deps\libs"

echo Downloading latest libsref...
curl -L -o "%LIBSREF%" "https://n11.dev/libsref"
if errorlevel 1 (
    echo Failed to download libsref!
    exit /b 1
)

if not exist "%OUTDIR%" mkdir "%OUTDIR%"

for /f "usebackq tokens=1,2 delims=:" %%A in ("%LIBSREF%") do (
    set "FILENAME=%%A"
    set "CHECKSUM=%%B"
    set "URL=https://n11.dev/libs/!FILENAME!"
    set "OUTFILE=%OUTDIR%\!FILENAME!"

    echo Downloading !FILENAME!...
    curl -L -o "!OUTFILE!" "!URL!"
    if errorlevel 1 (
        echo Failed to download !FILENAME!
        del "!OUTFILE!" >nul 2>&1
        exit /b 1
    )

    for /f "delims=" %%C in ('certutil -hashfile "!OUTFILE!" SHA256 ^| find /i /v "SHA256" ^| find /i /v "certutil"') do (
        set "CALC=%%C"
        set "CALC=!CALC: =!"
    )

    if /i "!CALC!"=="!CHECKSUM!" (
        echo Checksum OK for !FILENAME!
    ) else (
        echo Checksum FAILED for !FILENAME!
        del "!OUTFILE!" >nul 2>&1
        exit /b 1
    )
)

echo All files downloaded and verified.
endlocal