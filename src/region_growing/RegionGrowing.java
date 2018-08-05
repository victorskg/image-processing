package region_growing;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class RegionGrowing {
    public void run(String[] args) {
        String imagemPadrao = "src/images/3310.jpg";
        int diferencaPadrao = 50; //Altere para um resultado diferente na imagem após o crescimento de regiões
        List<Regiao> regioes = new ArrayList<>();

        //Verifica os argumentos do programa
        String caminhoImagem = ((args.length > 0) ? args[0] : imagemPadrao);
        int diferencaCores = ((args.length > 1) ? Integer.parseInt(args[1]) : diferencaPadrao);

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
                if (regioes.isEmpty()) {
                    Regiao regiao = new Regiao();
                    regiao.pixels.add(pixel);
                    regioes.add(regiao);
                    System.out.println("Primeira região");
                } else {
                    boolean adicionado = false;

                    //Laço que verifica se o novo pixel pertence a uma nova região ou a uma já existente
                    for (int k = 0; k < regioes.size(); k++) {
                        if( Math.abs(regioes.get(k).pixels.get(0).intensidade[0] - imagemCrescimentoRegioes.get(i, j)[0]) +
                                Math.abs(regioes.get(k).pixels.get(0).intensidade[1] - imagemCrescimentoRegioes.get(i, j)[1]) +
                                        Math.abs(regioes.get(k).pixels.get(0).intensidade[2] - imagemCrescimentoRegioes.get(i, j)[2]) <= diferencaCores) {
                            regioes.get(k).pixels.add(pixel);
                            adicionado = true;
                            System.out.println("Região existente");

                            /*Atualiza na imagem do crescimento a nova tonalidade do pixel, de acordo com a tonalidade
                            do primeiro pixel daquela região*/
                            imagemCrescimentoRegioes.put(i, j, regioes.get(k).pixels.get(0).intensidade);
                            break;
                        }
                    }

                    //Se o pixel não foi adicionado a nenhuma região já existente, então ele é o primeiro de uma nova região
                    if(!adicionado) {
                        Regiao novaRegiao = new Regiao();
                        novaRegiao.pixels.add(pixel);
                        regioes.add(novaRegiao);
                        System.out.println("Nova região");
                    }
                }
            }
        }

        HighGui.imshow("Crescimento de regiões", imagemCrescimentoRegioes);

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
