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

package com.github.teriks.img2a.utils;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Maps CSS color names to HEX values / awt Color objects.
 */
public class CssColors {
    private static final java.util.Map<String, String> NameToHex;
    private static final java.util.Map<String, String> HexToName;

    static {
        // maintain iteration order, cause HexToName to prefer
        // the first declared value over other aliases.
        // such as gray vs grey.
        Map<String, String> nameToHex = new LinkedHashMap<String, String>();

        nameToHex.put("aliceblue", "#F0F8FF");
        nameToHex.put("antiquewhite", "#FAEBD7");
        nameToHex.put("aqua", "#00FFFF");
        nameToHex.put("aquamarine", "#7FFFD4");
        nameToHex.put("azure", "#F0FFFF");
        nameToHex.put("beige", "#F5F5DC");
        nameToHex.put("bisque", "#FFE4C4");
        nameToHex.put("black", "#000000");
        nameToHex.put("blanchedalmond", "#FFEBCD");
        nameToHex.put("blue", "#0000FF");
        nameToHex.put("blueviolet", "#8A2BE2");
        nameToHex.put("brown", "#A52A2A");
        nameToHex.put("burlywood", "#DEB887");
        nameToHex.put("cadetblue", "#5F9EA0");
        nameToHex.put("chartreuse", "#7FFF00");
        nameToHex.put("chocolate", "#D2691E");
        nameToHex.put("coral", "#FF7F50");
        nameToHex.put("cornflowerblue", "#6495ED");
        nameToHex.put("cornsilk", "#FFF8DC");
        nameToHex.put("crimson", "#DC143C");
        nameToHex.put("cyan", "#00FFFF");
        nameToHex.put("darkblue", "#00008B");
        nameToHex.put("darkcyan", "#008B8B");
        nameToHex.put("darkgoldenrod", "#B8860B");
        nameToHex.put("darkgray", "#A9A9A9");
        nameToHex.put("darkgrey", "#A9A9A9");
        nameToHex.put("darkgreen", "#006400");
        nameToHex.put("darkkhaki", "#BDB76B");
        nameToHex.put("darkmagenta", "#8B008B");
        nameToHex.put("darkolivegreen", "#556B2F");
        nameToHex.put("darkorange", "#FF8C00");
        nameToHex.put("darkorchid", "#9932CC");
        nameToHex.put("darkred", "#8B0000");
        nameToHex.put("darksalmon", "#E9967A");
        nameToHex.put("darkseagreen", "#8FBC8F");
        nameToHex.put("darkslateblue", "#483D8B");
        nameToHex.put("darkslategray", "#2F4F4F");
        nameToHex.put("darkslategrey", "#2F4F4F");
        nameToHex.put("darkturquoise", "#00CED1");
        nameToHex.put("darkviolet", "#9400D3");
        nameToHex.put("deeppink", "#FF1493");
        nameToHex.put("deepskyblue", "#00BFFF");
        nameToHex.put("dimgray", "#696969");
        nameToHex.put("dimgrey", "#696969");
        nameToHex.put("dodgerblue", "#1E90FF");
        nameToHex.put("firebrick", "#B22222");
        nameToHex.put("floralwhite", "#FFFAF0");
        nameToHex.put("forestgreen", "#228B22");
        nameToHex.put("fuchsia", "#FF00FF");
        nameToHex.put("gainsboro", "#DCDCDC");
        nameToHex.put("ghostwhite", "#F8F8FF");
        nameToHex.put("gold", "#FFD700");
        nameToHex.put("goldenrod", "#DAA520");
        nameToHex.put("gray", "#808080");
        nameToHex.put("grey", "#808080");
        nameToHex.put("green", "#008000");
        nameToHex.put("greenyellow", "#ADFF2F");
        nameToHex.put("honeydew", "#F0FFF0");
        nameToHex.put("hotpink", "#FF69B4");
        nameToHex.put("indianred", "#CD5C5C");
        nameToHex.put("indigo", "#4B0082");
        nameToHex.put("ivory", "#FFFFF0");
        nameToHex.put("khaki", "#F0E68C");
        nameToHex.put("lavender", "#E6E6FA");
        nameToHex.put("lavenderblush", "#FFF0F5");
        nameToHex.put("lawngreen", "#7CFC00");
        nameToHex.put("lemonchiffon", "#FFFACD");
        nameToHex.put("lightblue", "#ADD8E6");
        nameToHex.put("lightcoral", "#F08080");
        nameToHex.put("lightcyan", "#E0FFFF");
        nameToHex.put("lightgoldenrodyellow", "#FAFAD2");
        nameToHex.put("lightgray", "#D3D3D3");
        nameToHex.put("lightgrey", "#D3D3D3");
        nameToHex.put("lightgreen", "#90EE90");
        nameToHex.put("lightpink", "#FFB6C1");
        nameToHex.put("lightsalmon", "#FFA07A");
        nameToHex.put("lightseagreen", "#20B2AA");
        nameToHex.put("lightskyblue", "#87CEFA");
        nameToHex.put("lightslategray", "#778899");
        nameToHex.put("lightslategrey", "#778899");
        nameToHex.put("lightsteelblue", "#B0C4DE");
        nameToHex.put("lightyellow", "#FFFFE0");
        nameToHex.put("lime", "#00FF00");
        nameToHex.put("limegreen", "#32CD32");
        nameToHex.put("linen", "#FAF0E6");
        nameToHex.put("magenta", "#FF00FF");
        nameToHex.put("maroon", "#800000");
        nameToHex.put("mediumaquamarine", "#66CDAA");
        nameToHex.put("mediumblue", "#0000CD");
        nameToHex.put("mediumorchid", "#BA55D3");
        nameToHex.put("mediumpurple", "#9370DB");
        nameToHex.put("mediumseagreen", "#3CB371");
        nameToHex.put("mediumslateblue", "#7B68EE");
        nameToHex.put("mediumspringgreen", "#00FA9A");
        nameToHex.put("mediumturquoise", "#48D1CC");
        nameToHex.put("mediumvioletred", "#C71585");
        nameToHex.put("midnightblue", "#191970");
        nameToHex.put("mintcream", "#F5FFFA");
        nameToHex.put("mistyrose", "#FFE4E1");
        nameToHex.put("moccasin", "#FFE4B5");
        nameToHex.put("navajowhite", "#FFDEAD");
        nameToHex.put("navy", "#000080");
        nameToHex.put("oldlace", "#FDF5E6");
        nameToHex.put("olive", "#808000");
        nameToHex.put("olivedrab", "#6B8E23");
        nameToHex.put("orange", "#FFA500");
        nameToHex.put("orangered", "#FF4500");
        nameToHex.put("orchid", "#DA70D6");
        nameToHex.put("palegoldenrod", "#EEE8AA");
        nameToHex.put("palegreen", "#98FB98");
        nameToHex.put("paleturquoise", "#AFEEEE");
        nameToHex.put("palevioletred", "#DB7093");
        nameToHex.put("papayawhip", "#FFEFD5");
        nameToHex.put("peachpuff", "#FFDAB9");
        nameToHex.put("peru", "#CD853F");
        nameToHex.put("pink", "#FFC0CB");
        nameToHex.put("plum", "#DDA0DD");
        nameToHex.put("powderblue", "#B0E0E6");
        nameToHex.put("purple", "#800080");
        nameToHex.put("rebeccapurple", "#663399");
        nameToHex.put("red", "#FF0000");
        nameToHex.put("rosybrown", "#BC8F8F");
        nameToHex.put("royalblue", "#4169E1");
        nameToHex.put("saddlebrown", "#8B4513");
        nameToHex.put("salmon", "#FA8072");
        nameToHex.put("sandybrown", "#F4A460");
        nameToHex.put("seagreen", "#2E8B57");
        nameToHex.put("seashell", "#FFF5EE");
        nameToHex.put("sienna", "#A0522D");
        nameToHex.put("silver", "#C0C0C0");
        nameToHex.put("skyblue", "#87CEEB");
        nameToHex.put("slateblue", "#6A5ACD");
        nameToHex.put("slategray", "#708090");
        nameToHex.put("slategrey", "#708090");
        nameToHex.put("snow", "#FFFAFA");
        nameToHex.put("springgreen", "#00FF7F");
        nameToHex.put("steelblue", "#4682B4");
        nameToHex.put("tan", "#D2B48C");
        nameToHex.put("teal", "#008080");
        nameToHex.put("thistle", "#D8BFD8");
        nameToHex.put("tomato", "#FF6347");
        nameToHex.put("turquoise", "#40E0D0");
        nameToHex.put("violet", "#EE82EE");
        nameToHex.put("wheat", "#F5DEB3");
        nameToHex.put("white", "#FFFFFF");
        nameToHex.put("whitesmoke", "#F5F5F5");
        nameToHex.put("yellow", "#FFFF00");
        nameToHex.put("yellowgreen", "#9ACD32");

        Map<String, String> hexToName = new HashMap<String, String>();

        for (Map.Entry<String, String> entry : nameToHex.entrySet()) {
            String hexValue = entry.getValue();
            if (hexToName.containsKey(hexValue)) continue;
            hexToName.put(hexValue, entry.getKey());
        }

        NameToHex = Collections.unmodifiableMap(nameToHex);
        HexToName = Collections.unmodifiableMap(hexToName);
    }

