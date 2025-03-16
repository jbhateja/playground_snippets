jshell> 0x1.fffffeP+1f
$40 ==> 3.9999998

jshell> Math.floor(3.9999998f)
$41 ==> 3.0

jshell> Math.getExponent(0x1.fffffep+1f)
$42 ==> 1

jshell> 0x1.ffffffP+1f
$43 ==> 4.0

// Inexact IEEE 754 single precision number rounding towards zero prior to application of floor results into 4.0
jshell> Math.floor(3.9999999f)
$44 ==> 4.0

jshell> Math.getExponent(0x1.ffffffp+1f)
$45 ==> 2

// Number is representable in IEEE 754 double precision format, and not an inexact value.
jshell> Math.floor(3.9999999)
$46 ==> 3.0

jshell> Math.getExponent(0x1.ffffffp+1)
$47 ==> 1

jshell> 0x1.ffffffP+1
$48 ==> 3.9999998807907104

