package it.create.theater.assets.images.providers;

import it.create.theater.assets.images.ImageProvider;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileImageProvider implements ImageProvider {

	private static final long serialVersionUID = -1318687519789428424L;
	
	private String path;

	private File file;

	public FileImageProvider(String path) {
		this.path = path.trim();
		this.file = new File(this.path);
	}
	public FileImageProvider(File file) {
		this.path = file.getAbsolutePath();
		this.file = file;
	}

	@Override
	public BufferedImage getImage() throws IOException {
		return ImageIO.read(this.file);
	}

	@Override
	public String getImageType() {
		int i = this.path.lastIndexOf(".");
		return this.path.substring(i+1);
	}

	@Override
	public String getBaseFileName() {
		int i = this.path.lastIndexOf('.');
		return this.path.substring(0, i);
	}

}
