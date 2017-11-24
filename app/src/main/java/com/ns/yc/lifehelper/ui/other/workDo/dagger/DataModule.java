package com.ns.yc.lifehelper.ui.other.workDo.dagger;

import com.ns.yc.lifehelper.ui.other.toDo.bean.MainPageItem;
import com.ns.yc.lifehelper.ui.other.workDo.data.DataDao;
import com.ns.yc.lifehelper.ui.other.workDo.data.PageFactory;

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
    public DataDao dataDao() {
        return DataDao.getInstance();
    }

}
