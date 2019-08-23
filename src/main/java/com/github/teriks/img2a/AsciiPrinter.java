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
 * For printing an image read by a {@link ImageAsciiReader}
 * to stdout or an arbitrary stream with minimal formatting.
 * <br>
 * For optional ANSI color support and console auto fitting use {@link ConsolePrinter}.
 */
public class AsciiPrinter {
    private final ImageAsciiReader m_reader;
    private float m_defaultHeightScale = 0.5f;


    /**
     * Construct an AsciiPrinter around a given {@link ImageAsciiReader}
     *
     * @param reader The image reader.
     */
    public AsciiPrinter(ImageAsciiReader reader) {
        this.m_reader = reader;
    }

    /**
     * See: {@link #setDefaultHeightScale(float)}
     * <br>
     * Defaults to 0.5f, assumes characters are half as tall as they are wide.
     *
     * @return Default height scale.
     */
    public final float getDefaultHeightScale() {
        return m_defaultHeightScale;
    }

    /**
     * Set the default height scale when output height needs to
     * be calculated automatically.
     * <br>
     * This scales the final output height by a factor to compensate
     * for characters which may be wider than they are tall.
     *
     * @param value Scaling factor for output height.
     */
    public final void setDefaultHeightScale(float value) {
        m_defaultHeightScale = value;
    }

    /**
     * Get the default output size when no size is specified.
     * <br>
     * The value is created by calculating the aspect correct dimensions
     * which will fit the source image as text into a 80x80 block.
     * <br>
     * The final height will be scaled by {@link #getDefaultHeightScale()}
     *
     * @return Default output size.
     */

    public Point getDefaultSize() {
        return this.getReader().calcAspectCorrectSize(new Point(80, 80),
                this.getDefaultHeightScale());
    }

    /**
     * Get the {@link ImageAsciiReader}
     *
     * @return {@link ImageAsciiReader}
     */
    public final ImageAsciiReader getReader() {
        return this.m_reader;
    }

    /**
     * Write a line break sequence to a stream writer.
     *
     * @param writer Stream Writer.
     * @throws IOException Upon writer.write IOException
     */
    public void lineBreak(OutputStreamWriter writer) throws IOException {
        writer.write('\n');
    }

    /**
     * Write img2a output to stdout with the given dimensions.
     * <br>
     * The output dimensions are retrieved from {@link #getDefaultSize()}.
     *
     * @throws IOException Upon writer.write IOException
     */
    public final void print() throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        this.print(this.getDefaultSize(), writer);
        writer.flush();
    }

    /**
     * Write img2a output to stdout with the given dimensions.
     *
     * @param cols ASCII column count.
     * @param rows ASCII row count.
     * @throws IOException Upon writer.write IOException
     */
    public final void print(int cols, int rows) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        this.print(cols, rows, writer);
        writer.flush();
    }

    /**
     * Write img2a output to a stream writer with the given dimensions.
     *
     * @param cols   ASCII column count.
     * @param rows   ASCII row count.
     * @param writer {@link OutputStreamWriter}
     * @throws IOException Upon writer.write IOException
     */
    public void print(int cols, int rows, OutputStreamWriter writer) throws IOException {
        for (ImageRow row : this.m_reader.read(cols, rows)) {
            for (Pixel pix : row) {
                this.writePixel(writer, pix);
            }
            this.lineBreak(writer);
        }
    }

    /**
     * Write img2a output to a stream writer with the given dimensions.
     * <br>
     * The output dimensions are retrieved from {@link #getDefaultSize() getDefaultSize}.
     *
     * @param writer {@link OutputStreamWriter}
     * @throws IOException Upon writer.write IOException
     */
    public void print(OutputStreamWriter writer) throws IOException {
        this.print(this.getDefaultSize(), writer);
    }

    /**
     * Write img2a output to stdout with the given dimensions.
     * <br>
     * If **size** is **null**, the size from {@link #getDefaultSize()} is used.
     *
     * @param size Desired dimensions, Point(rows, cols).
     * @throws IOException Upon writer.write IOException
     */
    public final void print(Point size) throws IOException {
        if (size == null) {
            this.print();
            return;
        }
        this.print(size.x, size.y);
    }

    /**
     * Write img2a output to a stream writer with the given dimensions.
     * <br>
     * If **size** is **null**, the size from {@link #getDefaultSize()} is used.
     *
     * @param size   Desired dimensions, Point(rows, cols).
     * @param writer Stream Writer.
     * @throws IOException Upon writer.write IOException
     */
    public final void print(Point size, OutputStreamWriter writer) throws IOException {
        if (size == null) {
            this.print(writer);
            return;
        }

        this.print(size.x, size.y, writer);
    }

    /**
     * Write the character representation of a pixel to a stream writer.
     *
     * @param writer Stream Writer.
     * @param pixel  img2a Pixel.
     * @throws IOException Upon writer.write IOException
     */
    public void writePixel(OutputStreamWriter writer, Pixel pixel) throws IOException {
        writer.write(pixel.getChar());
    }
}
