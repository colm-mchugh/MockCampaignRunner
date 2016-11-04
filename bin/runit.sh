if [ -z $VIXI_HOME ]; then                                                                                    
  if [[ ! -z $ZSH_NAME ]]; then                                                                               
    export VIXI_HOME=$(dirname $(cd $(dirname ${(%):-%x}) && pwd))                                            
  else                                                                                                        
    export VIXI_HOME=$(dirname $(cd $(dirname "${BASH_SOURCE[0]}") && pwd))                                   
  fi                                                                                                          
fi 

CP=$VIXI_HOME/lib/*

ARGS=();                                                                                                      
while [ $# -gt 0 ] ; do                                                                                       
  case "$1" in                                                                                                
  -q) shift;                                                                                                  
      QUERY=$1;;                                                                                              
  -e) shift;                                                                                                  
      QUERY=$1;;                                                                                              
  -f) shift;                                                                                                  
      FILE=$1;;                                                                                               
   *) ARGS+=($1);;                                                                                            
  esac                                                                                                        
  shift                                                                                                       
done                                                                                                          
  
java -cp ./build/classes/mockcampaignrunner/MockCampaignRunner.class
$VIXI_HOME/fe/target/vixi-frontend-1.0-SNAPSHOT.jar:$CP -Dlog4j.configurationFile=$VIXI_HOME/bin/log4j.xml  "${ARGS[@]}"
