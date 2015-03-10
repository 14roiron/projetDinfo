import java.awt.Color;
import java.awt.Graphics;

//Source TP10
public class Balle extends DessinObjet{  
  //coordonnées, rayon couleur du cercle et vecteur vitesse :
  int r;

  public Balle(double x, double y, int r, Color c, double vx, double vy) {
	  	super(x,y,c,vx,vy);
	    this.r = r;
  }
  public Balle(int x, int y, int r, Color c, double vx, double vy) {
	  	this((double)x,(double)y,r,c,vx,vy);
}
  public Balle inverser()
  {
	  double x;
	  x=Pannel.width-this.x;
	  return new Balle(x, y, r, c, vx, vy);
  }
  public void dessiner(Graphics g) {
    g.setColor(c);
    g.fillOval((int)x-r, (int)y-r, 2*r, 2*r);
  }
}