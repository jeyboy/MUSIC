<?xml version="1.0" encoding="UTF-8"?>
<!--
/*	XSL for creation of a German csv.
 *  ********************************************************************   **
 *  Copyright notice                                                       **
 *  **																	   **
 *  (c) 2003 Entagged Developpement Team				                   **
 *  http://www.sourceforge.net/projects/entagged                           **
 *  **																	   **
 *  All rights reserved                                                    **
 *  **																	   **
 *  This script is part of the Entagged project. The Entagged 			   **
 *  project is free software; you can redistribute it and/or modify        **
 *  it under the terms of the GNU General Public License as published by   **
 *  the Free Software Foundation; either version 2 of the License, or      **
 *  (at your option) any later version.                                    **
 *  **																	   **
 *  The GNU General Public License can be found at                         **
 *  http://www.gnu.org/copyleft/gpl.html.                                  **
 *  **																	   **
 *  This copyright notice MUST APPEAR in all copies of the file!           **
 *  ********************************************************************
 */
-->
<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xalan="http://xml.apache.org/xslt">

	<xsl:output method="text" 
              encoding="UTF-8"
              indent="no"/>

	<xsl:template match="/">
		<xsl:text>Datei,K&#252;nstler,Album,Track,Titel,Genre,Jahr,Kommentar
		</xsl:text>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="audiolist">
		<xsl:for-each select="/audiolist/file">
		<xsl:value-of select="@name" />,<xsl:value-of select="tag/artist" />,<xsl:value-of select="tag/album" />,<xsl:value-of select="tag/track" />,<xsl:value-of select="tag/title" />,<xsl:value-of select="tag/genre" />,<xsl:value-of select="tag/year" />,<xsl:value-of select="tag/comment" />
		<xsl:text>
		</xsl:text>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>