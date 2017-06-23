package fun.sure.any.ubb;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangshuo on 6/23/17.
 */

class UbbConverter {

    private UbbConverter() {}

    private static final Set<String> excludeTags = new HashSet<>();

    static {
        excludeTags.add("doc");
        excludeTags.add("txt");
    }

    static String convertJson2String(@NonNull JSONObject jsonObject) {
        StringBuilder sb = new StringBuilder();
        String name = jsonObject.optString("name");
        String value = jsonObject.optString("value");
        sb.append(openingTag(name, value));
        JSONArray childrenArray = jsonObject.optJSONArray("children");
        if (childrenArray != null && childrenArray.length() > 0) {
            for (int i = 0; i < childrenArray.length(); i++) {
                JSONObject object = childrenArray.optJSONObject(i);
                if (object != null) {
                    sb.append(convertJson2String(object));
                }
            }
        }
        sb.append(closingTag(name));
        return sb.toString();
    }

    private static String openingTag(String tag, String value) {
        if ("doc".equals(tag)) {
            return "";
        } else if ("txt".equals(tag)) {
            return value;
        } else {
            return "[" + tag + (TextUtils.isEmpty(value) ? "" : "=" + value) + "]";
        }
    }

    private static String closingTag(String tag) {
        return excludeTags.contains(tag) ? "" :"[/" + tag + "]";
    }

    final static Pattern PATTERN_TEX = Pattern.compile("(.*)\\?widthRatio=(.*)?&heightRatio=(.*)");

    private static String texTag(String value) {
        final String TAG = "[tex]";
        Matcher matcher = PATTERN_TEX.matcher(value);
        if (matcher.find()) {
            String texValue = matcher.group(1);
            float width = Float.valueOf(matcher.group(2));
            float height = Float.valueOf(matcher.group(3));
            return "[tex=" + width + "x" + height + "]" + texValue;
        }
        return TAG;
    }

    private final static Pattern PATTERN_IMG = Pattern.compile("(.*)\\?width=(.*)?&height=(.*)");

    private static String imgTag(String value) {
        final String TAG = "[img]";
        Matcher matcher = PATTERN_IMG.matcher(value);
        if (matcher.find()) {
            String texValue = matcher.group(1);
            int width = Integer.valueOf(matcher.group(2));
            int height = Integer.valueOf(matcher.group(3));
            return "[img=" + width + "x" + height + "]" + texValue;
        }
        return TAG;
    }

    public static void main(String[] args) {
        final String TEX_STRING = "uswT/CEcOIwMpCvTz/zeaA==?widthRatio=0.929&heightRatio=1.286";
        final String IMG_STRING = "1552ba097ece8c9.svg?width=223&height=269";
        System.out.print(UbbConverter.imgTag(IMG_STRING));

    }
}
