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
    private static final int MAX_THREAD_COUNT = 16;
    //Needs to be even
    private static final int NUMBER_WRITES_PER_THREAD = 10000;
    private static final int BYTES_PER_THREAD = 4096;
    private static final String PFILEPATH = "C:/Users/Walker/GoogleDrive/VT/Third Year/test20";
    public static void main(String[] args) {
        String testString = "";
        PFile file = null;
        //open a new file
        try {
            //create our PFile
            file = new PFile(PFILEPATH);
        } catch (IOException e) {
                e.printStackTrace();
        }
        long totalBytesPerSecond = 0;
        int THREAD_COUNT = 8;
        for(int i = 0; i < BYTES_PER_THREAD ; i++ ){
            testString = testString + "d";
        }

        PFileTestThreadLongWrite[] threads = new PFileTestThreadLongWrite[THREAD_COUNT];

        for(int t=0; t<THREAD_COUNT; t++) {
            threads[t] = new PFileTestThreadLongWrite(file, NUMBER_WRITES_PER_THREAD, testString);
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
        totalBytesPerSecond = 0;

        PFileTestThreadLongRead[] threadsP = new PFileTestThreadLongRead[THREAD_COUNT];
        int index1 = 0;
        int index2 = -1;
        for(int t=0; t<THREAD_COUNT; t++) {
            index2 = index2 + NUMBER_WRITES_PER_THREAD;
            threadsP[t] = new PFileTestThreadLongRead(file, index1, index2, BYTES_PER_THREAD );
            index1 = index2+1;
        }


        for(int t=0; t<THREAD_COUNT; t++) {
            threadsP[t].start();
        }


        for(int t=0; t<THREAD_COUNT; t++) {
            try {
                threadsP[t].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println(threads[t].getElapsedTime());
            totalBytesPerSecond += threadsP[t].getBytesPerSecond();
        }
        threads[0].setID_GEB(0);
        System.out.println( " Read at " + String.valueOf(totalBytesPerSecond) + " bytes per second with " +
                String.valueOf(THREAD_COUNT) + " threads");

        totalBytesPerSecond = 0;

        for(THREAD_COUNT = 2; THREAD_COUNT <= MAX_THREAD_COUNT; THREAD_COUNT = THREAD_COUNT + 2 ) {
            totalBytesPerSecond = 0;
            PFileTestThreadReadandWrite[] threadsRWT = new PFileTestThreadReadandWrite[THREAD_COUNT];

            for(int t=0; t<THREAD_COUNT; t++) {
                threadsRWT[t] = new PFileTestThreadReadandWrite(file, 5000, testString, 79999);
            }

            for(int t=0; t<THREAD_COUNT; t++) {
                threadsRWT[t].start();
            }


            for(int t=0; t<THREAD_COUNT; t++) {
                try {
                    threadsRWT[t].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //System.out.println(threads[t].getElapsedTime());
                totalBytesPerSecond += threadsRWT[t].getBytesPerSecond();
            }
            threadsRWT[0].setID_GEB(0);
            System.out.println( " Read Writes at " + String.valueOf(totalBytesPerSecond) + " bytes per second with " +
                    String.valueOf(THREAD_COUNT) + " threads");
        }

        file.close();
        System.gc();

    }
}
