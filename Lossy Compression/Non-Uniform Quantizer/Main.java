package non.uniform;

import java.io.IOException;

public class Main {
        public static void main(String[] args) throws IOException {
		
            int[][] pixels=Image.readImage("Itachi.jpg");
            
            NonUniform test = new NonUniform(3, pixels);
            test.CompressImage();
            int[][] ret = test.DeCompressImage();
            
            Image.writeImage(ret, "out.jpg");
            
	}
}
