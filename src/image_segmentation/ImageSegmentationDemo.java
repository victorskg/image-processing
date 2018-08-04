package image_segmentation;

import org.opencv.core.Core;

public class ImageSegmentationDemo {
    public static void main(String[] args) {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        new ImageSegmentation().run(args);
    }
}
