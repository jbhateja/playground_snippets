
class Node {
   private Node next;
   private int info;
   private int tid;

   public Node getNext() {
      return next;
   }
   public void setNext(Node next) {
      this.next = next;
   }

   public int getInfo() {
      return info;
   }

   public Node(int tid, int info, Node next) {
      this.tid = tid;
      this.next = next;
      this.info = info;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append(" [tid ")
        .append(tid)
        .append("] : [")
        .append(hashCode())
        .append(" , ")
        .append(info)
        .append(" , ")
        .append(next == null ? "null" : next.hashCode())
        .append("] \n"); 
      return sb.toString();
   }
}

class Logger {
  interface logging {
    public void log(Object ... objs); 
  }
  public static logging lobj = null;
  
  public static void init(boolean dump) { 
      lobj = dump ? 
          new logging() { 
             public void log(Object ... objs) {
                StringBuilder sb = new StringBuilder();
                for (var obj : objs) {
                  sb.append(obj);
                }
                sb.append("\n");
                System.out.println(sb.toString());
             }
          }      :
          new logging() { 
              public void log(Object ... objs) { }
          };
  } 
  public static void log(Object ... objs) { 
      lobj.log(objs); 
  } 
}

class List { 
    private Node head; 
    public synchronized Node getHead() { 
        return head; 
    }  
    public synchronized void setHead(Node head) { 
       this.head = head;
    }
    public void printList() { 
       Node itr = head; 
       while(itr != null) {
          Logger.log(itr); 
          itr = itr.getNext();
       } 
   } 
}

public class mt_list implements Runnable { 
   public static boolean debug = false;
   private int tid; 
   private int start; 
   private int end; 
   private static List link;
   
   public mt_list(int tid, int start, int end, List head) { 
      this.tid = tid;
      this.start = start; 
      this.end = end; 
      this.link = head;
   } 

   public void run() { 
      Logger.log("[tid ", tid, "] Entering thread."); 
      for (int i = start; i < end; i++) { 
         Node node = new Node(tid, i, null);
         node.setNext(link.getHead());
         link.setHead(node); 
         Logger.log("[tid ", tid, "] Adding node to unified list", node);
      }
   }

   public static void main (String [] args) { 
      int tcount = Integer.parseInt(args[0]); 
      int ncount = Integer.parseInt(args[1]); 
      boolean debug = Boolean.parseBoolean(args[2]); 
      Logger.init(debug);
      link = new List();

      mt_list [] tds = new mt_list[tcount]; 
      for (int i = 0; i < tcount; i++) {
         tds[i] = new mt_list(i + 1, i * ncount, i * ncount + ncount, link);
      } 
      try {
         Thread [] tids = new Thread[tcount]; 
         for (int i = 0; i < tcount; i++) { 
           tids[i] = new Thread(tds[i]); 
         }
         for (int i = 0; i < tcount; i++) { 
           tids[i].start();
         } 
         for (int i = 0; i < tcount; i++) { 
           tids[i].join();
         }
      } catch (Exception e) {
         System.out.println(e);
      }
      Logger.log(" Printing unified list ");
      link.printList();
  }
} 
