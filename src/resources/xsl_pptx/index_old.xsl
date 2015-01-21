<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns="http://schemas.openxmlformats.org/package/2006/content-types"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:ct="http://schemas.openxmlformats.org/package/2006/content-types"
    exclude-result-prefixes="#all" version="2.0">

    <xsl:include href="./utils/general.xsl"/>
    <xsl:include href="./params/general.xsl"/>

    <xsl:param name="pptx.merge"/>
    <xsl:param name="pptx.tmp"/>
    <xsl:param name="pptx.docfinal"/>
    <xsl:param name="pptx.doc2"/>
    <xsl:param name="pptx.job"/>

    <xsl:output indent="no" omit-xml-declaration="no" standalone="yes" media-type="xml"
        encoding="UTF-8"/>

    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <Types xmlns="http://schemas.openxmlformats.org/package/2006/content-types">
            <!-- Copiem overrides -->
            <xsl:apply-templates select="//ct:Override"/>
            <!-- Copiem tipus de fitxer que poden apareixer al pptx -->
            <xsl:call-template name="type-content"/>
            <!-- 
                Elements numerables que s'han de comparar amb el document 2:
                notesMaster
                theme
                notesSlide
                slideMaster
                slideLayout
                slide
                handoutMaster
                tags
                
                Obtenim el maxim número de cada tipologia d'aquestes, p.ex.
                slide1
                slide2
                ...
                slide9
                
                obtenim dins la variable slide un 9, aquest número serirà per renumerar el doc2.
            -->
            <xsl:call-template name="create-override"/>

        </Types>
    </xsl:template>

    <xsl:template name="create-override">

        <!-- Modifiquem tots els override del document 2 en funcio del 1 -->
        <xsl:variable name="content-type-doc2" select="document($pptx.merge)//ct:Override"/>

        <!-- notesMaster -->
        <xsl:variable name="notesMaster">
            <xsl:call-template name="toc">
                <xsl:with-param name="path-etiqueta" select="$NOTESMASTER-TYPE"/>
                <xsl:with-param name="nom-etiqueta" select="$NOTESMASTER"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="modifica-override-numeric">
            <xsl:with-param name="nodes"
                select="$content-type-doc2[contains(@ContentType,$NOTESMASTER-TYPE)]"/>
            <xsl:with-param name="tipo" select="$NOTESMASTER"/>
            <xsl:with-param name="slide" select="$notesMaster"/>
        </xsl:call-template>
        <!-- FI notesMaster -->

        <!-- theme -->
        <xsl:variable name="theme">
            <xsl:call-template name="toc">
                <xsl:with-param name="path-etiqueta" select="$THEME-TYPE"/>
                <xsl:with-param name="nom-etiqueta" select="$THEME"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="modifica-override-numeric">
            <xsl:with-param name="nodes"
                select="$content-type-doc2[contains(@ContentType,$THEME-TYPE)]"/>
            <xsl:with-param name="tipo" select="$THEME"/>
            <xsl:with-param name="slide" select="$theme"/>
        </xsl:call-template>
        <!-- FI theme -->
        
        <!-- tag -->
        <xsl:variable name="tag">
            <xsl:call-template name="toc">
                <xsl:with-param name="path-etiqueta" select="$TAG-TYPE"/>
                <xsl:with-param name="nom-etiqueta" select="$TAG"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="modifica-override-numeric">
            <xsl:with-param name="nodes"
                select="$content-type-doc2[contains(@ContentType,$TAG-TYPE)]"/>
            <xsl:with-param name="tipo" select="$TAG"/>
            <xsl:with-param name="slide" select="$tag"/>
        </xsl:call-template>
        <!-- FI tag -->

        <!-- notesSlide -->
        <xsl:variable name="notesSlide">
            <xsl:call-template name="toc">
                <xsl:with-param name="path-etiqueta" select="$NOTESSLIDE-TYPE"/>
                <xsl:with-param name="nom-etiqueta" select="$NOTESSLIDE"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="modifica-override-numeric">
            <xsl:with-param name="nodes"
                select="$content-type-doc2[contains(@ContentType,$NOTESSLIDE-TYPE)]"/>
            <xsl:with-param name="tipo" select="$NOTESSLIDE"/>
            <xsl:with-param name="slide" select="$notesSlide"/>
        </xsl:call-template>
        <!-- FI notesSlide -->

        <!-- slideMaster -->
        <xsl:variable name="slideMaster">
            <xsl:call-template name="toc">
                <xsl:with-param name="path-etiqueta" select="$SLIDEMASTER-TYPE"/>
                <xsl:with-param name="nom-etiqueta" select="$SLIDEMASTER"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="modifica-override-numeric">
            <xsl:with-param name="nodes"
                select="$content-type-doc2[contains(@ContentType,$SLIDEMASTER-TYPE)]"/>
            <xsl:with-param name="tipo" select="$SLIDEMASTER"/>
            <xsl:with-param name="slide" select="$slideMaster"/>
        </xsl:call-template>
        <!-- FI slideMaster -->

        <!-- slideLayout -->
        <xsl:variable name="slideLayout">
            <xsl:call-template name="toc">
                <xsl:with-param name="path-etiqueta" select="$SLIDELAYOUT-TYPE"/>
                <xsl:with-param name="nom-etiqueta" select="$SLIDELAYOUT"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="modifica-override-numeric">
            <xsl:with-param name="nodes"
                select="$content-type-doc2[contains(@ContentType,$SLIDELAYOUT-TYPE)]"/>
            <xsl:with-param name="tipo" select="$SLIDELAYOUT"/>
            <xsl:with-param name="slide" select="$slideLayout"/>
        </xsl:call-template>
        <!-- FI slideLayout -->

        <!-- handoutMaster -->
        <xsl:variable name="handoutMaster">
            <xsl:call-template name="toc">
                <xsl:with-param name="path-etiqueta" select="$HANDOUTMASTER-TYPE"/>
                <xsl:with-param name="nom-etiqueta" select="$HANDOUTMASTER"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="modifica-override-numeric">
            <xsl:with-param name="nodes"
                select="$content-type-doc2[contains(@ContentType,$HANDOUTMASTER-TYPE)]"/>
            <xsl:with-param name="tipo" select="$HANDOUTMASTER"/>
            <xsl:with-param name="slide" select="$handoutMaster"/>
        </xsl:call-template>
        <!-- FI handoutMaster -->

        <!-- SLIDE -->
        <xsl:variable name="slide">
            <xsl:call-template name="toc">
                <xsl:with-param name="path-etiqueta" select="$SLIDE-TYPE"/>
                <xsl:with-param name="nom-etiqueta" select="$SLIDE"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="modifica-override-numeric">
            <xsl:with-param name="nodes"
                select="$content-type-doc2[contains(@ContentType,$SLIDE-TYPE)]"/>
            <xsl:with-param name="tipo" select="$SLIDE"/>
            <xsl:with-param name="slide" select="$slide"/>
        </xsl:call-template>
        <!-- FI SLIDE -->

        <!-- themeOverride -->
        <xsl:variable name="themeOverride">
            <xsl:call-template name="toc">
                <xsl:with-param name="path-etiqueta" select="$THEMEOVERRIDE-TYPE"/>
                <xsl:with-param name="nom-etiqueta" select="$THEMEOVERRIDE"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:call-template name="modifica-override-numeric">
            <xsl:with-param name="nodes"
                select="$content-type-doc2[contains(@ContentType,$THEMEOVERRIDE-TYPE)]"/>
            <xsl:with-param name="tipo" select="$THEMEOVERRIDE"/>
            <xsl:with-param name="slide" select="$themeOverride"/>
        </xsl:call-template>
        <!-- FI themeOverride -->

        <!-- bolquem el resultat en un fitxer extern que renombrara els fitxers -->
        <xsl:result-document href="{concat($pptx.tmp,'relation.xml')}">
            <files xmlns="http://omaonk.com/relationfile" job="{$pptx.job}">
                <xsl:call-template name="modifica-override-numeric">
                    <xsl:with-param name="nodes"
                        select="$content-type-doc2[contains(@ContentType,$NOTESMASTER-TYPE)]"/>
                    <xsl:with-param name="tipo" select="$NOTESMASTER"/>
                    <xsl:with-param name="slide" select="$notesMaster"/>
                    <xsl:with-param name="internal" select="false()"/>
                </xsl:call-template>

                <xsl:call-template name="modifica-override-numeric">
                    <xsl:with-param name="nodes"
                        select="$content-type-doc2[contains(@ContentType,$THEME-TYPE)]"/>
                    <xsl:with-param name="tipo" select="$THEME"/>
                    <xsl:with-param name="slide" select="$theme"/>
                    <xsl:with-param name="internal" select="false()"/>
                </xsl:call-template>
                
                <xsl:call-template name="modifica-override-numeric">
                    <xsl:with-param name="nodes"
                        select="$content-type-doc2[contains(@ContentType,$TAG-TYPE)]"/>
                    <xsl:with-param name="tipo" select="$TAG"/>
                    <xsl:with-param name="slide" select="$tag"/>
                    <xsl:with-param name="internal" select="false()"/>
                </xsl:call-template>

                <xsl:call-template name="modifica-override-numeric">
                    <xsl:with-param name="nodes"
                        select="$content-type-doc2[contains(@ContentType,$NOTESSLIDE-TYPE)]"/>
                    <xsl:with-param name="tipo" select="$NOTESSLIDE"/>
                    <xsl:with-param name="slide" select="$notesSlide"/>
                    <xsl:with-param name="internal" select="false()"/>
                </xsl:call-template>

                <xsl:call-template name="modifica-override-numeric">
                    <xsl:with-param name="nodes"
                        select="$content-type-doc2[contains(@ContentType,$SLIDEMASTER-TYPE)]"/>
                    <xsl:with-param name="tipo" select="$SLIDEMASTER"/>
                    <xsl:with-param name="slide" select="$slideMaster"/>
                    <xsl:with-param name="internal" select="false()"/>
                </xsl:call-template>

                <xsl:call-template name="modifica-override-numeric">
                    <xsl:with-param name="nodes"
                        select="$content-type-doc2[contains(@ContentType,$SLIDELAYOUT-TYPE)]"/>
                    <xsl:with-param name="tipo" select="$SLIDELAYOUT"/>
                    <xsl:with-param name="slide" select="$slideLayout"/>
                    <xsl:with-param name="internal" select="false()"/>
                </xsl:call-template>

                <xsl:call-template name="modifica-override-numeric">
                    <xsl:with-param name="nodes"
                        select="$content-type-doc2[contains(@ContentType,$HANDOUTMASTER-TYPE)]"/>
                    <xsl:with-param name="tipo" select="$HANDOUTMASTER"/>
                    <xsl:with-param name="slide" select="$handoutMaster"/>
                    <xsl:with-param name="internal" select="false()"/>
                </xsl:call-template>

                <xsl:call-template name="modifica-override-numeric">
                    <xsl:with-param name="nodes"
                        select="$content-type-doc2[contains(@ContentType,$SLIDE-TYPE)]"/>
                    <xsl:with-param name="tipo" select="$SLIDE"/>
                    <xsl:with-param name="slide" select="$slide"/>
                    <xsl:with-param name="internal" select="false()"/>
                </xsl:call-template>

                <xsl:call-template name="modifica-override-numeric">
                    <xsl:with-param name="nodes"
                        select="$content-type-doc2[contains(@ContentType,$THEMEOVERRIDE-TYPE)]"/>
                    <xsl:with-param name="tipo" select="$THEMEOVERRIDE"/>
                    <xsl:with-param name="slide" select="$themeOverride"/>
                    <xsl:with-param name="internal" select="false()"/>
                </xsl:call-template>
            </files>
        </xsl:result-document>

    </xsl:template>
    <!-- 
        Modifica una tipologia de document override
    -->
    <xsl:template name="modifica-override-numeric">
        <xsl:param name="nodes"/>
        <xsl:param name="tipo"/>
        <xsl:param name="slide"/>
        <xsl:param name="internal" select="true()"/>

        <xsl:for-each select="$nodes">
            <xsl:variable name="num-doc2-file" as="xs:integer">
                <xsl:call-template name="get-num-file">
                    <xsl:with-param name="path-file" select="@PartName"/>
                    <xsl:with-param name="nom-etiqueta" select="$tipo"/>
                </xsl:call-template>
            </xsl:variable>
            <!-- num:<xsl:value-of select="$num-doc2-file"/> per <xsl:value-of select="$slide"/> -->

            <xsl:variable name="rename-file"
                select="replace(@PartName,concat($tipo,$num-doc2-file),concat($tipo,$num-doc2-file + $slide))"/>
            <xsl:choose>
                <xsl:when test="$internal">
                    <xsl:copy>
                        <xsl:copy-of select="@ContentType"/>
                        <xsl:attribute name="PartName">
                            <xsl:value-of select="$rename-file"/>
                        </xsl:attribute>
                    </xsl:copy>
                </xsl:when>
                <xsl:otherwise>
                    <!-- source -->
                    <xsl:variable name="path" select="tokenize(concat($pptx.doc2,@PartName), '/')"/>
                    <xsl:variable name="path-string">
                        <xsl:call-template name="get-path">
                            <xsl:with-param name="path-complert" select="$path"/>
                        </xsl:call-template>
                    </xsl:variable>

                    <!-- target -->
                    <xsl:variable name="path"
                        select="tokenize(concat($pptx.docfinal,$rename-file), '/')"/>
                    <xsl:variable name="path-string-target">
                        <xsl:call-template name="get-path">
                            <xsl:with-param name="path-complert" select="$path"/>
                        </xsl:call-template>
                    </xsl:variable>

                    <file dir_source="{concat('/',$pptx.doc2,@PartName)}"
                        path_source="{$path-string}" file_source="{concat($tipo,$num-doc2-file)}"
                        dir_target="{concat('/',$pptx.docfinal,$rename-file)}"
                        file_target="{concat($tipo,$num-doc2-file + $slide)}"
                        path_target="{$path-string-target}"/>
                </xsl:otherwise>
            </xsl:choose>

        </xsl:for-each>

    </xsl:template>

    <xsl:template name="get-path">
        <xsl:param name="path-complert"/>

        <xsl:for-each select="$path-complert">
            <xsl:if test="position()!=last()">
                <xsl:value-of select="concat('/',.)"/>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

    <!-- 
        Tractament del override del document 1, custom i app contenen informació tipo metadades, es pot eliminar. 
    -->
    <xsl:template match="ct:Override">
        <!--
        <xsl:choose>
            
            <xsl:when
                test="contains(@PartName,'/docProps/app.xml') or contains(@PartName,'/docProps/custom.xml')"/>
            
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:copy-of select="@*"/>
                </xsl:copy>
            </xsl:otherwise>
        </xsl:choose>
        -->
        <xsl:copy>
            <xsl:copy-of select="@*"/>
        </xsl:copy>
    </xsl:template>

    <!--
        Tractament dels tipus de document que poden apareixer.
     -->
    <xsl:template name="type-content">
        <xsl:variable name="type-content" select="//ct:Default | document($pptx.merge)//ct:Default"/>
        <xsl:for-each-group select="$type-content" group-by="@Extension">
            <xsl:copy>
                <xsl:copy-of select="@*"/>
            </xsl:copy>
        </xsl:for-each-group>
    </xsl:template>

    <!-- 
        Retorna el num maxim d'un tipus de document.
    -->
    <xsl:template name="toc">
        <xsl:param name="path-etiqueta"/>
        <xsl:param name="nom-etiqueta"/>

        <!-- Contem cada un daquests elements -->
        <xsl:variable name="num-elements"
            select="//ct:Override[contains(@ContentType,$path-etiqueta)]"/>
        <xsl:value-of select="count($num-elements)"></xsl:value-of>
<!--
        <xsl:choose>
            <xsl:when test="$num-elements">
                <xsl:for-each select="$num-elements">
                    <xsl:sort select="@PartName"/>
                    <xsl:if test="position()=last()">
                       
                        <xsl:call-template name="get-num-file">
                            <xsl:with-param name="nom-etiqueta" select="$nom-etiqueta"/>
                        </xsl:call-template>
                    </xsl:if>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>0</xsl:otherwise>
        </xsl:choose>
        -->
    </xsl:template>

    <xsl:template name="get-num-file">
        <xsl:param name="path-file" select="@PartName"/>
        <xsl:param name="nom-etiqueta"/>

        <xsl:variable name="partName">
            <xsl:call-template name="strip-path">
                <xsl:with-param name="string-value" select="$path-file"/>
            </xsl:call-template>
        </xsl:variable>
        <!-- Obtenim el numero de fitxer -->
        <xsl:variable name="numero-fitxer" select="substring-after($partName,$nom-etiqueta)"/>
        <xsl:value-of select="substring-before($numero-fitxer,'.')"/>
    </xsl:template>

</xsl:stylesheet>
