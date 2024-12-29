public class infer_cond_moves {
   public static int micro(int a, int b, int c, int d) {
       int res = 0;
       if (a == b) {
          res = c;
       }
       return res + d;
   }

   public static void main(String [] args) {
      int res = 0;
      int mask = Integer.parseInt(args[0]);
      for (int i = 0; i < 100000; i++) {
          res += micro(i, i & mask, i + 20, i + 30);
      }
      long t1 = System.currentTimeMillis();
      for (int i = 0; i < 100000; i++) {
          res += micro(i, i & mask, i + 20, i + 30);
      }
      long t2 = System.currentTimeMillis();
      System.out.println("[time] "  + (t2-t1) + "ms [res] " + res);
   }
}


/*
    Mask = 4095
        - CMoveI
        [time] 7ms [res] 1432902368
    Mask = 2047
        - No-CMoveI 
        - No Uncommon Trap
        [time] 22ms [res] 1420239584
    Mask = 1023
        - No-CMovI
        - Dopt due to Uncommon Trap
        [time] 31ms [res] 1417053920
*/
