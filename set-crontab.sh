app_path="/home/developer/Salarizare"

# create tmp file that stores new crontab
crontab -l > tmp_cron

# make sure pull-restart.sh is executable
chmod +x $app_path/pull-restart.sh

# write line at the end of crontab
echo "30 9-18/1 * * MON-FRI $app_path/pull-restart.sh" >> tmp_cron

# show new crontab contents
cat tmp_cron

# install new cron file
crontab tmp_cron

# remove the local temporary file
rm tmp_cron
