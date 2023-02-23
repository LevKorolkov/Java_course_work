package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;

public class TextConverter implements TextGraphicsConverter {
    private int maxWidth = Integer.MAX_VALUE;
    private int maxHeight = Integer.MAX_VALUE;
    private double maxRatio = Double.MAX_VALUE;
    private TextColorSchema colorSchema = new ColorScheme();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));
        controlImgRatio(img.getHeight(), img.getWidth());

        int newHeight = newHeight(img.getWidth(), img.getHeight());
        int newWidth = newWidth(img.getWidth(), img.getHeight());

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D graphics = bwImg.createGraphics();

        ImageIO.write(bwImg, "png", new File("out.png"));

        graphics.drawImage(scaledImage, 0, 0, null);

        StringBuilder sb = new StringBuilder();

        WritableRaster bwRaster = bwImg.getRaster();
        for (int w = 0; w < newWidth; w++) {
            for (int h = 0; h < newHeight; h++) {
                int color = bwRaster.getPixel(h, w, new int[3]) [0];
                char c = colorSchema.convert(color);
                sb.append(c).append(c);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public void controlImgRatio(int width, int height) throws BadImageSizeException {
        double highPicture = (double) height / width;
        double widthPicture = (double) width / height;
        if (width > height && widthPicture > maxRatio) {
            throw new BadImageSizeException(widthPicture, maxRatio);
        }
        if (height > width && highPicture > maxRatio) {
            throw new BadImageSizeException(highPicture, maxRatio);
        }
    }

    public int newHeight(int width, int height) {
        double widthRatio = (double) width / maxWidth;
        int newHeight;
        if (height >= width) {
            newHeight = Math.min(maxHeight, height);
        } else {
            BigDecimal num1 = new BigDecimal(Double.toString(widthRatio));
            BigDecimal num2 = new BigDecimal(height);
            BigDecimal dividingResult = num2.divide(num1, 2, RoundingMode.HALF_UP);
            newHeight = dividingResult.intValue();
        }
        return newHeight;
    }

    public int newWidth(int height, int width) {
        double heightRatio = (double) height / maxHeight;
        int newWidth;
        if (width >= height) {
            newWidth = Math.min(maxWidth, width);
        } else {
            BigDecimal num1 = new BigDecimal(Double.toString(heightRatio));
            BigDecimal num2 = new BigDecimal(width);
            BigDecimal dividingResult = num2.divide(num1, 2, RoundingMode.HALF_UP);
            newWidth = dividingResult.intValue();
        }
        return newWidth;
    }

    @Override
    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    @Override
    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.colorSchema = schema;
    }
}
