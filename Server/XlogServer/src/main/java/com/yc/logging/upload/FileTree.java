package com.yc.logging.upload;

import androidx.annotation.RestrictTo;
import com.yc.logging.annotation.KeepClass;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


@RestrictTo(RestrictTo.Scope.LIBRARY)
@KeepClass
public class FileTree {

    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("data")
    private List<FileEntry> fileEntries;


    public FileTree() {
        timestamp = String.valueOf(System.currentTimeMillis());
        fileEntries = new ArrayList<>();
    }


    public void addSubTree(FileEntry fileEntry) {
        fileEntries.add(fileEntry);
    }


    public String toJson() {
        return new Gson().toJson(this);
    }
}
