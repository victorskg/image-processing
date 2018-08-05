package rectangle_detection;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class RectangleDetection {
    public void run(String[] args) {
        String caminhoImage = "src/images/3665.jpg";
        Mat src = Imgcodecs.imread(caminhoImage, Imgcodecs.IMREAD_COLOR);


        HighGui.waitKey();

        System.exit(0);
    }
}
