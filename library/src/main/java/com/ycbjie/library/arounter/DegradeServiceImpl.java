package com.ycbjie.library.arounter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.DegradeService;
import com.ycbjie.library.constant.Constant;
import com.ycbjie.library.web.view.WebViewActivity;

/**
 * <pre>
 *     @author 杨充
 *     blog  : https://github.com/yangchong211
 *     time  : 2019/02/12
 *     desc  : ARouter路由降级处理
 *     revise:
 * </pre>
 */
@Route(path = DegradeServiceImpl.PATH)
public class DegradeServiceImpl implements DegradeService {

    static final String PATH = "/service/DegradeServiceImpl";

    @Override
    public void onLost(Context context, Postcard postcard) {
        if (context != null && postcard.getGroup().equals("activity")) {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra(Constant.URL, Constant.GITHUB);
            intent.putExtra(Constant.TITLE, "github地址");
            ActivityCompat.startActivity(context, intent, null);
        }
    }

    @Override
    public void init(Context context) {

    }
}
