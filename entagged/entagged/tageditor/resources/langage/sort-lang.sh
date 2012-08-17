#!/bin/sh
[[ $1 == "" ]] && echo "./sort-lang.sh xxx.lang" && exit

egrep -v "#" $1 | sort | uniq > $1.temp

cat - $1.temp > $1.temp.2 <<EOF
# $1 Langage Properties File
#*********************************************************************
#  Copyright notice
#  
#  (c) 2003 Entagged Developpement Team
#  http://www.sourceforge.net/projects/entagged
#  
#  All rights reserved
# 
#  This script is part of the Entagged project. The Entagged
#  project is free software; you can redistribute it and/or modify
#  it under the terms of the GNU General Public License as published by
#  the Free Software Foundation; either version 2 of the License, or
#  (at your option) any later version.
#
#  The GNU General Public License can be found at
#  http://www.gnu.org/copyleft/gpl.html.
# 
#  This copyright notice MUST APPEAR in all copies of the file!
#  ********************************************************************
#
# \$Id\$
#
EOF

mv $1.temp.2 $1
rm $1.temp
