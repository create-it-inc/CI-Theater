package it.create.theater.assets.images;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public interface ImageProcessor extends Serializable {

	public void process(BufferedImage image);

}
