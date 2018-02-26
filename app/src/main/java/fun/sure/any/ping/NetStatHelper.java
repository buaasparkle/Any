package fun.sure.any.ping;

import android.util.Log;

import com.yuantiku.android.common.util.UnitUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangshuo on 26/02/2018.
 */

public class NetStatHelper {

    private static final String TAG = NetStatHelper.class.getSimpleName();

    private static final String FORMAT_PING_COUNT = "ping -c %d %s";
    private static final String FORMAT_PING_COUNT_TTL = "ping -c %d -t %d %s";

    private static final Pattern PATTERN_TRACE = Pattern.compile("(?<=From )(?:[0-9]{1,3}\\.){3}[0-9]{1,3}");
    private static final Pattern PATTERN_PING_IP = Pattern.compile("(?<=from ).*(?=: icmp_seq=1 ttl=)");
    private static final Pattern PATTERN_PING_TIME = Pattern.compile("(?<=time=)(.*?)ms");

    private NetStatHelper() {
    }

    public static PingResult netStatPing(PingConfig pingConfig) {
        // init ping result
        PingResult pingResult = new PingResult();
        pingResult.setTimeout(true);

        // ping or tracert
        if (pingConfig != null) {
            String result;
            String cmd = String.format(Locale.getDefault(),
                    FORMAT_PING_COUNT_TTL, 1, pingConfig.getTtl(), pingConfig.getDestHost());
            result = execPing(cmd, pingConfig.getTimeout() * UnitUtils.SECOND);
            Log.e(TAG, "netStatPing1: cmd = " + cmd);
            Log.e(TAG, "netStatPing1: timeout = " + pingConfig.getTimeout());
            Log.e(TAG, "netStatPing1: result = " + result);

            // check ttl exceeded
            Matcher traceMatcher = PATTERN_TRACE.matcher(result);
            if (traceMatcher.find()) {
                // 如果获得trace:IP，表明ttl超出，需再次发送ping命令获取ping的时间
                // e.g. From 140.205.27.226: icmp_seq=1 Time to live exceeded
                String pingIp = traceMatcher.group();
                cmd = String.format(Locale.getDefault(), FORMAT_PING_COUNT, 1, pingIp);
                result = execPing(cmd, pingConfig.getTimeout() * UnitUtils.SECOND);
                Log.e(TAG, "netStatPing2: cmd = " + cmd);
                Log.e(TAG, "netStatPing2: result = " + result);
            }
            // ping response
            Matcher pingMatcher = PATTERN_PING_IP.matcher(result);
            if (pingMatcher.find()) {
                String pingIp = pingMatcher.group();
                pingResult.setHost(pingIp);
                Log.e(TAG, "netStatPing: ip = " + pingIp);
                Matcher matcherTime = PATTERN_PING_TIME.matcher(result);
                if (matcherTime.find()) {
                    String time = matcherTime.group(1).trim();
                    pingResult.setRtt(Float.valueOf(time).intValue());
                    pingResult.setTimeout(false);
                    Log.e(TAG, "netStatPing: time = " + time);
                }
            }
        }
        return pingResult;
    }

    private static String execPing(String pingCommand, long timeout) {
        Process process = null;
        ProcessWorker worker = null;
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            // process
            process = Runtime.getRuntime().exec(pingCommand);
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            // worker for timeout
            worker = new ProcessWorker(process);
            worker.start();
            worker.join(timeout);
            if (worker.getStatus() == null) { // timeout
                sb.setLength(0); // clear
            }
        } catch (Exception e) {
            sb.setLength(0);
            if (worker != null) {
                worker.interrupt();
            }
            Log.e(TAG, "execPing: exception", e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                Log.e(TAG, "execPing: finally Exception", e);
            }
        }
        return sb.toString();
    }

    // Wrap process in a Thread in case of checking timeout
    private static class ProcessWorker extends Thread {

        private final Process process;
        private Integer status;

        ProcessWorker(Process process) {
            this.process = process;
        }

        public Integer getStatus() {
            return status;
        }

        @Override
        public void run() {
            try {
                status = process.waitFor();
            } catch (InterruptedException e) {
            }
        }

        @Override
        public void interrupt() {
            if (process != null) {
                process.destroy();
            }
            super.interrupt();
        }
    }
}
