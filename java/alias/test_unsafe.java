
import jdk.internal.misc.Unsafe;

class Dummy {
   public int field1;
   public int field2;
   Dummy(int f1, int f2) {
      field1 = f1;
      field2 = f2;
   }
}

public class test_unsafe {
   public static final Unsafe UNSAFE = Unsafe.getUnsafe();

   public static int micro(Dummy obj, int val) {
       UNSAFE.putInt(obj, 12, 20);
       UNSAFE.putInt(obj, 16, 40);
       return UNSAFE.getInt(obj, 12);
   }

   public static void main(String [] args) {
      int res = 0;
      Dummy obj = new Dummy(0, 0);
      for (int i = 0; i < 10000; i++) {
          res += micro(obj, i);
      }
      long t1 = System.currentTimeMillis();
      for (int i = 0; i < 200000; i++) {
          res += micro(obj, i);
      }
      long t2 = System.currentTimeMillis();
      System.out.println("[time] " + (t2-t1) + "ms  [res] " + res);
   }
}
