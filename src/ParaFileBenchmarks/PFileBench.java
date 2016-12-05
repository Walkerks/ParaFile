package ParaFileBenchmarks;

import ParaFiles.PFile;

import java.io.IOException;

/**
 * Created by Walker on 12/4/2016.
 */
public class PFileBench {

    public static void main(String[] args) {
        PFile file = null;
        try {
            //create our PFile
            file = new PFile("C:/Users/Walker/GoogleDrive/VT/Third Year/test5");
        } catch (IOException e) {
                e.printStackTrace();
        }
        for(int i = 0; i < 1; i++){
            file.write("doo, do, do");
        }
        for(int i = 0; i < 1; i++){
            file.write("doo, do, do", i);
        }

        file.close();
        System.gc();

    }
}
