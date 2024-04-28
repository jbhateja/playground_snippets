
public class FloatMatrix {
   private int m;
   private int n;
   private float [] mat;

   public FloatMatrix(int m, int n) {
      this.m = m;
      this.n = n;
      this.mat = new float[m * n];
   }

   public FloatMatrix (float [] mat, int m, int n) {
      assert mat.length == m * n;
      this.m = m;
      this.n = n;
      this.mat = mat;
   }

   public int getDim1() { return m;}

   public int getDim2() { return n;}

   public float [] mat() {
      return mat;
   }
   public float get(int x, int y) {
      return mat[x * n + y];
   }
   public void set(int x, int y, float val) {
      mat[x * n + y] = val;
   }
   
   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder("");
      for (int i = 0; i < m; i++) {
        sb.append("[ "); 
        for (int j = 0; j < n; j++) { 
            sb.append(mat[i * n + j]);
            if (j != n -1) {
               sb.append(" , ");
            }
        }
        sb.append(" ] \n");
      }
      return sb.toString();
   }

   public boolean compare (FloatMatrix mat) {
       if (getDim1() != mat.getDim1()) {
           throw new RuntimeException("Rows count mismatch");
       }
       if (getDim2() != mat.getDim2()) {
           throw new RuntimeException("Column count mismatch");
       }
       for (int i = 0; i < this.mat.length; i++) {
          if (this.mat[i] != mat.mat()[i]) {
            throw new RuntimeException("Matrix Mismatch at row = " + i / n + " col = " + i % n ); 
          } 
       }
       return true;
   }
}
