package io.itit.event;

/**
 * Created by Lee_3do on 16/7/23.
 */

public class WxLoginEvent {
    public boolean isSuccess;
    public String code;

    public WxLoginEvent(boolean success, String code) {
        this.isSuccess = success;
        this.code = code;
    }
}
