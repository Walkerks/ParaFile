/**
 * Created by Austin on 12/3/2016.
 * Modified by Walker Sensabaough on 12/4/2016
 */

import Lists.LockFreeList;
import java.util.concurrent.ConcurrentHashMap;
import ParaStructure.LineNode;

import java.io.File;
import java.io.IOException;


public class PFile<T> {

    //LockFreeList<Integer> filename = new LockFreeList<>();
    ConcurrentHashMap<Integer, LineNode> fileMap = null;
    private final int numThreadsAvail = Runtime.getRuntime().availableProcessors();
    private final int hashLoadFactor = 16; //16 is the default
    private int hashInitCap = 10; //number of bins to start with, defaults to # of lines in an existing pfile
    private final String context = "/context.sd";
    private final String chunks = "/lineChunks/";
    File contextFile = null;
    //Line storage
    File chunksDir = null;
    //The file seen by the user
    File topLevelDir = null;


    private void initNewFile() throws IOException{
        chunksDir.mkdir();
        contextFile.createNewFile();
    }

    //sets up the internal structure
    private void setup(){
        //read in the context File
        fileMap = new ConcurrentHashMap<>(hashInitCap, hashLoadFactor, numThreadsAvail);
    }

    public PFile(String fileName) throws IOException {
        topLevelDir = new File(fileName);
        //check to see if this is an exist file (cough folder)
        if(!topLevelDir.isDirectory()){
            //If it's not we need to init the file
            initNewFile();
        }
        //Now that we know we have something that may be our file let's check
        contextFile = new File(fileName + context );
        chunksDir = new File(fileName + chunksDir);
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

    public void read(T content) {

    }

    public void read(T content, int lineNumber) {

    }

    public void write(T content) {

    }

    public void write(T content, int lineNumber) {


    }
}
