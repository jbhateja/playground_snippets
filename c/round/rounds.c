#include <assert.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>

float roundss(float src, int mode) {
   float dst = 0.0f;
   switch(mode) {
     case 0:
       asm volatile (
           "roundss $0, %1, %0   \n\t"
         : "=x"(dst)
         : "x"(src)
         :
       );
     break;
     case 1:
       asm volatile (
           "roundss $1, %1, %0   \n\t"
         : "=x"(dst)
         : "x"(src)
         :
       );
     break;
     case 2:
       asm volatile (
           "roundss $2, %1, %0   \n\t"
         : "=x"(dst)
         : "x"(src)
         :
       );
     break;
     default:
       asm volatile (
           "roundss $3, %1, %0   \n\t"
         : "=x"(dst)
         : "x"(src)
         :
       );
     break;
   }
   return dst;
}

int main(int argc, char* argv[]) {
  float dst = 0.0f;
  float src = 0.5f;
  fprintf(stdout, "RC = 00 (nearest), src = %lf ,  dst = %f \n", src, roundss(src, 0)); // Last 4 bits 110 1 (only LSB is extra)
  fprintf(stdout, "RC = 01 (down),    src = %lf ,  dst = %f \n", src, roundss(src, 1));
  fprintf(stdout, "RC = 10 (up),      src = %lf ,  dst = %f \n", src, roundss(src, 2));
  fprintf(stdout, "RC = 11 (truncate) src = %lf ,  dst = %f \n", src, roundss(src, 3));
  return 0;
}

/*
PROMPT>./a.out
RC = 00 (nearest), src = 8191.964844 ,  dst = 8192.000000
RC = 01 (down),    src = 8191.964844 ,  dst = 8191.000000
RC = 10 (up),      src = 8191.964844 ,  dst = 8192.000000
RC = 11 (truncate) src = 8191.964844 ,  dst = 8191.000000

RC = 00 (nearest), src = 0.500000 ,  dst = 0.000000
RC = 01 (down),    src = 0.500000 ,  dst = 0.000000
RC = 10 (up),      src = 0.500000 ,  dst = 1.000000
RC = 11 (truncate) src = 0.500000 ,  dst = 0.000000
PROMPT>jshell
|  Welcome to JShell -- Version 24-ea
|  For an introduction type: /help intro

jshell> Math.round(0.5f)
$1 ==> 1
*/
