
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException{
        
        int[][] pixels=Image.readImage("cameraMan.jpg");
        VectorQuantizer vq = new VectorQuantizer(32, 1, 1, pixels);
        vq.CompressImage("out.txt");
        pixels = vq.deCompressImage("out.txt");
        Image.writeImage(pixels, "out.jpg");

    }
}
