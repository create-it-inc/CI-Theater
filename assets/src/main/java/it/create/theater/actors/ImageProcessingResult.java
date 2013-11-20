package it.create.theater.actors;

import java.io.File;
import java.io.Serializable;

public class ImageProcessingResult implements Serializable {

	private static final long serialVersionUID = -2041402269535152039L;

	private File file;

	public ImageProcessingResult(File result) {
		this.file = result;
	}

	public File getFile() {
		return file;
	}
}
