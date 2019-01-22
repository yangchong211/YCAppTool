package com.ycbjie.todo.data;

import com.ycbjie.todo.model.MainPageItem;
import com.ycbjie.todo.ui.fragment.PageFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;




public class PageFactory {

    private static List<MainPageItem> items = new ArrayList<>();
    public static List<MainPageItem> createPages() {
        if (items == null || items.size() != 7) {
            items = new ArrayList<>();
            items.add(new MainPageItem(Calendar.SUNDAY, "周日", new PageFragment()));
            items.add(new MainPageItem(Calendar.MONDAY, "周一", new PageFragment()));
            items.add(new MainPageItem(Calendar.TUESDAY, "周二", new PageFragment()));
            items.add(new MainPageItem(Calendar.WEDNESDAY, "周三", new PageFragment()));
            items.add(new MainPageItem(Calendar.THURSDAY, "周四", new PageFragment()));
            items.add(new MainPageItem(Calendar.FRIDAY, "周五", new PageFragment()));
            items.add(new MainPageItem(Calendar.SATURDAY, "周六", new PageFragment()));
        }
        return items;
    }

}
