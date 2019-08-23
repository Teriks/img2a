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

import com.github.teriks.img2a.utils.CssColors;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.action.StoreTrueArgumentAction;
import net.sourceforge.argparse4j.impl.action.VersionArgumentAction;
import net.sourceforge.argparse4j.inf.*;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * img2a command line tool entry point.
 */
class App {

    private static final String PROG_NAME = "img2a";

    private App() {
    }

    private static Point calcOutputSize(Namespace arguments,
                                        ImageAsciiReader imgReader,
                                        float height_scale) {

        boolean arg_preserve_aspect = arguments.get("preserve_aspect");

        Point output_size = arguments.get("size");
        Integer arg_width = arguments.get("width");
        Integer arg_height = arguments.get("height");

        if (output_size != null && arg_preserve_aspect) {

            return imgReader.
                    calcAspectCorrectSize(
                            output_size.x,
                            output_size.y,
                            height_scale);

        } else if (arg_width != null) {
            return new Point(arg_width,
                    imgReader.calcAspectCorrectHeight(
                            arg_width,
                            height_scale)
            );
        } else if (arg_height != null) {
            return new Point(
                    imgReader.calcAspectCorrectWidth(arg_height),
                    Math.round(arg_height * height_scale)
            );
        }

        return output_size;
    }

