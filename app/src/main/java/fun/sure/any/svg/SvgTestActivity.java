package fun.sure.any.svg;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;

import fun.sure.any.R;
import fun.sure.any.common.activity.BaseActivity;
import fun.sure.any.common.util.SvgUtils;

/**
 * Created by wangshuo on 7/28/17.
 */

public class SvgTestActivity extends BaseActivity {

    private Button button;
    private ImageView imageView;
    private ImageView svgView;

    private int width;
    private int height;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        button = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.image);
        svgView = (ImageView) findViewById(R.id.svg);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });
    }

    private void loadImage() {
        try {
            InputStream is = getAssets().open("image_answer.jpg");
            Drawable drawable = Drawable.createFromStream(is, null);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            imageView.setImageBitmap(bitmap);
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    loadSvg();
                }
            });
        } catch (IOException e) {
        }
    }

    private void loadSvg() {
        float ratio = 1 / 3f;
        String svgData = readFromAssets("scratch.svg");
        Bitmap bitmap = SvgUtils.svg2Bitmap(svgData, width * ratio, height * ratio,
                NumberFormat.getPercentInstance().format(ratio));
        svgView.setImageBitmap(bitmap);
        svgView.setLayoutParams(new LayoutParams(imageView.getWidth(), imageView.getHeight()));
    }

    private String readFromAssets(String fileName) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            isr = new InputStreamReader(getAssets().open(fileName));
            input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null)
                    isr.close();
                if (fIn != null)
                    fIn.close();
                if (input != null)
                    input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.svg_activity_test;
    }
}
