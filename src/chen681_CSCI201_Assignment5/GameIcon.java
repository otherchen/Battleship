package chen681_CSCI201_Assignment5;

import javax.swing.ImageIcon;

public class GameIcon extends ImageIcon{

	private static final long serialVersionUID = 1L;
	String marker;
	
	public GameIcon(String url, String marker){
		super(url);
		this.marker = marker;
	}
	
	@Override
	public boolean equals(Object other){
	   GameIcon otherImage = (GameIcon) other;
	   if (other == null) return false;
	   return marker == otherImage.getMarker();
	}
	
	public String getMarker(){
		return marker;
	}
	
}

