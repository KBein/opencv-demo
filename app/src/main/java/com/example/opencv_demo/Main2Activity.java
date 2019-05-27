package com.example.opencv_demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Main2Activity extends AppCompatActivity {

    private ImageView mImg;
    private Bitmap mBitmap;

    static {
        System.loadLibrary("native-lib");

    }

    private Bitmap mResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mImg = findViewById(R.id.img);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img1);

        mResource = BitmapFactory.decodeResource(getResources(), R.mipmap.img3);


        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();

//        matAdd(w, h);
//        brightness(w,h);

//        addWeight(mBitmap,mResource);


    }

    /**
     * 基于权重的图像叠加
     * 两张图片的尺寸要一直
     * @param bitmap
     * @param resource
     */
    private void addWeight(Bitmap bitmap, Bitmap resource) {
        int w = mBitmap.getWidth();
        int h = mBitmap.getHeight();


        Mat src1 = new Mat();
        Mat src2 = new Mat();
        Mat dst = new Mat();
        Utils.bitmapToMat(mBitmap,src1);
        Utils.bitmapToMat(resource,src2);

        //调整src2的尺寸和 src1大小一致
        Imgproc.resize(src2,src2,new Size(src1.cols(),src1.rows()));

        Core.addWeighted(src1,0.8,src2,0.2,1,dst);

        Bitmap bitmap1 = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,bitmap1);
        mImg.setImageBitmap(bitmap1);


    }




    /**
     * 亮度，对比度调节
     * @param w
     * @param h
     */
    private void brightness(int w, int h) {

        Mat src = new Mat(w,h,CvType.CV_8SC3);
        Utils.bitmapToMat(mBitmap,src);

        Mat dst = new Mat();
        //调节亮度
        //负数-->暗；正数-->亮
//        Core.add(src,new Scalar(-100,-100,-100),dst);
        //调节对比度
        //浮点值 > 1.0-->提升对比度；浮点值 < 1.0 -->降低对比度；
        Core.multiply(src,new Scalar(4.0,4.0,4.0),dst);

        Bitmap bitmap1 = Bitmap.createBitmap(w,h,Bitmap.Config.RGB_565);
        Utils.matToBitmap(dst,bitmap1);
        mImg.setImageBitmap(bitmap1);
    }

    /**
     * 在图片上添加一个月亮
     * @param w
     * @param h
     */
    private void matAdd(int w, int h) {
        Mat src = new Mat(w,h,CvType.CV_16SC3);

        Utils.bitmapToMat(mBitmap,src);

        Mat moon = Mat.zeros(src.rows(),src.cols(),src.type());

        int cx = src.cols() - 100;
        int cy = 100;

        //画一个月亮
        Imgproc.circle(moon,new Point(cx,cy),90,new Scalar(90,95.234),-1,8,0);

        Mat dst = new Mat();

        Core.add(src,moon,dst);

        Bitmap bitmap1 = Bitmap.createBitmap(w,h,Bitmap.Config.RGB_565);
        Utils.matToBitmap(dst,bitmap1);
        mImg.setImageBitmap(bitmap1);
    }
}
