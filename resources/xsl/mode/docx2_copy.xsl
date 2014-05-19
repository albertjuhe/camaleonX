<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:ve="http://schemas.openxmlformats.org/markup-compatibility/2006"
    xmlns:o="urn:schemas-microsoft-com:office:office"
    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
    xmlns:relation="http://schemas.openxmlformats.org/package/2006/relationships"
    xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math"
    xmlns:v="urn:schemas-microsoft-com:vml"
    xmlns:wp="http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing"
    xmlns:w10="urn:schemas-microsoft-com:office:word"
    xmlns:w="http://schemas.openxmlformats.org/wordprocessingml/2006/main"
    xmlns:wne="http://schemas.microsoft.com/office/word/2006/wordml"
    xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:w14="http://schemas.microsoft.com/office/word/2010/wordml"
    xmlns:wpg="http://schemas.microsoft.com/office/word/2010/wordprocessingGroup"
    xmlns:wpi="http://schemas.microsoft.com/office/word/2010/wordprocessingInk"
    xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape"
    xmlns:wp14="http://schemas.microsoft.com/office/word/2010/wordprocessingDrawing"
    xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
    exclude-result-prefixes="xs relation" version="2.0">

    <xsl:param name="relacions-maxim"
        select="max(document(concat($docx.relations,'//_rels/document.xml.rels'))//relation:Relationship[@Id]/number(substring-after(@Id,'rId')))"/>

    <xsl:template match="*" mode="docx2-copy">
        <xsl:copy>
            <xsl:copy-of select="@* except (@w:rsidR,@w:rsidRPr,@w:rsidRDefault,@w:rsidP)"/>
            <xsl:call-template name="attributes-treatment"/>
            <xsl:apply-templates mode="docx2-copy"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template name="attributes-treatment">
        <xsl:for-each select="@* except (@w:rsidR,@w:rsidRPr,@w:rsidRDefault,@w:rsidP)">
            <xsl:choose>
                <xsl:when test="contains(.,'rId')">
                    <xsl:variable name="idv" select="number(substring-after(.,'rId'))"/>
                    <xsl:attribute name="{name(.)}">
                        <xsl:value-of select="concat('rId',$relacions-maxim + $idv)"/>
                    </xsl:attribute>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:attribute name="{name(.)}">
                        <xsl:value-of select="."/>
                    </xsl:attribute>
                </xsl:otherwise>
            </xsl:choose>

        </xsl:for-each>
    </xsl:template>

    <xsl:template match="w:pStyle" mode="docx2-copy">

        <w:pStyle>
            <!--    <xsl:attribute name="w:val"><xsl:value-of select="concat(@w:val,'001')"/></xsl:attribute> -->
            <xsl:attribute name="w:val">
                <xsl:value-of select="@w:val"/>
            </xsl:attribute>
        </w:pStyle>
    </xsl:template>

    <xsl:template match="w:rStyle" mode="docx2-copy">
        <w:rStyle>
            <!--<xsl:attribute name="w:val"><xsl:value-of select="concat(@w:val,'001')"/></xsl:attribute>-->
            <xsl:attribute name="w:val">
                <xsl:value-of select="@w:val"/>
            </xsl:attribute>
        </w:rStyle>
    </xsl:template>

    <xsl:template match="w:rFonts" mode="docx2-copy">
        <w:rFonts>
            <xsl:copy-of select="@*"/>
            <xsl:variable name="fonts" select="@w:ascii | @w:hAnsi | @w:cs"/>
            <xsl:if test="not(@w:cs)">
                <xsl:attribute name="w:cs">
                    <xsl:value-of select="$fonts"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="not(@w:ascii)">
                <xsl:attribute name="w:ascii">
                    <xsl:value-of select="$fonts"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="not(@w:hAnsi)">
                <xsl:attribute name="w:hAnsi">
                    <xsl:value-of select="$fonts"/>
                </xsl:attribute>
            </xsl:if>
        </w:rFonts>
    </xsl:template>

    <!--
        Modificacio de llistes ordenades i sense ordenar
        <w:numPr>
        <w:ilvl w:val="0"/>
        <w:numId w:val="9"/>
        </w:numPr>
    -->
    <xsl:template match="w:numPr/w:numId" mode="docx2-copy">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:if test="@w:val">
                <xsl:attribute name="w:val">
                    <xsl:value-of select="@w:val + $total-numbering-doc1 + 1"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates mode="docx2-copy"/>
        </xsl:copy>
    </xsl:template>

    <!-- w:headerReference -->
    <xsl:template match="w:proofErr" mode="docx2-copy"/>

    <!-- w:headerReference -->
    <xsl:template match="w:sectPr" mode="docx2-copy">
        <xsl:if test="$salt-entre-documents">
            <xsl:choose>
                <xsl:when test="w:headerReference or w:footerReference"/>
                <xsl:otherwise>
                    <xsl:copy>
                        <xsl:copy-of select="@*"/>
                        <xsl:apply-templates mode="copy-sectr"/>
                    </xsl:copy>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:if>
    </xsl:template>

    <xsl:template match="wp:docPr" mode="docx2-copy">
        
        <xsl:variable name="posisicio-wpdocpr">
            <xsl:number count="wp:docPr" level="any" format="1"></xsl:number>
        </xsl:variable>
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:attribute name="id" select="$wpdocPr-total + $posisicio-wpdocpr"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*" mode="copy-sectr">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates mode="copy-sectr"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
