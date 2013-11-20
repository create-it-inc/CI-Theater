package it.create.theater.actors;

import static akka.dispatch.Futures.future;
import it.create.theater.assets.images.ImageProcessor;
import it.create.theater.assets.images.ImageProvider;
import it.create.theater.assets.images.processors.TextOverlayProcessor;
import it.create.theater.assets.images.providers.URLImageProvider;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.apache.commons.beanutils.BeanUtils;

import scala.concurrent.Future;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.dispatch.OnSuccess;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ImageProcessingActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public static Props mkProps() {
		return new Props(ImageProcessingActor.class);
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof ImageProcessingMessage) {
			ImageProcessingMessage ipMessage = (ImageProcessingMessage) message;
			File file = execute(ipMessage);
			ImageProcessingResult result = new ImageProcessingResult(file);
			try {
				getSender().tell(result, getSelf());
			} catch (Exception e) {
				getSender().tell(new akka.actor.Status.Failure(e), getSelf());
			}
		} else {
			unhandled(message);
		}
	}

	private File execute(ImageProcessingMessage message) throws Exception {
		ImageProcessor processor = message.getImageProcessorType().newInstance();
		ImageProvider provider = message.getImageProvider();
		BeanUtils.copyProperties(processor, message);
		BufferedImage image = provider.getImage();
		processor.process(image);

		File tempFile = File.createTempFile(provider.getBaseFileName(), "." + provider.getImageType());
		ImageIO.write(image, provider.getImageType(), tempFile);
		return tempFile;
	}

	public static void main(String[] args) throws Exception {
		String[] urls = new String[] {
				"https://i1.ytimg.com/vi/Mw_Jnn_Y5iA/hqdefault.jpg",
				"https://i1.ytimg.com/vi/Mw_Jnn_Y5iA/mqdefault.jpg",
				"https://i1.ytimg.com/vi/Mw_Jnn_Y5iA/default.jpg"
				};

		ActorSystem system = ActorSystem.create("ImageProcessingSytem");
		List<Future<File>> futures = new ArrayList<Future<File>>();

		for (final String url : urls) {
			Future<File> future = future(new Callable<File>() {
				public File call() {
					try {
						URLImageProvider provider = new URLImageProvider(url);
						TextOverlayProcessor processor = new TextOverlayProcessor();
						processor.setText("Ultimate Concentration");
						BufferedImage image = provider.getImage();
						processor.process(image);
						File output = File.createTempFile(provider.getBaseFileName(), "." + provider.getImageType(), new File("/tmp"));
						ImageIO.write(image, provider.getImageType(), output);
						return output;
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}, system.dispatcher());
			futures.add(future);
		}
		
		for (Future<File> future : futures) {
			future.onSuccess(new OnSuccess<File>() {
				@Override
				public void onSuccess(File file) throws Throwable {
					System.out.println(file.getAbsolutePath());
					Runtime.getRuntime().exec("open " + file.getAbsolutePath());
				}
			}, system.dispatcher());
		}

		// actor usage
//		ActorRef actor = system.actorOf(ImageProcessingActor.mkProps(), "ImageProcessingActor");
//		List<Future> futures = new ArrayList<Future>();
//		for (String url : urls) {
//			ImageProvider provider = new URLImageProvider(url);
//			TextOverlayProcessingMessage message = new TextOverlayProcessingMessage(provider);
//			message.setText("Ultimate Concentration");
//			Future<Object> future = ask(actor, message, 20000);
//			futures.add(future);
//		}
//		for (Future future : futures) {
//			ImageProcessingResult result = Await.result(future, Duration.create(30, TimeUnit.SECONDS));
//			System.out.println(result.getFile());
//		}
		
	}

}
