
import java.util.Arrays;
import java.lang.foreign.*;

public class test {
    public float micro_native(float [] arr) {
        try (Arena arena = Arena.ofConfined()) {
            MemorySegment ms = arena.allocate(arr.length * Float.BYTES);
            ms.copyFrom(MemorySegment.ofArray(arr));
            return natives_h.micro_native(ms);
        }
    }

    public float micro_scalar_java(float [] arr) {
        float res = 0.0f;
        for (int i = 0; i < arr.length; i++) {
            res += arr[i];
        }
        return res;
    }

    public float micro_hot_java(float [] arr) {
        float res = 0.0f;
        for (int i = 0; i < arr.length; i++) {
            float [] arr1 = new float[1024];
            Arrays.fill(arr1, arr[i]); 
            for (int j = 0; j < arr1.length; j++) {
                float [] arr2 = new float[1024];
                Arrays.fill(arr2, arr1[j]); 
                for (int k = 0; k < arr2.length; k++) {
                    float [] arr3 = new float[1024];
                    Arrays.fill(arr3, arr2[k]); 
                    res += arr3[k];
                }
            }
        } 
        return res;
    }

    static {
        System.loadLibrary("natives");
        System.out.println("Loaded libnatives.so..");
    }
   
    public static void main (String [] args) {
        test obj = new test();
        float [] arr = new float[1024];
        Arrays.fill(arr, 10.2f);
        float res = 0.0f;
        res += obj.micro_scalar_java(arr);
        for (int i = 0; i < 100000; i++) {
            res += obj.micro_native(arr); 
            res += obj.micro_hot_java(arr);
        } 
        System.out.println("[res] " + res);
    }
}
