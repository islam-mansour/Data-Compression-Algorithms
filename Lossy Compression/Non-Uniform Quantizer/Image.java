package non.uniform;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Image {
        
        static int height, width;

	public static int[][] readImage(String path){
		
		
		BufferedImage img;
		try {
			img = ImageIO.read(new File(path));
		
		height=img.getHeight();
		width=img.getWidth();
		
		int[][] imagePixels=new int[width][height];
		for(int x=0;x<width;x++){
			for(int y=0;y<height;y++){
				
				int pixel=img.getRGB(x, y);
				
				int red=(pixel  & 0x00ff0000) >> 16;
//				int green=(pixel  & 0x0000ff00) >> 8;
//				int blue=pixel  & 0x000000ff;
//				int alpha=(pixel & 0xff000000) >> 24;
				imagePixels[x][y] = red;
			}
		}
		
		return imagePixels;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
		
	}
	
	public static void writeImage(int[][] imagePixels,String outPath){
            
            BufferedImage image = new BufferedImage(imagePixels.length, imagePixels[0].length, BufferedImage.TYPE_INT_RGB);
	    for (int x= 0; x < imagePixels.length; x++) {
	        for (int y = 0; y < imagePixels[x].length; y++) {
	             int value =-1 << 24;
	             value= 0xff000000 | (imagePixels[x][y]<<16) | (imagePixels[x][y]<<8) | (imagePixels[x][y]);
	             image.setRGB(x, y, value);
	        }
	    }

	    File ImageFile = new File(outPath);
	    try {
	        ImageIO.write(image, "jpg", ImageFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

		
	}
}

