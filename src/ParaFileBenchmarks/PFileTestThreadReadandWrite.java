package ParaFileBenchmarks;

import ParaFiles.PFile;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Walker on 12/5/2016.
 */
public class PFileTestThreadReadandWrite extends Thread implements ThreadId{
    private static int ID_GEN = 0;

    private PFile pFile;
    private int id;
    private int numberOfTries;
    private String testString;
    private int bytesPerTestString;// = testString.getBytes(StandardCharsets.UTF_8).length;
    private long testTime;
    private int readWrites;
    private int max_Index;



    public PFileTestThreadReadandWrite(PFile pFile, int readWrites, String testString, int max_Index) {
        id = ID_GEN++;
        numberOfTries = 0;
        this.pFile = pFile;
        this.readWrites = readWrites;
        this.testString = testString;
        this.max_Index = max_Index;
        //testTime = time;
    }

    @Override
    public void run() {
        long start;
        long end;
        bytesPerTestString = testString.getBytes(StandardCharsets.UTF_8).length;
        int randReadOrWrite = 0;
        start = System.currentTimeMillis();
        //test goes here
        for(int i = 0; i < readWrites; i++){
            randReadOrWrite = getRand();
            if(randReadOrWrite == 0){
                pFile.write(testString, getRandIndex());
            } else{
                pFile.read(getRandIndex());
            }

        }
        end = System.currentTimeMillis();
        testTime = end-start;
    }

    public long getBytesPerSecond(){
        return (bytesPerTestString * readWrites) / ((testTime/1000));
    }

    public int getThreadId(){
        return id;
    }
    public void setID_GEB(int start){
        ID_GEN = start;
    }
    private int getRandIndex(){
        return ThreadLocalRandom.current().nextInt(0, max_Index+1);
    }
    private int getRand(){
        //http://stackoverflow.com/questions/363681/generating-random-integers-in-a-specific-range
        return ThreadLocalRandom.current().nextInt(0, 1+1);
    }
}
