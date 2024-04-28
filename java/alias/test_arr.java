

public class test_arr {
   public static int micro(int [] arr1, int [] arr2) {
       arr1[10] = 20;
       arr2[10] = 40;
       return arr1[10];
   }
   public static void main(String [] args) {
      int res = 0;
      int [] arr = new int[1024];
      for (int i = 0; i < 10000; i++) {
          res += micro(arr, arr);
      }
      long t1 = System.currentTimeMillis();
      for (int i = 0; i < 200000; i++) {
          res += micro(arr, arr);
      }
      long t2 = System.currentTimeMillis();
      System.out.println("[time] " + (t2-t1) + "ms  [res] " + res);
   }
}
