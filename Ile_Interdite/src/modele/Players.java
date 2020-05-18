package modele;

import java.util.ArrayList;

public class Players {

	private ArrayList<SinglePlayer> playersList = new ArrayList<>();
	private ArrayList<Coord> playersCoord = new ArrayList<>();
	private ArrayList<Integer> deadPlayersList = new ArrayList<>();
	private int nbPlayers;
	private int activePlayer;
	
	
	public int actionsRestantes;
	public final int totalActions = 3;
	
	public Players() {
		nbPlayers = 0;
		activePlayer = 0;
		//addPlayer(new Coord (4,4));
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
		actionsRestantes = totalActions;
	}
	
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
		return true;
	}
	


	/**
	 * Deplace le player sur la coord c, position superposable avec autres players
	 * @param c la coordonnee de l heliport
	 * @return la liste des artefacts du player
	 */
	public int[] moveToHelico(Coord c) {
		return null;
		
	}
	
	public boolean move(Coord c) {
		if(! playersCoord.contains(c)) {
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
	 * Bouge forciblement un joueur de position cP sur une case cSafe si possible. Sinon le joueur meurs
	 * @param cP La coord du joueur devenue zone submergee
	 * @param cSafe Une coord voisine safe du joueur prise au hasard. Si cSafe == null, le joueur meurs
	 */
	public void savePlayer(Coord cP, Coord cSafe) {
		SinglePlayer p = getPlayer(cP);
		if (cSafe != null) {
			p.move(cSafe);
			playersCoord.set(p.getId(), cSafe);
			
		} else {
			diePlayer(p.getId());
		}
	}
	
	/**
	 * Recupere le premier SinglePlayer a la coordonnee c
	 * @param c
	 * @return
	 */
	public SinglePlayer getPlayer(Coord c) {
		for(SinglePlayer p : playersList) {
			if(p.getCoord() == c) {
				return p;
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
