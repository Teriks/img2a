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
 * An iterator over {@link Pixel} objects in an image scanline.
 */
class ImageRowIterator implements Iterator<Pixel> {
    private final int m_colCount;
    private final ImageAsciiReader m_reader;
    private final int m_row;
    private int m_colIndex;

    ImageRowIterator(ImageRow row) {
        this.m_reader = row.getReader();
        this.m_colIndex = 0;
        this.m_row = row.getRowIndex();
        this.m_colCount = this.m_reader.getWorkImageWidth();
    }

    public boolean hasNext() {
        return this.m_colIndex < this.m_colCount;
    }

    public Pixel next() {
        return this.m_reader.getPixel(this.m_colIndex++, this.m_row);
    }

    public void remove() {
    }
}
