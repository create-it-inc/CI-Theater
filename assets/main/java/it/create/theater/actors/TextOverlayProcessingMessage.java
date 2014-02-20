package it.create.theater.actors;

import it.create.theater.assets.images.ImageProcessor;
import it.create.theater.assets.images.ImageProvider;
import it.create.theater.assets.images.processors.TextOverlayProcessor;

public class TextOverlayProcessingMessage extends ImageProcessingMessage {

	private static final long serialVersionUID = 4721300918129847738L;

	private String text;

	public TextOverlayProcessingMessage(ImageProvider imageProvider) {
		super(imageProvider);
	}
	

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public Class<? extends ImageProcessor> getImageProcessorType() {
		return TextOverlayProcessor.class;
	}

	
}
