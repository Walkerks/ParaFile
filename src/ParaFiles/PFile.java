package ParaFiles; /**
 * Created by Austin on 12/3/2016.
 * Modified by Walker Sensabaough on 12/4/2016
 */


import java.util.concurrent.ConcurrentHashMap;
import ParaFiles.ParaStructure.LineNode;
import java.util.concurrent.atomic.AtomicInteger;
import java.nio.file.Files;

import java.io.File;
import java.io.IOException;


public class PFile {

    //LockFreeList<Integer> filename = new LockFreeList<>();
    private AtomicInteger lineCount = null;
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
        //TODO: lines should be read in from the conxt file
        lineCount = new AtomicInteger(0);
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

    }

    //reads the entire list
    public String read() {
        return null;
    }

    //reads a specific line
    public String read(int lineNumber) {
        //get the node with key lineNumber
        LineNode lineN = fileMap.get(lineNumber);
        //read the line
        return lineN.read();
    }

    //writes to the end of the file
    public int write(String content) {
        int newLineNumber = lineCount.getAndIncrement();
        //give the lines absolute paths, hopefully, it makes the OS happy and speedy
        LineNode newNode = new LineNode(newLineNumber, chunksDir.getAbsolutePath());
        //write the content
        newNode.write(content);
        //add it to the hash map so all the other threads can see it.
        fileMap.put(newLineNumber, newNode);
        return newLineNumber;
    }

    //writes to specific line
    public void write(String content, int lineNumber) throws IndexOutOfBoundsException {
        //get the number of lines we currently have
        int lineNum = lineCount.get();
        //check to see if their line number is above the number of lines we have
        if(lineNumber > lineNum){
            throw new IndexOutOfBoundsException();
        }
        //otherwise get the line
        LineNode lineToUpdate = fileMap.get(lineNumber);
        lineToUpdate.write(content);

    }
}
