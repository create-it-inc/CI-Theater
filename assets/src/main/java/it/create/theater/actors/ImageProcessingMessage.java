package it.create.theater.actors;

import it.create.theater.assets.images.ImageProcessor;
import it.create.theater.assets.images.ImageProvider;

import java.io.Serializable;

public abstract class ImageProcessingMessage implements Serializable {

	private static final long serialVersionUID = 7560395393767898208L;

	private ImageProvider imageProvider;

	public ImageProcessingMessage(ImageProvider imageProvider) {
		this.imageProvider = imageProvider;
	}

	public ImageProvider getImageProvider() {
		return imageProvider;
	}

	
	public abstract Class<? extends ImageProcessor> getImageProcessorType();
}
