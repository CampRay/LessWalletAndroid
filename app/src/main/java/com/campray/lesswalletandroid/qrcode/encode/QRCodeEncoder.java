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

package com.campray.lesswalletandroid.qrcode.encode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;

import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


import java.util.EnumMap;
import java.util.Map;

/**
 * 二维码生成工具类
 */
public final class QRCodeEncoder {

  private static final int WHITE = 0xFFFFFFFF;
  private static final int BLACK = 0xFF000000;
  public QRCodeEncoder() {

  }

  /**
   *生成二维码图片
   * @param contents
   * @param format 二维码格式
   * @param dimension 生在的二维码的尺寸
   * @return
   * @throws WriterException
   */
  public static Bitmap encodeAsBitmap(String contents,BarcodeFormat format,int dimension) throws WriterException {
    if (contents == null) {
      return null;
    }
    Map<EncodeHintType,Object> hints = new EnumMap<>(EncodeHintType.class);
    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
    //容错级别
    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
    //设置空白边距的宽度
    hints.put(EncodeHintType.MARGIN, 0);
    BitMatrix result;
    try {
      result = new MultiFormatWriter().encode(contents, format, dimension, dimension, hints);
    } catch (IllegalArgumentException iae) {
      // Unsupported format
      return null;
    }
    int width = result.getWidth();
    int height = result.getHeight();
    int[] pixels = new int[width * height];
    for (int y = 0; y < height; y++) {
      int offset = y * width;
      for (int x = 0; x < width; x++) {
        pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
      }
    }

    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    return bitmap;
  }

  /**
   *生成二维码图片，带logo
   * @param contents
   * @param format 二维码格式
   * @param dimension 生在的二维码的尺寸
   * @param logo
   * @return
   * @throws WriterException
   */
  public static Bitmap encodeAsBitmap(String contents,BarcodeFormat format,int dimension,Bitmap logo) throws WriterException {
    if (contents == null) {
      return null;
    }
    Bitmap scaleLogo = getScaleLogo(logo,dimension,dimension);
    int offsetX = dimension / 2;
    int offsetY = dimension / 2;
    int scaleWidth = 0;
    int scaleHeight = 0;
    if (scaleLogo != null) {
      scaleWidth = scaleLogo.getWidth();
      scaleHeight = scaleLogo.getHeight();
      offsetX = (dimension - scaleWidth) / 2;
      offsetY = (dimension - scaleHeight) / 2;
    }

    Map<EncodeHintType,Object> hints = new EnumMap<>(EncodeHintType.class);
    hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
    //容错级别
    hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
    //设置空白边距的宽度
    hints.put(EncodeHintType.MARGIN, 0);
    BitMatrix result;
    try {
      result = new MultiFormatWriter().encode(contents, format, dimension, dimension, hints);
    } catch (IllegalArgumentException iae) {
      // Unsupported format
      return null;
    }
    int width = result.getWidth();
    int height = result.getHeight();
    int[] pixels = new int[width * height];
    for (int y = 0; y < height; y++) {
      int offset = y * width;
      for (int x = 0; x < width; x++) {
        if(x >= offsetX && x < offsetX + scaleWidth && y>= offsetY && y < offsetY + scaleHeight){
          int pixel = scaleLogo.getPixel(x-offsetX,y-offsetY);
          if(pixel == 0){
            pixel = result.get(x, y) ? BLACK : WHITE;
          }
          pixels[offset + x] = pixel;
        }else{
          pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
        }
      }
    }

    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    return bitmap;
  }

  private static String guessAppropriateEncoding(CharSequence contents) {
    // Very crude at the moment
    for (int i = 0; i < contents.length(); i++) {
      if (contents.charAt(i) > 0xFF) {
        return "UTF-8";
      }
    }
    return null;
  }

  /**
   * 转换logo图片到指定的大小
   * @param logo
   * @param w
   * @param h
   * @return
   */
  private static Bitmap getScaleLogo(Bitmap logo,int w,int h){
    if(logo == null)return null;
    Matrix matrix = new Matrix();
    float scaleFactor = Math.min(w * 1.0f / 5 / logo.getWidth(), h * 1.0f / 5 /logo.getHeight());
    matrix.postScale(scaleFactor,scaleFactor);
    Bitmap result = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(),   logo.getHeight(), matrix, true);
    return result;
  }

}
