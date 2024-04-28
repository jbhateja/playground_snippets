

abstract value class AbsValue {}

class MyValue1 extends AbsValue {
   public float field1;
   public float field2;
   MyValue (float f) {
      field1 = f * 15.0f;
      field2 = f * 20.0f;
   }
}

class MyValue2 extends AbsValue {
   public float field1;
   public float field2;
   MyValue (float f) {
      field1 = f * 5.0f;
      field2 = f * 10.0f;
   }
}
