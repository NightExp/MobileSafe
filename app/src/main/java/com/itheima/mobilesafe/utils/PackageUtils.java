package com.itheima.mobilesafe.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.itheima.mobilesafe.bean.AppBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */

public class PackageUtils {

    private static File file;

    public static List<AppBean> getAppInfo(Context context) {
        List<AppBean> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo packageInfo : installedPackages) {
            ApplicationInfo info = packageInfo.applicationInfo;
            String label = info.loadLabel(pm).toString();
            Drawable icon = info.loadIcon(pm);
            String sourceDir = info.sourceDir;
            AppBean bean = new AppBean();
            bean.label = label;
            bean.icon = icon;
            bean.apkPath = sourceDir;
            bean.installSD = isInstallSD(info);
            list.add(bean);
        }
        return list;
    }

    /**判断是否安装在SD卡中
     * @param info
     * @return	true表示安装在sd卡中,false表示安装在内置ROM中
     */
    public static boolean isInstallSD(ApplicationInfo info) {
        int flags = info.flags;
        return (ApplicationInfo.FLAG_EXTERNAL_STORAGE & flags) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;
    }
}
