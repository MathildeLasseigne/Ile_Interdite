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

public class Controleur implements ActionListener, MouseMotionListener, MouseListener {
	
	private Ile ile;
	private Players players;
	
	/**JPannels**/
	private VueCommandes cmds;
	private VueGrille grille;
	
	/**
	 * MouseListener
	 */
	private Coord actifC;
	private Coord newC;
	
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
	private boolean premierEchange;
	
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
		premierEchange = true;
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
		if(e.getSource() instanceof JButton) {
			if(! partieFinie) {
				if(e.getSource() == this.cmds.finTour) {
					resetEtat();
					if(debutPartie == false) { //Si la partie n a pas encore commence
						if(this.players.getNbPlayers()>0) { //Si il y a au moins 1 joueur
							debutPartie = true;
							this.grille.initPlayers(this.players.getNbPlayers());
							this.grille.updatePlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
							this.grille.initHeliport(this.ile.getHeliport());
							this.grille.update();
							this.cmds.finTour.setText("Tour suivant");
							this.cmds.changeActivePlayer(players.getStringPlayer(this.players.getId()), players.getActionsRes());
							this.cmds.setInventaire(this.players.getInventaire());
							String str01 = "<u>But du jeu :</u>";
							String str02 = "Vous êtes  un groupe d' explorateurs sur une île en train de couler.";
							String str03 = "Vous êtes venus sur l'île dans le but de trouver 4 artefacts élémentaires.";
							String str04 = "Travaillez ensemble pour récupérer les 4 artefacts et vous échapper de l'île par héliport !";
							String str05 = "Mais attention ! Pour ouvrir les coffres renfermant les précieux artefacts, vous devrez trouver "+this.nbCleNecessaire+" clés correspondant à l'élément de chaque l'artefact !";
							String str06 = "<u>Instructions :</u>";
							String str1 = "Pour effectuer une action, cliquez sur le bouton correspondant <br> puis sur la direction où vous voulez l'appliquer.";
							String str2 = "Vous pouvez effectuer plusieurs actions par tour.";
							String str3 = "<u>Astuce :</u>";
							String str4 = "Pour interagir avec le jeu vous pouvez utiliser le panneau de commandes <br>Ou ";
							String str5 = "[Clic gauche] sur une zone pour s'y déplacer (ou méthode drag and drop)";
							String str6 = "[Clic droit] sur une zone pour l'assècher";
							String str7 = "[Clic molette] sur une zone pour récupérer l'artefact présent";
							//JOptionPane.showMessageDialog(null, str1+str2);
							JOptionPane.showMessageDialog(
			                        null,
			                        new JLabel("<html>"+str01+"<br>"+str02+"<br>"+str03+"<br>"+str04+"<br>"+str05+"<br>"+str06+"<br>"+str1+"<br>"+str2+"<br>"+str3+"<br>"+str4+"<br>"+str5+"<br>"+str6+"<br>"+str7+"</html>", JLabel.CENTER),
			                        "Instructions",  JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "Veuillez d'abord selectionner le nombre de joueurs et lancer la partie", "Trop d'enthousiasme !", JOptionPane.WARNING_MESSAGE);
						}
						
					} else { //Partie commencee
						//Chercher les cles
						if ( ! ile.updateIle()) { //Mise a jour de l ile
							endGame(false);
						}
						verifiePlayers(); //Sauve les players et verifie fin du jeu
						verifieZones();
						
						this.ile.checkZones();
						this.players.checkPlayers();
						 
						changePlayer();
						chercheCle();
						this.cmds.informHeliport(this.players.getCoord().equals(this.ile.getHeliport()));
						this.cmds.setInventaire(this.players.getInventaire()); //Met a jour l affichage de l inventaire
					}
				} else if (e.getSource() == this.cmds.addPlayer) {
					if(debutPartie == false) {
						if(this.players.getNbPlayers()<=20) {
							addPlayer();
						}
					}
				}
				if(debutPartie) {
					if (e.getSource() == this.cmds.move) {  //Actions
						if(this.move == true) {
							resetEtat();
						} else {
							resetEtat();
							this.move = true;
							selectJButton(this.cmds.move, false);
						}
						
					} else if (e.getSource() == this.cmds.asseche) { 
						if(this.asseche == true) {
							resetEtat();
						} else {
							resetEtat();
							this.asseche = true;
							selectJButton(this.cmds.asseche, false);
						}
						
					} else if (e.getSource() == this.cmds.artefact) { 
						if(this.searchArtefacts == true) {
							resetEtat();
						} else {
							resetEtat();
							this.searchArtefacts = true;
							selectJButton(this.cmds.artefact, false);
						}
						
					} else if (e.getSource() == this.cmds.echange) { 
						
						if(this.echange == true) {
							resetEtat();
						} else {
							resetEtat();
							if(getSommeTab(this.players.getInventaire())>0) {
								this.echange = true;
								selectJButton(this.cmds.echange, false);
								if(this.premierEchange) {
									String str1 = "Pour donner quelque chose à un joueur, veuillez d'abord sélectionner les objets à échanger.";
									String str2 = "Ils apparaitront dans votre étalage.";
									String str3 = "Puis sélectionner un joueur adjacent à l'aide des flèches ou cliquez sur le joueur correspondant.";
									JOptionPane.showMessageDialog(null,"<html>"+ str1+"<br>"+str2+"<br>"+str3+"</html>", "L'art du troc", JOptionPane.INFORMATION_MESSAGE);
								}
							} else {
								ImageIcon fauche = new ImageIcon(((new ImageIcon("images/fauche.jpg")).getImage()).getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
								JOptionPane.showMessageDialog(
				                        null,
				                        new JLabel("Vous n'avez rien à échanger !", fauche, JLabel.LEFT),
				                        "Fauché !", JOptionPane.WARNING_MESSAGE);
							}
						}
						
						
						
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
					} else if(this.cmds.estInventaire((JButton) e.getSource())) {
						if(echange) {
							addToEtalage(this.cmds.estArtefact((JButton) e.getSource()), this.cmds.getElement((JButton) e.getSource()));
						} else {
							ImageIcon objet = this.cmds.getImageIcon(this.cmds.estArtefact((JButton) e.getSource()), this.cmds.getElement((JButton) e.getSource()));
						JOptionPane.showMessageDialog(
		                        null,
		                        new JLabel("", objet, JLabel.LEFT),
		                        "Quel bel objet !", JOptionPane.PLAIN_MESSAGE);
						}
						
					} else if(e.getSource() == this.cmds.clearEtalage) {
						if(echange) {
							resetEchange();
						}
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
			selectJButton(this.cmds.echange, true);
			resetEchange();
			echange = false;
			
		}
	}
	
	private void resetEchange() {
		if(echange) {
			for(int i=0; i<this.plateformeEchange.length; i++) {
				for(int j=0; j<this.plateformeEchange[0].length; j++) {
					this.plateformeEchange[i][j] = 0;
				}
			}
			this.cmds.setEtalage(plateformeEchange);
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
		if(! this.ile.isSafe(this.ile.getHeliport())) {
			heliportPerdu = true;
			endGame(false);
		}
		if(this.ile.getZone(this.ile.getHeliport()).getType().isFull()) {
			endGame(true);
		}
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
		this.cmds.changeActivePlayer(players.getStringPlayer(this.players.getId()), players.getActionsRes());
	}
	
	/**
	 * Cherche une cle
	 * </br> A un certain pourcentage de change de trouver une cle, un de ne rien trouver et un de provoquer une montee des eaux
	 */
	private void chercheCle() {
		int probaCle = 35;
		int probaInond = 15;
		int randCle = this.ile.rangedRandomInt(0, 100);
		int randInond = this.ile.rangedRandomInt(0, 100);
		if(randCle <= probaCle) {
			int element = this.ile.rangedRandomInt(0, 3);
			this.players.addCle(element);
			JOptionPane.showMessageDialog(
                    null,
                    new JLabel("Félicitation ! Joueur "+this.players.getId()+" a trouvé une nouvelle clé !", this.cmds.getImageIcon(false, element), JLabel.LEFT),
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
				move(newC);
				
			} else if (this.asseche) {
				asseche(newC);
			} else if(this.searchArtefacts) {
				searchArtefact(newC);
			} else if(echange) {
				
				if(doExchange(newC)) {
					resetEtat();
				}
			}
			this.cmds.updateActionsPlayer(this.players.getActionsRes());
		}
	}
	
	/**
	 * Deplace le player actif sur la coord c
	 * @param c
	 * @return
	 */
	private void move(Coord newC) {
		if(this.ile.isSafe(newC)) {
			if(this.ile.getZone(newC).getType().isExit()) { //heliport
				boolean reussite = true;
				int[] artefacts = this.players.moveHelico(true, reussite, newC);
				if(reussite == true) {
					if(this.ile.getZone(newC).getType() instanceof Heliport) {
						Heliport heliport = (Heliport) this.ile.getZone(newC).getType();
						heliport.addArtefact(artefacts);
						this.grille.repaintPlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
						this.cmds.informHeliport(this.players.getCoord().equals(this.ile.getHeliport()));
					}
				} else {
					JOptionPane.showMessageDialog(null, "Vous ne pouvez pas vous déplacer là !","Vous n'êtes pas prêts pour l'heliport !", JOptionPane.WARNING_MESSAGE);
				}
			} else if(this.ile.getZone(this.players.getCoord()).getType().isExit()) {
				System.out.println("Sors heliport");
				//boolean reussite = false;
				//int[] artefacts = this.players.moveHelico(false, reussite, newC);
				Coord initC = this.players.getCoord();
				if(this.players.move(newC)) {
					this.grille.repaintPlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
					int[] artefacts = this.players.getInventaire()[0];
					if(this.ile.getZone(initC).getType() instanceof Heliport) {
						Heliport heliport = (Heliport) this.ile.getZone(initC).getType();
						heliport.removeArtefact(artefacts);
						//this.cmds.informHeliport(this.players.getCoord().equals(this.ile.getHeliport()));
						this.cmds.informHeliport(false);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Vous ne pouvez pas vous déplacer là !","L'héliport est confortable hein ?", JOptionPane.WARNING_MESSAGE);
				}
//				if(reussite == true) {
//					if(this.ile.getZone(newC).getType() instanceof Heliport) {
//						Heliport heliport = (Heliport) this.ile.getZone(newC).getType();
//						heliport.removeArtefact(artefacts);
//					}
//				} else {
//					JOptionPane.showMessageDialog(null, "Vous ne pouvez pas vous déplacer là !","L'héliport est confortable hein ?", JOptionPane.WARNING_MESSAGE);
//				}
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
	}
	
	/**
	 * Le player actif asseche la coord newC
	 * @param newC
	 */
	private void asseche(Coord newC) {
		if(! this.ile.asseche(newC)) {
			JOptionPane.showMessageDialog(null, "Vous ne pouvez pas assécher cette zone !","Plus sec que sec", JOptionPane.WARNING_MESSAGE);
		} else {
			this.players.play();
			this.grille.update();
		}
	}
	
	/**
	 * Le player actif recupere un artefact sur la coord newC
	 * @param newC
	 */
	private void searchArtefact(Coord newC) {
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
			                        new JLabel("Félicitation ! Vous avez trouvé un nouvel artefact !", this.cmds.getImageIcon(true, element), JLabel.LEFT),
			                        "Nouveau trésor !", JOptionPane.INFORMATION_MESSAGE);
						 this.cmds.setInventaire(this.players.getInventaire());
					}
				} else {
					ImageIcon tresorVerrouille = new ImageIcon(((new ImageIcon("images/coffre_verrouille.jpg")).getImage()).getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
					JOptionPane.showMessageDialog(null, "Vous ne possèdez pas assez de clés ! Cherchez encore !","J'ouvre le coffre avec quoi ?", JOptionPane.WARNING_MESSAGE, tresorVerrouille);
				}
				
			}
		} else {
			JOptionPane.showMessageDialog(null, "Il n'y a pas d'artefact ici !","Ouvrez les yeux !", JOptionPane.WARNING_MESSAGE);
		}
	}
	
	
	/**
	 * Donne les objets du player actif se situant dans la plateforme d echange au player sur la position c
	 * @param c
	 * @return
	 */
	private boolean doExchange(Coord c) {
		if(getSommeTab(plateformeEchange) == 0) {
			JOptionPane.showMessageDialog(null, "Vous n'avez selectionné aucun objet à échanger !","Etalage vide", JOptionPane.WARNING_MESSAGE);
			return false;
		} else {
			int id = this.players.getId(c);
			if(id != -1) {
				if(! this.players.modInventaire(id, true, plateformeEchange)) {
					System.out.println("Problem echange !");
					return false;
				} else {
					ImageIcon troc = new ImageIcon(((new ImageIcon("images/echange.jpg")).getImage()).getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
					JOptionPane.showMessageDialog(
		                    null,
		                    new JLabel("Vous avez donné "+ getListInventaire(plateformeEchange) + " à "+this.players.getStringPlayer(id), troc, JLabel.LEFT),
		                    "Généreux", JOptionPane.INFORMATION_MESSAGE);
					this.players.modInventaire(this.players.getId(), false, plateformeEchange);
					resetEchange();
					this.cmds.setInventaire(this.players.getInventaire());
					this.players.play();
					premierEchange = false;
				}
				return true;
			} else {
				ImageIcon whereP = new ImageIcon(((new ImageIcon("images/cherche_player.jpg")).getImage()).getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
				JOptionPane.showMessageDialog(
	                    null,
	                    new JLabel("Il n'y a aucun joueur ici !", whereP, JLabel.LEFT),
	                    "Regardez mieux !", JOptionPane.WARNING_MESSAGE);
				return false;
			}
		}
		
	}
	
	/**
	 * Ajoute l objet a la plateforme d echange
	 * @param artefact
	 * @param element
	 * @return
	 */
	private boolean addToEtalage(boolean artefact, int element) {
		int y;
		if(artefact) {
			y = 0;
		} else {
			y = 1;
		}
		if(this.players.getInventaire()[y][element] > this.plateformeEchange[y][element]) {
			this.plateformeEchange[y][element]++;
			this.cmds.setEtalage(plateformeEchange);
			return true;
		} else {
			ImageIcon fauche = new ImageIcon(((new ImageIcon("images/fauche.jpg")).getImage()).getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH));
			JOptionPane.showMessageDialog(
                    null,
                    new JLabel("Vous n'avez plus assez de stock de cet objet !", fauche, JLabel.LEFT),
                    "Généreux", JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}
	
	/**
	 * Retourne la liste des objets présents dans l'inventaire
	 * @param inventaire
	 * @return
	 */
	private String getListInventaire(int[][] inventaire) {
		String list = "";
		if(getSommeTab(plateformeEchange) == 0) {
			return "Inventaire vide";
		}
		int count = 0;
		for(int i = 0; i<inventaire.length; i++) {
			for(int el = 0; el<inventaire[0].length; el++) {
				if(i==0 && inventaire[i][el] != 0) {
					if(count != 0) {
						list += ", ";
					}
					count++;
					list += "x"+inventaire[i][el]+" [Artefact";
					if(el == 0) {
						list += " de l'eau]";
					} else if(el == 1) {
						list += " du feu]";
					} else if(el == 2) {
						list += " de l'air]";
					} else if(el == 3) {
						list += " de la terre]";
					}
				} else if(inventaire[i][el] != 0) {
					if(count != 0) {
						list += ", ";
					}
					count++;
					list += "x"+inventaire[i][el]+" [Clé";
					if(el == 0) {
						list += " de l'eau]";
					} else if(el == 1) {
						list += " du feu]";
					} else if(el == 2) {
						list += " de l'air]";
					} else if(el == 3) {
						list += " de la terre]";
					}
				}
			}
		}
		return list;
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
                        new JLabel("<html>Félicitation ! Vous avez gagné ! <br>Vous pouvez maintenant vivre une vie de luxe !</html>", tresor, JLabel.LEFT),
                        "Aventurier à succès", JOptionPane.PLAIN_MESSAGE);
			}
		} else {
			if(artefactPerdu) {
				if(this.players.getNbPlayersAlive() != this.players.getNbPlayers()) {
					int dead = this.players.getNbPlayers()-this.players.getNbPlayersAlive();
					JOptionPane.showMessageDialog(null, "<html>Vous avez laissé un artefact être submergé !<br> Votre expedition tombe à l'eau. <br>"+dead+" joueurs sont morts !</html>",
	                        "Aventuriers malchanceux",  JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "<html>Vous avez laissé un artefact être submergé ! <br>Votre expedition tombe à l'eau. </html>",
	                        "Faites plus attention à vos trésors !",  JOptionPane.PLAIN_MESSAGE);
				}
			} else if(heliportPerdu) {
				if(this.players.getNbPlayersAlive() != this.players.getNbPlayers()) {
					int dead = this.players.getNbPlayers()-this.players.getNbPlayersAlive();
					JOptionPane.showMessageDialog(null, "<html>Vous avez laissé l heliport être submergé ! <br>Vous êtes dorénavant bloqué sur l'ile jusqu'à ce que vous vous noyez. <br>"+dead+" joueurs sont morts avant vous. Les chanceux !</html>",
	                        "Un cimetière d'eau",  JOptionPane.PLAIN_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "<html>Vous avez laissé l heliport être submergé ! <br>Vous êtes dorénavant bloqué sur l'ile jusqu'à ce que vous vous noyez. </html>",
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
	
	private int getSommeTab(int[][] tab) {
		int sum = 0;
		for(int i = 0; i<tab.length;i++) {
			for(int j = 0; j<tab[0].length; j++) {
				sum+= tab[i][j];
			}
		}
		return sum;
	}
	
	
	/**
	 * Donne au bouton une apparence activee/desactivee
	 * @param button
	 * @param select le button est-il deja selectionne ?
	 */
	private void selectJButton(JButton button, boolean select) {
		this.cmds.selectionne(button, select);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(debutPartie) {
			if(this.players.getActionsRes()==0) {
				JOptionPane.showMessageDialog(null, "Il ne vous reste plus d'action !","Il vous reste encore de l'énergie ?", JOptionPane.ERROR_MESSAGE);
			} else {
				Coord c = this.grille.getCoord(e.getX(), e.getY());
				//System.out.print("mouseClicked ");
				if(this.echange == true) {
					if(this.ile.estVoisin(this.players.getCoord(), c, false, false)) {
						if(doExchange(c)) {
							resetEtat();
						}
					} else {
						JOptionPane.showMessageDialog(null, "Cette zone est trop loin !","1km à pied... 10km à pied...", JOptionPane.WARNING_MESSAGE);
					}
				} else if(e.getButton() == MouseEvent.BUTTON1) {
					int testWay = this.ile.searchShortestWay(this.players.getCoord(), c, false, false);
					System.out.println("Test way = "+testWay+"<="+ this.players.getActionsRes()+" = "+ (testWay<=this.players.getActionsRes()));
					if(testWay<=this.players.getActionsRes()) {
						if(this.ile.isSafe(c)) {
							move(c);
							for(int act = 1; act < testWay; act++) {
								this.players.play();
							}
							this.grille.repaintPlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
						}
					} else {
						JOptionPane.showMessageDialog(null, "Cette zone est trop loin !","1km à pied... 10km à pied...", JOptionPane.WARNING_MESSAGE);
					}
				} else if (e.getButton() == MouseEvent.BUTTON2) {
					if(this.ile.estVoisin(this.players.getCoord(), c, false, false)) {
						searchArtefact(c);
					} else {
						JOptionPane.showMessageDialog(null, "Cette zone est trop loin !","1km à pied... 10km à pied...", JOptionPane.WARNING_MESSAGE);
					}
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					if(this.ile.estVoisin(this.players.getCoord(), c, false, false)) {
						asseche(c);
					} else {
						JOptionPane.showMessageDialog(null, "Cette zone est trop loin !","1km à pied... 10km à pied...", JOptionPane.WARNING_MESSAGE);
					}
				}
				this.cmds.updateActionsPlayer(this.players.getActionsRes());
			}
		}
		
		
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(debutPartie) {
			//System.out.println("mousePressed ("+e.getX()+", "+e.getY()+")");
			Coord c = this.grille.getCoord(e.getX(), e.getY());
			if(c.equals(this.players.getCoord())) { //Verifie a la fois que on ne selectionne pas d autre joueur et n est pas clic
				this.actifC = c;
				//System.out.println("ActifC = Coord"+ c);
				if(e.getButton() == MouseEvent.BUTTON1) {
					selectJButton(this.cmds.move, false);
					this.move = true;
				} else if(e.getButton() == MouseEvent.BUTTON2) {
					selectJButton(this.cmds.artefact, false);
					this.searchArtefacts = true;
				} else if(e.getButton() == MouseEvent.BUTTON3) {
					selectJButton(this.cmds.asseche, false);
					this.asseche = true;
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(debutPartie) {
			//System.out.println("Mouse released");
			if(this.players.getActionsRes()==0) {
				JOptionPane.showMessageDialog(null, "Il ne vous reste plus d'action !","Il vous reste encore de l'énergie ?", JOptionPane.ERROR_MESSAGE);
				this.grille.repaintPlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
			} else if(this.echange == false) {
				Coord c = this.grille.getCoord(e.getX(), e.getY());
				if(e.getButton() == MouseEvent.BUTTON1) {
					
					if(c.equals(this.ile.getHeliport()) && this.actifC != null) {
						JOptionPane.showMessageDialog(null, "<html>Vous ne pouvez pas déplacer votre joueur en utilisant cette méthode. <br> Veuillez utiliser une autre méthode ! <br><br> <u>Astuce :</u> Utilisez les flèches du tableau de commande ou cliquez la zone correspondante</html>",
		                        "Heliport hors limite",  JOptionPane.PLAIN_MESSAGE);
					} else if(this.players.getId(actifC) != -1) {
						int testWay = this.ile.searchShortestWay(actifC, c, false, false);
						//System.out.println("Test way = "+testWay+"<="+ this.players.getActionsRes()+" = "+ (testWay<=this.players.getActionsRes()));
						if(testWay<=this.players.getActionsRes()) {
							if(this.ile.isSafe(c)) {
								move(c);
								for(int act = 1; act < testWay; act++) {
									this.players.play();
								}
								this.grille.repaintPlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
							}
						} else {
							JOptionPane.showMessageDialog(null, "Cette zone est trop loin !","1km à pied... 10km à pied...", JOptionPane.WARNING_MESSAGE);
						}
					}
					System.out.println(this.players.getCoord());
					resetEtat();
				} else if(e.getButton() == MouseEvent.BUTTON2) {
					resetEtat();
				} else if(e.getButton() == MouseEvent.BUTTON3) {
					resetEtat();
				}
				this.grille.repaintPlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
				this.cmds.updateActionsPlayer(this.players.getActionsRes());
				this.actifC = null;
				//this.grille.repaintPlayers(this.players.getCoordPlayersAlive(), this.players.getIdPlayersAlive());
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if(debutPartie) {
			if(this.actifC != null) {
				if(this.players.getId(actifC) != -1) {
					if(this.actifC == this.ile.getHeliport()) {
						JOptionPane.showMessageDialog(null, "<html>Vous ne pouvez pas déplacer votre joueur en utilisant cette méthode. <br> Veuillez utiliser une autre méthode !</html>",
		                        "Heliport hors limite",  JOptionPane.PLAIN_MESSAGE);
					} else {
						int id = this.players.getId(actifC);
						//System.out.println("id Player = "+id);
						Coord c = this.grille.getCoord(e.getX(), e.getY());
						this.grille.movePlayerLabel(id, c);
						//System.out.println("Mouse dragged avec effet");
					}
				}
				//System.out.println("Mouse dragged sans effet");
				}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

}
