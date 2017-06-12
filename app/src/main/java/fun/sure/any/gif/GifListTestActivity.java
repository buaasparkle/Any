package fun.sure.any.gif;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.RecyclerListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fun.sure.any.R;
import fun.sure.any.common.activity.BaseActivity;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by wangshuo on 6/8/17.
 */

public class GifListTestActivity extends BaseActivity {

    private static final String[] GIF_LIST = {
//            "http://i.imgur.com/rFLNqWI.jpg",
//            "http://i.imgur.com/C9pBVt7.jpg",
//            "http://i.imgur.com/rT5vXE1.jpg",
//            "http://i.imgur.com/aIy5R2k.jpg",
//            "http://i.imgur.com/MoJs9pT.jpg",

            "http://i.kinja-img.com/gawker-media/image/upload/s--B7tUiM5l--/gf2r69yorbdesguga10i.gif",
            "https://media.giphy.com/media/kWW2kEDCu7O6s/giphy.gif",
            "https://media.giphy.com/media/3og0IMX3eAbHe5rPGg/giphy.gif",
            "https://media.giphy.com/media/3og0IKJzFIkChLOY1i/giphy.gif",
            "https://media.giphy.com/media/3ohzdRmJspKrpKjL5C/giphy.gif",
            "https://media.giphy.com/media/WuGSL4LFUMQU/giphy.gif",
            "https://media.giphy.com/media/xB1RqLGoBNh3W/giphy.gif",
            "https://media.giphy.com/media/g9y1QV8lltulG/giphy.gif",
            "https://media.giphy.com/media/3ohzdVLdumYpfcZnig/giphy.gif"
    };

    private static final String GIF_ASSET_FORMAT = "200w_d_%d.gif";

    private ListView listView;
    private InnerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        // GIF ASSET LIST
        final List<String> GIF_ASSET_LIST = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            GIF_ASSET_LIST.add(String.format(GIF_ASSET_FORMAT, i));
        }
        // Gifs
        List<String> gifs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
//            gifs.addAll(Arrays.asList(GIF_LIST));
            gifs.addAll(GIF_ASSET_LIST);
        }
        adapter = new InnerAdapter(gifs);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setRecyclerListener(new RecyclerListener() {
            @Override
            public void onMovedToScrapHeap(View view) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.git_activity_list;
    }

    private class InnerAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<String> gifs;

        InnerAdapter(@NonNull List<String> gifs) {
            this.gifs = gifs;
            inflater = LayoutInflater.from(GifListTestActivity.this);
        }

        @Override
        public int getCount() {
            return gifs.size();
        }

        @Override
        public Object getItem(int position) {
            return gifs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.gif_adapter_item, parent, false);
            }

            String url = (String) getItem(position);

//            Glide
//            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
//            Glide.with(GifListTestActivity.this)
//                    .load(url) // from http
//                    .load(assetUrl(url)) // from assets
//                    .asGif()
//                    .dontAnimate()
//                    .placeholder(R.mipmap.placeholder)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(imageView);

//            Fresco
//            SimpleDraweeView draweeView = (SimpleDraweeView) convertView.findViewById(R.id.simple_drawee);
//            DraweeController controller = Fresco.newDraweeControllerBuilder()
//                    .setUri(Uri.parse(url))
//                    .setAutoPlayAnimations(true)
//                    .build();
//            draweeView.setController(controller);


//            WebView
//            WebView webView = (WebView) convertView.findViewById(R.id.web);
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//            webView.clearCache(false);
//            webView.loadUrl(assetUrl(url));

//            Gif ImageView
            GifImageView gifImageView = (GifImageView) convertView.findViewById(R.id.gif_image);
//            if (gifImageView.getDrawable() instanceof GifDrawable) {
//                GifDrawable gifDrawable = (GifDrawable) gifImageView.getDrawable();
//                if (gifDrawable.isRunning()) {
//                    gifDrawable.stop();
//                }
//            }
            gifImageView.setImageDrawable(null);
            try {
                GifDrawable curGifDrawable = new GifDrawable(GifListTestActivity.this.getAssets(), url);
                gifImageView.setImageDrawable(curGifDrawable);
            } catch (IOException e) {
                Log.e("GIF", e.toString());
            }

            return convertView;
        }
    }

    private static String assetUrl(String url) {
        return "file:///android_asset/" + url;
    }
}
