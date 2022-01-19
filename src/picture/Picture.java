package picture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class that encapsulates and provides a simplified interface for manipulating an image. The
 * internal representation of the image is based on the RGB direct colour model.
 */
public class Picture {

  /** The internal image representation of this picture. */
  private final BufferedImage image;

  /** Construct a new (blank) Picture object with the specified width and height. */
  public Picture(int width, int height) {
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
  }

  /** Construct a new Picture from the image data in the specified file. */
  public Picture(String filepath) {
    try {
      image = ImageIO.read(new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Test if the specified point lies within the boundaries of this picture.
   *
   * @param x the x co-ordinate of the point
   * @param y the y co-ordinate of the point
   * @return <tt>true</tt> if the point lies within the boundaries of the picture, <tt>false</tt>
   *     otherwise.
   */
  public boolean contains(int x, int y) {
    return x >= 0 && y >= 0 && x < getWidth() && y < getHeight();
  }

  /**
   * Returns true if this Picture is graphically identical to the other one.
   *
   * @param other The other picture to compare to.
   * @return true iff this Picture is graphically identical to other.
   */
  @Override
  public boolean equals(Object other) {
    if (other == null) {
      return false;
    }
    if (!(other instanceof Picture)) {
      return false;
    }

    Picture otherPic = (Picture) other;

    if (image == null || otherPic.image == null) {
      return image == otherPic.image;
    }
    if (image.getWidth() != otherPic.image.getWidth()
        || image.getHeight() != otherPic.image.getHeight()) {
      return false;
    }

    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        if (image.getRGB(i, j) != otherPic.image.getRGB(i, j)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Return the height of the <tt>Picture</tt>.
   *
   * @return the height of this <tt>Picture</tt>.
   */
  public int getHeight() {
    return image.getHeight();
  }

  /**
   * Return the colour components (red, green, then blue) of the pixel-value located at (x,y).
   *
   * @param x x-coordinate of the pixel value to return
   * @param y y-coordinate of the pixel value to return
   * @return the RGB components of the pixel-value located at (x,y).
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *     the boundaries of this picture.
   */
  public Color getPixel(int x, int y) {
    int rgb = image.getRGB(x, y);
    return new Color((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff);
  }

  /**
   * Return the width of the <tt>Picture</tt>.
   *
   * @return the width of this <tt>Picture</tt>.
   */
  public int getWidth() {
    return image.getWidth();
  }

  @Override
  public int hashCode() {
    if (image == null) {
      return -1;
    }
    int hashCode = 0;
    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        hashCode = 31 * hashCode + image.getRGB(i, j);
      }
    }
    return hashCode;
  }

  public void saveAs(String filepath) {
    try {
      ImageIO.write(image, "png", new File(filepath));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Update the pixel-value at the specified location.
   *
   * @param x the x-coordinate of the pixel to be updated
   * @param y the y-coordinate of the pixel to be updated
   * @param rgb the RGB components of the updated pixel-value
   * @throws ArrayIndexOutOfBoundsException if the specified pixel-location is not contained within
   *     the boundaries of this picture.
   */
  public void setPixel(int x, int y, Color rgb) {

    image.setRGB(
        x,
        y,
        0xff000000
            | (((0xff & rgb.getRed()) << 16)
                | ((0xff & rgb.getGreen()) << 8)
                | (0xff & rgb.getBlue())));
  }

  /** Returns a String representation of the RGB components of the picture. */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        Color rgb = getPixel(x, y);
        sb.append("(");
        sb.append(rgb.getRed());
        sb.append(",");
        sb.append(rgb.getGreen());
        sb.append(",");
        sb.append(rgb.getBlue());
        sb.append(")");
      }
      sb.append("\n");
    }
    sb.append("\n");
    return sb.toString();
  }

  //  inverts an image
  public void invert(String filepath) {
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        final Color color = getPixel(x, y);
        setPixel(
            x,
            y,
            new Color((255 - color.getRed()), (255 - color.getGreen()), (255 - color.getBlue())));
      }
    }
    saveAs(filepath);
  }
  // greyscale
  public void grayscale(String filepath) {
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        final Color color = getPixel(x, y);
        final int colourAvg = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
        setPixel(x, y, new Color(colourAvg, colourAvg, colourAvg));
      }
    }
    saveAs(filepath);
  }

