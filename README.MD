MANUAL COMPILE AND RUN INSTRUCTIONS (JETTY 12.0.25 SERVER)  
1. Project Structure  
---------------------
my-jetty-app/  
- lib/ (Jetty JARs from Jetty 12.0.25 server)  
- src/com/example/ (Java source files)  
- resources/ (HTML page)  
- classes/ (Output from compilation)  
- MANIFEST.MF (Jar config)  

2. Prepare Jetty JARs  
----------------------
From your Jetty 12..25 server zip file, copy the following JARs into 'lib/':  
- jetty-session-12.0.25.jar  
- jakarta.servlet-api-6.0.0.jar  
- jetty-server-12.0.25.jar  
- jetty-ee10-servlet-12.0.25.jar  
- jetty-util-12.0.25.jar  
- jetty-http-12.0.25.jar  
- jetty-io-12.0.25.jar  
- jetty-security-12.0.25.jar  
(Optional)  
- jetty-slf4j-impl-12.0.25.jar  
- slf4j-api-2.0.17.jar  
  
3. Compile  
-----------  
javac -cp "lib/*" -d classes src/com/example/*.java  
  
  
4. Create Manifest (in project root)
-------------------------------------
Main-Class: com.example.Main  
Class-Path: lib/jetty-session-12.0.25.jar lib/jakarta.servlet-api-6.0.0.jar lib/jetty-security-12.0.25.jar lib/jetty-server-12.0.25.jar lib/jetty-ee10-servlet-12.0.25.jar lib/jetty-util-12.0.25.jar lib/jetty-http-12.0.25.jar lib/jetty-io-12.0.25.jar  lib/jetty-slf4j-impl-12.0.25.jar lib/slf4j-api-2.0.17.jar 
     
(all JARs listed space-separated on one line. Leave one empty line at the bottom)  

5. Create Runnable JAR    
jar -cvfm app.jar MANIFEST.MF -C classes . -C resources .

6. Run the App    
java -jar app.jar  

7. Visit    
http://localhost:8081