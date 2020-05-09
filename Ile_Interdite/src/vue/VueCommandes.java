package vue;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controleur.Controleur;
import modele.Ile;

public class VueCommandes extends JPanel {

	public JLabel idPlayer;
	
	public JButton addPlayer;
	public JButton ileInonde;
	
	
	/**
     * Controleur
     */
    private Controleur ctrl;
    
	
	public VueCommandes(Controleur control) {
		this.ctrl = control;
		ctrl.setCommandes(this);
		
		idPlayer = new JLabel("Joueur 0, " + "Actions: 3", JLabel.CENTER);
		this.add(idPlayer);
		
		addPlayer = new JButton("+ player");
		this.add(addPlayer);
		addPlayer.addActionListener(ctrl);
		ctrl.setButton(2, addPlayer);
		
		
		ileInonde = new JButton("Tour suivant");
		this.add(ileInonde);
		ileInonde.addActionListener(ctrl);
		ctrl.setButton(1, ileInonde);
	}
	
	public void changeActivePlayer(int id, int actions) {
		idPlayer.setText("Joueur "+id+", " + "Actions: "+actions);
	}

}
