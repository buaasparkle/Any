package fun.sure.any.common.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.Log;

import com.caverock.androidsvg.SVG;

/**
 * Created by wangshuo on 7/28/17.
 */

public class SvgUtils {

    private SvgUtils() {
    }

    /**
     * Generate a bitmap from svg data.
     * @param string svg data
     * @param width bitmap width
     * @param height bitmap height
     * @param percent scale percent
     * @return bitmap
     */
    public static Bitmap svg2Bitmap(String string, float width, float height, String percent) {
        if (!TextUtils.isEmpty(string)) {
            try {
                SVG svg = SVG.getFromString(string);
                svg.setDocumentViewBox(0f, 0f, width, height);
                svg.setDocumentWidth(percent);
                svg.setDocumentHeight(percent);
                Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                canvas.drawARGB(0, 255, 255, 255);
                svg.renderToCanvas(canvas);
                return bitmap;
            } catch (Exception e) {
                Log.e("svg", "svg2Bitmap failed", e);
            }
        }
        return null;
    }
}
