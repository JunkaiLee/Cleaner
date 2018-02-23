package com.skydata.cleaner.runner.file;

import java.io.File;
import java.io.IOException;

import ch.qos.logback.classic.Logger;

import com.skydata.cleaner.logger.CleanerLogback;
import org.slf4j.LoggerFactory;

/**
 * Created by junkai.li on 2017/7/28.
 * This part commited to delete file or directory
 */

public class FileUtil {
    private static Logger LOG = (Logger) LoggerFactory.getLogger(FileUtil.class);

    // -----------------@@@@@@@@@@@@@@@-----------------
    // -----------------@ DELETE FILE @-----------------
    // -----------------@@@@@@@@@@@@@@@-----------------
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            LOG.error("Error! File(" + fileName + ") doesn't exist.");
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    // Delete one single file
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // If the file path corresponding to the file exists, and is a file, then delete directly
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                LOG.info("Delete file(" + fileName + ") succeed!");
                return true;
            } else {
                LOG.error("Delete file(" + fileName + ") failed!");
                return false;
            }
        } else {
            LOG.error("Error! File(" + fileName + ") doesn't exist.");
            return false;
        }
    }

    // Delete one single folder
    public static boolean deleteDirectory(String dir) {
        // If the dir does not end with the file separator, the file separator is automatically added
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }

        File dirFile = new File(dir);

        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            LOG.error("Error! Directory(" + dir + ") doesn't exist.");
            return false;
        }
        boolean flag = true;
        // Delete all files in the folder, including subdirectories
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = FileUtil.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } else if (files[i].isDirectory()) {
                flag = FileUtil.deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            LOG.error("Delete fail!");
            return false;
        }
        // Delete the current folder
        if (dirFile.delete()) {
            LOG.info("Delete directory(" + dir + ") succeed!");
            return true;
        } else {
            return false;
        }
    }

    // -----------------@@@@@@@@@@@@@@@-----------------
    // -----------------@ CREATE FILE @-----------------
    // -----------------@@@@@@@@@@@@@@@-----------------
    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if(file.exists()) {
            LOG.warn("Create failed! File(" + destFileName + ") has already existed.");
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            LOG.error("Create failed! File(" + destFileName + ") is a directory.");
            return false;
        }
        // Determines whether the directory in which the destination file resides exists
        if(!file.getParentFile().exists()) {
            LOG.info("Directory where target file resides does not exist. Ready to create it!");
            if(!file.getParentFile().mkdirs()) {
                LOG.error("Create directory failed!");
                return false;
            }
        }
        // Create target file
        try {
            if (file.createNewFile()) {
                LOG.info("Create file(" + destFileName + ") succeed!");
                return true;
            } else {
                LOG.error("Create file(" + destFileName + ") failed!");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error("Create file(" + destFileName + ") failed: " + e.getMessage());
            return false;
        }
    }


    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            //System.out.println("Create failed! Directory(" + destDirName + ") has already existed.");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        // Create directory
        if (dir.mkdirs()) {
            LOG.info("Create directory(" + destDirName + ") succeed!");
            return true;
        } else {
            LOG.error("Create directory(" + destDirName + ") failed!");
            return false;
        }
    }

    // ----------------@@@@@@@@@@@@@@@@@@@----------------
    // ----------------@ OTHER FUNCTIONS @-----------------
    // ----------------@@@@@@@@@@@@@@@@@@@----------------
    public static boolean isEmpty(String dir) {
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);

        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            LOG.error("Error! Directory(" + dir + ") doesn't exist.");
            System.exit(0);
        }
        File[] childFiles = dirFile.listFiles();
        if(childFiles.length == 0) {
            LOG.info("Directory(" + dir + ") is empty.");
            return true;
        } else {
            return false;
        }
    }

    public static void listDirectoryContents(String dir) {
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            LOG.error("Error! Directory(" + dir + ") doesn't exist.");
            System.exit(1);
        }
        File[] childFiles = dirFile.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                System.out.println(childFile.getAbsolutePath());
                listDirectoryContents(childFile.getAbsolutePath());
            }
            System.out.println(childFile.getAbsolutePath());
        }
    }

    public static boolean isRootFolder(String dirName) {
        String[] folderArray = dirName.split("/");
        // TODO: To be modified
        if(folderArray.length <= 2) {
            LOG.error("Please check file path(" + dirName + "). It cannot be a root folder.");
            return true;
        }
        return false;
    }

    public static void main(String[] args) {

        String dir = ",bin,hhh:";
        System.out.println(FileUtil.isRootFolder(dir));
        //FileUtil.listDirectoryContents(dir);
        //FileUtil.delete(dir);
    }

}
