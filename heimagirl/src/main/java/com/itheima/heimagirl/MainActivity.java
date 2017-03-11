package com.itheima.heimagirl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.itheima.heimagirl.Bean.GirlBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener {
    private static final String TAG = "MainActivity";
    private Gson mgson = new Gson();
    @BindView(R.id.list_view)
    ListView mListView;
    private List<GirlBean.ResultsBean> mList = new ArrayList<>();
    private GirlAdapter mAdapter;
    private boolean isLoading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        sendAsynRequest();
    }

    private void initData() {
        mAdapter = new GirlAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(this);
    }


    private void sendRequest() {
        new Thread(new Runnable() {
            public void run() {
                String url = new String("http://gank.io/api/data/福利/10/1");
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().get().url(url).build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
                    Log.d(TAG, "run: "+response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    private void sendAsynRequest() {
        new Thread(new Runnable() {
            public void run() {
                String url = new String("http://gank.io/api/data/福利/10/1");
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().get().url(url).build();

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d(TAG, "onFailure: 请求失败了");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            GirlBean girlBean = mgson.fromJson(result, GirlBean.class);
                            mList.addAll(girlBean.getResults());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    });

            }
        }).start();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(SCROLL_STATE_IDLE == scrollState) {
            if(mListView.getLastVisiblePosition() == mList.size() - 1) {
                loadMoreData();
            }
        }
    }

    private void loadMoreData() {
        if(isLoading) {
            return;
        }
        new Thread(new Runnable() {
            public void run() {
                isLoading = true;
                int index = mList.size() / 10 + 1;
                String url = new String("http://gank.io/api/data/福利/10/"+index);
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().get().url(url).build();

                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: 请求失败了");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        GirlBean girlBean = mgson.fromJson(result, GirlBean.class);
                        mList.addAll(girlBean.getResults());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                isLoading = false;
                            }
                        });
                    }
                });

            }
        }).start();
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    private  class GirlAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if(convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(MainActivity.this,R.layout.item_girl_view,null);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder.ivGirl = (ImageView) convertView.findViewById(R.id.iv_girl);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            GirlBean.ResultsBean resultsBean = mList.get(position);
            holder.tvTime.setText(resultsBean.getPublishedAt());
            String url = resultsBean.getUrl();
            Glide.with(MainActivity.this).load(url)
                    .bitmapTransform(new CropCircleTransformation(MainActivity.this)).into(holder.ivGirl);
            return convertView;
        }
    }

    static class ViewHolder {
        TextView tvTime;
        ImageView ivGirl;
    }
}
