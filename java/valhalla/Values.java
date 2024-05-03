

// Value object.
value class VABC {
   long x;
   long y;
   VABC (long x, long y) {
      this.x = x;
      this.y = y;
   }
   long y() { return y; }
   long x() { return x; }
}

public class Values {
   public static void main(String [] args) {
       VABC obj1 = new VABC(10, 20);
       VABC obj2 = new VABC(10, 20);
       if (obj1 == obj2) {
          if (obj1.equals(obj2)) {
             System.out.println("Proved referential equality == which is same as .equals ");
          }
       } else if (obj1.equals(obj2)) {
          System.out.println("Proved value equality .equals ");
       } else {
          System.out.println("Proved non-equality ");
       }

       /*
        synchronized (obj1) {
       ^
  required: a type with identity
       synchronized (obj1) {
          System.out.println("Record object as monitor ");
       }
        */
       // Values are never sclarized when passed to OSR buffer.
       long res = 0;
       for (int i = 0; i < 10000; i++) {
           res += obj2.x() + obj2.y();
       }
       System.out.println("[obj2.x] " + obj2.x() + "  [obj2.y] " + obj2.y() + " [res]  " + res);
   }
}
