#!/bin/bash
echo "Transforming changelog"
java org.apache.xalan.xslt.Process -in changelog.xml -xsl changelog.xslt -out ../changelog.txt

