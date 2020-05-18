package controleur;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import modele.Coord;
import modele.Direction;
import modele.Ile;
import modele.Players;
import vue.VueCommandes;
import vue.VueGrille;
import vue.VuePositionnement;

public class Controleur implements ActionListener {
	
	private Ile ile;
	private Players players;
	
	/**JPannels**/
	private VueCommandes cmds;
	private VueGrille grille;
	
	/**Heliport**/
	
	private int[] artefactsOnHeli;
	private ArrayList<Integer> playersOnHeli = new ArrayList<>();
	private Coord heliport;
	
	
	/** Etats **/
	private boolean debutPartie;
	private boolean partieFinie;
	
	private boolean move;
	private boolean asseche;
	private boolean searchArtefacts;
	
	/**
	 * Cree un controleur gerant la partie
	 * @param ile Le modele de l ile
	 * @param players La liste des joueurs
	 */
	public Controleur(Ile ile, Players players) {
		this.ile = ile;
		this.players = players;
		init();
	}
	
	/**
	 * Initialise les etats et l heliport
	 */
	private void init() {
		// Heliport
		artefactsOnHeli = new int[4];
		for(int i =0; i<4; i++) {
			artefactsOnHeli[i] = 0;
		}
		heliport = ile.getHeliport();
		
		//Etats
		debutPartie = false;
		partieFinie = false;
		move = false;
		asseche = false;
		searchArtefacts = false;
	}
	
	
	//Link JPannels
	public void setCommandes(VueCommandes cmd) {
			this.cmds = cmd;
	}
	
	public void setGrille(VueGrille grille) {
		this.grille = grille;
	}
	
	
	


	@Override
	public void actionPerformed(ActionEvent e) {
		if(! partieFinie) {
			if(e.getSource() == this.cmds.finTour) {
				resetEtat();
				if(debutPartie == false) { //Si la partie n a pas encore commence
					if(this.players.getNbPlayers()>0) { //Si il y a au moins 1 joueur
						debutPartie = true;
						this.grille.initPlayers(this.players.getNbPlayers());
						this.grille.updatePlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
						this.grille.update();
						this.cmds.finTour.setText("Tour suivant");
						this.cmds.changeActivePlayer(players.getId(), players.getActionsRes());
						JOptionPane.showMessageDialog(null, "Pour effectuer une action, cliquez sur le bouton correspondant puis sur la direction où vous voulez l'appliquer");
					}
					
				} else { //Partie commencee
					//Chercher les cles
					if ( ! ile.updateIle()) { //Mise a jour de l ile
						endGame(false);
					}
					this.ile.checkZones();
					this.players.checkPlayers();
					verifiePlayers(); //Sauve les players et verifie fin du jeu -> Faire un verifie artefacts ?
					changePlayer();
				}
			} else if (e.getSource() == this.cmds.addPlayer) {
				if(debutPartie == false) {
					addPlayer();
				}
			}
			if(debutPartie) {
				if (e.getSource() == this.cmds.move) {  //Actions
					this.move = true;
				} else if (e.getSource() == this.cmds.up) { //Actions de positionnement
					actionPos(Direction.up);
					resetEtat();
				} else if (e.getSource() == this.cmds.down) {
					actionPos(Direction.down);
					resetEtat();
				} else if (e.getSource() == this.cmds.center) {
					actionPos(Direction.center);
					resetEtat();
				} else if (e.getSource() == this.cmds.right) {
					actionPos(Direction.right);
					resetEtat();
				} else if (e.getSource() == this.cmds.left) {
					actionPos(Direction.left);
					resetEtat();
				}
				
			} else {
				if (e.getSource() == this.cmds.move) {  //Actions
					JOptionPane.showMessageDialog(null, "Veuillez d'abord selectionner le nombre de joueurs et lancer la partie");
				} else if (e.getSource() == this.cmds.up) { //Actions de positionnement
					JOptionPane.showMessageDialog(null, "Veuillez d'abord selectionner le nombre de joueurs et lancer la partie");
				} else if (e.getSource() == this.cmds.down) {
					JOptionPane.showMessageDialog(null, "Veuillez d'abord selectionner le nombre de joueurs et lancer la partie");
				} else if (e.getSource() == this.cmds.center) {
					JOptionPane.showMessageDialog(null, "Veuillez d'abord selectionner le nombre de joueurs et lancer la partie");
				} else if (e.getSource() == this.cmds.right) {
					JOptionPane.showMessageDialog(null, "Veuillez d'abord selectionner le nombre de joueurs et lancer la partie");
				} else if (e.getSource() == this.cmds.left) {
					JOptionPane.showMessageDialog(null, "Veuillez d'abord selectionner le nombre de joueurs et lancer la partie");
				}
				
			}
			
		} else {
			JOptionPane.showMessageDialog(null, "La partie est déjà finie !"); //Pour ne pas pouvoir continuer apres la fin
		}
		
	}
	
	
	
	
	
	
		/**
	 * Remet les booleans des etat a false
	 */
	private void resetEtat() {
		if(move) {
			selectJButton(this.cmds.move);
			move = false;
		}
		if(asseche) {
			//selectJButton(this.cmds.move);
			asseche = false;
		}
		if(searchArtefacts) {
			//selectJButton(this.cmds.move);
			searchArtefacts = false;
		}
	}
	
	
	/**
	 * Pour chaque player, verifie si ils sont safe et sinon les sauver.
	 * </br> Verifie aussi si un player mort n avait pas d artefact
	 * </br> Peut aussi etre utilise pour la victoire
	 */
	private void verifiePlayers() {
		for(Coord c : this.players.getCoordPlayersAlive()) {
			if(! this.ile.isSafe(c)) {
				Coord safeC = this.ile.getVoisin(c, false, this.players.getCoordPlayersAlive());
				this.players.savePlayer(c, safeC);
				this.grille.repaintPlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
				System.out.println("Old c :"+this.ile.isSubmerged(c));
				System.out.println("New c :"+this.ile.isSubmerged(safeC));
			}
		}
		if(this.players.getNbPlayersAlive()==0) {
			endGame(false);
		}
	}
	
