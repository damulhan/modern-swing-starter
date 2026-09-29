@echo off
setlocal
set "JAVA_HOME=C:\Users\damul\.jdks\jbr-21.0.11"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo ========================================
echo Starting Swing Modern Sample Application (Gradle Build)
echo Java: %JAVA_HOME%
echo ========================================

rem Gradle 빌드된 Shadow Jar 우선 실행, 없으면 Maven Jar 실행
if exist "%~dp0build\libs\swing-modern-sample-1.0.0.jar" (
    "%JAVA_HOME%\bin\java.exe" -jar "%~dp0build\libs\swing-modern-sample-1.0.0.jar"
) else if exist "%~dp0target\swing-modern-sample-1.0.0.jar" (
    "%JAVA_HOME%\bin\java.exe" -jar "%~dp0target\swing-modern-sample-1.0.0.jar"
) else (
    echo [ERROR] JAR file not found. Please run: .\gradlew.bat shadowJar
    pause
)

endlocal
