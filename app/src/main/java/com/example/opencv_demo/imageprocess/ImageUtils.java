package com.example.opencv_demo.imageprocess;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static org.opencv.core.CvType.CV_8U;
import static org.opencv.imgproc.Imgproc.MORPH_CROSS;

public class ImageUtils {

    private ImageUtils(){

    }

    public static ImageUtils getInstance(){
        return ImageUtilsHolder.mImageUtils;
    }


    static class ImageUtilsHolder{

        private static ImageUtils mImageUtils = new ImageUtils();
    }

    //灰度化处理
    public native long bitMap2Gray(long src);



    public native long getLaplacian(long src);

    //二值化处理
    public native long utils_threshold(long src,double thresh,double maxval,int type);

    //中直平滑处理
    public native long utils_median_blur(long src,int ksize);


    public native long utils_morphology_ex(long src,long dst,int op, long kernel);

    //边缘检测
    public native long utils_canny(long src,double threshold1, double threshold2);




    public static Bitmap doICPretreatmentOne(Bitmap bitmap) {
        Mat rgbMat = new Mat(); //原图
        Mat grayMat = new Mat();  //灰度图
        Mat binaryMat = new Mat(); //二值化图
        Mat canny = new Mat();
        Utils.bitmapToMat(bitmap, rgbMat);
        Imgproc.cvtColor(rgbMat, grayMat, Imgproc.COLOR_RGB2GRAY);//灰度化


        Imgproc.blur(grayMat, canny, new Size(3, 3));//低通滤波处理


        Imgproc.Canny(grayMat, canny, 125, 225);//边缘检测处理


        Imgproc.threshold(canny, binaryMat, 165, 255, Imgproc.THRESH_BINARY);//二值化


        Imgproc.medianBlur(binaryMat, binaryMat, 3);//中值平滑处理

        Mat element_9 = new Mat(20, 20, CV_8U, new Scalar(1));

        Imgproc.morphologyEx(binaryMat, element_9, MORPH_CROSS, element_9);//闭运算
        /**
         * 轮廓提取()
         */
        ArrayList<MatOfPoint> contoursList = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(element_9, contoursList, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_NONE);
        Mat resultImage = Mat.zeros(element_9.size(), CV_8U);
        Imgproc.drawContours(resultImage, contoursList, -1, new Scalar(255, 0, 255));

        Mat effective = new Mat(); //身份证位置
        //外包矩形区域
        for (int i = 0; i < contoursList.size(); i++) {
            Rect rect = Imgproc.boundingRect(contoursList.get(i));
            if (rect.width != rect.height && rect.width / rect.height > 9) { //初步判断找到有效位置
                Imgproc.rectangle(resultImage, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 255), 1);
                effective = new Mat(rgbMat, rect);
            }
        }
        if (effective != null && effective.cols() > 0 && effective.rows() > 0) {
            bitmap = Bitmap.createBitmap(effective.cols(), effective.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(effective, bitmap);
        } else {
            bitmap = Bitmap.createBitmap(bitmap, 280, 360, 600, 70);
        }
        return bitmap;
    }



}
