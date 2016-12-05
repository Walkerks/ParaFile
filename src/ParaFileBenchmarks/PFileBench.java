package ParaFileBenchmarks;

import ParaFiles.PFile;

import java.io.IOException;

/**
 * Created by Walker on 12/4/2016.
 */

//http://stackoverflow.com/questions/363681/generating-random-integers-in-a-specific-range
       /* for(int i=0; i<=50; i++) {
            randomNum = randGenerator.nextInt((99 - 0) + 1) + 0;
            myList.add(randomNum);
        }*/
//start the list half full
//

public class PFileBench {
    private static final int MAX_THREAD_COUNT = 64;
    public static void main(String[] args) {
        PFile file = null;
        //open a new file
        try {
            //create our PFile
            file = new PFile("C:/Users/Walker/GoogleDrive/VT/Third Year/test7");
        } catch (IOException e) {
                e.printStackTrace();
        }
        double totalBytesPerSecond = 0;
        int THREAD_COUNT = 8;

        PFileTestThreadLongWrite[] threads = new PFileTestThreadLongWrite[THREAD_COUNT];

        for(int t=0; t<THREAD_COUNT; t++) {
            threads[t] = new PFileTestThreadLongWrite(file);
        }

        for(int t=0; t<THREAD_COUNT; t++) {
            threads[t].start();
        }


        for(int t=0; t<THREAD_COUNT; t++) {
            try {
                threads[t].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println(threads[t].getElapsedTime());
            totalBytesPerSecond += threads[t].getBytesPerSecond();
        }
        threads[0].setID_GEB(0);
        System.out.println( " Wrote at " + String.valueOf(totalBytesPerSecond) + " bytes per second with " +
            String.valueOf(THREAD_COUNT) + " threads");



        /*
        for(int THREAD_COUNT = 2; THREAD_COUNT <= MAX_THREAD_COUNT; THREAD_COUNT = THREAD_COUNT + 2 ) {

        }*/

        file.close();
        System.gc();

    }
}
