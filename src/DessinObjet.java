import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;


public abstract class DessinObjet implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double x, y;
	  Color c;
	  double vx, vy;
	  public DessinObjet(double x,double y,Color c,double vx,double vy)
	  {
		  	this.x = x;
		    this.y = y;
		    this.c = c;
		    this.vx = vx;
		    this.vy = vy;
	  }
	  public DessinObjet(int x,int y,Color c,double vx,double vy)
	  {
		  this((double)x,(double)y,c,vx,vy);
	  }
	  public abstract void dessiner(Graphics g);
	  public DessinObjet()
	  {
		  
	  }
	  
}
