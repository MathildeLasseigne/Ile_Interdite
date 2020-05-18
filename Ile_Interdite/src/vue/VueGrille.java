package vue;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import modele.Artefact;
import modele.Coord;
import modele.Etat;
import modele.Ile;
import modele.Players;
import modele.Zone;
import observateur.Observer;

public class VueGrille extends JPanel implements Observer {

	
	/** On maintient une r�f�rence vers le mod�le. */
    private Ile ile;
    private Players players;
    
    /**Liste players**/
    private ArrayList<Coord> coordPlayersAlive = new ArrayList<>();
    private ArrayList<Integer> idPlayersAlive = new ArrayList<>();
    private int nbPlayers;
    private ArrayList<JLabel> charaPlayers = new ArrayList<>();
    
    
    /** D�finition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 30;
	
	public VueGrille(Ile nouvIle, Players players) {
		this.ile = nouvIle;
		this.players = players;
		/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
		ile.addObserver(this);
		
		this.setLayout(null);
		/**
		 * D�finition et application d'une taille fixe pour cette zone de
		 * l'interface, calcul�e en fonction du nombre de cellules et de la
		 * taille d'affichage.
		 */
		Dimension dim = new Dimension(TAILLE*Ile.LARGEUR,
					      TAILLE*Ile.HAUTEUR);
		this.setPreferredSize(dim);
		
//		JLabel player1 = new JLabel("1", JLabel.CENTER);
//		this.add(player1);
//		player1.setLocation(3*TAILLE, 4*TAILLE);
//		player1.setSize(TAILLE, TAILLE);
		
	}

	@Override
	public void update() {
		repaint();
		paintPlayers();
	}
	
	/**
     * Les �l�ments graphiques comme [JPanel] poss�dent une m�thode
     * [paintComponent] qui d�finit l'action � accomplir pour afficher cet
     * �l�ment. On la red�finit ici pour lui confier l'affichage des cellules.
     *
     * La classe [Graphics] regroupe les �l�ments de style sur le dessin,
     * comme la couleur actuelle.
     */
    public void paintComponent(Graphics g) {
	super.repaint();
	/** Pour chaque cellule... */
		for(int i=0; i<Ile.HAUTEUR; i++) {
		    for(int j=0; j<Ile.LARGEUR; j++) {
			/**
			 * ... Appeler une fonction d'affichage auxiliaire.
			 * On lui fournit les informations de dessin [g] et les
			 * coordonn�es du coin en haut � gauche.
			 */
			paint(g, ile.getZone(j, i), j*TAILLE, i*TAILLE);
			paintZone(g, ile.getZone(j, i));
		    }
		}
    }
    
    /**
     * Fonction auxiliaire de dessin d'une cellule.
     * Ici, la classe [Cellule] ne peut �tre d�sign�e que par l'interm�diaire
     * de la classe [CModele] � laquelle elle est interne, d'o� le type
     * [CModele.Cellule].
     * Ceci serait impossible si [Cellule] �tait d�clar�e priv�e dans [CModele].
     */
    private void paint(Graphics g, Zone z, int x, int y) {
        /** S�lection d'une couleur. */
	if (z.getEtat() == Etat.submergee) {
	    g.setColor(new Color(0, 47, 167));
	} else if (z.getEtat() == Etat.inondee) {
            g.setColor(new Color(115, 194, 251));
    } else {
    	g.setColor(new Color(224, 205, 169));
    }
        /** Coloration d'un rectangle. */
        g.fillRect(x, y, TAILLE, TAILLE);
    }
    
    /**
     * Si la zone est speciale, rajoute un element de dessin par dessus
     * </br> Si la zone est un artefact, dessine une croix X de la couleur de l element correspondant
     * @param g
     * @param z
     */
    private void paintZone(Graphics g, Zone z) {
    	if(z.getType().isSpecial()) {
    		if(z.getType().hasArtefact() && z.estAccessible()) {
    			if(z.getType() instanceof Artefact ) {
    				Artefact art = (Artefact) z.getType();
    				g.setColor(getColorArtefact(art.getElement()));
    				//Dessine une croix
    				g.drawLine(z.getCoord().getAbsc()*TAILLE, z.getCoord().getOrd()*TAILLE, ((z.getCoord().getAbsc()+1)*TAILLE)-1, ((z.getCoord().getOrd()+1)*TAILLE)-1);
    				g.drawLine(z.getCoord().getAbsc()*TAILLE, ((z.getCoord().getOrd()+1)*TAILLE)-1, ((z.getCoord().getAbsc()+1)*TAILLE)-1, z.getCoord().getOrd()*TAILLE);
    			}
    		}
    	}
    }
    
    /**
     * Deplace les JLabels des players
     */
    private void paintPlayers() {
    	for(int i = 0; i<this.charaPlayers.size(); i++) {
    		if(this.idPlayersAlive.contains(i)) {
    			int idx = this.idPlayersAlive.indexOf(i);
    			Coord c = this.coordPlayersAlive.get(idx);
    			this.charaPlayers.get(i).setLocation(c.getAbsc()*TAILLE, c.getOrd()*TAILLE);
    		}
    	}
    }
    
    /**
     * Met a jour les coord des players et les affiche sur la grille
     * @param coord Les coordonnees des joueurs vivants
     * @param id Leur id
     */
    public void repaintPlayers(ArrayList<Coord> coord, ArrayList<Integer> id) {
    	updatePlayers(coord, id);
    	paintPlayers();
    }
    
    /**
     * Initialise l'affichage des players
     * @param nbPlayers le nb total de players
     */
    public void initPlayers(int nbPlayers) {
    	for(int i = 0; i<nbPlayers; i++) {
    		this.charaPlayers.add(new JLabel(String.valueOf(i), JLabel.CENTER));
    		this.add(this.charaPlayers.get(i));
    		this.charaPlayers.get(i).setVisible(true);
    		this.charaPlayers.get(i).setSize(TAILLE, TAILLE);
    	}
    }
    
    /**
     * Met a jour la liste des joueurs vivants � afficher
     * @param coord Les coordonnees des joueurs vivants
     * @param id Leur id
     */
    public void updatePlayers(ArrayList<Coord> coord, ArrayList<Integer> id) {
    	this.coordPlayersAlive = coord;
    	this.idPlayersAlive = id;
    	for(int i=0; i<this.charaPlayers.size();i++) {
    		if( (! this.idPlayersAlive.contains(i)) && (this.charaPlayers.get(i).isVisible() )) {
    			this.charaPlayers.get(i).setVisible(false);
    		}
    	}
    }
    
    /**
     * Recupere la couleur a associer a un artefact en fonction de son element
     * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
     * @param element
     * @return
     */
    private Color getColorArtefact(int element) {
    	if(element == 0) {
    		return Color.BLUE;
    	} else if(element == 1) {
    		return Color.RED;
    	} else if(element == 2) {
    		return Color.WHITE;
    	} else if(element == 3) {
    		return new Color(34, 120, 15);
    	}
    	return Color.BLACK;
    }
    

}
