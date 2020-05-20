package vue;

import java.awt.*;
import javax.swing.*;

import controleur.Controleur;

public class VueCommandes extends JPanel {

	public JLabel idPlayer;
	public JLabel actionsPlayer;
	public JLabel nbPlayers;
	public JLabel inventaire;
	
	private Color buttonBackground;
	
	/**
	 * Actions
	 */
	public JButton addPlayer;
	public JButton finTour;
	public JButton move;
	public JButton asseche;
	public JButton artefact;
	public JButton echange;
	
	/**
	 * Positionnement
	 */
	public JButton up;
	public JButton down;
	public JButton center;
	public JButton right;
	public JButton left;
	
	/**
	 * Inventaire
	 */
	public JButton artEau;
	public JButton artFeu;
	public JButton artAir;
	public JButton artTerre;
	public JButton cleEau;
	public JButton cleFeu;
	public JButton cleAir;
	public JButton cleTerre;
	
	
	/**
	 * Images
	 */
	private ImageIcon artEauImg;
	private ImageIcon artFeuImg;
	private ImageIcon artAirImg;
	private ImageIcon artTerreImg;
	private ImageIcon cleEauImg;
	private ImageIcon cleFeuImg;
	private ImageIcon cleAirImg;
	private ImageIcon cleTerreImg;
	
