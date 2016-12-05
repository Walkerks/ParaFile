package ParaFiles; /**
 * Created by Austin on 12/3/2016.
 * Modified by Walker Sensabaough on 12/4/2016
 */


import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import ParaFiles.ParaStructure.LineNode;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.io.FileWriter;
import java.nio.file.Files;

import java.io.File;
import java.io.IOException;


public class PFile {

    //LockFreeList<Integer> filename = new LockFreeList<>();
    private AtomicInteger lineCount = null;
    private ReentrantReadWriteLock closeLock = new ReentrantReadWriteLock();
    private ConcurrentHashMap<Integer, LineNode> fileMap = null;
    private final int numThreadsAvail = Runtime.getRuntime().availableProcessors();
    private final int hashLoadFactor = 16; //16 is the default
    private int hashInitCap = 10; //number of bins to start with, defaults to # of lines in an existing pfile
    private final String context = "/context.txt";
    private final String chunks = "/lineChunks/";
    private File contextFile = null;
    //Line storage
    private File chunksDir = null;
    //The file seen by the user
    private File topLevelDir = null;


    private void initNewFile() throws IOException{
        topLevelDir.mkdir();
        chunksDir.mkdir();
        contextFile.createNewFile();

    }

    //sets up the internal structure
    private void setup(){
        //read in the context File
        fileMap = new ConcurrentHashMap<>(hashInitCap, hashLoadFactor, numThreadsAvail);
        Scanner myReader = null;
        int numLines = 0;
        try{
            myReader= new Scanner(contextFile);
            if(myReader.hasNext()) {
                numLines = Integer.parseInt(myReader.nextLine());
                for (int i = 0; i < numLines; i++) {
                    String fileName = myReader.nextLine();
                    LineNode generatedNode = new LineNode(i, chunksDir.getAbsolutePath(), fileName);
                    fileMap.put(i, generatedNode);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //TODO: lines should be read in from the conxt file
        lineCount = new AtomicInteger(numLines);
    }

    public PFile(String fileName) throws IOException {
        topLevelDir = new File(fileName);
        //check to see if this is an exist file (cough folder)
        contextFile = new File(fileName + context );
        chunksDir = new File(fileName + chunks);
        if(!topLevelDir.isDirectory()){
            //If it's not we need to init the file
            initNewFile();
        }
        //Now that we know we have something that may be our file let's check

        if(!contextFile.isFile()){
            //Not our file, throw a generic exception
            throw new IOException();
        }
        //Now that we have an IDEA this is what we want, we'll assume everything else is correct
        setup();
    }
    public void open() {

    }

    public void close() {
        try {
            closeLock.writeLock().lock();
            saveContext();
            System.gc();
        } finally {
            closeLock.writeLock().unlock();
        }

    }

    private void saveContext(){
        FileWriter myWriter = null;
        try{
            myWriter = new FileWriter(contextFile);
            //make sure all the readers and writers are done
            int numLines = lineCount.get();
            myWriter.write(String.valueOf(numLines));
            for(int i = 0; i < numLines; i++){
                //get the node
                LineNode node = fileMap.get(i);
                //get the files name
                String nodeFileName = node.getCurrentFileLocation();
                //write the file name to the settings
                myWriter.write("\n");
                myWriter.write(nodeFileName);
            }
            //Make sure it's written to disk
            myWriter.flush();
            //close
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //reads the entire list
    public String read() {
        return null;
    }

    //reads a specific line
    public String read(int lineNumber) {
        try{
            closeLock.readLock().lock();
            //get the node with key lineNumber
            LineNode lineN = fileMap.get(lineNumber);
            //read the line
            return lineN.read();
        } finally {
            closeLock.readLock().unlock();
        }
    }

    //writes to the end of the file
    public int write(String content) {
        try{
            //yes, we do want the read  lock
            closeLock.readLock().lock();
            int newLineNumber = lineCount.getAndIncrement();
            //give the lines absolute paths, hopefully, it makes the OS happy and speedy
            LineNode newNode = new LineNode(newLineNumber, chunksDir.getAbsolutePath());
            //write the content
            newNode.write(content);
            //add it to the hash map so all the other threads can see it.
            fileMap.put(newLineNumber, newNode);
            return newLineNumber;
        } finally {
            closeLock.readLock().unlock();
        }
    }

    //writes to specific line
    public void write(String content, int lineNumber) throws IndexOutOfBoundsException {
        try{
            //Yes, we do want the read lock
            closeLock.readLock().lock();
            //get the number of lines we currently have
            int lineNum = lineCount.get();
            //check to see if their line number is above the number of lines we have
            if(lineNumber > lineNum-1){
                throw new IndexOutOfBoundsException();
            }
            //otherwise get the line
            LineNode lineToUpdate = fileMap.get(lineNumber);
            lineToUpdate.write(content);
        } finally {
            closeLock.readLock().unlock();
        }

    }
}
