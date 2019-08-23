/*
*
* Copyright 2017 Teriks
*
* Redistribution and use in source and binary forms, with or without modification, are permitted
* provided that the following conditions are met:
*
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions
* and the following disclaimer.
*
* 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
* and the following disclaimer in the documentation and/or other materials provided with the
* distribution.
*
* 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse
* or promote products derived from this software without specific prior written permission.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
* IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
* FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
* CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
* DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
* IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
* THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.github.teriks.img2a;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * For printing an image read by a {@link ImageAsciiReader} to
 * an output stream as HTML5.
 */
public class HtmlPrinter extends AsciiPrinter {
    private boolean m_colorFill = false;
    private String m_fontSize = "8pt";
    private String m_fontWeight = "bold";
    private boolean m_raw = false;
    private String m_title = null;
    private boolean m_useColors = false;

    private String m_background = "black";
    private String m_foreground = "white";
    private String m_fontStyle = "normal";

    /**
     * Construct an HtmlPrinter around a given {@link ImageAsciiReader}
     *
     * @param reader The image reader.
     */
    public HtmlPrinter(ImageAsciiReader reader) {
        super(reader);
    }

    /**
     * See: {@link #setBackground(String)}
     *
     * @return CSS html background color.
     */
    public final String getBackground() {
        return m_background;
    }

    /**
     * Set the CSS color which will be used as the background color
     * in the HTML output.
     *
     * @param background CSS color.
     */
    public final void setBackground(String background) {
        m_background = background;
    }

    /**
     * See: {@link #setForeground(String)}
     *
     * @return CSS html background color.
     */
    public final String getForeground() {
        return m_foreground;
    }

    /**
     * Set the CSS color which will be used as the default foreground
     * color in the HTML output.
     *
     * @param foreground CSS color.
     */
    public final void setForeground(String foreground) {
        m_foreground = foreground;
    }

    /**
     * See: {@link #setColorFill(boolean)}
     *
     * @return Whether or not to fill colors.
     */
    public final boolean getColorFill() {
        return m_colorFill;
    }

    /**
     * Set whether or not the background color of each character will be
     * filled in with the actual color of the pixel.
     * <br>
     * Each character will be dimmed so that it is still visible over it's
     * backdrop when this is set to True.
     *
     * @param value True or False
     */
    public final void setColorFill(boolean value) {
        m_colorFill = value;
    }

    /**
     * Calculate the default size for img2a HTML ASCII output.
     * <br>
     * The value is created by calculating the aspect correct dimensions
     * which will fit the source image as text into a 128x128 block.
     * <br>
     * The final height will be scaled by {@link #getDefaultHeightScale()}
     *
     * @return Point(rows, cols)
     */
    @Override
    public Point getDefaultSize() {
        return this.getReader().calcAspectCorrectSize(new Point(128, 128),
                this.getDefaultHeightScale());
    }

    /**
     * See: {@link #setFontSize(String)}
     *
     * @return Get CSS font size for ASCII characters as specified with {@link #setFontSize(String)}
     */
    public final String getFontSize() {
        return this.m_fontSize;
    }

    /**
     * Set the CSS font size which will be applied to all ASCII characters.
     * <br>
     * This should be a CSS specifier such as: "8pt", "1em", "10px" etc..
     *
     * @param value CSS font size.
     */
    public final void setFontSize(String value) {
        this.m_fontSize = value;
    }

    /**
     * See {@link #setFontWeight(String)}
     *
     * @return Get CSS font weight for ASCII characters as specified with {@link #setFontWeight(String)}
     */
    public final String getFontWeight() {
        return this.m_fontWeight;
    }

    /**
     * Set the CSS font weight which will be applied to all ASCII characters.
     * <br>
     * This should be a CSS specifier such as: "normal", "bold", "bolder", "900" etc..
     *
     * @param value CSS font weight.
     */
    public final void setFontWeight(String value) {
        this.m_fontWeight = value;
    }


    /**
     * See {@link #setFontStyle(String)}
     *
     * @return Get CSS font style for ASCII characters as specified with {@link #setFontStyle(String)}
     */
    public final String getFontStyle() {
        return this.m_fontStyle;
    }

    /**
     * Set the CSS font style which will be applied to all ASCII characters.
     * <br>
     * This should be a CSS specifier such as: "normal", "italic", "oblique".
     *
     * @param value CSS font style.
     */
    public final void setFontStyle(String value) {
        this.m_fontStyle = value;
    }

