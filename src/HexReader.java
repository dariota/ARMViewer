import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class HexReader {

	private static BufferedImage crest;
	private static JFrame display = new JFrame();
	// This was correct when I did it, but may have changed, so you may need to
	// tweak this and recompile
	private static int width = 120, height = 151;
	private static int scale = 4;
	private static long lastModified = -1;
	private static ImagePanel result = new ImagePanel();

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("File path argument required.");
			return;
		} else if (args.length == 2) {
			scale = Integer.parseInt(args[1]);
		}

		File source = new File(args[0]);
		display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		display.add(result);

		while (true) {
			// poll for file changes - should be done with a WatchKey or similar
			// instead, but this works fine
			if (source.lastModified() == lastModified) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
			} else {				
				lastModified = source.lastModified();

				Scanner file = new Scanner(source);
				file.nextLine();

				int[] colours = new int[width * height];
				crest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

				int index = 0;

				while (file.hasNextLine()) {
					StringBuilder sb = new StringBuilder();
					char[] nextLine = file.nextLine().replaceFirst(".{9}", "").toCharArray();
					for (int i = 0; i < nextLine.length/8; i++) {
						// account for the endianness of memory, would be better
						// done with masking/shifting
						sb.append(nextLine[4 + 8 * i]);
						sb.append(nextLine[5 + 8 * i]);
						sb.append(nextLine[2 + 8 * i]);
						sb.append(nextLine[3 + 8 * i]);
						sb.append(nextLine[0 + 8 * i]);
						sb.append(nextLine[1 + 8 * i]);
						// parse the hex value
						colours[index++] = Integer.parseInt(sb.toString(), 16);

						// reuse the StringBuilder
						sb.delete(0, 6);
					}
				}

				crest.setRGB(0, 0, width, height, colours, 0, width);
				crest = scaleUp(crest);
				display(crest);
				file.close();
			}
		}
	}

	public static void display(BufferedImage i) throws IOException {
		File f = new File("crest.png");
		f.delete();
		ImageIO.write(crest, "PNG", f);
		result.setImage(f);
		Dimension d = new Dimension(result.w + 16, result.h + 40);
		display.setMinimumSize(d);
		display.pack();
		display.repaint();
		display.setVisible(true);
	}

	// Nearest neighbour scaling. I'm unsure why I did this rather than use some
	// standard call in BufferedImage.
	public static BufferedImage scaleUp(BufferedImage i) {
		int[] colours = new int[i.getWidth() * scale * i.getHeight() * scale];
		int w = i.getWidth();
		int h = i.getHeight();
		for (int j = 0; j < h * scale; j += scale) {
			for (int k = 0; k < w * scale; k += scale) {
				int c = i.getRGB(k/scale, j/scale);
				for (int l = 0; l < scale; l++) {
					for (int m = 0; m < scale; m++) {
						colours[j * w * scale + k + l + m * w * scale] = c;
					}
				}
			}
		}
		i = new BufferedImage(i.getWidth() * scale, i.getHeight() * scale, i.getType());
		i.setRGB(0, 0, i.getWidth(), i.getHeight(), colours, 0, i.getWidth());
		return i;
	}

}
