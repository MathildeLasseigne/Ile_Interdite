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
	
	private Coord heliport;
	private ArrayList<Coord> artefacts = new ArrayList<>();
	
	public static final int HAUTEUR=20, LARGEUR=20;
	/**
	 * Le nombre de coordonnees utilisees par l ile (ile+cadre)
	 */
	private int nbCoord;
	
	/**
	 * Modele de l ile
	 */
	public Ile() {
		initialize();
	}
	
	/**
	 * Met a jour l ile en l inondant
	 * @param nbInnond Le nombre de zones a innonder
	 * @return
	 */
	public boolean updateIle(int nbInnond, boolean useShore) {
		notifyObservers();
		return inonde(nbInnond, useShore);
		
	}
	
	/**
	 * Initialise les zones de l'ile, telles que leur etat soit normal
	 */
	private void initialize() {
		territoire = new Zone[HAUTEUR][LARGEUR];
		for (int i = 0; i<HAUTEUR; i++) {
			for(int j = 0; j < LARGEUR; j++) {
				territoire[i][j] = new Zone(new Coord(j,i));
			}
		}
		submerg = getCadre(); //Considere les coordonnees hors de l'ile comme submergees
		initShore();
		nbCoord = (HAUTEUR*LARGEUR)+getCadre().size();
		
		//Heliport
		//heliport = getRandCoord(6);
		heliport = new Coord(10,10);
		getZone(heliport).setType(new Heliport());
		
		//Artefacts
		for(int element = 0; element<4; element++) {
			Coord cArt = getRandCoord(5);
			while(artefacts.contains(cArt) || cArt.equals(heliport)) {
				cArt = getRandCoord(5);
			}
			artefacts.add(cArt);
			getZone(cArt).setType(new Artefact(element));
		}
	}
	
	
	/**
	 * 
	 */
	/**
	 * 
	 * @return
	 */
	/**
	 * Inonde nb zones non submergees
	 * @param nb Le nombre de zones a submerger
	 * @param useShore Doit on innonder l ile a partir de la cote ?
	 * @return Indique s'il reste assez de zones a submerger
	 */
	private boolean inonde(int nb, boolean useShore) {
		if(this.nbCoord-submerg.size() < nb) {
			return false;
		}
		
		for (int i=0; i<nb; i++) {
			Coord c;
			c = getRandCoordVois(useShore);
			subCoordList(territoire[c.getOrd()][c.getAbsc()]);
			territoire[c.getOrd()][c.getAbsc()].inonde();
			addCoordList(territoire[c.getOrd()][c.getAbsc()]);
			if(useShore) {
				shore.add(c);
				cleanShore();
			}
		}
		
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	/**
	 * Recupere au hazard une coord parmis les voisines qui ne sont pas submergees
	 * @param shore ne prendont en compte que les voisines de shore ?
	 * @return
	 */
	private Coord getRandCoordVois(boolean shoreBool) {
		if(shoreBool) {
			int rand = rangedRandomInt(0, this.shore.size()-1);
			Coord c;
			
			c = getVoisin(this.shore.get(rand), false, null);
			if (c==null) {
				c = getRandCoordVois(shoreBool);
			}
			
			return c;
		} else {
			Coord c;
			
			c = getVoisin(rangedRandomCoord(0,LARGEUR-1, 0, HAUTEUR-1), false, null);
			if (c==null) {
				c = getRandCoordVois(shoreBool);
			}
			return c;
		}
		
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
		if(! estSurIle(c)) {
			return false;
		}
		Zone z = getZone(c);
		boolean result = z.asseche();
		if(result) {
			subCoordList(z);
			cleanShore();
		}
		return result;	
	}
	
	
	
	/**
	 * Verifie si la coordonnee est a l'interieur de l'ile
	 * @param c
	 * @return
	 */
	public boolean estSurIle(Coord c) {
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
	 * @param except La liste des voisins a ne pas prendre (peut etre null)
	 * @return null si sub == false et c est entouree de zones submergees 
	 * ou si tous les voisins dispos sont dans la liste d exceptions
	 */
	public Coord getVoisin(Coord c, boolean sub, ArrayList<Coord> except) {
		
		if(estIlot(c) && sub==false) {
			return null;
		}
		
		ArrayList<Coord> voisList = getListVoisins(c);
		
		if(except != null) {  //Verifie s il est possible d avoir un voisin en prenant en compte except
			int voisinsDisp = 0;
			for(Coord cVois : voisList) {
				if(sub == true) {  //Besoin car comme c est submerg (car sauve), c n est pas ilot !
					if(! except.contains(cVois)) {
						voisinsDisp++;
					}
				} else {
					if((! except.contains(cVois)) && ! submerg.contains(cVois)) {
						voisinsDisp++;
					}
				}
				
			}
			if(voisinsDisp == 0) {
				return null;
			}
		}
		
		
		Coord vois = null;
		boolean valid = false;
		
		while (valid == false) {
			vois = voisList.get(rangedRandomInt(0,voisList.size()-1));
			if (sub == false) {
				if(except != null) {
					if((! submerg.contains(vois)) && (! except.contains(vois))) {
						valid = true;
					}
				} else {
					if(! submerg.contains(vois)) {
						valid = true;
					}
				}
				
			} else {
				if(except != null) {
					if(! except.contains(vois)) {
						valid = true;
					}
				} else {
					valid = true;
				}
			}
		}
		return vois;
	}
	
	
	
	/**
	 * Recuperer la liste des coord adjacentes a la coord c
	 * @param c
	 * @return
	 */
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
	 * Recuperer la liste des coord adjacentes a la coord c
	 * @param c
	 * @param diagonales Les diagonales sont-elles prises en compte ?
	 * @param sub Prend en compte les zones submergees ?
	 * @return
	 */
	private ArrayList<Coord> getListVoisins(Coord c, boolean diagonales, boolean sub){
		if(! diagonales) {
			if(sub) {
				return getListVoisins(c);
			} else {
				ArrayList<Coord> vois = new ArrayList<>();
				ArrayList<Coord> voisSub = getListVoisins(c);
				for(Coord cVois : voisSub) {
					if(! isSubmerged(cVois)) {
						vois.add(cVois);
					}
				}
				return vois;
			}
			
		} else {
			ArrayList<Coord> vois = getListVoisins(c);
			Coord[] voisins = new Coord[4];
			voisins[0] = new Coord(c.getAbsc()-1, c.getOrd()-1);
			voisins[1] = new Coord(c.getAbsc()+1, c.getOrd()-1);
			voisins[2] = new Coord(c.getAbsc()+1, c.getOrd()+1);
			voisins[3] = new Coord(c.getAbsc()-1, c.getOrd()+1);
			for(int i=0; i<4; i++) {
				if(sub) {
					if(estSurIle(voisins[i])) {
						vois.add(voisins[i]);
					}
				} else {
					if(estSurIle(voisins[i]) && !isSubmerged(voisins[i])) {
						vois.add(voisins[i]);
					}
				}
			}
			
			return vois;
		}
	}
	

	/**
	 * Verifie si la coord c2 est voisine de la coord c (Quelque soit l etat de c)
	 * @param c
	 * @param c2
	 * @param diagonales Les diagonales sont-elles prises en compte ?
	 * @param sub true si on garde c2 submergés
	 * @return
	 */
	public boolean estVoisin(Coord c, Coord c2, boolean diagonales, boolean sub) {
		ArrayList<Coord> vois = getListVoisins(c, diagonales, sub);
		if (sub == false) {
			if( ! submerg.contains(c2)) {
				return vois.contains(c2) && estSurIle(c2);
			}
		} else {
			return vois.contains(c2) && estSurIle(c2);
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
			if(estSurIle(this.shore.get(i))) {
				if(getZone(this.shore.get(i)).getEtat() == Etat.normale) {
					isShore = false;
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
		return territoire[y][x];
	}
	
//	/**
//	 * Retourne la coord (x,y) en prenant en compte les pointeurs
//	 * @param x
//	 * @param y
//	 * @return
//	 */
//	public Coord getCoord(int x, int y) {
//		Coord c = new Coord(x, y);
//		if(estSurIle(c)) {
//			return territoire[y][x].getCoord();
//		} else {
//			return c;
//		}
//		
//	}
//	
//	/**
//	 * Retourne la coord en prenant en compte les pointeurs
//	 * @param c
//	 * @return
//	 */
//	public Coord getCoord(Coord c) {
//		if(estSurIle(c)) {
//			return territoire[c.getAbsc()][c.getOrd()].getCoord();
//		} else {
//			return c;
//		}
//	}
	
	/**
	 * Verifie si la zone de coord c est safe
	 * @param c
	 * @return return false si c == null
	 */
	public boolean isSafe(Coord c) {
		if(c == null) {
			return false;
		}
		if(estSurIle(c)) {
			if(getZone(c).estAccessible()) {
				return true;
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
	
	/**
	 * Recupere une coord random tant qu elle est sur l ile
	 * @param x1 x min
	 * @param x2 x max
	 * @param y1 y min
	 * @param y2 y max
	 * @return
	 */
	public Coord rangedRandomCoord(int x1, int x2, int y1, int y2) {
		Coord c = new Coord(rangedRandomInt(x1, x2), rangedRandomInt(y1, y2));
		if(!estSurIle(c)) {
			System.out.println("rangedRandomCoord() creation hors ile");
		}
		return c;
	}
	
	/**
	 * Renvoie la position de l heliport
	 * @return
	 */
	public Coord getHeliport() {
		return heliport;
	}

	/**
	 * Decoupe l ile en 4 secteurs et choisi une coord au hasard selon les criteres voulus :
	 * </br>1 : Center
	 * </br>2 : Inter
	 * </br>3 : Outer
	 * </br>4 : Border (3 zones a cote de la bordure)
	 * </br>5 : All - Border
	 * </br>6 : Center + Inter
	 * @param range Le secteur de la coordonnee voulue
	 * @return
	 */
	public Coord getRandCoord(int range) {
		Coord c = rangedRandomCoord(0, LARGEUR-1, 0, HAUTEUR-1);
		if (range == 1) {
			while(! isInSector(1, c)) {
				c = rangedRandomCoord(0, LARGEUR-1, 0, HAUTEUR-1);
			}
		} else if (range == 2) {
			while(! ( isInSector(2, c) && ! isInSector(1, c))) {
				c = rangedRandomCoord(0, LARGEUR-1, 0, HAUTEUR-1);
			}
		} else if (range == 3) {
			while(! ( isInSector(3, c) && ! isInSector(2, c))) {
				c = rangedRandomCoord(0, LARGEUR-1, 0, HAUTEUR-1);
			}
		} else if (range == 4) {
			while(! ( isInSector(4, c) && ! isInSector(3, c))) {
				c = rangedRandomCoord(0, LARGEUR-1, 0, HAUTEUR-1);
			}
		} else if (range == 5) {
			while(! isInSector(3, c)) {
				c = rangedRandomCoord(0, LARGEUR-1, 0, HAUTEUR-1);
			}
		} else if (range == 6) {
			while(! isInSector(2, c)) {
				c = rangedRandomCoord(0, LARGEUR-1, 0, HAUTEUR-1);
			}
		} else {
			System.out.println("Ne fait pas partie des secteurs pris en charge");
		}
		return c;
	}
	

	/**
	 * Verifie si la coord (x, y) est dans le secteur :
	 * </br>1 : Center
	 * </br>2 : Inter + Center
	 * </br>3 : Outer + Inter + Center
	 * </br>4 : All
	 * @param range
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isInSector(int range, int x, int y) {
		int hMilieu = HAUTEUR/2;
		int lMilieu = LARGEUR/2;
		if (range == 1) {
			if( ( x >lMilieu - (lMilieu/3)) && ( x <lMilieu + (lMilieu/3)) && (y>hMilieu - (hMilieu/3)) && (y<hMilieu + (hMilieu/3))  ) {
				return true;
			}
		} else if (range == 2) {
			if( ( x >(2*lMilieu) - (lMilieu/3)) && ( x <(2*lMilieu) + (lMilieu/3)) && (y>(2*hMilieu) - (hMilieu/3)) && (y<(2*hMilieu) + (hMilieu/3))  ) {
				return true;
			}
		} else if (range == 3) {
			if( ( x > 2) && ( x <LARGEUR-4) && (y>2) && (y<HAUTEUR-4)  ) {
				return true;
			}
		} else if (range == 4) {
			return estSurIle(new Coord(x,y));
		}
		return false;
	}
	
	/**
	 * Verifie si la coord c est dans le secteur :
	 * </br>1 : Center
	 * </br>2 : Inter + Center
	 * </br>3 : Outer + Inter + Center
	 * </br>4 : All
	 * @param range
	 * @param c
	 * @return
	 */
	private boolean isInSector(int range, Coord c) {
		return isInSector(range, c.getAbsc(), c.getOrd());
	}
	
	public boolean isSubmerged(Coord c) {
		if(this.submerg.contains(c) || c==null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Verifie si les artefacts ne sont pas submerges
	 * @return
	 */
	public boolean accessibiliteArtefacts() {
		for(Coord c : this.artefacts) {
			if( ! getZone(c).estAccessible() && getZone(c).getType().hasArtefact()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Verifie les relations et enregistrements des coord
	 * </br> <b>Methode de test<b>
	 */
	public void checkZones() {
		for(int i=0; i<territoire.length; i++) {
			for(int j = 0; j<territoire[0].length; j++) {
				if(! territoire[i][j].estAccessible()) {
					Coord c = territoire[i][j].getCoord();
					Coord newC = new Coord(j, i);
					if(! submerg.contains(c)) {
						System.out.println("Zone "+territoire[i][j].getCoord()+" non enregistree en Coord "+newC+" !");
					}
				}
			}
		}
		for(int i=0; i<this.shore.size(); i++) {
			if(this.shore.get(i) != this.shore.get(i)) {
				System.out.println(this.shore.get(i)+" n'est pas enregistree en tant que shore "+this.shore.get(i)+" !");
			}
		}
	}
	

	/**
	 * Cherche la longueur du plus court chemin entre c1 et c2 avec la méthode de flooding
	 * </br> (Voronoi Territoire)
	 * </br> https://askcodez.com/comment-calculer-le-plus-court-chemin-entre-deux-points-dans-une-grille.html
	 * </br> https://stackoverflow.com/questions/2288830/change-floodfill-algorithm-to-get-voronoi-territory-for-two-data-points/2288898#2288898
	 * @param c1
	 * @param c2
	 * @param diagonales prend ont en compte les diagonales ?
	 * @param sub prend ont en compte les zones submergees ?
	 * @return
	 */
	public int searchShortestWay(Coord c1, Coord c2, boolean diagonales, boolean sub) {
		if(c1.equals(c2)) {
			return 0;
		}
		int f=0; // first element
		int l = 0; //last element
		int hauteur = HAUTEUR;
		int largeur = LARGEUR;
		Coord[] Q = new Coord[hauteur*largeur];  //La queue
		int[][] nbWay = new int[hauteur][largeur]; //Tableau de remplissage
		for(int i = 0; i<nbWay.length; i++) {
			for(int j = 0; j< nbWay[0].length; j++) {
				nbWay[i][j] = 0;
			}
		}
		ArrayList<Coord> marked = new ArrayList<>();
		Q[f] = c1;
		marked.add(c1);
		while(f<hauteur*largeur) {
			Coord poz = Q[f];
			++f;
			int nb = nbWay[poz.getOrd()][poz.getAbsc()];
			for(Coord poz_ : getListVoisins(poz, diagonales, sub)) {
				if(! marked.contains(poz_)) {
					marked.add(poz_);
					nbWay[poz_.getOrd()][poz_.getAbsc()] = nb+1;
					Q[++l] = poz_; //Ajout de poz_ a la queue
					if(poz_.equals(c2)) {
						return nbWay[poz_.getOrd()][poz_.getAbsc()];
					}
				}
			}
		}
		
		
		return -1;
	}
	
	
	
}
