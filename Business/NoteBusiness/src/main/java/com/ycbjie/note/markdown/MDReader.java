package com.ycbjie.note.markdown;

import android.text.SpannableStringBuilder;
import android.util.Log;

import com.ycbjie.note.markdown.parser.BoldParser;
import com.ycbjie.note.markdown.parser.CenterParser;
import com.ycbjie.note.markdown.parser.HeaderParser;
import com.ycbjie.note.markdown.parser.OrderListParser;
import com.ycbjie.note.markdown.parser.QuoteParser;
import com.ycbjie.note.markdown.parser.UnOrderListParser;

import java.util.ArrayList;
import java.util.List;

public class MDReader {

    private final String mContent;
    private List<Markdown.MDLine> mMDLines = new ArrayList<Markdown.MDLine>();
    private static List<Markdown.MDParser> mMDParsers = new ArrayList<Markdown.MDParser>();

    static {
        mMDParsers.add(new HeaderParser());
        mMDParsers.add(new QuoteParser());
        mMDParsers.add(new OrderListParser());
        mMDParsers.add(new UnOrderListParser());
        mMDParsers.add(new BoldParser());
        mMDParsers.add(new CenterParser());
    }

    public MDReader(String content) {
        mContent = content;
        if (mContent == null || "".equals(content)) {
            return;
        }
        String[] lines = content.split("\n");
        for (String line : lines) {
            mMDLines.add(parseLine(line));
        }
    }

    public String getTitle() {
        if (mContent == null || "".equals(mContent)) {
            return "";
        }
        int end = mContent.indexOf("\n");
        return mContent.substring(0, end == -1 ? mContent.length() : end);
    }

    public String getContent() {
        return mContent;
    }

    public String getRawContent() {
        StringBuilder builder = new StringBuilder();
        for (Markdown.MDLine line : mMDLines) {
            builder.append(line.getRawContent());
            builder.append("\n");
        }
        return builder.toString();
    }

    public SpannableStringBuilder getFormattedContent() {
        return new MDFormatter(mMDLines).getFormattedContent();
    }

    private Markdown.MDLine parseLine(String lineContent) {

        Markdown.MDLine mdline = new Markdown.MDLine(lineContent);
        if ("".equals(lineContent)) {
            return mdline;
        }

        String pContent = lineContent;

        //Parse the start format        
        for (Markdown.MDParser parser : mMDParsers) {
            Markdown.MDWord word = parser.parseLineFmt(pContent);
            if (word.mFormat != Markdown.MD_FMT_TEXT) {
                mdline.mFormat = word.mFormat;
                pContent = lineContent.substring(word.mLength);
                break;
            }
        }

        //Parse the word format              
        StringBuilder mNoFmtContent = new StringBuilder();
        while (pContent.length() != 0) {
            boolean isFmtFound = false;
            //Check format start with pContent
            for (Markdown.MDParser parser : mMDParsers) {
                Markdown.MDWord word = parser.parseWordFmt(pContent);
                if (word.mLength > 0) {
                    isFmtFound = true;
                    //Add no format string first 
                    int noFmtContentLen = mNoFmtContent.length();
                    if (noFmtContentLen != 0) {
                        mdline.mMDWords.add(new Markdown.MDWord(mNoFmtContent.toString(), noFmtContentLen, Markdown.MD_FMT_TEXT));
                        mNoFmtContent = new StringBuilder();
                    }
                    mdline.mMDWords.add(word);
                    pContent = pContent.substring(word.mLength);
                    break;
                }
            }
            //If no format found, move to next position
            if (!isFmtFound) {
                mNoFmtContent.append(pContent.charAt(0));
                pContent = pContent.substring(1);
                if (pContent.length() == 0) {
                    mdline.mMDWords.add(new Markdown.MDWord(mNoFmtContent.toString(), mNoFmtContent.length(), Markdown.MD_FMT_TEXT));
                    break;
                }
            }
        }
        return mdline;
    }

    protected void display() {
        StringBuilder builder = new StringBuilder();
        builder.append("Markdown Parse: \n")
                .append(mContent)
                .append("\n\n");
        for (Markdown.MDLine line : mMDLines) {
            builder.append("Line format: ")
                    .append(line.mFormat)
                    .append("\n");
            for (Markdown.MDWord word : line.mMDWords) {
                builder.append("Word: ")
                        .append(word.mRawContent)
                        .append(", ")
                        .append(word.mFormat)
                        .append("\n");
            }
        }
        Log.d("JNote", builder.toString());
    }
}
