package game;

import java.awt.EventQueue;

import modele.Ile;
import modele.Players;
import vue.CVue;

public class Ile_Interdite {

	/**
     * L'amor�age est fait en cr�ant le mod�le et la vue, par un simple appel
     * � chaque constructeur.
     * Ici, le mod�le est cr�� ind�pendamment (il s'agit d'une partie autonome
     * de l'application), et la vue prend le mod�le comme param�tre (son
     * objectif est de faire le lien entre mod�le et utilisateur).
     */
    public static void main(String[] args) {
	/**
	 * Pour les besoins du jour on consid�re la ligne EvenQueue... comme une
	 * incantation qu'on pourra expliquer plus tard.
	 */
	EventQueue.invokeLater(() -> {
		/** Voici le contenu qui nous int�resse. */
                Ile ile = new Ile();
                Players players = new Players();
                CVue vue = new CVue(ile, players);
	    });
    }

}
