package circle_detection;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class TheHough {
    public void run(String[] args) {
        File diretorio = new File("src/images/MediumCut"); //Carrega o diretorio que contem as imagens
        String[] arquivos = diretorio.list(); //Cria uma lista com o nome dos arquivos
        int quantidadeCirculos;
        for(int i = 0; i < arquivos.length; i++) {
            //Carrega imagem
            Mat src = Imgcodecs.imread(diretorio.getPath()+"/"+arquivos[i], Imgcodecs.IMREAD_COLOR);
            //Verifica se imagem foi carregada corretamente
            if( src.empty() ) {
                System.out.println("Erro ao abrir imagem: " + arquivos[i]);
                System.exit(-1);
            }
            Mat gray = new Mat();
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY); //Converte a imagem para os niveis de cinza
            Imgproc.medianBlur(gray, gray, 5); //Aplica um filtro de suavização
            Mat circles = new Mat();
            Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1.0,
                    (double)gray.rows()/4, // mude este valor para detectar circulos a certas distancias um do outro.
                    100.0, 30.0, 30, 150); // ajuste os dois ultimos parametros
            // (min_radius & max_radius) para detectar circulos largos.
            quantidadeCirculos = 0;
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

                System.out.println("Imagem " + arquivos[i]);
                System.out.println("Raio: "+ radius + " Centro: " + center);
                System.out.println("Área: "+ area + " Perimetro: " + perimetro);
                System.out.println("Razão área/perimetro: "+ (area/perimetro));

                quantidadeCirculos++;
            }

            System.out.println("Quantidade de circulos: " + quantidadeCirculos + "\n");

            //Salva o resultado em um arquivo
            Image image = HighGui.toBufferedImage(src);
            BufferedImage bi = (BufferedImage) image;
            saveImage(diretorio.getPath()+"/circulos/"+arquivos[i], bi);
        }

        System.exit(0);
    }

    public static void saveImage(String filename,
                                 BufferedImage image) {
        File file = new File(filename);

        try {
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            System.out.println(e.toString()+" Imagem '"+filename
                    +"' falhou ao salvar.");
        }
    }
}
