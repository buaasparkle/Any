package fun.sure.any;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import fun.sure.any.keyboard.KeyboardTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toKeyboardTest();
    }

    private void toKeyboardTest() {
        Intent intent = new Intent(this, KeyboardTestActivity.class);
        startActivity(intent);
    }
}
