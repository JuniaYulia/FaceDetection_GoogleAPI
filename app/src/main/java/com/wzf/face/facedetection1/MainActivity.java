package com.wzf.face.facedetection1;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private final static int LOAD_IMAGE_PHONE = 0;
    private final static int LOAD_IMAGE_CARMER = 1;
    private File cameraImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.iv_show);
    }

    public void click_loadFromPhone(View view) {
        // 2. 调用隐式意图，跳转到gallery界面
        Intent intent = new Intent();
        // 3. 设置action和MimeType
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 4. 跳转到选照片页面页面
        startActivityForResult(intent, LOAD_IMAGE_PHONE);
    }

    /**
     * 从照相机直接拍摄
     *
     *
     * @param view
     */
    public void click_loadFromCarmer(View view) {
        try {
            // 2. 调用隐式意图，跳转到照相机页面
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            // 3. 创建牧流，如果对应目录不存在，则创建
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FaceDetection1");
            if (!file.exists()) {
                boolean boo = file.mkdirs();
                System.out.println("Create dir: " + boo);
            }
            // 4. 创建文件存储照片
            cameraImage = new File(file, System.currentTimeMillis() + ".png");
            boolean foo=cameraImage.createNewFile();
            System.out.println("create file: "+ foo);
            System.out.println(cameraImage.getAbsolutePath());
            // 4. 设置传递信息
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraImage));
            // 5. 开启activity
            startActivityForResult(intent, LOAD_IMAGE_CARMER);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 5. 当信息返回时做什么
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOAD_IMAGE_PHONE:
                // 缩放图片并显示
                loadImagePhone(data);
                break;
            case LOAD_IMAGE_CARMER:
                loadImageCarmer(data);
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 将拍照得到的图片传递到新activity中显示
     * @param data
     */
    private void loadImageCarmer(Intent data) {
        System.out.println("I am loading!!!!!!!!!!!!!!!!!");
        //1. 发送广播，让图库加载sd卡图片
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(cameraImage));
        sendBroadcast(intent);
        //2. 缩放图片
        Uri uri=Uri.fromFile(cameraImage);
        // 3. 在新界面中显示
        toFaceActivity(uri);
    }

    /**
     * 将手机图库中的图片传递到新activity中显示
     *
     * @param data
     */
    private void loadImagePhone(Intent data) {
        if (data != null) {
            // 6. 返回的是图片的路径
            Uri uri = data.getData();
            // 7. 跳转到新的页面
            toFaceActivity(uri);
        }
    }

    /**
     * 跳转到Face页面
     */
    private void toFaceActivity(Uri uri){
        //1. 设置要跳转的页面
        Intent intent=new Intent(getApplicationContext(), FaceActivity.class);
        //2. 配置需要传递的数据
        // 3. 利用putExtra中接受序列化Parcelable参数的方法
        intent.putExtra("image", uri);
        startActivity(intent);
    }
}
