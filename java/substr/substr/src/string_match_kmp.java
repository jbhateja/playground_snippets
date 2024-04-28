//
//  T = aabbaaaabbaaa
//  P = aabbaabb
//           ^
//           |
//

public class string_match_kmp {

    public static int [] tbl = null;

     public static int [] compute_prefix_tbl(String pattern) {
        if (tbl != null) {
            return tbl;
        }
        tbl = new int[pattern.length()];
        for (int i = 0; i < pattern.length(); i++) {
            for (int j = 0; j < i; j++) {
               if (pattern.substring(0, i).endsWith(pattern.substring(0, j))) {
                    tbl[i] = j;     
               }
            }
        }
        return tbl;
     }

     public static void print_prefix_tbl(String pattern, int [] tbl) {
        for (int i = 0; i < pattern.length(); i++) {
            System.out.println("tbl[" + pattern.charAt(i) + "] = " + tbl[i]);
        }
     }

     public static int kmp_string_match(String haystack, String needle) {
        int i = 0;
        int j = 0;
        tbl = compute_prefix_tbl(needle);

        // Text string is always hopped by a unit distance, its only the beginning 
        // pattern match character whose position is based on precomputed prefix table.
        byte [] needle_arr = needle.getBytes();
        byte [] haystack_arr = haystack.getBytes();
        while (i < haystack_arr.length) {
           if (needle_arr[j] == haystack_arr[i]) {
               while (j < needle_arr.length && (i + j) < haystack_arr.length && needle_arr[j] == haystack_arr[i+j]) {
                  j++;
               }
               if (j == needle_arr.length) {
                  return i;
               } 
               j = tbl[j];
           } else {
               j = 0;
           }
           i++;
        }
        return -1;
     }

     public static void main(String[] args) {
        int algo = Integer.parseInt(args[0]);

        String needle = "aabbaabbc";
        String haystack = "cccaabbaaabbaaabbaabbaabbaabbc";
        
        int res = 0;
        if (algo == 0 || algo == -1) {
            for (int i = 0; i < 1000000; i++) {
                res += kmp_string_match(haystack, needle);
            }
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                res += kmp_string_match(haystack, needle);
            }
            long t2 = System.currentTimeMillis();
            System.out.println("[time kmp] " + (t2-t1) + " ms  [res] " + res);
        } 
        if (algo == 1 || algo == -1) {
            res = 0;
            for (int i = 0; i < 1000000; i++) {
                res += haystack.indexOf(needle);
            }
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < 1000000; i++) {
                res += haystack.indexOf(needle);
            }
            long t2 = System.currentTimeMillis();
            System.out.println("[time baseline] " + (t2-t1) + " ms  [res] " + res);  
        }
     }
}