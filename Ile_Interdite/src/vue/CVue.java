package vue;

import java.awt.*;
import javax.swing.*;

import controleur.Controleur;
import modele.Ile;
import modele.Players;
public class CVue {
	 
	/**
	 * Fenetre principale
	 */
	private JFrame frame;
	/**
	 * Representation de l ile
	 */
	private VueGrille grille;
	/**
	 * Ensemble du panneau de commandes
	 */
    private VueCommandes commandes;
    
    /**
     * Controleur
     */
    private Controleur ctrl;
    
    
    
	
    /** Construction d'une vue attach�e � un mod�le. */
    public CVue(Ile ile, Players players) {
	/** D�finition de la fen�tre principale. */
	frame = new JFrame();
	frame.setTitle("L Ile interdite");
	/**
	 * On pr�cise un mode pour disposer les diff�rents �l�ments �
	 * l'int�rieur de la fen�tre. Quelques possibilit�s sont :
	 *  - BorderLayout (d�faut pour la classe JFrame) : chaque �l�ment est
	 *    dispos� au centre ou le long d'un bord.
	 *  - FlowLayout (d�faut pour un JPanel) : les �l�ments sont dispos�s
	 *    l'un � la suite de l'autre, dans l'ordre de leur ajout, les lignes
	 *    se formant de gauche � droite et de haut en bas. Un �l�ment peut
	 *    passer � la ligne lorsque l'on redimensionne la fen�tre.
	 *  - GridLayout : les �l�ments sont dispos�s l'un � la suite de
	 *    l'autre sur une grille avec un nombre de lignes et un nombre de
	 *    colonnes d�finis par le programmeur, dont toutes les cases ont la
	 *    m�me dimension. Cette dimension est calcul�e en fonction du
	 *    nombre de cases � placer et de la dimension du contenant.
	 */
	frame.setLayout(new FlowLayout());
	
	ctrl = new Controleur(ile, players);

	/** D�finition des deux vues et ajout � la fen�tre. */
	grille = new VueGrille(ile, players, ctrl);
	frame.add(grille);
	ctrl.setGrille(grille);
	commandes = new VueCommandes(ctrl);
	frame.add(commandes);
	/**
	 * Remarque : on peut passer � la m�thode [add] des param�tres
	 * suppl�mentaires indiquant o� placer l'�l�ment. Par exemple, si on
	 * avait conserv� la disposition par d�faut [BorderLayout], on aurait
	 * pu �crire le code suivant pour placer la grille � gauche et les
	 * commandes � droite.
	 *     frame.add(grille, BorderLayout.WEST);
	 *     frame.add(commandes, BorderLayout.EAST);
	 */

	/**
	 * Fin de la plomberie :
	 *  - Ajustement de la taille de la fen�tre en fonction du contenu.
	 *  - Indiquer qu'on quitte l'application si la fen�tre est ferm�e.
	 *  - Pr�ciser que la fen�tre doit bien appara�tre � l'�cran.
	 */
	frame.pack();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }
	
	
}
