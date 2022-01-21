package picture;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PictureProcessor {

  public static void main(String[] args) {
    System.out.println("Your first argument is: " + args[0]);
    switch (args[0]) {
      case "invert" -> {
        Picture picture = new Picture(args[1]);
        picture.invert(args[2]);
      }
      case "grayscale" -> {
        Picture picture1 = new Picture(args[1]);
        picture1.grayscale(args[2]);
      }
      case "rotate" -> {
        Picture picture2 = new Picture(args[2]);
        picture2.rotate(args[1], args[3]);
      }
      case "flip" -> {
        Picture picture3 = new Picture(args[2]);
        picture3.flip(args[1], args[3]);
      }
      case "blend" -> {
        List<String> pictures = Arrays.stream(args).skip(1).limit(args.length - 2)
            .collect(Collectors.toList());
        Picture.blend(pictures, args[args.length - 1]);
      }
      case "blur" -> {
        Picture picture5 = new Picture(args[1]);
        picture5.blur(args[2]);
      }
    }
  }
}
