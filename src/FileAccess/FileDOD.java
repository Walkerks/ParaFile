package FileAccess;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;

/**
 * Created by Walker on 12/3/2016.
 */
public class FileDOD {
    File myFile;
    boolean deleteFileOnGarbage = false;
    //Opens or creates a new file
    public FileDOD(String fileName){
        myFile = new File(fileName);
        if(!myFile.exists()){
            try {
                myFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void moveToRead(){

    }
    //read the file from disk
    public String read(){
        String theString = null;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(myFile.getPath()));
            theString = new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return theString;
    }
    //write the file to disk
    public void write(String content){
        FileWriter writer;

        try{
            writer = new FileWriter(myFile);
            writer.write(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Set the file to be deleted when the garbage collector comes along
    public void delOnGarb(){
        deleteFileOnGarbage = true;
    }

    public String getFileName(){
        return myFile.getName();
    }

    protected void finalize() throws Throwable {
        try {
            //If the file is marked to delete
            if (deleteFileOnGarbage){
                myFile.delete();
            }
        } finally {
            super.finalize();
        }
    }
}
