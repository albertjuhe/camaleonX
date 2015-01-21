<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ve="http://schemas.openxmlformats.org/markup-compatibility/2006"
    xmlns:o="urn:schemas-microsoft-com:office:office"
    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
    xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
    xmlns:v="urn:schemas-microsoft-com:vml"
    xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
    xmlns:w10="urn:schemas-microsoft-com:office:word"
    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
    xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
    xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
    xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
    xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
    xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
    xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
    xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" version="2.0">

    <!-- 

Estils per llistes ordenades i no ordenades.
-->
    <xsl:param name="docx.doc2"/>
    <xsl:param name="docx.iteracio"/>

    <xsl:output indent="no" omit-xml-declaration="no" standalone="yes" media-type="xml"
        encoding="UTF-8"/>

    <xsl:strip-space elements="*"/>
    
    <xsl:param name="totalAbstractNumbering" select="max(//w:abstractNum/@w:abstractNumId)"/>
        
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="w:numbering">
        <w:numbering xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
            xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
            xmlns:o="urn:schemas-microsoft-com:office:office"
            xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
            xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
            xmlns:v="urn:schemas-microsoft-com:vml"
            xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
            xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
            xmlns:w10="urn:schemas-microsoft-com:office:word"
            xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
            xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
            xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
            xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
            xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
            xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape"
            mc:Ignorable="w14 wp14">
            <xsl:comment><xsl:value-of select="$totalAbstractNumbering"/></xsl:comment>

            <xsl:apply-templates select="//w:abstractNum"/>
            <xsl:apply-templates select="document($docx.doc2)/w:numbering//w:abstractNum"
                mode="copia-doc2"/>
            <xsl:apply-templates select="//w:num"/>
            <xsl:apply-templates select="document($docx.doc2)/w:numbering//w:num" mode="copia-doc2"/>

        </w:numbering>
    </xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!--
    <w:abstractNum w:abstractNumId="0">
    -->
    <xsl:template match="w:abstractNum" mode="copia-doc2">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:if test="@w:abstractNumId">
                <xsl:attribute name="w:abstractNumId">
                    <xsl:value-of select="@w:abstractNumId + $totalAbstractNumbering + 1"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates mode="copia-doc2"/>
        </xsl:copy>
    </xsl:template>

    <!--
        w:num w:numId="7"
    -->
    <xsl:template match="w:num" mode="copia-doc2">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:if test="@w:numId">
                <xsl:attribute name="w:numId" select="@w:numId + $totalAbstractNumbering + 1"/>
            </xsl:if>
            <xsl:apply-templates mode="copia-doc2"/>
        </xsl:copy>
    </xsl:template>

    <!-- 
        w:abstractNumId w:val="1"
    -->
    <xsl:template match="w:abstractNumId" mode="copia-doc2">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:if test="@w:val">
                <xsl:attribute name="w:val">
                    <xsl:value-of select="@w:val + $totalAbstractNumbering + 1"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates mode="copia-doc2"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="w:lvlPicBulletId" mode="copia-doc2"/>

    <xsl:template match="w:lvlPicBulletId"/>

    <xsl:template match="*" mode="copia-doc2">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
