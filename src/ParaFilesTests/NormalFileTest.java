package ParaFilesTests;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Austin on 12/5/2016.
 */
public class NormalFileTest {
    public static void main (String[] args) {
        String content = "Go Hokies!\n";
        String file = "./NormalFileTest.txt";
        int loops = 80000;
        long startTime = System.currentTimeMillis();
        writeToFile(content, file, loops);
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);
        System.out.println("Execution time: " + duration);
    }

    private static void writeToFile(String content, String fileName, int loops) {

        Path file = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(file)){
            for(int i = 0; i < loops; i++) {
                writer.write(content);
            }
        } catch (IOException x) {
            System.out.println("error writing to file");
        }
    }
}
