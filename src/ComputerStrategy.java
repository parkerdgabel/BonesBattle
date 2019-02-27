public class ComputerStrategy implements Strategy {
    Player player;  // The player playing meekly.


    public void setPlayer (Player whom) { player = whom; }


            // A devotee of the Milquetoast strategy will never attack
            // another player.

    public boolean willAttack (Map board) { return false; }


            // The Strategy interface requires that we create getAttacker()
            // and getDefender() methods.  However, as willAttack() will
            // never return true, these methods will never be called.

    public Territory getAttacker () { return null; }
    public Territory getDefender () { return null; }

}
