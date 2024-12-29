public class infer_cond_moves {
   public static int micro(int a, int b, int c, int d) {
       int res = 0;
       // apx : if (a == b && c == d) {
       if (a == b) {
          res = c;
       }
       return res + d;
   }

   public static void main(String [] args) {
      int res = 0;
      int mask = Integer.parseInt(args[0]);
      for (int i = 0; i < 980000; i++) {
          res += micro(i, i & mask, i , i & mask);
      }
      long t1 = System.currentTimeMillis();
      for (int i = 0; i < 1000000; i++) {
          res += micro(i, i & mask, i , i & mask);
      }
      long t2 = System.currentTimeMillis();
      System.out.println("[time] "  + (t2-t1) + "ms [res] " + res);
   }
}


/*
    Mask = 4095
        - CMoveI
        [time] 7ms [res] 1432902368
    Mask = 2047
        - No-CMoveI 
        - No Uncommon Trap
        [time] 22ms [res] 1420239584
    Mask = 1023
        - No-CMovI
        - Dopt due to Uncommon Trap
        [time] 31ms [res] 1417053920
*/

/*
 Quick perf stats.
 ================

CompileCommand: PrintIdealPhase infer_cond_moves.micro const char* PrintIdealPhase = 'BEFORE_MATCHING'
AFTER: BEFORE_MATCHING
  0  Root  === 0 36  [[ 0 1 3 25 ]] inner
  3  Start  === 3 0  [[ 3 5 6 7 8 9 10 11 12 13 ]]  #{0:control, 1:abIO, 2:memory, 3:rawptr:BotPTR, 4:return_address, 5:int, 6:int, 7:int, 8:int}
  5  Parm  === 3  [[ 28 ]] Control !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
  6  Parm  === 3  [[ 36 ]] I_O !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
  7  Parm  === 3  [[ 36 ]] Memory  Memory: @BotPTR *+bot, idx=Bot; !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
  8  Parm  === 3  [[ 36 ]] FramePtr !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
  9  Parm  === 3  [[ 36 ]] ReturnAdr !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
 10  Parm  === 3  [[ 26 ]] Parm0: int !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
 11  Parm  === 3  [[ 26 ]] Parm1: int !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
 12  Parm  === 3  [[ 38 ]] Parm2: int !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
 13  Parm  === 3  [[ 37 38 ]] Parm3: int !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
 26  CmpI  === _ 10 11  [[ 27 28 ]]  !jvms: infer_cond_moves::micro @ bci:5 (line 5)
 27  Bool  === _ 26  [[ 28 ]] [ne] !jvms: infer_cond_moves::micro @ bci:5 (line 5)
 28  If  === 5 27 26  [[ 29 30 ]] P=0.924823, C=6784.000000 !jvms: infer_cond_moves::micro @ bci:5 (line 5)
 29  IfTrue  === 28  [[ 33 ]] #1 !jvms: infer_cond_moves::micro @ bci:5 (line 5)
 30  IfFalse  === 28  [[ 33 ]] #0 !jvms: infer_cond_moves::micro @ bci:5 (line 5)
 33  Region  === 33 30 29  [[ 33 36 37 ]] #reducible  !jvms: infer_cond_moves::micro @ bci:11 (line 8)
 36  Return  === 33 6 7 8 9 returns 37  [[ 0 ]]
 37  Phi  === 33 38 13  [[ 36 ]]  #int !orig=[35] !jvms: infer_cond_moves::micro @ bci:14 (line 8)
 38  AddI  === _ 12 13  [[ 37 ]]  !orig=[35] !jvms: infer_cond_moves::micro @ bci:14 (line 8)
[time] 32ms [res] 2029774544

 Performance counter stats for '/mnt/c/GitHub/jdk/build/linux-x86_64-server-slowdebug/images/jdk//bin/java -XX:-TieredCompilation -Xbatch -XX:CompileCommand=PrintIdealPhase,infer_cond_moves::micro,BEFORE_MATCHING -cp . infer_cond_moves 2047':

        2804396431      cycles
        5023171180      instructions                     #    1.79  insn per cycle
         980583992      branch-instructions
          14381785      branch-misses                    #    1.47% of all branches

       1.590470505 seconds time elapsed

       0.990462000 seconds user
       0.314432000 seconds sys


CompileCommand: PrintIdealPhase infer_cond_moves.micro const char* PrintIdealPhase = 'BEFORE_MATCHING'
AFTER: BEFORE_MATCHING
  0  Root  === 0 36  [[ 0 1 3 25 ]] inner
  3  Start  === 3 0  [[ 3 5 6 7 8 9 10 11 12 13 ]]  #{0:control, 1:abIO, 2:memory, 3:rawptr:BotPTR, 4:return_address, 5:int, 6:int, 7:int, 8:int}
  5  Parm  === 3  [[ 36 ]] Control !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
  6  Parm  === 3  [[ 36 ]] I_O !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
  7  Parm  === 3  [[ 36 ]] Memory  Memory: @BotPTR *+bot, idx=Bot; !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
  8  Parm  === 3  [[ 36 ]] FramePtr !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
  9  Parm  === 3  [[ 36 ]] ReturnAdr !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
 10  Parm  === 3  [[ 26 ]] Parm0: int !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
 11  Parm  === 3  [[ 26 ]] Parm1: int !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
 12  Parm  === 3  [[ 39 ]] Parm2: int !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
 13  Parm  === 3  [[ 35 ]] Parm3: int !jvms: infer_cond_moves::micro @ bci:-1 (line 3)
 25  ConI  === 0  [[ 39 ]]  #int:0
 26  CmpI  === _ 10 11  [[ 27 38 ]]  !jvms: infer_cond_moves::micro @ bci:5 (line 5)
 27  Bool  === _ 26  [[ 38 ]] [ne] !jvms: infer_cond_moves::micro @ bci:5 (line 5)
 35  AddI  === _ 13 37  [[ 36 ]]  !jvms: infer_cond_moves::micro @ bci:14 (line 8)
 36  Return  === 5 6 7 8 9 returns 35  [[ 0 ]]
 37  CMoveI  === _ 38 39  [[ 35 ]]  #int !orig=[34] !jvms: infer_cond_moves::micro @ bci:11 (line 8)
 38  Binary  === _ 27 26  [[ 37 ]]
 39  Binary  === _ 12 25  [[ 37 ]]
[time] 10ms [res] -226763056

 Performance counter stats for '/mnt/c/GitHub/jdk/build/linux-x86_64-server-slowdebug/images/jdk//bin/java -XX:-TieredCompilation -Xbatch -XX:CompileCommand=PrintIdealPhase,infer_cond_moves::micro,BEFORE_MATCHING -cp . infer_cond_moves 4095':

        2723770026      cycles
        4743627276      instructions                     #    1.74  insn per cycle
         922509466      branch-instructions
          14125665      branch-misses                    #    1.53% of all branches

       1.512564469 seconds time elapsed

       0.909291000 seconds user
       0.349422000 seconds sys
*/
