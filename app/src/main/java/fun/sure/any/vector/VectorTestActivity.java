package fun.sure.any.vector;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import fun.sure.any.R;
import fun.sure.any.common.activity.BaseActivity;

/**
 * Created by wangshuo on 16-8-3.
 */
public class VectorTestActivity extends BaseActivity implements OnClickListener {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private ImageView simpleAnimView;
    private ImageView searchBarView;
    private ImageView drawer2ArrowView;
    private ImageView arrow2DrawerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        simpleAnimView = (ImageView) findViewById(R.id.simple_anim);
        searchBarView = (ImageView) findViewById(R.id.search_bar);
        drawer2ArrowView = (ImageView) findViewById(R.id.drawer_to_arrow);
        arrow2DrawerView = (ImageView) findViewById(R.id.arrow_to_drawer);

        simpleAnimView.setImageResource(R.drawable.vector_arrow);
        searchBarView.setImageResource(R.drawable.vector_searchbar);
        drawer2ArrowView.setImageResource(R.drawable.vector_drawer);
        arrow2DrawerView.setImageResource(R.drawable.vector_arrow);

        simpleAnimView.setOnClickListener(this);
        searchBarView.setOnClickListener(this);
        drawer2ArrowView.setOnClickListener(this);
        arrow2DrawerView.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.vector_activity_test;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.simple_anim) {
            startAnimatedVector(simpleAnimView, R.drawable.anim_vector_arrow_round, false);
        } else if (id == R.id.search_bar) {
            startAnimatedVector(searchBarView, R.drawable.anim_vector_searchbar, false);
        } else if (id == R.id.drawer_to_arrow) {
            startAnimatedVector(drawer2ArrowView, R.drawable.anim_vector_drawer_to_arrow, true);
        } else if (id == R.id.arrow_to_drawer) {
            startAnimatedVector(arrow2DrawerView, R.drawable.anim_vector_arrow_to_drawer, true);
        }
    }

    private void startAnimatedVector(ImageView view, int drawableId, boolean needMorph) {
        if (!isApiBelow21()) {
            AnimatedVectorDrawable morphing = (AnimatedVectorDrawable) getDrawable(drawableId);
            if (morphing != null) {
                view.setImageDrawable(morphing);
                morphing.start();
            }
        } else if (needMorph) {
            Toast.makeText(this, "无能为力", Toast.LENGTH_SHORT).show();
        } else {
            AnimatedVectorDrawableCompat drawableCompat =AnimatedVectorDrawableCompat.create(this, drawableId);
            if (drawableCompat != null) {
                view.setImageDrawable(drawableCompat);
                drawableCompat.start();
            }
        }
    }

    private boolean isApiBelow21() {
        return android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP;
    }
}
