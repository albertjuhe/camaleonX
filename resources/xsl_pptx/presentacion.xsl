<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:relation="http://schemas.openxmlformats.org/package/2006/relationships"
    xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
    xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main"
    exclude-result-prefixes="#all" version="2.0">

    <!-- Indica si volem que posi un slide de portada -->
    <xsl:param name="portada" select="0"/>
    <xsl:param name="pptx.merge"/>
    <xsl:param name="pptx.relacions"/>
    <!-- path al document final ho farem servir per saber on crear els arxius d'annexes -->
    <xsl:param name="pptx.doc_final"/>
    <!-- Segon fitxer que fusionem -->
    <xsl:param name="pptx.doc2"/>
    <!-- fitxer inicial -->
    <xsl:param name="pptx.doc1"/>

    <xsl:param name="num-relacions" select="document($pptx.relacions)/relation:relacions"/>
    <!-- Aquest arxiu ens l'envia l'aplicacio web, hi ha l'abre de fusio -->
    <xsl:param name="tasca.xml" select="'tree.xml'"/>
    <!-- Indica si volem que posi anexxos, en aquest cas la informaco esta al parametre tasca.xml -->
    <xsl:param name="annex" select="0"/>

    <!-- Obtenim el valor maxim de p:sldId -->
    <xsl:param name="max-sldId" select="max(//p:sldId/@id)"/>
    <!-- Obtenim el valor maxim de p:sldMasterId -->
    <xsl:param name="num-sldMasterId" select="count(//p:sldMasterId)"/>
    <!-- No se perque ho fan aixi de raro -->
    <xsl:param name="NUMERACIO_SLDMASTERID_1" select="2147483647"/>
    <!-- es 19 , pero quan s'aplica ja han passat 5 per aixo poso 13 -->
    <xsl:param name="NUMERACIO_SLDMASTERID_2" select="2147483814"/>
    <!-- fonts incrustades en el document -->
    <xsl:param name="fonts-incrustades" select="//p:embeddedFont"/>
    <!-- num de fonts que es referencies desde el pptx -->
    <xsl:param name="maxId" select="count(//p:embeddedFont/*[@r:id])"/>



    <xsl:output indent="no" omit-xml-declaration="no" standalone="yes" media-type="xml"
        encoding="UTF-8"/>

    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="p:sldIdLst">
        <xsl:copy>
            <xsl:copy-of select="@*"/>

            <xsl:if test="number($portada)=1">
                <!-- slide de portada -->
                <p:sldId id="9999" r:id="rId9999"/>
            </xsl:if>
            <xsl:apply-templates/>
            <xsl:choose>
                <xsl:when test="doc-available($pptx.merge)">
                    <xsl:apply-templates select="document($pptx.merge)//p:sldId"
                        mode="copia-document"/>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of
                        select="concat('[ERROR] (presentacion.xsl:p:sldIdLst) Fitxer ',$pptx.merge,' no trobat.')"
                    />
                </xsl:otherwise>
            </xsl:choose>

            <!-- Afegim els annexes -->
            <xsl:if test="number($annex)=1">
                <xsl:variable name="tree-processos-annexes" select="document($tasca.xml)//annexes"/>
                <xsl:for-each select="$tree-processos-annexes/annex">
                    <!-- Els annexes tenen una numeracio que comenca per 9000 -->
                    <xsl:variable name="identificador-slide" select="9000 + position()"/>
                    <p:sldId id="{$identificador-slide}" r:id="rId{$identificador-slide}"/>
                    <!--  Creem cada un dels fitxers als que fem referencia -->
                    <!-- fitxer sliden.xml -->
                    <xsl:result-document
                        href="{concat($pptx.doc_final,'/ppt/slides/slide',$identificador-slide,'.xml')}"
                        method="xml">
                        <xsl:call-template name="esquelet-annexe"/>
                    </xsl:result-document>
                    <!-- fitxer sliden.xml.rels -->
                    <xsl:result-document
                        href="{concat($pptx.doc_final,'/ppt/slides/_rels/slide',$identificador-slide,'.xml.rels')}"
                        method="xml">
                        <xsl:call-template name="rels-esquelet-annexe">
                            <xsl:with-param name="imatge" select="@href"/>
                        </xsl:call-template>
                    </xsl:result-document>
                </xsl:for-each>


            </xsl:if>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="p:defaultTextStyle">
        <xsl:call-template name="default-styles"/>
    </xsl:template>

    <xsl:template match="p:sldMasterIdLst">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:for-each select="p:sldMasterId">
                <xsl:copy>
                    <xsl:copy-of select="@* except @id"/>
                    <!-- Eliminem el id que nomes dona problemes
                    <xsl:attribute name="id">
                        <xsl:value-of select="$NUMERACIO_SLDMASTERID_1 + position()"/>
                    </xsl:attribute>
                    -->
                </xsl:copy>
            </xsl:for-each>
            <xsl:apply-templates select="document($pptx.merge)//p:sldMasterId" mode="copia-document"/>

        </xsl:copy>
    </xsl:template>

    <!-- 
    El Id:
    <xsd:simpleType name="ST_SlideId" oxsd:cname="SlideId">
    <xsd:restriction base="xsd:unsignedInt">
    <xsd:minInclusive value="256"/>
    <xsd:maxExclusive value="2147483648"/>
    </xsd:restriction>
    </xsd:simpleType>
    es defineix d'aquesta manera tant per sldId com per sldMasterId.
    -->
    <xsl:template match="p:sldId" mode="copia-document">
        <xsl:copy>
            <xsl:attribute name="id">
                <xsl:value-of select="$max-sldId + position()"/>
            </xsl:attribute>
            <!--xsl:copy-of select="@id"/-->
            <xsl:variable name="idv" select="number(substring-after(@r:id,'rId'))"/>
            <xsl:variable name="nou-identificador" select="$idv + $num-relacions"/>
            <xsl:attribute name="r:id">
                <xsl:value-of select="concat('rId',$nou-identificador)"/>
            </xsl:attribute>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="p:sldMasterId" mode="copia-document">

        <xsl:copy>
            <xsl:copy-of select="@* except @id"/>
            <xsl:variable name="idv" select="number(substring-after(@r:id,'rId'))"/>
            <xsl:variable name="nou-identificador" select="$idv + $num-relacions"/>
            <xsl:attribute name="r:id">
                <xsl:value-of select="concat('rId',$nou-identificador)"/>
            </xsl:attribute>
            <!--
            <xsl:attribute name="id">
                
                 Calcula quin id ha de sumar
                <xsl:variable name="valor-id"
                    select="$NUMERACIO_SLDMASTERID_1 + $num-sldMasterId + position()"/>
                <xsl:variable name="valor-id-slidemaster">
                    <xsl:choose>
                        <xsl:when test="$valor-id > 2147483651">
                            <xsl:value-of select="$NUMERACIO_SLDMASTERID_2"/>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:value-of select="$NUMERACIO_SLDMASTERID_1"/>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                
                <xsl:value-of
                    select="format-number($valor-id-slidemaster + $num-sldMasterId + position(),'#')"
                />
            </xsl:attribute>
            -->
        </xsl:copy>

    </xsl:template>

    <!-- Annexes -->
    <xsl:template name="rels-esquelet-annexe">
        <xsl:param name="imatge"/>
        <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
            <Relationship Id="rId1"
                Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/slideLayout"
                Target="../slideLayouts/slideLayout1.xml"/>
            <Relationship Id="rId2"
                Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image"
                Target="../media/{$imatge}"/>
        </Relationships>
    </xsl:template>

    <xsl:template name="esquelet-annexe">

        <p:sld xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
            xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
            xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main">
            <p:cSld>
                <p:spTree>
                    <p:nvGrpSpPr>
                        <p:cNvPr id="1" name=""/>
                        <p:cNvGrpSpPr/>
                        <p:nvPr/>
                    </p:nvGrpSpPr>
                    <p:grpSpPr>
                        <a:xfrm>
                            <a:off x="0" y="0"/>
                            <a:ext cx="0" cy="0"/>
                            <a:chOff x="0" y="0"/>
                            <a:chExt cx="0" cy="0"/>
                        </a:xfrm>
                    </p:grpSpPr>
                    <p:pic>
                        <p:nvPicPr>
                            <p:cNvPr id="2" name="1 Imagen" descr=""/>
                            <p:cNvPicPr>
                                <a:picLocks noChangeAspect="1"/>
                            </p:cNvPicPr>
                            <p:nvPr/>
                        </p:nvPicPr>
                        <p:blipFill>
                            <a:blip r:embed="rId2" cstate="print"/>
                            <a:stretch>
                                <a:fillRect/>
                            </a:stretch>
                        </p:blipFill>
                        <p:spPr>
                            <a:xfrm>
                                <a:off x="381000" y="0"/>
                                <a:ext cx="9144000" cy="6858000"/>
                            </a:xfrm>
                            <a:prstGeom prst="rect">
                                <a:avLst/>
                            </a:prstGeom>
                        </p:spPr>
                    </p:pic>
                </p:spTree>
            </p:cSld>
            <p:clrMapOvr>
                <a:masterClrMapping/>
            </p:clrMapOvr>
        </p:sld>
    </xsl:template>

    <!-- controlem les embeded fonts, nomes posarem les que siguin diferents -->
    <xsl:template match="p:embeddedFontLst">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
            <xsl:apply-templates select="document($pptx.merge)//p:embeddedFont" mode="copy-fonts"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="p:embeddedFont" mode="copy-fonts">
        <xsl:variable name="font-externa" select="p:font/@typeface"/>
        <xsl:variable name="font-family" select="p:font/@pitchFamily"/>
        <xsl:if
            test="not($fonts-incrustades[p:font/@typeface=$font-externa and p:font/@pitchFamily=$font-family])">
            <xsl:copy>
                <xsl:copy-of select="@*"/>
                <xsl:apply-templates mode="copy-fonts"/>
            </xsl:copy>
        </xsl:if>

    </xsl:template>
    <xsl:template match="p:regular | p:bold | p:italic | p:boldItalic" mode="copy-fonts">
        <xsl:variable name="relacio">
            <xsl:call-template name="calcular-valor-relacio"/>
        </xsl:variable>

        <xsl:variable name="identificador-relacio" select="@r:id"/>

        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:attribute name="r:id">
                <xsl:value-of select="$relacio"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
        <!-- quants fitxers de fonts hi ha 
        <xsl:variable name="relacions-fonts-1" select="document(concat($pptx.doc1,'ppt/_rels/presentation.xml.rels'))//relation:Relationship[@Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/font']"/>
        <xsl:variable name="num-max-relacions-font" select="max($relacions-fonts-1/substring-before(substring-after(@Target,'fonts/font'),'.fntdata'))"/>
        -->
        <!-- Una vegada hem creat la referencia hem de copiar el fitxer que conte aquestes fonts -->
        <!-- Primer mirem quina relacio li pertany a aquesta relacio dins el fitxer de relacions del doc 2
        <xsl:variable name="document-relacions-2" select="document(concat($pptx.doc2,'ppt/_rels/presentation.xml.rels'))"/>
        -->
        <!-- Agafem la relacio
        <xsl:variable name="nom-fitxer" select="$document-relacions-2//relation:Relationship[@Id=$identificador-relacio]/@Target"/>
        
        <xsl:variable name="font" select="substring-after($nom-fitxer,'fonts/font')"></xsl:variable>
        <xsl:variable name="numero-font" select="substring-before($font,'.fntdata')"></xsl:variable>
        -->
        <!--fitxer inicial="{@r:id}" max="{$num-max-relacions-font}" num="{count($document-relacions-2//relation:Relationship)}"><xsl:value-of select="$nom-fitxer"></xsl:value-of></fitxer-->
    </xsl:template>

    <xsl:template name="calcular-valor-relacio">
        <xsl:param name="relacio" select="@r:id"/>

        <xsl:variable name="idv" select="number(substring-after(@r:id,'rId'))"/>
        <xsl:variable name="nou-identificador" select="$idv + $num-relacions"/>
        <xsl:value-of select="concat('rId',$nou-identificador)"/>
    </xsl:template>

    <xsl:template match="*">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*" mode="copy-fonts">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates mode="copy-fonts"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template name="default-styles">
        <p:defaultTextStyle>
            <a:defPPr>
                <a:defRPr lang="de-DE"/>
            </a:defPPr>
            <a:lvl1pPr algn="l" rtl="0" fontAlgn="base">
                <a:spcBef>
                    <a:spcPct val="0"/>
                </a:spcBef>
                <a:spcAft>
                    <a:spcPct val="0"/>
                </a:spcAft>
                <a:defRPr kern="1200">
                    
                    <a:latin typeface="Imago" pitchFamily="2" charset="0"/>
                    <a:ea typeface="MS PGothic" pitchFamily="34" charset="-128"/>
                    <a:cs typeface="+mn-cs"/>
                </a:defRPr>
            </a:lvl1pPr>
            <a:lvl2pPr marL="457200" algn="l" rtl="0" fontAlgn="base">
                <a:spcBef>
                    <a:spcPct val="0"/>
                </a:spcBef>
                <a:spcAft>
                    <a:spcPct val="0"/>
                </a:spcAft>
                <a:defRPr kern="1200">
                    
                    <a:latin typeface="Imago" pitchFamily="2" charset="0"/>
                    <a:ea typeface="MS PGothic" pitchFamily="34" charset="-128"/>
                    <a:cs typeface="+mn-cs"/>
                </a:defRPr>
            </a:lvl2pPr>
            <a:lvl3pPr marL="914400" algn="l" rtl="0" fontAlgn="base">
                <a:spcBef>
                    <a:spcPct val="0"/>
                </a:spcBef>
                <a:spcAft>
                    <a:spcPct val="0"/>
                </a:spcAft>
                <a:defRPr kern="1200">
                    
                    <a:latin typeface="Imago" pitchFamily="2" charset="0"/>
                    <a:ea typeface="MS PGothic" pitchFamily="34" charset="-128"/>
                    <a:cs typeface="+mn-cs"/>
                </a:defRPr>
            </a:lvl3pPr>
            <a:lvl4pPr marL="1371600" algn="l" rtl="0" fontAlgn="base">
                <a:spcBef>
                    <a:spcPct val="0"/>
                </a:spcBef>
                <a:spcAft>
                    <a:spcPct val="0"/>
                </a:spcAft>
                <a:defRPr kern="1200">
                    
                    <a:latin typeface="Imago" pitchFamily="2" charset="0"/>
                    <a:ea typeface="MS PGothic" pitchFamily="34" charset="-128"/>
                    <a:cs typeface="+mn-cs"/>
                </a:defRPr>
            </a:lvl4pPr>
            <a:lvl5pPr marL="1828800" algn="l" rtl="0" fontAlgn="base">
                <a:spcBef>
                    <a:spcPct val="0"/>
                </a:spcBef>
                <a:spcAft>
                    <a:spcPct val="0"/>
                </a:spcAft>
                <a:defRPr kern="1200">
                    
                    <a:latin typeface="Imago" pitchFamily="2" charset="0"/>
                    <a:ea typeface="MS PGothic" pitchFamily="34" charset="-128"/>
                    <a:cs typeface="+mn-cs"/>
                </a:defRPr>
            </a:lvl5pPr>
            <a:lvl6pPr marL="2286000" algn="l" defTabSz="914400" rtl="0" eaLnBrk="1" latinLnBrk="0"
                hangingPunct="1">
                <a:defRPr kern="1200">
                    
                    <a:latin typeface="Imago" pitchFamily="2" charset="0"/>
                    <a:ea typeface="MS PGothic" pitchFamily="34" charset="-128"/>
                    <a:cs typeface="+mn-cs"/>
                </a:defRPr>
            </a:lvl6pPr>
            <a:lvl7pPr marL="2743200" algn="l" defTabSz="914400" rtl="0" eaLnBrk="1" latinLnBrk="0"
                hangingPunct="1">
                <a:defRPr kern="1200">
                    
                    <a:latin typeface="Imago" pitchFamily="2" charset="0"/>
                    <a:ea typeface="MS PGothic" pitchFamily="34" charset="-128"/>
                    <a:cs typeface="+mn-cs"/>
                </a:defRPr>
            </a:lvl7pPr>
            <a:lvl8pPr marL="3200400" algn="l" defTabSz="914400" rtl="0" eaLnBrk="1" latinLnBrk="0"
                hangingPunct="1">
                <a:defRPr kern="1200">
                    
                    <a:latin typeface="Imago" pitchFamily="2" charset="0"/>
                    <a:ea typeface="MS PGothic" pitchFamily="34" charset="-128"/>
                    <a:cs typeface="+mn-cs"/>
                </a:defRPr>
            </a:lvl8pPr>
            <a:lvl9pPr marL="3657600" algn="l" defTabSz="914400" rtl="0" eaLnBrk="1" latinLnBrk="0"
                hangingPunct="1">
                <a:defRPr kern="1200">
                    
                    <a:latin typeface="Imago" pitchFamily="2" charset="0"/>
                    <a:ea typeface="MS PGothic" pitchFamily="34" charset="-128"/>
                    <a:cs typeface="+mn-cs"/>
                </a:defRPr>
            </a:lvl9pPr>
            </p:defaultTextStyle>
    </xsl:template>


</xsl:stylesheet>
