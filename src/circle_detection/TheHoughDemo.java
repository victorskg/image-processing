package circle_detection;

import org.opencv.core.*;

public class TheHoughDemo {
    public static void main(String[] args) {
        // Load the native library.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new TheHough().run(args);
    }
}
