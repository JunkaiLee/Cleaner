This project called cleaner has been built for scanning and cleaning the specified directories regularly.

How to use cleaner?
    1.Clone this project
    2.Copy cleaner.jar cleaner.conf install.sh uninstall.sh to particular directories
      /bin/cleaner.jar > /usr/local/cleaner/bin/cleaner.jar
      /conf/cleaner.conf > /etc/cleaner.conf
      /bin/install.sh > somewhere suits you
      /bin/uninstall.sh > somewhere suits you
    3.Execute install.sh to install cleaner to a target machine
      (1)Enter the ip of target machine
      (2)Enter the password of root twice(one for scp, one for ssh)
      (3)Enter the args of cleaner.jar
    4.Execute uninstall.sh to uninstall cleaner from a target machine
      (1)Enter the ip of target machine
      (2)Enter the password of root

How many functions does cleaner have?
    usage: Cleaner options [-d <SCAN_INFO>] [-h] [-i <CONFIG_FILE>] [-l <LOG_LEVEL>]
    -d,--delete <SCAN_INFO>      Scans folders in a certain period
                                 And delete the contents of the folders after a period of delay
                                 Use the following form of command:
                                   -d TARGET_FILE_PATH(absolute path):SCAN_CYCLE(seconds):DELETE_DELAY(days)
                                   eg: -d /tmp/A/:4:2,/tmp/B/:2:1
    -h,--help                    Print help
    -i,--initlog <CONFIG_FILE>   Initialize logback from external config file
                                 Default: /usr/local/cleaner/logs
                                 Use the following form of command:
                                 -i CONFIG_FILE_PATH(absolute path)
                                 eg: -i /tmp/logback.xml
    -l,--loglevel <LOG_LEVEL>    Adjust the level of logs
                                 Default: INFO
                                 Use the following form of command:
                                 -l LOGBACK_LEVEL
                                 eg: -l DEBUG
