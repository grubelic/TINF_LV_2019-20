package hr.fer.tinf.lab.zad3;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsoleUITest {

    /**
     * Test that runs ConsoleUI's main() and redirects input from files in
     * test/resources/in directory. Compares the output with files in
     * test/resources/out. In case of mismatch, stores output to
     * test/resources/log.
     */
    @Test
    public void integrationTest() {
        final String testResDirInPath = "src/test/resources/in/";
        final String testResDirOutPath = "src/test/resources/out/";
        final String testResDirLogPath = "src/test/resources/log/";

        File testResDir = new File(testResDirInPath);
        File[] inputFiles = testResDir.listFiles();
        if (inputFiles != null) {
            for (File inputFile : inputFiles) {
                String fileName = inputFile.getName();
                File outputFile = new File(testResDirOutPath + fileName);
                if (!(inputFile.isFile() && inputFile.canRead()
                    && outputFile.exists() && outputFile.isFile()
                    && outputFile.canRead())
                ) {
                    continue;
                }
                File logFile = new File(testResDirLogPath + fileName);
                InputStream defaultIn = System.in;
                PrintStream defaultOut = System.out;
                PrintStream defaultErr = System.err;
                try (FileInputStream fis = new FileInputStream(inputFile);
                     PrintStream ps = new PrintStream(logFile)
                ){
                    logFile.createNewFile();
                    System.setIn(fis);
                    System.setOut(ps);
                    System.setErr(ps);
                    ConsoleUI.main();
                } catch (IOException ignored) {

                } finally {
                    System.out.flush();
                    System.err.flush();
                    System.setIn(defaultIn);
                    System.setOut(defaultOut);
                    System.setErr(defaultErr);
                }
                try {
                    String expected = String.join("\n",
                        Files.readAllLines(outputFile.toPath())).trim();
                    String actual = String.join("\n",
                        Files.readAllLines(logFile.toPath())).trim();
                    assertEquals(expected, actual);
                    logFile.deleteOnExit();
                } catch (IOException ioe) {
                    System.err.println(
                        "Error while testing output for " + fileName
                    );
                }
            }
        }
    }


}