    /**
     * Return the CSS properties associated with a specific pixel as a string.
     *
     * @param pixel The Pixel.
     * @return A string of CSS properties with values.
     */
    public String getPixelStyle(Pixel pixel) {
        Color color = pixel.getColor();

        if (this.m_colorFill) {

            float fg_Factor =
                    getReader().getUseGrayscaleColor() ? 0.5f : pixel.getLuma();

            int fg_R = Math.round(color.getRed() * fg_Factor);
            int fg_G = Math.round(color.getGreen() * fg_Factor);
            int fg_B = Math.round(color.getBlue() * fg_Factor);


            return String.format(
                    "color:#%02x%02x%02x; background-color:#%02x%02x%02x",
                    fg_R,
                    fg_G,
                    fg_B,
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue());
        } else {

            return String.format(
                    "color:#%02x%02x%02x",
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue());
        }
    }

    /**
     * See: {@link #setRaw(boolean)}
     *
     * @return Whether or not raw HTML output is turned on.
     */
    public final boolean getRaw() {
        return this.m_raw;
    }

    /**
     * Set whether or not only the ASCII portion of the HTML should be output.
     * <br>
     * If colors are enabled setRaw(True) will output all of the characters with their
     * colored span wrappers.
     * <br>
     * If colors are not enabled, setRaw(True) will just output raw text with &lt;br&gt;
     * line breaks.
     * <br>
     * In either case, the rest of the document is discarded and you are responsible
     * for styling everything else besides the foreground/background color of each
     * character.
     *
     * @param value True or False
     */
    public final void setRaw(boolean value) {
        this.m_raw = value;
    }

    /**
     * See: {@link #setTitle(String)}
     *
     * @return The title currently set for the html document, which may be null.
     */
    public final String getTitle() {
        return this.m_title;
    }

    /**
     * Set the title to use for the produced HTML document.
     * <br>
     * You may set this to null if you do not want a title element at all.
     *
     * @param value The document title.
     */
    public final void setTitle(String value) {
        this.m_title = value;
    }

    /**
     * See: {@link #setUseColors(boolean)}
     *
     * @return Whether or not colors are currently specified to render.
     */
    public final boolean getUseColors() {
        return this.m_useColors;
    }

    /**
     * Set whether or not to colorize the ASCII characters in the HTML
     * output with element local CSS.
     * <br>
     * Characters are wrapped in span elements so that they can be colored
     * individually using the style attribute of each span.
     *
     * @param value True or False
     */
    public final void setUseColors(boolean value) {
        this.m_useColors = value;
    }

    @Override
    public void lineBreak(OutputStreamWriter writer) throws IOException {
        writer.write("<br>");
    }

    @Override
    public void print(int cols, int rows, OutputStreamWriter writer) throws IOException {

        if (this.m_raw) {
            super.print(cols, rows, writer);
            return;
        }

        writer.write("<!DOCTYPE html>");
        writer.write("<head>");

        if (this.m_title != null) {
            writer.write(String.format("<title>%s</title>", this.m_title));
        }

        writer.write("<style>");

        writer.write(String.format(
                "body{background:%s}", getBackground()));

        writer.write(String.format(
                ".ascii{" +
                        "font-weight:%s;" +
                        "font-style:%s;" +
                        "font-size:%s;" +
                        "font-family:monospace;" +
                        "color:%s" +
                        "}</style>",
                this.m_fontWeight,
                this.m_fontStyle,
                this.m_fontSize, getForeground()));

        writer.write("</style>");
        writer.write("<body><pre class=\"ascii\">");

        super.print(cols, rows, writer);

        writer.write("</pre></body>");
        writer.write("</html>");
    }

    public void writeColoredPixel(OutputStreamWriter writer, Pixel pixel) throws IOException {
        writer.write(
                String.format(
                        "<span style=\"%s\">%s</span>",
                        this.getPixelStyle(pixel),
                        pixel.getChar())
        );
    }

    @Override
    public void writePixel(OutputStreamWriter writer, Pixel pixel) throws IOException {
        if (this.m_useColors) {
            this.writeColoredPixel(writer, pixel);
        } else {
            super.writePixel(writer, pixel);
        }
    }
}
