
#include <stdio.h>

long micro1(long src1, long src2, long cond1, long cond2) {
   long res = 0;
   asm volatile (
      "movq    %[SRC1],  %[RES]                 \n\t"
      "cmpq    $10,      %[COND1]               \n\t"
      "ccmplq   %{dfv=sf%} $20, %[COND2]        \n\t"
      "cmovlq  %[SRC2],  %[RES]                \n\t"
     : [RES]    "+r"(res)
     : [COND1]  "r"(cond1), 
       [COND2]  "r"(cond2), 
       [SRC1]   "r"(src1),
       [SRC2]   "r"(src2)
     : "cc"
   );
   return res;
}

long micro1_c(long src1, long src2, long cond1, long cond2) {
   long res = src1;
   if (cond1 < 10 && cond2 < 20) {
      res = src2;
   }
   return res;
}

long micro2(long src1, long src2, long cond1, long cond2) {
   long res = 0;
   asm volatile (
      "movq    %[SRC1],  %[RES]                 \n\t"
      "cmpq    $10,      %[COND1]               \n\t"
      "ccmplq   %{dfv=sf%} $20, %[COND2]        \n\t"
      "cmovgq  %[SRC2],  %[RES]                \n\t"
     : [RES]    "+r"(res)
     : [COND1]  "r"(cond1),
       [COND2]  "r"(cond2),
       [SRC1]   "r"(src1),
       [SRC2]   "r"(src2)
     : "cc"
   );
   return res;
}

long micro3(long src1, long src2, long cond1, long cond2) {
   long res = 0;
   asm volatile (
      "movq    %[SRC1],  %[RES]                 \n\t"
      "cmpq    $0,       %[COND1]               \n\t"
      "ccmplq   %{dfv=zf%} $20, %[COND2]        \n\t"
      "cmovlq  %[SRC2],  %[RES]                 \n\t"
     : [RES]    "+r"(res)
     : [COND1]  "r"(cond1),
       [COND2]  "r"(cond2),
       [SRC1]   "r"(src1),
       [SRC2]   "r"(src2)
     : "cc"
   );
   return res;
}

int main() {
   long dst = 10;
   printf("[res1] %ld\n", micro1(100, 200, 1, 2));
   printf("[res1] %ld\n", micro1_c(100, 200, 1, 2));
   printf("[res2] %ld\n", micro2(100, 200, 1, 2));
   return printf("[res3] %ld\n", micro3(100, 200, 1, 2));
}

// [1] https://godbolt.org/z/Ks47ev1oY
// [2] https://godbolt.org/z/YWf5nsv5j