	//Echange
	private ImageIcon caseEtalage;
	private JLabel[][] etalage = new JLabel[4][2];
	public JButton clearEtalage;
	
	
	/**
     * Controleur
     */
    private Controleur ctrl;
    
	
	public VueCommandes(Controleur control) {
		this.ctrl = control;
		ctrl.setCommandes(this);
		setImages();
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//Ajout Joueurs  Ligne 0
		nbPlayers = new JLabel("Nb Joueurs : 0 ", JLabel.CENTER);
		c.gridx = 1;
		c.gridy = 0;
		this.add(nbPlayers, c);
		
		addPlayer = new JButton("+ player");
		c.gridx = 2;
		c.gridy = 0;
		this.add(addPlayer, c);
		addPlayer.addActionListener(ctrl);
		
		this.buttonBackground = addPlayer.getBackground();
		
		
		//Passage de tour  Ligne 1-3
		int debutTour = 1;
		idPlayer = new JLabel("Actif : Joueur _ ", JLabel.CENTER);
		c.gridx = 1;
		c.gridy = debutTour+1;
		c.ipady = 40;
		this.add(idPlayer, c);
		
		finTour = new JButton("Commencer"); //Deviens "Tour suivant" après le debut de la partie
		c.gridx = 2;
		c.gridy = debutTour;
		c.gridheight = 3;
		c.gridwidth = 2;
		//c.ipady = 40;
		c.fill = GridBagConstraints.VERTICAL;
		this.add(finTour, c);
		finTour.addActionListener(ctrl);
		//reset
		c.gridheight = 1; 
		c.gridwidth = 1;
		c.ipady = 0;
		c.fill = GridBagConstraints.NONE;
		
		//Actions  Ligne 4
		int debutAction = 4;
		actionsPlayer = new JLabel("Actions restantes: 3 ", JLabel.CENTER);
		c.gridx = 0;
		c.gridy = debutAction;
		this.add(actionsPlayer, c);
		
		move = new JButton("Se déplacer");
		c.gridx = 1;
		c.gridy = debutAction;
		this.add(move, c);
		move.addActionListener(ctrl);
		
		asseche = new JButton("Assecher");
		c.gridx = 2;
		c.gridy = debutAction;
		this.add(asseche, c);
		asseche.addActionListener(ctrl);
		
		artefact = new JButton("Prend Artefact");
		c.gridx = 3;
		c.gridy = debutAction;
		this.add(artefact, c);
		artefact.addActionListener(ctrl);
		
		echange = new JButton("Echange");
		c.gridx = 4;
		c.gridy = debutAction;
		this.add(echange, c);
		echange.addActionListener(ctrl);
		
		
		
		
		//Positionnement  Ligne 5-7
		int debutPos = 5;
		int alignementPos = 2;
		
		
		up = new JButton("^");
		c.gridx = alignementPos;
		c.gridy = debutPos;
		c.anchor = GridBagConstraints.SOUTH;
		this.add(up, c);
		up.addActionListener(ctrl);
		
		down = new JButton("v");
		c.gridx = alignementPos;
		c.gridy = debutPos+2;
		c.anchor = GridBagConstraints.NORTH;
		this.add(down, c);
		down.addActionListener(ctrl);
		
		
		center = new JButton("O");
		c.gridx = alignementPos;
		c.gridy = debutPos+1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		this.add(center, c);
		center.addActionListener(ctrl);
		c.fill = GridBagConstraints.NONE;
		
		right = new JButton(">");
		c.gridx = alignementPos+1;
		c.gridy = debutPos+1;
		c.anchor = GridBagConstraints.WEST;
		this.add(right, c);
		right.addActionListener(ctrl);
		
		left = new JButton("<");
		c.gridx = alignementPos-1;
		c.gridy = debutPos+1;
		c.anchor = GridBagConstraints.EAST;
		this.add(left, c);
		left.addActionListener(ctrl);
		
		//reset
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.CENTER;
		
		
		//Inventaire Ligne 8 - 12
		//https://stackoverflow.com/questions/4801386/how-do-i-add-an-image-to-a-jbutton
		int debutInventaire = 8;
		inventaire = new JLabel("Inventaire : ", JLabel.CENTER);
		c.gridx = 0;
		c.gridy = debutInventaire;
		this.add(inventaire, c);
		
		JLabel artefact = new JLabel("Artefacts : ", JLabel.CENTER);
		c.gridx = 0;
		c.gridy = debutInventaire+1;
		this.add(artefact, c);
		
		artEau = new JButton(artEauImg);
		c.gridx = 0;
		c.gridy = debutInventaire+2;
		this.add(artEau, c);
		artEau.addActionListener(ctrl);
		
		artFeu = new JButton(artFeuImg);
		c.gridx = 1;
		c.gridy = debutInventaire+2;
		this.add(artFeu, c);
		artFeu.addActionListener(ctrl);
		
		artAir = new JButton(artAirImg);
		c.gridx = 2;
		c.gridy = debutInventaire+2;
		this.add(artAir, c);
		artAir.addActionListener(ctrl);
		
		artTerre = new JButton(artTerreImg);
		c.gridx = 3;
		c.gridy = debutInventaire+2;
		this.add(artTerre, c);
		artTerre.addActionListener(ctrl);
		
		JLabel cle = new JLabel("Clés : ", JLabel.CENTER);
		c.gridx = 0;
		c.gridy = debutInventaire+3;
		this.add(cle, c);
		
		cleEau = new JButton(cleEauImg);
		c.gridx = 0;
		c.gridy = debutInventaire+4;
		this.add(cleEau, c);
		cleEau.addActionListener(ctrl);
		
		cleFeu = new JButton(cleFeuImg);
		c.gridx = 1;
		c.gridy = debutInventaire+4;
		this.add(cleFeu, c);
		cleFeu.addActionListener(ctrl);
		
		cleAir = new JButton(cleAirImg);
		c.gridx = 2;
		c.gridy = debutInventaire+4;
		this.add(cleAir, c);
		cleAir.addActionListener(ctrl);
		
		cleTerre = new JButton(cleTerreImg);
		c.gridx = 3;
		c.gridy = debutInventaire+4;
		this.add(cleTerre, c);
		cleTerre.addActionListener(ctrl);
		
		//Echange
		int debutEtalageY = 13;
		JLabel echangeLab = new JLabel("Etalage :", JLabel.CENTER);
		c.gridx = 0;
		c.gridy = debutEtalageY;
		this.add(echangeLab, c);
		
		this.caseEtalage =  new ImageIcon(((new ImageIcon("images/caseEtalage.jpg")).getImage()).getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
		int debutEtalageX = 1;
		for(int i = 0; i<etalage.length; i++) {
			for(int j=0; j<etalage[0].length; j++) {
				etalage[i][j] = new JLabel(this.caseEtalage);
				c.gridx = debutEtalageX+j;
				c.gridy = debutEtalageY+1+i;
				this.add(etalage[i][j], c);
			}
		}
		
		this.clearEtalage = new JButton("Clear Etalage");
		c.gridx = 3;
		c.gridy = debutEtalageY+3;
		this.add(this.clearEtalage, c);
		this.clearEtalage.addActionListener(ctrl);
		
		
	}
	
	/**
	 * Change l affichage du joueur actif
	 * @param id Joueur actif
	 * @param actions Nombre d actions restantes
	 */
	public void changeActivePlayer(int id, int actions) {
		idPlayer.setText("Joueur "+id+" ");
		updateActionsPlayer(actions);
	}
	
	/**
	 * Change le nombre d actions restantes
	 * @param actionsRes Nombre d actions restantes
	 */
	public void updateActionsPlayer(int actionsRes) {
		actionsPlayer.setText("Actions restantes: "+actionsRes+" ");
	}
	
	public void changeNbPlayer(int nb) {
		nbPlayers.setText("Nb Joueurs : " + nb+" ");
	}
	
	/**
	 * Donne au JButton un apparence selectionnee/deselectionnee en fonction de estSelect
	 * @param button
	 * @param estSelect le bouton est il deja selectionne ?
	 */
	public void selectionne(JButton button, boolean estSelect) {
		if(estSelect) {
			button.setBackground(this.buttonBackground);
		} else {
			button.setBackground(new Color(131, 166, 151));
		}
	}
	
	/**
	 * Met a jour la vue de l inventaire actif
	 * @param inventaire
	 */
	public void setInventaire(int[][] inventaire) {
		int[] artefacts = inventaire[0];
		int[] cles = inventaire[1];
		if(artefacts[0]==0) {
			this.artEau.setVisible(false);
		} else {
			if(! this.artEau.isVisible()) {
				this.artEau.setVisible(true);
			}
		}
		if(artefacts[1]==0) {
			this.artFeu.setVisible(false);
		} else {
			if(! this.artFeu.isVisible()) {
				this.artFeu.setVisible(true);
			}
		}
		if(artefacts[2]==0) {
			this.artAir.setVisible(false);
		} else {
			if(! this.artAir.isVisible()) {
				this.artAir.setVisible(true);
			}
		}
		if(artefacts[3]==0) {
			this.artTerre.setVisible(false);
		} else {
			if(! this.artTerre.isVisible()) {
				this.artTerre.setVisible(true);
			}
		}
		if(cles[0]==0) {
			this.cleEau.setVisible(false);
		} else {
			if(! this.cleEau.isVisible()) {
				this.cleEau.setVisible(true);
			}
			this.cleEau.setText("x"+cles[0]);
		}
		if(cles[1]==0) {
			this.cleFeu.setVisible(false);
		} else {
			if(! this.cleFeu.isVisible()) {
				this.cleFeu.setVisible(true);
			}
			this.cleFeu.setText("x"+cles[1]);
		}
		if(cles[2]==0) {
			this.cleAir.setVisible(false);
		} else {
			if(! this.cleAir.isVisible()) {
				this.cleAir.setVisible(true);
			}
			this.cleAir.setText("x"+cles[2]);
		}
		if(cles[3]==0) {
			this.cleTerre.setVisible(false);
		} else {
			if(! this.cleTerre.isVisible()) {
				this.cleTerre.setVisible(true);
			}
			this.cleTerre.setText("x"+cles[3]);
		}
	}
	/**
	 * Affiche la plateforme d echange et les objets qu'elle contient
	 * @param plateformeEchange
	 */
	public void setEtalage(int[][] plateformeEchange) {
		for(int i = 0; i<plateformeEchange.length; i++) {
			for(int j = 0; j<plateformeEchange[0].length; j++) {
				if(plateformeEchange[i][j] == 0) {
					this.etalage[j][i].setIcon(this.caseEtalage);
					this.etalage[j][i].setText("");
				} else {
					ImageIcon marchandise = new ImageIcon(((getImageIcon(i==0, j)).getImage()).getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
					this.etalage[j][i].setIcon(marchandise);
					this.etalage[j][i].setText("x"+ plateformeEchange[i][j]);
				}
			}
		}
	}
	
	private void setImages() {
		int scale = 30;
//		ImageIcon imgArtEauTemp = new ImageIcon("images/artefact_eau.png");
//		Image img = imgArtEauTemp.getImage();
//		Image newimg = img.getScaledInstance(30, 30,  java.awt.Image.SCALE_SMOOTH);
//		artEauImg = new ImageIcon(newimg);
		artEauImg = new ImageIcon(((new ImageIcon("images/artefact_eau.png")).getImage()).getScaledInstance(scale, scale, java.awt.Image.SCALE_SMOOTH));
		artFeuImg = new ImageIcon(((new ImageIcon("images/artefact_feu.png")).getImage()).getScaledInstance(scale, scale, java.awt.Image.SCALE_SMOOTH));
		artAirImg = new ImageIcon(((new ImageIcon("images/artefact_air.png")).getImage()).getScaledInstance(scale, scale, java.awt.Image.SCALE_SMOOTH));
		artTerreImg = new ImageIcon(((new ImageIcon("images/artefact_terre.png")).getImage()).getScaledInstance(scale, scale, java.awt.Image.SCALE_SMOOTH));
		cleEauImg = new ImageIcon(((new ImageIcon("images/cle_eau.png")).getImage()).getScaledInstance(scale, scale, java.awt.Image.SCALE_SMOOTH));
		cleFeuImg = new ImageIcon(((new ImageIcon("images/cle_feu.png")).getImage()).getScaledInstance(scale, scale, java.awt.Image.SCALE_SMOOTH));
		cleAirImg = new ImageIcon(((new ImageIcon("images/cle_air.png")).getImage()).getScaledInstance(scale, scale, java.awt.Image.SCALE_SMOOTH));
		cleTerreImg = new ImageIcon(((new ImageIcon("images/cle_terre.png")).getImage()).getScaledInstance(scale, scale, java.awt.Image.SCALE_SMOOTH));
	}
	
	/**
	 * Recupere l'image taille 200 de l artefact ou la cle
	 * @param artefact Est-ce un artefact ?
	 * @param element
	 * @return
	 */
	public ImageIcon getImageIcon(boolean artefact, int element) {
		if(artefact) {
			if(element == 0) {
				return new ImageIcon("images/artefact_eau.png");
			} else if (element == 1) {
				return new ImageIcon("images/artefact_feu.png");
			} else if(element == 2) {
				return new ImageIcon("images/artefact_air.png");
			} else {
				return new ImageIcon("images/artefact_terre.png");
			}
		} else {
			if(element == 0) {
				return new ImageIcon("images/cle_eau.png");
			} else if (element == 1) {
				return new ImageIcon("images/cle_feu.png");
			} else if(element == 2) {
				return new ImageIcon("images/cle_air.png");
			} else {
				return new ImageIcon("images/cle_terre.png");
			}
		}
		
	}
	
	/**
	 * Verifie si le bouton est bien celui d'un inventaire
	 * @param button
	 * @return
	 */
	public boolean estInventaire(JButton button) {
		return (button==artEau || button==artFeu || button==artAir || button==artTerre || button==cleEau || button==cleFeu || button==cleAir || button==cleTerre);
	}
	
	public boolean estArtefact(JButton button) {
		return button==artEau || button==artFeu || button==artAir || button==artTerre;
	}
	
	public int getElement(JButton button) {
		if(button==artEau || button==cleEau) {
			return 0;
		} else if(button==artFeu || button==cleFeu) {
			return 1;
		} else if(button==artAir || button==cleAir) {
			return 2;
		} else if(button==artTerre || button==cleTerre) {
			return 3;
		} else {
			return -1;
		}
	}

}
