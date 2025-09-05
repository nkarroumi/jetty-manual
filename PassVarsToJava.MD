**Option 1: Set Environment Variable in Shell**  
  
- On Linux/macOS:  
`export API_URL=http://somehost:8081`  
`java -jar main-app.jar`
- On Windows CMD:  
`set API_URL=http://somehost:8081`  
`java -jar main-app.jar`  
This is the most common and 12-Factor App–friendly method.
  
  
**Option 2: Inline Environment Variable**  
- Linux/macOS:  
`API_URL=http://somehost:8081 java -jar main-app.jar`  
Useful in scripts and Docker containers.  
  

**Option 3: Use a Wrapper Script**  
- Windows (run-app.bat):  
`@echo off`  
`set API_URL=http://somehost:8081`  
`java -jar main-app.jar`  
- Linux/macOS (run-app.sh): 
`#!/bin/bash`  
`export API_URL=http://somehost:8081`  
`java -jar main-app.jar`  
Recommended for production VMs where operations teams manage startup.  
  
  
**Option 4: JVM System Property (Alternative)**  
You can pass properties using -D:  
`java -Dapi.url=http://somehost:8081 -jar main-app.jar`  
Then access in Java with: 
`String apiUrl = System.getProperty("api.url", "http://localhost:8081");`  
This uses JVM properties instead of environment variables.  
  
  
**Pro Tip: Use .env File for Local Development**  
Create a `.env` file: `API_URL=http://somehost:8081` Then load it before running:
`export $(cat .env | xargs)`  
`java -jar main-app.jar`  
Great for developers using different environments  
  
  
**What are JVM System Properties?**  
These are key-value pairs passed to the Java Virtual Machine at runtime using the `-D` flag. Example:
`java -Dapi.url=http://somehost:8081 -jar app.jar`  
You can access them in Java via:
`System.getProperty("api.url")`  
  

**What is JAVA_OPTS?**  
JAVA_OPTS is an environment variable conventionally used to store JVM options like `-Xmx`, `-D`,
`-XX`, etc. It is not interpreted by Java itself but is often included in launch scripts. Example: `export JAVA_OPTS="-Dapi.url=http://somehost:8081 -Xmx512m"`  
`java $JAVA_OPTS -jar app.jar`  
  
  
**Key Differences**  
The table below summarizes the key differences between the two:  

|      Feature        | -Dkey=value (System Property) | JAVA_OPTS (Environment Variable)   |  
|---------------------|-------------------------------|------------------------------------|  
|      Purpose        |   Set a system property       |Container for JVM options           |    
| Java understands it?|              Yes              | No (requires manual inclusion)     |   
|   Access in code?   |   System.getProperty("key")   | Depends on included -D flags       |   
|       Scope         |          Runtime only         | Launch environment                 |  
|   Where it's used   |       java -Dkey=value ...    | java $JAVA_OPTS ... (or in scripts)|   
   
  
**When Should You Use Each?**  
- Use `-Dkey=value` when you control the launch command and want to configure one specific
application.  
- Use `JAVA_OPTS` when running in a scripted, containerized, or CI/CD environment
where you want default JVM behavior shared across multiple runs or apps.