<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"
    xmlns:relation="http://schemas.openxmlformats.org/package/2006/relationships"
    xmlns="http://schemas.openxmlformats.org/package/2006/relationships"
    xmlns:omaonk="http://omaonk.com/relationfile"
    xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"
    xmlns:p="http://schemas.openxmlformats.org/presentationml/2006/main"
    xmlns:ct="http://schemas.openxmlformats.org/package/2006/content-types"
    exclude-result-prefixes="#all" version="2.0">

    <!-- 
        per funcionar necessita els fitxers:
        relation.xml
        Indica com hem de renombrar els fitxer del doc 2 respecte el doc 1, ja que sino el doc 2 xafaria documents del doc1
        per exemple slides, notes,...
        file xmlns="http://schemas.openxmlformats.org/package/2006/content-types"
        dir_source="/Introduction/ppt/theme/theme2.xml" path_source="/Introduction/ppt/theme"
        file_source="theme2" dir_target="/eproject/ppt/theme/theme5.xml" file_target="theme5"
        path_target="/eproject/ppt/theme"/>
        En aquest cas el fitxer theme2 s'ha de renombrar com theme5.
        
        relationDrawing.xml
        Com no tenim un llistat de drawings del primer i segon document, em creat aquest document de referencia.
        Si el document 2 té drawing indica com s'han de renombrar respecte el document 1.
        <drawings>
        <drawing origen="vmlDrawing1.vml" rel_origen="1" desti="vmlDrawing7.vml" rel_desti="7"/>
        </drawings>
        
        
    -->
    <xsl:include href="./utils/general.xsl"/>

    <xsl:output indent="no" omit-xml-declaration="no" standalone="yes" media-type="xml"
        encoding="UTF-8"/>

    <xsl:param name="pptx.job"/>
    <xsl:param name="numdoc"/>
    <xsl:param name="path.tmp" select="'../tmp/'"/>
    <xsl:param name="FILE-RELATION" select="'relation.xml'"/>
    <xsl:param name="FILE-RELATION-DRAWING" select="'relationsDrawing.xml'"/>
    <xsl:param name="numfile" select="0"/>

    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <!-- tractament slidemaster -->
    <xsl:template match="p:sldMaster">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates mode="slidemaster"/>
        </xsl:copy>
    </xsl:template>
    <!--  <p:sldLayoutId id="2147483798" r:id="rId1"/> 
        2147483648
    -->
    <xsl:template match="p:sldLayoutId" mode="slidemaster">
        <xsl:copy>
            <xsl:copy-of select="@* except @id"/>
            <!-- 
                Eliminem l'atribut, ens crea conflictes amb altres id
          
            <xsl:attribute name="id">
                <xsl:value-of
                    select="format-number(2147483648 + (number($numfile)*1000) + position(),'#')"/>
            </xsl:attribute>
            -->
            <xsl:apply-templates mode="slidemaster"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="*" mode="slidemaster">
        <xsl:copy>
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates mode="slidemaster"/>
        </xsl:copy>
    </xsl:template>

    <!-- fi slidemaster -->

    <xsl:template match="relation:Relationships">
        <Relationships xmlns="http://schemas.openxmlformats.org/package/2006/relationships">
            <xsl:apply-templates/>
        </Relationships>
    </xsl:template>

    <!-- 
        <Relationship Id="rId1"
        Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/theme"
        Target="../theme/theme2.xml"/>
        
        <file xmlns="http://schemas.openxmlformats.org/package/2006/content-types"
        dir_source="/Introduction/ppt/theme/theme2.xml" path_source="/Introduction/ppt/theme"
        file_source="theme2" dir_target="/eproject/ppt/theme/theme5.xml" file_target="theme5"
        path_target="/eproject/ppt/theme"/>
    -->
    <xsl:template match="relation:Relationship">
        <xsl:variable name="document-relacions"
            select="document(concat($path.tmp,$pptx.job,'/',$FILE-RELATION))/omaonk:files"/>

        <xsl:variable name="fulla">
            <xsl:call-template name="strip-path">
                <xsl:with-param name="string-value" select="@Target"/>
            </xsl:call-template>
        </xsl:variable>

        <xsl:variable name="get-file-reference"
            select="$document-relacions//ct:file[contains(@dir_source,$fulla)]"/>

        <xsl:choose>
            <!-- es deixa igual, nomès pod haber-hi un notesmaster per cada document -->
            <xsl:when
                test="@Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/notesMaster'">
                <xsl:copy>
                    <xsl:copy-of select="@*"/>
                </xsl:copy>
            </xsl:when>
            <!-- tractament de hipervincles -->
            <xsl:when
                test="@Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/hyperlink'">
                <xsl:copy>
                    <xsl:copy-of select="@*"/>
                </xsl:copy>
            </xsl:when>
            <!-- tractament drawings 
            <Relationship Id="rId8"
                Type="http://schemas.microsoft.com/office/2007/relationships/diagramDrawing"
                Target="../diagrams/drawing1.xml"/>
            -->
            <xsl:when
                test="@Type='http://schemas.microsoft.com/office/2007/relationships/diagramDrawing'">
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id | @TargetMode"/>
                    <xsl:attribute name="Target">
                        <xsl:value-of
                            select="replace(@Target,'../diagrams/',concat('../diagrams/diagrams',$numdoc,'/'))"
                        />
                    </xsl:attribute>
                </xsl:copy>
            </xsl:when>
            <!-- 
                diagramColors
                <Relationship Id="rId7"
                Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramColors"
                Target="../diagrams/colors1.xml"/>
            -->
            <xsl:when
                test="@Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramColors'">
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id | @TargetMode"/>
                    <xsl:attribute name="Target">
                        <xsl:value-of
                            select="replace(@Target,'../diagrams/',concat('../diagrams/diagrams',$numdoc,'/'))"
                        />
                    </xsl:attribute>
                </xsl:copy>
            </xsl:when>
            <!-- 
                <Relationship Id="rId6"
                Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramQuickStyle"
                Target="../diagrams/quickStyle1.xml"/>
            -->
            <xsl:when
                test="@Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramQuickStyle'">
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id | @TargetMode"/>
                    <xsl:attribute name="Target">
                        <xsl:value-of
                            select="replace(@Target,'../diagrams/',concat('../diagrams/diagrams',$numdoc,'/'))"
                        />
                    </xsl:attribute>
                </xsl:copy>
            </xsl:when>
            <!--
            <Relationship Id="rId5"
                Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramLayout"
                Target="../diagrams/layout1.xml"/>
            -->
            <xsl:when
                test="@Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramLayout'">
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id | @TargetMode"/>
                    <xsl:attribute name="Target">
                        <xsl:value-of
                            select="replace(@Target,'../diagrams/',concat('../diagrams/diagrams',$numdoc,'/'))"
                        />
                    </xsl:attribute>
                </xsl:copy>
            </xsl:when>
            <!--
            <Relationship Id="rId4"
                Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramData"
                Target="../diagrams/data1.xml"/>
            -->
            <xsl:when
                test="@Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/diagramData'">
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id | @TargetMode"/>
                    <xsl:attribute name="Target">
                        <xsl:value-of
                            select="replace(@Target,'../diagrams/',concat('../diagrams/diagrams',$numdoc,'/'))"
                        />
                    </xsl:attribute>
                </xsl:copy>
            </xsl:when>
            <xsl:when
                test="@Type='http://schemas.microsoft.com/office/2007/relationships/hdphoto'">
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id | @TargetMode"/>
                    <xsl:attribute name="Target">
                        <xsl:value-of
                            select="replace(@Target,'../media/',concat('../media/media',$numdoc,'/'))"
                        />
                    </xsl:attribute>
                </xsl:copy>
            </xsl:when>
            <xsl:when
                test="@Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/image'">
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id | @TargetMode"/>
                    <xsl:attribute name="Target">
                        <xsl:value-of
                            select="replace(@Target,'../media/',concat('../media/media',$numdoc,'/'))"
                        />
                    </xsl:attribute>
                </xsl:copy>
            </xsl:when>
            <xsl:when
                test="@Type='http://schemas.microsoft.com/office/2007/relationships/media'">
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id | @TargetMode"/>
                    <xsl:attribute name="Target">
                        <xsl:value-of
                            select="replace(@Target,'../media/',concat('../media/media',$numdoc,'/'))"
                        />
                    </xsl:attribute>
                </xsl:copy>
            </xsl:when>
            <xsl:when
                test="@Type='http://schemas.openxmlformats.org/officeDocument/2006/relationships/video'">
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id | @TargetMode"/>
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
                    <xsl:copy-of select="@Type | @Id | @TargetMode"/>
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
                    <xsl:copy-of select="@Type | @Id | @TargetMode"/>
                    <xsl:attribute name="Target">
                        <!--    
                            Relacio:
                            <Relationship Id="rId1"
                            Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/vmlDrawing"
                            Target="../drawings/vmlDrawing7.vml"/>
                            
                            Relacio del fitxer Drawing:
                            <drawings>
                            <drawing origen="vmlDrawing1.vml" rel_origen="1" desti="vmlDrawing7.vml" rel_desti="7"/>
                            </drawings>
                            
                        -->
                        <xsl:variable name="drawing-target" select="@Target"/>
                        <!-- Busca la relacio en el fitxer extern per canviar-li el nom -->
                        <xsl:variable name="vmlDrawing-relation"
                            select="document(concat($path.tmp,$pptx.job,'/',$FILE-RELATION-DRAWING))//drawing[contains($drawing-target,@origen)]"/>
                        <xsl:value-of
                            select="replace(@Target,$vmlDrawing-relation/@origen,$vmlDrawing-relation/@desti)"
                        />
                    </xsl:attribute>
                </xsl:copy>
            </xsl:when>
            <xsl:otherwise>
                <xsl:copy>
                    <xsl:copy-of select="@Type | @Id | @TargetMode"/>
                    <xsl:attribute name="Target">
                        <xsl:choose>
                            <xsl:when test="$get-file-reference">
                                <xsl:value-of
                                    select="replace(@Target,$get-file-reference/@file_source,$get-file-reference/@file_target)"
                                />
                            </xsl:when>
                            <xsl:otherwise>
                                <!-- No sabem que es -->
                                <xsl:comment>ERROR </xsl:comment>
                                
                            </xsl:otherwise>
                        </xsl:choose>

                    </xsl:attribute>

                </xsl:copy>
                <!--
                    <xsl:comment>
                    fs <xsl:value-of select="$get-file-reference/@file_source"></xsl:value-of>,
                    ft <xsl:value-of select="$get-file-reference/@file_target"></xsl:value-of>,
                    dr <xsl:value-of select="@dir_source"></xsl:value-of>,
                    f <xsl:value-of select="$fulla"></xsl:value-of>,
                    </xsl:comment>
                -->
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>


    <xsl:template match="*"/>


</xsl:stylesheet>
