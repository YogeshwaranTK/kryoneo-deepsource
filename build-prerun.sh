#!/bin/bash
sudo mkdir /mnt/kjms
if [ ! -d "/etc/smbcredentials" ]; then
sudo mkdir /etc/smbcredentials
fi
if [ ! -f "/etc/smbcredentials/uatkjms.cred" ]; then
    sudo bash -c 'echo "username=uatkjms" >> /etc/smbcredentials/uatkjms.cred'
    sudo bash -c 'echo "password=ZmwzVEbLXPNl3TuOpGORUVrDYe6VNH+4NXey+45+mfyOYyRwRWrCj4jro4a4bX6gQ8OtGFNtZV2r+AStO6fZBg==" >> /etc/smbcredentials/uatkjms.cred'
fi
sudo chmod 600 /etc/smbcredentials/uatkjms.cred

sudo bash -c 'echo "//uatkjms.file.core.windows.net/kjms /mnt/kjms cifs nofail,credentials=/etc/smbcredentials/uatkjms.cred,dir_mode=0777,file_mode=0777,serverino,nosharesock,actimeo=30" >> /etc/fstab'
sudo mount -t cifs //uatkjms.file.core.windows.net/kjms /mnt/kjms -o credentials=/etc/smbcredentials/uatkjms.cred,dir_mode=0777,file_mode=0777,serverino,nosharesock,actimeo=30
