package modele;

import java.util.ArrayList;
import java.util.Random;
import observateur.Observable;;


public class Ile extends Observable {
	/**
	 * Liste des zones de l'ile, arrangees selon leurs coordonnees
	 */
	private Zone[][] territoire;
	/**
	 * Liste des zones submergees
	 */
	private ArrayList<Coord> submerg = new ArrayList<>();
	/**
	 * Liste des zones innondees
	 */
	private ArrayList<Coord> innond = new ArrayList<>();
	/**
	 * Liste des zones ayant un voisin non submergé et au moins un submerge
	 * <br/>Mis a jour par cleanShore()
	 */
	private ArrayList<Coord> shore = new ArrayList<>();
	
	public static final int HAUTEUR=40, LARGEUR=40;
	/**
	 * Le nombre de coordonnees utilisees par l ile (ile+cadre)
	 */
	private int nbCoord;
	
	public Ile() {
		initialize();
	}
	
	public boolean updateIle() {
		notifyObservers();
		return inonde(30);
		
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
		initShore();
		nbCoord = (HAUTEUR*LARGEUR)+getCadre().size();
	}
	
	
	/**
	 * 
	 */
	/**
	 * Inonde nb zones non submergees
	 * @param nb Le nombre de zones a submerger
	 * @return Indique s'il reste assez de zones a submerger
	 */
	private boolean inonde(int nb) {
		if(this.nbCoord-submerg.size() < nb) {
			return false;
		}
		
		for (int i=0; i<nb; i++) {
			Coord c;
			c = getRandCoordVois();
			subCoordList(territoire[c.getOrd()][c.getAbsc()]);
			territoire[c.getOrd()][c.getAbsc()].inonde();
			addCoordList(territoire[c.getOrd()][c.getAbsc()]);
			shore.add(c);
			cleanShore();
		}
		
		return true;
	}
	
	/**
	 * Recupere au hazard une coord parmis les voisines de shore qui ne sont pas submergees
	 * @return
	 */
	private Coord getRandCoordVois() {
		int rand = rangedRandomInt(0, shore.size()-1);
		Coord c;
		
		c = getVoisin(shore.get(rand), false);
		if (c==null) {
			c = getRandCoordVois();
		}
		
		return c;
	}
	
//	/**
//	 * Recupere au hazard une coord parmis les voisines de shore et innond qui ne sont pas submergees
//	 * @return
//	 */
//	private Coord getRandCoordVois() {
//		int rand = rangedRandomInt(0, shore.size()+innond.size()-1);
//		Coord c;
//		if (rand >= shore.size()) {
//			c = getVoisin(innond.get(rand-shore.size()), false);
//			if (c==null) { //Au cas ou la zone inondee soit entouree de zones submergees (ilot)
//				c = getRandCoordVois();
//			}
//		} else {
//			c = getVoisin(shore.get(rand), false);
//			if (c==null) {
//				c = getRandCoordVois();
//			}
//		}
//		return c;
//	}
	
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
	 * Verifie si la zone de coordonne c n est pas submergee mais entouree de zones submergees
	 * @param c
	 * @return
	 */
	private boolean estIlot(Coord c) {
		if(submerg.contains(c)) {
			return false;
		}
		ArrayList<Coord> vois = getListVoisins(c);
		boolean isIlot = true;
		for (Coord cVois : vois) {
			if(getZone(cVois).estAccessible()) {
				isIlot = false;
			}
		}
		return isIlot;
	}
	
	/**
	 * Renvoie une coordonnée adjacente à c
	 * @param c 
	 * @param sub true si on garde les voisins submergés
	 * @return null si sub == false et c est entouree de zones submergees
	 */
	public Coord getVoisin(Coord c, boolean sub) {
		
		if(estIlot(c) && sub==false) {
			return null;
		}
		
		ArrayList<Coord> voisList = getListVoisins(c);
		
		Coord vois = null;
		boolean valid = false;
		
		while (valid == false) {
			vois = voisList.get(rangedRandomInt(0,voisList.size()-1));
			if (sub == false) {
				if(! submerg.contains(vois)) {
					valid = true;
				}
			} else {
				valid = true;
			}
		}
		return vois;
	}
	
	private ArrayList<Coord> getListVoisins(Coord c){
		ArrayList<Coord> vois = new ArrayList<>();
		Coord[] voisins = new Coord[4];
		voisins[0] = new Coord(c.getAbsc(), c.getOrd()+1);
		voisins[1] = new Coord(c.getAbsc()+1, c.getOrd());
		voisins[2] = new Coord(c.getAbsc(), c.getOrd()-1);
		voisins[3] = new Coord(c.getAbsc()-1, c.getOrd());
		for(int i=0; i<4; i++) {
			if(estSurIle(voisins[i])) {
				vois.add(voisins[i]);
			}
		}
		
		return vois;
	}
	
	/**
	 * Verifie si la coord c2 est voisine de la coord c (Quelque soit l etat de c)
	 * @param c
	 * @param c2
	 * @param sub true si on garde c2 submergés
	 * @return
	 */
	public boolean estVoisin(Coord c, Coord c2, boolean sub) {
		ArrayList<Coord> vois = getListVoisins(c);
		if (sub == false) {
			if( ! submerg.contains(c2)) {
				return vois.contains(c2);
			}
		} else {
			return vois.contains(c2);
		}
		return false;
	}

	/**
	 * Met a jour la liste des zones ayant un voisin non submergé et au moins un submerge
	 */
	private void cleanShore() {
		for(int i = 0; i<this.shore.size(); i++) {
			ArrayList<Coord> vois = getListVoisins(this.shore.get(i));
			boolean isShore = false;
			for (Coord cVois : vois) {
				if(getZone(cVois).estAccessible()) {
					isShore = true;
				}
			}
			if(isShore == false) {
				this.shore.remove(i); 
				//Si la coord n a pas au moins un voisin accessible, l enlever de la liste shore
			}
		}
	}	
	
	/**
	 * Initialise le cadre en tant que shore sauf les coins
	 */
	private void initShore() {
		for(Coord c : getCadre()) {
			ArrayList<Coord> vois = getListVoisins(c);
			if(vois.size()>1) {
				System.out.println("Probleme initialisation shore coord" + c +"\n");
			} else if (vois.size()==1) {
				this.shore.add(c);
			}
			
		}
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
				System.out.println("Probleme creation cadre hauteur\n");
			}
		}
		for(int j = 0; j<LARGEUR; j++) {
			cadre.add(new Coord(j, -1));
			cadre.add(new Coord(j, HAUTEUR));
			if (estSurIle(new Coord(j, -1)) || estSurIle(new Coord(j, HAUTEUR))) {
				System.out.println("Probleme creation cadre largeur\n");
			}
		}
		cadre.add(new Coord(-1, -1));
		cadre.add(new Coord(-1, HAUTEUR));
		cadre.add(new Coord(LARGEUR, -1));
		cadre.add(new Coord(LARGEUR, HAUTEUR));
		
		return cadre;
	}
	
	/**
	 * Retourne la zone de coordonnees c
	 * @param c
	 * @return
	 */
	public Zone getZone(Coord c) {
		return territoire[c.getOrd()][c.getAbsc()];
	}
	
	/**
	 * Retourne la zone d ordonee x et d abscisse y
	 * @param x
	 * @param y
	 * @return
	 */
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
