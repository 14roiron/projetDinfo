import java.awt.Color;
import java.awt.Graphics;

class Rectangle extends DessinObjet {
	int h, w;// hauteur, largeur

	public Rectangle(double x, double y, int w, int h, Color c, double vx,
			double vy) {
		super(x, y, c, vx, vy);
		this.h = h;
		this.w = w;
	}

	public Rectangle(int x, int y, int w, int h, Color c, double vx, double vy) {
		this((double) x, (double) y, w, h, c, vx, vy);
	}

	public void dessiner(Graphics g) {
		g.setColor(c);
		g.fillRect((int) x, (int) y, w, h);
	}
}