/*
 * Copyright 2018 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.core.examples.java.computervision;

import java.nio.ByteBuffer;

/** Detects edges from input YUV image. */
public class ColorCorrection {
    private byte[] yinputPixels = new byte[0]; // Reuse java byte array to avoid multiple allocations.
    private byte[] uinputPixels = new byte[0]; // Reuse java byte array to avoid multiple allocations.
    private byte[] vinputPixels = new byte[0]; // Reuse java byte array to avoid multiple allocations.

  private static final int SOBEL_EDGE_THRESHOLD = 128 * 128;

  /**
   * Process a grayscale image using the Sobel edge detector.
   *
   * @param width image width.
   * @param height image height.
   * @param ystride image ystride (number of bytes per row, equals to width if no row padding).
   * @param yinput bytes of the image, assumed single channel grayscale of size [ystride * height].
   * @return bytes of the processed image, where the byte value is the strength of the edge at that
   *     pixel. Number of bytes is width * height, row padding (if any) is removed.
   */
  public synchronized ByteBuffer correct(int width, int height, int ystride, ByteBuffer yinput, int ustride, ByteBuffer uinput, int vstride, ByteBuffer vinput) {
    // Reallocate yinput byte array if its size is different from the required size.
      if (ystride * height > yinputPixels.length) {
          yinputPixels = new byte[ystride * height];
      }
      if (ustride * height > uinputPixels.length) {
          uinputPixels = new byte[ustride * height];
      }
      if (vstride * height > vinputPixels.length) {
          vinputPixels = new byte[vstride * height];
      }

    // Allocate a new output byte array.
    byte[] routputPixels = new byte[width * height];
      byte[] goutputPixels = new byte[width * height];
      byte[] boutputPixels = new byte[width * height];

    // Copy yinput buffer into a java array for ease of access. This is not the most optimal
    // way to process an image, but used here for simplicity.
      yinput.position(0);

      uinput.position(0);

      vinput.position(0);

    // Note: On certain devices with specific resolution where the ystride is not equal to the width.
    // In such situation the memory allocated for the frame may not be exact multiple of ystride x
    // height hence the capacity of the ByteBuffer could be less. To handle such situations it will
    // be better to transfer the exact amount of image bytes to the destination bytes.
      yinput.get(yinputPixels, 0, yinput.capacity());
      uinput.get(uinputPixels, 0, uinput.capacity());
      vinput.get(vinputPixels, 0, vinput.capacity());

    // Detect edges.

      byte[] outputArray = new byte[width*height*3];
      int index = 0;
    for (int j = 0; j < height; j++) {
      for (int i = 0; i < width; i++) {
          int r, g, b, y, u, v;
          y = yinputPixels[(j * width )+i ];
          u = uinputPixels[(j * width )+i ];
          v = vinputPixels[(j * width )+i ];
          r = (int) (y + (1.370705 * (v-128)));
          g = (int) ((y - (0.698001 * (v-128))) - (0.337633 * (u-128)));
          b = (int) (y + (1.732446 * (u-128)));

//          routputPixels [(j*width ) + i ] = (byte) Math.max(0, Math.min(255, r));
//          goutputPixels [(j*width ) + i ] = (byte) Math.max(0, Math.min(255, g));
//          boutputPixels [(j*width ) + i ] = (byte) Math.max(0, Math.min(255, b));
//          outputArray[index] = (byte) Math.max(0, Math.min(255, y));
//          outputArray[index+1] = (byte) Math.max(0, Math.min(255, 255));
//          outputArray[index+2] = (byte) Math.max(0, Math.min(255, y));
          outputArray[index] = (byte) 128;
          outputArray[index+1] = (byte) 0;
          outputArray[index+2] = (byte) 255;
//          outputArray[index] = (byte) Math.max(0, Math.min(255, y));
//          outputArray[index+1] = (byte) Math.max(0, Math.min(255, u));
//          outputArray[index+2] = (byte) Math.max(0, Math.min(255, v));
          index += 3;


//          routputPixels [(j*width ) + i ] =
//          goutputPixels [(j*width ) + i ] = uinputPixels[(j * width )+i ];
//          boutputPixels [(j*width ) + i ] = vinputPixels[(j * width )+i ];
        // Offset of the pixel at [i, j] of the yinput image.

//        int offset = (j * ystride) + i;
//
//        // Neighbour pixels around the pixel at [i, j].
//        int a00 = yinputPixels[offset - ystride - 1];
//        int a01 = yinputPixels[offset - ystride];
//        int a02 = yinputPixels[offset - ystride + 1];
//        int a10 = yinputPixels[offset - 1];
//        int a12 = yinputPixels[offset + 1];
//        int a20 = yinputPixels[offset + ystride - 1];
//        int a21 = yinputPixels[offset + ystride];
//        int a22 = yinputPixels[offset + ystride + 1];
//
//        // Sobel X filter:
//        //   -1, 0, 1,
//        //   -2, 0, 2,
//        //   -1, 0, 1
//        int xSum = -a00 - (2 * a10) - a20 + a02 + (2 * a12) + a22;
//
//        // Sobel Y filter:
//        //    1, 2, 1,
//        //    0, 0, 0,
//        //   -1, -2, -1
//        int ySum = a00 + (2 * a01) + a02 - a20 - (2 * a21) - a22;
//
//        if ((xSum * xSum) + (ySum * ySum) > SOBEL_EDGE_THRESHOLD) {
//          outputPixels[(j * width) + i] = (byte) 0xFF;
//        } else {
//          outputPixels[(j * width) + i] = (byte) 0x1F;
//        }
      }
    }
    return ByteBuffer.wrap(outputArray);
//    return new ByteBuffer[] {ByteBuffer.wrap(routputPixels), ByteBuffer.wrap(goutputPixels), ByteBuffer.wrap(boutputPixels)};
  }
}
