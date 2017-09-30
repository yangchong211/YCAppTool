package com.ns.yc.lifehelper.event;

import com.ns.yc.lifehelper.api.ConstantBookReader;

/**
 * Created by PC on 2017/9/28.
 * 作者：PC
 */

public class BookReaderSelectionEvent {

    public String distillate;

    public String type;

    public String sort;

    public BookReaderSelectionEvent(@ConstantBookReader.Distillate String distillate,
                          @ConstantBookReader.BookType String type,
                          @ConstantBookReader.SortType String sort) {
        this.distillate = distillate;
        this.type = type;
        this.sort = sort;
    }

    public BookReaderSelectionEvent(@ConstantBookReader.SortType String sort) {
        this.sort = sort;
    }

}

