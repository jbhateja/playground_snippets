public class matrix_ops {
    public static void main(String[] args) {
        int m = 16;
        int n = 16;
        int ctr = 0;
        MatrixOps ops = new PanamaMatrixOps();
        float [] mat = new float[m * n];
        for (int i = 0; i < m; i++) {
            for (int j = 0;  j < n; j++) {
                mat[i * n + j] = ctr++;
            }
        }
        FloatMatrix m1 = new FloatMatrix(mat, m, n);
        FloatMatrix m2 = new FloatMatrix(mat, m, n);
        FloatMatrix res1 = new FloatMatrix(m, n);

        for (int i = 0; i < 70000; i++) {
           res1 = ops.add(res1, ops.matmul(m1, m2));
        }
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            res1 = ops.add(res1, ops.matmul(m1, m2));
         }
        long t2 = System.currentTimeMillis();
        //System.out.println(res1);
        System.out.println("[time panama matrix ops] " + (t2-t1) + " ms ");

        FloatMatrix res2 = new FloatMatrix(m, n);
        ops = new BaseMatrixOps();
        for (int i = 0; i < 70000; i++) {
            res2 = ops.add(res2, ops.matmul(m1, m2));
        }
        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
             res2 = ops.add(res2, ops.matmul(m1, m2));
        }
         t2 = System.currentTimeMillis();
         //System.out.println(res2);
         System.out.println("[time basic matrix ops] " + (t2-t1) + " ms ");
         
         res1.compare(res2);
    }   
}
