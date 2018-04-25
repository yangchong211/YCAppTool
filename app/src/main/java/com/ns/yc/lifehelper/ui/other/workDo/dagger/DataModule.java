package com.ns.yc.lifehelper.ui.other.workDo.dagger;

import com.ns.yc.lifehelper.db.realm.RealmWorkDoHelper;
import com.ns.yc.lifehelper.ui.other.workDo.data.PageFactory;
import com.ns.yc.lifehelper.ui.other.workDo.model.MainPageItem;

import java.util.List;

import dagger.Module;
import dagger.Provides;


@Module
public class DataModule {

    @Provides
    public List<MainPageItem> pages() {
        return PageFactory.createPages();
    }

    @Provides
    public RealmWorkDoHelper dataDao() {
        return RealmWorkDoHelper.getInstance();
    }

}
