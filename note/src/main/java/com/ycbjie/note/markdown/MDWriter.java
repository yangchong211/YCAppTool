package com.ycbjie.note.markdown;

import android.text.Editable;
import android.widget.EditText;
import android.widget.TextView.BufferType;

public class MDWriter {

    public static final String HEADER = "# ";
    public static final String HEADER2 = "## ";
    public static final String HEADER3 = "### ";
    public static final String CENTER_LEFT = "{";
    public static final String CENTER_RIGHT = "}";
    public static final String BOLD = "**";
    public static final String LIST = "- ";
    public static final String QUOTE = "> ";

    private class Position {
        int start;
        int end;

        public Position(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    private final EditText mEditText;

    public MDWriter(EditText edittext) {
        mEditText = edittext;
    }

    public void setContent(String content) {
        mEditText.setText(content, BufferType.EDITABLE);
    }

    public boolean setAsHeader() {
        String content = mEditText.getText().toString();
        Position position = getCurrentLinePosition(content);
        if (content.substring(position.start, position.end).startsWith(HEADER3)) {
            return false;
        }
        if (!content.startsWith("#")) {
            insert(position.start, " ");
        }
        insert(position.start, "#");
        return true;
    }

    public boolean setAsCenter() {
        String content = mEditText.getText().toString();
        Position position = getCurrentLinePosition(content);
        if (content.substring(position.start, position.end).startsWith(CENTER_LEFT)) {
            return false;
        }
        insert(position.start, CENTER_LEFT);
        insert(position.end + 1, CENTER_RIGHT);
        return true;
    }

    public boolean setAsBold() {
        insert(getCurrentPosition(), BOLD);
        return true;
    }

    public boolean setAsList() {
        String content = mEditText.getText().toString();
        Position position = getCurrentLinePosition(content);
        if (content.substring(position.start, position.end).startsWith(LIST)) {
            return false;
        }
        insert(position.start, LIST);
        return true;
    }

    public boolean setAsQuote() {
        String content = mEditText.getText().toString();
        Position position = getCurrentLinePosition(content);
        if (content.substring(position.start, position.end).startsWith(QUOTE)) {
            return false;
        }
        insert(position.start, QUOTE);
        return true;
    }

    public String getTitle() {
        String content = mEditText.getText().toString();
        if ("".equals(content)) {
            return "";
        }
        int end = content.indexOf("\n");
        return content.substring(0, end == -1 ? content.length() : end);
    }

    public String getContent() {
        return mEditText.getText().toString();
    }

    protected Position getCurrentLinePosition(String content) {
        int index = 0;
        if ("".equals(content)) {
            return new Position(0, 0);
        }
        Position position = new Position(-1, -1);
        //Find the line header "\n"         
        index = getCurrentPosition();
        while (index > 1 && content.charAt(index - 1) != '\n') {
            index--;
        }
        position.start = index == 1 ? 0 : index;
        index = getCurrentPosition();
        while (index < content.length() && content.charAt(index) != '\n') {
            index++;
        }
        position.end = index;
        return position;
    }

    protected int getCurrentPosition() {
        return mEditText.getSelectionStart();
    }

    protected void insert(int index, String text) {
        Editable editor = mEditText.getEditableText();
        if (index < 0 || index >= editor.length()) {
            editor.append(text);
        } else {
            editor.insert(index, text);
        }
    }
}
