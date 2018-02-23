package com.skydata.cleaner.logger;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by junkai.li on 2017/8/7.
 */
public class CleanerLogback {

    public static void initLogback() {
        String logbackConfigPath = "logback.xml";
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(logbackConfigPath);
        if (inputStream == null) {
            System.out.println("Couldn't find logger config file: " + logbackConfigPath);
            return;
        }
        if (LoggerFactory.getILoggerFactory() instanceof LoggerContext) {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            try {
                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(loggerContext);
                loggerContext.reset();
                configurator.doConfigure(inputStream);
            } catch (JoranException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Wrong logger factory of type: " + LoggerFactory.getILoggerFactory().getClass());
            return;
        }
    }

    /*public static void reloadLogback(String path, String level) {
        String logbackPath = path;
        String logbackLevel = level;

    }*/

    public static void reloadLogback (String externalConfigFilePath) {
        // Check externalConfigFile if exists, is a file and can be read
        File externalConfigFile = new File(externalConfigFilePath);
        if((!externalConfigFile.exists()) || (!externalConfigFile.isFile())){
            System.out.println("Logback external config file(" + externalConfigFilePath + ") doesn't exist");
        }else{
            if(!externalConfigFile.canRead()){
                System.out.println("Logback external config file(" + externalConfigFilePath + ") cannot be read");
            }else {
                try {
                    LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
                    JoranConfigurator configurator = new JoranConfigurator();
                    configurator.setContext(loggerContext);
                    loggerContext.reset();
                    configurator.doConfigure(externalConfigFilePath);
                    //StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
                } catch (JoranException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String args[]) {
        CleanerLogback.initLogback();
        Logger LOG = (Logger) LoggerFactory.getLogger(CleanerLogback.class);
        LOG.error("This is a ERROR.");
        /*CleanerLogback.reload("D:/test0/logback.xml");
        LOG.error("This is a ERROR.");*/
        LOG.warn("This is a WARN.");
        LOG.setLevel(Level.toLevel(""));
        //LOG.
        LOG.error("This is a ERROR.");
        LOG.warn("This is a WARN.");
    }
}
