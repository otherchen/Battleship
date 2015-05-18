package chen681_CSCI201_Assignment5;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

public class ImageLabel extends JLabel{
		
		//serialID
		private static final long serialVersionUID = 1L;
		
		//front and background images
		private BufferedImage frontImage;
		private BufferedImage backImage;
		private BufferedImage water1Image;
		private BufferedImage water2Image;
		private BufferedImage qImage;
		
		//flags
		boolean waterFlag = true;
		boolean isHeader = false;
		int explCount = 0;
		int splashCount = 0;

	    public ImageLabel() {
	        setOpaque(true);
	    }
	    
	    public ImageLabel(boolean header){
	    	setOpaque(true);
	    	isHeader = header;
	    }
	    
	    public void saveImages(File water1, File water2, File q){
	    	if(!isHeader){
		    	try {
		        	water1Image = ImageIO.read(water1);
		        	water2Image = ImageIO.read(water2);
		        	qImage = ImageIO.read(q);
		        	frontImage = qImage;
		        	backImage = water1Image;
		        }
		        catch(IOException ioe) {
		            System.out.println("Unable to fetch image.");
		            ioe.printStackTrace();
		        }
	    	}
	    }
	    
	    public void reset(){
	    	if(!isHeader){
	    		frontImage = qImage;
	    	}
	    }

	    @Override
	    protected void paintComponent(Graphics g){
	        super.paintComponent(g);
	        if(!isHeader){
	        	g.drawImage(backImage, 0, 0, this);
	        	g.drawImage(frontImage, 5, 5, this);
	        }
	    }
	    
	    public void paintWater(){
	    	if(!isHeader){
		    	if(waterFlag){
					backImage = water2Image;
					waterFlag = false;
				} else {
					backImage = water1Image;
					waterFlag = true;
				} 
	    	}    	
	    }
	    
	    public boolean paintExplosion(File[] images){
	    	if(!isHeader){
	    		try {
	    			if(explCount >= 5){
	    				explCount = 0;
	    				return false;
	    			} else {
	    				frontImage = ImageIO.read(images[explCount]);
	    				explCount++;
	    				return true;
	    			}
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	    	
	    	return false;
	    }
	    
	    public boolean paintSplash(File[] images){
	    	if(!isHeader){
	    		try {
	    			if(splashCount >= 7){
	    				splashCount = 0;
	    				return false;
	    			} else {	 
	    				//setting splash animation
	    				frontImage = ImageIO.read(images[splashCount]);
	    				
	    				//values for resizing splash images
	    				int width = 16;
	    				int height = 16;
	    				
	    				//creating a temporary buffered image to resize splash images
	    				BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    				Graphics g = bufferedImage.getGraphics();
	    				g.drawImage(frontImage, 0, 0, width, height, null);
	    				g.dispose();
	    				frontImage = bufferedImage;
	    				
	    				splashCount++;
	    				return true;
	    			}
	    		} catch (IOException e){
	    			e.printStackTrace();
	    		}
	    	}
	    	return false;
	    }
	    
	    public void setMarker(String URL){
	    	File file = new File(URL);
	    	try {
				frontImage = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	
}
