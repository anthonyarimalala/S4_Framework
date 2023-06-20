cd test-framework/WEB-INF/classes
    javac -cp ../lib/fw.jar -d . Emp.java
cd ../../..

cd framework/
    javac -d . *.java
    jar -cf fw.jar .
    move fw.jar "D:\ITU\S4\MrNaina\S4_Framework\test-framework\WEB-INF\lib"
cd ..

cd test-framework/
    jar -cf test-servlet.war *
    copy "test-servlet.war" "C:\Program Files\Apache Software Foundation\Tomcat 10.0\webapps\test-framework.war"
cd ..