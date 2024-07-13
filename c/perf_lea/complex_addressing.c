
#include <chrono>
#include <iostream>
#include <stdlib.h>
#include <string.h>

__attribute__((noinline))
void micro1(int* dst, int* src, int length) {
   for (long i = 0; i < length; i += 64) {
      asm volatile(
          "movq %1, %%rsi \n\t"
          "movq %0, %%rdi \n\t"
          "movq %2, %%rdx \n\t"
          "vmovdqu64 0x0(%%rsi, %%rdx, 4), %%zmm1   \n\t"
          "vmovdqu64 %%zmm1, 0x0(%%rdi, %%rdx, 4)   \n\t"
          "vmovdqu64 0x40(%%rsi, %%rdx, 4), %%zmm1   \n\t"
          "vmovdqu64 %%zmm1, 0x40(%%rdi, %%rdx, 4)   \n\t"
          "vmovdqu64 0x80(%%rsi, %%rdx, 4), %%zmm1   \n\t"
          "vmovdqu64 %%zmm1, 0x80(%%rdi, %%rdx, 4)   \n\t"
          "vmovdqu64 0xC0(%%rsi, %%rdx, 4), %%zmm1   \n\t"
          "vmovdqu64 %%zmm1, 0xC0(%%rdi, %%rdx, 4)   \n\t"
       : "=r"(dst)
       : "r"(src), "r"(i)
       : "%rsi", "%rdi", "%zmm1"
      );
   }
}

__attribute__((noinline))
void micro2(int* dst, int* src, int length) {
   for (long i = 0; i < length; i += 64) {
      asm volatile(
          "movq %1, %%rsi \n\t"
          "movq %0, %%rdi \n\t"
          "movq %2, %%rdx \n\t"
          "leaq (%%rsi, %%rdx, 4), %%rax \n\t"
          "leaq (%%rdi, %%rdx, 4), %%rbx \n\t"
          "vmovdqu64 0x0(%%rax), %%zmm1   \n\t"
          "vmovdqu64 %%zmm1, 0x0(%%rbx)   \n\t"
          "vmovdqu64 0x40(%%rax), %%zmm1   \n\t"
          "vmovdqu64 %%zmm1, 0x40(%%rbx)   \n\t"
          "vmovdqu64 0x80(%%rax), %%zmm1   \n\t"
          "vmovdqu64 %%zmm1, 0x80(%%rbx)   \n\t"
          "vmovdqu64 0xC0(%%rax), %%zmm1   \n\t"
          "vmovdqu64 %%zmm1, 0xC0(%%rbx)   \n\t"
       : "=r"(dst)
       : "r"(src), "r"(i)
       : "%rsi", "%rdi", "%zmm1"
      );
   }
}

int main(int argc, char* argv[]) {
   if (argc != 3) {
      std::cerr << "Incorrect Arguments!" << std::endl;
      return -1;
   }
   int algo = atoi(argv[1]);
   int size = atoi(argv[2]);
   if ((size & 0x3F) != 0) {
      std::cerr << "Size must be multiple of 64" << std::endl;
      return -1;
   }
   int* src = new int[size];
   int* dst = new int[size];
   memset(src, 1, sizeof(int)* size);
   if (algo == 0 || algo == -1) {
      for (int i = 0; i < 100000; i++) {
          micro1(dst, src, size);
      } 
      auto start = std::chrono::system_clock::now();
      for (int i = 0; i < 10000; i++) {
          micro1(dst, src, size);
      } 
      auto stop = std::chrono::system_clock::now();
      std::chrono::duration<double> diff = stop - start;
      std::cout << "[time micro1] " << diff.count() << std::endl;
   }
   if (algo == 1 || algo == -1) {
      for (int i = 0; i < 100000; i++) {
          micro2(dst, src, size);
      } 
      auto start = std::chrono::system_clock::now();
      for (int i = 0; i < 10000; i++) {
          micro2(dst, src, size);
      } 
      auto stop = std::chrono::system_clock::now();
      std::chrono::duration<double> diff = stop - start;
      std::cout << "[time micro2] " << diff.count() << std::endl;
   }
   return 0;
}
