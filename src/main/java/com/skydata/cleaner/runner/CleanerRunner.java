package com.skydata.cleaner.runner;


import ch.qos.logback.classic.Logger;
import com.skydata.cleaner.runner.file.FileUtil;
import com.skydata.cleaner.runner.file.SubFileInfor;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by junkai.li on 2017/7/31.
 */

public class CleanerRunner {
    private static Logger LOG = (Logger) LoggerFactory.getLogger(CleanerRunner.class);
    static ScheduledExecutorService threadPool;
    // Creates a fixed line pool that supports scheduled and periodic task execution
    public CleanerRunner(int sz) {
        if (sz <= 4) {
            threadPool = Executors.newScheduledThreadPool(sz);
        } else {
            threadPool = Executors.newScheduledThreadPool(4);
        }
    }
    //static ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(4);
    public static void runCleaner(ArrayList<DeleteParamInfor> deleteParamInfors) {
        for(final DeleteParamInfor deleteParamInfor : deleteParamInfors) {
            threadPool.scheduleWithFixedDelay(new Runnable() {
                public void run() {
                    //System.out.println("emmm");
                    autoCheck(deleteParamInfor);
                }
            }, 0, deleteParamInfor.getScanCycle(), TimeUnit.SECONDS);
            // TEST
            /*threadPool.scheduleAtFixedRate(new Runnable() {
                public void run() {
                    FileUtil.createFile("D:/test0/subfile0");
                    FileUtil.createDir("D:/test0/subdir0");
                    FileUtil.createDir("D:/test1/subdir0");
                    FileUtil.createFile("D:/test2/subfile0");
                }
            }, 0, 10, TimeUnit.SECONDS);*/
        }
    }

    public static void autoCheck(DeleteParamInfor deleteParamInfor) {
        // Initialize
        String filePath = deleteParamInfor.getFilePath();
        int deleteDelay = deleteParamInfor.getDeleteDelay();
        Set<SubFileInfor> subFileInfors = deleteParamInfor.getSubFileInfors();

        // Traversing subdirectories: if new files appear, add them to the set of subdirectories
        File dirFile = new File(filePath);
        File[] childFiles = dirFile.listFiles();
        for (File childFile : childFiles) {
            String childFileName = childFile.getAbsolutePath().toString();
            SubFileInfor subFileInfor = new SubFileInfor(childFileName);
            if(subFileInfors.add(subFileInfor)) {
                LOG.info("Add " + childFileName + " to scanner succeed.");
            }
        }
        // Traversing the set of subdirectories
        Iterator<SubFileInfor> it = subFileInfors.iterator();
        while (it.hasNext()) {
            SubFileInfor subFileInfor = it.next();
            long currentTime = System.currentTimeMillis() / 1000;
            String subfilePath = subFileInfor.getFilePath();
            File file = new File(subfilePath);
            // If the file in the set of subdirectories doesn't exist, remove it
            if(!file.exists()) {
                it.remove();
                LOG.info("Remove " + subfilePath + " from scanner succeed.");
            } else {
                // If the file exists longer than the delete delay
                if(currentTime - subFileInfor.getStartTime() > (deleteDelay * 3600 * 24)) {
                    LOG.info(subfilePath + " reached its deadline and to be deleted.");
                    FileUtil.delete(subfilePath);
                    it.remove();
                    LOG.info("Remove " + subfilePath + " from scanner succeed.");
                }
            }
        }
    }
}
