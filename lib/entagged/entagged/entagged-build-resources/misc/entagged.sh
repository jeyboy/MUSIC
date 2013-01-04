#!/bin/sh

#Entagged launch script to be placed in /usr/bin or something
#renamed to entagged if possible...

#These two variables have to be setup during installation process
#to reflect the program installation directory (eg. "/usr/share/entagged")
#and version number (eg. "0.15")
PROGRAM_DIR=##PROGRAM_DIR##
PROGRAM_VERSION=##PROGRAM_VERSION##

#If the java executable isn't in the path, or has to be specified, change this:
JAVA_EXECUTABLE="java"

MSG0="ERROR:\nYou must edit this script and change PROGRAM_DIR and PROGRAM_VERSION to point to where you installed Entagged, and the version of it"
MSG1="Attempting to start Entagged..."

if [ ! -d $PROGRAM_DIR ]; then
	echo $MSG0 >&2
	exit -1
fi
if [ ! -f ${PROGRAM_DIR}/entagged-tageditor-${PROGRAM_VERSION}.jar ]; then
	echo $MSG0 >&2
	exit -1
fi

echo $MSG1

$JAVA_EXECUTABLE -jar ${PROGRAM_DIR}/entagged-tageditor-${PROGRAM_VERSION}.jar $1
