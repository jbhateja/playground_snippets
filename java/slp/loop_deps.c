
#if 0
float micro1(float * res1, float * res2, float * src1, float * src2, int length, int m, int n) {
    for (int i = 0; i < 1024; i++) {
        // loop carried inter statement dependency, limiting VF to 4.
        res1[i] = src1[i] + src2[i];
        res2[i] = res1[i+4] + src2[i];
    }
    return res1[m] + res2[n];
}
#endif

#if 0
float micro2(float * res1, float * res2, float * src1, float * src2, int length, int m, int n) {
    for (int i = 0; i < 1024; i++) {
        // Forward reads intra-statment not a problem, Anti-dependency
        res1[i] = res1[i + 4] + src2[i];
    }
    return res1[m] + res2[n];
}
#endif

#if 0
float micro3(float * res1, float * res2, float * src1, float * src2, int length, int m, int n) {
    for (int i = 4; i < 1024; i++) {
        // forward write intra-statment, true dependence raw, limiting vf to 4.
        res1[i] = res1[i - 4] + src2[i];
    }
    return res1[m] + res2[n];
}
#endif

#if 0
float micro4(float * res1, float * res2, float * src1, float * src2, int length, int m, int n) {
    for (int i = 0; i < 1024; i++) {
        // forward write intra-statment, true dependence raw, limiting vf to 4.
        res1[i + 4] = res1[i] + src2[i];
    }
    return res1[m] + res2[n];
}
#endif

float micro5(float * res1, float * res2, float * src1, float * src2, int length, int m, int n) {
    for (int i = 4; i < 1024; i++) {
        // interstatement, forward read, limits VF to 4..
        res1[i-4] = src1[i] + src2[i];
        res2[i] = res1[i] * src2[i];
    }
    return res1[m] + res2[n];
}
