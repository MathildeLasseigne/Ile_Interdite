package modele;

import java.util.ArrayList;
import java.util.Random;


public class Ile {
	private Zone[][] territoire;
	private ArrayList<Coord> submerg = new ArrayList<>();
	private ArrayList<Coord> innond = new ArrayList<>();
	
	public static final int HAUTEUR=40, LARGEUR=60;
	
	public Ile() {
		initialize();
	}
	
	/**
	 * Initialise les zones de l'ile, telles que leur etat soit normal
	 */
	private void initialize() {
		territoire = new Zone[HAUTEUR][LARGEUR];
		for (int i = 0; i<HAUTEUR; i++) {
			for(int j = 0; j < LARGEUR; j++) {
				territoire[i][j] = new Zone(new Coord(i,j));
			}
		}
		submerg = getCadre(); //Considere les coordonnees hors de l'ile comme submergees
	}
	
	public void inonde() {
		int rand1 = rangedRandomInt(0, submerg.size()+innond.size()-1);
		int rand2 = rangedRandomInt(0, submerg.size()+innond.size()-1);
		int rand3 = rangedRandomInt(0, submerg.size()+innond.size()-1);
		
		int[] rand = new int[3];
		
	}
	
	
	/**
	 * Verifie si la coordonnee est sur la bordure de l'ile
	 * @param c
	 * @return
	 */
	private boolean estBordure(Coord c) {
		if ((c.getAbsc() == LARGEUR-1) || (c.getAbsc() == 0) || (c.getOrd() == HAUTEUR-1) || (c.getOrd() == 0)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Verifie si la coordonnee est a l'interieur de l'ile
	 * @param c
	 * @return
	 */
	private boolean estSurIle(Coord c) {
		if ((c.getAbsc() > LARGEUR-1) || (c.getAbsc() < 0) || (c.getOrd() > HAUTEUR-1) || (c.getOrd() < 0)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Renvoie une coordonnée adjacente à c si elle n'est pas submergée
	 * @param c
	 * @return
	 */
	public Coord getVoisin(Coord c) {
		Coord[] voisins = new Coord[4];
		voisins[0] = new Coord(c.getAbsc(), c.getOrd()+1);
		voisins[1] = new Coord(c.getAbsc()+1, c.getOrd());
		voisins[2] = new Coord(c.getAbsc(), c.getOrd()-1);
		voisins[3] = new Coord(c.getAbsc()-1, c.getOrd());
		Coord vois = null;
		boolean valid = false;
		
		while (valid == false) {
			vois = voisins[rangedRandomInt(0,3)];
			if(estSurIle(vois) && ! submerg.contains(vois)) {
				valid = true;
			}
		}
		return vois;
	}
	
	/**
	 * Récupère les coordonnees faisant partie du "cadre" de la grille
	 * @return ArrayList de Coord
	 */
	private ArrayList<Coord> getCadre() {
		ArrayList<Coord> cadre = new ArrayList<>();
		for(int i = 0; i<HAUTEUR; i++) {
			cadre.add(new Coord(-1, i)); 
			cadre.add(new Coord(LARGEUR, i));
			if (estSurIle(new Coord(-1, i)) || estSurIle(new Coord(LARGEUR, i))) {
				System.out.println("Probleme creation cadre hauteur");
			}
		}
		for(int j = 0; j<LARGEUR; j++) {
			cadre.add(new Coord(j, -1));
			cadre.add(new Coord(j, HAUTEUR));
			if (estSurIle(new Coord(j, -1)) || estSurIle(new Coord(j, HAUTEUR))) {
				System.out.println("Probleme creation cadre largeur");
			}
		}
		cadre.add(new Coord(-1, -1));
		cadre.add(new Coord(-1, HAUTEUR));
		cadre.add(new Coord(LARGEUR, -1));
		cadre.add(new Coord(LARGEUR, HAUTEUR));
		
		return cadre;
	}
	
	/**
	 * Verifie si la coord c1 est voisine de la coord c
	 * @param c
	 * @param c2
	 * @return
	 */
	public boolean estVoisin(Coord c, Coord c2) {
		if(estSurIle(c2) && ! submerg.contains(c2)) {
			if ((c == new Coord(c.getAbsc(), c.getOrd()+1)) || c == new Coord(c.getAbsc()+1, c.getOrd()) || c == new Coord(c.getAbsc(), c.getOrd()-1) || c == new Coord(c.getAbsc()-1, c.getOrd())) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * Cree un int random compris dans un certain intervalle [rangeMin, rangeMax]
	 * @param rangeMin
	 * @param rangeMax La borne maximale du random (doit etre positive !!)
	 * @return la valeur du int
	 */
	public int rangedRandomInt(int rangeMin, int rangeMax) {
		Random r = new Random();
		int randomValue = rangeMin + r.nextInt(rangeMax +1);
		return randomValue;
	}
}
