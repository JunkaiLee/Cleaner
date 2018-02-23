echo "Please enter the ip address:"
read ip
ssh $ip 'systemctl stop cleaner;
systemctl daemon-reload;

rm /usr/lib/systemd/system/cleaner.service;
rm /etc/cleaner.conf;
rm -rf /usr/local/cleaner;
exit'
