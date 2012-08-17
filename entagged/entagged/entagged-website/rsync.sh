#!/bin/sh
rsync -zvr \
--exclude .cvsignore \
--exclude rsync.sh \
--exclude CVS \
--exclude old \
--progress \
./ shell.sourceforge.net:/home/groups/e/en/entagged/htdocs
