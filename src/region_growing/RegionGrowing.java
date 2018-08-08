package region_growing;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RegionGrowing {
    public void run(String[] args) {
        File diretorio = new File("src/images/MediumCut"); //Carrega o diretorio que contem as imagens
        String[] arquivos = diretorio.list(); //Cria uma lista com o nome dos arquivos
        int limiar = 50; //Altere para um resultado diferente na imagem após o crescimento de regiões
        double[] corRGB = {127, 127, 127};

        for(int k = 0; k < arquivos.length; k++) {
            //Carrega a imagem
            Mat imagem = Imgcodecs.imread(diretorio.getPath()+"/"+arquivos[k], Imgcodecs.IMREAD_COLOR);
            //Verifica se a imagem foi carregada com sucesso
            if( imagem.empty() ) {
                System.out.println("Erro ao abrir imagem: " + arquivos[k]);
                System.exit(-1);
            }

            Regiao regiao = new Regiao();
            Point seed = new Point(imagem.size().height/2, imagem.size().width/2);
            int xSeed = (int) seed.x - 10;
            int ySeed = (int) seed.y + 10;

            //Crescimento de regiões a partir do pixel central.
            Mat imagemCrescimentoRegioes = imagem.clone();
            regiao.pixels.add(new Pixel(xSeed, ySeed, imagemCrescimentoRegioes.get(xSeed, ySeed)));
            byte[] srcData = new byte[(int) (imagemCrescimentoRegioes.total() * imagemCrescimentoRegioes.channels())]; //Dados dos pixels da imagem
            imagemCrescimentoRegioes.get(0, 0, srcData); //Pegando os dados em termos de coordenadas de linhas e colunas
            for (int i = 1; i < imagemCrescimentoRegioes.rows(); i++) {
                for (int j = 1; j < imagemCrescimentoRegioes.cols(); j++) {
                    Pixel pixel = new Pixel(i, j, imagemCrescimentoRegioes.get(i, j));
                    if( Math.abs(regiao.pixels.get(0).intensidade[0] - imagemCrescimentoRegioes.get(i, j)[0]) +
                            Math.abs(regiao.pixels.get(0).intensidade[1] - imagemCrescimentoRegioes.get(i, j)[1]) +
                            Math.abs(regiao.pixels.get(0).intensidade[2] - imagemCrescimentoRegioes.get(i, j)[2]) <= limiar) {
                        regiao.pixels.add(pixel);
                        imagemCrescimentoRegioes.put(i, j, regiao.pixels.get(0).intensidade);
                    } else {
                        imagemCrescimentoRegioes.put(i, j, corRGB);
                    }
                }
            }

            //Salva o resultado em um arquivo
            Image image = HighGui.toBufferedImage(imagemCrescimentoRegioes);
            BufferedImage bi = (BufferedImage) image;
            saveImage(diretorio.getPath()+"/crescimento/"+arquivos[k], bi);
        }

        System.exit(0);
    }

    static class Regiao {
        public List<Pixel> pixels = new ArrayList<>();

        public Regiao(){ }
    }

    static class Pixel {
        int linha;
        int coluna;
        double[] intensidade;
        int[][] vizinhos = new int[8][2];

        public Pixel(int l, int c, double[] intensidade) {
            this.linha = l;
            this.coluna = c;
            this.intensidade = intensidade;

            vizinhos[0] = new int[] {l+1, c};
            vizinhos[1] = new int[] {l, c+1};
            vizinhos[2] = new int[] {l-1, c};
            vizinhos[3] = new int[] {l, c-1};
            vizinhos[4] = new int[] {l+1, c+1};
            vizinhos[5] = new int[] {l+1, c-1};
            vizinhos[6] = new int[] {l-1, c+1};
            vizinhos[7] = new int[] {l-1, c-1};
        }
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
