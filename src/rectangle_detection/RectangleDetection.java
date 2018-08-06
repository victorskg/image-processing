package rectangle_detection;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class RectangleDetection {
    public void run(String[] args) {
        String caminhoImage = "src/images/3665.jpg";
        Mat src = Imgcodecs.imread(caminhoImage, Imgcodecs.IMREAD_COLOR);

        //A partir de um dado ponto superior esquerdo e um ponto inferior direito, o forma um retângulo a partir destes pontos
        /*Não foi possível procurar pelos pontos nas bordas do objeto da imagem para se assim fazer o retângulo, mas depois de
          achado os pontos, a detecção de retângulos se torna pronta*/
        Imgproc.rectangle(src, new Point(100, 100), new Point(50, 50), new Scalar(127, 127, 127));

        HighGui.imshow("Retângulo detectado: ", src);
        HighGui.waitKey();

        System.exit(0);
    }
}