    private static ArgumentParser createArgumentParser() {

        ArgumentParser parser = ArgumentParsers.newArgumentParser(PROG_NAME)
                .description("Convert an image file to ASCII");


        Package pkg = App.class.getPackage();

        parser.version(pkg.getImplementationTitle() + " " + pkg.getImplementationVersion());


        parser.addArgument("-v", "--version")
                .action(new VersionArgumentAction());


        parser.addArgument("input")
                .required(true)
                .help("Input image file or url.");

        parser.addArgument("--preserve-aspect")
                .action(new StoreTrueArgumentAction())
                .help("Preserve the aspect ratio of the original " +
                        "image when --size is used.");

        parser.addArgument("--resample-filter")
                .type(new ArgResampleFilterType())
                .help("Set the resample filter used when scaling an image. " +
                        "One of: \"default\", \"replicate\", \"smooth\", \"fast\", \"area_averaging\". " +
                        "These correspond to fields in Java's java.awt.Image class, " +
                        "img2a uses \"smooth\" by default.");

        parser.addArgument("--user-agent")
                .help("Set the user agent string used when fetching an image from a URL. " +
                        "The default user agent string impersonates curl.");

        MutuallyExclusiveGroup size_m_group =
                parser.addMutuallyExclusiveGroup("Size Options")
                        .description(
                                "Options controlling output size. " +
                                        "Only one of these may be specified at a time.");

        size_m_group.addArgument("--size")
                .type(new ArgSizeType())
                .help("Output size in the format WxH or WH. " +
                        "Terminal auto fit will be used if not specified. " +
                        "If using --html, the default size value is 128x128 (aspect corrected). " +
                        "If using --image-out, the default size is the input image size. " +
                        "Passing a single integer (WH) will cause that value to be used for both " +
                        "the width and height.");

        size_m_group.addArgument("--width")
                .type(Integer.class)
                .help("Set output width, auto calculate height from aspect ratio.");

        size_m_group.addArgument("--height")
                .type(Integer.class)
                .help("Set output height, auto calculate width from aspect ratio.");


        parser.addArgument("--height-scale")
                .type(Float.class)
                .setDefault(0.5f)
                .help("Set height scale for the final output dimension calculation. " +
                        "When using --size + --preserve-aspect, --width or --height. " +
                        "The default is 0.5f because it is assumed that characters are half " +
                        "as tall as they are wide. This also effects terminal/html auto-fit " +
                        "when the options mentioned above are not used. " +
                        "This option does not effect --image-out in any way.");

        parser.addArgument("--flip-x")
                .action(new StoreTrueArgumentAction())
                .help("Flip the generated output along the X (horizontal) axis.");

        parser.addArgument("--flip-y")
                .action(new StoreTrueArgumentAction())
                .help("Flip the generated output along the Y (vertical) axis.");

        ArgumentGroup html_group = parser.addArgumentGroup("HTML Output Options")
                .description("Options for producing HTML5 output.");


        html_group.addArgument("--html")
                .action(new StoreTrueArgumentAction())
                .help("Output an HTML5 document.");

        html_group.addArgument("--html-raw")
                .action(new StoreTrueArgumentAction())
                .help("Output HTML5 without any header/document/style." +
                        "This will just output the generated ASCII content, " +
                        "with characters wrapped in colored span elements if " +
                        "--colors is used.");


        html_group.addArgument("--html-title")
                .help("Set the document title when using --html. " +
                        "This has no effect if --html-raw is specified.");

        html_group.addArgument("--html-font-weight")
                .help("(Default 'bold') Set the CSS font weight for all ASCII content " +
                        "when using --html. This has no effect if --html-raw " +
                        "is specified. The value given here is passed directly " +
                        "into CSS in raw form.");

        html_group.addArgument("--html-font-style")
                .help("(Default 'normal') Set the CSS font style for all ASCII content " +
                        "when using --html. This has no effect if --html-raw " +
                        "is specified. The value given here is passed directly " +
                        "into CSS in raw form.");

        html_group.addArgument("--html-font-size")
                .help("(Default '8pt') Set the CSS font size for all ASCII content " +
                        "when using --html. This has no effect if --html-raw is specified. " +
                        "The value given here is passed directly into CSS in raw form.");

        html_group.addArgument("--html-background")
                .help("CSS background color for html output. Defaults to \"black\". " +
                        "The value given here is passed directly into CSS in raw form.");

        html_group.addArgument("--html-foreground")
                .help("CSS default foreground color for html output. Defaults to \"white\". " +
                        "The value given here is passed directly into CSS in raw form.");

        ArgumentGroup image_output_group =
                parser.addArgumentGroup("Image Output Options")
                        .description("Options for rendering output directly to another image. ");

        image_output_group.addArgument("--image-out")
                .type(String.class)
                .help("Output image file name. This option indicates that you " +
                        "want to render the generated ASCII art to an image " +
                        "as output. --size is interpreted as the output dimensions " +
                        "in pixels for the image itself. The options --colors, --fill, --invert, " +
                        "and --grayscale are fully supported.");

        image_output_group.addArgument("--image-out-format")
                .type(String.class)
                .help("Override image output format, it is usually " +
                        "determined by the file extension of the specified output file. " +
                        "It defaults to \"png\" if the output image file does not possess " +
                        "a file extension. The file extension will not be added to the file name " +
                        "if it is missing.");

        image_output_group.addArgument("--image-font")
                .setDefault("Monospaced")
                .help("Font family to use for rendering text, a Monospaced " +
                        "font is recommended for best results. " +
                        "The default font family used is \"Monospaced\".");

        image_output_group.addArgument("--image-font-style")
                .type(new ArgFontStyleType())
                .setDefault(Font.BOLD)
                .help("Font style to use for rendering text. " +
                        "Valid values are: \"plain\", \"bold\", \"italic\", \"bold_italic\". " +
                        "The default value is \"bold\", values are case insensitive.");

        image_output_group.addArgument("--image-font-size")
                .type(new ArgFontSizeType())
                .help("Font size (in points) to use when rendering an image, " +
                        "the default value is 12.")
                .setDefault(12);

        image_output_group.addArgument("--image-background")
                .type(new ArgColorType())
                .help("Image background color. " +
                        "Accepts a CSS color name (case insensitive), HEX value with hash, " +
                        "or comma separated RGB value.")
                .setDefault(Color.black);

        image_output_group.addArgument("--image-foreground")
                .type(new ArgColorType())
                .help("Image foreground (font color). " +
                        "Accepts a CSS color name (case insensitive), HEX value with hash, " +
                        "or comma separated RGB value. " +
                        "This value will have no effect if you specify the --colors option.")
                .setDefault(Color.white);

        ArgumentGroup color_group =
                parser.addArgumentGroup("Color/Shading Options")
                        .description("Options for controlling coloration and shading.");


        color_group.addArgument("--palette")
                .type(new ArgPaletteStringType())
                .help("Character palette to use when shading the image. " +
                        "characters at the low end of the pallet string will be used " +
                        "for pixels with low brightness, and characters at the high " +
                        "end will be used for pixels with high brightness.");

        color_group.addArgument("--invert")
                .action(new StoreTrueArgumentAction())
                .help("Invert the ASCII character palette used when " +
                        "approximating pixel 'brightness' with a character. " +
                        "This option has no effect on background/foreground color " +
                        "when using --html or --image-out");

        color_group.addArgument("--colors")
                .action(new StoreTrueArgumentAction())
                .help("Colorize output, this works with plain terminal output " +
                        "(color is approximated), --html output, and --image-out.");

        color_group.addArgument("--fill")
                .action(new StoreTrueArgumentAction())
                .help("Fill in background color when using --colors. " +
                        "This also works with --html and --image-out.");

        color_group.addArgument("--grayscale")
                .action(new StoreTrueArgumentAction())
                .help("Process the image in grayscale, this effects colorized output.");

        color_group.addArgument("--red-weight")
                .type(Float.class)
                .help("(Default: 0.2989) Red weight for pixel luma calculation.");

        color_group.addArgument("--green-weight")
                .type(Float.class)
                .help("(Default: 0.5866) Green weight for pixel luma calculation.");

        color_group.addArgument("--blue-weight")
                .type(Float.class)
                .help("(Default: 0.1145) Blue weight for pixel luma calculation.");

        return parser;
    }

