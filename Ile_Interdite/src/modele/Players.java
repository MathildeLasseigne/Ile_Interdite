package modele;

import java.util.ArrayList;
import java.util.Random;

public class Players {

	private ArrayList<SinglePlayer> playersList = new ArrayList<>();
	private ArrayList<Coord> playersCoord = new ArrayList<>();
	private ArrayList<Integer> deadPlayersList = new ArrayList<>();
	private int nbPlayers;
	private int activePlayer;
	
	
	public int actionsRestantes;
	//public final int totalActions = 3;
	
	/**
	 * La liste des players et l'ensemble des informations et commandes les concernant
	 */
	public Players() {
		actionsRestantes = 0;
		nbPlayers = 0;
		activePlayer = 0;
	}

	/**
	 * Passe au joueur suivant et remet le nb d actions restantes a totalActions
	 */
	public void changePlayer() {
		if(activePlayer == nbPlayers-1) {
			activePlayer = 0;
		} else {
			activePlayer++;
		}
		while(deadPlayersList.contains(activePlayer)) {
			changePlayer();
		}
		actionsRestantes = playersList.get(activePlayer).getTotalAction();
	}
	
	/**
	 * Tant qu'il reste des actions disponibles, les diminuent de 1
	 * @return
	 */
	public boolean play() {
		if (actionsRestantes>0) {
			actionsRestantes--;
			return true;
		}
		return false;
	}
	
	/**
	 * Ajoute le player a playersList et sa coord a playersCoord
	 */
	public boolean addPlayer(Coord c) {
		if(playersCoord.contains(c)) {
			return false;
		}
		playersList.add(new SinglePlayer(nbPlayers, c));
		playersCoord.add(c);
		nbPlayers++;
		actionsRestantes = playersList.get(activePlayer).getTotalAction();
		return true;
	}
	
	/**
	 * Assigne les roles en fonction du nb de joueurs
	 * <br> Au moins 3 : Pilote
	 * <br> Au moins 5 : Explorateur & Ingenieur
	 * <br> Au moins 7 : Navigateur & Plongeur
	 * <br> Au moins 8 : Messager
	 * @return
	 */
	public void assignRole() {
		ArrayList<Integer> except = new ArrayList<>();
		if(this.nbPlayers >= 3) {
			int rand1 = this.rangedRandomIntExcept(0, this.nbPlayers-1, except);
			except.add(rand1);
			this.playersList.get(rand1).setRole(new Pilote());
		}
		if(this.nbPlayers >= 5 ) {
			int rand2 = this.rangedRandomIntExcept(0, this.nbPlayers-1, except);
			except.add(rand2);
			int rand3 = this.rangedRandomIntExcept(0, this.nbPlayers-1, except);
			except.add(rand3);
			this.playersList.get(rand2).setRole(new Explorateur());
			this.playersList.get(rand3).setRole(new Ingenieur());
		}
		if(this.nbPlayers >= 7) {
			int rand4 = this.rangedRandomIntExcept(0, this.nbPlayers-1, except);
			except.add(rand4);
			int rand5 = this.rangedRandomIntExcept(0, this.nbPlayers-1, except);
			except.add(rand5);
			this.playersList.get(rand4).setRole(new Navigateur());
			this.playersList.get(rand5).setRole(new Plongeur());
		}
		if(this.nbPlayers >= 8) {
			int rand6 = this.rangedRandomIntExcept(0, this.nbPlayers-1, except);
			except.add(rand6);
			this.playersList.get(rand6).setRole(new Messager());
		}
	}
	

	/**
	 * Verifie si le player possede le role correspondant
	 * </br> 0 : Citoyen
	 * </br> 1 : Pilote
	 * </br> 2 : Ingenieur
	 * </br> 3 : Explorateur
	 * </br> 4 : Navigateur
	 * </br> 5 : Plongeur
	 * </br> 6 : Messager
	 * @param role
	 * @return
	 */
	public boolean estRole(int role) {
		SinglePlayer p = getPlayer(activePlayer);
		if(role == 0) {
			return p.getRole() instanceof Citoyen;
		} else if(role == 1) {
			return p.getRole() instanceof Pilote;
		} else if (role == 2) {
			return p.getRole() instanceof Ingenieur;
		} else if (role == 3) {
			return p.getRole() instanceof Explorateur;
		} else if (role == 4) {
			return p.getRole() instanceof Navigateur;
		} else if (role == 5) {
			return p.getRole() instanceof Plongeur;
		} else if (role == 6) {
			return p.getRole() instanceof Messager;
		} else {
			return false;
		}
	}
	
	public String getExplanationPouvoir(int id) {
		return getPlayer(id).getRole().getPower();
	}
	

	/**
	 * Deplace le player sur la coord c, position superposable avec autres players ou de la coord c
	 * @param to le player vas-il vers la position superposable (heliport)
	 * @param reussite le mouvement as-t-il reussi ? A recuperer plus tard (pointeur)
	 * @param c la coordonnee de l heliport
	 * @return la liste des artefacts du player
	 */
	public int[] moveHelico(boolean to, boolean reussite, Coord c) {
		if(to) {
			if(actionsRestantes > 0) {
				playersList.get(activePlayer).move(c);
				playersCoord.set(activePlayer, c);
				actionsRestantes--;
				reussite = true;
			} else {
				reussite = false;
			}
		} else {
			reussite = move(c);
		}
		return getInventaire()[0];
	}
	
