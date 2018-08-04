package region_growing;

import org.opencv.core.Core;

public class RegionGrowingDemo {
    public static void main(String[] args) {
        //Carrega a biblioteca core do OpenCV.
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new RegionGrowing().run(args);
    }
}
