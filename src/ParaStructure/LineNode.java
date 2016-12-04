package ParaStructure;


import java.util.concurrent.atomic.AtomicReference;
import FileAccess.FileDOD;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Walker on 12/3/2016.
 */
public class LineNode {
    AtomicReference<FileDOD> currFile;
    ConcurrentLinkedQueue<FileDOD> writeQueue;
    Lock writeLock = new ReentrantLock();
    Condition writeCondition = writeLock.newCondition();
    String fileLocation = null;
    //FileDOD fileRef = null;
    int lineNum;

    LineNode(int lineNum, String fileLocation){
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
        //create a new file name
        UUID uuid = UUID.randomUUID();
        String lineName = uuid.toString();
        //Create the new file
        FileDOD newFile = new FileDOD(fileLocation + lineName);
        //put myself in the queue to establish a write order
        writeQueue.add(newFile);
        //write to the file
        newFile.write(contents);
        //Wait until this thread is the first in the queue
        while(newFile != writeQueue.peek()) {
            try {
                writeCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        currFile.set(newFile);
        writeQueue.poll();
        //tell all the other threads you're done
        writeCondition.notifyAll();


    }

    //Reads the current file
    public String read(){
        FileDOD copyFileRef = currFile.get();
        return copyFileRef.read();
    }
}
