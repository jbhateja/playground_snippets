
class BaseMatrixOps implements MatrixOps {
    // MxN + MxN
    @Override
    public FloatMatrix add (FloatMatrix src1, FloatMatrix src2) {
      assert src1.getDim1() == src2.getDim2() && src1.getDim2() == src2.getDim2();
      FloatMatrix res = new FloatMatrix(src1.getDim1(), src2.getDim2()); 
      for(int i = 0; i < src1.mat().length; i++) {
         res.set(0, i, src1.mat()[i] + src2.mat()[i]);
      }
      return res;
    }
    @Override
    public FloatMatrix matmul(FloatMatrix src1, FloatMatrix src2) {
      assert src1.getDim2() == src2.getDim1() && src2.getDim1() == src1.getDim2();
      FloatMatrix res = new FloatMatrix(src1.getDim1(), src2.getDim2());
      for (int i = 0 ; i < src1.getDim1(); i++) {
        for (int j = 0; j < src2.getDim2(); j++) {
           float c = 0.0f;
           for (int k = 0; k < src1.getDim2(); k++) {
               c = Math.fma(src1.get(i, k), src2.get(k, j) , c);
           }
           res.set(i, j, c);
        }
      }
      return res;
    }
}


