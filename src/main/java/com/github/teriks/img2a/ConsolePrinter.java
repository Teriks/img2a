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

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static org.fusesource.jansi.Ansi.Color;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * For printing an image read by a {@link ImageAsciiReader} to
 * a terminal, with optional colorized output and terminal auto fit.
 */
public class ConsolePrinter extends AsciiPrinter {

    static {
        AnsiConsole.systemInstall();
    }

    private boolean m_useColors = false;
    private boolean m_colorFill = false;

    /**
     * Construct a ConsolePrinter around a given {@link ImageAsciiReader}
     *
     * @param reader The image reader.
     */
    public ConsolePrinter(ImageAsciiReader reader) {
        super(reader);
    }

    /**
     * See: {@link #setColorFill(boolean)}
     *
     * @param value True or False
     * @return {@link Boolean}
     */
    public final boolean getColorFill(boolean value) {
        return m_colorFill;
    }


    /**
     * Calculate the optimal size for img2a ASCII output
     * inside the current terminal if one exists.
     * <br>
     * If terminal size cannot be determined, the size defaults to 80x40.
     *
     * @return Point(rows, cols)
     */
    @Override
    public Point getDefaultSize() {

        Terminal term;

        try {
            term = TerminalBuilder.terminal();
        } catch (IOException err) {
            return super.getDefaultSize();
        }

        float screen_cols = term.getWidth();

        if (screen_cols == 0) {
            return super.getDefaultSize();
        }

        // minus 1 for the prompt line, which takes up a row already
        float screen_rows = (float) term.getHeight() - 1;

        float img_x = this.getReader().getImageWidth();
        float img_y = this.getReader().getImageHeight() * this.getDefaultHeightScale();

        float screen_aspect = screen_cols / screen_rows;
        float img_aspect = img_x / img_y;

        float scaleFactor;
        if (screen_aspect > img_aspect) {
            scaleFactor = screen_rows / img_y;
        } else {
            scaleFactor = screen_cols / img_x;
        }

        return new Point(
                Math.round(img_x * scaleFactor),
                Math.round(img_y * scaleFactor));
    }

    /**
     * See: {@link #setUseColors(boolean)}
     *
     * @return {@link Boolean}
     */
    public final boolean getUseColors() {
        return this.m_useColors;
    }

    /**
     * Set whether or not to colorize the ASCII characters in the console output
     * using platform specific escape sequences.
     *
     * @param value True or False
     */
    public final void setUseColors(boolean value) {
        this.m_useColors = value;
    }

    /**
     * Set whether or not to colorize the ASCII character backgrounds in the console output
     * using platform specific escape sequences.
     *
     * @param value True or False
     */
    public final void setColorFill(boolean value) {
        m_colorFill = value;
    }

    private void writeColoredPixel(OutputStreamWriter writer, Pixel pixel) throws IOException {
        float t = 25.5f;
        float i = 255 - t;

        // color components are 1 - 255

        int r = pixel.getColor().getRed();
        int g = pixel.getColor().getGreen();
        int b = pixel.getColor().getBlue();
        float y = pixel.getLuma();

        Color color = Color.DEFAULT;

        boolean bold = y >= 0.95f && r < 1 && g < 1 && b < 1;


        if (!getReader().getUseGrayscaleColor()) {
            if (r - t > g && r - t > b)
                color = Color.RED;
            else if (g - t > r && g - t > b)
                color = Color.GREEN;
            else if (r - t > b && g - t > b && r + g > i)
                color = Color.YELLOW;
            else if (b - t > r && b - t > g && y < 0.95f)
                color = Color.BLUE;
            else if (r - t > g && b - t > g && r + b > i)
                color = Color.MAGENTA;
            else if (g - t > r && b - t > r && b + g > i)
                color = Color.CYAN;
            else if (r + g + b >= (3.0f * y * 255))
                color = Color.WHITE;
        } else if (y > 0.7f) {
            color = Color.WHITE;
            bold = true;
        }

        Ansi output;

        if (color == Color.DEFAULT) {
            output = bold ? ansi().bold() : ansi();
        } else if (this.m_colorFill) {
            output = ansi().bg(color);
        } else {
            output = ansi().fg(color);
        }


        writer.write(output.a(pixel.getChar()).reset().toString());
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
