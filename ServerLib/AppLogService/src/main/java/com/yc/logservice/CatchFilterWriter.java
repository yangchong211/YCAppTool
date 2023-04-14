package com.yc.logservice;

import java.io.FilterWriter;
import java.io.Writer;

public class CatchFilterWriter extends FilterWriter {

    public CatchFilterWriter(Writer writer) {
        super(writer);
    }

    @Override
    public void write(String string) {
        if (string != null) {
            try {
                out.write(string);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void flush() {
        try {
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
