echo "Please enter the ip address:"
read ip
scp /etc/cleaner.conf /usr/local/cleaner/bin/cleaner.jar root@$ip:/etc
ssh $ip 'source /etc/cleaner.conf;
mkdir $INSTALL_DIR;
mkdir $INSTALL_DIR/bin;

mv /etc/cleaner.jar $INSTALL_DIR/bin/;

echo "Please enter the args:"
read args;

cat > ${SERVICE_DIR}/cleaner.service <<-EOF
[Unit]
Description=Cleaner

[Service]
Type=forking
ExecStart=/usr/bin/java -jar $INSTALL_DIR/bin/cleaner.jar $args
ExecStop=/bin/bash ${JAVA_HOME}/bin/jps | grep cleaner | awk -F" " '{print $1}' | xrags kill -9
KillMode=process
Restart=always
RestartSec=20

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload;
systemctl start cleaner;

exit'
