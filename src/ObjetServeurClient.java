import java.io.Serializable;


public class ObjetServeurClient implements Serializable{

	private static final long serialVersionUID = -6532768341693837919L;
	public Balle balle;
	public Joueurs j2;
	public Joueurs j3;
	public int ScoreJoueur;
	public int ScoreAdversaire;
	public double CompteARebour;
	public boolean terminee;
	public ObjetServeurClient()
	{
		ScoreJoueur=0;
		ScoreAdversaire=0;
	}
	
}
