app_path=$(pwd)

# create tmp file that stores new crontab
crontab -l > tmp_cron

# write line at the end of crontab
echo "30 9-17/1 * * MON-FRI $app_path/pull-restart.sh" >> tmp_cron
echo "00 09 * * MON-FRI $app_path/start-npm.sh" >> tmp_cron

# make sure pull-restart.sh is executable
chmod +x $app_path/pull-restart.sh

# show new crontab contents
cat tmp_cron

# install new cron file
crontab tmp_cron

# remove the file
rm tmp_cron
