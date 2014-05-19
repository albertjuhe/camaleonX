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
    xmlns:dc="http://purl.org/dc/elements/1.1/" 
    xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs relation"
    version="2.0">

    <!--xsl:param name="relacions-maxim" select="max(//@*[contains(.,'rId')]/number(substring-after(.,'rId')))"/-->
    <xsl:param name="relacions-maxim" select="max(document(concat($docx.relations,'//_rels/document.xml.rels'))//relation:Relationship[@Id]/number(substring-after(@Id,'rId')))"/>
    
    <xsl:template match="*" mode="docx2-copy">
        <xsl:copy>
            <xsl:copy-of select="@* except (@w:rsidR,@w:rsidRPr,@w:rsidRDefault,@w:rsidP)"/>
            <xsl:call-template name="attributes-treatment"/>
            <xsl:apply-templates mode="docx2-copy"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- Tractament de relacions -->
    <!--
    <xsl:template match="w:hyperlink[@r:id]" mode="docx2-copy">
        <xsl:copy>
            <xsl:copy-of select="@* except @r:id"/>
            <xsl:attribute name="r:id">
                <xsl:value-of select="concat(@r:id,'02')"/>
            </xsl:attribute>
            <xsl:apply-templates mode="docx2-copy"/>
        </xsl:copy>
    </xsl:template>
    
   
    <xsl:template match="a:blip[@r:embed]" mode="docx2-copy">
        <xsl:copy>
            <xsl:copy-of select="@* except @r:embed"/>
            <xsl:attribute name="r:embed">
                <xsl:value-of select="concat(@r:embed,'02')"/>
            </xsl:attribute>
            <xsl:apply-templates mode="docx2-copy"/>
        </xsl:copy>
    </xsl:template>
    -->
    
    <xsl:template name="attributes-treatment">
        <xsl:for-each select="@* except (@w:rsidR,@w:rsidRPr,@w:rsidRDefault,@w:rsidP)">
            <xsl:choose>
                <xsl:when test="contains(.,'rId')">
                    <xsl:variable name="idv" select="number(substring-after(.,'rId'))"></xsl:variable>
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
            <xsl:attribute name="w:val"><xsl:value-of select="concat(@w:val,'001')"/></xsl:attribute>
        </w:pStyle>
    </xsl:template>
    
    <xsl:template match="w:rStyle" mode="docx2-copy">
        <w:rStyle>
            <xsl:attribute name="w:val"><xsl:value-of select="concat(@w:val,'001')"/></xsl:attribute>
        </w:rStyle>
    </xsl:template>
        
    <xsl:template match="w:proofErr | w:headerReference" mode="docx2-copy"/>

</xsl:stylesheet>
