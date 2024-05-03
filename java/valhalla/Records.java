
// Identity objects.
record ABC (long x, long y) {}

public class Records {
   public static void main(String [] args) {
       ABC obj1 = new ABC(10, 20);
       ABC obj2 = new ABC(10, 20);
       
       if (obj1 == obj2) {
          System.out.println("Proved referential equality == ");
       } else if (obj1.equals(obj2)) {
          System.out.println("Proved value equality .equals ");
       } else {
          System.out.println("Proved non-equality ");
       }

       synchronized (obj1) {
          System.out.println("Record object as monitor ");
          long res = 0;
          for (int i = 0; i < 10000; i++) {
              res += obj2.x() + obj2.y();
          }
          System.out.println("[obj2.x] " + obj2.x() + "  [obj2.y] " + obj2.y());
       }
   }
}
