package ParaFileBenchmarks;

/**
 * Created by Walker on 9/26/2016.
 */
public class Exc extends Thread implements ThreadId {
    private static int ID_GEN = 0;
    private int id;
    public Exc() {
        id = ID_GEN++;
    }
    public int getThreadId(){
        return id;
    }

    protected void finalize(){
        v = false;
        x=0;
    }

    static int x = 0;
    static  boolean v = false;

    public void writer () {
        x = 42;
        v = true;
    }
    public void reader () {
        if ( v == true) {

            int y = 100/ x ;

            //System.out.println("Not zero");
        }

    }

    @Override
    public void run() {
        if(id % 2 == 0){
            //System.out.println("zero");

                reader();


        } else{
            //System.out.println("1");

                writer();


        }
    }
}
