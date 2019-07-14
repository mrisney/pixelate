import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ImageUtil {

	public static BufferedImage pixelate(BufferedImage imageToPixelate, int pixelSize) {
		BufferedImage pixelateImage = new BufferedImage(imageToPixelate.getWidth(), imageToPixelate.getHeight(),
				imageToPixelate.getType());

		for (int y = 0; y < imageToPixelate.getHeight(); y += pixelSize) {
			for (int x = 0; x < imageToPixelate.getWidth(); x += pixelSize) {
				BufferedImage croppedImage = getCroppedImage(imageToPixelate, x, y, pixelSize, pixelSize);
				Color dominantColor = getDominantColor(croppedImage);
				for (int yd = y; (yd < y + pixelSize) && (yd < pixelateImage.getHeight()); yd++) {
					for (int xd = x; (xd < x + pixelSize) && (xd < pixelateImage.getWidth()); xd++) {
						pixelateImage.setRGB(xd, yd, dominantColor.getRGB());
					}
				}
			}
		}

		return pixelateImage;
	}

	public static BufferedImage getCroppedImage(BufferedImage image, int startX, int startY, int width, int height) {
		if (startX < 0)
			startX = 0;
		if (startY < 0)
			startY = 0;
		if (startX > image.getWidth())
			startX = image.getWidth();
		if (startY > image.getHeight())
			startY = image.getHeight();
		if (startX + width > image.getWidth())
			width = image.getWidth() - startX;
		if (startY + height > image.getHeight())
			height = image.getHeight() - startY;
		return image.getSubimage(startX, startY, width, height);
	}

	public static Color getDominantColor(BufferedImage image) {
		Map<Integer, Integer> colorCounter = new HashMap<>(100);
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int currentRGB = image.getRGB(x, y);
				int count = colorCounter.getOrDefault(currentRGB, 0);
				colorCounter.put(currentRGB, count + 1);
			}
		}
		return getDominantColor(colorCounter);
	}

	@SuppressWarnings("unchecked")
	private static Color getDominantColor(Map<Integer, Integer> colorCounter) {
		int dominantRGB = ((Entry<Integer, Integer>) colorCounter.entrySet().stream().max(new EntryComparator()).get())
				.getKey();
		return new Color(dominantRGB);
	}
}

@SuppressWarnings("rawtypes")
class EntryComparator implements Comparator {

	@SuppressWarnings("unchecked")
	@Override
	public int compare(Object o1, Object o2) {
		Entry<Integer, Integer> entry1 = (Map.Entry<Integer, Integer>) o1;
		Entry<Integer, Integer> entry2 = (Map.Entry<Integer, Integer>) o2;
		return (entry1.getValue() > entry2.getValue() ? 1 : -1);
	}
}

