// ----------------------------------------------
//Inspiration TP11
// ----------------------------------------------

import java.awt.Color;
import java.io.*;
import java.util.*;
import java.net.*;

import javax.swing.JFrame;

public class ServeurBaBy extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int SCOREMAX = 2;
	private int numeroJoueur;
	private List<ThreadBaby> listeClients;
	boolean run;
	Joueurs[] tabsJoueurs;
	List<Rectangle> obstacles = new LinkedList<Rectangle>();
	Balle balle;
	Pannel pan;
	boolean pause = true;
	boolean terminee;
	double compteARebour;
	boolean enContactAvecLaBalle;

	/**
	 * 
	 */
	/**
	 * 
	 */
	public ServeurBaBy() {

		// Initialisations et lancement du serveur

		ServerSocket serveur = null;
		try {
			serveur = new ServerSocket(8888);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.exit(0);
		}

		Thread attente = new Thread(new Runnable() {
			ServeurBaBy arbitre;
			ServerSocket serveur;

			Runnable set(ServeurBaBy arbitre, ServerSocket serveur) {
				this.arbitre = arbitre;
				this.serveur = serveur;

				return this;
			}

			@Override
			public void run() {
				// TODO Auto-generated method stub

				while (true) {
					// Attente client (bloquant jusqu'a arrivee d'un
					// nouveau
					// client)
					System.out.println("Serveur en attente de client...");
					Socket s;
					try {
						s = serveur.accept();
						ObjectOutputStream out = new ObjectOutputStream(
								s.getOutputStream());
						out.flush();
						ObjectInputStream in = new ObjectInputStream(
								s.getInputStream());

						ajouterClient();
						int numJoueur = arbitre.numeroDernierJoueur();
						System.out.println("Connection joueur numero "
								+ numJoueur + " OK");
						// Creation et lancement du thread gerant le
						// nouveau
						// client
						ThreadBaby t = new ThreadBaby(arbitre, s, in, out);
						synchronized (arbitre.listeClients) {
							arbitre.listeClients.add(t);
						}
						t.start();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Ouverture du flux pour l'envoi des informations au
					// client a travers le reseau

				}
			}
		}.set(this, serveur));
		attente.start();
		listeClients = new ArrayList<ThreadBaby>();

		numeroJoueur = 0;
		balle = newBalle();
		obstacles.add(new Rectangle(0, 0, Pannel.width, 20, Color.green, 0, 0));
		obstacles
				.add(new Rectangle(0, 0, 20, Pannel.height, Color.green, 0, 0));
		obstacles.add(new Rectangle(Pannel.width - 20, 0, 20, Pannel.height,
				Color.green, 0, 0));
		obstacles.add(new Rectangle(0, Pannel.height - 20, Pannel.width, 20,
				Color.green, 0, 0));

		tabsJoueurs = new Joueurs[4];
		tabsJoueurs[0] = new Joueurs(0);
		tabsJoueurs[1] = new Joueurs(1);
		tabsJoueurs[2] = new Joueurs(2);
		tabsJoueurs[3] = new Joueurs(3);

		run = true;
		pause = true;
		terminee = false;
		compteARebour = 10;
		pan = new Pannel();

		this.setTitle("Serveur");
		this.setSize(Pannel.width, Pannel.height + 40);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setContentPane(pan);
		//this.setVisible(true);
		pan.CreerConstantes();
		run();
	}

	public void ajouterClient() {
		numeroJoueur++;
	}

	public int numeroDernierJoueur() {
		return numeroJoueur;
	}

	/**
	 * Envoi de l'etat du jeu sur un flux de sortie.
	 * 
	 * @throws IOException
	 */
	private void envoyerEtatJeu() {
		if (numeroJoueur >= 2) {
			// System.out.println("balle"+balle.x);
			ObjetServeurClient o = new ObjetServeurClient();
			o.balle = balle;
			o.j2 = pan.joueursListe.get(2);
			o.j3 = pan.joueursListe.get(3);
			o.CompteARebour = compteARebour;
			if (listeClients.size() > 1) {
				o.ScoreJoueur = listeClients.get(0).score;
				o.ScoreAdversaire = listeClients.get(1).score;
			}

			o.terminee = terminee;
			try {
				synchronized (listeClients) {
					listeClients.get(0).sendObject(o);
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				synchronized (listeClients) {
					listeClients.remove(0);
				}
				numeroJoueur--;
			}

			o.balle = balle.inverser();
			o.j2 = pan.joueursListe.get(1).numero(2);
			o.j3 = pan.joueursListe.get(0).numero(3);
			o.CompteARebour = compteARebour;
			if (listeClients.size() > 1) {
				o.ScoreJoueur = listeClients.get(1).score;
				o.ScoreAdversaire = listeClients.get(0).score;
			}
			o.terminee = terminee;
			try {
				synchronized (listeClients) {
					listeClients.get(1).sendObject(o);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				synchronized (listeClients) {
					listeClients.remove(1);
				}
				numeroJoueur--;
			}
		}

	}

	private boolean collision(Rectangle d) {
		if (Collision.CollisionCercleAABB(balle, d)) {
			if (true){//!d.enContactAvecLaBalle) {
				// il y a contacte, il faut g�rer la collision
				// gestion des contacts que sur les bordures, type E; C,D exclus
				// car improbables...
				boolean b = false;
				if (balle.x - balle.r < d.x && balle.x + balle.r >= d.x
						&& balle.y + 3 * balle.r / 4 >= d.y
						&& balle.y - 3 * balle.r / 4 <= (d.y + d.h)) {
					// balle arrive selon ex et rebondit a droite
					balle.vx = - Math.abs(balle.vx) -2* d.vx;
					b = true;
				}
				if (balle.x + balle.r > (d.x + d.w)
						&& balle.x - balle.r <= (d.x + d.w)
						&& balle.y + 3 * balle.r / 4 >= d.y
						&& balle.y - 3 * balle.r / 4 <= (d.y + d.h)) {
					// balle arrive selon -ex et rebondit a gauche &&
					// Math.abs(balle.x - d.x) < Math.abs(balle.y - d.y) * 0.95
					balle.vx =  Math.abs(balle.vx) -2* d.vx;
					b = true;
				}
				if (balle.y + balle.r > (d.y + d.h)
						&& balle.y - balle.r <= (d.y + d.h)
						&& balle.x + 3 * balle.r / 4 >= d.x
						&& balle.x - 3 * balle.r / 4 <= (d.x + d.w)) {
					// balle arrive selon ey et rebondit en haut &&
					// Math.abs(balle.x - d.x) * 0.95 > Math.abs(balle.y- d.y)
					balle.vy =  Math.abs(balle.vy) -2* d.vy;
					b = true;
				}
				if (balle.y - balle.r < d.y && balle.y + balle.r >= d.y
						&& balle.x + 3 * balle.r / 4 >= d.x
						&& balle.x - 3 * balle.r / 4 <= (d.x + d.w)) {
					// balle arrive selon -ey et rebondit en bas && balle.x >=
					// d.x && balle.x <= (d.x + d.w)
					balle.vy =  -  Math.abs(balle.vy) -2* d.vy;
					b = true;
				}
				if (b)
					d.enContactAvecLaBalle = true;
				else {
					System.err.println("absence de collision");
					balle.vy = rebondVitesse(balle.vy, d.vy);
					balle.vx = rebondVitesse(balle.vx, d.vy);
					d.enContactAvecLaBalle = true;
				}
			}
		} else {
			d.enContactAvecLaBalle = false;
		}
		return false;
	}

	private double rebondVitesse(double vx, double vobs) {
		return -vx - 2. * vobs;
	}

	public void run() {
		while (run) {
			synchronized (tabsJoueurs) {
				pan.joueursListe = new ArrayList<Joueurs>(
						Arrays.asList(tabsJoueurs));
			}
			pan.balle = balle;
			pan.CompteARebour = compteARebour;
			pan.terminee = false;
			pan.repaint();
			envoyerEtatJeu();
			if (numeroDernierJoueur() < 2) {
				System.out.println(numeroDernierJoueur() + "");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					run = false;
				}
			}

			else {
				if (!pause && !terminee) {
					// tabsJoueurs = new Joueurs[4];
					deplacerballe();
					verifBut();
					// System.out.println(tabsJoueurs.length);
					for (int i = 0; i < 4; i++) {
						// System.out.println(i);
						collisionJoueurs(pan.joueursListe.get(i));
					}
					for (Rectangle d : obstacles) {
						collision(d);
						// System.out.println("x:" + d.x + " y:" + d.y);
					}

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						run = false;
					}
				} else if (!terminee) {
					compteARebour -= 0.1;
					if (compteARebour <= 0) {
						compteARebour = 10;
						pause = false;
						balle = newBalle();
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}// partie terminee
				else {
					run = false;

				}
			}
		}
	}

	private Balle newBalle() {
		double v = Math.random() * 3;
		double theta = Math.random() * 2 * Math.PI;
		return new Balle(Pannel.width / 2, Pannel.height / 2, 15, Color.black,
				v * Math.cos(theta), v * Math.sin(theta));
	}

	private void deplacerballe() {
		final double MAX = 5.;
		final double MIN = 0.5;
//		balle.vx = (balle.vx > MAX) ? MAX : balle.vx;
//		balle.vx = (balle.vx < -MAX) ? -MAX : balle.vx;
//		balle.vy = (balle.vy > MAX) ? MAX : balle.vy;
//		balle.vy = (balle.vy < -MAX) ? -MAX : balle.vy;
//		
//		balle.vx = (balle.vx < MIN && balle.vx > 0) ? MIN : balle.vx;
//		balle.vx = (balle.vx > -MIN && balle.vx < 0) ? -MIN : balle.vx;
//		balle.vy = (balle.vy < MIN && balle.vy > 0) ? MIN : balle.vy;
//		balle.vy = (balle.vy > -MIN && balle.vy < 0) ? -MIN : balle.vy;
		double norme = Math.sqrt(balle.vx*balle.vx+balle.vy*balle.vy);
		balle.vx=(norme<MIN)? MIN*balle.vx/norme : balle.vx;
		balle.vy=(norme<MIN)? MIN*balle.vy/norme : balle.vy;
		
		balle.vx=(norme>MAX)? MAX*balle.vx/norme : balle.vx;
		balle.vy=(norme>MAX)? MAX*balle.vy/norme : balle.vy;
		
		
		balle.vx *= 0.999;
		balle.vy *= 0.999;
		balle.x += balle.vx;
		balle.y += balle.vy;
		// System.out.println("x" + balle.x + "y" + balle.y);
	}

	private void collisionJoueurs(Joueurs j) {
		// TODO Auto-generated method stub
		j.maj();
		for (Joueur joueur : j.joueurs) {
			if (joueur.presence)
				collision(joueur);
			// pan.listeBordure.add(joueur);

		}

	}

	private void verifBut() {
		for (int i = 0; i < 2; i++) {
			Rectangle r =pan.listeConstanteBUT.get(i);
			if (Collision.CollisionCercleAABB(balle,r) && balle.y+balle.r<=r.y+r.h && balle.y-balle.r>r.y) {
				listeClients.get(i).score++;
				if (listeClients.get(i).score >= SCOREMAX) {
					terminee = true;
				}
				pause = true;
			}
		}

	}

	static public void main(String[] args) {
		ServeurBaBy arbitre = new ServeurBaBy();

	}
}

/**
 * Classe pour gerer dans un thread les interactions avec un joueur connecte via
 * le reseau.
 */
class ThreadBaby extends Thread {
	private int numeroJoueur;
	private ServeurBaBy arbitre;
	private Socket sock;
	ObjectInputStream in;
	ObjectOutputStream out;
	private boolean stop;
	int score = 0;

	String demande;
	Joueurs[] joueurs = new Joueurs[2];

	public ThreadBaby(ServeurBaBy a, Socket s, ObjectInputStream in,
			ObjectOutputStream out) {
		arbitre = a;
		numeroJoueur = arbitre.numeroDernierJoueur();
		sock = s;
		this.in = in;
		this.out = out;
		score = 0;
	}

	public void sendObject(ObjetServeurClient arg0) throws IOException {
		out.writeObject(arg0);
		out.reset();
		out.flush();
	}

	public void run() {
		try {
			while (!stop) {
				ObjetClientServeur o = (ObjetClientServeur) in.readObject();
				joueurs[0] = o.j0;
				joueurs[1] = o.j1;

				synchronized (arbitre.tabsJoueurs) {

					if (numeroJoueur == 1) {
						o.j0 = o.j0;
						o.j1 = o.j1;
						arbitre.tabsJoueurs[0] = o.j0;
						arbitre.tabsJoueurs[1] = o.j1;
					}
					if (numeroJoueur == 2) {
						o.j0 = o.j0.changementSansCopyNumero(3);
						o.j1 = o.j1.changementSansCopyNumero(2);
						arbitre.tabsJoueurs[3] = o.j0;
						arbitre.tabsJoueurs[2] = o.j1;

					}
				}

				sleep(5);
			}
		} catch (Exception e) {
			System.err.println(e);
			stop = true;
		}
	}
}
