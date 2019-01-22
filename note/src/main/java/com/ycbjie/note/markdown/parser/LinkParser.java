package com.ycbjie.note.markdown.parser;


import com.ycbjie.note.markdown.Markdown;

public class LinkParser extends Markdown.MDParser {

    private static final String KEY = "[*](*)";

    @Override
    public Markdown.MDWord parseLineFmt(String content) {
        return Markdown.MDWord.NULL;
    }

    @Override
    public Markdown.MDWord parseWordFmt(String content) {
        if (!content.matches(KEY)) {
            return Markdown.MDWord.NULL;
        }
        int length = content.indexOf(')');
        return new Markdown.MDWord(content.substring(0, length + 1), length, Markdown.MD_FMT_LINK);
    }

}