    private static AsciiPrinter createConsolePrinter(
            Namespace arguments, ImageAsciiReader imgReader) {

        ConsolePrinter printer = new ConsolePrinter(imgReader);

        boolean arg_colors = arguments.get("colors");
        boolean arg_fill = arguments.get("fill");

        float arg_height_scale = arguments.get("height_scale");

        printer.setDefaultHeightScale(arg_height_scale);

        printer.setUseColors(arg_colors);
        printer.setColorFill(arg_fill);

        return printer;
    }

    private static AsciiPrinter createHtmlPrinter(
            Namespace arguments, ImageAsciiReader imgReader) {

        String arg_html_title = arguments.get("html_title");
        String arg_html_fontweight = arguments.get("html_font_weight");
        String arg_html_fontstyle = arguments.get("html_font_style");
        String arg_html_fontsize = arguments.get("html_font_size");

        String arg_html_background = arguments.get("html_background");
        String arg_html_foreground = arguments.get("html_foreground");

        boolean arg_html_raw = arguments.get("html_raw");
        boolean arg_colors = arguments.get("colors");
        boolean arg_fill = arguments.get("fill");

        float arg_height_scale = arguments.get("height_scale");

        HtmlPrinter printer = new HtmlPrinter(imgReader);

        printer.setDefaultHeightScale(arg_height_scale);

        printer.setUseColors(arg_colors);
        printer.setColorFill(arg_fill);
        printer.setRaw(arg_html_raw);


        if (arg_html_title != null) {
            printer.setTitle(arg_html_title);
        }

        if (arg_html_fontsize != null) {
            printer.setFontSize(arg_html_fontsize);
        }

        if (arg_html_fontweight != null) {
            printer.setFontWeight(arg_html_fontweight);
        }

        if (arg_html_fontstyle != null) {
            printer.setFontStyle(arg_html_fontstyle);
        }

        if (arg_html_background != null) {
            printer.setBackground(arg_html_background);
        }

        if (arg_html_foreground != null) {
            printer.setForeground(arg_html_foreground);
        }

        return printer;
    }

    private static ImageAsciiReader createImageAsciiReader(Namespace arguments) throws
            IOException,
            InvalidImageDataException {

        ImageAsciiReader imgReader;

        String arg_input = arguments.get("input");

        try {
            String arg_user_agent = arguments.get("user_agent");

            imgReader = new ImageAsciiReader(new URL(arg_input), arg_user_agent);

        } catch (MalformedURLException err) {
            imgReader = new ImageAsciiReader(new File(arg_input));
        }

        String arg_palette = arguments.get("palette");

        if (arg_palette != null) {
            imgReader.setPalette(arg_palette);
        }

        imgReader.setInvertPalette(arguments.getBoolean("invert"));
        imgReader.setUseGrayscaleColor(arguments.getBoolean("grayscale"));

        Float arg_red_weight = arguments.get("red_weight");
        Float arg_blue_weight = arguments.get("blue_weight");
        Float arg_green_weight = arguments.get("green_weight");

        if (arg_red_weight != null) {
            imgReader.setRedWeight(arg_red_weight);
        }

        if (arg_blue_weight != null) {
            imgReader.setBlueWeight(arg_blue_weight);
        }

        if (arg_green_weight != null) {
            imgReader.setGreenWeight(arg_green_weight);
        }

        imgReader.setFlipX(arguments.getBoolean("flip_x"));
        imgReader.setFlipY(arguments.getBoolean("flip_y"));

        Integer arg_resample_filter = arguments.get("resample_filter");

        if (arg_resample_filter != null) {
            imgReader.setResampleFilter(arg_resample_filter);
        }

        return imgReader;
    }


