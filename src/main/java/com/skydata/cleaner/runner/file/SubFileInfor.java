package com.skydata.cleaner.runner.file;

/**
 * Created by junkai.li on 2017/8/3.
 */
public class SubFileInfor {
    String filePath;
    long startTime;

    public SubFileInfor(String fp) {
        this.filePath = fp;
        startTime = System.currentTimeMillis() / 1000;
    }

    public String getFilePath() { return filePath; }

    public long getStartTime() { return startTime; }

    public boolean equals(Object obj) {
        if(!(obj instanceof SubFileInfor)) {
            return false;
        }
        SubFileInfor b = (SubFileInfor) obj;
        if(this.filePath.equals(b.filePath)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return filePath.hashCode();
    }
}
