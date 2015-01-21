<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
    xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
    xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
    xmlns:ve="http://schemas.openxmlformats.org/markup-compatibility/2006"
    xmlns:o="urn:schemas-microsoft-com:office:office"
    xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
    xmlns:v="urn:schemas-microsoft-com:vml"
    xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
    xmlns:w10="urn:schemas-microsoft-com:office:word"
    xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
    xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
    xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
    xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
    xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
    xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" version="2.0">

    <xsl:param name="docx.merge"/>
    <xsl:param name="docx.numbering"/>

    <!-- Nombre maxim destils -->
    <xsl:param name="numero-estils" select="count(//w:style)"/>
    <xsl:param name="docx.iteracio"/>
    <xsl:param name="estils-doc1" select="//w:style"/>

    <xsl:param name="total-numbering-doc1"
        select="max(document($docx.numbering)//w:abstractNum/@w:abstractNumId)"/>

    <xsl:output indent="no" omit-xml-declaration="no" standalone="yes" media-type="xml"
        encoding="UTF-8"/>

    <xsl:strip-space elements="*"/>
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <!--
        Tractament dels estils dels dos docuemtns
    -->
    <xsl:template match="w:styles">
        <w:styles xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006"
            xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
            xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
            xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml" mc:Ignorable="w14">
            <xsl:apply-templates select="w:docDefaults | w:latentStyles"/>
            <xsl:apply-templates select="* except (w:docDefaults,w:latentStyles)"/>
            <xsl:comment>doc2</xsl:comment>
            <xsl:apply-templates select="document($docx.merge)//w:style" mode="copy"/>
            <xsl:comment>
                <xsl:value-of select="$total-numbering-doc1"/>
            </xsl:comment>
        </w:styles>
    </xsl:template>

    <xsl:template match="w:style">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!--
        
        Tractament dels estils del document 2.
    -->
    <xsl:template match="w:style" mode="copy">
        <xsl:variable name="estil-nom" select="@w:styleId"/>
        <!-- nomes copiem els estils que no existeixen al docuemnt 1 -->
        <xsl:if test="not($estils-doc1[@w:styleId=$estil-nom])">
            <xsl:copy>
                <xsl:copy-of select="@*"/>
                <xsl:apply-templates mode="copy"/>
            </xsl:copy>
        </xsl:if>

    </xsl:template>

    <xsl:template match="*" mode="copy">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates mode="copy"/>
        </xsl:copy>
    </xsl:template>

    <!--
        w:numId: controla el numbering
        Els estils poden tenir numbering, per aixo necessitem el num iteracio
        renumerem els numbering dels estils del docuemnt 2.
    -->
    <xsl:template match="w:numId" mode="copy">
        <xsl:copy>
            <xsl:attribute name="w:val">
                <xsl:value-of select="$total-numbering-doc1 + @w:val +1"/>
            </xsl:attribute>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
