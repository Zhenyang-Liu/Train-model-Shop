package helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageUtils {
    /**
     * Converts a given file to a base64 encoded string
     * 
     * @param file a file :D
     * @return a base64 encoded string representing the file
     */
    public static String toBase64(File file){
        byte[] bytes;
        try{
            FileInputStream reader = new FileInputStream(file);
            bytes = new byte[(int)file.length()];
            reader.read(bytes);
            reader.close();
            return Base64.getEncoder().encodeToString(scaleImage(bytes, 256, 150));
        }catch(FileNotFoundException e){
           Logging.getLogger().warning("Could not find file specified: " + file.getAbsolutePath());
        }catch(IOException e){
            Logging.getLogger().warning("IOException thrown while reading file at " + file.getAbsolutePath() + "\nStacktrace: " + e.getMessage());
        }
        return "";
    }

    /**
     * Scales a given image in byte array form
     * 
     * @param imageData the image in byte array form
     * @param width desired width of the image
     * @param height desired height of the image
     * @return
     */
    public static byte[] scaleImage(byte[] imageData, int width, int height){
        ByteArrayInputStream in = new ByteArrayInputStream(imageData);
        try{
            BufferedImage image = ImageIO.read(in);
            Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0,0,0), null);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write(imageBuff, "jpg", buffer);
            return buffer.toByteArray();
        }catch(IOException e){
            Logging.getLogger().warning("Error while attempting to scale image to dimensions (" + width + ", " + height + ")" + "\nStacktrace: " + e.getMessage());
        }
        return new byte[]{};
    }

    /**
     * Converts an image in base64 format to an java swing ImageIcon
     * @param base64Image image to convert
     * @return
     */
    public static ImageIcon imageToIcon(String base64Image){
        try{
            byte[] imageBytes = Base64.getDecoder().decode(base64Image.getBytes("UTF-8"));
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            return new ImageIcon(image);
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
