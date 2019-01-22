package com.ycbjie.todo.dagger;

import com.ycbjie.library.db.realm.RealmWorkDoHelper;
import com.ycbjie.todo.data.PageFactory;
import com.ycbjie.todo.model.MainPageItem;

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
