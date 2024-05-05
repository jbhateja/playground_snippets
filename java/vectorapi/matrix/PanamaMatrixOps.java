
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

class PanamaMatrixOps implements  MatrixOps {
    public static final VectorSpecies<Float> FSP = FloatVector.SPECIES_512;
    public static final VectorSpecies<Integer> I512 = IntVector.SPECIES_512;
    // MxN + MxN
    /**
    * @param src1
    * @param src2
    * @return
    */
   @Override
    public FloatMatrix add (FloatMatrix src1, FloatMatrix src2) {
       assert src1.getDim1() == src2.getDim1() && src1.getDim2() == src2.getDim2();
       FloatMatrix res = new FloatMatrix(src1.getDim1(), src2.getDim2());
       for (int i = 0; i  < src1.length(); i += FSP.length()) {
          FloatVector.fromArray(FSP, src1.mat(), i)
                     .lanewise(VectorOperators.ADD, FloatVector.fromArray(FSP, src2.mat(), i))
                     .intoArray(res.mat(), i);
       }
       return res;
    }

    // NxM X MxN
    @Override
    public FloatMatrix matmul(FloatMatrix src1, FloatMatrix src2) {
       assert src1.getDim2() == src2.getDim1() && src1.getDim1() == src2.getDim2();
       FloatMatrix res = null;
/*
       int src1_rows_tail_masked_elems = src1.getDim1() % FSP.length() > 0 ?
                                         FSP.length() - (src1.getDim1() % FSP.length()) : 0;
       int src2_col_tail_masked_elems = src2.getDim2() % FSP.length() > 0 ?
                                  FSP.length() - (src2.getDim2() % FSP.length()) : 0;
       if (src1_rows_tail_masked_elems > 0 || src2_col_tail_masked_elems > 0) {
          // Use masked operation for last iteration.
          assert false;
       } else {
*/
       res = new FloatMatrix(src1.getDim1(), src2.getDim2());
       for (int i = 0; i < src1.getDim1(); i++) {
           for (int j = 0; j < src1.getDim2(); j += FSP.length()) {
               FloatVector vres = FloatVector.zero(FSP);
               // Trip count = src2.dim1 which is same as src1.dim2
               for (int k = 0, col = 0; k < src2.length(); k += src2.getDim2(), col++) {
                   FloatVector vec1 = FloatVector.broadcast(FSP, src1.get(i, col));
                   FloatVector vec2 = FloatVector.fromArray(FSP, src2.mat(), k + j);
                   vres = vec1.lanewise(VectorOperators.FMA, vec2, vres);
               }
               vres.intoArray(res.mat(), i * src1.getDim2() + j);
           }
       }
 //    }
       return res;
    }
}

