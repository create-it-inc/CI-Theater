package it.create.theater.assets.images;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class GraphicsUtils {


    private static Map<String, Color> defaultColors;
	private static List<String> names;

    /**
     * Attempts to generate a color object from the given text including a
     * transparency value.  The value may be a comma delimited rgb value or a
     * default color name such as WHITE, DARK_GRAY, or LIGHT_BLUE.  If the
     * transparency is > 255 or < 0, no transparency will be used.
     *
     * @param value A string indicating a color value.
     * @param transparency An alpha transparency value.
     *
     * @return A color or Color.WHITE if not found.
     * @throws BadColorValue 
     */
    public static Color colorFromText(String value, int transparency) throws BadColorValue {
        if (value != null) {
            if (value.matches("\\s*\\d*\\s*,\\s*\\d*\\s*,\\s*\\d*\\s*")) {
                String[] values = value.split(",");
                int r = new Integer(values[0].trim());
                int g = new Integer(values[1].trim());
                int b = new Integer(values[2].trim());

                if (transparency >= 0 && transparency <= 255) {
                    try {
                        return new Color(r,g,b,transparency);
                    } catch (RuntimeException e) {
                        throw new BadColorValue(e.getMessage());
                    }
                } else {
                    try {
                        return new Color(r,g,b);
                    } catch (RuntimeException e) {
                        throw new BadColorValue(e.getMessage());
                    }
                }
            } else {
                value = value.trim().toUpperCase();
                Map<String, Color> colors = getDefaultColors();
                if (colors.containsKey(value)) {
                    Color color = colors.get(value);
                    // add transparency
                    if (transparency <= 255 && transparency >= 0) {
                        color = new Color(color.getRed(), color.getGreen(), color.getBlue(), transparency);
                    }
                    return color;
                }
            }
        }

        return Color.white;        
    }

    /**
     * Attempts to generate a color object from the given text.  The value may
     * be a comma delimited rgb value or a default color name such as WHITE,
     * DARK_GRAY, or LIGHT_BLUE.
     *
     * @param value A string indicating a color value.
     * @return A color or Color.WHITE if not found.
     * @throws BadColorValue 
     */
    public static Color colorFromText(String value) throws BadColorValue {
        return colorFromText(value, -1);
    }

    /**
     * Returns a mapping of default colors to their field name.
     *
     * @return A map of color objects and their names.
     */
    public static Map<String,Color> getDefaultColors() {
        if (defaultColors == null) {
            defaultColors = new HashMap<String, Color>();
            Field[] fields = Color.class.getFields();
            for (Field field : fields) {
                String name = field.getName();
                if (field.getType().isAssignableFrom(Color.class) && name.matches("[A-Z_]*")) {
                    try {
                        defaultColors.put(name, (Color) field.get(null));
                    } catch (IllegalArgumentException e) {
                        // no way
                    } catch (IllegalAccessException e) {
                        // nothing to do
                    }
                }
            }            
        }
        return defaultColors;
    }

    /**
     * Attempts to find the exact font, or return the given default.  If the 
     * default is not specified, finds a closely matching one.
     *
     * @param namePattern
     * @param style
     * @param size
     * @param defaultFont
     * @return
     */
    public static Font getFont(String namePattern, int style, int size, String defaultFont) {
    	List<String> names = getAvailableFontFamilyNames();
    	for (String fontName: names) {
			if (fontName.equalsIgnoreCase(namePattern)) {
				return new Font(fontName, style, size);
			}
		}
    	// if we got here, then the font name didn't match, return the first
    	// matching one
    	if (defaultFont != null) {
    		return new Font(defaultFont, style, size);
    	}
    	// find one that seems to match
    	for (String fontName: names) {
    		if (fontName.toLowerCase().contains(namePattern.toLowerCase())) {
    			return new Font(fontName, style, size);
    		}
    	}
    	// else, return the first font we can find
    	String name = names.get(0);
    	return new Font(name, style, size);
    }
    
    public static List<String> getAvailableFontFamilyNames() {
    	if (names != null) {
    		return names;
    	}
    	names = Arrays.asList(
    		GraphicsEnvironment.getLocalGraphicsEnvironment()
    			.getAvailableFontFamilyNames());
    	return names;
    }

    

    public static void main(String[] args) throws IOException {
        File output = new File(args[0]);
        List<String> names = GraphicsUtils.getAvailableFontFamilyNames();
        BufferedImage image = new BufferedImage(900, ((names.size() * 32)/2) + 24, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int col = (names.size() / 2);
        int y = 29;
        int x = 20;
        int n = 0;
        for (String name : names) {
        	Font font = new Font(name, Font.TRUETYPE_FONT, 24);
        	g.setFont(font);
        	g.setColor(Color.WHITE);
        	g.drawString(font.getFontName(), x, y);
        	y += 29;
        	if (n == col) {
        		x = 400;
        		y = 25;
        	}
        	n += 1;
		}
        ImageIO.write(image, "png", output);
	}
    

    @SuppressWarnings("serial")
	public static class BadColorValue extends Exception {

        public BadColorValue() {
            super();
            // TODO Auto-generated constructor stub
        }

        public BadColorValue(String message, Throwable cause) {
            super(message, cause);
            // TODO Auto-generated constructor stub
        }

        public BadColorValue(String message) {
            super(message);
            // TODO Auto-generated constructor stub
        }

        public BadColorValue(Throwable cause) {
            super(cause);
            // TODO Auto-generated constructor stub
        }

    }


    
}
