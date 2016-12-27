package io.itit.event;

import com.tencent.mm.sdk.modelbase.BaseResp;

/**
 * Created by Lee_3do on 16/7/23.
 */

public class WxPayEvent {
    public BaseResp req;

    public WxPayEvent(BaseResp req) {
        this.req = req;
    }
}
