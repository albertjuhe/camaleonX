<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:dc="http://purl.org/dc/elements/1.1/"   
    xmlns:ve="http://schemas.openxmlformats.org/markup-compatibility/2006"
    xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
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
    xmlns:wps="http://schemas.microsoft.com/office/word/2010/wordprocessingShape" version="2.0">

    <xsl:include href="./mode/docx2_copy.xsl"/>

    <xsl:param name="docx.merge"/>
    <xsl:param name="docx.numbering"/>
    <xsl:param name="docx.relations"/>
    <xsl:param name="docx.titol"/>
    <xsl:param name="docx.media"/>
    <xsl:param name="docx.iteracio"/>
    <xsl:param name="id-header" select="//w:sectPr[1]/w:headerReference/@r:id"/>
    <xsl:param name="main-header" select="//w:sectPr[1]"/>
    <xsl:param name="total-numbering-doc1" select="max(document($docx.numbering)//w:abstractNum/@w:abstractNumId)"/>
    <xsl:param name="salt-entre-documents" select="true()"/>
    <xsl:param name="wpdocPr-total" select="count(//wp:docPr)"/>
    
    <xsl:output indent="no" omit-xml-declaration="no" standalone="yes" media-type="xml"
        encoding="UTF-8"/>

    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="w:document">
        <w:document xmlns:ve="http://schemas.openxmlformats.org/markup-compatibility/2006"
            xmlns:wpc="http://schemas.microsoft.com/office/word/2010/wordprocessingCanvas"
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
            <xsl:apply-templates select="w:body"/>
        </w:document>
    </xsl:template>

    <xsl:template match="w:body">
        <w:body>
            <xsl:apply-templates select="* except w:sectPr"/>

            <!-- Document title -->
            <xsl:if test="normalize-space($docx.titol)">
                <xsl:call-template name="skeleton-title-document">
                    <xsl:with-param name="title-docx" select="$docx.titol"/>
                </xsl:call-template>
            </xsl:if>
            
            <xsl:if test="$salt-entre-documents">
                <xsl:call-template name="salt-pagina"/>
            </xsl:if>
            
            <xsl:apply-templates
                select="document(concat($docx.merge,'/document.xml'))/w:document/w:body/*"
                mode="docx2-copy"/>
            <xsl:apply-templates select="$main-header"/>
        </w:body>
    </xsl:template>
    <!-- w:rsidR="002401DB" w:rsidRPr="00F32D70" w:rsidRDefault="002401DB" w:rsidP="00BD7CD3"-->
    <xsl:template match="*">
        <xsl:copy>
            <xsl:copy-of select="@* except (@w:rsidR,@w:rsidRPr,@w:rsidRDefault,@w:rsidP)"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!--
        Add a title to the merged document.
        @title-docx: Document title.        
    -->
    <xsl:template name="skeleton-title-document">
        <xsl:param name="title-docx"/>

        <w:p>
            <w:pPr>
                <w:tabs>
                    <w:tab w:val="left" w:pos="465"/>
                </w:tabs>
                <w:jc w:val="center"/>
                <w:rPr>
                    <w:rFonts w:ascii="Verdana" w:hAnsi="Verdana"/>
                    <w:sz w:val="56"/>
                    <w:szCs w:val="56"/>
                </w:rPr>
            </w:pPr>
            <w:r>
                <w:rPr>
                    <w:rFonts w:ascii="Verdana" w:hAnsi="Verdana"/>
                    <w:sz w:val="56"/>
                    <w:szCs w:val="56"/>
                </w:rPr>
                <w:t>
                    <xsl:value-of select="$title-docx"/>
                </w:t>
            </w:r>
            <w:r>
                <w:rPr>
                    <w:rFonts w:ascii="Verdana" w:hAnsi="Verdana"/>
                    <w:sz w:val="56"/>
                    <w:szCs w:val="56"/>
                </w:rPr>
                <w:br/>
            </w:r>
            <w:r>
                <w:rPr>
                    <w:rFonts w:ascii="Verdana" w:hAnsi="Verdana"/>
                    <w:sz w:val="56"/>
                    <w:szCs w:val="56"/>
                </w:rPr>
                <w:br/>
            </w:r>
        </w:p>


    </xsl:template>
    
    <xsl:template match="w:bookmarkStart">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:attribute name="w:id">
                <xsl:number count="w:bookmarkStart" level="any" format="1"></xsl:number>
            </xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="w:bookmarkEnd">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:attribute name="w:id">
                <xsl:number count="w:bookmarkEnd" level="any" format="1"></xsl:number>
            </xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <!-- 
        Les imatges dins un document han de tenir un identificador únic, per
        això recalculem cada vegada el id del wp:docPr
    -->
    <xsl:template match="wp:docPr">
        
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:attribute name="id">
                <xsl:number count="wp:docPr" level="any" format="1"></xsl:number>
            </xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
    
    <!-- 
        Aquest codi provoca un salt de pàgina
    -->
    <xsl:template name="salt-pagina">
        <w:p>
            <w:pPr>
                <w:rPr>
                    <w:lang w:val="es-ES"/>
                </w:rPr>
            </w:pPr>
            <w:r>
                <w:rPr>
                    <w:lang w:val="es-ES"/>
                </w:rPr>
                <w:br w:type="page"/>
            </w:r>
        </w:p>        
    </xsl:template>
    
  
    <!--  | w:headerReference -->
    <xsl:template match="w:proofErr"/>

</xsl:stylesheet>
