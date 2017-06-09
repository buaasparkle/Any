package fun.sure.any;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fun.sure.any.gif.GifListTestActivity;
import fun.sure.any.keyboard.KeyboardTestActivity;
import fun.sure.any.vector.VectorTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toGifTest();;
    }

    private void toKeyboardTest() {
        Intent intent = new Intent(this, KeyboardTestActivity.class);
        startActivity(intent);
    }

    private void toVectorTest() {
        Intent intent = new Intent(this, VectorTestActivity.class);
        startActivity(intent);
        finish();
    }

    private void toGifTest() {
        Intent intent = new Intent(this, GifListTestActivity.class);
        startActivity(intent);
        finish();
    }
}
