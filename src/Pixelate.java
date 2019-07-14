import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Pixelate {

	public static void main(String[] args) {
		int PIXEL_SIZE = 10;
		BufferedImage img;
		try {
			img = ImageIO.read(new File("./image.jpg"));
			BufferedImage imagePixelated = ImageUtil.pixelate(img, PIXEL_SIZE);
			ImageIO.write(imagePixelated, "jpg", new File("./image-pixelated.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
