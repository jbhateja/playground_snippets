
#include <stdio.h>

long micro1(long src1, long src2, long cond1, long cond2) {
   long res = 0;
   asm volatile (
      "movq    %[SRC1],  %[RES]                 \n\t"
      "cmpq    $10,      %[COND1]               \n\t"
      "ccmpnlq   %{dfv=sf%} %[COND2], %[COND2]  \n\t"
      "cmovzq  %[SRC2],  %[RES]                 \n\t"
     : [RES]    "=r"(res)
     : [COND1]  "r"(cond1), 
       [COND2]  "r"(cond2), 
       [SRC1]   "r"(src1),
       [SRC2]   "r"(src2)
     : "cc"
   );
   return res;
}

long micro2(long src1, long src2, long cond1, long cond2) {
   long res = 0;
   asm volatile (
      "movq    %[SRC1],  %[RES]                 \n\t"
      "cmpq    $10,      %[COND1]               \n\t"
      "ccmpleq   %{dfv=sf%} %[COND2], %[COND2]  \n\t"
      "cmovzq  %[SRC2],  %[RES]                 \n\t"
     : [RES]    "=r"(res)
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
   return printf("[res2] %ld\n", micro2(100, 200, 1, 2));
}

// [1] https://godbolt.org/z/Ks47ev1oY
// [2] https://godbolt.org/z/YWf5nsv5j
