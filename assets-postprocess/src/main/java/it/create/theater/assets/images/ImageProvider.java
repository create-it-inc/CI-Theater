package it.create.theater.assets.images;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

public interface ImageProvider extends Serializable {

	public BufferedImage getImage() throws IOException;

	/**
	 * Essentially, the file name suffix. 
	 * @return
	 */
	public String getImageType();

	public String getBaseFileName();
}
