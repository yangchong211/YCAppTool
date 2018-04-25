package com.ns.yc.lifehelper.model.event;

import com.ns.yc.lifehelper.comment.ConstantBookReader;

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

