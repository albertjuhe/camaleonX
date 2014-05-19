<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://schemas.openxmlformats.org/package/2006/content-types"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:omaonk="http://omaonk.com/relationfile"
    xmlns:ct="http://schemas.openxmlformats.org/package/2006/content-types"
    exclude-result-prefixes="#all" version="2.0">

    <xsl:output indent="no" omit-xml-declaration="no" standalone="yes" media-type="xml"
        encoding="UTF-8"/>

    <xsl:param name="pptx.tmp"/>
    <xsl:param name="pptx.doc1"/>
    <xsl:param name="pptx.doc2"/>
    <xsl:param name="numdoc"/>

    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates select="omaonk:files"/>
    </xsl:template>

    <!-- 
    <file xmlns="http://schemas.openxmlformats.org/package/2006/content-types"
    dir_source="./Introduction/ppt/notesMasters/notesMaster1.xml" file_source="notesMaster1"
    dir_target=" ./eproject/ppt/notesMasters/notesMaster2.xml" file_target="notesMaster2"/>
-->
    <xsl:template match="omaonk:files">
        <xsl:result-document href="{concat($pptx.tmp,'/copy_relation.bat')}" method="text">
            <xsl:text>set xsl=C:\Projectes\eProject\fusiopptx\xsl\</xsl:text>
            <xsl:text>&#10;</xsl:text>
            <xsl:text>set Saxon_path=C:\saxonb9-1-0-5j\saxon9.jar</xsl:text>
            <xsl:text>&#10;</xsl:text>
            <xsl:for-each select="ct:file">
                <xsl:value-of
                    select="translate(concat('copy ',$pptx.tmp,@dir_source,' ',$pptx.tmp,@dir_target),'/','\')"/>
                <xsl:if test="not(contains(@dir_source,'/theme/'))">
                    <xsl:text>&#10;</xsl:text>
                    <xsl:value-of
                        select="translate(concat('java -jar  %Saxon_path% -dtd:off .\',$pptx.tmp,@path_source,'/_rels/',@file_source,'.xml.rels %xsl%relacions.xsl numdoc=',$numdoc,' pptx.job=',/omaonk:files/@job,' > ./',$pptx.tmp,@path_target,'/_rels/',@file_target,'.xml.rels'),'/','\')"
                    />
                </xsl:if>
                <xsl:text>&#10;</xsl:text>

            </xsl:for-each>
        </xsl:result-document>
    </xsl:template>

</xsl:stylesheet>
