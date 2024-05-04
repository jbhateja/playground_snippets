
import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

class PanamaMatrixOps implements  MatrixOps {
    public static VectorSpecies<Float> F512 = FloatVector.SPECIES_512;
    public static VectorSpecies<Integer> I512 = IntVector.SPECIES_512;
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
       for (int i = 0; i  < src1.mat().length; i += F512.length()) {
          FloatVector.fromArray(F512, src1.mat(), i)
                     .lanewise(VectorOperators.ADD, FloatVector.fromArray(F512, src2.mat(), i))
                     .intoArray(res.mat(), i);
       }
       return res;
    }

    // 16x16 
    @Override
    public FloatMatrix matmul(FloatMatrix src1, FloatMatrix src2) {
       assert src1.getDim2() == src2.getDim1();
       assert src1.getDim1() == src2.getDim1() && src1.getDim2() == src2.getDim2();
       FloatMatrix res = new FloatMatrix(src1.getDim1(), src2.getDim2());
       for (int i = 0; i < src1.getDim1(); i++) {
          FloatVector vres = FloatVector.zero(F512);
          for (int j = 0, k = 0; j < src2.mat().length; j += F512.length(), k++) {
             FloatVector vec1 = FloatVector.broadcast(F512, src1.get(i, k));
             FloatVector vec2 = FloatVector.fromArray(F512, src2.mat(), j);
             vres = vec1.lanewise(VectorOperators.FMA, vec2, vres);
          }
          vres.intoArray(res.mat(), i * F512.length());
       }
       return res;
    }
}
