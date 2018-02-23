package com.skydata.cleaner.runner;

import com.skydata.cleaner.runner.file.SubFileInfor;

//import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by junkai.li on 2017/8/3.
 */
public class DeleteParamInfor {
    //public int flag;
    String filePath;
    int scanCycle;
    int deleteDelay;
    Set<SubFileInfor> subFileInfors;

    public DeleteParamInfor(String fp, int sc, int dd) {
        this.filePath = fp;
        this.scanCycle = sc;
        this.deleteDelay = dd;
        this.subFileInfors = new HashSet<SubFileInfor>();
    }

    public String getFilePath() { return filePath; }

    public int getScanCycle() { return scanCycle; }

    public int getDeleteDelay() { return deleteDelay; }

    public Set<SubFileInfor> getSubFileInfors() { return subFileInfors; }

}
