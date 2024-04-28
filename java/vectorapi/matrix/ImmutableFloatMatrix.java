/* 
import jdk.internal.vm.annotation.*;
import jdk.internal.value.*;

@ImplicitlyConstrictible
value class FloatVal {
  float elem;
  FloatVal(float elem) {
     this.elem = elem;
  }
} 

public value class ImmutableFloatMatrix {
   private int m;
   private int n;
   @NullRestricted
   private Object [] mat;

   public FloatMatrix(int m, int n) {
      this.m = m;
      this.n = n;
      this.mat = ValueClass.newNullRestrictedArray(FloatVal.class, m * n); 
   }

   public FloatMatrix (Objec [] mat, int m, int n) {
      assert mat.isNullRestrictedArray();
      assert mat.length == m * n;
      this.m = m;
      this.n = n;
      this.mat = mat;
   }

   public float [] mat() {
      return mat;
   }
   public float get(int x, int y) {
      return mat[x * n + y];
   }
   public float set(int x, int y, float val) {
      mat[x * n + y] = val;
   }
}
*/