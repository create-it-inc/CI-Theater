package it.create.theater.assets.images.providers;

import it.create.theater.assets.images.ImageProvider;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class URLImageProvider implements ImageProvider {

	private static final long serialVersionUID = -1830056716472298225L;

	private String url;

	public URLImageProvider(String url) throws MalformedURLException {
		new URL(url); // just to validate it

		this.url = url.trim();
	}

	@Override
	public BufferedImage getImage() throws IOException {
		return ImageIO.read(new URL(this.url));
	}

	@Override
	public String getImageType() {
		String[] values = this.url.split("\\?");
		return values[0].substring(values[0].lastIndexOf('.') + 1);
	}

	@Override
	public String getBaseFileName() {
		String[] values = this.url.split("\\?");
		return values[0].substring(values[0].lastIndexOf('/') + 1);
	}
}
