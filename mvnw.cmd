@echo off  
if not "%MAVEN_PROJECTBASEDIR%"=="" goto endOfInit  
set MAVEN_PROJECTBASEDIR=%~dp0  
:endOfInit  
java -cp ".mvn\wrapper\maven-wrapper.jar" "-Dmaven.home=%MAVEN_HOME%" "-Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %* 