	/**
	 * Deplace le player sur la coord c, position superposable avec autres players ou de la coord c
	 * @param to le player vas-il vers la position superposable (heliport)
	 * @param reussite le mouvement as-t-il reussi ? A recuperer plus tard (pointeur)
	 * @param cPlayer la coord du player a deplacer
	 * @param c la coordonnee de l heliport
	 * @return la liste des artefacts du player
	 */
	public int[] moveHelico(boolean to, boolean reussite, Coord cPlayer, Coord c) {
		int id = getId(cPlayer);
		if(to) {
			if(actionsRestantes > 0) {
				playersList.get(id).move(c);
				playersCoord.set(id, c);
				actionsRestantes--;
				reussite = true;
			} else {
				reussite = false;
			}
		} else {
			reussite = move(c);
		}
		return getInventaire(id)[0];
	}
	
	/**
	 * Deplace le player actif sur la coord c
	 * @param c
	 * @return
	 */
	public boolean move(Coord c) {
		//if(! playersCoord.contains(c)) {
		if(! getCoordPlayersAlive().contains(c)) {
			if(actionsRestantes > 0) {
				playersList.get(activePlayer).move(c);
				playersCoord.set(activePlayer, c);
				actionsRestantes--;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Deplace le player se trouvant sur la coord cPlayer sur la coord newC
	 * @param cPlayer
	 * @param newC
	 * @return
	 */
	public boolean move(Coord cPlayer, Coord newC) {
		if(! getCoordPlayersAlive().contains(newC)) {
			if(getId(cPlayer) == -1) {
				System.out.println("move() : Pas de player a la Coord"+cPlayer);
			} else if(actionsRestantes > 0) {
				playersList.get(getId(cPlayer)).move(newC);
				playersCoord.set(getId(cPlayer), newC);
				actionsRestantes--;
				return true;
			}
		}
		return false;
	}
	
	
	
	
	
	
	/**
	 * Fait mourir le joueur a l id correspondant
	 */
	private void diePlayer(int id) {
		deadPlayersList.add(id);
	}
	
	
	/**
	 * 
	 */
	/**Bouge forciblement un joueur de position cP sur une case cSafe si possible. Sinon le joueur meurs
	 * @param cP La coord du joueur devenue zone submergee
	 * @param cSafe Une coord voisine safe du joueur prise au hasard. Si cSafe == null, le joueur meurs
	 * @return l id du joueur s il est mort -1 sinon
	 */
	public int savePlayer(Coord cP, Coord cSafe) {
		SinglePlayer p = getPlayer(cP, false);
		if (cSafe != null) {
			p.move(cSafe);
			playersCoord.set(p.getId(), cSafe);
			System.out.println("Save Player "+ p);
			return -1;
			
		} else {
			System.out.println("Die player");
			diePlayer(p.getId());
			return p.getId();
		}
	}
	

	/**
	 * Recupere le premier SinglePlayer a la coordonnee c
	 * @param c
	 * @param dead Les joueurs morts compris ?
	 * @return
	 */
	public SinglePlayer getPlayer(Coord c, boolean dead) {
		if(dead) {
			for(SinglePlayer p : playersList) {
				if(p.getCoord().equals(c) ) {
					return p;
				}
			}
		} else {
			for(Coord cP : getCoordPlayersAlive()) {
				if(cP.equals(c)) {
					int idx = getCoordPlayersAlive().indexOf(cP);
					return getPlayer(getIdPlayersAlive().get(idx));
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Recupere le SinglePlayer ayant id
	 * @param id du SinglePlayer a recuperer
	 * @return
	 */
	public SinglePlayer getPlayer(int id) {
		if(id < 0 || id >= nbPlayers) {
			System.out.println("Probleme recuperation player with id "+id+"\n");
			return null;
		}
		return playersList.get(id);
	}
	
	
	/**
	 * Recupere les coordonnees du joueur actif
	 * @return
	 */
	public Coord getCoord() {
		return playersList.get(activePlayer).getCoord();
	}
	
	/**
	 * Recupere l id du joueur actif
	 * @return
	 */
	public int getId() {
		return activePlayer;
	}
	
	/**
	 * Return l id du joueur sur la coord c
	 * @param c
	 * @return -1 si il n y a pas de joueur sur la coord c
	 */
	public int getId(Coord c) {
//		System.out.println("Coord test = Coord"+c);
//
//		for(Coord cP : this.playersCoord) {
//			System.out.println("Compare Coord"+cP);
//			if(cP.getAbsc() == c.getAbsc() && cP.getOrd() == c.getOrd()) {
//				int idx = this.playersCoord.indexOf(cP);
//				System.out.println("Id trouvee = " +idx);
//				return idx;
//			}
//		}
//		System.out.println("Id trouvee = " +-1);
		for(Coord cP : getCoordPlayersAlive()) {
		if(cP.equals(c)) {
			int idx = getCoordPlayersAlive().indexOf(cP);
			return getIdPlayersAlive().get(idx);
		}
	}
		return -1;
	}
	
	public Role getRole() {
		return this.playersList.get(activePlayer).getRole();
	}
	
	public String getStringPlayer(int id) {
		return this.playersList.get(id).toString();
	}
	
	/**
	 * Recupere l inventaire du joueur actif
	 * </br>Ligne 0 : Artefacts
	 * </br>Ligne 1 : Cles
	 * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
	 * @return
	 */
	public int[][] getInventaire(){
		return this.playersList.get(activePlayer).getInventaire();
	}
	
	private int[][] getInventaire(int id){
		return this.playersList.get(id).getInventaire();
	}
	
	/**
	 * Ajoute a l inventaire du player actif l artefact d element correspondant
	 * @param element
	 * @return
	 */
	public boolean addArtefact(int element) {
		return this.playersList.get(activePlayer).addArtefact(element);
	}
	
	/**
	 * Ajoute a l inventaire du player actif la cle d element correspondant
	 * @param element
	 * @return
	 */
	public boolean addCle(int element) {
		return this.playersList.get(activePlayer).addCle(element);
	}
	

	/**
	 * Ajoute ou soustrait le nouvel inventaire a celui du player id
	 * @param id
	 * @param add Ajoute ?
	 * @param newInventaire
	 * @return
	 */
	public boolean modInventaire(int id, boolean add, int[][] newInventaire) {
		boolean reussite = true;
		if(add) {
			for(int i = 0; i<newInventaire.length; i++) {
				for(int el = 0; el<newInventaire[0].length; el++) {
					if(i==0) {
						for(int qt = 0; qt< newInventaire[0][el]; qt++) {
							if(! getPlayer(id).addArtefact(el)) {
								reussite = false;
							}
						}
					} else {
						for(int qt = 0; qt< newInventaire[1][el]; qt++) {
							if(! getPlayer(id).addCle(el)) {
								reussite = false;
							}
						}
					}
				}
			}
		} else {
			for(int i = 0; i<newInventaire.length; i++) {
				for(int el = 0; el<newInventaire[0].length; el++) {
					if(i==0) {
						for(int qt = 0; qt< newInventaire[0][el]; qt++) {
							if(! getPlayer(id).removeArtefact(el)) {
								reussite = false;
							}
						}
					} else {
						for(int qt = 0; qt< newInventaire[1][el]; qt++) {
							if(! getPlayer(id).removeCle(el)) {
								reussite = false;
							}
						}
					}
				}
			}
		}
		
		return reussite;
	}
	
	/**
	 * Recupere le nb de cles d un certain element que le joueur actif possede
	 * @param element
	 * @return
	 */
	public int getNbCle(int element) {
		return this.playersList.get(activePlayer).getCles()[element];
	}
	
	
	/**
	 * Renvoie le nb de players restants
	 * @return
	 */
	public int getNbPlayersAlive() {
		return nbPlayers - deadPlayersList.size();
	}
	
	/**
	 * Renvoie le nb de players
	 * @return
	 */
	public int getNbPlayers() {
		return nbPlayers;
	}
	
	public ArrayList<Coord> getCoordPlayersAlive(){
		ArrayList<Coord> lc = new ArrayList<>();
		for(int i=0; i<nbPlayers; i++) {
			if(! this.deadPlayersList.contains(i)) {
				lc.add(this.playersCoord.get(i));
			}
		}
		return lc;
	}
	
	
	public ArrayList<Integer> getIdPlayersAlive(){
		ArrayList<Integer> lId = new ArrayList<>();
		for(int i=0; i<nbPlayers; i++) {
			if(! this.deadPlayersList.contains(i)) {
				lId.add(i);
			}
		}
		return lId;
	}
	
	
	
	/**
	 * Retourne le nombre d actions restantes du joueur actif
	 * @return
	 */
	public int getActionsRes() {
		return this.actionsRestantes;
	}
	
	
	/**
	 * Verifie les relations et enregistrements des coord
	 * </br> <b>Methode de test<b>
	 */
	public void checkPlayers() {
		if(this.playersCoord.isEmpty()) {
			System.out.println("Aucune coord de players enregistree !");
		}
		for(SinglePlayer p : playersList) {
			if(! this.playersCoord.contains(p.getCoord())) {
				System.out.println(p+ " n'est pas enregistre !");
			}
		}
	}
	
	private int rangedRandomIntExcept(int rangeMin, int rangeMax, ArrayList<Integer> except) {
		Random r = new Random();
		int randomValue = rangeMin + r.nextInt(rangeMax +1);
		if(except != null) {
			if((rangeMax - rangeMin) == except.size()) {
				System.out.println("Aucun random int possible sans except");
				return -1;
			} else if(except.contains(randomValue)) {
				rangedRandomIntExcept(rangeMin, rangeMax, except);
			}
		}
		return randomValue;
	}
}
