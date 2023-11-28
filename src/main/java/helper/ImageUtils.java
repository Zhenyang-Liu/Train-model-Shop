package helper;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    public static String toBase64(File file){
        byte[] bytes;
        try{
            FileInputStream reader = new FileInputStream(file);
            bytes = new byte[(int)file.length()];
            reader.read(bytes);
            reader.close();
            return Base64.getEncoder().encodeToString(scaleImage(bytes, 256, 150));
        }catch(FileNotFoundException e){
            System.out.println("Could not find file specified!");
        }catch(IOException e){
            System.out.println("IO EXCEPTION");
            e.printStackTrace();
        }
        return "";
    }

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
            System.out.println("IO Exception while scaling image :D");
            e.printStackTrace();
        }
        return new byte[]{};
    }
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
