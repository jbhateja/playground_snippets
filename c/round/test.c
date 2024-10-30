
#include <assert.h>
#include <stdio.h>
#include <string.h>
#include <stdbool.h>

int mxcsr_nearest = 0x1f80;
int mxcsr_down = 0x3f80;
int mxcsr_up = 0x5f80;
int mxcsr_truncate = 0x7f80;

float rounds(double src, int mode) {
   float dst = 0.0f;
   int* mxcsr = NULL;
   switch(mode) {
     case 0 : mxcsr = &mxcsr_nearest; break;
     case 1 : mxcsr = &mxcsr_down; break;
     case 2 : mxcsr = &mxcsr_up; break;
     case 3 : mxcsr = &mxcsr_truncate; break;
     default : assert(false);
   }
   asm volatile (
       "ldmxcsr %2        \n\t"
       "cvtsd2ss %1, %0   \n\t"
     : "=x"(dst)
     : "x"(src), "m"(*mxcsr)
     : "%xmm1" 
   );
   return dst;
}

int main(int argc, char* argv[]) {
  float dst = 0.0f;
  double src = 3.4028233649732406E38; // 0x1.fffffDP+127  => 127 exp in FP32 exp ranges. => exact src needs 25 bits precision 
                                      // which is greater than 24 bits precision of FP32. Rounding scenario.
                                      // If EXP > 127 then clearly an Inf values.
  fprintf(stdout, "RC = 00 (nearest), src = %lf ,  dst = %f \n", src, rounds(src, 0)); // Last 4 bits 110 1 (only LSB is extra) 
                                                                                       // nextup : 111 0 , nextdown : 101 0
  fprintf(stdout, "RC = 01 (down),    src = %lf ,  dst = %f \n", src, rounds(src, 1));
  fprintf(stdout, "RC = 10 (up),      src = %lf ,  dst = %f \n", src, rounds(src, 2));
  fprintf(stdout, "RC = 11 (truncate) src = %lf ,  dst = %f \n", src, rounds(src, 3));
  return 0;
} 

/*
    PROMPT>./a.out
    RC = 00 (nearest), src = 340282336497324057985868971510891282432.000000 ,  dst = 340282326356119256160033759537265639424.000000
    RC = 01 (down),    src = 340282336497324057985868971510891282432.000000 ,  dst = 340282326356119256160033759537265639424.000000
    RC = 10 (up),      src = 340282336497324057985868971510891282432.000000 ,  dst = 340282346638528859811704183484516925440.000000
    RC = 11 (truncate) src = 340282336497324057985868971510891282432.000000 ,  dst = 340282326356119256160033759537265639424.000000
    PROMPT>
    PROMPT># RC 00 => DST = 1111111011111111111111111111110
    PROMPT># RC 01 => DST = 1111111011111111111111111111110
    PROMPT># RC 10 => DST = 1111111011111111111111111111111
    PROMPT># RC 11 => DST = 1111111011111111111111111111110

jshell> Double.doubleToRawLongBits(0x1.fffffDP+127)
$18 ==> 5183643170298134528

        // Normalized Notation:-
jshell> // 0 10001111110  1111,1111,1111,1111,1111,110 1,0000000000000000000000000000

jshell> Float.floatToRawIntBits((float)0x1.fffffDP+127)
$19 ==> 2139095038

jshell> // 0 11111110 1111,1111,1111,1111,1111,110 

jshell> Float.floatToRawIntBits(Math.nextDown((float)0x1.fffffDP+127))
$20 ==> 2139095037

jshell> // 0 11111110 1111,1111,1111,1111,1111,101

jshell> Float.floatToRawIntBits(Math.nextUp((float)0x1.fffffDP+127))
$21 ==> 2139095039

jshell> // 0 11111110 1111,1111,1111,1111,1111,111


 Theory: 

Rounding Mode RC Field
Setting
Description
Round to
nearest (even)
00B Rounded result is the closest to the infinitely precise result. If two values are equally close, the
result is the even value (that is, the one with the least-significant bit of zero). Default
Round down
(toward −∞)
01B Rounded result is closest to but no greater than the infinitely precise result.
Round up
(toward +∞)
10B Rounded result is closest to but no less than the infinitely precise result.
Round toward
zero (Truncate)
11B Rounded result is closest to but no greater in absolute value than the infinitely precise result.

The round up and round down modes are termed directed rounding and can be used to implement interval arithmetic.
Interval arithmetic is used to determine upper and lower bounds for the true result of a multistep computation,
when the intermediate results of the computation are subject to rounding.
The round toward zero mode (sometimes called the “chop” mode) is commonly used when performing integer
arithmetic with the x87 FPU.
The rounded result is called the inexact result. When the processor produces an inexact result, the floating-point
precision (inexact) flag (PE) is set (see Section 4.9.1.6, “Inexact-Result (Precision) Exception (#P)”).
The rounding modes have no effect on comparison operations, operations that produce exact results, or operations
that produce NaN results.
*/
