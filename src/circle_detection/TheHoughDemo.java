package circle_detection;

import org.opencv.core.*;

public class TheHoughDemo {
    public static void main(String[] args) {
        //Carrega a biblioteca Core do OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new TheHough().run(args);
    }
}
