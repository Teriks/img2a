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

import java.util.Iterator;

/**
 * Represents an iterable row/scanline of {@link Pixel}
 * objects produced by an {@link ImageAsciiReader}
 */
public class ImageRow implements Iterable<Pixel> {
    private final ImageAsciiReader m_reader;
    private final int m_row;


    ImageRow(ImageAsciiReader imageReader, int row) {
        this.m_reader = imageReader;
        this.m_row = row;
    }

    /**
     * Get the {@link ImageAsciiReader} that produced this object.
     *
     * @return {@link ImageAsciiReader}
     */
    final ImageAsciiReader getReader() {
        return this.m_reader;
    }

    /**
     * Get the 0 based index of the current row / image scanline.
     *
     * @return Row index.
     */
    final int getRowIndex() {
        return this.m_row;
    }

    /**
     * Retrieve an iterator over the {@link Pixel} objects in this row.
     *
     * @return Iterator over scanline pixels.
     */
    public final Iterator<Pixel> iterator() {
        return new ImageRowIterator(this);
    }
}
