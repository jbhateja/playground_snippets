
import jdk.incubator.vector.ByteVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

public class test {
   public static final VectorSpecies<Byte> BSP = ByteVector.SPECIES_256;

   public int find_substring_vector(byte[] haystack, byte [] needle) {
       int idx = 0;
       VectorMask<Byte> nmask = VectorMask.fromLong(BSP, (1 << needle.length) - 1);
       ByteVector nvec = ByteVector.fromArray(BSP, needle, 0, nmask); 
       ByteVector first = ByteVector.broadcast(BSP, needle[0]);
       ByteVector last = ByteVector.broadcast(BSP, needle[needle.length - 1]);
       while (idx  + BSP.length() < haystack.length) {
           ByteVector segment1 = ByteVector.fromArray(BSP, haystack, idx);
           ByteVector segment2 = ByteVector.fromArray(BSP, haystack, idx + needle.length - 1);       
           VectorMask<Byte> mask = first.compare(VectorOperators.EQ, segment1)
                                        .and(last.compare(VectorOperators.EQ, segment2));
           int firstset = 0;
           int truecount = mask.trueCount();
           while (truecount != 0) {
               firstset = mask.firstTrue();
               if (BSP.length() - firstset < needle.length) {
                   break;
               } 
               // Special case matches for needle sizes..  
               if (nvec.compare(VectorOperators.EQ, ByteVector.fromArray(BSP, haystack, idx + firstset, nmask)).allTrue()) {
                   return idx + firstset;
               }
               mask = mask.and(VectorMask.fromLong(BSP, ~(1 << firstset)));
               truecount--;
           }
           idx += truecount != 0 ? firstset : BSP.length();
       }
       // Tail handling.
       return -1;
   } 

   public int find_substring(String haystack, String needle) {
       if (haystack.length() < needle.length()) {
           return -1;
       }
       if (needle.length() == 0) {
           return -1;
       }
       if (needle.length() < BSP.length()) {
           return find_substring_vector(haystack.getBytes(), needle.getBytes());
       }
       return haystack.indexOf(needle);
   }

   public static void main(String [] args) {
       int res = 0;
       test obj = new test();
       int algo = Integer.parseInt(args[0]);
       String haystack = "points in the specified text range of this String. The text range begins at the specified beginIndex and extends to the char at index endIndex - 1. Thus the length (in chars) of the text range is endIndex-beginIndex. Unpaired surrogates within the text range count as one code point each. If the char value specified at the given index is in the high-surrogate range, the following index is less than the length, returns the number of Unicode code points in the specified text range of this String. The text range begins at the specified beginIndex and extends to the char at index endIndex - 1. Thus the length (in chars) of the text range is endIndex-beginIndex. Unpaired surrogates within the text range count as one code point each. Returns the number of Unicode code points in the specified text range of this String. The text range begins at the specified beginIndex and extends to the char at index endIndex - 1. Thus the length (in chars) of the text range is endIndex-beginIndex. Unpaired surrogates within the text range count as one code point each. If the char value specified at the given index is in the high-surrogate range, the following index is less than the length of this String, and the char value at the following index is in the low-surrogate range, then the supplementary code point corresponding to this surrogate pair is returned. Otherwise, the char value at the given index is returned.";
       //String needle = "corresponding";
       String needle = "corresponding";
       if (algo == 0 || algo == -1) {
           for (int i = 0; i < 100000; i++) {
               res += obj.find_substring(haystack, needle); 
           }
           long t1 = System.currentTimeMillis(); 
           for (int i = 0; i < 100000; i++) {
               res += obj.find_substring(haystack, needle); 
           }
           long t2 = System.currentTimeMillis(); 
           System.out.println("[time vector] " + (t2-t1) + " ms   [res] " + res);
       } 
       if (algo == 1 || algo == -1) {
           res = 0;
           for (int i = 0; i < 100000; i++) {
               res += haystack.indexOf(needle); 
           }
           long t1 = System.currentTimeMillis(); 
           for (int i = 0; i < 100000; i++) {
               res += haystack.indexOf(needle); 
           }
           long t2 = System.currentTimeMillis(); 
           System.out.println("[time scalar] " + (t2-t1) + " ms   [res] " + res);
       }
   }
} 
