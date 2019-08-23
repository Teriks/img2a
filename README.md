[![Javadocs](http://javadoc.io/badge/com.github.teriks/img2a.svg)](http://javadoc.io/doc/com.github.teriks/img2a)

# img2a

Img2a can convert images in various formats to ASCII art.

It can to produce raw text, HTML5 markup, or images as output.

Supported input image formats: JPEG, PNG, GIF, BMP and WBMP.

Text colorization is supported in the console as well as in HTML5 and image output.

Img2a works on *Nix as well as Windows platforms.


## Console Art Example

Cat on a white background rendered with **--invert**:

![Demo1](https://raw.githubusercontent.com/Teriks/img2a/master/example.png)


## Direct To Image Example

Butterfly on a flower rendered with **--colors** and a reduced font size:

![Demo2](https://raw.githubusercontent.com/Teriks/img2a/master/example2.png)


# Usage as as Library

img2a provides a small library which covers all
the functionality of the command line tool.

It can be added to maven projects with:


```xml

<dependency>
  <groupId>com.github.teriks</groupId>
  <artifactId>img2a</artifactId>
  <version>...</version>
</dependency>


```

Follow the javadoc badge link at the top of this readme for library documentation.


# Install / Run

See the releases page for artifacts.

## Debian / Ubuntu:


```bash

# Replace VERSION with the artifact version.

sudo dpkg -i img2a_VERSION_all.deb
sudo apt-get install -f

```


## Redhat:

```bash

# Replace VERSION with the artifact version.

sudo yum install img2a-VERSION-noarch.rpm

```


## Windows:

Install Java 1.7+ for Windows.

Place the **img2a.exe** artifact wherever you like and add 
the folder it resides in to your PATH environmental variable.

**img2a.exe** can be downloaded from the github releases
page for each release.


## Other Platforms:

The **img2a-VERSION-jar-with-dependencies.jar** artifact can be used
to run **img2a** on platforms for which an installer or specific binary
has not been created.

**img2a-VERSION-jar-with-dependencies.jar** can be downloaded from
the releases page on github or maven central.

You can invoke **img2a** with:


```bash

# Replace VERSION with the artifact version.

java -jar img2a-VERSION-jar-with-dependencies.jar (your args)


```

Or write an entry script to do it for you.


# Command Line Examples

### Write To Console:

```bash

# --size, --width, and --height are interpreted
# as output image dimensions in character columns/rows.

# Use default terminal autosizing

img2a myfile.png

img2a www.myurl.com/image.jpg

# Force a size

img2a myfile.png --size 40x20

# Force a size: 40x40

img2a myfile.png --size 40

# Force a size but maintain aspect ratio.
# This will try to fit the image into a 40x20 column/row
# box while preserving the aspect ratio.

img2a myfile.png --size 40x20 --preserve-aspect

# Auto calculate height from width using aspect ratio

img2a myfile.png --width 40

# Auto calculate width from height using aspect ratio

img2a myfile.png --height 40

```

### Write To Html File (Redirect works on Windows also):

```bash

# --size, --width, and --height work the same
# as when producing console output.

img2a myfile.png --html > mypage.html

img2a www.myurl.com/image.jpg --html > mypage.html

# Adjust background color, same syntax as --html-foreground

img2a myfile.png --html --colors --html-background white > mypage.html

img2a myfile.png --html --colors --html-background "rgb(255,255,255)" > mypage.html

```

### Write Directly To Another Image:

```bash

# --size, --width, and --height are interpreted
# as output image dimensions in pixels.

img2a myfile.png --image-out myfile-ascii.png

img2a myfile.png --image-out myfile-ascii.png --invert

img2a myfile.png --image-out myfile-ascii.png --colors --fill

# Keep original aspect ratio, but try to fit into a certain area

img2a myfile.png --image-out myfile-ascii.png --colors --fill --size 300x200 --preserve-aspect

# Auto calculate height from width using aspect ratio

img2a myfile.png --image-out myfile-ascii.png --colors --fill --width 300

# Auto calculate width from height using aspect ratio

img2a myfile.png --image-out myfile-ascii.png --colors --fill --height 300

# Adjust background color, same syntax as --image-foreground

img2a myfile.png --image-out myfile-ascii.png --colors --image-background white

img2a myfile.png --image-out myfile-ascii.png --colors --image-background "#FFFFFF"

img2a myfile.png --image-out myfile-ascii.png --colors --image-background 255,255,255

```

# All Options

```

usage: img2a [-h] [-v] [--preserve-aspect]
             [--resample-filter RESAMPLE_FILTER] [--user-agent USER_AGENT]
             [--height-scale HEIGHT_SCALE] [--flip-x] [--flip-y] [--html]
             [--html-raw] [--html-title HTML_TITLE]
             [--html-font-weight HTML_FONT_WEIGHT]
             [--html-font-style HTML_FONT_STYLE]
             [--html-font-size HTML_FONT_SIZE]
             [--html-background HTML_BACKGROUND]
             [--html-foreground HTML_FOREGROUND] [--image-out IMAGE_OUT]
             [--image-out-format IMAGE_OUT_FORMAT]
             [--image-font IMAGE_FONT]
             [--image-font-style IMAGE_FONT_STYLE]
             [--image-font-size IMAGE_FONT_SIZE]
             [--image-background IMAGE_BACKGROUND]
             [--image-foreground IMAGE_FOREGROUND] [--palette PALETTE]
             [--invert] [--colors] [--fill] [--grayscale]
             [--red-weight RED_WEIGHT] [--green-weight GREEN_WEIGHT]
             [--blue-weight BLUE_WEIGHT] [--size SIZE | --width WIDTH |
             --height HEIGHT] input

Convert an image file to ASCII

positional arguments:
  input                  Input image file or url.

optional arguments:
  -h, --help             show this help message and exit
  -v, --version
  --preserve-aspect      Preserve the aspect  ratio  of  the original image
                         when --size is used.
  --resample-filter RESAMPLE_FILTER
                         Set the  resample  filter  used  when  scaling  an
                         image. One of:  "default",  "replicate", "smooth",
                         "fast",  "area_averaging".  These   correspond  to
                         fields in Java's java.awt.Image  class, img2a uses
                         "smooth" by default.
  --user-agent USER_AGENT
                         Set the user agent  string  used  when fetching an
                         image from a URL.  The  default  user agent string
                         impersonates curl.
  --height-scale HEIGHT_SCALE
                         Set height scale  for  the  final output dimension
                         calculation.  When  using   --size  +  --preserve-
                         aspect, --width or --height.  The  default is 0.5f
                         because it is assumed that  characters are half as
                         tall  as  they   are   wide.   This  also  effects
                         terminal/html auto-fit when  the options mentioned
                         above are not used. This option does not effect --
                         image-out in any way.
  --flip-x               Flip   the   generated   output    along   the   X
                         (horizontal) axis.
  --flip-y               Flip the generated output  along  the Y (vertical)
                         axis.

Size Options:
  Options controlling output size. Only one of  these may be specified at a
  time.

  --size SIZE            Output size in  the  format  WxH  or  WH. Terminal
                         auto fit will be used  if  not specified. If using
                         --html, the default size  value is 128x128 (aspect
                         corrected).  If  using  --image-out,  the  default
                         size is the  input  image  size.  Passing a single
                         integer (WH) will cause that  value to be used for
                         both the width and height.
  --width WIDTH          Set  output  width,  auto  calculate  height  from
                         aspect ratio.
  --height HEIGHT        Set  output  height,  auto  calculate  width  from
                         aspect ratio.

HTML Output Options:
  Options for producing HTML5 output.

  --html                 Output an HTML5 document.
  --html-raw             Output HTML5  without  any  header/document/style.
                         This  will  just   output   the   generated  ASCII
                         content, with characters  wrapped  in colored span
                         elements if --colors is used.
  --html-title HTML_TITLE
                         Set the document  title  when  using  --html. This
                         has no effect if --html-raw is specified.
  --html-font-weight HTML_FONT_WEIGHT
                         (Default 'bold') Set the  CSS  font weight for all
                         ASCII content  when  using  --html.  This  has  no
                         effect  if  --html-raw  is  specified.  The  value
                         given here is  passed  directly  into  CSS  in raw
                         form.
  --html-font-style HTML_FONT_STYLE
                         (Default 'normal') Set the CSS  font style for all
                         ASCII content  when  using  --html.  This  has  no
                         effect  if  --html-raw  is  specified.  The  value
                         given here is  passed  directly  into  CSS  in raw
                         form.
  --html-font-size HTML_FONT_SIZE
                         (Default '8pt') Set  the  CSS  font  size  for all
                         ASCII content  when  using  --html.  This  has  no
                         effect  if  --html-raw  is  specified.  The  value
                         given here is  passed  directly  into  CSS  in raw
                         form.
  --html-background HTML_BACKGROUND
                         CSS background color for  html output. Defaults to
                         "black". The value given  here  is passed directly
                         into CSS in raw form.
  --html-foreground HTML_FOREGROUND
                         CSS default  foreground  color  for  html  output.
                         Defaults to  "white".  The  value  given  here  is
                         passed directly into CSS in raw form.

Image Output Options:
  Options for rendering output directly to another image.

  --image-out IMAGE_OUT  Output image  file  name.  This  option  indicates
                         that you want to  render  the  generated ASCII art
                         to an image as  output.  --size  is interpreted as
                         the output  dimensions  in  pixels  for  the image
                         itself. The  options  --colors,  --fill, --invert,
                         and --grayscale are fully supported.
  --image-out-format IMAGE_OUT_FORMAT
                         Override  image  output  format,   it  is  usually
                         determined by the file  extension of the specified
                         output file. It defaults  to  "png"  if the output
                         image file does not possess  a file extension. The
                         file extension will not be  added to the file name
                         if it is missing.
  --image-font IMAGE_FONT
                         Font  family  to   use   for   rendering  text,  a
                         Monospaced font is  recommended  for best results.
                         The default font family used is "Monospaced".
  --image-font-style IMAGE_FONT_STYLE
                         Font  style  to  use  for  rendering  text.  Valid
                         values    are:    "plain",    "bold",    "italic",
                         "bold_italic".  The  default   value   is  "bold",
                         values are case insensitive.
  --image-font-size IMAGE_FONT_SIZE
                         Font size (in  points)  to  use  when rendering an
                         image, the default value is 12.
  --image-background IMAGE_BACKGROUND
                         Image background color. Accepts  a  CSS color name
                         (case insensitive), HEX value  with hash, or comma
                         separated RGB value.
  --image-foreground IMAGE_FOREGROUND
                         Image  foreground  (font  color).  Accepts  a  CSS
                         color name  (case  insensitive),  HEX  value  with
                         hash, or comma  separated  RGB  value.  This value
                         will have no effect  if  you  specify the --colors
                         option.

Color/Shading Options:
  Options for controlling coloration and shading.

  --palette PALETTE      Character palette to use  when  shading the image.
                         characters at the  low  end  of  the pallet string
                         will be used for  pixels  with low brightness, and
                         characters at  the  high  end  will  be  used  for
                         pixels with high brightness.
  --invert               Invert  the  ASCII  character  palette  used  when
                         approximating   pixel    'brightness'    with    a
                         character.  This   option   has   no   effect   on
                         background/foreground color when  using  --html or
                         --image-out
  --colors               Colorize output, this  works  with  plain terminal
                         output (color  is  approximated),  --html  output,
                         and --image-out.
  --fill                 Fill in  background  color  when  using  --colors.
                         This also works with --html and --image-out.
  --grayscale            Process  the  image  in  grayscale,  this  effects
                         colorized output.
  --red-weight RED_WEIGHT
                         (Default:  0.2989)  Red  weight   for  pixel  luma
                         calculation.
  --green-weight GREEN_WEIGHT
                         (Default: 0.5866)  Green  weight  for  pixel  luma
                         calculation.
  --blue-weight BLUE_WEIGHT
                         (Default:  0.1145)  Blue  weight  for  pixel  luma
                         calculation.

```
