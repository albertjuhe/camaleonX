<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	 
	
	<!--
	We get the caracters before the last /.
	Example: a/b/c/dddd 
	@return dddd
    -->
  <xsl:template name="strip-path">
	<xsl:param name="string-value"/>
	
	<xsl:choose>
		<xsl:when test="normalize-space($string-value)">
			<xsl:analyze-string select="$string-value" regex="[/]">
				<xsl:non-matching-substring>
					<xsl:if test="position()=last()">
						<xsl:value-of select="."/>
					</xsl:if>
				</xsl:non-matching-substring>
				
			</xsl:analyze-string>			
		</xsl:when>
		<xsl:otherwise>
			<xsl:value-of select="$string-value"/>
		</xsl:otherwise>
	</xsl:choose>

  </xsl:template>
  

	
</xsl:stylesheet>