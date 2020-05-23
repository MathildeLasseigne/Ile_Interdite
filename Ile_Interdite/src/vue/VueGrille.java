package vue;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import controleur.Controleur;
import modele.Artefact;
import modele.Coord;
import modele.Etat;
import modele.Ile;
import modele.Players;
import modele.Zone;
import observateur.Observer;

@SuppressWarnings("serial")
public class VueGrille extends JPanel implements Observer {

	
	/** On maintient une référence vers le modèle. */
    private Ile ile;
    
    private Controleur ctrl;
    
    /**Liste players**/
    private ArrayList<Coord> coordPlayersAlive = new ArrayList<>();
    private ArrayList<Integer> idPlayersAlive = new ArrayList<>();
    private ArrayList<JLabel> charaPlayers = new ArrayList<>();
    
    private JLabel heliport;
    
    
    /** Définition d'une taille (en pixels) pour l'affichage des cellules. */
    private final static int TAILLE = 32;
	
	public VueGrille(Ile nouvIle, Players players, Controleur control) {
		this.ile = nouvIle;
		setControleur(control);
		/** On enregistre la vue [this] en tant qu'observateur de [modele]. */
		ile.addObserver(this);
		
		this.addMouseListener(ctrl);
		this.addMouseMotionListener(ctrl);
		
		this.setLayout(null);
		/**
		 * Définition et application d'une taille fixe pour cette zone de
		 * l'interface, calculée en fonction du nombre de cellules et de la
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
	
	public void setControleur(Controleur ctrl) {
		this.ctrl = ctrl;
	}

	@Override
	public void update() {
		repaint();
		paintPlayers();
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
		for(int i=0; i<Ile.HAUTEUR; i++) {
		    for(int j=0; j<Ile.LARGEUR; j++) {
			/**
			 * ... Appeler une fonction d'affichage auxiliaire.
			 * On lui fournit les informations de dessin [g] et les
			 * coordonnées du coin en haut à gauche.
			 */
			paint(g, ile.getZone(j, i), j*TAILLE, i*TAILLE);
			paintZone(g, ile.getZone(j, i));
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
    	Graphics2D g2d = (Graphics2D) g;
    	g2d.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
    	if(z.getType().isSpecial()) {
    		if(z.getType().hasArtefact() && z.estAccessible()) {
    			if(z.getType() instanceof Artefact ) {
    				Artefact art = (Artefact) z.getType();
    				g2d.setColor(getColorArtefact(art.getElement()));
    				//Dessine une croix
    				g2d.drawLine(z.getCoord().getAbsc()*TAILLE, z.getCoord().getOrd()*TAILLE, ((z.getCoord().getAbsc()+1)*TAILLE)-1, ((z.getCoord().getOrd()+1)*TAILLE)-1);
    				g2d.drawLine(z.getCoord().getAbsc()*TAILLE, ((z.getCoord().getOrd()+1)*TAILLE)-1, ((z.getCoord().getAbsc()+1)*TAILLE)-1, z.getCoord().getOrd()*TAILLE);
    			}
    		} else if(z.getType().isExit() && z.estAccessible()) {
    			g.setColor(Color.black);
    			g.drawOval(z.getCoord().getAbsc()*TAILLE, z.getCoord().getOrd()*TAILLE, TAILLE-1, TAILLE-1);
    			
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
     * Met a jour la liste des joueurs vivants à afficher
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
    		return new Color(187, 11, 11);
    	} else if(element == 2) {
    		return Color.WHITE;
    	} else if(element == 3) {
    		return new Color(34, 120, 15);
    	}
    	return Color.BLACK;
    }
    
    /**
     * Retourne la coord de la position des coord(x, y) en pixels
     * @param x
     * @param y
     * @return
     */
    public Coord getCoord(int x, int y) {
    	int absc = x/TAILLE;
    	int ord = y/TAILLE;
    	return new Coord(absc, ord);
    }
    
    /**
     * Initialise l heliport
     * @param c la Coord de l heliport
     */
    public void initHeliport(Coord c) {
//    	ImageIcon heliportImg = new ImageIcon(((new ImageIcon("images/heliport.jpg")).getImage()).getScaledInstance(TAILLE, TAILLE, java.awt.Image.SCALE_SMOOTH));
//		this.heliport = new JLabel(heliportImg, JLabel.CENTER);
		this.heliport = new JLabel("H", JLabel.CENTER);
		this.heliport.setSize(TAILLE, TAILLE);
		this.heliport.setFont(new Font("Arial",Font.BOLD,TAILLE));
		this.add(heliport);
		heliport.setLocation((c.getAbsc()*TAILLE)-1, c.getOrd()*TAILLE);
    }
    
    /**
     * Deplace le label du player id a la zone de coord c
     * @param id
     * @param c
     */
    public void movePlayerLabel(int id, Coord c) {
    	this.charaPlayers.get(id).setLocation(c.getAbsc()*TAILLE, c.getOrd()*TAILLE);
    	this.charaPlayers.get(id).setVisible(true);
    }

}
