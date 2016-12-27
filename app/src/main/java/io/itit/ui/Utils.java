package io.itit.ui;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

/**
 * Created by Lee_3do on 2016/12/27.
 */

public class Utils {
    public static MaterialDialog generateWaitingDialog(String name, Context context) {
        MaterialDialog progressDialog = new MaterialDialog.Builder(context).theme(Theme.LIGHT)
                .title(name).content("请稍候").progress(true, 0).show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        return progressDialog;
    }
}