    private CssColors() {

    }

    /**
     * Attempt to return a # prefixed HEX code for a given CSS color name.
     *
     * @param colorName The color name.
     * @return A hex code, or <b>null</b>.
     */
    public static String getHex(String colorName) {
        return NameToHex.get(colorName);
    }

    /**
     * Attempt to return a {@link java.awt.Color} object for a given CSS color name.
     *
     * @param colorName The color name.
     * @return A {@link java.awt.Color} object, or <b>null</b>.
     */
    public static Color getColor(String colorName) {
        String hex = NameToHex.get(colorName);
        if (hex != null) {
            return Color.decode(hex);
        } else {
            return null;
        }
    }

    /**
     * Return a CSS color name if one exists, or a HEX code with # prefix for a given color.
     * <br><br>
     * See: {@link #describeColorHEX(Color, String)}
     *
     * @param color {@link java.awt.Color}
     * @return CSS color name or HEX code with # prefix
     */
    public static String describeColorHEX(Color color) {
        return describeColorHEX(color, "#");
    }

    /**
     * Return a CSS color name if one exists, or a HEX code with for a given color.
     *
     * @param color     {@link java.awt.Color}
     * @param hexPrefix The hex code prefix to use, typically "#".
     * @return CSS color name or HEX code
     */
    public static String describeColorHEX(Color color, String hexPrefix) {
        return describeColorComponents(color, (hexPrefix == null ? "#" : hexPrefix) + "%02X%02X%02X");
    }

