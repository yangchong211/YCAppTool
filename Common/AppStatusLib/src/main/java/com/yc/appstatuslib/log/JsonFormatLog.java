package com.yc.appstatuslib.log;

import com.yc.appstatuslib.info.CollectionInfo;

import java.io.File;
import java.util.Date;

public class JsonFormatLog extends BaseFormatStrategy {
    private File mFile;

    public JsonFormatLog(File dir) {
        super(dir);
        this.mFile = new File(dir, this.sDateFormat.format(new Date()) + "-battery.txt");
    }

    public String log(CollectionInfo info) {
        this.write(this.mFile, info.toString());
        return null;
    }
}
