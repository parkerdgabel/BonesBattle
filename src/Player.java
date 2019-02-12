//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.awt.Color;

public class Player implements Comparable<Player> {
    private static boolean DEBUG = false;
    public static int playerQty = 0;
    private int id;
    private String name;
    private Strategy strategy = null;
    private Color color;
    private Color clickColor;
    private int wins;

    public Player(String var1, Color var2) {
        this.id = playerQty++;
        this.name = var1;
        this.color = var2;
        this.clickColor = var2.darker();
        this.strategy = new MilquetoastStrategy();
        this.strategy.setPlayer(this);
        this.wins = 0;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Strategy getStrategy() {
        return this.strategy;
    }

    public Color getColor() {
        return this.color;
    }

    public Color getClickColor() {
        return this.clickColor;
    }

    public int getWins() {
        return this.wins;
    }

    public void setName(String var1) {
        this.name = var1;
    }

    public void setStrategy(Strategy var1) {
        this.strategy = var1;
    }

    public void setColor(Color var1) {
        this.color = var1;
    }

    public void setClickColor(Color var1) {
        this.clickColor = var1;
    }

    public void incrementWins() {
        ++this.wins;
    }

    public boolean willAttack(Map var1) {
        return this.strategy.willAttack(var1);
    }

    public Territory getAttacker() {
        return this.strategy.getAttacker();
    }

    public Territory getDefender() {
        return this.strategy.getDefender();
    }

    public int compareTo(Player var1) {
        return this.name.compareTo(var1.getName());
    }

    public static void main(String[] var0) {
        System.out.printf("Nothing to see here; move along...\n");
    }
}
