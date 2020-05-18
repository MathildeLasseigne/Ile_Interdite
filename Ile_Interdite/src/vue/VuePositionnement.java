package vue;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controleur.Controleur;
import modele.Ile;

public class VuePositionnement extends JPanel {
	
//Utiliser GridBagLayout ?
	
	public JButton up;
	public JButton down;
	public JButton center;
	public JButton right;
	public JButton left;
	
	/**
     * Controleur
     */
    private Controleur ctrl;
	
	public VuePositionnement(Controleur control) {
		this.ctrl = control;
		//this.ctrl.setPositionnement(this);
		
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		
		up = new JButton("^");
		c.gridx = 1;
		c.gridy = 0;
		this.add(up, c);
		up.addActionListener(ctrl);
		
		down = new JButton("v");
		c.gridx = 1;
		c.gridy = 2;
		this.add(down, c);
		down.addActionListener(ctrl);
		
		
		center = new JButton("O");
		c.gridx = 1;
		c.gridy = 1;
		this.add(center, c);
		center.addActionListener(ctrl);
		
		right = new JButton(">");
		c.gridx = 2;
		c.gridy = 1;
		this.add(right, c);
		right.addActionListener(ctrl);
		
		left = new JButton("<");
		c.gridx = 0;
		c.gridy = 1;
		this.add(left, c);
		left.addActionListener(ctrl);
		
	
		
	}

}
