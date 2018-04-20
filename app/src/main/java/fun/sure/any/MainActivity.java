package fun.sure.any;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import fun.sure.any.gif.GifListTestActivity;
import fun.sure.any.keyboard.KeyboardTestActivity;
import fun.sure.any.notificationpolicy.NotificationPolicyTestActivity;
import fun.sure.any.patheffect.PathEffectTestActivity;
import fun.sure.any.ping.PingTestActivity;
import fun.sure.any.svg.SvgTestActivity;
import fun.sure.any.textsize.TextSizeTestActivity;
import fun.sure.any.thread.ThreadTestActivity;
import fun.sure.any.ubb.UbbTestActivity;
import fun.sure.any.vector.VectorTestActivity;
import fun.sure.any.websocket.WebSocketTestActivity;
import fun.sure.any.webview.WebViewTestActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.action).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toPathEffectTest();
            }
        });
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

    private void toUbbTest() {
        Intent intent = new Intent(this, UbbTestActivity.class);
        startActivity(intent);
        finish();
    }

    private void toSvgTest() {
        Intent intent = new Intent(this, SvgTestActivity.class);
        startActivity(intent);
        finish();
    }

    private void toTextSizeTest() {
        Intent intent = new Intent(this, TextSizeTestActivity.class);
        startActivity(intent);
        finish();
    }

    private void toNotificationPolicyTest() {
        Intent intent = new Intent(this, NotificationPolicyTestActivity.class);
        startActivity(intent);
        finish();
    }

    private void toWebsocketTest() {
        Intent intent = new Intent(this, WebSocketTestActivity.class);
        startActivity(intent);
        finish();
    }

    private void toWebViewTest() {
        Intent intent = new Intent(this, WebViewTestActivity.class);
        startActivity(intent);
    }

    private void toThreadTest() {
        Intent intent = new Intent(this, ThreadTestActivity.class);
        startActivity(intent);
    }

    private void toPingTest() {
        Intent intent = new Intent(this, PingTestActivity.class);
        startActivity(intent);
    }

    private void toPathEffectTest() {
        Intent intent = new Intent(this, PathEffectTestActivity.class);
        startActivity(intent);
    }
}
