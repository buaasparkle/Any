package fun.sure.any.ping;


import com.yuantiku.android.common.data.BaseData;

/**
 * Created by wangshuo on 23/02/2018.
 */

public class PingResult extends BaseData {

    private boolean timeout;
    private String host;
    private int rtt; // ms

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getRtt() {
        return rtt;
    }

    public void setRtt(int rtt) {
        this.rtt = rtt;
    }

    @Override
    public String toString() {
        return "timeout = " + timeout +
                ", host = " + host +
                ", rtt = " + rtt;
    }
}
