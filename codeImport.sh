#!/bin/bash
joernPath=$1    
joernServer="${joernPath}joern-server/"
joernCLI="${joernPath}joern-cli/"
joernCpg="${joernPath}cpg/cpgclientlib/"

filePath=$2    
outDir=$3      

file=$4 
file=${file/\.c/""}

BLUE=`tput setaf 4`
RED=`tput setaf 1`
GREEN=`tput setaf 2`
RESET=`tput sgr0`
CYAN=`tput setaf 6`
tag="${BLUE}[codeImport]${RESET} "

export _Java_OPTS="-Xmx20G"  #20GB of ram 



echo "${tag}Parsing file ${CYAN}${file}${RESET} ..."
(cd $joernCLI && ./joern-parse $filePath$file.c --out "${outDir}${file}.bin.zip" >/dev/null 2>&1)
echo "${tag}Parsing file ${CYAN}${file}${RESET} ... ${GREEN}DONE!${RESET}"



