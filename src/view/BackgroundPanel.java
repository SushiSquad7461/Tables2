package src.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import javax.imageio.*;

public class BackgroundPanel extends JPanel
{
    BufferedImage image;
    Paint textureBackground;
 
    public BackgroundPanel()
    {
        loadImage();
        Rectangle r = new Rectangle(0, 0, image.getWidth(), image.getHeight());
        textureBackground = new TexturePaint(image, r);
    }
 
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        int width = getWidth();
        int height = getHeight();
        // option one - centered image
        int imageWidth = image.getWidth(this);
        int imageHeight = image.getHeight(this);
        int x = (width - imageWidth)/2;
        int y = (height - imageHeight)/2;
        g2.drawImage(image, null, x, y);
        // option 2 - tiled background
//        g2.setPaint(textureBackground);
//        g2.fillRect(0, 0, width, height);
    }
 
    private void loadImage()
    {
        String fileName = "/Users/nitya/java/tables_copy/assets/images/img1.png";
        try
        {
            URL url = getClass().getResource(fileName);
            image = ImageIO.read(url);
        }
        catch(MalformedURLException mue)
        {
            System.out.println(mue.getMessage());
        }
        catch(IOException ioe)
        {
            System.out.println(ioe.getMessage());
        }
    }
}
