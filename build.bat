@echo off
REM === Compile ===
javac -cp "lib/*" -d classes src/com/example/*.java

REM === Package into JAR ===
jar -cvfm app.jar MANIFEST.MF -C classes . -C resources .

REM === Run ===
echo Starting Jetty app...
java -jar app.jar
