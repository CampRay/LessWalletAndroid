/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.campray.lesswalletandroid.qrcode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.campray.lesswalletandroid.R;
import com.campray.lesswalletandroid.qrcode.camera.CameraManager;
import com.google.zxing.ResultPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder rectangle and partial
 * transparency outside it, as well as the laser scanner animation and result points.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ViewfinderView extends View {

//  private static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
  private static final long ANIMATION_DELAY = 80L;
//  private static final int CURRENT_POINT_OPACITY = 0xA0;
//  private static final int MAX_RESULT_POINTS = 20;
//  private static final int POINT_SIZE = 6;

  private CameraManager cameraManager;
  private final Paint paint;
  private Bitmap resultBitmap;
  private Bitmap scanLine;
  private final int maskColor;
  private final int resultColor;
  private final int cornerColor;
  //private final int laserColor;
  //private final int resultPointColor;
  //private int scannerAlpha;
  private List<ResultPoint> possibleResultPoints;
  //private List<ResultPoint> lastPossibleResultPoints;
  //扫描线的top位置
  private int scanLineTop=0;

  // This constructor is used when the class is built from an XML resource.
  public ViewfinderView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Initialize these once for performance rather than calling them every time in onDraw().
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Resources resources = getResources();
    maskColor = resources.getColor(R.color.viewfinder_mask);
    resultColor = resources.getColor(R.color.result_view);
    cornerColor= resources.getColor(R.color.viewfinder_laser);
    scanLine = BitmapFactory.decodeResource(getResources(), R.mipmap.qrcode_scan_line);
//    laserColor = resources.getColor(R.color.viewfinder_laser);
//    resultPointColor = resources.getColor(R.color.possible_result_points);
//    scannerAlpha = 0;
    possibleResultPoints = new ArrayList<>(5);
//    lastPossibleResultPoints = null;
  }

  public void setCameraManager(CameraManager cameraManager) {
    this.cameraManager = cameraManager;
  }

  @SuppressLint("DrawAllocation")
  @Override
  public void onDraw(Canvas canvas) {
    if (cameraManager == null) {
      return; // not ready yet, early draw before done configuring
    }
    Rect frame = cameraManager.getFramingRect();
    Rect previewFrame = cameraManager.getFramingRectInPreview();    
    if (frame == null || previewFrame == null) {
      return;
    }
    int width = canvas.getWidth();
    int height = canvas.getHeight();

    // Draw the exterior (i.e. outside the framing rect) darkened
    paint.setColor(resultBitmap != null ? resultColor : maskColor);
    canvas.drawRect(0, 0, width, frame.top, paint);
    canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
    canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1, paint);
    canvas.drawRect(0, frame.bottom + 1, width, height, paint);

    drawFrameCorner(canvas,frame);
    drawScanLine(canvas,frame);
//    if (resultBitmap != null) {
//      // 绘画透明结果图片在扫描框区域
//      paint.setAlpha(CURRENT_POINT_OPACITY);
//      canvas.drawBitmap(resultBitmap, null, frame, paint);
//    } else {
//
//      // 画一个绿色的扫描线
//      paint.setColor(laserColor);
////原来的居中闪动扫描线（我已注销更改）
////      paint.setAlpha(SCANNER_ALPHA[scannerAlpha]);
////      scannerAlpha = (scannerAlpha + 1) % SCANNER_ALPHA.length;
//      paint.setAlpha(64);
//      //int middle = frame.height() / 2 + frame.top;
//      if((line_top+5)>=frame.bottom-frame.top){
//        line_top=5;
//      }
//      int middle = line_top+ frame.top;
//      canvas.drawRect(frame.left + 10, middle -5, frame.right - 10, middle + 5, paint);
//      line_top+=5;
//    }
    // 延时重绘指定区域(主要是重绘动态扫描线)Request another update at the animation interval, but only repaint the laser line,
    postInvalidateDelayed(ANIMATION_DELAY,frame.left,frame.top, frame.right,frame.bottom);
  }

  /**
   * 绘制移动扫描线
   *
   * @param canvas
   * @param frame
   */
  private void drawScanLine(Canvas canvas, Rect frame) {
    if (scanLineTop == 0) {
      scanLineTop = frame.top;
    }

    if (scanLineTop >= frame.bottom - 18) {
      scanLineTop = frame.top;
    } else {
      scanLineTop += 5;
    }
    Rect scanRect = new Rect(frame.left, scanLineTop, frame.right,
            scanLineTop + 18);
    canvas.drawBitmap(scanLine, null, scanRect, paint);
  }

  /**
   * 绘制取景框边角
   * @param canvas
   * @param frame
   */
  private void drawFrameCorner(Canvas canvas, Rect frame) {
    paint.setColor(cornerColor);
    paint.setStyle(Paint.Style.FILL);

    int corWidth = 15;
    int corLength = 65;

    // 左上角
    canvas.drawRect(frame.left, frame.top, frame.left + corWidth, frame.top
            + corLength, paint);
    canvas.drawRect(frame.left, frame.top, frame.left
            + corLength, frame.top + corWidth, paint);
    // 右上角
    canvas.drawRect(frame.right - corWidth, frame.top, frame.right,
            frame.top + corLength, paint);
    canvas.drawRect(frame.right - corLength, frame.top,
            frame.right, frame.top + corWidth, paint);
    // 左下角
    canvas.drawRect(frame.left, frame.bottom - corLength,
            frame.left + corWidth, frame.bottom, paint);
    canvas.drawRect(frame.left, frame.bottom - corWidth, frame.left
            + corLength, frame.bottom, paint);
    // 右下角
    canvas.drawRect(frame.right - corWidth, frame.bottom - corLength,
            frame.right, frame.bottom, paint);
    canvas.drawRect(frame.right - corLength, frame.bottom - corWidth,
            frame.right, frame.bottom, paint);
  }

  //清除上一次扫码结果图片
  public void drawViewfinder() {
    Bitmap resultBitmap = this.resultBitmap;
    this.resultBitmap = null;
    if (resultBitmap != null) {
      resultBitmap.recycle();
    }
    //重新绘制当前View
    invalidate();
  }

  /**
   * Draw a bitmap with the result points highlighted instead of the live scanning display.
   *
   * @param barcode An image of the decoded barcode.
   */
  public void drawResultBitmap(Bitmap barcode) {
    resultBitmap = barcode;
    //重新绘制当前View
    invalidate();
  }
  //ViewfinderResultPointCallback回调方法计算到可能的定位点后，会调用此方法
  public void addPossibleResultPoint(ResultPoint point) {
//    List<ResultPoint> points = possibleResultPoints;
//    synchronized (points) {
//      points.add(point);
//      int size = points.size();
//      if (size > MAX_RESULT_POINTS) {
//        // trim it
//        points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
//      }
//    }
  }

}
