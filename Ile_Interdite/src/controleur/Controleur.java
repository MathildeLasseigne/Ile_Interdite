package controleur;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import modele.Ile;

public class Controleur implements ActionListener {
	
	private Ile ile;
	
	JButton B_ile;
	
	/**
	 * Enregistre les boutons dans le controleur :
	 * </br> Numero 1 : ile_inonde 
	 * @param x le numero du bouton
	 * @param button
	 */
	public void setButton(int x, JButton button) {
		if (x == 1) {
			this.B_ile = button;
		}
	}
	
	public Controleur(Ile ile) {
		this.ile = ile;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == B_ile) {
			if ( ! ile.updateIle()) {
				JOptionPane.showMessageDialog(null, "Vous avez perdu !");
			}
		}
		
	}

}
