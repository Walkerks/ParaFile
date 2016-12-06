package ParaFilesTests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Austin on 12/5/2016.
 */
public class NormalFileTest {
    public static void main (String[] args) {
        String content = "Go Hokies!\n";
        String newContent = "GOBBLE GOBBLE\n";
        String file = "./NormalFileTest.txt";
        int numLines = 80000;
        int totalReadWriteLoops = 160000;

        long writeStartTime = System.currentTimeMillis();
        writeToFile(content, file, numLines);
        long writeEndTime = System.currentTimeMillis();
        long readDuration = (writeEndTime - writeStartTime);
        System.out.println("Write Execution Time: " + readDuration);

        long readStartTime = System.currentTimeMillis();
        readFromFile(file);
        long readEndTime = System.currentTimeMillis();
        long writeDuration = (readEndTime - readStartTime);
        System.out.println("Read Execution Time: " + writeDuration);


        long readWriteStartTime = System.currentTimeMillis();
        readAndWrite(newContent, file, numLines, totalReadWriteLoops);
        long readWriteEndTime = System.currentTimeMillis();
        long readWriteDuration = (readWriteEndTime - readWriteStartTime);
        System.out.println("Read/Write Execution time: " + readWriteDuration);

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

    private static void readFromFile(String filename) {
        Path file = Paths.get(filename);
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line = null;
            while ((line = reader.readLine()) != null) {

            }
        } catch (IOException x) {
            System.out.println("could not read file.\n");
        }
    }

    private static void readAndWrite(String newContent, String filename, int numLines, int totalReadWriteLoops) {
        Path file = Paths.get(filename);
        Random rand = new Random();
        for(int i = 0; i < totalReadWriteLoops; i++) {
            int targetLine = rand.nextInt(numLines);
            int methodChoice = rand.nextInt(2);
            if (methodChoice == 0) {
                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    for (int j = 0; j < targetLine; j++) {
                        reader.readLine();
                    }
                } catch (IOException x) {
                    System.out.println("could not read file.\n");
                }
            } else {
                List<String> list = new ArrayList<String>();
                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        list.add(line);
                    }
                } catch (IOException x) {
                    System.out.println("could not read file.\n");
                }

                list.set(targetLine, newContent);

                try (BufferedWriter writer = Files.newBufferedWriter(file)) {
                    String line = null;
                    for (int j = 0; j < list.size(); j++) {
                        line = list.get(j);
                        writer.write(line);
                        writer.newLine();
                    }
                } catch (IOException x) {
                    System.out.println("could not write to file.\n");
                }
            }
        }
    }
}