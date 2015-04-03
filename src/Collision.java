public class Collision {

	public static boolean  CollisionAABB(Rectangle box1, Rectangle box2) {
		if ((box2.x >= box1.x + box1.w) // trop � droite
				|| (box2.x + box2.w <= box1.x) // trop � gauche
				|| (box2.y >= box1.y + box1.h) // trop en bas
				|| (box2.y + box2.h <= box1.y)) // trop en haut
			return false;
		else
			return true;
	}

	public static boolean CollisionCercleAABB(Balle C1, Rectangle box1) {
		Rectangle boxCercle = GetBoxAutourCercle(C1); 
		// retourner la bounding  box de l'image
		// porteuse, ou calculer la bounding box.
		if (!CollisionAABB(box1, boxCercle))
			return false; // premier test
		if (CollisionPointCercle(box1.x, box1.y, C1)
				|| CollisionPointCercle(box1.x, box1.y + box1.h, C1)
				|| CollisionPointCercle(box1.x + box1.w, box1.y, C1)
				|| CollisionPointCercle(box1.x + box1.w, box1.y + box1.h, C1))
			return true; // deuxieme test
		if (CollisionPointAABB(C1.x, C1.y, box1))
			return true; // troisieme test
		int projvertical = ProjectionSurSegment(C1.x, C1.y, box1.x, box1.y,
				box1.x, box1.y + box1.h);
		int projhorizontal = ProjectionSurSegment(C1.x, C1.y, box1.x, box1.y,
				box1.x + box1.w, box1.y);
		if (projvertical == 1 || projhorizontal == 1)
			return true; // cas E
		return false; // cas B
	}

	private static boolean CollisionPointAABB(double x, double y, Rectangle box1) {
		if ((x > box1.x && x < (box1.x + box1.w))
				&& (y > box1.y && y < (box1.y + box1.h)))
			return true;
		return false;
	}

	private static boolean CollisionPointCercle(double x, double y, Balle c1) {
		if (((x - c1.x) * (x - c1.x) + (y - c1.y) * (y - c1.y)) < (c1.r * c1.r)) {
			return true;
		}
		return false;
	}

	public static Rectangle GetBoxAutourCercle(Balle c1) {
		// TODO Auto-generated method stub
		return new Rectangle(c1.x - c1.r, c1.y - c1.r, 2 * c1.r, 2 * c1.r,
				c1.c, 0, 0);
	}

	private static int  ProjectionSurSegment(double x, double y, double x2, double y2, double x3, double d) {
		double ACx = x - x2;
		double ACy = y - y2;
		double ABx = x3 - x2;
		double ABy = d - y2;
		double BCx = x - x3;
		double BCy = y - d;
		double s1 = (ACx * ABx) + (ACy * ABy);
		double s2 = (BCx * ABx) + (BCy * ABy);
		if (s1 * s2 > 0)
			return 0;
		return 1;
	}

}
