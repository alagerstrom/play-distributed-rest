#!/bin/bash

local_ip=192.168.50.129
linux_ip=192.168.50.210
ports=(6789 6790 6791)
names=(master orange cayenne)
names_linux=(black red silver)

init_node() {
  port=$1
  name=$2
  ip=$3
  ./send_message http://$ip:$port set_url http://$ip:$port none
  ./send_message http://$ip:$port set_name $name none
}

for i in "${!ports[@]}"
do
  port=${ports[$i]}
  name=${names[$i]}
  init_node $port $name $local_ip
done

./send_message http://$local_ip:6790 connect_to_master http://$local_ip:6789 none
./send_message http://$local_ip:6791 connect_to_master http://$local_ip:6789 none

for i in "${!ports[@]}"
do
  port=${ports[$i]}
  name=${names_linux[$i]}
  init_node $port $name $linux_ip
  ./send_message http://$linux_ip:$port connect_to_master http://$local_ip:6789 none
done
