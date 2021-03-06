package modele;

public class SinglePlayer {

	private int id;
	private Coord coord;
	
	private Role role;
	
	private int totalAction;
	
	/**
	 * Inventaire :
	 * </br>Ligne 0 : Artefacts
	 * </br>Ligne 1 : Cles
	 * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
	 */
	private int[][] inventaire = new int[2][4];
	
	public SinglePlayer(int id, Coord c) {
		this.id = id;
		this.coord = c;
		this.totalAction = 3;
		this.role = new Citoyen();
		
		for(int i=0; i<inventaire.length; i++) {
			for(int j=0; j<inventaire[0].length; j++) {
				inventaire[i][j] = 0;
			}
		}
	}

	public Coord getCoord() {
		return this.coord;
	}
	
	
	public int getId() {
		return this.id;
	}
	
	public int getTotalAction() {
		return this.totalAction;
	}
	
	public Role getRole() {
		return this.role;
	}
	
	protected void setRole(Role role) {
		this.role = role;
	}
	
	/**
	 * Recupere l ensemble des cles de l inventaire du player
	 * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
	 * @return
	 */
	public int[] getCles() {
		return inventaire[1];
	}
	
	/**
	 * Recupere l ensemble des artefacts de l inventaire du player
	 * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
	 * @return
	 */
	public int[] getArtefacts() {
		return inventaire[0];
	}
	
	/**
	 * Recupere l ensemble de l inventaire du player
	 * </br>Ligne 0 : Cles
	 * </br>Ligne 1 : Artefacts
	 * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
	 * @return
	 */
	public int[][] getInventaire(){
		return this.inventaire;
	}
	
	/**
	 * Ajoute une cle a l inventaire du player
	 * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
	 * @param cle element de la cle
	 */
	public boolean addCle(int cle) {
		if(cle<0 || cle >=4) {
			return false;
		}
		inventaire[1][cle]++;
		return true;
	}
	
	/**
	 * Enl�ve une cle a l inventaire du player
	 * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
	 * @param cle element de la cle
	 */
	public boolean removeCle(int cle) {
		if((cle<0 || cle >=4) && inventaire[1][cle]<=0) {
			return false;
		}
		inventaire[1][cle]--;
		return true;
	}
	
	/**
	 * Ajoute un artefact a l inventaire du player
	 * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
	 * @param art element de l artefact
	 * @return
	 */
	public boolean addArtefact(int art) {
		if(art<0 || art >=4) {
			return false;
		}
		if(inventaire[0][art]<1) {
			inventaire[0][art]++;
			return true;
		} else {
			System.out.println("Artefact de type "+art+" de trop !");
			return false;
		}
	}
	
	/**
	 * Enleve un artefact a l inventaire du player
	 * </br>Ordre : 0: Eau, 1: Feu, 2: Air, 3: Terre
	 * @param art element de l artefact
	 * @return
	 */
	public boolean removeArtefact(int art) {
		if((art<0 || art >=4) && inventaire[0][art]<=0) {
			return false;
		}
		if(inventaire[0][art]<2) {
			inventaire[0][art]--;
			return true;
		} else {
			System.out.println("Artefact de type "+art+" de trop !");
			return false;
		}
	}
	
	/**
	 * Modifie la coord du player
	 * @param c
	 */
	public void move(Coord c) {
		this.coord = c;
	}
	
	/**
	 * Retourne le role du player + son id(+1)
	 */
	public String toString() {
		return this.role+" "+(this.id+1);
	}
	
	
}
