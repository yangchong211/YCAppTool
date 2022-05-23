package com.yc.monitorfilelib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.yc.statusbar.bar.StateAppBar;

import java.util.ArrayDeque;


/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2021/8/11
 *     desc   : 文件管理
 *     revise :
 * </pre>
 */
public class FileExplorerActivity extends AppCompatActivity {

    /**
     * 存储fragment的队列。
     * 一个双端队列实现，不过它内部使用的是数组来对元素进行操作，不允许存储null值，同时可以当做队列，双端队列，栈来进行使用。
     */
    private final ArrayDeque<Fragment> mFragments = new ArrayDeque<>();
    private static final String TAG = "FileExplorerActivity";

    /**
     * 开启页面
     *
     * @param context 上下文
     */
    public static void startActivity(Context context) {
        try {
            Intent target = new Intent();
            target.setClass(context, FileExplorerActivity.class);
            target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(target);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_main);
        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorTheme));
        showContent(FileExplorerFragment.class);
    }

    public void showContent(Class<? extends Fragment> target) {
        showContent(target, null);
    }

    /**
     * 添加fragment
     *
     * @param target target对象
     * @param bundle bundle数据
     */
    public void showContent(Class<? extends Fragment> target, Bundle bundle) {
        try {
            Fragment fragment = target.newInstance();
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.add(R.id.fl_file_content, fragment);
            //push等同于addFirst，添加到第一个
            mFragments.push(fragment);
            //add等同于addLast，添加到最后
            //mFragments.add(fragment);
            fragmentTransaction.addToBackStack("");
            //将fragment提交到任务栈中
            fragmentTransaction.commit();
        } catch (InstantiationException exception) {
            FileExplorerUtils.logError(TAG + exception.toString());
        } catch (IllegalAccessException exception) {
            FileExplorerUtils.logError(TAG + exception.toString());
        }
    }

    /**
     * 处理fragment返回键逻辑
     */
    @Override
    public void onBackPressed() {
        if (!mFragments.isEmpty()) {
            Fragment fragment = mFragments.getFirst();
            if (fragment != null) {
                //移除最上面的一个
                mFragments.removeFirst();
            }
            super.onBackPressed();
            //如果fragment栈为空，则直接关闭activity
            if (mFragments.isEmpty()) {
                finish();
            }
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 回退fragment任务栈操作
     *
     * @param fragment fragment
     */
    public void doBack(Fragment fragment) {
        if (mFragments.contains(fragment)) {
            mFragments.remove(fragment);
            FragmentManager fm = getSupportFragmentManager();
            //回退fragment操作
            fm.popBackStack();
            if (mFragments.isEmpty()) {
                //如果fragment栈为空，则直接关闭宿主activity
                finish();
            }
        }
    }

}
