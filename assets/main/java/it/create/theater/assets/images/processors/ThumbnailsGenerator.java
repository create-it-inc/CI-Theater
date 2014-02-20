package it.create.theater.assets.images.processors;

import it.create.theater.assets.images.GraphicsUtils;
import it.create.theater.assets.images.GraphicsUtils.BadColorValue;
import it.create.theater.assets.images.ImageProcessor;
import it.create.theater.assets.images.providers.URLImageProvider;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ThumbnailsGenerator implements ImageProcessor {

	private int width = 320;
	private int height = 240;
	private String backgroundColor = "BLACK";
	private Color gBackgroundColor;

	private boolean crop = true;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public ThumbnailsGenerator() {
	}

	@Override
	public BufferedImage process(BufferedImage image) {
		init();

		BufferedImage targetImage = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_BGR);
		Graphics2D g2 = targetImage.createGraphics();
		g2.setBackground(this.gBackgroundColor);
		g2.fillRect(0, 0, this.width, this.height);
		try {
			rescaleImage(g2, image, targetImage);
		} finally {
			g2.dispose();
		}

		return targetImage;
	}

	private void init() {
		try {
			gBackgroundColor = GraphicsUtils.colorFromText(backgroundColor);
		} catch (BadColorValue e) {
			gBackgroundColor = Color.BLACK;
		}

	}

    private void rescaleImage(Graphics2D g2, BufferedImage image, BufferedImage targetImage) {
        float origHeight = image.getHeight();
        float origWidth = image.getWidth();

        float targetHeight = this.height;
        float targetWidth = this.width;

        float resizeRatio = ratio(origHeight, origWidth);

        float scaledHeight = origHeight * resizeRatio;
        float scaledWidth = origWidth * resizeRatio;

        // figure out the position
        int x = (int)((targetWidth - scaledWidth) / 2);
        int y = (int)((targetHeight - scaledHeight) / 2);
        //g2.setComposite(AlphaComposite.Src);
        g2.drawImage(image, x, y, (int)scaledWidth, (int)scaledHeight, null);

    }

	private float ratio(float origHeight, float origWidth) {
		float widthRatio = this.width / origWidth;
		float heightRatio = this.height / origHeight;

		float ratio = 0;
		// if cropping, then take the bigger ratio
		if (crop) {
			ratio = (heightRatio > widthRatio) ? heightRatio : widthRatio; 
		} else {
			ratio = (heightRatio > widthRatio) ? widthRatio : heightRatio;
		}
		return ratio;
	}

	public static void main(String[] args) throws IOException {
		URLImageProvider provider = new URLImageProvider("https://secure-b.vimeocdn.com/ts/455/380/455380690_960.jpg");
		BufferedImage image = provider.getImage();
		File inFile = new File("/tmp/mediumin.jpg");
		ImageIO.write(image, provider.getImageType(), inFile);
		Runtime.getRuntime().exec("open " + inFile.getAbsolutePath());
		ThumbnailsGenerator thumbnailsGenerator = new ThumbnailsGenerator();
		thumbnailsGenerator.crop = true;
		BufferedImage thumbnail = thumbnailsGenerator.process(image);
		File outFile = new File("/tmp/medium.jpg");
		ImageIO.write(thumbnail, provider.getImageType(), outFile);
		Runtime.getRuntime().exec("open " + outFile.getAbsolutePath());
	}
}
