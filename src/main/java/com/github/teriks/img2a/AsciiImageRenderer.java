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
import java.awt.image.BufferedImage;

/**
 * For rendering an image read by a {@link ImageAsciiReader}
 * as ASCII art directly into another image.
 */
public class AsciiImageRenderer {
    private final ImageAsciiReader m_reader;
    private boolean m_useColors = false;
    private boolean m_colorFill = false;
    private Color m_background = Color.black;
    private Color m_foreground = Color.white;
    private Font m_font = new Font("Monospaced", Font.BOLD, 12);

    public AsciiImageRenderer(ImageAsciiReader reader) {
        m_reader = reader;
    }

    /**
     * See: {@link #setFont(Font)}
     * <br>
     * Return the font object that will be used when drawing text in the image.
     *
     * @return {@link Font}.
     */
    public final Font getFont() {
        return m_font;
    }

    /**
     * Set the font object that will be used when drawing text in the image.
     * <br>
     * It is recommended to use a Monospaced font for best result.
     * <br>
     * The default font is: "Monospaced", Bold, 12pt
     *
     * @param font {@link Font}
     */
    public final void setFont(Font font) {
        m_font = font;
    }

    /**
     * See: {@link #setBackground(Color)}
     *
     * @return {@link Color}
     */
    public final Color getBackground() {
        return m_background;
    }

    /**
     * Set the default background color for the image.
     * <br>
     * If {@link #setUseColors(boolean)} and {@link #setColorFill(boolean)} are set to True,
     * the default background will be completely invisible.
     *
     * @param color {@link Color}
     */
    public final void setBackground(Color color) {
        m_background = color;
    }

    /**
     * See: {@link #setForeground(Color)}
     *
     * @return {@link Color}
     */
    public final Color getForeground() {
        return m_foreground;
    }

    /**
     * Set the default foreground color for text in the image.
     *
     * @param color {@link Color}
     */
    public final void setForeground(Color color) {
        m_foreground = color;
    }

    /**
     * Get the {@link ImageAsciiReader}
     *
     * @return {@link ImageAsciiReader}
     */
    public final ImageAsciiReader getReader() {
        return m_reader;
    }

    /**
     * See: {@link #setUseColors(boolean)}
     *
     * @return {@link Boolean}
     */
    public final boolean getUseColors() {
        return m_useColors;
    }

    /**
     * Set whether or not to colorize the ASCII characters in the resulting
     * image according to the original image.
     *
     * @param value True or False
     */
    public final void setUseColors(boolean value) {
        m_useColors = value;
    }

    /**
     * See: {@link #setColorFill(boolean)}
     *
     * @return {@link Boolean}
     */
    public final boolean getColorFill() {
        return m_colorFill;
    }

    /**
     * Set whether or not to colorize the background of the ASCII characters
     * in the resulting image according to the original image.
     *
     * @param value True or False
     */
    public final void setColorFill(boolean value) {
        m_colorFill = value;
    }

    /**
     * Render to and return a {@link BufferedImage} containing
     * the resulting ASCII output.
     *
     * @param size Desired size of the output image in pixels.
     * @return {@link BufferedImage}
     */
    public BufferedImage render(Point size) {
        return render(size.x, size.y);
    }

    /**
     * Render to and return a {@link BufferedImage} containing
     * the resulting ASCII output.
     *
     * @param width  Desired width of the output image in pixels.
     * @param height Desired height of the output image in pixels.
     * @return {@link BufferedImage}
     */
    public BufferedImage render(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        final Font font = m_font;

        Graphics2D graphics = (Graphics2D) img.getGraphics();

        setRenderingHints(graphics);

        FontMetrics metrics = graphics.getFontMetrics(font);

        int charHeight = metrics.getHeight();
        int charWidth = 0;

        for (char c : getReader().getPalette().toCharArray()) {
            int w = metrics.charWidth(c);
            if (w > charWidth) {
                charWidth = w;
            }
        }

        graphics.setColor(m_background);

        graphics.fillRect(0, 0, width, height);

        graphics.setFont(font);

        // round up to ensure the image is filled perfectly in the best case
        // and overfilled in the worse case.  This prevents gaps at the edges
        // when setColorFill() is set to True.

        int cols = (int) Math.ceil(((float) width / (float) charWidth));
        int rows = (int) Math.ceil((float) height / (float) charHeight);

        // mitigate overflow typesetting ugliness by shifting things
        // up or left by half the overflow amount.
        int offW = Math.round((width - (cols * charWidth)) * 0.5f);
        int offH = Math.round((height - (rows * charHeight)) * 0.5f);

        // starting X coord characters
        int charAdvance = offW;

        // starting baseline Y coord for characters
        int charDescent = charHeight + offH;

        // starting top left Y coord for background boxes.
        int backgroundDescent = 0;

        // characters render with the origin at the lower left corner (effectively)
        // while the background boxes render with the origin at the top left.
        //
        // charDescent needs to start at one character height down because of this.
        // to get the top left Y coord for background boxes you need: charDescent - charHeight.

        // default foreground color if characters are not colored.

        Color fg_color = m_foreground;

        graphics.setColor(fg_color);

        char[] charData = new char[1];

        for (ImageRow row : m_reader.read(cols, rows)) {
            for (Pixel pix : row) {

                if (m_useColors) {
                    fg_color = pix.getColor();

                    if (m_colorFill) {

                        // Fill rect with pixel color, dim / modify the foreground a bit
                        // so that it stands out.

                        graphics.setColor(fg_color);

                        // charDescent - charHeight to get the top left Y position for the box,
                        // because the characters origin is actually the lower left hand corner
                        // when rendering

                        graphics.fillRect(charAdvance, backgroundDescent, charWidth, charHeight);

                        fg_color = calcColorFillForeground(pix);
                    }

                    graphics.setColor(fg_color);
                }

                charData[0] = pix.getChar();


                graphics.drawChars(charData, 0, 1, charAdvance, charDescent);

                charAdvance += charWidth;
            }

            charDescent += charHeight;
            backgroundDescent += charHeight;

            charAdvance = 0;
        }

        return img;
    }

    private Color calcColorFillForeground(Pixel pix) {
        float fg_factor =
                getReader().getUseGrayscaleColor() ? 0.5f : pix.getLuma();

        Color fg_color = pix.getColor();

        int fg_R = Math.round(fg_color.getRed() * fg_factor);
        int fg_G = Math.round(fg_color.getGreen() * fg_factor);
        int fg_B = Math.round(fg_color.getBlue() * fg_factor);

        return new Color(fg_R, fg_G, fg_B);
    }

    private void setRenderingHints(Graphics2D graphics) {
        graphics.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}
