package ParaStructure;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import FileAccess.FileDOD;

/**
 * Created by Walker on 12/3/2016.
 */
public class LineNode {
    AtomicReference<FileDOD> currFile;
    //FileDOD fileRef = null;
    AtomicInteger verisonNumber;
    int lineNum;
    LineNode(int lineNum, String writeLocation, String readLocation){
        this.lineNum = lineNum;
        verisonNumber = new AtomicInteger(0);
    }

    public void write(String contents){
        //create a new file name
        String newFileName = String.valueOf(lineNum) + String.valueOf(verisonNumber.getAndIncrement());
        FileDOD newFile = new FileDOD(newFileName);
        newFile.write(contents);
    }

    public String read(){
        FileDOD copyFileRef = currFile.get();
        return copyFileRef.read();
    }
}
