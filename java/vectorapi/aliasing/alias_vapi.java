
import jdk.incubator.vector.*;
import java.util.Arrays;

public class alias_vapi {
    public static final VectorSpecies<Float> FSP = FloatVector.SPECIES_512;

    public static void micro(float [] res1, float [] res2, float [] src1, float [] src2) {
         // Vectors backed by array storage imbibes the aliasing property of arrays where
         // all the elements are considered to be aliases.
         // This is different from aliasing behavior of field accesses where alias type
         // also includes offset, thus different field access are of an instance are 
         // not aliases.

         // Memory [..............].
         FloatVector.fromArray(FSP, src1, 0)
                    .lanewise(VectorOperators.ADD, FloatVector.fromArray(FSP, src2, 0))
                    .intoArray(res1, 0);
         //   
         //            StoreVector
         //                |
         //                V
         // Memory [..... [F] ......] 

         // Subsequent LoadVector cannot be shared with a LoadVector prior to memory state 
         // update since loads and store have same alias types.
         FloatVector.fromArray(FSP, src2, 0)
                    .lanewise(VectorOperators.MUL, FloatVector.fromArray(FSP, res1, 0))
                    .intoArray(res2, 0);
    }

    public static void main(String [] args) {
         float [] src1 = new float[16];
         float [] src2 = new float[16];
         float [] res1 = new float[16];
         float [] res2 = new float[16];

         Arrays.fill(src1, 10.0f);
         Arrays.fill(src2, 20.0f);

         for (int i = 0; i < 50000; i++) {
             micro(res1, res2, src1, src2);
         }
         System.out.println(" [res2] " +  Arrays.toString(res1));
    }
} 
