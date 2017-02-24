import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image image;
	public int h;
	public int w;
    
    public ImagePanel() {}

	public void setImage(File im) {
    	try {                
            image = ImageIO.read(im);
            BufferedImage t = ImageIO.read(im);
            h = t.getHeight();
            w = t.getWidth();
            
         } catch (IOException | IllegalArgumentException e) {
              e.printStackTrace();
         }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
    
    public int getWidth() {
    	return w;
    }
    
    public int getHeight() {
    	return h;
    }

}
