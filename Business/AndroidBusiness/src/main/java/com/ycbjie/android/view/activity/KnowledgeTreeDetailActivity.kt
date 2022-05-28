package com.ycbjie.android.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.yc.baseclasslib.adapter.BaseFragmentPagerAdapter
import com.yc.library.base.mvp.BaseActivity
import com.yc.statusbar.bar.StateAppBar
import com.yc.toastutils.ToastUtils
import com.ycbjie.android.R
import com.ycbjie.android.model.bean.TreeBean
import com.ycbjie.android.presenter.KnowledgeTreeDetailPresenter
import com.ycbjie.android.view.fragment.KnowledgeListFragment
import kotlinx.android.synthetic.main.base_tab_layout.*

/**
 * <pre>
 *     @author 杨充
 *     blog  :
 *     time  : 2017/05/30
 *     desc  : 知识体系列表
 *     revise:
 * </pre>
 */
class KnowledgeTreeDetailActivity : BaseActivity<KnowledgeTreeDetailPresenter>(){


    private var treeBean: TreeBean? = null
    private var initIndex: Int = 0
    private var fragments = mutableListOf<KnowledgeListFragment>()

    companion object {

        private const val TREE_BEAN = "tree_bean"
        private const val INDEX = "index"

        fun lunch(activity: AndroidActivity?, treeBean: TreeBean?, index: Int) {
            val intent = Intent(activity, KnowledgeTreeDetailActivity::class.java)
            intent.putExtra(TREE_BEAN, treeBean)
            intent.putExtra(INDEX, index)
            activity?.startActivity(intent)
        }
    }


    override fun getContentView(): Int {
        return R.layout.base_tab_layout
    }

    override fun initView() {
        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorTheme))
        getIntentData()
        initActionBar()
        initTabs()
    }

    override fun initListener() {

    }

    override fun initData() {

    }


    private fun getIntentData() {
        treeBean = intent.getSerializableExtra(TREE_BEAN) as TreeBean?
        initIndex = intent.getIntExtra(INDEX, 0)
        if (treeBean == null) {
            finish()
            return
        }
    }


    private fun initActionBar() {
        toolbar.run {
            toolbar.title = treeBean?.name
            setSupportActionBar(toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
        }
    }

    private fun initTabs() {
        val tabs: List<TreeBean> = treeBean!!.children!!
        for (child in tabs) {
            val fragment = KnowledgeListFragment()
            val bundle = Bundle()
            bundle.putSerializable(KnowledgeListFragment.TREE_BEAN, child)
            fragment.arguments = bundle
            fragments.add(fragment)
        }
        val adapter = BaseFragmentPagerAdapter(supportFragmentManager)
        adapter.addFragmentList(fragments as List<Fragment>?, null)
        viewPager.run {
            setAdapter(adapter)
            offscreenPageLimit = fragments.size
            setCurrentItem(initIndex, false)
        }
        tabLayout.run {
            tabMode = TabLayout.MODE_SCROLLABLE
            setupWithViewPager(viewPager)
        }
        for (value in tabs) tabLayout.getTabAt(tabs.indexOf(value))!!.text = value.name
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 0, 0, "添加到桌面")!!
                .setIcon(R.drawable.icon_home)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == 0) {
            //ShortCutUtils.addShortcut(this,treeBean!!,initIndex)
            ToastUtils.showRoundRectToast("添加shortcut成功")
        }
        return super.onOptionsItemSelected(item)
    }

}