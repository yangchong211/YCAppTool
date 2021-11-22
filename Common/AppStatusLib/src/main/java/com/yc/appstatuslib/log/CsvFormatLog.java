package com.yc.appstatuslib.log;

import com.yc.appstatuslib.info.CollectionInfo;

import java.io.File;
import java.util.Date;

public class CsvFormatLog extends BaseFormatStrategy {
    private boolean hasPrintHeader = false;

    public CsvFormatLog(File dir) {
        super(dir);
        String format = this.sDateFormat.format(new Date());
        this.mFile = new File(dir, "battery-" + format + ".txt");
    }

    private void printCsvHeader() {
        StringBuilder builder = new StringBuilder();
        builder.append("currentTime");
        builder.append(",");
        builder.append("humanTime");
        builder.append(",");
        builder.append("level");
        builder.append(",");
        builder.append("scale");
        builder.append(",");
        builder.append("temperature");
        builder.append(",");
        builder.append("status");
        builder.append(",");
        builder.append("health");
        builder.append(",");
        builder.append("plugged");
        builder.append(",");
        builder.append("voltage");
        builder.append(",");
        builder.append("technology");
        builder.append(",");
        builder.append("cpuCount");
        builder.append(",");
        builder.append("cpuTemperature");
        builder.append(",");
        builder.append("appStatus");
        this.write(this.mFile, builder.toString());
    }

    public String log(CollectionInfo info) {
        if (!this.hasPrintHeader) {
            this.printCsvHeader();
            this.hasPrintHeader = true;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(info.currentTime);
        builder.append(",");
        builder.append(info.batteryInfo.humanTime);
        builder.append(",");
        builder.append(info.batteryInfo.level);
        builder.append(",");
        builder.append(info.batteryInfo.scale);
        builder.append(",");
        builder.append(info.batteryInfo.temperature);
        builder.append(",");
        builder.append(info.batteryInfo.status);
        builder.append(",");
        builder.append(info.batteryInfo.health);
        builder.append(",");
        builder.append(info.batteryInfo.plugged);
        builder.append(",");
        builder.append(info.batteryInfo.voltage);
        builder.append(",");
        builder.append(info.batteryInfo.technology);
        builder.append(",");
        builder.append(info.cpuInfo.cpuCount);
        builder.append(",");
        builder.append(info.cpuInfo.cpuTemperature);
        builder.append(",");
        builder.append(info.appStatus);
        this.write(this.mFile, builder.toString());
        return builder.toString();
    }
}

