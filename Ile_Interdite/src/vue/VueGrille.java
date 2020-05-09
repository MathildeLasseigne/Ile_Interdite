package vue;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import modele.Etat;
import modele.Ile;
import modele.Zone;
import observateur.Observer;

public class VueGrille extends JPanel implements Observer {

	
	/** On maintient une référence vers le modèle. */
    private Ile ile;
    
    /** Définition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 14;
	
	public VueGrille(Ile nouvIle) {
		this.ile = nouvIle;
		/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
		ile.addObserver(this);
		/**
		 * Définition et application d'une taille fixe pour cette zone de
		 * l'interface, calculée en fonction du nombre de cellules et de la
		 * taille d'affichage.
		 */
		Dimension dim = new Dimension(TAILLE*Ile.LARGEUR,
					      TAILLE*Ile.HAUTEUR);
		this.setPreferredSize(dim);
	}

	@Override
	public void update() {
		repaint();
	}
	
	/**
     * Les éléments graphiques comme [JPanel] possèdent une méthode
     * [paintComponent] qui définit l'action à accomplir pour afficher cet
     * élément. On la redéfinit ici pour lui confier l'affichage des cellules.
     *
     * La classe [Graphics] regroupe les éléments de style sur le dessin,
     * comme la couleur actuelle.
     */
    public void paintComponent(Graphics g) {
	super.repaint();
	/** Pour chaque cellule... */
		for(int i=0; i<Ile.LARGEUR; i++) {
		    for(int j=0; j<Ile.HAUTEUR; j++) {
			/**
			 * ... Appeler une fonction d'affichage auxiliaire.
			 * On lui fournit les informations de dessin [g] et les
			 * coordonnées du coin en haut à gauche.
			 */
			paint(g, ile.getZone(j, i), i*TAILLE, j*TAILLE);
		    }
		}
    }
    
    /**
     * Fonction auxiliaire de dessin d'une cellule.
     * Ici, la classe [Cellule] ne peut être désignée que par l'intermédiaire
     * de la classe [CModele] à laquelle elle est interne, d'où le type
     * [CModele.Cellule].
     * Ceci serait impossible si [Cellule] était déclarée privée dans [CModele].
     */
    private void paint(Graphics g, Zone z, int x, int y) {
        /** Sélection d'une couleur. */
	if (z.getEtat() == Etat.submergee) {
	    g.setColor(Color.BLACK);
	} else if (z.getEtat() == Etat.inondee) {
            g.setColor(Color.blue);
    } else {
    	g.setColor(Color.lightGray);
    }
        /** Coloration d'un rectangle. */
        g.fillRect(x, y, TAILLE, TAILLE);
    }

}
