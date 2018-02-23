package com.skydata.cleaner.handler;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.skydata.cleaner.runner.file.FileUtil;
import com.skydata.cleaner.logger.CleanerLogback;
import com.skydata.cleaner.runner.CleanerRunner;
import com.skydata.cleaner.runner.DeleteParamInfor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by junkai.li on 2017/7/28.
 */
public class CleanerMain {
    private static Logger LOG = (Logger) LoggerFactory.getLogger(CleanerMain.class);

    private static Options options = new Options();

    static {
        Option opt = new Option("h", "help", false, "Print help");
        opt.setRequired(false);
        options.addOption(opt);

        opt = new Option("d", "delete", true, "Scans folders in a certain period\n" +
                "And delete the contents of the folders after a period of delay\n" +
                "Use the following form of command:\n" +
                "-d TARGET_FILE_PATH(absolute path):SCAN_CYCLE(seconds):DELETE_DELAY(days)\n" +
                "eg: -d /tmp/A/:4:2,/tmp/B/:2:1");
        opt.setArgName("SCAN_INFO");
        options.addOption(opt);

        opt = new Option("i", "initlog", true, "Initialize logback from external config file\n" +
                "Default: /usr/local/cleaner/logs\n" +
                "Use the following form of command:\n" +
                "-i CONFIG_FILE_PATH(absolute path)\n" +
                "eg: -i /tmp/logback.xml");
        // eg: l
        opt.setArgName("CONFIG_FILE");

        options.addOption(opt);
        opt = new Option("l", "loglevel", true, "Adjust the level of logs\n" +
                "Default: INFO\n" +
                "Use the following form of command:\n" +
                "-l LOGBACK_LEVEL\n" +
                "eg: -l DEBUG");
        // eg: l
        opt.setArgName("LOG_LEVEL");
        options.addOption(opt);
    }

    private static void printHelp() {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("Cleaner options", options, true);
        System.exit(1);
    }

    private static ArrayList<DeleteParamInfor> deleteParamParsing(String value) {
        ArrayList<DeleteParamInfor> deleteParamInfors = new ArrayList<DeleteParamInfor>();
        String[] cmdArray = value.split(",");
        for(int i = 0; i < cmdArray.length; i++) {
            // TODO: To be modified
            String[] paramArray = cmdArray[i].split(":");

            String filePath = paramArray[0];
            if (!filePath.endsWith(File.separator)) {
                filePath = filePath + File.separator;
            }
            File dirFile = new File(filePath);
            if (!dirFile.exists()) {
                LOG.error("Error! Directory(" + filePath + ") doesn't exist.");
                System.exit(0);
            }
            if(FileUtil.isRootFolder(filePath)) {
                System.exit(0);
            }

            try {
                int scanCycle = 0;
                int deleteDelay = 0;
                scanCycle = Integer.parseInt(paramArray[1]);
                deleteDelay = Integer.parseInt(paramArray[2]);
                DeleteParamInfor deleteParamInfor = new DeleteParamInfor(filePath, scanCycle, deleteDelay);
                deleteParamInfors.add(deleteParamInfor);
            } catch (NumberFormatException e) {
                LOG.error(e.getMessage(), e);
                System.exit(1);
            }

        }
        return deleteParamInfors;
    }

    public static void main(String[] args) {
        CleanerLogback.initLogback();

        CommandLineParser parser = new PosixParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            if (commandLine.hasOption('h')) {
                printHelp();
            }

            Option[] opts = commandLine.getOptions();
            if (opts == null || opts.length == 0) {
                printHelp();
            } else {
                for (Option opt : opts) {
                    String name = opt.getLongOpt();
                    String value = opt.getValue();
                    switch (name) {
                        case "delete":
                            CleanerRunner cleanerRunner = new CleanerRunner(deleteParamParsing(value).size());
                            cleanerRunner.runCleaner(deleteParamParsing(value));
                            break;
                        case "initlog":
                            CleanerLogback.reloadLogback(value);
                            break;
                        case "loglevel":
                            LOG.setLevel(Level.toLevel(value));
                            break;
                        default:
                            LOG.error("Unknown commands: " + name);
                            System.exit(0);
                    }
                }
            }
        } catch (ParseException e) {
        LOG.error(e.getMessage(), e);
        printHelp();
        }
    }
}
