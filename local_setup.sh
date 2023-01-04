#!/bin/bash

ports=(6789 6790 6791)
names=(master orange cayenne)

init_node() {
  port=$1
  name=$2
  ./send_message http://localhost:$port set_url http://localhost:$port
  ./send_message http://localhost:$port set_name $name
}

for i in "${!ports[@]}"
do
  port=${ports[$i]}
  name=${names[$i]}
  init_node $port $name
done

./send_message http://localhost:6790 connect_to_master http://localhost:6789 
./send_message http://localhost:6791 connect_to_master http://localhost:6789 