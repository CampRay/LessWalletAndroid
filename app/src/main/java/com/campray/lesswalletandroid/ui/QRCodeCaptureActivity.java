package com.campray.lesswalletandroid.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.qrcode.AmbientLightManager;
import com.campray.lesswalletandroid.qrcode.BeepManager;
import com.campray.lesswalletandroid.qrcode.CaptureActivityHandler;
import com.campray.lesswalletandroid.qrcode.FinishListener;
import com.campray.lesswalletandroid.qrcode.InactivityTimer;
import com.campray.lesswalletandroid.qrcode.ViewfinderView;
import com.campray.lesswalletandroid.qrcode.camera.CameraManager;
import com.campray.lesswalletandroid.qrcode.decode.ImageDecoder;
import com.campray.lesswalletandroid.ui.base.ParentWithNaviActivity;
import com.campray.lesswalletandroid.util.ImageUtil;
import com.google.zxing.Result;

import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 二维码扫描页面
 * Created by Phills on 11/9/2017.
 */

public class QRCodeCaptureActivity extends ParentWithNaviActivity implements SurfaceHolder.Callback,EasyPermissions.PermissionCallbacks{
    private static final String TAG = QRCodeCaptureActivity.class.getSimpleName();
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;
    public final static  int TAKE_STORAGE_READ=101;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private BeepManager beepManager;
    private InactivityTimer inactivityTimer;
    private AmbientLightManager ambientLightManager;
    private boolean hasSurface;
    private boolean hasReject=false;

    private ViewfinderView viewfinderView;
//    @BindView(R.id.barcode_image_view)
//    ImageView barcode_image_view;

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public Handler getHandler() {
        return handler;
    }

    @Override
    protected String title() {
        return getResources().getString(R.string.scan_title);
    }
    @Override
    public Object right() {return "Image";}

    @Override
    public ParentWithNaviActivity.ToolBarListener setToolBarListener() {
        return new ParentWithNaviActivity.ToolBarListener() {
            @Override
            public void clickLeft() {finish();}
            @Override
            public void clickRight() {
                if (EasyPermissions.hasPermissions(QRCodeCaptureActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //如果有权限,打开扫码页面
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_IMAGE);
                } else {
                    if(!hasReject) {
                        hasReject=true;
                        EasyPermissions.requestPermissions(QRCodeCaptureActivity.this, "扫码本地图片需要存储空间的访问权限",
                                QRCodeCaptureActivity.TAKE_STORAGE_READ, Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                    else {
                        new AppSettingsDialog.Builder(QRCodeCaptureActivity.this)
                                .setTitle(getString(R.string.dialog_permission_title))
                                .setRationale("扫码本地图片需要存储空间的访问权限,您没有授权此权限，此功能将无法使用！您也可以在权限设置中开启此权限。")
                                .setPositiveButton(getString(R.string.button_ok))
                                .setNegativeButton(getString(R.string.button_cancel))
                                .setRequestCode(TAKE_STORAGE_READ)
                                .build()
                                .show();
                    }
                }
            }
        };
    }

    /**
     * startActivityForResult方法解码本地图片调用后的返回结果处理方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 选择系统图片并解析
         */
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    Result result=ImageDecoder.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri));
                    closeActivity(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 一个有效的二维码被扫描到，给出一个成功的表示并显示结果
     * A valid barcode has been found, so give an indication of success and show the results.
     *
     * @param rawResult The contents of the barcode.
     * @param scaleFactor amount by which thumbnail was scaled
     * @param barcode   A greyscale bitmap of the camera data which was decoded.
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        //inactivityTimer.onActivity();
        inactivityTimer.shutdown();
        closeActivity(rawResult);
    }

    /**
     * 关闭此扫码页面并返回解码成功结果
     * @param result
     */
    private void closeActivity(Result result){
        if (result!=null) {
            // Then not from history, so beep/vibrate and we have an image to draw on
            beepManager.playBeepSoundAndVibrate();
            //显示扫码截图
            //barcode_image_view.setImageBitmap(barcode);

            Bundle bundle = new Bundle();
            bundle.putString("result", result.getText());
            Intent resultIntent = new Intent();
            resultIntent.putExtras(bundle);
            this.setResult(RESULT_OK, resultIntent);
            this.finish();

        }
        else{
            toast("解码失败，请重试");
        }
    }

    /**
     * Called when the activity is first created.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //让屏幕一直显示
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_qrode_cature);
        initNaviView();

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);
        handler = null;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
//    if (prefs.getBoolean(SettingsActivity.KEY_DISABLE_AUTO_ORIENTATION, true)) {
//      setRequestedOrientation(getCurrentOrientation());
//    } else {
//      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
//    }

        beepManager.updatePrefs();
        ambientLightManager.start(cameraManager);
        inactivityTimer.onResume();

//        decodeHints=new Hashtable<>();
//        decodeHints.put(DecodeHintType.CHARACTER_SET,"utf-8");
//        decodeHints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
//        decodeHints.put(DecodeHintType.POSSIBLE_FORMATS, BarcodeFormat.QR_CODE);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            surfaceHolder.addCallback(this);
        }
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();
        cameraManager.closeDriver();
        //historyManager = null; // Keep for onActivityResult
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // do nothing
    }

    //初始化摄像头开始扫码
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, null, null, null, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }


    /**
     * 显示扫码页面打开异常信息的对话框
     */
    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }
}
