package angrycars.engine;

public class Rectangle {
    public double minX;
    public double maxX;
    public double minY;
    public double maxY;
    
    public void expand(Rectangle other) {
    	if (other.minX < minX) minX = other.minX;
    	if (other.maxX > maxX) maxX = other.maxX;
    	if (other.minY < minY) minY = other.minY;
    	if (other.maxY > maxY) maxY = other.maxY;
    }
    
    @Override
    public String toString() {
    	return String.format("<Rectangle %f %f %f %f>", minY, minX, maxY, maxX);
    }
}
