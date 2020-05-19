package controleur;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import modele.Artefact;
import modele.Coord;
import modele.Direction;
import modele.Heliport;
import modele.Ile;
import modele.Players;
import vue.VueCommandes;
import vue.VueGrille;
import vue.VuePositionnement;

public class Controleur implements ActionListener {
	
	private Ile ile;
	private Players players;
	
	/**JPannels**/
	private VueCommandes cmds;
	private VueGrille grille;
	
	/**Heliport**/
	
	private ArrayList<Integer> playersOnHeli = new ArrayList<>();
	private Coord heliport;
	
	private final int nbCleNecessaire = 1;
	
	
	
	/** Etats **/
	private boolean debutPartie;
	private boolean partieFinie;
	private boolean artefactPerdu;
	private boolean heliportPerdu;
	
	private boolean move;
	private boolean asseche;
	private boolean searchArtefacts;
	private boolean echange;
	
	/**
	 * Echange
	 */
	private int[][] plateformeEchange = new int[2][4]; //A la maniere d un inventaire
	
	/**
	 * Cree un controleur gerant la partie
	 * @param ile Le modele de l ile
	 * @param players La liste des joueurs
	 */
	public Controleur(Ile ile, Players players) {
		this.ile = ile;
		this.players = players;
		init();
	}
	
	/**
	 * Initialise les etats et l heliport
	 */
	private void init() {
		// Heliport
		heliport = ile.getHeliport();
		artefactPerdu = false;
		
		//Etats
		debutPartie = false;
		partieFinie = false;
		move = false;
		asseche = false;
		searchArtefacts = false;
		echange = false;
		
		//Echange
		for(int i=0; i<this.plateformeEchange.length; i++) {
			for(int j=0; j<this.plateformeEchange[0].length; j++) {
				this.plateformeEchange[i][j] = 0;
			}
		}
	}
	
	
	//Link JPannels
	public void setCommandes(VueCommandes cmd) {
			this.cmds = cmd;
	}
	
	public void setGrille(VueGrille grille) {
		this.grille = grille;
	}
	
	
	


