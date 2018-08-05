package circle_detection;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class TheHough {
    public void run(String[] args) {
        String default_file = "src/images/MediumCut/3259_lg.jpg";
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
                100.0, 30.0, 5, 80); // ajuste os dois ultimos parametros
        // (min_radius & max_radius) para detectar circulos largos.
        for (int x = 0; x < circles.cols(); x++) {
            double[] c = circles.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            //centro do circulo
            Imgproc.circle(src, center, 1, new Scalar(0,100,100), 3, 8, 0 );

            //Raio, área e perimetro
            int radius = (int) Math.round(c[2]);
            double area = 3.14 * (radius ^ 2);
            double perimetro = ((2 * 3.14) * radius);
            Imgproc.circle(src, center, radius, new Scalar(255,0,255), 3, 8, 0 );

            System.out.println("Raio: "+ radius + " Centro: " + center);
            System.out.println("Área: "+ area + " Perimetro: " + perimetro);
            System.out.println("Razão área/perimetro: "+ (area/perimetro));

            quantidadeCirculos++;
        }
        HighGui.imshow("Circulos detectados: " + quantidadeCirculos, src);
        HighGui.waitKey();
        System.exit(0);
    }
}
