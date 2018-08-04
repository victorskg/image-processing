import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class TheHough {
    public void run(String[] args) {
        String default_file = "src/images/circulares/3829.jpg";
        String filename = ((args.length > 0) ? args[0] : default_file);
        int quantidadeCirculos = 0;
        // Carrega imagem.
        Mat src = Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);
        // Verifica se imagem foi carregada corretamente.
        if( src.empty() ) {
            System.out.println("Erro ao abrir imagem!");
            System.out.println("Argumentos do programa: [image_name -- default "
                    + default_file +"] \n");
            System.exit(-1);
        }
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);
        Mat circles = new Mat();
        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                (double)gray.rows()/16, // mude este valor para detectar circulos a certas distancias um do outro.
                100.0, 30.0, 1, 45); // ajuste os dois ultimos parametros
        // (min_radius & max_radius) para detectar circulos largos.
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            // centro do circulo
            Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );
            // circle outline
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );
            System.out.println("Raio: "+ radius + " Centro: " + center);
            quantidadeCirculos++;
        }
        HighGui.imshow("Circulos detectados: " + quantidadeCirculos, src);
        HighGui.waitKey();
        System.exit(0);
    }
}
