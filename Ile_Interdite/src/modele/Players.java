package modele;

import java.util.ArrayList;

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
			return -1;
			
		} else {
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
}
