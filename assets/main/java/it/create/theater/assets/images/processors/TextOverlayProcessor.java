package it.create.theater.assets.images.processors;

import it.create.theater.assets.images.GraphicsUtils;
import it.create.theater.assets.images.GraphicsUtils.BadColorValue;
import it.create.theater.assets.images.ImageProcessor;
import it.create.theater.assets.images.providers.URLImageProvider;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TextOverlayProcessor implements ImageProcessor {

	private static final long serialVersionUID = 3388476944648387627L;

	public static final String DEFAULT_FONT = "Lucida Sans";

	public static final int POSITION_BOTTOM = 0;
	public static final int POSITION_TOP = 1;

	public static final String DEFAULT_BACKGROUND_COLOR = "BLACK";
	public static final String DEFAULT_FONT_COLOR = "WHITE";


	private String fontName = DEFAULT_FONT;

	/** 
	 * A font size of -1 will indicate that a proporational size font will be used
	 * based on the height of the image.
	 */
	private int fontSize = -1;

	private int backgroundTransparency = 150;
	
	private int fontTransparency = 255;

	private String backgroundColor = DEFAULT_BACKGROUND_COLOR;

	private String fontColor = DEFAULT_FONT_COLOR;

	private String text;


	// established for processing
	private transient int imageHeight;
	private transient int imageWidth;

	private transient Color fontAwtColor = Color.WHITE;

	private transient Font font;

	private transient FontMetrics fontMetrics;

	
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public BufferedImage process(BufferedImage image) {
		// if there is no text, then nothing to do
		if ("".equals(this.text) || this.text == null) {
			return null;
		}

		imageHeight = image.getHeight();
		imageWidth = image.getWidth();
		// if the image is too small, don't do anything
		if (imageWidth < 5 || imageHeight < 5) {
			return null;
		}

		Graphics2D g2 = image.createGraphics();
		try {
			init(image, g2);
			drawBox(g2);
			drawText(g2);
		} finally {
			g2.dispose();
		}
		return image;
	}

	private void init(BufferedImage image, Graphics2D g2) {
		font = establishFont(g2, image, this.text);
		fontMetrics = g2.getFontMetrics(font);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	}

	private void drawBox(Graphics2D g2) {
		g2.setPaint(getBackgroundColor());
		int height = getBoxHeight();
		int width = imageWidth;
		int x = 0;
		int y = imageHeight - height;
		g2.fillRect(x, y, width, height);
	}

	private void drawText(Graphics2D g2) {
		try {
			GraphicsUtils.colorFromText(fontColor, fontTransparency);
		} catch (BadColorValue e) {
			fontAwtColor = Color.WHITE;
		}
		g2.setColor(fontAwtColor);
		g2.setFont(font);
		int x = (fontMetrics.getHeight() / 6);
		int fontHeight = fontMetrics.getHeight();
		int y = imageHeight - (fontHeight/2) + (fontHeight/4);
		g2.drawString(text, x, y);
	}

	/**
	 * Get a font that will fit all the text (on one line).
	 *
	 * @param image
	 * @param text
	 * @return
	 */
	private Font establishFont(Graphics2D g2, BufferedImage image, String text) {
		if (this.fontSize > 0) {
			font = GraphicsUtils.getFont(fontName, Font.BOLD, this.fontSize, null);
			return font;
		}

		// otherwise, see if we can figure out the best size of the font
		int height = image.getHeight();
		int width = image.getWidth();
		// this is our ideal font size
		int fontHeight = height / 8; // 16.7%
		Font font = GraphicsUtils.getFont(fontName, Font.BOLD, fontHeight, null);

		FontMetrics metrics = g2.getFontMetrics(font);
		int padding = metrics.getHeight() / 6;

		// some reason, the padding seems to end up being too much
		int maxWidth = width - (padding + (padding/2));

		boolean fit = false;
		while (!fit) {
			int stringWidth = metrics.stringWidth(text);
			if (stringWidth > (maxWidth)) {
				// font is too big for text to fit, adjust down and try again
				// the ratio of size
				double ratio = (double)maxWidth / (double)stringWidth;
				int adjustedHeight = (int) (ratio * fontHeight);
				if (adjustedHeight < fontHeight) {
					fontHeight = adjustedHeight;
				} else {
					// something is wonky, just try reducing some
					fontHeight -= 10;
				}
				font = GraphicsUtils.getFont(fontName, Font.BOLD, fontHeight, null);
				metrics = g2.getFontMetrics(font);
				padding = metrics.getHeight() / 6;
			} else {
				fit = true;
				this.font = font;
				this.fontMetrics = metrics;
			}
		}
		// with the given text, see if it will actually fit
		
		return font;
	}
	private int getBoxHeight() {
		int padding = (fontMetrics.getHeight() / 6);
		return fontMetrics.getHeight() + padding;
	}

	private Color getBackgroundColor() {
		try {
			return GraphicsUtils.colorFromText(backgroundColor, backgroundTransparency);
		} catch (BadColorValue e) {
			return Color.DARK_GRAY;
		}
	}

	public static void main(String[] args) throws IOException {
		{
			TextOverlayProcessor op = new TextOverlayProcessor();
			op.setText("Ultimate Determination");
			URLImageProvider i = new URLImageProvider("https://i1.ytimg.com/vi/Mw_Jnn_Y5iA/hqdefault.jpg");
			BufferedImage image = i.getImage();
			op.process(image);
			File output = new File("/tmp/small.jpg");
			ImageIO.write(image, "jpg", output);
			Runtime.getRuntime().exec("open " + output.getAbsolutePath());
		}
		{
			TextOverlayProcessor op = new TextOverlayProcessor();
			op.setText("Ultimate Determination");
			URLImageProvider i = new URLImageProvider("https://i1.ytimg.com/vi/Mw_Jnn_Y5iA/mqdefault.jpg");
			BufferedImage image = i.getImage();
			op.process(image);
			File output = new File("/tmp/medium.jpg");
			ImageIO.write(image, "jpg", output);
			Runtime.getRuntime().exec("open " + output.getAbsolutePath());
		}
		{
			TextOverlayProcessor op = new TextOverlayProcessor();
			op.setText("Ultimate Determination");
			URLImageProvider i = new URLImageProvider("https://i1.ytimg.com/vi/Mw_Jnn_Y5iA/default.jpg");
			BufferedImage image = i.getImage();
			op.process(image);
			File output = new File("/tmp/small.jpg");
			ImageIO.write(image, "jpg", output);
			Runtime.getRuntime().exec("open " + output.getAbsolutePath());
		}
	}
}

// ImageProcess
//   ImageProvider
//     -> return
//   ImageProcessor
//     process image overlay
//     -> return BufferedImage
//   ImageResultHandler
//     write image to file
//     upload file to S3
//     -> return s3 id
//   
