<?xml version="1.0" encoding="UTF-8"?>
<!-- 
Processa el pptx de presentation.xml.rels

9-11-2011 Eliminem els comentarisAuthor.xml
-->
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
    xmlns:omaonk="http://omaonk.com/relationfile"
    xmlns="http://schemas.openxmlformats.org/package/2006/relationships"
    xmlns:ct="http://schemas.openxmlformats.org/package/2006/content-types"
    exclude-result-prefixes="#all" version="2.0">

    <xsl:include href="./utils/general.xsl"/>

    <xsl:param name="pptx.merge"/>
    <xsl:param name="pptx.relacions"/>
    <xsl:param name="pptx.numrelacions"/>
    <!-- Indica si volem que posi un slide de portada -->
    <xsl:param name="portada" select="0"/>
    <!-- fitxer que guarda el num de identificadors del primer document -->
    <xsl:param name="annex" select="0"/>
    <xsl:param name="tasca.xml"/>

    <xsl:param name="relacions-maxim"
        select="max(//relation:Relationship[@Id]/number(substring-after(@Id,'rId')))"/>
    <xsl:param name="ESQUEMA_IMATGES_DOCX"
        select="'http://schemas.openxmlformats.org/officeDocument/2006/relationships/image'"/>

    <xsl:key name="relacions" match="relation:Relationship" use="@Target"/>

    <xsl:output indent="no" omit-xml-declaration="no" standalone="yes" media-type="xml"
        encoding="UTF-8"/>
    <xsl:strip-space elements="*"/>
    <xsl:template match="/">
        <xsl:apply-templates/>

        <xsl:result-document href="{$pptx.numrelacions}">
            <relacions>
                <xsl:value-of select="$relacions-maxim"/>
            </relacions>
        </xsl:result-document>
    </xsl:template>

    <xsl:template match="relation:Relationships">
        <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
            <!-- deprecated           
            <xsl:if test="number($portada)=1">
           
                <Relationship Id="rId9999"
                    Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/slide"
                    Target="slides/slide999.xml"/>
            </xsl:if>
            -->
            <!-- tractament dels annexos -->
            <xsl:if test="number($annex)=1">
                <xsl:variable name="tree-processos-annexes" select="document($tasca.xml)//annexes"/>
                <xsl:for-each select="$tree-processos-annexes/annex[@include=1][@href]">
                    <xsl:variable name="identificador-slide" select="9000 + position()"/>
                    <Relationship Id="rId{$identificador-slide}"
                        Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/slide"
                        Target="slides/slide{$identificador-slide}.xml"/>
                </xsl:for-each>
            </xsl:if>
            <xsl:apply-templates/>

            <xsl:apply-templates
                select="document($pptx.merge)/relation:Relationships/relation:Relationship"
                mode="copy"/>
        </Relationships>
    </xsl:template>

    <xsl:template match="relation:Relationship">
        <Relationship>
            <xsl:copy-of select="@*"/>

        </Relationship>
    </xsl:template>

    <xsl:template match="relation:Relationship" mode="copy">

        <!-- 
            Hi han tipus de relacions especials, que no van numerades
        -->
        <xsl:choose>
            <xsl:when
                test="
                contains(@Target,'theme/theme') or 
                contains(@Target,'notesMasters/notesMaster') or 
                contains(@Target,'handoutMasters/handoutMaster') or
                contains(@Target,'tags/tag')"/>
            <!-- Anulem els comentaris d'autor -->
            <xsl:when test="@Target='commentAuthors.xml'"/>
            <xsl:when test="contains(@Target,'customXml/item')"/>
            <xsl:when
                test="@Target='tableStyles.xml' or 
                @Target='presProps.xml' or 
                @Target='viewProps.xml'">
                <xsl:variable name="relacio" select="key('relacions',@Target)"/>
                <xsl:if test="not($relacio)">
                    <Relationship>
                        <xsl:copy-of select="@*"/>
                    </Relationship>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>

                <Relationship>
                    <xsl:copy-of select="@*"/>
                    <xsl:if test="@Id">
                        <xsl:variable name="idv" select="number(substring-after(@Id,'rId'))"/>
                        <xsl:variable name="nou-identificador" select="$idv + $relacions-maxim"/>
                        <xsl:attribute name="Id">
                            <xsl:value-of select="concat('rId',$nou-identificador)"/>
                        </xsl:attribute>
                    </xsl:if>
                    <xsl:if test="@Target">
                        <xsl:variable name="target">
                            <xsl:call-template name="strip-path">
                                <xsl:with-param name="string-value" select="@Target"/>
                            </xsl:call-template>
                        </xsl:variable>
                        <xsl:variable name="relation-file"
                            select="document($pptx.relacions)//ct:file[contains($target,concat(@file_source,'.xml'))]"/>
                        <xsl:choose>
                            <xsl:when test="$relation-file">
                                <xsl:attribute name="Target">
                                    <xsl:value-of
                                        select="replace(@Target,$target,concat($relation-file/@file_target,'.xml'))"
                                    />
                                </xsl:attribute>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:copy-of select="@Target"/>
                            </xsl:otherwise>
                        </xsl:choose>

                    </xsl:if>

                </Relationship>

            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>


    <xsl:template match="*"/>


</xsl:stylesheet>
