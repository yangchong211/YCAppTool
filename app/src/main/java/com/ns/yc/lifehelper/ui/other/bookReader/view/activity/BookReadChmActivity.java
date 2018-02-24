package com.ns.yc.lifehelper.ui.other.bookReader.view.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ns.yc.lifehelper.R;
import com.ns.yc.lifehelper.api.constant.ConstantBookReader;
import com.ns.yc.lifehelper.base.mvp1.BaseActivity;
import com.ns.yc.lifehelper.weight.chmview.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import butterknife.Bind;


/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/29
 * 描    述：页面chm
 * 修订历史：
 * ================================================
 */
public class BookReadChmActivity extends BaseActivity {


    @Bind(R.id.ll_title_menu)
    FrameLayout llTitleMenu;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.pb)
    ProgressBar pb;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<String> listSite = new ArrayList<>();
    private ArrayList<String> listBookmark;
    private String extractPath, md5File;
    private String filePath;
    private String fileName;

    @Override
    public int getContentView() {
        return R.layout.base_web_view;
    }

    @Override
    public void initView() {
        initToolBar();
        Utils.chm = null;
        initWebView();
        initFile();
    }


    private void initToolBar() {
        filePath = Uri.decode(getIntent().getDataString().replace("file://", ""));
        fileName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.lastIndexOf("."));
        toolbarTitle.setText(fileName);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);//在菜单中找到对应控件的item
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                webView.clearMatches();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                webView.findAllAsync(newText);
                try {
                    for (Method m : WebView.class.getDeclaredMethods()) {
                        if (m.getName().equals("setFindIsUp")) {
                            m.setAccessible(true);
                            m.invoke(webView, true);
                            break;
                        }
                    }
                } catch (Exception ignored) {
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void initWebView() {
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
    }

    private void initFile() {
        new AsyncTask<Void, Void, Void>() {
            int historyIndex = 1;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                md5File = Utils.checkSum(filePath);
                extractPath = ConstantBookReader.PATH_CHM + "/" + md5File;
                if (!(new File(extractPath).exists())) {
                    if (Utils.extract(filePath, extractPath)) {
                        try {
                            listSite = Utils.domparse(filePath, extractPath, md5File);
                            listBookmark = Utils.getBookmark(extractPath, md5File);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        (new File(extractPath)).delete();
                    }
                } else {
                    listSite = Utils.getListSite(extractPath, md5File);
                    listBookmark = Utils.getBookmark(extractPath, md5File);
                    historyIndex = Utils.getHistory(extractPath, md5File);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                webView.loadUrl("file://" + extractPath + "/" + listSite.get(historyIndex));

            }
        }.execute();
    }


    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            pb.setProgress(newProgress);
            if (newProgress == 100) {
                pb.setVisibility(View.GONE);
            }
        }
    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!url.startsWith("http") && !url.endsWith(md5File)) {
                String temp = url.substring("file://".length());
                if (!temp.startsWith(extractPath)) {
                    url = "file://" + extractPath + temp;
                }
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            if (!url.startsWith("http") && !url.endsWith(md5File)) {
                String temp = url.substring("file://".length());
                if (!temp.startsWith(extractPath)) {
                    url = "file://" + extractPath + temp;
                }
            }
            super.onLoadResource(view, url);
        }


        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String url = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                url = request.getUrl().toString();
            }
            if (url != null && !url.startsWith("http") && !url.endsWith(md5File)) {
                String temp = url.substring("file://".length());
                String insideFileName;
                if (!temp.startsWith(extractPath)) {
                    url = "file://" + extractPath + temp;
                    insideFileName = temp;
                } else {
                    insideFileName = temp.substring(extractPath.length());
                }
                if (insideFileName.contains("#")) {
                    insideFileName = insideFileName.substring(0, insideFileName.indexOf("#"));
                }
                if (insideFileName.contains("?")) {
                    insideFileName = insideFileName.substring(0, insideFileName.indexOf("?"));
                }
                if (insideFileName.contains("%20")) {
                    insideFileName = insideFileName.replaceAll("%20", " ");
                }
                if (url.endsWith(".gif") || url.endsWith(".jpg") || url.endsWith(".png")) {
                    try {
                        return new WebResourceResponse("image/*", "", Utils.chm.getResourceAsStream(insideFileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return super.shouldInterceptRequest(view, request);
                    }
                } else if (url.endsWith(".css") || url.endsWith(".js")) {
                    try {
                        return new WebResourceResponse("", "", Utils.chm.getResourceAsStream(insideFileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return super.shouldInterceptRequest(view, request);
                    }
                } else {
                    Utils.extractSpecificFile(filePath, extractPath + insideFileName, insideFileName);
                }
            }
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!url.startsWith("http") && !url.endsWith(md5File)) {
                String temp = url.substring("file://".length());
                if (!temp.startsWith(extractPath)) {
                    url = "file://" + extractPath + temp;
                    view.loadUrl(url);
                    return true;
                }
            }
            return false;
        }
    }
}