    public static void main(String[] args) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        ArgumentParser parser = createArgumentParser();

        Namespace arguments;

        try {
            arguments = parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
            return;
        }

        if (arguments == null) {
            return;
        }

        ImageAsciiReader imgReader;

        try {
            imgReader = createImageAsciiReader(arguments);

        } catch (InvalidImageDataException e) {
            System.err.println("Provided image source contained invalid image data.");
            System.exit(3);
            return;
        } catch (FileNotFoundException e) {

            System.err.println(
                    String.format("File not found: %s", e.getMessage())
            );

            System.exit(3);
            return;
        } catch (IOException e) {
            System.err.println("Error reading from provided image source.");
            System.exit(3);
            return;
        }


        String arg_image_out = arguments.get("image_out");


        if (arg_image_out != null) {

            Point output_size = calcOutputSize(arguments, imgReader, 1.0f);

            renderImageOut(arguments, imgReader, output_size, arg_image_out);

            return;
        }


        boolean arg_html = arguments.get("html");

        AsciiPrinter printer;

        if (arg_html) {
            printer = createHtmlPrinter(
                    arguments,
                    imgReader);

        } else {
            printer = createConsolePrinter(
                    arguments,
                    imgReader);
        }

        Point output_size = calcOutputSize(
                arguments,
                imgReader,
                arguments.getFloat("height_scale"));

