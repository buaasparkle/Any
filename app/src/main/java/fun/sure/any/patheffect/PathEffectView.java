package fun.sure.any.patheffect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangshuo on 20/04/2018.
 */

public class PathEffectView extends View {

    public static final int PATH_COUNT = 4;

    private Paint paint;
    private Path[] path;
    private PathEffect pathEffect;

    private Path curvePath;

    public PathEffectView(Context context) {
        this(context, null);
    }

    public PathEffectView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathEffectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setStrokeWidth(3);
        pathEffect = new DashPathEffect(new float[]{10f, 10f}, 0);

        path = new Path[PATH_COUNT];
        for (int i = 0; i < PATH_COUNT; i++) {
            path[i] = new Path();
            path[i].moveTo(100 * i, 100 * i);
            path[i].lineTo(100 * i + 100, 100 * i + 100);
            path[i].close();
        }

        curvePath = new Path();
        curvePath.moveTo(100, 100);
        curvePath.quadTo(0, 200, 150, 150);
        curvePath.close();
        curvePath.moveTo(150, 150);
        curvePath.quadTo(50, 50, 500, 600);
        curvePath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
//        for (int i = 0; i < PATH_COUNT; i++) {
//            paint.setPathEffect(i % 2 == 0 ? pathEffect : null);
//            canvas.drawPath(path[i], paint);
//        }
        paint.setPathEffect(pathEffect);
        canvas.drawPath(curvePath, paint);
    }
}
