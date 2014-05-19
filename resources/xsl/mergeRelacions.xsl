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
    xmlns="http://schemas.openxmlformats.org/package/2006/relationships"
    exclude-result-prefixes="#all" version="2.0">

    <xsl:param name="docx.merge"/>
    <xsl:param name="docx.media"/>
    <xsl:param name="relacions-maxim"
        select="max(//relation:Relationship[@Id]/number(substring-after(@Id,'rId')))"/>
    <xsl:param name="ESQUEMA_IMATGES_DOCX" select="'http://schemas.openxmlformats.org/officeDocument/2006/relationships/image'"/>
    <xsl:param name="ESQUEMA_EMBEDDINGS_DOCX" select="'http://schemas.openxmlformats.org/officeDocument/2006/relationships/oleObject'"/>
    <xsl:param name="ESQUEMA_DRAWINGS_DOCX" select="'http://schemas.openxmlformats.org/officeDocument/2006/relationships/vmlDrawing'"/>
    <!-- Necessari per els diagrames -->
    <xsl:param name="ESQUEMA_DIAGRAMES_QUICKSTYLE_DOCX" select="'http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramQuickStyle'"/>
    <xsl:param name="ESQUEMA_DIAGRAMES_LAYOUT_DOCX" select="'http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramLayout'"/>
    <xsl:param name="ESQUEMA_DIAGRAMES_DATA_DOCX" select="'http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramData'"/>
    <xsl:param name="ESQUEMA_DIAGRAMES_DRAWING_DOCX" select="'http://schemas.microsoft.com/office/2007/relationships/diagramDrawing'"/>
    <xsl:param name="ESQUEMA_DIAGRAMES_COLORS_DOCX" select="'http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramColors'"/>
    


    <xsl:output indent="no" omit-xml-declaration="no" standalone="yes" media-type="xml"
        encoding="UTF-8"/>
    <xsl:strip-space elements="*"/>
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="relation:Relationships">
        <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
            <xsl:apply-templates/>
           
            <xsl:apply-templates
                select="document($docx.merge)/relation:Relationships/relation:Relationship"
                mode="copy"/>
        </Relationships>
    </xsl:template>

    <xsl:template match="relation:Relationship">
        <xsl:if test="not(contains(@Target,'customXml') or contains(@Target,'theme/') or contains(@Target,'settings.xml'))">
            <Relationship>
                <xsl:copy-of select="@*"/>
                <xsl:if test="@Type=$ESQUEMA_IMATGES_DOCX">
                    <xsl:copy-of select="@Target"/>
                        
                </xsl:if>
            </Relationship>
        </xsl:if>
    </xsl:template>

    <xsl:template match="relation:Relationship" mode="copy">
        <xsl:if test="not(contains(@Target,'.xml')) or contains(@Target,'diagrams')">
            <Relationship>
                <xsl:copy-of select="@*"/>
                <xsl:if test="@Id">
                    <xsl:variable name="idv" select="number(substring-after(@Id,'rId'))"/>
                    <xsl:variable name="nou-identificador" select="$idv + $relacions-maxim"/>
                    <xsl:attribute name="Id">
                        <xsl:value-of select="concat('rId',$nou-identificador)"/>
                    </xsl:attribute>
                </xsl:if>
                <xsl:if test="@Type=$ESQUEMA_IMATGES_DOCX">
                    <xsl:attribute name="Target">
                        <xsl:value-of select="concat('media/media',number($docx.media) + 1,'/',substring-after(@Target,'/'))"/>
                    </xsl:attribute>
                </xsl:if>
                <xsl:if test="@Type=$ESQUEMA_EMBEDDINGS_DOCX">
                    <xsl:attribute name="Target">
                        <xsl:value-of select="concat('embeddings/embeddings',number($docx.media) + 1,'/',substring-after(@Target,'/'))"/>
                    </xsl:attribute>
                </xsl:if>
                <!-- diagrames -->
                <xsl:if test="@Type=$ESQUEMA_DIAGRAMES_COLORS_DOCX">
                    <xsl:attribute name="Target">
                        <xsl:value-of select="concat('diagrams/diagrams',number($docx.media) + 1,'/',substring-after(@Target,'/'))"/>
                    </xsl:attribute>
                </xsl:if>
                <xsl:if test="@Type=$ESQUEMA_DIAGRAMES_DRAWING_DOCX">
                    <xsl:attribute name="Target">
                        <xsl:value-of select="concat('diagrams/diagrams',number($docx.media) + 1,'/',substring-after(@Target,'/'))"/>
                    </xsl:attribute>
                </xsl:if>
                <xsl:if test="@Type=$ESQUEMA_DIAGRAMES_DATA_DOCX">
                    <xsl:attribute name="Target">
                        <xsl:value-of select="concat('diagrams/diagrams',number($docx.media) + 1,'/',substring-after(@Target,'/'))"/>
                    </xsl:attribute>
                </xsl:if>
                <xsl:if test="@Type=$ESQUEMA_DIAGRAMES_LAYOUT_DOCX">
                    <xsl:attribute name="Target">
                        <xsl:value-of select="concat('diagrams/diagrams',number($docx.media) + 1,'/',substring-after(@Target,'/'))"/>
                    </xsl:attribute>
                </xsl:if>
                <xsl:if test="@Type=$ESQUEMA_DIAGRAMES_QUICKSTYLE_DOCX">
                    <xsl:attribute name="Target">
                        <xsl:value-of select="concat('diagrams/diagrams',number($docx.media) + 1,'/',substring-after(@Target,'/'))"/>
                    </xsl:attribute>
                </xsl:if>
                 
            </Relationship>
        </xsl:if>
    </xsl:template>


    <xsl:template match="*"/>


</xsl:stylesheet>
