/**
 * Represents the Basic territory of the game.
 */
public class Territory {

    private Map map;
    private int dice;
    private int idNum;
    private Player owner;

    /** Constructor that accepts only a map reference.
     * @param map       The map for the game.
     */
    public Territory(Map map) {
        this.map = map;
        this.dice = -1;
        this.owner = null;
    }

    /** Constructor that accepts all field arguments
     * @param map       the map for the game
     * @param owner     the owner of the territory
     * @param dice      the number of dice on the territory
     * @param idNum     the idNum number of the territory
     */
    public Territory(Map map, Player owner, int dice, int idNum) {
        this.map = map;
        this.owner = owner;
        this.dice = dice;
        this.idNum = idNum;
    }

    /** Getter for owner
     * @return      owner of the territory
     */
    public Player getOwner() {
        return owner;
    }

    /** Setter for owner
     * @param owner        the owner to set the field too.
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    /** Getter for idNum
     * @return      the idNum number for the territory
     */
    public int getIdNum() {
        return idNum;
    }

    /** Setter for idNum
     * @param idNum    the idNum to set the idNum to
     */
    public void setIdNum(int idNum) {
        this.idNum = idNum;
    }

    /** Getter for dice
     * @return      the number of dice on the territory
     */
    public int getDice() {
        return dice;
    }

    /** Setter for dice
     * @param dice      the number to set dice too.
     */
    public void setDice(int dice) {
        this.dice = dice;
    }

    /** Getter for the map
     * @return      the map reference
     */
    public Map getMap() {
        return map;
    }

    /** Setter for map
     * @param map
     */
    public void setMap(Map map) {
        this.map = map;
    }

    /** Returns the row the territory is on
     * @return      The row of the territory
     */
    public int getRow() {
        return Math.floorDiv(this.idNum, map.getROWS());
    }

    /** Returns the column of the territory
     * @return      The column of the territory
     */
    public int getCol() {
        return this.idNum % map.getCOLUMNS();
    }
}
