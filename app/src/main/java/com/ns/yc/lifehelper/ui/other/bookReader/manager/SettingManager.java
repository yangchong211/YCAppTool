package com.ns.yc.lifehelper.ui.other.bookReader.manager;

import com.blankj.utilcode.util.SPUtils;
import com.ns.yc.lifehelper.comment.Constant;

/**
 * Created by PC on 2017/9/22.
 * 作者：PC
 */

public class SettingManager {

    private volatile static SettingManager manager;

    public static SettingManager getInstance() {
        return manager != null ? manager : (manager = new SettingManager());
    }


    public boolean isNoneCover() {
        return SPUtils.getInstance(Constant.SP_NAME).getBoolean("isNoneCover");
    }

    public void saveNoneCover(boolean isNoneCover) {
        SPUtils.getInstance(Constant.SP_NAME).put("isNoneCover",isNoneCover);
    }

    /**
     * 获取上次阅读章节及位置
     * @param bookId
     * @return
     */
    public int[] getReadProgress(String bookId) {
        int lastChapter = SPUtils.getInstance(Constant.SP_NAME).getInt(getChapterKey(bookId), 1);
        int startPos = SPUtils.getInstance(Constant.SP_NAME).getInt(getStartPosKey(bookId), 0);
        int endPos = SPUtils.getInstance(Constant.SP_NAME).getInt(getEndPosKey(bookId), 0);
        return new int[]{lastChapter, startPos, endPos};
    }

    private String getChapterKey(String bookId) {
        return bookId + "-chapter";
    }

    private String getStartPosKey(String bookId) {
        return bookId + "-startPos";
    }

    private String getEndPosKey(String bookId) {
        return bookId + "-endPos";
    }


}
