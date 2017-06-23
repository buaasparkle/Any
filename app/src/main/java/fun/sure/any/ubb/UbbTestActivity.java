package fun.sure.any.ubb;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.yuantiku.android.common.ubb.view.UbbView;

import org.json.JSONException;
import org.json.JSONObject;

import fun.sure.any.R;
import fun.sure.any.common.activity.BaseActivity;

/**
 * Created by wangshuo on 6/23/17.
 */

public class UbbTestActivity extends BaseActivity {

    private UbbView ubbView;

    private static final String UBB = "[p]泥（ní）古    喋血（xuè）    胳臂（bei）    丢三落（là）四[/p]";

    private static final String UBB_JSON =
//            "{\"name\":\"doc\",\"value\":\"\",\"children\":[{\"name\":\"p\",\"value\":\"\","
//                    + "\"children\":[{\"name\":\"txt\",\"value\":\"泥（ní）古    喋血（xuè）    胳臂（bei）    丢三落（là）四\","
//                    + "\"children\":[]}]}]}";

//    "{\"name\":\"doc\",\"value\":\"\",\"children\":[{\"name\":\"p\",\"value\":\"\",\"children\":[{\"name\":\"txt\","
//            + "\"value\":\"尽管下着大雪，人们\",\"children\":[]},{\"name\":\"u\",\"value\":\"\","
//            + "\"children\":[{\"name\":\"txt\",\"value\":\"万人空巷\",\"children\":[]}]},{\"name\":\"txt\","
//            + "\"value\":\"地出来欢迎我们，他们    从四面八方围拢上来，争着向我们献花，同我们握手。\",\"children\":[]}]}]}";

