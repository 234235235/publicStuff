jD=$1
joernDir="${jD}joern-cli/"
scriptDir="${jD}scripts/"
filePath=$2
outDir=$3
#binFile="vulnerable.bin.zip"

BLUE=`tput setaf 4`
RED=`tput setaf 1`
GREEN=`tput setaf 2`
RESET=`tput sgr0`
CYAN=`tput setaf 6`
tag="${BLUE}[jQtest]${RESET} "




for jfile in *.bin.zip
do
fileName=${jfile/\.bin\.zip/""}
echo "${tag}Testing ${CYAN}${fileName}${RESET}..."
(cd $joernDir &&
./joern --script "${filePath}jQtest.sc" --params "cpgFile=${filePath}${jfile},outFile=${outDir}jQ_${fileName}.txt" >/dev/null 2>&1)
echo "${tag}Testing ${CYAN}${fileName}${RESET}... ${GREEN}DONE!${RESET}"
done


