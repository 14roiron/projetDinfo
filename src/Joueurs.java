import java.awt.Color;
import java.awt.Graphics;

public class Joueurs extends DessinObjet {

	double angle;
	double position;
	int numero;
	int nombreDeJoueurs;
	double plage;

	double Vposition;
	double Vangle;

	Joueur[] joueurs;

	public Joueurs(int numero) {
		this.numero = numero;
		int w = Pannel.width;
		int h = Pannel.height;

		switch (numero) {
		case 0:
			nombreDeJoueurs = 1;
			plage = h / 10;
			joueurs = new Joueur[nombreDeJoueurs];
			joueurs[0] = new Joueur(60, (h / 2) + 20);
			break;

		case 1:
			nombreDeJoueurs = 2;
			plage = h / 5;
			joueurs = new Joueur[nombreDeJoueurs];
			joueurs[0] = new Joueur(w / 3, h / 2 - h / 4);
			joueurs[1] = new Joueur(w / 3, h / 2 + h / 4);
			break;
		case 2:
			nombreDeJoueurs = 2;
			plage = h / 5;
			joueurs = new Joueur[nombreDeJoueurs];
			joueurs[0] = new Joueur(w - w / 3, h / 2 - h / 4);
			joueurs[1] = new Joueur(w - w / 3, h / 2 + h / 4);
			break;

		case 3:
			nombreDeJoueurs = 1;
			plage = h / 10;
			joueurs = new Joueur[nombreDeJoueurs];
			joueurs[0] = new Joueur(w - 60, (h / 2) + 20);
			break;
		}
	}

	public Joueurs(Color c, double angle, double position, int numero,
			int nombreDeJoueurs, double plage, double vposition, double vangle,
			Joueur[] joueurs) {
		super(0, 0, c, 0, 0);
		this.angle = angle;
		this.position = position;
		this.numero = numero;
		this.nombreDeJoueurs = nombreDeJoueurs;
		this.plage = plage;
		this.Vposition = vposition;
		this.Vangle = vangle;
		this.joueurs = joueurs;
	}

	public Joueurs numero(int numero) {
		int w = Pannel.width;
		int h = Pannel.height;
		double angle = this.angle;
		Joueur[] joueurs = null;
		int nombreDeJoueurs = 0;

		switch (numero) {
		case 0:
			nombreDeJoueurs = 1;
			joueurs = new Joueur[nombreDeJoueurs];
			joueurs[0] = new Joueur(0, 0);
			joueurs[0].xBase = 60;
			joueurs[0].yBase = (h / 2) + 20;
			break;
		case 1:
			nombreDeJoueurs = 2;
			joueurs = new Joueur[nombreDeJoueurs];
			joueurs[0] = new Joueur(0, 0);
			joueurs[1] = new Joueur(0, 0);
			joueurs[0].xBase = (w / 3);
			joueurs[0].yBase = (h / 2 - h / 4);
			joueurs[1].xBase = (w / 3);
			joueurs[1].yBase = (h / 2 + h / 4);
			break;
		case 2:
			nombreDeJoueurs = 2;
			joueurs = new Joueur[nombreDeJoueurs];
			joueurs[0] = new Joueur(0, 0);
			joueurs[1] = new Joueur(0, 0);
			joueurs[0].xBase = (w - w / 3);
			joueurs[0].yBase = (h / 2 - h / 4);
			joueurs[1].xBase = (w - w / 3);
			joueurs[1].yBase = (h / 2 + h / 4);
			angle *= -1;
			break;

		case 3:
			nombreDeJoueurs = 1;
			joueurs = new Joueur[nombreDeJoueurs];
			joueurs[0] = new Joueur(0, 0);
			angle *= -1;
			joueurs[0].xBase = w - 60;
			joueurs[0].yBase = (h / 2) + 20;
			break;
		}

		return new Joueurs(c, angle, position, numero, nombreDeJoueurs, plage,
				Vposition, Vangle, joueurs);

	}

	public Joueurs changementSansCopyNumero(int numero) {
		int w = Pannel.width;
		int h = Pannel.height;

		switch (numero) {
		case 0:
			joueurs[0].xBase = 60;
			joueurs[0].yBase = (h / 2) + 20;
			break;
		case 1:
			joueurs[0].xBase = (w / 3);
			joueurs[0].yBase = (h / 2 - h / 4);
			joueurs[1].xBase = (w / 3);
			joueurs[1].yBase = (h / 2 + h / 4);
			break;
		case 2:
			joueurs[0].xBase = (w - w / 3);
			joueurs[0].yBase = (h / 2 - h / 4);
			joueurs[1].xBase = (w - w / 3);
			joueurs[1].yBase = (h / 2 + h / 4);
			angle *= -1;
			break;

		case 3:
			angle *= -1;
			joueurs[0].xBase = w - 60;
			joueurs[0].yBase = (h / 2) + 20;
			break;
		}

		return this;

	}

	public void maj() {
		for (Joueur j : joueurs) {
			j.maj(angle, position, Vangle, Vposition);
		}
	}

	public void dessiner(Graphics g) {
		seDeplacer();
		for (Joueur j : joueurs) {
			if (j != null) {
				j.maj(angle, position, Vangle, Vposition);
				j.dessiner(g);
			}
		}

	}

	public void seDeplacer() {
		// System.out.println("Vpos "+Vposition);
		position += Vposition;
		angle += Vangle / 50.;
		if (position > plage + Joueur.h) {
			position = plage + Joueur.h;
		}
		if (position < -plage) {
			position = -plage;
		}
		if (angle > (1.1 * Math.PI)) {
			angle -= 2 * Math.PI;
		}
		if (angle < -(1.1 * Math.PI)) {
			angle += 2 * Math.PI;
		}
	}

}