    /**
     * Return a CSS color name if one exists, or return a string formatted with the color components.
     * <br>
     * The format string defaults to <b>"rgb(%d, %d, %d)"</b>, which results in <b>"rgb(r, g, b)"</b>
     * being returned when a CSS color name does not exist for the color.
     * <br><br>
     * See: {@link #describeColorComponents(Color, String)}
     *
     * @param color {@link java.awt.Color}
     * @return CSS color name or formatted string
     */
    public static String describeColorComponents(Color color) {
        return describeColorComponents(color, "rgb(%d, %d, %d)");
    }

    /**
     * Return a CSS color name if one exists, or return a string formatted with the color components.
     * <br>
     * The format string is typically <b>"rgb(%d, %d, %d)"</b>, which results in <b>"rgb(r, g, b)"</b>
     * being returned when a CSS color name does not exist for the color.
     *
     * @param color        {@link java.awt.Color}
     * @param formatString The component format string.
     * @return CSS color name or formatted string
     */
    public static String describeColorComponents(Color color, String formatString) {
        String hex =
                String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());

        String name = HexToName.get("#" + hex);

        if (name != null) {
            return name;
        }

        return String.format(formatString, color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Determine if a CSS color name exists.
     *
     * @param colorName The CSS color name.
     * @return True or False
     */
    public static boolean colorExists(String colorName) {
        return NameToHex.get(colorName.toLowerCase()) != null;
    }
}