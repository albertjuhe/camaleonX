<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://schemas.openxmlformats.org/package/2006/content-types"
    xmlns:ct="http://schemas.openxmlformats.org/package/2006/content-types"
    exclude-result-prefixes="#all" version="2.0">

    <xsl:param name="docx.merge"/>
    <xsl:param name="docx.iteracio"/>


    <xsl:output indent="no" omit-xml-declaration="no" standalone="yes" media-type="xml"
        encoding="UTF-8"/>

    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
            <xsl:call-template name="type-content"/>
            <xsl:call-template name="toc"/>
        </Types>

        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template name="type-content">
        <!--
        <xsl:variable name="type-content" select="//ct:Default | document($docx.merge)//ct:Default"></xsl:variable>
        <xsl:for-each-group select="$type-content" group-by="@Extension">
            <xsl:copy>
                <xsl:copy-of select="@*"/>
            </xsl:copy>
        </xsl:for-each-group>
        -->
        <Default Extension="rels"
            ContentType="application/vnd.openxmlformats-package.relationships+xml"/>
        <Default Extension="xml" ContentType="application/xml"/>
        <Default Extension="jpeg" ContentType="image/jpeg"/>
        <Default Extension="jpg" ContentType="image/jpg"/>
        <Default Extension="tiff" ContentType="image/tiff"/>
        <Default ContentType="image/png" Extension="png"/>
        <Default ContentType="image/gif" Extension="gif"/>
        <Default Extension="xls" ContentType="application/vnd.ms-excel"/>
        <Default Extension="vml"
            ContentType="application/vnd.openxmlformats-officedocument.vmlDrawing"/>
        <Default Extension="emf" ContentType="image/x-emf"/>
        <Default Extension="wmf" ContentType="image/x-wmf"/>
        <Default Extension="bin"
            ContentType="application/vnd.openxmlformats-officedocument.oleObject"/>
    </xsl:template>

    <xsl:template name="toc">
        <!--   
        <Override PartName="/word/footnotes.xml"
            ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.footnotes+xml"/>                
        <Override PartName="/word/document.xml"
            ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.document.main+xml"/>
        <Override PartName="/word/numbering.xml"
            ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.numbering+xml"/>
        <Override PartName="/word/styles.xml"
            ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.styles+xml"/>
        <Override PartName="/word/endnotes.xml"
            ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.endnotes+xml"/>
        <Override PartName="/docProps/app.xml"
            ContentType="application/vnd.openxmlformats-officedocument.extended-properties+xml"/>
        <Override PartName="/word/theme/theme1.xml"
            ContentType="application/vnd.openxmlformats-officedocument.theme+xml"/>
        <Override PartName="/word/fontTable.xml"
            ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.fontTable+xml"/>
        <Override PartName="/word/webSettings.xml"
            ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.webSettings+xml"/>
        <Override PartName="/docProps/core.xml"
            ContentType="application/vnd.openxmlformats-package.core-properties+xml"/>
        <Override PartName="/word/settings.xml"
            ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.settings+xml"/>
        <Override PartName="/word/header1.xml"
            ContentType="application/vnd.openxmlformats-officedocument.wordprocessingml.header+xml"/>
-->

        <xsl:variable name="type-content"
            select="//ct:Override | document($docx.merge)//ct:Override[not(contains(@PartName,'diagrams'))]"/>
        <xsl:for-each-group select="$type-content" group-by="@PartName">
            <xsl:copy>
                <xsl:copy-of select="@*"/>
            </xsl:copy>
        </xsl:for-each-group>
        
        <!-- tractament de diagrames, es especial necessitem saber la iteració -->
        <xsl:variable name="type-content-diagrams" select="document($docx.merge)//ct:Override[contains(@PartName,'diagrams')]"/>
        <xsl:for-each select="$type-content-diagrams">
            <xsl:variable name="getFileName" select="substring-after(@PartName,'diagrams/')"/>
            <xsl:copy>
                <xsl:copy-of select="@*"/>
                <xsl:attribute name="PartName" select="concat('/word/diagrams/diagrams',$docx.iteracio,'/',$getFileName)"/>
            </xsl:copy>
        </xsl:for-each>
    </xsl:template>



</xsl:stylesheet>
