
import java.lang.foreign.*;
import java.util.stream.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

public class mem_segments_fork_join {
   public int sindex;
   public int eindex;
   public int val;

   public static MemorySegment ms = null;
   public static Arena arena = Arena.ofConfined();

   public static class MyTask extends RecursiveTask<Integer> {
       MemorySegment ms;
       int sindex;
       int eindex; 
       int val;
        
       public MyTask(MemorySegment ms, int sindex, int eindex, int val) {
           this.ms = ms;
           this.sindex = sindex;
           this.eindex = eindex;
           this.val = val;
       }       

       public Integer compute() {
          System.out.println(" [thread] " + Thread.currentThread() + " sindex = " + sindex + " eindex = " + eindex);
          if (sindex >= eindex) {
              return 0;
          } else if (sindex == (eindex - 1)) {
              ms.setAtIndex(ValueLayout.JAVA_INT, sindex, val);   
              ms.setAtIndex(ValueLayout.JAVA_INT, eindex, val);   
              return 1;
          } else {
              int mid = (sindex + eindex) >> 1;
              MyTask ltask = new MyTask(ms, sindex, mid - 1, val);
              ltask.fork();
              MyTask rtask = new MyTask(ms, mid, eindex, val);
              rtask.fork();
              return ltask.join() + rtask.join();
          }
       }
   }

   // Confined memory segments are only accessible by
   // thread allocating the segment.
   public static void setup(int size) {
       ms = arena.allocate(size << 2, 64);
   }

   public static void finish() {
       arena.close();
   }

   public static void main(String [] args) throws Exception {
       int size = Integer.parseInt(args[0]);

       setup(size);

       ForkJoinPool pool = new ForkJoinPool(2);
       ForkJoinTask task = new MyTask(ms, 0, size, 10);
       pool.submit(task);
       pool.close();

       IntStream.range(0, size).forEach( i ->  { System.out.println(ms.getAtIndex(ValueLayout.JAVA_INT, i)); });
       finish();
   }
}
