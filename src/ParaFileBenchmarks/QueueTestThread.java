package ParaFileBenchmarks;



import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Walker on 11/8/2016.
 */
public class QueueTestThread extends Thread implements ThreadId{
    private static int ID_GEN = 0;

    //private Queue<Integer> myQueue;
    private int id;
    private double elapsed;
    private int iter;
    private double perContains;
    private double perAdd;
    private double perRemove;
    private int numberOfTries;
    private int numberOfEnq;
    private int numberOfDeq;
    private int testTime;
    private CyclicBarrier barr;
    Timer myTimer = new Timer(true);
    private volatile boolean doAgain = true;
    private boolean tryAgain = true;
/*
    public QueueTestThread(Queue<Integer> aQueue, CyclicBarrier barr, int time) {
        id = ID_GEN++;
        numberOfTries = 0;
        this.myQueue = aQueue;
        testTime = time;
        this.barr = barr;


}*/

    @Override
    public void run() {
        long start;
        long end;
        long startTimer = System.currentTimeMillis();
        int randIndex;
        myTimer.schedule(new Jar(this), 2000);
        while (true) {
            //System.out.println("This should happen twice");

            while(doAgain){
                //test here

            }

            if (tryAgain) {
                tryAgain = false;
                doAgain = true;
                numberOfTries = 0;
                numberOfEnq = 0;
                numberOfDeq = 0;
                /*
                while(myQueue.size() != 0){
                    myQueue.deq();
                }
                try {
                    barr.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }*/
                myTimer.schedule(new Jar(this), testTime*1000);
                continue;
            }
            //We don't actually want to loop, just want to use continue
            break;
        }

        //numberOfTries = (numberOfTries*(((PCLH)lock).getPro()));
    }

    public int getThreadId(){
        return id;
    }
    public void setID_GEB(int start){
        ID_GEN = start;
    }
    private int getRand(){
        //http://stackoverflow.com/questions/363681/generating-random-integers-in-a-specific-range
        return ThreadLocalRandom.current().nextInt(0, 1+1);
    }

    public double getElapsedTime() {
        if(numberOfTries == 0){
            return 0;
        }
        return numberOfTries/(testTime*1.0);
    }
    public int getNumEnq(){
        return numberOfEnq;
    }

    public int getNumDeq(){
        return numberOfDeq;
    }
/*
    public double getContainsRatio(){
        return (numberOfContains*1.0)/numberOfTries;

    }*/
    public void breakMe(){
        doAgain = false;
    }
}

class Jar extends TimerTask {
    QueueTestThread runningThread;
    Jar(QueueTestThread test){
        super();
        runningThread = test;
    }
    @Override
    public void run() {
        runningThread.breakMe();
    }
}
