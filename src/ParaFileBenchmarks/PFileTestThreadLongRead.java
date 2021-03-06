package ParaFileBenchmarks;

import ParaFiles.PFile;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Walker on 12/5/2016.
 */
public class PFileTestThreadLongRead extends Thread implements ThreadId{
    private static int ID_GEN = 0;

    private PFile pFile;
    private int id;
    private String testString;
    private int bytesPerTestString;
    private long testTime;
    private int startLine;
    private int endLine;


    public PFileTestThreadLongRead(PFile pFile, int startLine, int endLine, int bytesPerTestString) {
        id = ID_GEN++;
        this.pFile = pFile;
        this.startLine = startLine;
        this.endLine = endLine;
        this.bytesPerTestString = bytesPerTestString;
    }

    @Override
    public void run() {
        long start;
        long end;
        start = System.currentTimeMillis();
        //test goes here
        for(int i = startLine; i <= endLine; i++){
            pFile.read(i);
        }
        end = System.currentTimeMillis();
        testTime = end-start;
    }

    public long getBytesPerSecond(){
        return (bytesPerTestString * (endLine-startLine + 1)) / ((testTime/1000));
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
