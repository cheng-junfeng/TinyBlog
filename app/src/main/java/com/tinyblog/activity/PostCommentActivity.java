package com.tinyblog.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.blankj.utilcode.utils.NetworkUtils;
import com.google.gson.Gson;
import com.tinyblog.R;
import com.tinyblog.base.BaseActivity;
import com.tinyblog.bean.PostCommentBean;
import com.tinyblog.sys.Constants;
import com.tinyblog.sys.Url;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * @author xiarui
 * @date 2017/1/14 19:19
 * @desc 文章评论页
 * @remark
 */
public class PostCommentActivity extends BaseActivity {
    private Toolbar mCommentTBar;
    //下拉刷新
    private SwipeRefreshLayout mCommentSRLayout;
    //停止刷新操作
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == Constants.REFRESH_SUCCESS) {
                if (mCommentSRLayout.isRefreshing()) {
                    mCommentSRLayout.setRefreshing(false);//设置不刷新
                }
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_post_comment;
    }

    @Override
    public void initView() {
        mCommentTBar = (Toolbar) findViewById(R.id.tb_post_comment);
        setSupportActionBar(mCommentTBar);
        //隐藏默认标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //添加返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCommentSRLayout = (SwipeRefreshLayout) findViewById(R.id.srl_post_comment);
        mCommentSRLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    @Override
    public void initData() {
        mCommentTBar.setTitle(getIntent().getStringExtra(Constants.CUR_POSTS_ID));
        mCommentTBar.setSubtitle(getIntent().getStringExtra(Constants.CUR_POSTS_COUNT) + " 条评论");
        //开启自动刷新
        mCommentSRLayout.post(new Runnable() {
            @Override
            public void run() {
                mCommentSRLayout.setRefreshing(true);
                //加载网络数据
                loaderNetWorkData();
            }
        });
    }

    /**
     * 加载网络数据
     */
    private void loaderNetWorkData() {
        OkHttpUtils
                .get()
                .url(Url.GET_POST_COMMENT + getIntent().getStringExtra(Constants.POST_DETAILS_ID))
                .build()
                .execute(new PostCommentCallBack());
    }

    private class PostCommentCallBack extends StringCallback {
        @Override
        public void onError(Call call, Exception e, int id) {
            if (!NetworkUtils.isConnected()) {
                showBaseToast("获取失败，请检查网络连接");
            }
        }

        @Override
        public void onResponse(String response, int id) {
            PostCommentBean postCommentBean = new Gson().fromJson(response, PostCommentBean.class);
            if (postCommentBean.getStatus().equals("ok")) {
                mHandler.sendEmptyMessage(Constants.REFRESH_SUCCESS);
                //获得数据后更新UI
                //updateUIFromNet(postDetailsBean);
                showBaseToast("成功");
            } else {
                showBaseToast("数据异常，请重新刷新");
            }
        }
    }

    @Override
    public void initEvents() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cur_posts_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_item_refresh:
                showBaseToast("刷新是没有用的——老者");
                return true;
        }
        return true;
    }
}