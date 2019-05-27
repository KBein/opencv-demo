#include <jni.h>
#include <string>
#include "opencv2/opencv.hpp"
using namespace cv;
using namespace std;




extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_opencv_1demo_imageprocess_ImageUtils_bitMap2Gray(JNIEnv *env, jobject instance,
                                                                  jlong src) {


    Mat *mat = (Mat*) src;

    cvtColor(*mat,*mat,COLOR_BGR2GRAY,1);


    return (jlong)mat;

}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_opencv_1demo_imageprocess_ImageUtils_getLaplacian(JNIEnv *env, jobject instance,
                                                                   jlong src) {


    Mat *mat = (Mat*) src;

    Laplacian(*mat,*mat,mat->depth());

    return (jlong)mat;


}

//二值化处理
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_opencv_1demo_imageprocess_ImageUtils_utils_1threshold(JNIEnv *env,
                                                                       jobject instance, jlong src,
                                                                       jdouble thresh,
                                                                       jdouble maxval, jint type) {

    Mat *src_threshold = (Mat*) src;


    threshold(*src_threshold,*src_threshold,thresh, maxval, type);

    return (jlong)src_threshold;
}

//中值平滑处理
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_opencv_1demo_imageprocess_ImageUtils_utils_1median_1blur(JNIEnv *env,
                                                                          jobject instance,
                                                                          jlong src, jint ksize) {

    Mat *src_median_1blur = (Mat*) src;

    medianBlur(*src_median_1blur,*src_median_1blur,ksize);

    return (jlong)src_median_1blur;

}
//边缘检测处理
extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_opencv_1demo_imageprocess_ImageUtils_utils_1canny(JNIEnv *env, jobject instance,
                                                                   jlong src, jdouble threshold1,
                                                                   jdouble threshold2) {
    Mat *src_1canny = (Mat*) src;

    Canny(*src_1canny,*src_1canny,threshold1,threshold2);

    return (jlong)src_1canny;

}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_example_opencv_1demo_imageprocess_ImageUtils_utils_1morphology_1ex(JNIEnv *env,
                                                                            jobject instance,
                                                                            jlong src, jlong dst,
                                                                            jint op, jlong kernel) {

    Mat *src_morphology_ex = (Mat*)src;

    Mat *dst_morphology_ex = (Mat*)dst;

    morphologyEx(*src_morphology_ex,*dst_morphology_ex,op,*dst_morphology_ex);

    return (jlong)dst_morphology_ex;
}