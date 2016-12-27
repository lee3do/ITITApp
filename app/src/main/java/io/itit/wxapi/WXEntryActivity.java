package io.itit.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.greenrobot.eventbus.EventBus;

import cn.trinea.android.common.util.StringUtils;
import cn.trinea.android.common.util.ToastUtils;
import io.itit.ITITApplication;
import io.itit.db.DBHelper;
import io.itit.event.WxLoginEvent;
import io.itit.event.WxPayEvent;
import io.itit.http.HttpUtils;
import rx.schedulers.Schedulers;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = ITITApplication.msgApi;
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                break;
            default:
                break;
        }
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        String code = "";
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Logger.d("onPayFinish,errCode=" + resp.errCode);
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    ToastUtils.show(getApplicationContext(), "支付成功！");
                    EventBus.getDefault().post(new WxPayEvent(resp));
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    ToastUtils.show(getApplicationContext(), "支付取消！");
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    ToastUtils.show(getApplicationContext(), "支付拒绝！");
                    break;
                default:
                    ToastUtils.show(getApplicationContext(), "支付失败！");
                    break;
            }
        } else if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
//                    code = ((SendMessageToWX.Resp) resp).errCode;
//                    EventBus.getDefault().post(new WxShareEvent(true));
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    break;
                default:
                    break;
            }
        } else {
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    code = ((SendAuth.Resp) resp).code;
                    HttpUtils.appApis.wxAuth(generateWxUrl(code)).subscribeOn(Schedulers.io())
                            .observeOn(Schedulers.io()).filter(info -> StringUtils.isEmpty(info
                            .getErrcode())).flatMap(info -> HttpUtils.appApis.getWxUserinfo
                            (generateWXUserUrl(info.getAccess_token(), info.getOpenid()))).filter
                            (info -> StringUtils.isEmpty(info.getErrcode())).subscribe(info -> {
                        ITITApplication.uuid = info.getUnionid();
                        DBHelper.insertValue("USER", info.getUnionid());
                        DBHelper.insertValue("NAME", info.getNickname());
                        DBHelper.insertValue("HEAD", info.getHeadimgurl());
                        EventBus.getDefault().post(new WxLoginEvent(true, info.getUnionid()));
                    }, error -> {
                        EventBus.getDefault().post(new WxLoginEvent(false, error.getMessage()));
                    });
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    EventBus.getDefault().post(new WxLoginEvent(false, code));
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    EventBus.getDefault().post(new WxLoginEvent(false, code));
                    break;
                default:
                    break;
            }
        }
        finish();
    }

    private String generateWXUserUrl(String token, String openId) {
        return "https://api.weixin.qq.com/sns/userinfo?access_token=" + token + "&openid=" + openId;
    }

    private String generateWxUrl(String code) {
        return "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + ITITApplication
                .APP_ID + "&secret=" + ITITApplication.WECHAT_KEY + "&code=" + code +
                "&grant_type=authorization_code";
    }


}