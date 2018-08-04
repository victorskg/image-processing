package region_growing;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class RegionGrowing {
    public void run(String[] args) {
        String imagem_padrão = "src/images/circulares/3829.jpg";
        int diferenca_padrao = 50;
        List<Regiao> regioes = new ArrayList<>();

        //Verifica os argumentos do programa
        String caminhoImagem = ((args.length > 0) ? args[0] : imagem_padrão);
        int diferenca_cores = ((args.length > 1) ? Integer.parseInt(args[1]) : diferenca_padrao);

        //Carrega a imagem
        Mat imagem = Imgcodecs.imread(caminhoImagem, Imgcodecs.IMREAD_COLOR);

        //Verifica se a imagem foi carregada com sucesso
        if( imagem.empty() ) {
            System.out.println("Erro ao abrir imagem! Por favor, passe como argumento: [caminho para imagem] e [diferenca de cores].");
            System.exit(-1);
        }

        HighGui.imshow("Imagem Original", imagem);

        //Binarização da imagem
        Mat imagem_binaria = new Mat();
        Imgproc.cvtColor(imagem, imagem_binaria, Imgproc.COLOR_BGR2GRAY);
        Imgproc.threshold(imagem_binaria, imagem_binaria, 40, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        HighGui.imshow("Imagem Binária", imagem_binaria);

        //Crescimento de regiões
        Mat imagemCrescimentoRegioes = imagem.clone();
        byte[] srcData = new byte[(int) (imagemCrescimentoRegioes.total() * imagemCrescimentoRegioes.channels())]; //Dados dos pixels da imagem
        imagemCrescimentoRegioes.get(0, 0, srcData); //Pegando os dados em termos de coordenadas de linhas e colunas
        for (int i = 0; i < imagemCrescimentoRegioes.rows(); i++) {
            for (int j = 0; j < imagemCrescimentoRegioes.cols(); j++) {
                Pixel pixel = new Pixel(i, j, imagemCrescimentoRegioes.get(i, j));

                //Primeira região criada
                if (regioes.size() == 0) {
                    Regiao regiao = new Regiao();
                    regiao.pixels.add(pixel);
                    regioes.add(regiao);
                    System.out.println("Primeira regiao");
                } else {
                    boolean adicionado = false;

                    //Laço que verifica se o novo pixel pertence a uma nova região ou a uma já existente
                    for (int k = 0; k < regioes.size(); k++) {
                        if( Math.abs(regioes.get(k).pixels.get(0).intensidade[0] - imagemCrescimentoRegioes.get(i, j)[0]) +
                                Math.abs(regioes.get(k).pixels.get(0).intensidade[1] - imagemCrescimentoRegioes.get(i, j)[1]) +
                                        Math.abs(regioes.get(k).pixels.get(0).intensidade[2] - imagemCrescimentoRegioes.get(i, j)[2]) <= diferenca_padrao) {
                            regioes.get(k).pixels.add(pixel);
                            adicionado = true;
                            System.out.println("Mesma regiao");

                            /*Atualiza na imagem do crescimento a nova tonalidade do pixel pegando a tonalidade
                            do primeiro pixel daquela região*/
                            imagemCrescimentoRegioes.put(i, j, regioes.get(k).pixels.get(0).intensidade);
                            break;
                        }
                    }
                    if(!adicionado) {
                        Regiao regiao1 = new Regiao();
                        regiao1.pixels.add(pixel);
                        regioes.add(regiao1);
                        System.out.println("Nova região");
                    }
                }
            }
        }

        HighGui.imshow("Crescimento regiao", imagemCrescimentoRegioes);

        HighGui.waitKey();
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

        public Pixel(int l, int c, double[] intensidade) {
            this.linha = l;
            this.coluna = c;
            this.intensidade = intensidade;
        }
    }

}
