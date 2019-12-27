mkdir -p target/main/java
mkdir -p target/test/java
javac -d target/main/java src/main/java/hr/fer/tinf/lab/zad3/*.java
javac -d target/test/java -cp "src/test/resources/*:target/main/java" src/test/java/hr/fer/tinf/lab/zad3/*.java
java -jar "src/test/resources/junit-platform-console-standalone-1.5.2.jar" -cp target/main/java -cp target/test/java --scan-classpath=target/test/java
