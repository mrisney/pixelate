import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class PixelateUI {
	public static void main(String[] args) {
        JFrame frame = new JFrame("Pixelating");
        frame.add(new PixelatePanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static class PixelatePanel extends JPanel {
        int width;
        int height;
        String fileName = "image.jpg";
        BufferedImage image;
        BufferedImage pixelatedImage;

        public final static Rectangle pixelateSection = new Rectangle(200, 450, 200, 600);

        public static final int PIXEL_SIZE = 10;


        public PixelatePanel() {
            image = getImage();
            width = image.getWidth();
            height = image.getHeight();

            pixelatedImage = pixelateImage(image);

        }

        private BufferedImage pixelateImage(BufferedImage bufferedImage) {
            Raster src = bufferedImage.getData();
            WritableRaster dest = src.createCompatibleWritableRaster();

            bufferedImage.copyData(dest);

            for (int y = pixelateSection.y; y < pixelateSection.y + pixelateSection.getHeight(); y += PIXEL_SIZE) {
                for (int x = pixelateSection.x; x < pixelateSection.x + pixelateSection.getWidth(); x += PIXEL_SIZE) {

                    double[] pixel = new double[3];
                    pixel = src.getPixel(x, y, pixel);

                    for (int yd = y; (yd < y + PIXEL_SIZE) && (yd < dest.getHeight()); yd++) {
                        for (int xd = x; (xd < x + PIXEL_SIZE) && (xd < dest.getWidth()); xd++) {
                            dest.setPixel(xd, yd, pixel);
                        }
                    }
                }
            }

            bufferedImage.setData(dest);

            return bufferedImage;
        }

        private BufferedImage getImage() {
            BufferedImage image = null;
            try {
                image = ImageIO.read(new File(fileName));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return image;
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
    }
}
