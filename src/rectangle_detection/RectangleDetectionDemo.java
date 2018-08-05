package rectangle_detection;

import org.opencv.core.Core;

public class RectangleDetectionDemo {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new RectangleDetection().run(args);
    }
}
