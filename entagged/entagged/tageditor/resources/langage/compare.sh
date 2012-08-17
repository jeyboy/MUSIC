#!/bin/sh
[[ $1 == "" ]] && echo "./compare.sh xxx.lang" && exit

for i in english.lang $1
do
	cat $i | cut -d"=" -f1 > $i.temp
done

vimdiff english.lang.temp $1.temp
rm *.temp
