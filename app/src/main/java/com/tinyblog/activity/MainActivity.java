package com.tinyblog.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.tinyblog.R;
import com.tinyblog.base.BaseActivity;
import com.tinyblog.fragment.CategoryFragment;
import com.tinyblog.fragment.MeFragment;
import com.tinyblog.fragment.NewsFragment;
import com.tinyblog.fragment.FindFragment;
import com.tinyblog.sys.Constants;

/**
 * @author xiarui
 * @date 2016/10/10 13:32
 * @desc 主页面
 */
public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    /*======== 控件相关 ========*/
    private BottomNavigationBar mBottomBar;         //底部导航栏

    /*======== 数据相关 ========*/
    private Fragment mCurFragment;                  //当前显示的 Fragment
    private NewsFragment newsFragment;              //主页
    private CategoryFragment categoryFragment;      //分类页
    private FindFragment findFragment;          //搜索页
    private MeFragment meFragment;                  //我的页面
    private FragmentTransaction mFTransaction;      //事务

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        mBottomBar = (BottomNavigationBar) findViewById(R.id.bnb_main);
        mBottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
    }

    @Override
    public void initData() {
        //设置底部导航栏
        mBottomBar.addItem(new BottomNavigationItem(R.drawable.svg_recent, "最近").setActiveColorResource(R.color.md_blue_400))
                .addItem(new BottomNavigationItem(R.drawable.svg_cate, "分类").setActiveColorResource(R.color.md_blue_400))
                .addItem(new BottomNavigationItem(R.drawable.svg_find, "发现").setActiveColorResource(R.color.md_blue_400))
                .addItem(new BottomNavigationItem(R.drawable.svg_me, "我的").setActiveColorResource(R.color.md_blue_400))
                .setFirstSelectedPosition(0)
                .initialise();

        //设置初始化 Fragment
        mFTransaction = getSupportFragmentManager().beginTransaction();
        //注意这里初始化的 Fragment 一定要跟上面的 setFirstSelectedPosition 中默认选中的标签一致
        newsFragment = new NewsFragment();
        mFTransaction.add(R.id.fl_fragment_content, newsFragment).commit();
        mCurFragment = newsFragment;
    }

    @Override
    public void initEvents() {
        mBottomBar.setTabSelectedListener(this);
    }

    /**
     * 底部导航栏标签选中监听事件
     *
     * @param position 标签位置
     */
    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                if (newsFragment == null)
                    newsFragment = new NewsFragment();
                switchFragmentContent(newsFragment);
                break;
            case 1:
                if (categoryFragment == null)
                    categoryFragment = new CategoryFragment();
                switchFragmentContent(categoryFragment);
                break;
            case 2:
                if (findFragment == null)
                    findFragment = new FindFragment();
                switchFragmentContent(findFragment);
                break;
            case 3:
                if (meFragment == null)
                    meFragment = new MeFragment();
                switchFragmentContent(meFragment);
                break;
        }
    }

    /**
     * 切换不同内容的 Fragment
     *
     * @param toFragment 需要切换的 Fragment
     */
    public void switchFragmentContent(Fragment toFragment) {
        if (mCurFragment != toFragment) {
            mFTransaction = getSupportFragmentManager().beginTransaction();
            // 判断是否被add过
            if (!toFragment.isAdded()) {
                // 如果没有 add ，隐藏当前 Fragment，add 需要展示的 Fragment
                mFTransaction.hide(mCurFragment).add(R.id.fl_fragment_content, toFragment).commit();
            } else {
                //如果已经 add ，隐藏当前 Fragment，直接 show 需要展示的 Fragment 即可
                mFTransaction.hide(mCurFragment).show(toFragment).commit();
            }
            //当前的主 Fragment 设置更改为已经展示的 Fragment
            mCurFragment = toFragment;
        }
    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {
    }

    //再按一次退出程序
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - Constants.EXIT_TIME) > 2000) {
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            Constants.EXIT_TIME = System.currentTimeMillis();
            return;
        }
        finish();
    }
}