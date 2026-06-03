@echo off
setlocal
if "%JAVA_HOME%"=="" goto resolveJavaHome
if not exist "%JAVA_HOME%\bin\java.exe" goto resolveJavaHome
goto runMaven

:resolveJavaHome
for /f "tokens=2,* delims==" %%i in ('java -XshowSettings:properties -version 2^>^&1 ^| findstr /C:"java.home ="') do set "JAVA_HOME=%%j"
for /f "tokens=* delims= " %%i in ("%JAVA_HOME%") do set "JAVA_HOME=%%i"

:runMaven
mvn %*
set EXIT_CODE=%ERRORLEVEL%
endlocal & exit /b %EXIT_CODE%
