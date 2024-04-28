
import jdk.incubator.vector.*;
import java.util.Arrays;

public class naive_string_match {
    public static final VectorSpecies<Byte> BSP = ByteVector.SPECIES_512;

    public static int naive_all_chars_different(byte [] haystack, byte [] needle) {
        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] == needle[0]) {
               int k = 1;
               for (; k < needle.length; k++) {
                   if (haystack[i + k] != needle[k])
                      break;
               }
               if (k == needle.length) return i;
               
            }
        }
        return -1;
    }

    public static int all_chars_different_opt(byte [] haystack, byte [] needle) {
        for (int i = 0, hop = 1; i < haystack.length; i += hop) {
            hop = 1;  // Causes performance degradation on account of increased path length.
            if (haystack[i] == needle[0]) {
               int k = 1;
               for (; k < needle.length; k++) {
                   if (haystack[i + k] != needle[k]) {
                      break;
                   }
               }
               if (k == needle.length) return i;
               hop = needle.length;
            }
        }
        return -1;
    }
    public static int all_chars_different_vec(byte [] haystack, byte[] needle) {
        VectorMask<Byte> nmask = VectorMask.fromLong(BSP, (1L << needle.length) - 1);
        ByteVector nvec = ByteVector.fromArray(BSP, needle, 0, nmask);
        ByteVector fvec = ByteVector.broadcast(BSP, needle[0]);
        int i = 0;
        for (; i < BSP.loopBound(haystack.length); i += BSP.length()) {
            VectorMask<Byte> mask = ByteVector.fromArray(BSP, haystack, i)
                                              .compare(VectorOperators.EQ, fvec);
            int truecount = mask.trueCount();
            while (truecount > 0) {
                int k = 1;
                int fidx = mask.firstTrue();
                for(; k < needle.length; k++) {
                   if (needle[k] != haystack[k + i + fidx]) {
                       break;
                   }    
                }
                if (k == needle.length)  return i + fidx;
                mask = mask.and(VectorMask.fromLong(BSP, ~(1 << fidx))); 
                truecount--;
            }
        }
        // Tail handling.
        for (; i < haystack.length; i += needle.length) {
            if (needle[0] == haystack[i]) {
               int k = 1;
               for (; k < needle.length; k++) {
                   if (haystack[i + k] != needle[k])
                      break;
               }
               if (k == needle.length) return i;
            }
        }
        return -1;
    }
    public static void main(String [] args) {
        int algo = Integer.parseInt(args[0]);
        int haystacksize = Integer.parseInt(args[1]);
        int needleidx = Integer.parseInt(args[2]);
        byte [] needle = "123456789zxcvbnm".getBytes();
        byte [] haystack = new byte[haystacksize];
        Arrays.fill(haystack, (byte)'a'); 
        System.arraycopy(needle, 0, haystack, needleidx, needle.length -1);
        System.arraycopy(needle, 0, haystack, needleidx + 64, needle.length);
        if (algo == 0 || algo == -1) {
            int res = 0;
            for (int i = 0; i < 100000; i++) {
               res += naive_all_chars_different(haystack, needle); 
            }
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
               res += naive_all_chars_different(haystack, needle); 
            }
            long t2 = System.currentTimeMillis();
            System.out.println("[time ACD NAIVE] " + (t2-t1) + " ms  [res] " + res);
        }
        if (algo == 1 || algo == -1) {
            int res = 0;
            for (int i = 0; i < 100000; i++) {
               res += all_chars_different_opt(haystack, needle); 
            }
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
               res += all_chars_different_opt(haystack, needle); 
            }
            long t2 = System.currentTimeMillis();
            System.out.println("[time ACD OPT] " + (t2-t1) + " ms  [res] " + res);
        }
        if (algo == 2 || algo == -1) {
            int res = 0;
            for (int i = 0; i < 100000; i++) {
               res += all_chars_different_vec(haystack, needle); 
            }
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
               res += all_chars_different_vec(haystack, needle); 
            }
            long t2 = System.currentTimeMillis();
            System.out.println("[time ACD VEC] " + (t2-t1) + " ms  [res] " + res);
        }
    }     
}
