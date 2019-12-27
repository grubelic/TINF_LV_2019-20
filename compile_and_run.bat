mkdir "target/main/java"
mkdir "target/test/java"
javac -d target/main/java src/main/java/hr/fer/tinf/lab/zad3/*.java
java -cp target/main/java hr.fer.tinf.lab.zad3.ConsoleUI
