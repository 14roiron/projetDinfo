import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class ClientMainActivity extends JFrame {
	Pannel pan;
	Reseau res;

	public static void main(String[] args) {

		new ClientMainActivity(args);
	}

	public ClientMainActivity(String[] args) {
		pan = new Pannel();
		this.setTitle("Baby");
		this.setSize(Pannel.width, Pannel.height + 40 + 40 + 20);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setContentPane(pan);
		this.setVisible(true);
		pan.CreerConstantes();
		this.addKeyListener(new KeyListener() {


			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyChar() == 'z') {
					if (pan.joueursListe.get(0).Vposition < 0)
						pan.joueursListe.get(0).Vposition = 0;
					else
						pan.joueursListe.get(0).Vposition=0;//--
				}
				if (arg0.getKeyChar() == 's') {
					if (pan.joueursListe.get(0).Vposition > 0)
						pan.joueursListe.get(0).Vposition = 0;
					else
						pan.joueursListe.get(0).Vposition=0;//++;
				}
				if (arg0.getKeyChar() == 'q') {
					if (pan.joueursListe.get(0).Vangle < 0)
						pan.joueursListe.get(0).Vangle = 0;
					else
						pan.joueursListe.get(0).Vangle=0;//++;
				}
				if (arg0.getKeyChar() == 'd') {
					if (pan.joueursListe.get(0).Vangle > 0)
						pan.joueursListe.get(0).Vangle = 0;
					else
						pan.joueursListe.get(0).Vangle=0;//--;
				}
				if (arg0.getKeyChar() == 'y') {
					if (pan.joueursListe.get(1).Vposition < 0)
						pan.joueursListe.get(1).Vposition = 0;
					else
						pan.joueursListe.get(1).Vposition=0;//--;
				}
				if (arg0.getKeyChar() == 'h') {
					if (pan.joueursListe.get(1).Vposition > 0)
						pan.joueursListe.get(1).Vposition = 0;
					else
						pan.joueursListe.get(1).Vposition=0;//++;
				}
				if (arg0.getKeyChar() == 'g') {
					if (pan.joueursListe.get(1).Vangle > 0)
						pan.joueursListe.get(1).Vangle = 0;
					else
						pan.joueursListe.get(1).Vangle=0;//++;
				}
				if (arg0.getKeyChar() == 'j') {
					if (pan.joueursListe.get(1).Vangle < 0)
						pan.joueursListe.get(1).Vangle = 0;
					else
						pan.joueursListe.get(1).Vangle=0;//--;
				}

			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("touche" + arg0.getKeyChar());
				if (arg0.getKeyChar() == 'z') {
					pan.joueursListe.get(0).Vposition--;
				}
				if (arg0.getKeyChar() == 's') {
					pan.joueursListe.get(0).Vposition++;
				}
				if (arg0.getKeyChar() == 'q') {
					pan.joueursListe.get(0).Vangle--;
				}
				if (arg0.getKeyChar() == 'd') {
					pan.joueursListe.get(0).Vangle++;
				}
				if (arg0.getKeyChar() == 'y') {
					pan.joueursListe.get(1).Vposition--;
				}
				if (arg0.getKeyChar() == 'h') {
					pan.joueursListe.get(1).Vposition++;
				}
				if (arg0.getKeyChar() == 'g') {
					pan.joueursListe.get(1).Vangle--;
				}
				if (arg0.getKeyChar() == 'j') {
					pan.joueursListe.get(1).Vangle++;
				}
			}
		});
		JMenuBar mb = new JMenuBar();
		this.setJMenuBar(mb);
		JMenuItem menu = new JMenuItem();
		// affecter le texte du menu
		menu.setText("Aide");
		// affecter l'icône du menu
		//menu.setIcon(new ImageIcon(getClass().getResource("aide.png")));
		// affecter le raccourci alt+M
		menu.setMnemonic(KeyEvent.VK_M);
		// ajouter le menu dans la barre de menu
		menu.setEnabled(true);
		menu.addActionListener(new ActionListener() {
			JFrame f;
			ActionListener set (JFrame f)
			{
				this.f=f;
				return this;
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JOptionPane.showMessageDialog(f,
					    "Pour Jouer, Merci d'attendre qu'un  autre joueur se connecte.\n\n" +
					    "Pour Jouer, vous pouvez utilisez les Touches (Z,S,Q,D) pour le goal, et ( Y,H,G,J) pour les attaquants." +
					    "\n\n" +
					    "Vous Pouvez aussi Brancher une manette");
			}
		}.set(this));

		
		mb.add(menu);
	

		res = new Reseau(args);
		res.start();
		go();
	}

	private void go() {
		while (true) {
			pan.repaint();
			try {
				res.write();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class Reseau extends Thread implements Runnable {
		ObjectOutputStream ecriture;
		// Ouverture flux lecture sur socket
		ObjectInputStream lecture;

		public Reseau(String[] args) {
			try {
				InetAddress adresseIP = null;

				if (args.length > 0) {
					// S'il y a au moins un argument, prendre la machine dont le
					// nom
					// ou l'adresse est en argument
					adresseIP = InetAddress.getByName(args[0]);
				} else {
					// Par d�faut, se connecter en local
					adresseIP = InetAddress.getLocalHost();
				}
				// Ouverture de socket sur le port 8888 de la machine locale
				Socket client = new Socket(adresseIP, 8888);
				// Ouverture flux �criture sur socket
				ecriture = new ObjectOutputStream(client.getOutputStream());
				lecture = new ObjectInputStream(client.getInputStream());

				// Ouverture flux lecture sur socket

				System.out.println("connsction OK!");
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}

		public void write() throws IOException {
			ObjetClientServeur o = new ObjetClientServeur();
			o.j0 = pan.joueursListe.get(0);
			o.j1 = pan.joueursListe.get(1);
			// System.out.println("joueur"+o.j1.angle+"   "+pan.joueursListe.get(2).angle);
			ecriture.writeObject(o);
			ecriture.reset();
			ecriture.flush();
		}

		public void run() {
			boolean run = true;
			while (run) {
				try {
					ObjetServeurClient o = (ObjetServeurClient) lecture
							.readObject();
					pan.balle = o.balle;
					pan.joueursListe.set(2, o.j2);
					pan.joueursListe.set(3, o.j3);
					pan.CompteARebour = o.CompteARebour;
					pan.ScoreAdversaire = o.ScoreAdversaire;
					pan.ScoreJoueur = o.ScoreJoueur;
					pan.terminee = o.terminee;

				} catch ( IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					run = false;
					e.printStackTrace();
				}
			}
		}

	}
}
