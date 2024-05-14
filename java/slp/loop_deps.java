
import java.util.Arrays;

public class loop_deps {
/*
  public static float micro1(float [] res1, float [] res2, float [] src1, float [] src2, int m, int n) {
      for (int i = 4; i < res1.length; i++) {
          // Inter statement dependency, forward write leading to failed vectorization.
          // Should be a no-dependency.
          res1[i] = src1[i] + src2[i];
          res2[i] = res1[i-4] * src2[i];
      }
      return res1[m] + res2[n];
  }
  public static float micro1(float [] res1, float [] res2, float [] src1, float [] src2, int m, int n) {
      for (int i = 4; i < res1.length; i++) {
          // Inter statement dependency, forward reads leading to failed vectorization.
          // This is correct.
          res1[i-4] = src1[i] + src2[i];
          res2[i] = res1[i] * src2[i];
      }
      return res1[m] + res2[n];
  }
*/
  public static float micro1(float [] res1, float [] res2, float [] src1, float [] src2, int m, int n) {
      for (int i = 4; i < res1.length; i++) {
          // Inter statement dependency, forward write leading to failed vectorization.
          // Should be a no-dependency.
          res1[i-4] = res1[i] + src2[i];
          res2[i] = res2[i] * src2[i];
      }
      return res1[m] + res2[n];
  }
  public static void main(String [] args) {
      float [] src1 = new float[1024]; 
      float [] src2 = new float[1024]; 
      float [] res1 = new float[1024]; 
      float [] res2 = new float[1024]; 
      Arrays.fill(src1, 10.0f);
      Arrays.fill(src2, 20.0f);
      for (int i = 0; i < 100000; i++) {
          micro1(res1, res2, src1, src2, 10, 20);
      }
      System.out.println("[res] " + Arrays.toString(Arrays.copyOfRange(res2, 0, 16)));
  }
}
