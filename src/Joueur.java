import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Joueur extends Rectangle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double angle;
	double position;
	int xBase;
	int yBase;
	double Vangle;
	double Vposition;
	boolean presence;

	final static int w = 40;
	final static int h = 40;

	public Joueur(int xBase, int yBase) {
		super(xBase - w / 2 ,yBase - h / 2 - h / 2,w,h,Color.green,0,0);
		double angle = 0;
		double position = 0;
		this.xBase = xBase;
		this.yBase = yBase;
		this.x = xBase - w / 2 + ((double) w * Math.sin(angle));
		this.y = yBase - h / 2 + position - h / 2;
		this.vx=0;
		this.vy=0;

	}
	public void maj(double angle, double position,
			double Vangle, double Vposition)
	{
		this.angle=angle;
		this.position=position;
		this.Vangle=Vangle;
		this.Vposition=Vposition;
		if (angle > -Math.PI / 3. && angle < Math.PI / 3.) {
			presence = true;
			c = Color.blue;

		} else {
			presence = false;
			c = Color.GRAY;

		}
		
		x = xBase - w / 2 + (int) ((double) 2* w * Math.sin(angle));
		y = yBase - h / 2 + (int) position - h / 2;
		vx=(int) ((double) w * Vangle * Math.cos(angle));//dérivation
		vy = Vposition;
	}
	public void dessiner(Graphics g, double angle, double position,
			double Vangle, double Vposition) {
		maj(angle,position,Vangle,Vposition);
		dessiner(g);
		// System.out.println("dessin joueur x:"+(xBase-w/2+(int)((double)w*Math.sin(angle)))+" y:"+(yBase-h/2+(int)position));

	}

}
