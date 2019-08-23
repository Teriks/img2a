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

/**
 * Represents a pixel as read by an {@link ImageAsciiReader}.
 * <br>
 * The {@link ImageAsciiReader} instance calculates the luma value for each pixel
 * based on it's color and supplied settings, and selects an appropriate character
 * to represent it.
 */
public class Pixel {
    private final char m_char;
    private final Color m_color;
    private final Point m_coord;
    private final float m_luma;

    /**
     * @param coord     Pixel Coordinate
     * @param color     Pixel Color
     * @param luma      Pixel Luma, value between 0.0f and 1.0f.
     * @param character Pixel's character representation.
     */
    Pixel(Point coord, Color color, float luma, char character) {
        this.m_coord = coord;
        this.m_color = color;
        this.m_luma = luma;
        this.m_char = character;
    }


    /**
     * Get the ASCII character selected for the pixel.
     * <br>
     * The character palette can be set with:
     * <br>
     * {@link ImageAsciiReader#setPalette(String)}
     *
     * @return Pixel Character
     */
    public final char getChar() {
        return this.m_char;
    }

    /**
     * Get the original pixel color.
     *
     * @return Pixel Color.
     */
    public final Color getColor() {
        return this.m_color;
    }

    /**
     * Get the original pixel coordinate.
     *
     * @return Pixel Coordinate.
     */
    public final Point getCoord() {
        return this.m_coord;
    }

    /**
     * Get the calculated luma value for the pixel.
     * <br>
     * The value will be between 0.0f and 1.0f.
     *
     * @return Luma
     */
    public final float getLuma() {
        return this.m_luma;
    }
}
