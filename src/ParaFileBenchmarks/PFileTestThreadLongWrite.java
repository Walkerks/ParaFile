package ParaFileBenchmarks;

import ParaFiles.PFile;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Walker on 11/8/2016.
 */
public class PFileTestThreadLongWrite extends Thread implements ThreadId{
    private static int ID_GEN = 0;

    private PFile pFile;
    private int id;
    private int numberOfTries;
    private String testString;
    private int bytesPerTestString;// = testString.getBytes(StandardCharsets.UTF_8).length;
    private long testTime;
    private int numWrites;



    public PFileTestThreadLongWrite(PFile pFile, int numWrites, String testString) {
        id = ID_GEN++;
        numberOfTries = 0;
        this.pFile = pFile;
        this.numWrites = numWrites;
        this.testString = testString;
        //testTime = time;
}

    @Override
    public void run() {
        long start;
        long end;
        bytesPerTestString = testString.getBytes(StandardCharsets.UTF_8).length;
        start = System.currentTimeMillis();
        //test goes here
        for(int i = 0; i < numWrites; i++){
            pFile.write(testString);
        }
        end = System.currentTimeMillis();
        testTime = end-start;
    }

    public double getBytesPerSecond(){
        return (bytesPerTestString * numWrites) / ((testTime/1000) * 1.0);
    }

    public int getThreadId(){
        return id;
    }
    public void setID_GEB(int start){
        ID_GEN = start;
    }
    private int getRand(){
        //http://stackoverflow.com/questions/363681/generating-random-integers-in-a-specific-range
        return ThreadLocalRandom.current().nextInt(0, 80000+1);
    }
}