package vue;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controleur.Controleur;
import modele.Ile;

public class VueCommandes extends JPanel {

	public JLabel idPlayer;
	public JLabel actionsPlayer;
	public JLabel nbPlayers;
	
	/**
	 * Actions
	 */
	public JButton addPlayer;
	public JButton finTour;
	public JButton move;
	
	/**
	 * Positionnement
	 */
	public JButton up;
	public JButton down;
	public JButton center;
	public JButton right;
	public JButton left;
	
	
	/**
     * Controleur
     */
    private Controleur ctrl;
    
	
	public VueCommandes(Controleur control) {
		this.ctrl = control;
		ctrl.setCommandes(this);
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//Ajout Joueurs  Ligne 0
		nbPlayers = new JLabel("Nb Joueurs : 0 ", JLabel.CENTER);
		c.gridx = 1;
		c.gridy = 0;
		this.add(nbPlayers, c);
		
		addPlayer = new JButton("+ player");
		c.gridx = 2;
		c.gridy = 0;
		this.add(addPlayer, c);
		addPlayer.addActionListener(ctrl);
		
		
		//Passage de tour  Ligne 1-3
		int debutTour = 1;
		idPlayer = new JLabel("Joueur _ ", JLabel.CENTER);
		c.gridx = 1;
		c.gridy = debutTour+1;
		c.ipady = 40;
		this.add(idPlayer, c);
		
		finTour = new JButton("Commencer"); //Deviens "Tour suivant" après le debut de la partie
		c.gridx = 2;
		c.gridy = debutTour;
		c.gridheight = 3;
		c.gridwidth = 2;
		//c.ipady = 40;
		c.fill = GridBagConstraints.VERTICAL;
		this.add(finTour, c);
		finTour.addActionListener(ctrl);
		//reset
		c.gridheight = 1; 
		c.gridwidth = 1;
		c.ipady = 0;
		c.fill = GridBagConstraints.NONE;
		
		//Actions  Ligne 4
		int debutAction = 4;
		actionsPlayer = new JLabel("Actions restantes: 3 ", JLabel.CENTER);
		c.gridx = 0;
		c.gridy = debutAction;
		this.add(actionsPlayer, c);
		
		move = new JButton("Se déplacer");
		c.gridx = 1;
		c.gridy = debutAction;
		this.add(move, c);
		move.addActionListener(ctrl);
		
		
		//Positionnement  Ligne 5-7
		int debutPos = 5;
		
		
		up = new JButton("^");
		c.gridx = 1;
		c.gridy = debutPos;
		c.anchor = GridBagConstraints.SOUTH;
		this.add(up, c);
		up.addActionListener(ctrl);
		
		down = new JButton("v");
		c.gridx = 1;
		c.gridy = debutPos+2;
		c.anchor = GridBagConstraints.NORTH;
		this.add(down, c);
		down.addActionListener(ctrl);
		
		
		center = new JButton("O");
		c.gridx = 1;
		c.gridy = debutPos+1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		this.add(center, c);
		center.addActionListener(ctrl);
		c.fill = GridBagConstraints.NONE;
		
		right = new JButton(">");
		c.gridx = 2;
		c.gridy = debutPos+1;
		c.anchor = GridBagConstraints.WEST;
		this.add(right, c);
		right.addActionListener(ctrl);
		
		left = new JButton("<");
		c.gridx = 0;
		c.gridy = debutPos+1;
		c.anchor = GridBagConstraints.EAST;
		this.add(left, c);
		left.addActionListener(ctrl);
		
		//reset
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		
		
		
	}
	
	/**
	 * Change l affichage du joueur actif
	 * @param id Joueur actif
	 * @param actions Nombre d actions restantes
	 */
	public void changeActivePlayer(int id, int actions) {
		idPlayer.setText("Joueur "+id+" ");
		updateActionsPlayer(actions);
	}
	
	/**
	 * Change le nombre d actions restantes
	 * @param actionsRes Nombre d actions restantes
	 */
	public void updateActionsPlayer(int actionsRes) {
		actionsPlayer.setText("Actions restantes: "+actionsRes+" ");
	}
	
	public void changeNbPlayer(int nb) {
		nbPlayers.setText("Nb Joueurs : " + nb+" ");
	}

}