	@Override
	public void actionPerformed(ActionEvent e) {
		if(! partieFinie) {
			if(e.getSource() == this.cmds.finTour) {
				resetEtat();
				if(debutPartie == false) { //Si la partie n a pas encore commence
					if(this.players.getNbPlayers()>0) { //Si il y a au moins 1 joueur
						debutPartie = true;
						this.grille.initPlayers(this.players.getNbPlayers());
						this.grille.updatePlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
						this.grille.update();
						this.cmds.finTour.setText("Tour suivant");
						this.cmds.changeActivePlayer(players.getId(), players.getActionsRes());
						this.cmds.setInventaire(this.players.getInventaire());
						String str1 = "Pour effectuer une action, cliquez sur le bouton correspondant <br> puis sur la direction où vous voulez l'appliquer.<br>";
						String str2 = "Vous pouvez effectuer 3 actions en 1 tour.";
						//JOptionPane.showMessageDialog(null, str1+str2);
						JOptionPane.showMessageDialog(
		                        null,
		                        new JLabel("<html>"+str1+str2+"</html>", JLabel.CENTER),
		                        "Instructions",  JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Veuillez d'abord selectionner le nombre de joueurs et lancer la partie", "Trop d'enthousiasme !", JOptionPane.WARNING_MESSAGE);
					}
					
				} else { //Partie commencee
					//Chercher les cles
					if ( ! ile.updateIle()) { //Mise a jour de l ile
						endGame(false);
					}
					chercheCle();
					this.ile.checkZones();
					this.players.checkPlayers();
					 //Sauve les players et verifie fin du jeu -> Faire un verifie artefacts ?
					changePlayer();
					verifiePlayers();
					verifieZones();
					this.cmds.setInventaire(this.players.getInventaire()); //Met a jour l affichage de l inventaire
				}
			} else if (e.getSource() == this.cmds.addPlayer) {
				if(debutPartie == false) {
					addPlayer();
				}
			}
			if(debutPartie) {
				if (e.getSource() == this.cmds.move) {  //Actions
					resetEtat();
					this.move = true;
					selectJButton(this.cmds.move, false);
				} else if (e.getSource() == this.cmds.asseche) { //Actions de positionnement
					resetEtat();
					this.asseche = true;
					selectJButton(this.cmds.asseche, false);
				} else if (e.getSource() == this.cmds.artefact) { //Actions de positionnement
					resetEtat();
					this.searchArtefacts = true;
					selectJButton(this.cmds.artefact, false);
				} else if (e.getSource() == this.cmds.up) { //Actions de positionnement
					actionPos(Direction.up);
				} else if (e.getSource() == this.cmds.down) {
					actionPos(Direction.down);
				} else if (e.getSource() == this.cmds.center) {
					actionPos(Direction.center);
				} else if (e.getSource() == this.cmds.right) {
					actionPos(Direction.right);
				} else if (e.getSource() == this.cmds.left) {
					actionPos(Direction.left);
				}
				
			} else {
				if (e.getSource() != this.cmds.addPlayer && e.getSource() != this.cmds.finTour) {
					JOptionPane.showMessageDialog(null, "Veuillez d'abord selectionner le nombre de joueurs et lancer la partie", "Trop d'enthousiasme !", JOptionPane.WARNING_MESSAGE);
				}				
			}
			
		} else {
			JOptionPane.showMessageDialog(null, "La partie est déjà finie !","Où est passée l'île ?", JOptionPane.ERROR_MESSAGE); //Pour ne pas pouvoir continuer apres la fin
		}
		
	}
	
	
	
	
	
	
		/**
	 * Remet les booleans des etat commandant les actions a false
	 */
	private void resetEtat() {
		if(move) {
			selectJButton(this.cmds.move, true);
			move = false;
		}
		if(asseche) {
			selectJButton(this.cmds.asseche, true);
			asseche = false;
		}
		if(searchArtefacts) {
			selectJButton(this.cmds.artefact, true);
			searchArtefacts = false;
		}
		if(echange) {
			//selectJButton(this.cmds.artefact, true);
			echange = false;
			for(int i=0; i<this.plateformeEchange.length; i++) {
				for(int j=0; j<this.plateformeEchange[0].length; j++) {
					this.plateformeEchange[i][j] = 0;
				}
			}
		}
	}
	
	
	/**
	 * Pour chaque player, verifie si ils sont safe et sinon les sauver.
	 * </br> Verifie aussi si un player mort n avait pas d artefact
	 * </br> Peut aussi etre utilise pour la victoire
	 */
	private void verifiePlayers() {
		int nbMort = 0;
		String joueurs = "";
		for(Coord c : this.players.getCoordPlayersAlive()) {
			if(! this.ile.isSafe(c)) {
				Coord safeC = this.ile.getVoisin(c, false, this.players.getCoordPlayersAlive());
				boolean nulle = false;
				if(safeC == null) {
					nulle = true;
				}
				if(this.ile.isSafe(safeC) || nulle) {
					int idMort = this.players.savePlayer(c, safeC);
					if(idMort != -1) { //S'il y a eu un mort
						if(nbMort == 0) {
							joueurs += this.players.getPlayer(idMort);
						} else {
							joueurs += ", " + this.players.getPlayer(idMort);
						}
						nbMort ++;
					}
				} else {
					this.players.savePlayer(c, null);
				}
				if(nbMort != 0) {
					
				}
				
				System.out.println("Old c :"+this.ile.isSubmerged(c));
				System.out.println("New c :"+this.ile.isSubmerged(safeC));
			}
		}
		this.grille.repaintPlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
		if(nbMort != 0) {
			String str = "";
			if(nbMort == 1) {
				str += " s'est noyé !";
			} else {
				str += " se sont noyés !";
			}
			ImageIcon noyade = new ImageIcon("images/noyade.jpg");
			JOptionPane.showMessageDialog(
	                null,
	                new JLabel("Malheur ! "+joueurs+str, noyade, JLabel.LEFT),
	                "Nous nous souviendrons de votre sacrifice !", JOptionPane.INFORMATION_MESSAGE);
		}
		
		if(this.players.getNbPlayersAlive()==0) {
			endGame(false);
		}
	}
	
	/**
	 * Verifie si les zones importantes ne sont pas submergées.
	 */
	private void verifieZones() {
		if(! this.ile.accessibiliteArtefacts()) {
			artefactPerdu = true;
			endGame(false);
		}
//		if(! this.ile.isSafe(this.ile.getHeliport())) {
//			heliportPerdu = true;
//			endGame(false);
//		}
//		if(this.ile.getZone(this.ile.getHeliport()).getType().isFull()) {
//			endGame(true);
//		}
	}
	
	
	
	/**
	 * Recupere la coordonnee dans la direction donnee par rapport a la coordonnee 
	 * du player actif de players
	 * </br> ! Verifier si est dans l ile !
	 * @param d
	 * @return
	 */
	public Coord getCoordDir(Direction d) {
		Coord pl = players.getCoord();
		if(d == Direction.center) {
			return pl;
		} else if (d == Direction.down) {
			return new Coord(pl.getAbsc(), pl.getOrd()+1);
		} else if (d == Direction.left) {
			return new Coord(pl.getAbsc()-1, pl.getOrd());
		} else if (d == Direction.up) {
			return new Coord(pl.getAbsc(), pl.getOrd()-1);
		} else if (d == Direction.right) {
			return new Coord(pl.getAbsc()+1, pl.getOrd());
		}
		return null;
	}
	
	/**
	 * Ajoute un nouveau player a players
	 */
	private void addPlayer() {
		if(debutPartie == false) {
			Coord c = ile.getRandCoord(3);
			while (! players.addPlayer(c)) {
				addPlayer();
			}
			this.cmds.changeNbPlayer(players.getNbPlayersAlive());
		}
		
	}
	
	
	/**
	 * Change le player actif et son affichage
	 */
	private void changePlayer() {
		players.changePlayer();
		this.cmds.changeActivePlayer(players.getId(), players.getActionsRes());
	}
	
	/**
	 * Cherche une cle
	 * </br> A un certain pourcentage de change de trouver une cle, un de ne rien trouver et un de provoquer une montee des eaux
	 */
	private void chercheCle() {
		int probaCle = 30;
		int probaInond = 20;
		int randCle = this.ile.rangedRandomInt(0, 100);
		int randInond = this.ile.rangedRandomInt(0, 100);
		if(randCle <= probaCle) {
			int element = this.ile.rangedRandomInt(0, 3);
			this.players.addCle(element);
			JOptionPane.showMessageDialog(
                    null,
                    new JLabel("Félicitation ! Vous avez trouvé une nouvelle clé !", this.cmds.getImageIcone(false, element), JLabel.LEFT),
                    "Un pas de plus vers les artefacts !", JOptionPane.INFORMATION_MESSAGE);
			this.cmds.setInventaire(this.players.getInventaire());
			
		} else {
			if(randInond <= probaInond) {
				if(! this.ile.updateIle()) {
					endGame(false);
				}
				ImageIcon vague = new ImageIcon("images/vague.jpg");
				JOptionPane.showMessageDialog(
                        null,
                        new JLabel("Oh non ! Vous avez déclanché une montée des eaux en cherchant trop profondément dans le sable !", vague, JLabel.LEFT),
                        "Catastrophe !", JOptionPane.INFORMATION_MESSAGE);
				//JOptionPane.showMessageDialog(null, "Oh non ! Vous avez déclanché une montée des eaux en cherchant trop profondément dans le sable !","Catastrophe !", JOptionPane.INFORMATION_MESSAGE);
			}
		}

	}
	
	
	/**
	 * Effectue l action correspondante en fonction des etats
	 * @param dir
	 */
	private void actionPos(Direction dir) {
		if(this.players.getActionsRes()==0) {
			JOptionPane.showMessageDialog(null, "Il ne vous reste plus d'action !","Il vous reste encore de l'énergie ?", JOptionPane.ERROR_MESSAGE);
		} else {
			Coord newC = getCoordDir(dir);
			if(this.move) {
				if(this.ile.isSafe(newC)) {
					if(this.ile.getZone(newC).getType().isExit()) {
						boolean reussite = false;
						int[] artefacts = this.players.moveHelico(true, reussite, newC);
						if(reussite == true) {
							if(this.ile.getZone(newC).getType() instanceof Heliport) {
								Heliport heliport = (Heliport) this.ile.getZone(newC).getType();
								heliport.addArtefact(artefacts);
							}
						} else {
							JOptionPane.showMessageDialog(null, "Vous ne pouvez pas vous déplacer là !","Vous n'êtes pas prêts pour l'heliport !", JOptionPane.WARNING_MESSAGE);
						}
					} else if(this.ile.getZone(this.players.getCoord()).getType().isExit()) {
						boolean reussite = false;
						int[] artefacts = this.players.moveHelico(false, reussite, newC);
						if(reussite == true) {
							if(this.ile.getZone(newC).getType() instanceof Heliport) {
								Heliport heliport = (Heliport) this.ile.getZone(newC).getType();
								heliport.removeArtefact(artefacts);
							}
						} else {
							JOptionPane.showMessageDialog(null, "Vous ne pouvez pas vous déplacer là !","L'héliport est confortable hein ?", JOptionPane.WARNING_MESSAGE);
						}
					} else {
						if(! this.players.move(newC)) {
							JOptionPane.showMessageDialog(null, "Vous ne pouvez pas vous déplacer là !","1km à pied... 10km à pied...", JOptionPane.WARNING_MESSAGE);
						} else {
							this.grille.repaintPlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
							//this.grille.update();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Cette zone n'est pas accessible !","Le vide vous attire ?", JOptionPane.ERROR_MESSAGE);
				}
				
			} else if (this.asseche) {
				if(! this.ile.asseche(newC)) {
					JOptionPane.showMessageDialog(null, "Vous ne pouvez pas assécher cette zone !","Plus sec que sec", JOptionPane.WARNING_MESSAGE);
				} else {
					this.players.play();
					this.grille.update();
				}
			} else if(this.searchArtefacts) {
				if(this.ile.getZone(newC).getType().hasArtefact()) {
					if(this.ile.getZone(newC).getType() instanceof Artefact) {
						Artefact artefact = (Artefact) this.ile.getZone(newC).getType();
						//int element = artefact.takeArtefact();
						int element = artefact.getElement();
						if(this.players.getNbCle(element) >= this.nbCleNecessaire) {
							if(this.players.addArtefact(element)) {
								this.players.play();
								this.grille.update();
								artefact.takeArtefact();
								this.cmds.updateActionsPlayer(this.players.getActionsRes());
								 JOptionPane.showMessageDialog(
					                        null,
					                        new JLabel("Félicitation ! Vous avez trouvé un nouvel artefact !", this.cmds.getImageIcone(true, element), JLabel.LEFT),
					                        "Nouveau trésor !", JOptionPane.INFORMATION_MESSAGE);
								 this.cmds.setInventaire(this.players.getInventaire());
							}
						} else {
							ImageIcon tresorVerrouille = new ImageIcon(((new ImageIcon("images/coffre_verrouille.jpg")).getImage()).getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
							JOptionPane.showMessageDialog(null, "Vous ne possèdez pas assez de clés ! Cherchez encore !","J'ouvre le coffre avec quoi ?", JOptionPane.WARNING_MESSAGE, tresorVerrouille);
						}
						
					}
				}
			} else if(echange) {
				boolean aEchange = false;
				for(int i=0; i<this.plateformeEchange.length; i++) {
					for(int j=0; j<this.plateformeEchange[0].length; j++) {
						if(this.plateformeEchange[i][j] != 0) {
							aEchange = true;
						}
					}
				}
				if(! aEchange) {
					//Message Vous n'avez selectionne aucun objet a echanger !
				} else {
					
				}
			}
			this.cmds.updateActionsPlayer(this.players.getActionsRes());
		}
	}
	
	
	
	/**
	 * Affiche le message de fin de partie
	 * @param win Partie gagnee ?
	 */
	private void endGame(boolean win) {
		if(win) {
			
			if(this.players.getNbPlayersAlive() != this.players.getNbPlayers()) {
				int dead = this.players.getNbPlayers()-this.players.getNbPlayersAlive();
				JOptionPane.showMessageDialog(null, "<html>Vous avez gagné !<br> Mais vous avez laissé "+dead+" joueurs derrière vous ! <br> Monstres ! Q_Q</html>",
                        "Aventurier sans pitié",  JOptionPane.PLAIN_MESSAGE);
			} else {
				ImageIcon tresor = new ImageIcon(((new ImageIcon("images/coffre_au_tresor.jpg")).getImage()).getScaledInstance(200, 165, java.awt.Image.SCALE_SMOOTH));
				JOptionPane.showMessageDialog(
                        null,
                        new JLabel("Félicitation ! Vous avez gagné ! Vous pouvez maintenant vivre une vie de luxure !", tresor, JLabel.LEFT),
                        "Aventurier à succès", JOptionPane.PLAIN_MESSAGE);
			}
		} else {
			if(artefactPerdu) {
				if(this.players.getNbPlayersAlive() != this.players.getNbPlayers()) {
					int dead = this.players.getNbPlayers()-this.players.getNbPlayersAlive();
					JOptionPane.showMessageDialog(null, "Vous avez laissé un artefact être submergé ! Votre expedition tombe à l'eau. "+dead+" joueurs sont morts !",
	                        "Aventuriers malchanceux",  JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Vous avez laissé un artefact être submergé ! Votre expedition tombe à l'eau. ",
	                        "Faites plus attention à vos trésors !",  JOptionPane.PLAIN_MESSAGE);
				}
			} else if(heliportPerdu) {
				if(this.players.getNbPlayersAlive() != this.players.getNbPlayers()) {
					int dead = this.players.getNbPlayers()-this.players.getNbPlayersAlive();
					JOptionPane.showMessageDialog(null, "Vous avez laissé l heliport être submergé ! Vous êtes dorénavant bloqué sur l'ile jusqu'à ce que vous vous noyez. "+dead+" joueurs sont morts avant vous. Les chanceux !",
	                        "Un cimetière d'eau",  JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Vous avez laissé l heliport être submergé ! Vous êtes dorénavant bloqué sur l'ile jusqu'à ce que vous vous noyez. ",
							"Un cimetière d'eau",  JOptionPane.PLAIN_MESSAGE);
				}
			} else {
				if(this.players.getNbPlayersAlive() != this.players.getNbPlayers()) {
					int dead = this.players.getNbPlayers()-this.players.getNbPlayersAlive();
					JOptionPane.showMessageDialog(null, "Vous avez perdu ! "+dead+" joueurs sont morts !",
	                        "Aventuriers malchanceux",  JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Vous avez perdu !",
	                        "Trop tard",  JOptionPane.PLAIN_MESSAGE);
				}
			}
			
			
		}
		this.partieFinie = true;
	}
	
	
	/**
	 * Donne au bouton une apparence activee/desactivee
	 * @param button
	 * @param select
	 */
	private void selectJButton(JButton button, boolean select) {
		this.cmds.selectionne(button, select);
	}

}
