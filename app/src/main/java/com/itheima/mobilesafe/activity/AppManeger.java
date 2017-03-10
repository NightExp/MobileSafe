package com.itheima.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe.R;
import com.itheima.mobilesafe.bean.AppBean;
import com.itheima.mobilesafe.utils.PackageUtils;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9.
 */

public class AppManeger extends Activity {


    private MyAdapter mAdapter;
    private ListView mListView;
    private List<AppBean> mList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_maneger);
        initView();
        initData();
        Toast.makeText(this,"再来一次",Toast.LENGTH_SHORT).show();
    }



    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_app);

    }

    private void initData() {
        new File("`aa.txt");
        mList = PackageUtils.getAppInfo(this);
        mAdapter =  new MyAdapter();
        mListView.setAdapter(mAdapter);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return mList.size();
        }

        @Override
        public AppBean getItem(int position) {

            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(AppManeger.this,R.layout.item_app_maneger,null);
                holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_app_icon);
                holder.tvLabel = (TextView) convertView.findViewById(R.id.tv_app_name);
                holder.tvLocal = (TextView) convertView.findViewById(R.id.tv_app_local);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            AppBean bean = getItem(position);
            holder.ivIcon.setImageDrawable(bean.icon);
            holder.tvLabel.setText(bean.label);
            if(bean.installSD) {
                holder.tvLocal.setText("SD卡安装");
            }else {
                holder.tvLocal.setText("Rom安装");
            }
            return convertView;
        }
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvLabel;
        TextView tvLocal;
    }
}
