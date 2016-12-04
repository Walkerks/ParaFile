package ParaFilesTests
import ParaFiles.FileAccess.FileDOD

/**
 * Created by Walker on 12/3/2016.
 */
class FileDODTest extends groovy.util.GroovyTestCase {
    String fileName = "testFile.txt";

    void testCreation(){
        FileDOD myFile = new FileDOD(fileName);
        File checkFile = new File(fileName);
        assert(checkFile.exists());
    }
    void testWriteRead() {
        FileDOD myFile = new FileDOD(fileName);
        String testCont = "sldfkjslk  aaa hfisfs";
        myFile.write(testCont);
        assert(testCont.equals(myFile.read()));
    }
    void testGarbDelete(){
        FileDOD myFile = new FileDOD(fileName);
        myFile.delOnGarb();

        myFile = null;
        File checkFile = new File(fileName);
        System.gc();
        while(checkFile.exists()){
            //spin
        }
    }


}
