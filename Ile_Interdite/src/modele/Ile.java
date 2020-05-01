package modele;

import java.util.ArrayList;
import java.util.Random;
import observateur.Observable;;


public class Ile extends Observable {
	private Zone[][] territoire;
	private ArrayList<Coord> submerg = new ArrayList<>();
	private ArrayList<Coord> innond = new ArrayList<>();
	
	public static final int HAUTEUR=40, LARGEUR=60;
	
	public Ile() {
		initialize();
	}
	
	public void updateIle() {
		inonde();
		notifyObservers();
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
		
		for (int i=0; i<3; i++) {
			int rand = rangedRandomInt(0, submerg.size()+innond.size()-1);
			Coord c;
			if (rand >= submerg.size()) {
				c = getVoisin(innond.get(rand), true);
			} else {
				c = getVoisin(submerg.get(rand), true);
			}
			subCoordList(territoire[c.getOrd()][c.getAbsc()]);
			territoire[c.getOrd()][c.getAbsc()].inonde();
			addCoordList(territoire[c.getOrd()][c.getAbsc()]);
		}
		
	}
	
	/**
	 * Asseche une zone à la coordonee c
	 * @param c
	 * @return true si la zone a pu etre assechee
	 */
	public boolean asseche(Coord c) {
		return territoire[c.getOrd()][c.getAbsc()].asseche();
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
	 * Ajoute les coordonnees de z à la liste submerg ou innond en fonction de son etat
	 * @param z
	 */
	private void addCoordList(Zone z) {
		if (z.getEtat() == Etat.submergee) {
			this.submerg.add(z.getCoord());
		} else if (z.getEtat() == Etat.inondee) {
			this.innond.add(z.getCoord());
		}
	}
	
	/**
	 * Retire les coordonnees de z à la liste submerg ou innond en fonction de son etat
	 * @param z
	 */
	private void subCoordList(Zone z) {
		if (z.getEtat() == Etat.submergee) {
			this.submerg.remove(z.getCoord());
		} else if (z.getEtat() == Etat.inondee) {
			this.innond.remove(z.getCoord());
		}
	}

	/**
	 * Renvoie une coordonnée adjacente à c
	 * @param c 
	 * @param sub true si on garde les voisins submergés
	 * @return
	 */
	public Coord getVoisin(Coord c, boolean sub) {
		Coord[] voisins = new Coord[4];
		voisins[0] = new Coord(c.getAbsc(), c.getOrd()+1);
		voisins[1] = new Coord(c.getAbsc()+1, c.getOrd());
		voisins[2] = new Coord(c.getAbsc(), c.getOrd()-1);
		voisins[3] = new Coord(c.getAbsc()-1, c.getOrd());
		Coord vois = null;
		boolean valid = false;
		
		while (valid == false) {
			vois = voisins[rangedRandomInt(0,3)];
			if (sub == false) {
				if(estSurIle(vois) && ! submerg.contains(vois)) {
					valid = true;
				}
			} else {
				if(estSurIle(vois)) {
					valid = true;
				}
			}
		}
		return vois;
	}
	
	/**
	 * Verifie si la coord c1 est voisine de la coord c
	 * @param c
	 * @param c2
	 * @param sub true si on garde les voisins submergés
	 * @return
	 */
	public boolean estVoisin(Coord c, Coord c2, boolean sub) {
		if (sub == false) {
			if(estSurIle(c2) && ! submerg.contains(c2)) {
				if ((c == new Coord(c.getAbsc(), c.getOrd()+1)) || c == new Coord(c.getAbsc()+1, c.getOrd()) || c == new Coord(c.getAbsc(), c.getOrd()-1) || c == new Coord(c.getAbsc()-1, c.getOrd())) {
					return true;
				} else {
					return false;
				}
			}
		} else {
			if(estSurIle(c2)) {
				if ((c == new Coord(c.getAbsc(), c.getOrd()+1)) || c == new Coord(c.getAbsc()+1, c.getOrd()) || c == new Coord(c.getAbsc(), c.getOrd()-1) || c == new Coord(c.getAbsc()-1, c.getOrd())) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
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
	
	public Zone getZone(Coord c) {
		return territoire[c.getOrd()][c.getAbsc()];
	}
	public Zone getZone(int x, int y) {
		return territoire[x][y];
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