	/**
	 * Recupere la coordonnee dans la direction donnee par rapport a la coordonnee 
	 * du player actif de players
	 * </br> ! Verifier si est dans l ile !
	 * @param d
	 * @return
	 */
	public Coord getCoordDir(Direction d) {
		Coord pl = players.getCoord();
		if(d == Direction.center) {
			return pl;
		} else if (d == Direction.down) {
			return new Coord(pl.getAbsc(), pl.getOrd()+1);
		} else if (d == Direction.left) {
			return new Coord(pl.getAbsc()-1, pl.getOrd());
		} else if (d == Direction.up) {
			return new Coord(pl.getAbsc(), pl.getOrd()-1);
		} else if (d == Direction.right) {
			return new Coord(pl.getAbsc()+1, pl.getOrd());
		}
		return null;
	}
	
	/**
	 * Ajoute un nouveau player a players
	 */
	private void addPlayer() {
		if(debutPartie == false) {
			Coord c = ile.getRandCoord(3);
			while (! players.addPlayer(c)) {
				addPlayer();
			}
			this.cmds.changeNbPlayer(players.getNbPlayersAlive());
		}
		
	}
	
	
	/**
	 * Change le player actif et son affichage
	 */
	private void changePlayer() {
		players.changePlayer();
		this.cmds.changeActivePlayer(players.getId(), players.getActionsRes());
	}
	
	
	/**
	 * Effectue l action correspondante en fonction des etats
	 * @param dir
	 */
	private void actionPos(Direction dir) {
		Coord newC = getCoordDir(dir);
		if(this.move) {
			if(this.ile.isSafe(newC)) {
				if(! this.players.move(newC)) {
					JOptionPane.showMessageDialog(null, "Vous ne pouvez pas vous déplacer là !");
				} else {
					this.grille.repaintPlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
					//this.grille.update();
					this.cmds.updateActionsPlayer(this.players.getActionsRes());
				}
			} else {
				JOptionPane.showMessageDialog(null, "Cette zone n'est pas accessible !");
			}
			
		}
	}
	
	
	
	/**
	 * Affiche le message de fin de partie
	 * @param win Partie gagnee ?
	 */
	private void endGame(boolean win) {
		if(win) {
			JOptionPane.showMessageDialog(null, "Félicitation ! Vous avez gagné !");
		} else {
			if(this.players.getNbPlayersAlive() != this.players.getNbPlayers()) {
				int dead = this.players.getNbPlayers()-this.players.getNbPlayersAlive();
				JOptionPane.showMessageDialog(null, "Vous avez perdu ! "+dead+" joueurs sont morts !");
			}
			JOptionPane.showMessageDialog(null, "Vous avez perdu !");
		}
		this.partieFinie = true;
	}
	
	/**
	 * Donne au bouton une apparence activee/desactivee
	 * @param button
	 */
	private void selectJButton(JButton button) {
		//https://openclassrooms.com/fr/courses/26832-apprenez-a-programmer-en-java/23727-interagissez-avec-des-boutons
	}

}
