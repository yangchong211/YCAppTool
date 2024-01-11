package com.yc.compress.luban;

public interface OnRenameListener {

    /**
     * 压缩前调用该方法用于修改压缩后文件名
     * <p>
     * Call before compression begins.
     *
     * @param filePath 传入文件路径/ file path
     * @return 返回重命名后的字符串/ file name
     */
    String rename(String filePath);
}
