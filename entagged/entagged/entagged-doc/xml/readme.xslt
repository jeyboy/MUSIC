<?xml version="1.0" encoding="UTF-8"?>
<!--
/*
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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text" indent="no"/>
	
	<xsl:strip-space elements="*"/>
	
	<xsl:template match="readme">
		<xsl:text>README&#xa;</xsl:text>
		<xsl:text>------&#xa;</xsl:text>
		
		<xsl:apply-templates select="introduction" />
		
		<xsl:text>&#xa;</xsl:text>
		
		<xsl:text>Disclaimer&#xa;</xsl:text>
		<xsl:text>----------&#xa;</xsl:text>
		<xsl:apply-templates select="disclaimer" />
		
		<xsl:text>&#xa;</xsl:text>
		
		<xsl:apply-templates select="section" />
		
		<xsl:text>&#xa;</xsl:text>
		
		<xsl:apply-templates select="used-libs" />
		
		<xsl:text>&#xa;</xsl:text>
		
		<xsl:apply-templates select="thankings" />
		
		<xsl:text>&#xa;</xsl:text>
		
		<xsl:apply-templates select="lastupdated" />
		
	</xsl:template>
	
	<xsl:template match="introduction">
		<xsl:value-of select="text"/>
		<xsl:text>&#xa;Tested With:&#xa;</xsl:text>
		<xsl:text>&#x9;</xsl:text><xsl:value-of select="tested"/>
		<xsl:text>&#xa;Requirements:&#xa;</xsl:text>
		<xsl:apply-templates select="requirements"/>
	</xsl:template>
	
	<xsl:template match="requirements">
		<xsl:for-each select="item">
			<xsl:text>&#x9;* </xsl:text><xsl:value-of select="." /><xsl:text>&#xa;</xsl:text>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="disclaimer">
		<xsl:value-of select="."/>
		<xsl:text>&#xa;</xsl:text>
	</xsl:template>
	
	<xsl:template match="section">
		<xsl:value-of select="title" /><xsl:text>&#xa;---------------&#xa;</xsl:text>
		<xsl:apply-templates select="p"/>
		<xsl:text>&#xa;</xsl:text>
	</xsl:template>
	
	<xsl:template match="p">
		<xsl:value-of select="."/>
		<xsl:text>&#xa;</xsl:text>
	</xsl:template>
	
	<xsl:template match="used-libs">
		<xsl:text>This product includes:&#xa;</xsl:text>
		<xsl:for-each select="lib">
			<xsl:text>&#x9;- </xsl:text><xsl:value-of select="name" /><xsl:text>&#xa;</xsl:text>
			<xsl:text>&#x9;&#x9;(</xsl:text><xsl:value-of select="link" /><xsl:text>)&#xa;</xsl:text>
		</xsl:for-each>
		<xsl:text>&#xa;All the licenses regarding these libraries can be found either on their website, or in the /lib/ folder located in your entagged installation directory.&#xa;</xsl:text>
		<xsl:text>Thanks to them, for providing such great libraries!&#xa;</xsl:text> 
	</xsl:template>
	
	<xsl:template match="thankings">
		<xsl:text>Thanks to:&#xa;</xsl:text>
		<xsl:text>----------&#xa;</xsl:text>
		<xsl:for-each select="thanks">
			<xsl:text> - </xsl:text><xsl:value-of select="." /><xsl:text>&#xa;</xsl:text>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="lastupdated">
		<xsl:text>---------------------------------&#xa;</xsl:text>
		<xsl:text>Last Modified: </xsl:text>
		<xsl:value-of select="." />
	</xsl:template>
</xsl:stylesheet>
