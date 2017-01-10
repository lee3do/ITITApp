package io.itit.ui;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;

import java.io.File;

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

    public static void clearWebViewCache(Context context){

        //清理Webview缓存数据库
        try {
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir = new File(context.getDir("cache", Context.MODE_PRIVATE).getPath());

        File webviewCacheDir = new File(context.getCacheDir().getAbsolutePath()+"/webviewCache");

        //删除webview 缓存目录
        if(webviewCacheDir.exists()){
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if(appCacheDir.exists()){
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        } else {
        }
    }
}
