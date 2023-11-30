package helper;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Base64;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import DAO.ProductDAO;
import model.Product;


public class ImageUtils {

    /**
     * Manages image icons for trains so that they are not constantly created / destroyed
     */
    public class ResourceManager{

        private static ImageIcon defaultImageIcon;
        private static HashMap<Integer, ImageIcon> resources;

        /**
         * Initialises the default image and the map containing the resources
         */
        public static void Init(){
            // purely to use getClass
            Product p = new Product();
            resources = new HashMap<>();
            URI defaultImage = null;
            try {
                defaultImage = p.getClass().getResource("/images/tgv.jpeg").toURI();
            } catch (URISyntaxException e) {
                Logging.getLogger().info("Could not find default image when initialising the resource manager");
            }
            defaultImageIcon = imageFromPath(defaultImage);
        }

        /**
         * 
         * @param productID
         */
        public static void updateImageForProduct(int productID){
            if(resources == null)
                Init();
            String base64 = ProductDAO.getImageForProduct(productID);
            // Catch nullptr as this would mean image not created successfully and hence we can just return default image
            try{
                resources.put(productID, imageToIcon(base64));
            }catch(NullPointerException e){
                Logging.getLogger().warning("Could not update image for product " + productID + " as image is null!\nStacktrace: " + e.getMessage());
            }
        }

        public static ImageIcon getProductImage(int productID){
            if(resources == null)
                Init();
            if (!resources.containsKey(productID) && productID != -1)
                updateImageForProduct(productID);
            return resources.getOrDefault(productID, defaultImageIcon);
        }
    }
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

    /**
     * Takes a resource URI and returns the image at the path as a java.swing.ImageIcon
     * 
     * @param path URI 
     * @return
     */
    public static ImageIcon imageFromPath(URI path){
        return imageToIcon(toBase64(new File(path)));
    }

    /**
     * Zoom and crop the image to fit the specified size.
     *
     * @param originalIcon The original image icon to be processed.
     * @param targetWidth The target width.
     * @param targetHeight The target height.
     * @return The scaled and possibly cropped image icon.
     */
    public static ImageIcon resizeAndFillImageIcon(ImageIcon originalIcon, int targetWidth, int targetHeight) {
        Image originalImage = originalIcon.getImage();


        double aspectRatio = (double) originalImage.getWidth(null) / originalImage.getHeight(null);
        double targetRatio = (double) targetWidth / targetHeight;

        int newWidth;
        int newHeight;

        if (aspectRatio > targetRatio) {
            newHeight = targetHeight;
            newWidth = (int) (newHeight * aspectRatio);
        } else {
            newWidth = targetWidth;
            newHeight = (int) (newWidth / aspectRatio);
        }

        Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        BufferedImage croppedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = croppedImage.createGraphics();

        int x = (targetWidth - newWidth) / 2;
        int y = (targetHeight - newHeight) / 2;
        g2d.drawImage(resizedImage, x, y, null);
        g2d.dispose();

        return new ImageIcon(croppedImage);
    }
}
