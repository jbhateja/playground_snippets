
import java.util.Arrays;
import java.lang.invoke.*;
import jdk.internal.misc.Unsafe;
import java.util.stream.IntStream;

interface func_ptr {
   public int apply (Object obj, int len, int val);
}

class RawAccess implements func_ptr {
   public static final Unsafe UNSAFE = Unsafe.getUnsafe();
   
   public int apply(Object obj, int len, int val) {
      int res =  0;      
      for (int i = 0; i < len; i++) {
          UNSAFE.putInt(obj, 16 + i * Integer.BYTES, val);
      }
      for (int i = 0; i < len; i++) {
          res += UNSAFE.getInt(obj, 16 + i * Integer.BYTES);
      }
      return res;
   }
}

class ArrAccess implements func_ptr {
   public int apply(Object obj, int len, int val) {
      int [] arr = (int[])obj;
      int res = 0;
      for (int i = 0; i < len; i++) {
         arr[i] = val; 
      }
      for (int i = 0; i < len; i++) {
         res += arr[i];
      }
      return res;
   }
}

/*class VarHandleAccess implements func_ptr {
   public void setMemVarHandles(Object obj, int len, int val) {
      for (int i = 0; i < len; i++) {
          UNSAFE.put(obj, 16 + i * Integer.BYTES, val);
      }
   }
}*/

public class test_mem_access {
   public static void main(String [] args) {
      int algo = Integer.parseInt(args[0]);
      int size = Integer.parseInt(args[1]);
      int [] arr = IntStream.range(0, size).toArray();
      func_ptr call = null;
      switch (algo) {
         case 0 : call = new RawAccess();  break;
         case 1 : call = new ArrAccess();  break;
         default : assert false;
      }
      // Warmup 
      int res = 0;
      for (int i = 0; i < 100000; i++) {
         res += call.apply(arr, arr.length, 10);
      }
      long t1 = System.currentTimeMillis();
      for (int i = 0; i < 100000; i++) {
         res += call.apply(arr, arr.length, 10);
      }
      long t2 = System.currentTimeMillis();
      
      System.out.println("[time] " + (t2 - t1) + " ms  [res]  " + res); 
   }
}
