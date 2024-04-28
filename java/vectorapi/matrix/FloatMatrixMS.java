
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

public class FloatMatrixMS {
   private int m;
   private int n;
   private MemorySegment matms;

   public FloatMatrixMS(int m, int n) {
      this.m = m;
      this.n = n;
      this.matms = Arena.ofAuto().allocate(m * n * Float.BYTES);
   }

   public FloatMatrixMS (MemorySegment matms, int m, int n) {
      assert matms.byteSize() == m * n;
      this.m = m;
      this.n = n;
      this.matms = matms;
   }

   public MemorySegment mat() {
      return matms;
   }
   public float get(int x, int y) {
      return matms.get(ValueLayout.JAVA_FLOAT, Float.BYTES * (x * n + y));
   }
   public void set(int x, int y, float val) {
      matms.set(ValueLayout.JAVA_FLOAT, Float.BYTES * (x * n + y), val);
   }
}
