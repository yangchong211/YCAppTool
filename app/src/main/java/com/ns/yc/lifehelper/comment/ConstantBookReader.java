package com.ns.yc.lifehelper.comment;

import android.support.annotation.StringDef;
import android.support.v4.util.ArrayMap;

import com.blankj.utilcode.util.Utils;
import com.ns.yc.lifehelper.utils.FileUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ================================================
 * 作    者：杨充
 * 版    本：1.0
 * 创建日期：2017/9/20
 * 描    述：存放阅读器相关常量
 * 修订历史：
 * ================================================
 */
public class ConstantBookReader {

    public static final String SUFFIX_TXT = ".txt";
    public static final String SUFFIX_PDF = ".pdf";
    public static final String SUFFIX_EPUB = ".epub";
    public static final String SUFFIX_ZIP = ".zip";
    public static final String SUFFIX_CHM = ".chm";
    public static final String SUFFIX_DOC = ".doc";
    public static final String SUFFIX_DOCX = ".docx";

    public static String PATH_DATA = FileUtils.createRootPath(Utils.getApp()) + "/cache";
    public static String PATH_TXT = PATH_DATA + "/book/";
    public static String PATH_CHM = PATH_DATA + "/chm";

    public static List<List<String>> list1 = new ArrayList<List<String>>() {{
        add(new ArrayList<String>() {{
            add("全部");
            add("精品");
        }});
        add(new ArrayList<String>() {{
            add("默认排序");
            add("最新发布");
            add("最多评论");
        }});
    }};

    public static List<List<String>> list2 = new ArrayList<List<String>>() {{
        add(new ArrayList<String>() {{
            add("全部");
            add("精品");
        }});
        add(new ArrayList<String>() {{
            add("全部类型");
            add("玄幻奇幻");
            add("武侠仙侠");
            add("都市异能");
            add("历史军事");
            add("游戏竞技");
            add("科幻灵异");
            add("穿越架空");
            add("豪门总裁");
            add("现代言情");
            add("古代言情");
            add("幻想言情");
            add("耽美同人");
        }});
        add(new ArrayList<String>() {{
            add("默认排序");
            add("最新发布");
            add("最多评论");
            add("最有用的");
        }});
    }};



    @StringDef({
            Distillate.ALL,
            Distillate.DISTILLATE
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Distillate {
        String ALL = "";
        String DISTILLATE = "true";
    }


   /* @StringDef({
            SortType.DEFAULT,
            SortType.COMMENT_COUNT,
            SortType.CREATED,
            SortType.HELPFUL
    })*/
    @Retention(RetentionPolicy.SOURCE)
    public @interface SortType {
        String DEFAULT = "updated";
        String CREATED = "created";
        String HELPFUL = "helpful";
        String COMMENT_COUNT = "comment-count";
    }
    public static List<String> sortTypeList = new ArrayList<String>() {{
        add(SortType.DEFAULT);
        add(SortType.CREATED);
        add(SortType.COMMENT_COUNT);
        add(SortType.HELPFUL);
    }};



    @Retention(RetentionPolicy.SOURCE)
    public @interface BookType {
        String ALL = "all";
        String XHQH = "xhqh";
        String WXXX = "wxxx";
        String DSYN = "dsyn";
        String LSJS = "lsjs";
        String YXJJ = "yxjj";
        String KHLY = "khly";
        String CYJK = "cyjk";
        String HMZC = "hmzc";
        String XDYQ = "xdyq";
        String GDYQ = "gdyq";
        String HXYQ = "hxyq";
        String DMTR = "dmtr";
    }
    public static List<String> bookTypeList = new ArrayList<String>() {{
        add(BookType.ALL);
        add(BookType.XHQH);
        add(BookType.WXXX);
        add(BookType.DSYN);
        add(BookType.LSJS);
        add(BookType.YXJJ);
        add(BookType.KHLY);
        add(BookType.CYJK);
        add(BookType.HMZC);
        add(BookType.XDYQ);
        add(BookType.GDYQ);
        add(BookType.HXYQ);
        add(BookType.DMTR);
    }};


    public static Map<String, String> bookType = new ArrayMap<String, String>() {{
        put("qt", "其他");
        put(BookType.XHQH, "玄幻奇幻");
        put(BookType.WXXX, "武侠仙侠");
        put(BookType.DSYN, "都市异能");
        put(BookType.LSJS, "历史军事");
        put(BookType.YXJJ, "游戏竞技");
        put(BookType.KHLY, "科幻灵异");
        put(BookType.CYJK, "穿越架空");
        put(BookType.HMZC, "豪门总裁");
        put(BookType.XDYQ, "现代言情");
        put(BookType.GDYQ, "古代言情");
        put(BookType.HXYQ, "幻想言情");
        put(BookType.DMTR, "耽美同人");
    }};

}
