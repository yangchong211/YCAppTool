package com.ycbjie.note.markdown;

import java.util.ArrayList;
import java.util.List;

public class Markdown {

    public static final int MD_FMT_TEXT = 0;
    public static final int MD_FMT_HEADER1 = 1;
    public static final int MD_FMT_HEADER2 = 2;
    public static final int MD_FMT_HEADER3 = 3;
    public static final int MD_FMT_QUOTE = 4;
    public static final int MD_FMT_ITALIC = 5;
    public static final int MD_FMT_BOLD = 6;
    public static final int MD_FMT_CENTER = 7;
    public static final int MD_FMT_UNORDER_LIST = 8;
    public static final int MD_FMT_ORDER_LIST = 9;
    public static final int MD_FMT_LINK = 10;

    public static class MDWord {
        public static final MDWord NULL = new MDWord(null, 0, Markdown.MD_FMT_TEXT);
        public final String mRawContent;
        public final int mLength;
        public final int mFormat;

        public MDWord(String rawContent, int length, int format) {
            mRawContent = rawContent;
            mLength = length;
            mFormat = format;
        }
    }

    public static class MDLine {

        public final String mLineContent;
        public List<MDWord> mMDWords = new ArrayList<MDWord>();
        ;
        public int mFormat = Markdown.MD_FMT_TEXT;

        public MDLine(String lineContent) {
            mLineContent = lineContent;
        }

        public String getRawContent() {
            StringBuilder builder = new StringBuilder();
            for (MDWord word : mMDWords) {
                builder.append(word.mRawContent);
            }
            return builder.toString();
        }
    }

    public static abstract class MDParser {
        public abstract MDWord parseLineFmt(String content);

        public abstract MDWord parseWordFmt(String content);
    }
}