        try {
            printer.print(output_size);
        } catch (IOException e) {
            System.err.println(String.format("IO Error while writing output: %s", e.getMessage()));
            System.exit(3);
        }
    }

    private static void renderImageOut(Namespace arguments,
                                       ImageAsciiReader reader,
                                       Point output_size,
                                       String out_path) {

        String arg_image_out_format = arguments.get("image_out_format");

        if (output_size == null) {
            output_size = new Point(
                    reader.getImageWidth(),
                    reader.getImageHeight());
        }

        AsciiImageRenderer render = new AsciiImageRenderer(reader);

        render.setUseColors(arguments.getBoolean("colors"));
        render.setColorFill(arguments.getBoolean("fill"));

        render.setBackground((Color) arguments.get("image_background"));
        render.setForeground((Color) arguments.get("image_foreground"));

        int arg_image_font_size = arguments.get("image_font_size");

        String arg_image_font = arguments.get("image_font");

        int arg_image_font_style = arguments.get("image_font_style");

        //noinspection MagicConstant
        render.setFont(new Font(
                arg_image_font,
                arg_image_font_style,
                arg_image_font_size));

        if (arg_image_out_format == null) {
            int ext_dot = out_path.lastIndexOf('.');

            arg_image_out_format = "png";

            if (ext_dot != -1) {
                arg_image_out_format = out_path.substring(
                        ext_dot + 1,
                        out_path.length());
            }
        }

        BufferedImage img = render.render(output_size);

        try {

            if (!ImageIO.write(img, arg_image_out_format, new File(out_path))) {

                System.err.println(
                        String.format("Unknown image output format: \"%s\"", arg_image_out_format));

                System.exit(3);
            }

        } catch (IOException e) {

            System.err.println(
                    String.format("IO Error writing to image file: \"%s\"", out_path));

            System.exit(3);
        }
    }

    private static class ArgPaletteStringType implements ArgumentType<String> {

        public String convert(ArgumentParser argumentParser,
                              Argument argument,
                              String input) throws ArgumentParserException {

            if (input.length() < 2) {
                throw new ArgumentParserException(
                        String.format("argument %s: Palette string must " +
                                        "contain at least two characters.",
                                argument.textualName()),
                        argumentParser);
            }

            return input;
        }
    }

    private static class ArgSizeType implements ArgumentType<Point> {

        public Point convert(ArgumentParser argumentParser,
                             Argument argument,
                             String input) throws ArgumentParserException {

            String[] parts = input.toLowerCase().split("x");


            if (parts.length > 2) {
                throw new ArgumentParserException(
                        String.format("argument %s: Size must " +
                                        "have one or two components.",
                                argument.textualName()),
                        argumentParser);
            }

            try {
                if (parts.length == 2) {
                    return new Point(
                            Integer.parseInt(parts[0]),
                            Integer.parseInt(parts[1]));
                } else {
                    int size = Integer.parseInt(parts[0]);
                    return new Point(size, size);
                }
            } catch (NumberFormatException err) {
                throw new ArgumentParserException(
                        String.format("argument %s: Unparsable size \"%s\", " +
                                        "all dimensions must be integers.",
                                argument.textualName(),
                                input),
                        argumentParser
                );
            }
        }
    }

    private static class ArgColorType implements ArgumentType<Color> {


        public Color convert(ArgumentParser argumentParser,
                             Argument argument,
                             String input) throws ArgumentParserException {

            Color cssColor = CssColors.getColor(input.toLowerCase());

            if (cssColor != null) {
                return cssColor;
            }

            String[] vals = input.split(",");

            try {
                if (vals.length < 3) {
                    return Color.decode(input);
                } else {
                    int R = Integer.parseInt(vals[0].trim());
                    int G = Integer.parseInt(vals[1].trim());
                    int B = Integer.parseInt(vals[2].trim());

                    return new Color(R, G, B);
                }
            } catch (NumberFormatException err) {
                throw new ArgumentParserException(
                        String.format("argument %s: Unparsable color " +
                                        "value: \"%s\".",
                                argument.textualName(),
                                input), argumentParser);
            }
        }
    }

    private static class ArgFontSizeType implements ArgumentType<Integer> {

        public Integer convert(ArgumentParser argumentParser,
                               Argument argument,
                               String input) throws ArgumentParserException {
            int size;
            try {
                size = Integer.parseInt(input);
            } catch (NumberFormatException err) {
                throw new ArgumentParserException(
                        String.format("argument %s: Font size " +
                                        "must be an integer value, got: \"%s\"",
                                argument.textualName(),
                                input), argumentParser);
            }

            if (size < 1) {
                throw new ArgumentParserException(
                        String.format("argument %s: Font size must " +
                                        "not be less than 1, was: %d",
                                argument.textualName(),
                                size),
                        argumentParser);
            }

            return size;
        }
    }

    private static class ArgFontStyleType implements ArgumentType<Integer> {

        public Integer convert(ArgumentParser argumentParser,
                               Argument argument,
                               String input) throws ArgumentParserException {

            String l_input = input.toLowerCase();

            if (l_input.equals("bold")) {
                return Font.BOLD;
            } else if (l_input.equals("italic")) {
                return Font.ITALIC;
            } else if (l_input.equals("plain")) {
                return Font.PLAIN;
            } else if (l_input.equals("bold_italic")) {
                return Font.BOLD | Font.ITALIC;
            } else {
                throw new ArgumentParserException(
                        String.format("argument %s: Unrecognized font style \"%s\".",
                                argument.textualName(),
                                input),
                        argumentParser);
            }

        }
    }

    private static class ArgResampleFilterType implements ArgumentType<Integer> {

        public Integer convert(ArgumentParser argumentParser,
                               Argument argument,
                               String input) throws ArgumentParserException {

            String l_input = input.toLowerCase();

            if (l_input.equals("smooth")) {
                return Image.SCALE_SMOOTH;
            } else if (l_input.equals("replicate")) {
                return Image.SCALE_REPLICATE;
            } else if (l_input.equals("area_averaging")) {
                return Image.SCALE_AREA_AVERAGING;
            } else if (l_input.equals("fast")) {
                return Image.SCALE_FAST;
            } else if (l_input.equals("default")) {
                return Image.SCALE_DEFAULT;
            } else {
                throw new ArgumentParserException(
                        String.format("argument %s: Unrecognized resample filter \"%s\".",
                                argument.textualName(),
                                input),
                        argumentParser);
            }
        }
    }
}
