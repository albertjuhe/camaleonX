<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
    xmlns:relation="http://schemas.openxmlformats.org/package/2006/relationships"
    exclude-result-prefixes="#all" version="2.0">

    <!-- 
        <drawings>
        <drawing origen="vmlDrawing1.vml" rel_origen="1" desti="vmlDrawing2.vml" rel_desti="2"/>
        <drawing origen="vmlDrawing2.vml" rel_origen="2" desti="vmlDrawing3.vml" rel_desti="3"/>
        <drawing origen="vmlDrawing3.vml" rel_origen="3" desti="vmlDrawing4.vml" rel_desti="4"/>
        <drawing origen="vmlDrawing4.vml" rel_origen="4" desti="vmlDrawing5.vml" rel_desti="5"/>
        <drawing origen="vmlDrawing5.vml" rel_origen="5" desti="vmlDrawing6.vml" rel_desti="6"/>
        <drawing origen="vmlDrawing6.vml" rel_origen="6" desti="vmlDrawing7.vml" rel_desti="7"/>
        </drawings>
    -->
    <xsl:output indent="no" omit-xml-declaration="no" standalone="yes" media-type="xml"
        encoding="UTF-8"/>

    <xsl:param name="pptx.tmp"/>
    <xsl:param name="pptx.doc2"/>
    <xsl:param name="numdoc"/>

    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates select="drawings"/>
    </xsl:template>

    <xsl:template match="drawings">
      
        <xsl:for-each select="drawing">
            <xsl:variable name="rel-drawing-origen"
                select="concat($pptx.doc2,'/ppt/drawings/_rels/vmlDrawing',@rel_origen,'.vml.rels')"/>
            <xsl:variable name="rel-drawing-desti"
                select="concat($pptx.tmp,'/ppt/drawings/_rels/vmlDrawing',@rel_desti,'.vml.rels')"/>
            <xsl:variable name="document-origen"
                select="document($rel-drawing-origen)//relation:Relationship"/>

            <xsl:result-document href="{$rel-drawing-desti}">
                <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
                    <xsl:apply-templates select="$document-origen"/>
                </Relationships>
            </xsl:result-document>
        </xsl:for-each>
        
    </xsl:template>

    <xsl:template match="relation:Relationship">

        <xsl:choose>
            <xsl:when
                test="@Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/image'">
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id"/>
                    <xsl:attribute name="Target">
                        <xsl:value-of
                            select="replace(@Target,'../media/',concat('../media/media',$numdoc,'/'))"
                        />
                    </xsl:attribute>
                </xsl:copy>
            </xsl:when>
            <xsl:when
                test="@Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/oleObject'">
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id"/>
                    <xsl:attribute name="Target">
                        <xsl:value-of
                            select="replace(@Target,'../embeddings/',concat('../embeddings/embeddings',$numdoc,'/'))"
                        />
                    </xsl:attribute>
                </xsl:copy>
            </xsl:when>
            <xsl:when
                test="@Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/vmlDrawing'">
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id"/>
                    <xsl:attribute name="Target">
                        <xsl:value-of
                            select="replace(@Target,'../embeddings/',concat('../embeddings/embeddings',$numdoc,'/'))"
                        />
                    </xsl:attribute>
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:copy-of select="@*"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>
</xsl:stylesheet>
