
class Dummy {
   public int field1;
   public int field2;
   Dummy(int f1, int f2) {
      field1 = f1;
      field2 = f2;
   }
}

public class test_inst {
   public static int micro(Dummy obj, int val) {
       obj.field1 = val;
       obj.field2 = 40;
       return obj.field1;
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
