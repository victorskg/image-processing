package image_segmentation;

import org.opencv.core.Core;

public class ImageSegmentationDemo {
    public static void main(String[] args) {
        //Carrega a biblioteca Core do OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        new ImageSegmentation().run(args);
    }
}
