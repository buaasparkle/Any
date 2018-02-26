package fun.sure.any.ping;


import com.yuantiku.android.common.data.BaseData;

/**
 * Created by wangshuo on 23/02/2018.
 */

public class PingConfig extends BaseData {

    private String destHost;
    private int timeout; // second
    private int ttl; // ip包的ttl, 如果是直接trace到dest_host, 填64

    public PingConfig(String destHost, int timeout, int ttl) {
        this.destHost = destHost;
        this.timeout = timeout;
        this.ttl = ttl;
    }

    public String getDestHost() {
        return destHost;
    }

    public int getTimeout() {
        return timeout;
    }

    public int getTtl() {
        return ttl;
    }
}
