<?xml version="1.0" encoding="UTF-8"?>
<!--
The MIT License (MIT)

Copyright (c) 2023 Mikhail Lipanin

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" id="mem-init" version="2.0">
  <!--
    Add `rust` attribute to `memory` object"
    -->
  <xsl:output indent="yes" method="xml"/>
  <xsl:strip-space elements="*"/>
  <xsl:template match="o[@base=&quot;memory&quot;]">
    <xsl:variable name="o" select="./o[1]/@base"/>
    <xsl:copy>
      <xsl:attribute name="rust">
        <xsl:choose>
          <xsl:when test="$o='int'">
            <xsl:text>uni.da("Φ.a.Δ").to_i64()?</xsl:text>
          </xsl:when>
          <xsl:when test="$o='bool'">
            <xsl:text>uni.da("Φ.f.Δ").to_bool()?</xsl:text>
          </xsl:when>
        </xsl:choose>
      </xsl:attribute>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
  <xsl:template match="node()|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
