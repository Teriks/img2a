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

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

/**
 * Reads image data from disk, the network, or a stream, and
 * processes it with the main img2a algorithm.
 * <br>
 * The primary output is {@link Pixel} objects, which are returned inside
 * of an iterator over each output row.
 * <br>
 * See: {@link ImageAsciiReader#read(int, int)}
 */
public class ImageAsciiReader {
    private static final String USER_AGENT = "curl/7.52.1";
    private float m_blueWeight = 0.1145f;

    private boolean m_flipX = false;
    private boolean m_flipY = false;
    private boolean m_grayscaleColor = false;
    private float m_greenWeight = 0.5866f;
    private boolean m_invertPalette = false;
    private String m_palette = "   ...',;:clodxkO0KXNWM";
    private float m_redWeight = 0.2989f;
    private int m_resampleFilter = Image.SCALE_SMOOTH;

    private BufferedImage m_img;
    private BufferedImage m_work_img;


    /**
     * Construct an image reader from a file on disk.
     *
     * @param file The image file.
     * @throws IOException               If there is an IO error reading the file.
     * @throws InvalidImageDataException if the file does not contain recognizable image data.
     * @throws FileNotFoundException     if the given file does not exist on disk.
     */
    public ImageAsciiReader(File file) throws
            IOException,
            InvalidImageDataException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.toString());
        }

        m_img = ImageIO.read(file);

        if (this.m_img == null) {
            throw new InvalidImageDataException(file.getName());
        }
    }

    /**
     * Construct an image reader from an image input stream.
     *
     * @param input_stream The image input stream.
     * @throws IOException               If there is an IO error reading the stream.
     * @throws InvalidImageDataException If the stream does not contain recognizable image data.
     */
    public ImageAsciiReader(ImageInputStream input_stream) throws
            IOException,
            InvalidImageDataException {
        m_img = ImageIO.read(input_stream);

        if (this.m_img == null) {
            throw new InvalidImageDataException();
        }

    }


    /**
     * Construct an image reader directly from a {@link BufferedImage}.
     *
     * @param image The {@link BufferedImage}.
     * @throws IllegalArgumentException If the image parameter is null.
     */
    public ImageAsciiReader(BufferedImage image) throws
            IllegalArgumentException {

        if (image == null) {
            throw new IllegalArgumentException("image");
        }

        m_img = image;
    }

    /**
     * Construct an image reader from an input stream.
     *
     * @param input_stream The image input stream.
     * @throws IOException               If there is an IO error reading the stream.
     * @throws InvalidImageDataException If the stream does not contain recognizable image data.
     */
    public ImageAsciiReader(InputStream input_stream) throws
            IOException,
            InvalidImageDataException {
        m_img = ImageIO.read(input_stream);
        if (this.m_img == null) {
            throw new InvalidImageDataException();
        }
    }

    /**
     * Construct a reader from a given url.
     * <br>
     * The user agent string will default to impersonating curl.
     *
     * @param url Image url.
     * @throws IOException               If there is an IO error reading from the URL.
     * @throws InvalidImageDataException If a request to the given URL does not return recognizable image data.
     */
    public ImageAsciiReader(URL url) throws IOException, InvalidImageDataException {
        this(url, null);
    }

    /**
     * Construct a reader from a given url and user agent string.
     * <br>
     * The user agent string will be used when requesting the image.
     *
     * @param url       Image url.
     * @param userAgent Browser / User Agent string.
     * @throws IOException               If there is an IO error reading from the URL.
     * @throws InvalidImageDataException If a request to the given URL does not return recognizable image data.
     */

    public ImageAsciiReader(URL url, String userAgent) throws
            IOException,
            InvalidImageDataException {

        URLConnection connection = url.openConnection();

        if (userAgent == null) {
            connection.setRequestProperty("User-Agent", USER_AGENT);
        } else {
            connection.setRequestProperty("User-Agent", userAgent);
        }

        connection.setRequestProperty("Accept", "*/*");

        this.m_img = ImageIO.read(connection.getInputStream());

        if (this.m_img == null) {
            throw new InvalidImageDataException(url.toString());
        }
    }

    /**
     * Calculate an aspect correct height for the current image using a given width.
     * <br>
     * See Also: {@link #calcAspectCorrectWidth(int)}
     *
     * @param width Desired width.
     * @return Aspect correct height.
     */
    public final int calcAspectCorrectHeight(int width) {
        return calcAspectCorrectHeight(width, 1.0f);
    }

    /**
     * Calculate an aspect correct height for the current image using a given width
     * and final height scaling factor.
     * <br>
     * See Also: {@link #calcAspectCorrectWidth(int)}
     *
     * @param width        Desired width.
     * @param height_scale Scaling factor for the height value.
     * @return Aspect correct height.
     */
    public final int calcAspectCorrectHeight(int width, float height_scale) {
        float ratio = (float) this.getImageHeight() / (float) this.getImageWidth();
        return Math.round((width * ratio) * height_scale);
    }

    /**
     * Calculate an aspect correct size for the current image with a height scaling factor of 1.0f
     * <br>
     * See: {@link #calcAspectCorrectSize(int, int, float)}
     *
     * @param new_x New width
     * @param new_y New height
     * @return The aspect correct size.
     */
    public final Point calcAspectCorrectSize(int new_x, int new_y) {
        return this.calcAspectCorrectSize(new_x, new_y, 1);
    }

    /**
     * Calculate an aspect correct size for the current image using a new set of dimensions.
     * The size returned will be a size with the same aspect ratio of the original image, that
     * will fit inside the dimensions given by **new_x** and **new_y**.
     * <br>
     * **scale_y** can be used to apply a scaling factor to the returned height.
     *
     * @param new_x   New width;
     * @param new_y   New height;
     * @param scale_y Scaling factor for final height.
     * @return The aspect correct size.
     */
    public final Point calcAspectCorrectSize(int new_x, int new_y, float scale_y) {
        float x = this.getImageWidth();
        float y = this.getImageHeight();

        if (x > new_x) {
            y = Math.max(y * (new_x / x), 1);
            x = new_x;
        }

        if (y > new_y) {
            x = Math.max(x * (new_y / y), 1);
            y = new_y;
        }

        return new Point(Math.round(x), Math.round(y * scale_y));
    }

    /**
     * Calculate an aspect correct size for the current image with a height scaling factor of 1.0f
     * <br>
     * See: {@link #calcAspectCorrectSize}
     *
     * @param newSize The new size as a {@link Point}, (width, height)
     * @return The aspect correct size.
     */
    public final Point calcAspectCorrectSize(Point newSize) {
        return calcAspectCorrectSize(newSize.x, newSize.y);
    }

    /**
     * Calculate an aspect correct size for the current image with a given height scaling factor.
     * <br>
     * See: {@link #calcAspectCorrectSize}
     *
     * @param newSize The new size as a {@link Point}, (width, height)
     * @param scale_y Scaling factor for final height.
     * @return The aspect correct size.
     */
    public final Point calcAspectCorrectSize(Point newSize, float scale_y) {
        return calcAspectCorrectSize(newSize.x, newSize.y, scale_y);
    }

    /**
     * Calculate an aspect correct width for the current image using a given height.
     * <br>
     * See Also: {@link #calcAspectCorrectHeight(int)}, {@link #calcAspectCorrectHeight(int, float)}
     *
     * @param height Desired height.
     * @return Aspect correct width.
     */
    public final int calcAspectCorrectWidth(int height) {
        float ratio = (float) this.getImageWidth() / (float) this.getImageHeight();
        return Math.round(height * ratio);
    }

    /**
     * Calculate the Luma value for a given RGB color using the current RGB weights.
     * <br>
     * See: {@link #setRedWeight(float)}, {@link #setGreenWeight(float)}, {@link #setBlueWeight(float)}
     *
     * @param color The color to calculate the Luma value for.
     * @return Luma value between 0.0f and 1.0f
     */
    public float calcLuma(Color color) {
        return ((this.getRedWeight() * color.getRed()) / 255)
                + ((this.getGreenWeight() * color.getGreen()) / 255)
                + ((this.getBlueWeight() * color.getBlue()) / 255);
    }

    /**
     * See: {@link #setBlueWeight(float)}
     * <br>
     * Get the blue component weight for Luma calculation.
     * <br>
     * Default value is: 0.1145f
     *
     * @return Blue component weight.
     */
    public final float getBlueWeight() {
        return m_blueWeight;
    }

    /**
     * Set the blue component weight for Luma calculation.
     * <br>
     * Default value is: 0.1145f
     *
     * @param weight Component weight.
     */
    public final void setBlueWeight(float weight) {
        this.m_blueWeight = weight;
    }

    /**
     * See: {@link #setFlipX(boolean)}
     *
     * @return Whether or not to flip the image along the X axis while reading.
     */
    public final boolean getFlipX() {
        return this.m_flipX;
    }

    /**
     * Set whether or not to flip the image along the X axis while reading.
     *
     * @param value True or False
     */
    public final void setFlipX(boolean value) {
        this.m_flipX = value;
    }

    /**
     * See: {@link #setFlipY(boolean)}
     *
     * @return Whether or not to flip the image the Y axis while reading.
     */
    public final boolean getFlipY() {
        return this.m_flipY;
    }

    /**
     * Set whether or not to flip the image along the Y axis while reading.
     *
     * @param value True or False
     */
    public final void setFlipY(boolean value) {
        this.m_flipY = value;
    }

    /**
     * See: {@link #setGreenWeight(float)}
     * <br>
     * Get the green component weight for Luma calculation.
     * <br>
     * Default value is: 0.5866f
     *
     * @return Green component weight for Luma calculation.
     */
    public final float getGreenWeight() {
        return m_greenWeight;
    }

    /**
     * Set the green component weight for Luma calculation.
     * <br>
     * Default value is: 0.5866f
     *
     * @param weight Component weight.
     */
    public final void setGreenWeight(float weight) {
        this.m_greenWeight = weight;
    }

    /**
     * Return a reference to the {@link BufferedImage} that is
     * set to be read/processed.
     *
     * @return BufferedImage reference.
     */
    public final BufferedImage getImage() {
        return m_img;
    }


    /**
     * getImage().getHeight(), See: {@link #getImage()}
     *
     * @return getImage().getHeight()
     */
    public final int getImageHeight() {
        return this.m_img.getHeight();
    }

    /**
     * getImage().getWidth(), See: {@link #getImage()}
     *
     * @return getImage().getWidth()
     */
    public final int getImageWidth() {
        return this.m_img.getWidth();
    }

    /**
     * See: {@link #setInvertPalette(boolean)}
     *
     * @return Whether or not to invert indexing of the character palette.
     */
    public final boolean getInvertPalette() {
        return this.m_invertPalette;
    }

    /**
     * Set whether or not to invert indexing of the current character palette.
     * <br>
     * See: {@link #setPalette(String)}
     *
     * @param invert Whether or not to invert indexing of the current character palette.
     */
    public final void setInvertPalette(boolean invert) {
        this.m_invertPalette = invert;
    }

    /**
     * See: {@link #setPalette(String)}
     *
     * @return The character palette string.
     */
    public final String getPalette() {
        return m_palette;
    }

    /**
     * Set the character palette used when converting the image to a shaded character representation.
     * <br>
     * The characters near the beginning of the palette represent dark image areas, and
     * the characters near the end represent light image areas.
     * <br>
     * {@link #setInvertPalette(boolean)} can be used to invert palette indexing.
     *
     * @param value The character palette string.
     */
    public final void setPalette(String value) {
        this.m_palette = value;
    }

    Pixel getPixel(int x, int y) {

        if (this.m_flipX) {
            x = (this.m_work_img.getWidth() - 1) - x;
        }

        if (this.m_flipY) {
            y = (this.m_work_img.getHeight() - 1) - y;
        }

        int color_int = this.m_work_img.getRGB(x, y);

        Point coord = new Point(x, y);

        Color color = new Color(color_int);

        float luma = this.calcLuma(color);

        if (this.getUseGrayscaleColor()) {
            int grey = Math.round(luma * 255);
            color = new Color(grey, grey, grey);
        }

        int palette_idx = Math.round(
                (this.getInvertPalette() ? (luma * -1) + 1 : luma) *
                        (this.getPalette().length() - 1));

        char character = this.getPalette().charAt(palette_idx);

        return new Pixel(coord, color, luma, character);
    }

    /**
     * See: {@link #setRedWeight(float)}
     * <br>
     * Get the red component weight for Luma calculation.
     * <br>
     * Default value is: 0.2989f
     *
     * @return Red component weight for Luma calculation.
     */
    public final float getRedWeight() {
        return m_redWeight;
    }

    /**
     * Set the red component weight for Luma calculation.
     * <br>
     * Default value is: 0.2989f
     *
     * @param weight Component weight.
     */
    public final void setRedWeight(float weight) {
        this.m_redWeight = weight;
    }

    /**
     * See: {@link #setResampleFilter(int)}
     *
     * @return Current resample filter.
     */
    public final int getResampleFilter() {
        return m_resampleFilter;
    }

    /**
     * Set the resample filter used when downsizing or up-sizing the image.
     * <br>
     * One Of:<br><br>
     * {@link Image#SCALE_DEFAULT}<br>
     * {@link Image#SCALE_FAST}<br>
     * {@link Image#SCALE_SMOOTH}<br>
     * {@link Image#SCALE_REPLICATE}<br>
     * {@link Image#SCALE_AREA_AVERAGING}<br>
     *
     * @param value java.awt.Image.SCALE_* Value
     */
    public final void setResampleFilter(int value) {
        this.m_resampleFilter = value;
    }

    /**
     * See: {@link #setUseGrayscaleColor(boolean)}
     *
     * @return Whether or not to read back pixel colors in grayscale.
     */
    public final boolean getUseGrayscaleColor() {
        return m_grayscaleColor;
    }

    /**
     * Set whether or not to read back pixel colors in grayscale
     * based of the current weights given for Luma calculation.
     * <br>
     * See: {@link #setRedWeight(float)}, {@link #setGreenWeight(float)}, {@link #setBlueWeight(float)}
     *
     * @param value True or False
     */
    public final void setUseGrayscaleColor(boolean value) {
        this.m_grayscaleColor = value;
    }

    final int getWorkImageHeight() {
        return this.m_work_img.getWidth();
    }

    final int getWorkImageWidth() {
        return this.m_work_img.getWidth();
    }

    /**
     * Read and process the current image with the img2a algorithm, using the
     * given amount of columns and rows as the dimensions for the resulting output.
     * <br>
     * The underlying image will be resized as necessary if it is smaller
     * or larger than the given dimensions, See: {@link #setResampleFilter(int)}.
     * <br>
     * The possible resize mentioned above happens to a working copy of
     * the image, and does not effect the {@link BufferedImage} instance
     * returned by {@link #getImage()}.
     * <br>
     * This returns an iterator over <b>rows</b> count {@link ImageRow} objects.
     * <br>
     * Each {@link ImageRow} is an iterator over <b>cols</b> count {@link Pixel} objects.
     *
     * @param cols Number of columns desired in the output from reading
     * @param rows Number of rows desired in the output from reading
     * @return An iterator over iterable {@link ImageRow} objects
     */
    public Iterable<ImageRow> read(int cols, int rows) {

        if (this.m_work_img == null ||
                (this.m_work_img.getWidth() != cols || this.m_work_img.getHeight() != rows)) {

            Image tmp_img = this.getImage().
                    getScaledInstance(cols, rows, this.getResampleFilter());

            this.m_work_img = new BufferedImage(cols, rows, this.getImage().getType());

            Graphics2D graphics = this.m_work_img.createGraphics();
            graphics.drawImage(tmp_img, 0, 0, null);
            graphics.dispose();
        }

        final int rowCount = rows;
        final ImageAsciiReader reader_self = this;

        return new Iterable<ImageRow>() {

            public Iterator<ImageRow> iterator() {
                return new Iterator<ImageRow>() {
                    int rowIndex = 0;

                    public boolean hasNext() {
                        return rowIndex < rowCount;
                    }

                    public ImageRow next() {
                        return new ImageRow(reader_self, this.rowIndex++);
                    }

                    public void remove() {
                    }
                };
            }
        };

    }

    /**
     * See: {@link #read(int, int)}
     *
     * @param size The output size (cols, rows) condensed into a {@link Point object}
     * @return An iterator over iterable {@link ImageRow} objects
     */
    public final Iterable<ImageRow> read(Point size) {
        return this.read(size.x, size.y);
    }
}
