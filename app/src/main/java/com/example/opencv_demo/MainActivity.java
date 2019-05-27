package com.example.opencv_demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.opencv_demo.imageprocess.ImageUtils;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.CvType.CV_8U;
import static org.opencv.imgproc.Imgproc.COLOR_RGB2GRAY;
import static org.opencv.imgproc.Imgproc.MORPH_CROSS;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "KBein";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");

    }

    private ImageView mImg;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImg = findViewById(R.id.img);

        mBitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.pic);



    }

    @NonNull
    private Bitmap getProcessBitmap() {
//        int h = mBitmap.getHeight();
//        int w = mBitmap.getWidth();

        Bitmap bitmap = null;

        Mat mat = new Mat();

        Utils.bitmapToMat(mBitmap,mat);

        //灰度处理
        long bitMap2Gray = ImageUtils.getInstance().bitMap2Gray(mat.getNativeObjAddr());

        Mat grayMat = new Mat(bitMap2Gray);


        //二值化处理
        long utils_threshold = ImageUtils.getInstance().utils_threshold(grayMat.getNativeObjAddr(),
                165, 255, Imgproc.THRESH_BINARY);

        Mat thresholdMat = new Mat(utils_threshold);

        //中值平滑处理
        long utilsMedianBlur = ImageUtils.getInstance().utils_median_blur(thresholdMat.getNativeObjAddr(), 3);

        Mat medianBlurMat = new Mat(utilsMedianBlur);


        //边缘检测处理
        long utilsCanny = ImageUtils.getInstance().utils_canny(medianBlurMat.getNativeObjAddr(), 125, 225);

        Mat cannyMat = new Mat(utilsCanny);




        Mat element = new Mat(20, 20, CV_8U, new Scalar(1));


        long element_9NativeObjAddr = element.getNativeObjAddr();

        long utils_morphology_ex = ImageUtils.getInstance().utils_morphology_ex(cannyMat.getNativeObjAddr(),
                element_9NativeObjAddr, MORPH_CROSS, element_9NativeObjAddr);

        Mat element_9 = new Mat(utils_morphology_ex);


        //---------------------------------------------------------------//

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
                effective = new Mat(mat, rect);
            }
        }
        if (effective != null && effective.cols() > 0 && effective.rows() > 0) {
            bitmap = Bitmap.createBitmap(effective.cols(), effective.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(effective, bitmap);
        } else {
            bitmap = Bitmap.createBitmap(bitmap, 280, 360, 600, 70);
        }

//        Bitmap resultBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.RGB_565);
        Utils.matToBitmap(effective,bitmap);
        return bitmap;
    }






    public void onClickBtn(View view) {
        switch (view.getId()) {

            case R.id.btn:

                Bitmap resultBitmap = getProcessBitmap();

                mImg.setImageBitmap(resultBitmap);


                break;
        }

    }
}
