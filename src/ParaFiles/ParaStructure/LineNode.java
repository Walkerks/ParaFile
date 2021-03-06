package ParaFiles.ParaStructure;


import java.util.concurrent.atomic.AtomicReference;
import ParaFiles.FileAccess.FileDOD;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Walker on 12/3/2016.
 */
public class LineNode {
    private final AtomicReference<FileDOD> currFile;
    private ConcurrentLinkedQueue<FileDOD> writeQueue;
    private String fileLocation = null;
    //FileDOD fileRef = null;
    private int lineNum;

    public LineNode(int lineNum, String fileLocation){
        currFile = new AtomicReference<FileDOD>(null);
        setup(lineNum, fileLocation);
    }

    public LineNode(int lineNum, String fileLocation, String existingName){

        FileDOD ref = new FileDOD(fileLocation + "/" + existingName);
        currFile = new AtomicReference<FileDOD>(ref);
        setup(lineNum, fileLocation);
    }
    private void setup(int lineNum, String fileLocation){
        this.lineNum = lineNum;
        writeQueue = new ConcurrentLinkedQueue<FileDOD>();
        //Path to write location
        this.fileLocation = fileLocation;
    }

    public String getCurrentFileLocation() {
        FileDOD copyFileRef = currFile.get();
        return copyFileRef.getFileName();
    }

    public void write(String contents){
        FileDOD old = null;
        //create a new file name
        UUID uuid = UUID.randomUUID();
        String lineName = uuid.toString();
        //Create the new file
        FileDOD newFile = new FileDOD(fileLocation + "/" + lineName);
        //put myself in the queue to establish a write order
        writeQueue.add(newFile);
        //write to the file
        newFile.write(contents);
        //Wait until this thread is the first in the queue
        while (newFile != writeQueue.peek());
        old = currFile.get();
        currFile.set(newFile);
        writeQueue.poll();

        //mark the old file to delete itself onces all threads no longer have reference
        if(old != null){
            old.delOnGarb();
        }
    }

    //Reads the current file
    public String read(){
        FileDOD copyFileRef = currFile.get();
        return copyFileRef.read();
    }
}
