#!/bin/bash

if [ -z $APP_HOME ]; then                                                                                    
  if [[ ! -z $ZSH_NAME ]]; then                                                                               
    export APP_HOME=$(dirname $(cd $(dirname ${(%):-%x}) && pwd))                                            
  else                                                                                                        
    export APP_HOME=$(dirname $(cd $(dirname "${BASH_SOURCE[0]}") && pwd))                                   
  fi                                                                                                          
fi 

CP=$APP_HOME/libs/*

echo "@ is $@"
ARGS=();
while [ $# -gt 0 ] ; do
     echo "1 is $1"
  ARGS+=($1)
echo "Args is $ARGS"
echo "Args[@] is $ARGS[@]"
  shift
done

echo "Args is $ARGS"

java -cp $APP_HOME/build/classes.jar:$CP mockcampaignrunner.MockCampaignRunner "${ARGS[@]}"
