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

public class Controleur implements ActionListener {
	
	private Ile ile;
	private Players players;
	
	private VueCommandes cmds;
	
	/**Heliport**/
	
	private int[] artefactsOnHeli;
	private ArrayList<Integer> playersOnHeli = new ArrayList<>();
	private Coord heliport;
	
	/** JButtons **/
	private JButton B_ile;
	private JButton add_players;
	
	/** Etats **/
	private boolean debutPartie;
	private boolean partieFinie;
	
	
	public Controleur(Ile ile) {
		this.ile = ile;
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
	}
	
	
	/**
	 * Enregistre les boutons dans le controleur :
	 * </br> Numero 1 : ile_inonde 
	 * </br> Numero 2 : add_players 
	 * @param x le numero du bouton
	 * @param button
	 */
	public void setButton(int x, JButton button) {
		if (x == 1) {
			this.B_ile = button;
		} else if (x == 2) {
			this.add_players = button;
		}
	}
	
	public void setCommandes(VueCommandes cmd) {
			this.cmds = cmd;
	}
	
	


	@Override
	public void actionPerformed(ActionEvent e) {
		if(! partieFinie) {
			if(e.getSource() == B_ile) {
				resetEtat();
				if(debutPartie == false) {
					debutPartie = true;
				}
				if ( ! ile.updateIle()) {
					JOptionPane.showMessageDialog(null, "Vous avez perdu !");
				}
				changePlayer();
			} else if (e.getSource() == add_players) {
				addPlayer();
			}
		} else {
			JOptionPane.showMessageDialog(null, "Partie finie !"); //En faire une fonction
		}
		
	}
	
	
	
	
	
	
		/**
	 * Remet les booleans des etat a false
	 */
	private void resetEtat() {
		
	}
	
	
	/**
	 * Pour chaque player, verifie si ils sont safe et sinon les sauver.
	 * </br> Peut aussi etre utilise pour la victoire
	 */
	private void verifiePlayers() {
		
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
			return new Coord(pl.getAbsc(), pl.getOrd()-1);
		} else if (d == Direction.left) {
			return new Coord(pl.getAbsc()-1, pl.getOrd());
		} else if (d == Direction.up) {
			return new Coord(pl.getAbsc(), pl.getOrd()+1);
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
			players.addPlayer(ile.getRandCoord(3));
//			while (! players.addPlayer(ile.getRandCoord(3))) {
//				addPlayer();
//			}
		}
		
	}
	
	private void changePlayer() {
		players.changePlayer();
		this.cmds.changeActivePlayer(players.getId(), players.getActionsRes());
	}

}
