<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html"  doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"/>

	<xsl:template match="frame">
		<html>
            <head>
                <title>GenerateCode</title>
                <meta content="text/html" charset="UTF-8"/>
                <style>
                .global{
					border: solid black 1px;
				}
				.cadre
				{
					height: 500px;
					width: 300px;
					float: left;
					border: solid black 1px;
				}
				.centre
				{

					margin-left: 100px;
				}
        		</style>
            </head>
            <body>
                <xsl:apply-templates/>
            </body>
        </html>
	</xsl:template>
		<!-- Régle 1 -->
	<xsl:template match="Pane">
        	<div class="global" >
	        	<xsl:if test="@id">
		        	<xsl:attribute name="id">
		        		<xsl:value-of select="@id"/>
		        	</xsl:attribute>
	        	</xsl:if>
        		<xsl:apply-templates/>
        	</div>
    </xsl:template>

    <xsl:template match="frame/Pane">
        	<div class="cadre" >
	        	<xsl:if test="@id">
	        		<xsl:attribute name="id">
	        			<xsl:value-of select="@id"/>
	        		</xsl:attribute>
	        	</xsl:if>
        		<xsl:apply-templates/>
        	</div>
    </xsl:template>

		<!-- Régle 2 -->
		<xsl:template match="button">
			<button class="centre">
				<xsl:attribute name="id">
					<xsl:value-of select="@id"/>
				</xsl:attribute>
				<xsl:attribute name="id"><xsl:value-of select="@id"/></xsl:attribute>
				<xsl:attribute name="height"><xsl:value-of select="@height"/></xsl:attribute>
				<xsl:attribute name="width"><xsl:value-of select="@width"/></xsl:attribute>
				<xsl:attribute name="editable"><xsl:value-of select="@editable"/></xsl:attribute>
				<xsl:attribute name="visible"><xsl:value-of select="@idvisible"/></xsl:attribute>
				<xsl:attribute name="color"><xsl:value-of select="@color"/></xsl:attribute>
				<xsl:attribute name="bg"><xsl:value-of select="@bg"/></xsl:attribute>
				<xsl:value-of select="text()"/>
			</button>
	    </xsl:template>

    	<xsl:template match="CheckBox">
        	<input class="centre" type="checkbox">
	        	<xsl:attribute name="id">
	        		<xsl:value-of select="@id"/>
	        	</xsl:attribute>
	        	<xsl:attribute name="value"></xsl:attribute>
        	</input>
    	</xsl:template>


    	<xsl:template match="label">
    		<label class="centre">
	    		<xsl:attribute name="id">
	    			<xsl:value-of select="@id"/>
	    		</xsl:attribute>
	    		<xsl:value-of select="@texte"/>
    		</label>
    	</xsl:template>

   	 	<xsl:template match="TextField">
   	 		<input class="centre" type="text" >
	   	 		<xsl:attribute name="id">
	   	 			<xsl:value-of select="@id"/>
	   	 		</xsl:attribute>
	   	 		<xsl:value-of select="text()"/>
   	 		</input>
    	</xsl:template>

</xsl:stylesheet>