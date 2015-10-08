package riknijessen.nl.bonwiebetaaltwat;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;

/**
 * Created by rik on 08/10/15.
 */
public class ImagePreprocessor {
    public static Bitmap preProcess(Bitmap bitmap)  {
        int [] pixels = new int [bitmap.getHeight()*bitmap.getWidth()];

        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

//        for(int i = 0; i < pixels.length; i++)
//        {
//            if((pixels[i] & 0x00FF0000) > 0x00630000 || (pixels[i] & 0x0000FF00) > 0x00006300 || (pixels[i] & 0x000000FF) > 0x00000063)
//            {
//                pixels[i] = Color.WHITE;
//            }
//            else    {
//                pixels[i] = Color.BLACK;
//            }
//        }
        // scan through all pixels
        for(int i = 0; i < pixels.length; i++)
        {
            // get pixel color
            int pixel = pixels[i];
            int A = Color.alpha(pixel);
            int R = Color.red(pixel);
            int G = Color.green(pixel);
            int B = Color.blue(pixel);
            int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);

            // use 128 as threshold, above -> white, below -> black
            if (gray > 80)
                gray = 255;
            else
                gray = 0;
            // set new pixel color to output bitmap
            pixels[i] = Color.argb(A, gray, gray, gray);

        }

        bitmap.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        return bitmap;
    }

    public static Bitmap rotate(Bitmap bitmap, float rotate)  {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        // Setting pre rotate
        Matrix mtx = new Matrix();
        mtx.preRotate(rotate);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, false);
    }

    public static Bitmap RuiHuaBitmap(Bitmap bitmap) {
        int width, height;
        height = bitmap.getHeight();
        width = bitmap.getWidth();
        int red, green, blue;
        int a1, a2, a3, a4, a5, a6, a7, a8, a9;
        Bitmap bmpBlurred = Bitmap.createBitmap(width, height, bitmap.getConfig());

        Canvas canvas = new Canvas(bmpBlurred);

        canvas.drawBitmap(bitmap, 0, 0, null);
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {

                a1 = bitmap.getPixel(i - 1, j - 1);
                a2 = bitmap.getPixel(i - 1, j);
                a3 = bitmap.getPixel(i - 1, j + 1);
                a4 = bitmap.getPixel(i, j - 1);
                a5 = bitmap.getPixel(i, j);
                a6 = bitmap.getPixel(i, j + 1);
                a7 = bitmap.getPixel(i + 1, j - 1);
                a8 = bitmap.getPixel(i + 1, j);
                a9 = bitmap.getPixel(i + 1, j + 1);
                red = (Color.red(a1) + Color.red(a2) + Color.red(a3) + Color.red(a4) + Color.red(a6) + Color.red(a7) + Color.red(a8) + Color.red(a9)) *(-1)   + Color.red(a5)*9 ;
                green = (Color.green(a1) + Color.green(a2) + Color.green(a3) + Color.green(a4) + Color.green(a6) + Color.green(a7) + Color.green(a8) + Color.green(a9)) *(-1)  + Color.green(a5)*9 ;
                blue = (Color.blue(a1) + Color.blue(a2) + Color.blue(a3) + Color.blue(a4) + Color.blue(a6) + Color.blue(a7) + Color.blue(a8) + Color.blue(a9)) *(-1)   + Color.blue(a5)*9 ;
                if(red>255) red = 255;
                if(red<0) red= 0;
                if(blue>255) blue = 255;
                if(blue<0) blue = 0;
                if(green>255) green = 255;
                if(green<0) green = 0;
                bmpBlurred.setPixel(i, j, Color.rgb(red, green, blue));
            }
        }
        return bmpBlurred;
    }
}