            " {\"name\":\"doc\",\"value\":\"\",\"children\":[{\"name\":\"p\",\"value\":\"\","
                    + "\"children\":[{\"name\":\"txt\","
                    + "\"value\":\"    Even though my parents were married for well over fifty years,"
                    + " I still felt that they stayed together \\\"for the sake of the children\\\""
                    + ". At eighty-five years old, Dad\",\"children\":[]},{\"name\":\"input\","
                    + "\"value\":\"type:choice,size:12,index:41\",\"children\":[]},{\"name\":\"txt\","
                    + "\"value\":\" a stroke(中风). While he was recovering in the hospital, I saw my parents'\","
                    + "\"children\":[]},{\"name\":\"input\",\"value\":\"type:choice,size:12,index:42\","
                    + "\"children\":[]},{\"name\":\"txt\",\"value\":\" shine through like never before"
                    + ". I watched Dad reach his hand out for Mom to\",\"children\":[]},{\"name\":\"input\","
                    + "\"value\":\"type:choice,size:12,index:43\",\"children\":[]},{\"name\":\"txt\",\"value\":\""
                    + ". I noticed he\",\"children\":[]},{\"name\":\"input\",\"value\":\"type:choice,size:12,"
                    + "index:44\",\"children\":[]},{\"name\":\"txt\",\"value\":\" pulled her toward him for a kis    "
                    + "s on the forehead. In my lifetime I had never witnessed these outward\",\"children\":[]},"
                    + "{\"name\":\"input\",\"value\":\"type:choice,size:12,index:45\",\"children\":[]},"
                    + "{\"name\":\"txt\",\"value\":\" of love by my parents.\",\"children\":[]}]},{\"name\":\"p\","
                    + "\"value\":\"\",\"children\":[{\"name\":\"txt\",\"value\":\"    Gradually, Dad's health\","
                    + "\"children\":[]},{\"name\":\"input\",\"value\":\"type:choice,size:12,index:46\",\"children"
                    + "\":[]},{\"name\":\"txt\","
                    + "\"value\":\" and soon after his eighty-eighth birthday he was hospitalized"
                    + ". During the next four months, he spent only a few days at home, in between\","
                    + "\"children\":[]},{\"name\":\"input\",\"value\":\"type:choice,size:12,index:47\","
                    + "\"children\":[]},{\"name\":\"txt\",\"value\":\" to the hospital and nursing homes"
                    + ". The doctors never found out exactly what the\",\"children\":[]},{\"name\":\"input\","
                    + "\"value\":\"type:choice,size:12,index:48\",\"children\":[]},{\"name\":\"txt\","
                    + "\"value\":\" was, but we knew his time here with us was\",\"children\":[]},"
                    + "{\"name\":\"input\",\"value\":\"type:choice,size:12,index:49\",\"children\":[]},"
                    + "{\"name\":\"txt\",\"value\":\".\",\"children\":[]}]},{\"name\":\"p\",\"value\":\"\","
                    + "\"children\":[{\"name\":\"txt\",\"value\":\"    Mum would visit Dad faithfully every day,"
                    + " and sometimes\",\"children\":[]},{\"name\":\"input\",\"value\":\"type:choice,size:12,"
                    + "index:50\",\"children\":[]},{\"name\":\"txt\",\"value\":\" twice a day. Of course, she\","
                    + "\"children\":[]},{\"name\":\"input\",\"value\":\"type:choice,size:12,index:51\","
                    + "\"children\":[]},{\"name\":\"txt\","
                    + "\"value\":\" made sure he had the cleanest laundry and the tastiest treats. While I was at work,"
                    + " Mom took the bus to the hospital to see him in the afternoon. She made certain the doctors,"
                    + " nurses and staff\",\"children\":[]},{\"name\":\"input\","
                    + "\"value\":\"type:choice,size:12,index:52\",\"children\":[]},{\"name\":\"txt\","
                    + "\"value\":\" him the finest care. Each day he would reach out to hold Mom's hand.\","
                    + "\"children\":[]}]},{\"name\":\"p\",\"value\":\"\",\"children\":[{\"name\":\"txt\","
                    + "\"value\":\"    The next day, Mom and I bought anniversary cards and flowers to\","
                    + "\"children\":[]},{\"name\":\"input\",\"value\":\"type:choice,size:12,index:53\","
                    + "\"children\":[]},{\"name\":\"txt\",\"value\":\" with Dad. That evening Mom and I went out for "
                    + "    a(n)\",\"children\":[]},{\"name\":\"input\",\"value\":\"type:choice,size:12,index:54\","
                    + "\"children\":[]},{\"name\":\"txt\",\"value\":\", but special anniversary dinner with my wife"
                    + ". The three of us sat at the table for four — the fourth place\",\"children\":[]},"
                    + "{\"name\":\"input\",\"value\":\"type:choice,size:12,index:55\",\"children\":[]},"
                    + "{\"name\":\"txt\",\"value\":\" Dad's loving presence.\",\"children\":[]}]},{\"name\":\"p\","
                    + "\"value\":\"\",\"children\":[{\"name\":\"txt\","
                    + "\"value\":\"    When Mom and I arrived at the hospital the following afternoon, Dad's nurse\","
                    + "\"children\":[]},{\"name\":\"input\",\"value\":\"type:choice,size:12,index:56\","
                    + "\"children\":[]},{\"name\":\"txt\",\"value\":\" us in the hallway. He stood\","
                    + "\"children\":[]},{\"name\":\"input\",\"value\":\"type:choice,size:12,index:57\","
                    + "\"children\":[]},{\"name\":\"txt\",\"value\":\" in front of the"
                    + " door and reached out his hand, placing it on my shoulder. There was no need for\","
                    + "\"children\":[]},{\"name\":\"input\",\"value\":\"type:choice,size:12,index:58\","
                    + "\"children\":[]},{\"name\":\"txt\",\"value\":\". We knew Dad was\",\"children\":[]},"
                    + "{\"name\":\"input\",\"value\":\"type:choice,size:12,index:59\",\"children\":[]},"
                    + "{\"name\":\"txt\",\"value\":\". Mom held Dad's hand and gave him one last\",\"children\":[]},"
                    + "{\"name\":\"input\",\"value\":\"type:choice,size:12,index:60\",\"children\":[]},"
                    + "{\"name\":\"txt\",\"value\":\", as their love shined through.\",\"children\":[]}]}]}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        ubbView = (UbbView) findViewById(R.id.ubb);
        try {
            JSONObject ubbJson = new JSONObject(UBB_JSON);
            ubbView.render(UbbConverter.convertJson2String(ubbJson));
        } catch (JSONException e) {
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ubb_activity_test;
    }
}
