@echo off
REM === Clean up previous compile ===
rmdir /s /q classes
del api-app.jar
mkdir classes
REM === Compile ===
javac -cp "lib/*" -d classes src/com/api/*.java

REM === Package into JAR ===
jar -cvfm api-app.jar MANIFEST.MF -C classes . -C resources .

REM === Run ===
echo Starting Jetty app...
java -jar -Dorg.eclipse.jetty.LEVEL=DEBUG api-app.jar
