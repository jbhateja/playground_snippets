
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

float micro_native(float* arr) {
    float res = 0.0f;
    for (int i = 0; i < 1024; i++) {
        float* arr1 = (float*)malloc(sizeof(float) * 1024);
        memset(arr1, arr[i], sizeof(float) * 1024);
        for (int j = 0; j < 1024; j++) {
            float* arr2 = (float*)malloc(sizeof(float) * 1024);
            memset(arr2, arr1[j], sizeof(float) * 1024);
            for (int k = 0; k < 1024; k++) {
                float* arr3 = (float*)malloc(sizeof(float) * 1024);
                memset(arr3, arr2[k], sizeof(float) * 1024);
                res += arr3[k];
                free(arr3);
            }
            free(arr2);
        }
        free(arr1);
    }
    return res;
}
