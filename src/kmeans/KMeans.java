package kmeans;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;

public class KMeans {
    BufferedImage original;
    BufferedImage result;
    Cluster[] clusters;
    public static final int MODE_CONTINUOUS = 1;
    public static final int MODE_ITERATIVE = 2;


    public static void main(String[] args) {
        int mode = 1;
        File diretorio = new File("src/images/MediumCut"); //Carrega o diretorio que contem as imagens
        String[] arquivos = diretorio.list(); //Cria uma lista com o nome dos arquivos
        int quantidadeClusters = 4;
        for(int i = 0; i < arquivos.length; i++) {
            // create new KMeans object
            KMeans kmeans = new KMeans();
            // call the function to actually start the clustering
            BufferedImage dstImage = kmeans.calculate(loadImage(diretorio.getPath()+"/"+arquivos[i]),
                    quantidadeClusters, mode);
            // save the resulting image
            saveImage(diretorio.getPath()+"/kmeans/"+arquivos[i], dstImage);
        }
    }

    public KMeans() {    }

    public BufferedImage calculate(BufferedImage image,
                                   int k, int mode) {
        long start = System.currentTimeMillis();
        int w = image.getWidth();
        int h = image.getHeight();
        // create clusters
        clusters = createClusters(image,k);
        // create cluster lookup table
        int[] lut = new int[w*h];
        Arrays.fill(lut, -1);

        // at first loop all pixels will move their clusters
        boolean pixelChangedCluster = true;
        // loop until all clusters are stable!
        int loops = 0;
        while (pixelChangedCluster) {
            pixelChangedCluster = false;
            loops++;
            for (int y=0;y<h;y++) {
                for (int x=0;x<w;x++) {
                    int pixel = image.getRGB(x, y);
                    Cluster cluster = findMinimalCluster(pixel);
                    if (lut[w*y+x]!=cluster.getId()) {
                        // cluster changed
                        if (mode==MODE_CONTINUOUS) {
                            if (lut[w*y+x]!=-1) {
                                // remove from possible previous
                                // cluster
                                clusters[lut[w*y+x]].removePixel(
                                        pixel);
                            }
                            // add pixel to cluster
                            cluster.addPixel(pixel);
                        }
                        // continue looping
                        pixelChangedCluster = true;

                        // update lut
                        lut[w*y+x] = cluster.getId();
                    }
                }
            }
            if (mode==MODE_ITERATIVE) {
                // update clusters
                for (int i=0;i<clusters.length;i++) {
                    clusters[i].clear();
                }
                for (int y=0;y<h;y++) {
                    for (int x=0;x<w;x++) {
                        int clusterId = lut[w*y+x];
                        // add pixels to cluster
                        clusters[clusterId].addPixel(
                                image.getRGB(x, y));
                    }
                }
            }

        }
        // create result image
        BufferedImage result = new BufferedImage(w, h,
                BufferedImage.TYPE_INT_RGB);
        for (int y=0;y<h;y++) {
            for (int x=0;x<w;x++) {
                int clusterId = lut[w*y+x];
                result.setRGB(x, y, clusters[clusterId].getRGB());
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Clusterizado em "+k
                + " clusters em "+loops
                +" laços em "+(end-start)+" ms.");
        return result;
    }

    public Cluster[] createClusters(BufferedImage image, int k) {
        // Here the clusters are taken with specific steps,
        // so the result looks always same with same image.
        // You can randomize the cluster centers, if you like.
        Cluster[] result = new Cluster[k];
        int x = 0; int y = 0;
        int dx = image.getWidth()/k;
        int dy = image.getHeight()/k;
        for (int i=0;i<k;i++) {
            result[i] = new Cluster(i,image.getRGB(x, y));
            x+=dx; y+=dy;
        }
        return result;
    }

    public Cluster findMinimalCluster(int rgb) {
        Cluster cluster = null;
        int min = Integer.MAX_VALUE;
        for (int i=0;i<clusters.length;i++) {
            int distance = clusters[i].distance(rgb);
            if (distance<min) {
                min = distance;
                cluster = clusters[i];
            }
        }
        return cluster;
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

    public static BufferedImage loadImage(String filename) {
        BufferedImage result = null;
        try {
            result = ImageIO.read(new File(filename));
        } catch (Exception e) {
            System.out.println(e.toString()+" Imagem '"
                    +filename+"' não encontrada.");
        }
        return result;
    }
}
