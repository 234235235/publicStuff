#!/bin/bash
joernPath="TODO/Joern/"

filePath="TODO/GitHubPublic/"
outDir="TODO/GitHubPublic/"
#dot2pngLoc=".../dot2png.bat"



#######################################################################


BLUE=`tput setaf 4`
RED=`tput setaf 1`
GREEN=`tput setaf 2`
RESET=`tput sgr0`
CYAN=`tput setaf 6`

tag="${RED}[MAIN]${RESET} "

echo "${tag}Applying Joern on source code..."
echo "${tag}Directory: ${CYAN}${filePath}${RESET}"

for cfile in *.c 
do
./codeImport.sh $joernPath $filePath $filePath $cfile
done


echo "${tag}Applying Joern on source code... ${GREEN}DONE!${RESET}"

#echo "${tag}Running Joern Scripts on previous output..."
#./runAllJoernScripts.sh $joernPath $filePath $outDir
#echo "${tag}Running Joern Scripts on previous output... ${GREEN}DONE!${RESET}"

#echo "${tag}Creating dot graphs for visualization..."
#./visualize.sh $outDir $dot2pngLoc
#echo "${tag}Creating dot graphs for visualization... ${GREEN}DONE!${RESET}"

echo "${tag}Searching for vulnerabilities..."
./jQtest.sh $joernPath $filePath $outDir
echo "${tag}Searching for vulnerabilities... ${GREEN}DONE!${RESET}"

