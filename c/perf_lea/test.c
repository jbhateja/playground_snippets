
#include <chrono>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>

#define WITER 1000000000
#define MITER 1000000000
typedef long (* func_ptr)(void);

__attribute__((noinline))
long micro1() {
   long res = 0;
   long a = 10;
   long b = 10;
   asm volatile (
       "leaq (%1, %2, 8)    , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "leaq (%%rax, %2, 8) , %%rax \n\t"
       "movq %%rax, %0 \n\t"
     : "=r"(res)
     : "r"(a), "r"(b)
     : "%rax" 
   );
   return res;
}

__attribute__((noinline))
long micro2() {
   long res = 0;
   long a = 10;
   long b = 10;
   asm volatile (
       "movq %1, %%rax \n\t"
       "movq %2, %%rbx \n\t"
       "shlq $3, %%rbx \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "movq %%rax, %0 \n\t"
     : "=r"(res)
     : "r"(a), "r"(b)
     : "%rax" 
   );
   return res;
}

__attribute__((noinline))
long micro3() {
   long res = 0;
   long a = 10;
   long b = 10;
   asm volatile (
       "leaq 0x10(%1, %2, 8)    , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "leaq 0x10(%%rax, %2, 8) , %%rax \n\t"
       "movq %%rax, %0 \n\t"
     : "=r"(res)
     : "r"(a), "r"(b)
     : "%rax" 
   );
   return res;
}

__attribute__((noinline))
long micro4() {
   long res = 0;
   long a = 10;
   long b = 10;
   asm volatile (
       "movq %1, %%rax \n\t"
       "movq %2, %%rbx \n\t"
       "shlq $3, %%rbx \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "addq %%rbx, %%rax \n\t"
       "addq $0x10, %%rax \n\t"
       "movq %%rax, %0 \n\t"
     : "=r"(res)
     : "r"(a), "r"(b)
     : "%rax" 
   );
   return res;
}

long do_warmup(func_ptr func) {
   long res = 0;
   for (int i = 0 ; i < WITER; i++) {
      res += func();
   }
   return res;
} 

long do_measurement(const char * msg, func_ptr func) {
   long res = 0;
   auto start = std::chrono::system_clock::now();
   for (int i = 0 ; i < MITER; i++) {
      res += func();
   }
   auto stop = std::chrono::system_clock::now();
   std::chrono::duration<double> diff = stop - start;
   std::cout << "[time] " << msg << " : " << diff.count() <<  "s [res] " << res << std::endl;
   return res;
} 

int main(int argc, char * argv[]) {
   if (argc != 2) {
     fprintf(stderr, "Incorrect Arguments!\n");
     return -1;
   }
   long cres = 0;
   int algo = atoi(argv[1]);
   if (algo == 0 || algo == -1) {
      cres += do_warmup(micro1); 
      cres += do_measurement("micro1", micro1);
   }
   if (algo == 1 || algo == -1) {
      cres += do_warmup(micro2); 
      cres += do_measurement("micro2", micro2);
   }
   if (algo == 2 || algo == -1) {
      cres += do_warmup(micro3); 
      cres += do_measurement("micro3", micro3);
   }
   if (algo == 3 || algo == -1) {
      cres += do_warmup(micro4); 
      cres += do_measurement("micro4", micro4);
   }
   return cres;
}
