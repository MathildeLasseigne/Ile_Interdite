package vue;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controleur.Controleur;
import modele.Ile;

public class VueCommandes extends JPanel {

	
	/**
     * Controleur
     */
    private Controleur ctrl;
	
	public VueCommandes(Controleur control) {
		this.ctrl = control;
		
		JButton ileInonde = new JButton("Tour suivant");
		this.add(ileInonde);
		ileInonde.addActionListener(ctrl);
		ctrl.setButton(1, ileInonde);
	}

}