//  rotation
  public void rotate(String angle, String filepath) {
    switch (angle) {
      case "90" -> rotate90().saveAs(filepath);
      case "180" -> rotate90().rotate90().saveAs(filepath);
      case "270" -> rotate90().rotate90().rotate90().saveAs(filepath);
    }
  }
  private Picture rotate90() {
    Picture instance = new Picture(getHeight(), getWidth());
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        final Color color = getPixel(x, y);
        instance.setPixel((getHeight() -1 -y), x, color);
      }
    }
    return instance;
  }

//  filp
  public void flip(String type, String filepath) {
    switch (type) {
      case "H" -> flipH().saveAs(filepath);
      case "V" -> flipV().saveAs(filepath);
    }
  }

  private Picture flipH() {
    Picture instance = new Picture(getWidth(), getHeight());
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        final Color color = getPixel(x, y);
        instance.setPixel(getWidth() - 1 - x, y, color);
      }
    }
    return instance;
  }

  private Picture flipV() {
    Picture instance = new Picture(getWidth(), getHeight());
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        final Color color = getPixel(x, y);
        instance.setPixel(x, getHeight() - 1 - y, color);
      }
    }
    return instance;
  }

//  Blend
  public static void blend(List<String> pictures, String filepath) {
    Picture blended = new Picture(minWidth(pictures), minHeight(pictures));
    List<Picture> pictures1 = new ArrayList<Picture>();
    for (String picture : pictures) {
      Picture temp = new Picture(picture);
      pictures1.add(temp);
      }

    for (int x = 0; x < minWidth(pictures); x++) {
      for (int y = 0; y < minHeight(pictures); y++) {
        int avgRed = 0;
        int avgGreen = 0;
        int avgBlue = 0;
        for (Picture picture : pictures1){
          final Color color = picture.getPixel(x, y);
          avgRed = avgRed + color.getRed();
          avgGreen = avgGreen + color.getGreen();
          avgBlue = avgBlue + color.getBlue();
        }
        avgRed = avgRed / pictures1.size();
        avgGreen = avgGreen / pictures1.size();
        avgBlue = avgBlue / pictures1.size();
        blended.setPixel(x, y, new Color(avgRed, avgGreen, avgBlue));
      }
    }

    blended.saveAs(filepath);
  }

  private static int minHeight(List<String> pictures) {
    int min = -1;
    for (String picture : pictures){
      Picture temp = new Picture(picture);
      if (min == -1 || temp.getHeight() < min){
        min = temp.getHeight();
      }
      }
    return min;
    }

  private static int minWidth(List<String> pictures) {
    int min = -1;
    for (String picture : pictures){
      Picture temp = new Picture(picture);
      if (min == -1 || temp.getWidth() < min){
        min = temp.getWidth();
      }
    }
    return min;
  }

//  Blur
  public void blur(String filepath) {
    Picture blured = new Picture(getWidth(), getHeight());
    for (int x = 0; x < getWidth(); x++) {
      for (int y = 0; y < getHeight(); y++) {
        if (x != 0 && x != (getWidth() - 1) && y != 0 && y != (getHeight() - 1)) {

          List<Color> colors = new ArrayList<Color>();
          for (int z = -1; z < 2; z++) {
            for (int w = -1; w < 2; w++) {
//              System.out.println(z + "+" + w);
//              System.out.println(x + "+" + y);
              final Color color = getPixel(x + z, y + w);
              colors.add(color);
            }
          }

          int avgRed = 0;
          int avgGreen = 0;
          int avgBlue = 0;

          for (Color color : colors) {
            avgRed = avgRed + color.getRed();
            avgGreen = avgGreen + color.getGreen();
            avgBlue = avgBlue + color.getBlue();
          }
          avgRed = avgRed / colors.size();
          avgGreen = avgGreen / colors.size();
          avgBlue = avgBlue / colors.size();

          blured.setPixel(x, y, new Color(avgRed, avgGreen, avgBlue));

        } else {
          blured.setPixel(x, y, getPixel(x, y));
        }
      }
    }
    blured.saveAs(filepath);
  }


  }
