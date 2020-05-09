package vue;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import controleur.Controleur;
import modele.Ile;

public class VuePositionnement extends JPanel {
	
//Utiliser GridBagLayout ?
	
	/**
     * Controleur
     */
    private Controleur ctrl;
	
	public VuePositionnement(Controleur control) {
		this.ctrl = control;
	}

}
