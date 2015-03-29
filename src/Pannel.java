import java.awt.Color;
import java.awt.Font;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Pannel extends JPanel {
	public static final int height=600;
	public static final int width=800;
	public List<DessinObjet> listeBordure = new ArrayList<DessinObjet>();
	public List<Rectangle> listeConstanteBUT = new ArrayList<Rectangle>();
	public List<DessinObjet> listeConstante = new ArrayList<DessinObjet>();
	public Balle balle;
	public List<Joueurs> joueursListe;
	
	public int ScoreJoueur;
	public int ScoreAdversaire;
	public double CompteARebour;
	public boolean terminee;
	boolean button=false;
	BufferedImage image;

	public Pannel() {
		button=false;
		try {
			image = ImageIO.read(new File("274_2.jpg"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void CreerConstantes() {

		listeBordure.clear();
		listeBordure.add(new Rectangle(0, 0, Pannel.width, 20, Color.green,
				0, 0));
		listeBordure
				.add(new Rectangle(0, 0, 20, Pannel.height, Color.green, 0, 0));
		listeBordure.add(new Rectangle(Pannel.width - 20, 0, 20,
				Pannel.height, Color.green, 0, 0));
		listeBordure.add(new Rectangle(0, Pannel.height - 20, Pannel.width, 20,
				Color.green, 0, 0));

		Rectangle but1 = new Rectangle(10, Pannel.height / 2 - 75, 10, 150,
				Color.RED, 0, 0);
		Rectangle but2 = new Rectangle(Pannel.width - 20, Pannel.height / 2 - 75,
				10, 150, Color.RED, 0, 0);
		listeConstanteBUT.clear();
		listeConstanteBUT.add(but1);
		listeConstanteBUT.add(but2);
		balle = new Balle(Pannel.width / 2, Pannel.height / 2, 20, Color.black,
				-2, 1);

		joueursListe = new ArrayList<Joueurs>();
		joueursListe.add(new Joueurs(0));
		joueursListe.add(new Joueurs(1));
		joueursListe.add(new Joueurs(2));
		joueursListe.add(new Joueurs(3));

	

		

	}

	public void paintComponent(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, Pannel.width, Pannel.height);
		g.drawImage(image, 0, 0, Pannel.width, Pannel.height, null);
		
		draw(g);

	}

	void draw(Graphics g) {
		// CreerConstantes();
		//System.out.println("repaint");

		
		synchronized (listeConstante) {
			listeConstante.clear();
			listeConstante.addAll(listeBordure);
			listeConstante.addAll(listeConstanteBUT);
			listeConstante.add(balle);
			listeConstante.addAll(joueursListe);

			Iterator<DessinObjet> i = listeConstante.iterator();
			while (i.hasNext()) {
				i.next().dessiner(g);

			}
		}
		g.setColor(Color.BLUE);
        Font f = new Font("Book Antiqua", Font.BOLD, 25);
        g.setFont(f);
        String s = "Score: "+ScoreAdversaire+" - "+ScoreJoueur;
        g.drawString(s, width/8, height+30);
        
		if(CompteARebour!=10)
		{
			setOpaque(false);
			 int xCenter=width/2;
			 int  yCenter=height/2; 
			 int r = 30;
			        g.setColor(Color.white);
			        g.fillOval(xCenter-r, yCenter-r, 2*r, 2*r);
			        g.setColor(Color.YELLOW);
			        g.fillArc(xCenter-r, yCenter-r, 2*r, 2*r, 90, (int) -(360-CompteARebour*360/10));
			        g.setColor(Color.black);
			        f = new Font("Book Antiqua", Font.BOLD, 25);
			        g.setFont(f);
			        if(CompteARebour>=10){
			            g.drawString(""+(int)this.CompteARebour, xCenter-2*r/3, yCenter+r/3);
			        }
			        else{
			            g.drawString("0"+(int)this.CompteARebour, xCenter-2*r/3, yCenter+r/3);
			        }
		}
		if(terminee && !button)
		{
			button=true;
			JOptionPane jop1 = new JOptionPane();
			final String[] TEXT=new String[2];
			if(ScoreJoueur>ScoreAdversaire)
			{
				TEXT[0]= "Vous Avez Gagné";
				TEXT[1]="Bravo!";
			}
			else
			{
				TEXT[0]= "Vous Avez perdu";
				TEXT[1]="Dommage!";
			}
		    javax.swing.SwingUtilities.invokeLater(new Runnable() {
		        @Override
		        public void run() {
		                JOptionPane.showMessageDialog(null, TEXT[0], TEXT[1],
		                        JOptionPane.INFORMATION_MESSAGE);
		        }
		    });
		}
	}
}