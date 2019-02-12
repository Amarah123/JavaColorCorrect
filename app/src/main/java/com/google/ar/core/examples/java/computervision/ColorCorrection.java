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
  public synchronized ByteBuffer[] correct(int width, int height, int ystride, ByteBuffer yinput, int ustride, ByteBuffer uinput, int vstride, ByteBuffer vinput) {
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
    byte[] youtputPixels = new byte[width * height];
      byte[] uoutputPixels = new byte[width * height];
      byte[] voutputPixels = new byte[width * height];

    // Copy yinput buffer into a java array for ease of access. This is not the most optimal
    // way to process an image, but used here for simplicity.
    yinput.position(0);

    // Note: On certain devices with specific resolution where the ystride is not equal to the width.
    // In such situation the memory allocated for the frame may not be exact multiple of ystride x
    // height hence the capacity of the ByteBuffer could be less. To handle such situations it will
    // be better to transfer the exact amount of image bytes to the destination bytes.
    yinput.get(yinputPixels, 0, yinput.capacity());

    // Detect edges.
    for (int j = 1; j < height - 1; j++) {
      for (int i = 1; i < width - 1; i++) {
        youtputPixels [(j*width ) + i ] = yinputPixels[(j * width )+i ];
          uoutputPixels [(j*width ) + i ] = uinputPixels[(j * width )+i ];
          voutputPixels [(j*width ) + i ] = vinputPixels[(j * width )+i ];
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
    return new ByteBuffer[] {ByteBuffer.wrap(youtputPixels), ByteBuffer.wrap(uoutputPixels), ByteBuffer.wrap(voutputPixels)};
  }
}
