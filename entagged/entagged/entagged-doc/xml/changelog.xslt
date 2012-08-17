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
 */-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="text"/>
	
	<xsl:template match="changelog">
		<xsl:text>Changelog&#xa;</xsl:text>
		<xsl:text>---------&#xa;</xsl:text>
		<xsl:text>&#x9;Version History&#xa;</xsl:text>
		
		<xsl:apply-templates select="version" />

		<xsl:apply-templates select="lastupdate" />
	</xsl:template>
	
	<xsl:template match="version">
		<xsl:apply-templates select="name"/>

		<xsl:text>&#x9;&#x9;&#x9;Description:&#xa;</xsl:text>
		<xsl:apply-templates select="description"/>

		<xsl:text>&#x9;&#x9;&#x9;Features:&#xa;</xsl:text>
		<xsl:apply-templates select="features"/>
		
		<xsl:text>&#x9;&#x9;&#x9;Bugfixes:&#xa;</xsl:text>
		<xsl:apply-templates select="bugfixes"/>

		<xsl:text>&#x9;&#x9;&#x9;To Do:&#xa;</xsl:text>
		<xsl:apply-templates select="todo"/>
		
		<xsl:text>&#xa;</xsl:text>
	</xsl:template>
	
	<xsl:template match="name">
		<xsl:text>&#x9;&#x9;</xsl:text><xsl:value-of select="."/><xsl:text>&#xa;</xsl:text>
	</xsl:template>
	
	<xsl:template match="description">
		<xsl:text>&#x9;&#x9;&#x9;&#x9;</xsl:text><xsl:value-of select="."/><xsl:text>&#xa;</xsl:text>
	</xsl:template>
	
	<xsl:template match="features">
		<xsl:for-each select="feature">
			<xsl:text>&#x9;&#x9;&#x9;&#x9;</xsl:text><xsl:value-of select="."/><xsl:text>&#xa;</xsl:text>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="bugfixes">
		<xsl:for-each select="bugfix">
			<xsl:text>&#x9;&#x9;&#x9;&#x9;</xsl:text><xsl:value-of select="."/><xsl:text>&#xa;</xsl:text>
		</xsl:for-each>
	</xsl:template>
	
	<xsl:template match="todo">
		<xsl:text>&#x9;&#x9;&#x9;&#x9;</xsl:text><xsl:value-of select="."/><xsl:text>&#xa;</xsl:text>
	</xsl:template>
	
	<xsl:template match="lastupdate">
		<xsl:text>*Last Update: </xsl:text><xsl:value-of select="." />
	</xsl:template>
</xsl:stylesheet>
