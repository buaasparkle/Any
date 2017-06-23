package fun.sure.any.ubb;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

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
}
