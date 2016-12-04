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
            file = new PFile("C:/Users/Walker/GoogleDrive/VT/Third Year/test");
        } catch (IOException e) {
                e.printStackTrace();
        }
        file.write("doo, do, do");
        file.close();

    }
}
