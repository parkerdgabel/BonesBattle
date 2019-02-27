
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class Bones extends JFrame implements ActionListener {
    public static final boolean DEBUG = false;
    public static final int WINDOW_HEIGHT = 620;
    public static final int WINDOW_WIDTH = 775;
    public static final Font DIRECTIONS_FONT = Font.decode("Arial-PLAIN-16");
    public static final Font QTY_BUTTON_FONT = Font.decode("Arial-BOLD-24");
    public static final Font SUM_BUTTON_FONT = Font.decode("Arial-BOLD-30");
    public static final Font STAT_LABEL_FONT = Font.decode("Monospaced-BOLD-15");
    public static final Font START_BUTTON_FONT = Font.decode("Arial-BOLD-48");
    public static final Color QTY_BUTTONTEXT_COLOR;
    public static final Color SUM_BUTTONTEXT_WIN_COLOR;
    public static final Color SUM_BUTTONTEXT_LOSS_COLOR;
    public static final Color ELIMINATION_COLOR;
    public static final int MAP_ROWS = 5;
    public static final int MAP_COLUMNS = 8;
    public static final int MAX_DICE = 8;
    public static final int ATTACK_TIME = 20;
    Container mainPane;
    Container topPane;
    Container centerPane;
    Container bottomPane;
    Map board;
    JButton[][] chart;
    JLabel[] statusLabel;
    JLabel[] nameLabel;
    JLabel titleLabel;
    JButton startButton;
    JButton nextButton;
    JButton quitButton;
    ArrayList<String> computerNames = null;
    ArrayList<Player> opponent;
    Player currentPlayer;
    Territory attackFrom = null;
    ArrayList<ActionListener> actionListeners;
    int turnCounter = 0;
    int currentPlayerIndex;
    int speedOfPlay = 20;
    int flickerDelay;
    int mapGaps;
    int numPlayers;
    int victory;
    int qtyGames;
    boolean preAttack;
    boolean tournament;
    Timer timer;

    public static void main(String[] var0) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException var2) {
        } catch (ClassNotFoundException var3) {
        } catch (InstantiationException var4) {
        } catch (IllegalAccessException var5) {
        }

        new Bones(var0);
    }

    public Bones(String[] var1) {
        this.flickerDelay = this.speedOfPlay / 5;
        this.mapGaps = 10;
        this.numPlayers = 5;
        this.victory = 16;
        this.qtyGames = 0;
        this.preAttack = true;
        this.tournament = true;
        ArrayList var2 = this.processCmdLineArgs(var1);
        this.opponent = this.createPlayers(var2);
        this.setTitle("Bones Battle");
        this.setSize(775, 620);
        this.setDefaultCloseOperation(3);
        this.mainPane = this.getContentPane();
        this.mainPane.setLayout(new BorderLayout(0, 0));
        this.layoutTopBorder();
        this.layoutSideBorders();
        JTextArea var3 = new JTextArea(this.getDirections(), 20, 80);
        var3.setFont(DIRECTIONS_FONT);
        var3.setEditable(false);
        this.mainPane.add(var3, "Center");
        this.startButton = new JButton("Start!");
        this.startButton.setFont(START_BUTTON_FONT);
        this.startButton.setFocusPainted(false);
        this.startButton.setPreferredSize(new Dimension(755, 75));
        this.startButton.addActionListener(this);
        this.mainPane.add(this.startButton, "Last");
        this.setVisible(true);
    }

    public ArrayList<String> processCmdLineArgs(String[] var1) {
        ArrayList var2 = new ArrayList();
        boolean var3 = false;

        for(int var4 = 0; var4 < var1.length; ++var4) {
            if (var1[var4].equalsIgnoreCase("human")) {
                var2.add("Human");
                this.tournament = false;
            } else if (var1[var4].startsWith("wins=")) {
                this.victory = Integer.parseInt(var1[var4].substring(5));
            } else if (var1[var4].startsWith("speed=")) {
                this.speedOfPlay = Integer.parseInt(var1[var4].substring(6));
                this.flickerDelay = this.speedOfPlay / 5;
                var3 = true;
            } else if (var1[var4].endsWith(".class")) {
                var2.add(var1[var4].substring(0, var1[var4].length() - 6));
            } else {
                var2.add(var1[var4]);
            }
        }

        boolean var10 = false;
        ArrayList var5 = new ArrayList();

        int var6;
        String var7;
        for(var6 = 0; var6 < var2.size(); ++var6) {
            var7 = (String)var2.get(var6);
            if (var7.equals("Human") && !var10) {
                var10 = true;
            } else if (var7.equals("Human") && var10) {
                var5.add(var6);
            }
        }

        for(var6 = var5.size() - 1; var6 >= 0; --var6) {
            var2.remove((Integer)var5.get(var6));
        }

        Iterator var11 = var2.iterator();

        while(var11.hasNext()) {
            var7 = (String)var11.next();
            if (var7.equals("Human") && !var3) {
                this.speedOfPlay = 2000;
                this.flickerDelay = this.speedOfPlay / 5;
                break;
            }
        }

        if (var2.size() == 0) {
            var2.add("Human");
            this.tournament = false;
            if (!var3) {
                this.speedOfPlay = 2000;
                this.flickerDelay = this.speedOfPlay / 5;
            }

            String[] var15 = new String[]{"Altos", "Amstrad", "Bondwell", "Cromemco", "Heathkit", "Kaypro", "Osborne", "Sinclair", "Tandy"};
            this.computerNames = new ArrayList(var15.length);

            for(int var14 = 0; var14 < var15.length; ++var14) {
                this.computerNames.add(var15[var14]);
            }

            Collections.shuffle(this.computerNames);
            var2.add(this.computerNames.get(0));
            var2.add(this.computerNames.get(1));
            var2.add(this.computerNames.get(2));
            var2.add(this.computerNames.get(3));
            return var2;
        } else if (var2.size() >= 2 && var2.size() <= 5) {
            boolean var12 = true;
            Iterator var13 = var2.iterator();

            while(true) {
                String var8;
                File var9;
                do {
                    do {
                        if (!var13.hasNext()) {
                            if (!var12) {
                                System.exit(1);
                            }

                            return var2;
                        }

                        var8 = (String)var13.next();
                    } while(var8.equals("Human"));

                    var9 = new File("./" + var8 + ".class");
                } while(var9.exists() && var9.canRead());

                var12 = false;
                System.out.println("ERROR: Strategy " + var8 + ".class either doesn't exist or can't be read.");
            }
        } else {
            System.out.println("\nERROR:  Argument list is invalid.\n");
            System.out.println("Arguments are to be names of strategy .class files.\nInclude 'human' if you wish to play against the computer players.\nList 2, 3, 4, or 5 names.  No arguments defaults\n human playing against 4 computer players.\n\nTournament Mode: List only .class file names.  First to 10\n wins is the winner.  Use arg. of 'wins=#' to change.\n");
            System.exit(1);
            return null;
        }
    }

    public void actionPerformed(ActionEvent var1) {
        String var2 = var1.getActionCommand();
        if (var1.getSource() == this.quitButton) {
            System.exit(0);
        } else if (var1.getSource() == this.startButton) {
            this.board = new Map(this.opponent, 5, 8, this.mapGaps, 8);
            this.mainPane.removeAll();
            this.mainPane.setLayout(new BorderLayout(50, 0));
            this.layoutTopBorder();
            this.layoutSideBorders();
            this.displayMap();
            this.layoutBottomBorder();
            this.mainPane.validate();
            this.currentPlayerIndex = this.turnCounter % this.opponent.size();
            this.currentPlayer = (Player)this.opponent.get(this.currentPlayerIndex);
            this.statusLabel[this.currentPlayerIndex].setBorder(new LineBorder(this.currentPlayer.getColor(), 2));
            if (this.timer == null) {
                this.timer = new Timer(this.speedOfPlay, this);
                this.timer.setInitialDelay(this.flickerDelay);
            }

            this.timer.start();
        } else if (var1.getSource() == this.nextButton) {
            this.nextButton.setEnabled(false);
            this.awardDice(this.currentPlayer);
            this.statusLabel[this.currentPlayerIndex].setBorder((Border)null);

            do {
                ++this.turnCounter;
                this.currentPlayerIndex = this.turnCounter % this.opponent.size();
                this.currentPlayer = (Player)this.opponent.get(this.currentPlayerIndex);
            } while(this.board.countTerritories(this.currentPlayer) < 1);

            this.statusLabel[this.currentPlayerIndex].setBorder(new LineBorder(this.currentPlayer.getColor(), 2));
            this.timer.start();
        } else if (var1.getSource() == this.timer && !this.currentPlayer.getName().equals("Human")) {
            this.computersTurn();
        } else if (var1.getSource() == this.timer && this.currentPlayer.getName().equals("Human")) {
            this.timer.stop();
            this.nextButton.setEnabled(true);
        } else if (this.currentPlayer.getName().equals("Human")) {
            Territory var3 = null;
            int var4 = -1;
            int var5 = -1;

            for(int var6 = 0; var6 < this.board.ROWS; ++var6) {
                for(int var7 = 0; var7 < this.board.COLUMNS; ++var7) {
                    if (var1.getSource() == this.chart[var6][var7]) {
                        var3 = this.board.getTerritory(var6, var7);
                        var4 = var6;
                        var5 = var7;
                    }
                }
            }

            if (this.preAttack) {
                if (var3.getOwner() == this.currentPlayer && var3.getDice() >= 2 && this.board.getEnemyNeighbors(var3).size() != 0) {
                    this.attackFrom = var3;
                    this.preAttack = false;
                    this.chart[var4][var5].setBackground(this.currentPlayer.getClickColor());
                    this.chart[var4][var5].update(this.chart[var4][var5].getGraphics());
                    return;
                }

                return;
            }

            if (var3 == this.attackFrom) {
                this.attackFrom = null;
                this.preAttack = true;
                this.chart[var4][var5].setBackground(this.currentPlayer.getColor());
                this.chart[var4][var5].update(this.chart[var4][var5].getGraphics());
                return;
            }

            if (!this.board.getEnemyNeighbors(this.attackFrom).contains(var3)) {
                return;
            }

            Territory var8 = this.attackFrom;
            this.chart[var4][var5].setBackground(var3.getOwner().getClickColor());
            this.chart[var4][var5].update(this.chart[var4][var5].getGraphics());
            this.processAttack(var8, var3);
            this.attackFrom = null;
            this.preAttack = true;
            this.updateStatLabels();
            if (this.board.countTerritories(this.currentPlayer) == this.board.OCCUPIED) {
                if (this.currentPlayer.getName().equals("Human")) {
                    this.titleLabel.setText("YOU WON!");
                } else {
                    this.titleLabel.setText(this.currentPlayer.getName() + " won");
                }

                this.nextButton.setEnabled(false);
            }
        }

    }

    private void computersTurn() {
        if (this.currentPlayer.willAttack(this.board)) {
            Territory var1 = this.currentPlayer.getAttacker();
            Territory var2 = this.currentPlayer.getDefender();
            int var3 = var1.getRow();
            int var4 = var1.getCol();
            int var5 = var2.getRow();
            int var6 = var2.getCol();
            this.chart[var3][var4].setBackground(var1.getOwner().getClickColor());
            this.chart[var3][var4].update(this.chart[var3][var4].getGraphics());

            try {
                Thread.sleep((long)this.flickerDelay);
            } catch (Exception var9) {
            }

            this.chart[var5][var6].setBackground(var2.getOwner().getClickColor());
            this.chart[var5][var6].update(this.chart[var5][var6].getGraphics());

            try {
                Thread.sleep((long)this.flickerDelay);
            } catch (Exception var8) {
            }

            this.processAttack(var1, var2);
            this.updateStatLabels();
        } else if (this.board.countTerritories(this.currentPlayer) == this.board.OCCUPIED) {
            this.titleLabel.setText(this.currentPlayer.getName() + " WINS!");
            ++this.qtyGames;
            this.timer.stop();
            if (this.tournament) {
                this.currentPlayer.incrementWins();
                Iterator var10;
                Player var11;
                if (this.currentPlayer.getWins() == this.victory) {
                    System.out.println("\n\tThe Tournament has a victor: " + this.currentPlayer.getName() + "!");
                    System.out.println("\nFinal Standings after " + this.qtyGames + " games:");
                    Collections.sort(this.opponent);
                    var10 = this.opponent.iterator();

                    while(var10.hasNext()) {
                        var11 = (Player)var10.next();
                        System.out.println(var11.getWins() + " wins for " + var11.getName());
                    }
                } else {
                    System.out.println("Game " + this.qtyGames + " won by " + this.currentPlayer.getName() + ".");
                    System.out.println("\tStandings: ");
                    Collections.sort(this.opponent);
                    var10 = this.opponent.iterator();

                    while(var10.hasNext()) {
                        var11 = (Player)var10.next();
                        System.out.print(var11.getName() + ": " + var11.getWins() + "  ");
                    }

                    System.out.println();
                    Collections.shuffle(this.opponent);
                    this.startButton.doClick();
                }
            }
        } else {
            this.awardDice(this.currentPlayer);
            this.statusLabel[this.currentPlayerIndex].setBorder((Border)null);

            do {
                ++this.turnCounter;
                this.currentPlayerIndex = this.turnCounter % this.opponent.size();
                this.currentPlayer = (Player)this.opponent.get(this.currentPlayerIndex);
            } while(this.board.countTerritories(this.currentPlayer) < 1);

            this.statusLabel[this.currentPlayerIndex].setBorder(new LineBorder(this.currentPlayer.getColor(), 2));
        }

    }

    private void processAttack(Territory var1, Territory var2) {
        int var3 = var1.getRow();
        int var4 = var1.getCol();
        int var5 = var2.getRow();
        int var6 = var2.getCol();
        int var7 = 0;
        int var8 = 0;

        int var9;
        for(var9 = 0; var9 < var1.getDice(); ++var9) {
            var7 += (int)(Math.random() * 6.0D + 1.0D);
        }

        for(var9 = 0; var9 < var2.getDice(); ++var9) {
            var8 += (int)(Math.random() * 6.0D + 1.0D);
        }

        this.chart[var3][var4].setText(Integer.toString(var7));
        this.chart[var5][var6].setText(Integer.toString(var8));
        this.chart[var3][var4].setFont(SUM_BUTTON_FONT);
        this.chart[var5][var6].setFont(SUM_BUTTON_FONT);
        if (var7 > var8) {
            this.chart[var3][var4].setForeground(SUM_BUTTONTEXT_WIN_COLOR);
            this.chart[var5][var6].setForeground(SUM_BUTTONTEXT_WIN_COLOR);
            var2.setOwner(var1.getOwner());
            var2.setDice(var1.getDice() - 1);
            var1.setDice(1);
        } else {
            this.chart[var3][var4].setForeground(SUM_BUTTONTEXT_LOSS_COLOR);
            this.chart[var5][var6].setForeground(SUM_BUTTONTEXT_LOSS_COLOR);
            var1.setDice(1);
        }

        this.chart[var3][var4].update(this.chart[var3][var4].getGraphics());
        this.chart[var5][var6].update(this.chart[var5][var6].getGraphics());

        try {
            Thread.sleep((long)(2 * this.flickerDelay));
        } catch (Exception var10) {
        }

        this.chart[var3][var4].setText(Integer.toString(var1.getDice()));
        this.chart[var5][var6].setText(Integer.toString(var2.getDice()));
        this.chart[var3][var4].setBackground(var1.getOwner().getColor());
        this.chart[var5][var6].setBackground(var2.getOwner().getColor());
        this.chart[var3][var4].setForeground(QTY_BUTTONTEXT_COLOR);
        this.chart[var5][var6].setForeground(QTY_BUTTONTEXT_COLOR);
        this.chart[var3][var4].setFont(QTY_BUTTON_FONT);
        this.chart[var5][var6].setFont(QTY_BUTTON_FONT);
    }

    private ArrayList<Player> createPlayers(ArrayList<String> var1) {
        ArrayList var2 = new ArrayList();
        ArrayList var3 = new ArrayList();
        var3.add(Color.blue);
        var3.add(Color.RED.darker());
        var3.add(new Color(238, 118, 0));
        var3.add(new Color(178, 58, 238));
        var3.add(new Color(139, 90, 43));
        int var4 = 0;
        int var5 = 0;
        Iterator var6 = var1.iterator();

        while(var6.hasNext()) {
            String var7 = (String)var6.next();
            if (var7.equals("Human")) {
                Player var8 = new Player(var7, Color.GREEN.darker().darker());
                var8.setStrategy((Strategy)null);
                var2.add(var5, var8);
                ++var5;
            } else {
                var2.add(var5, new Player(var7, (Color)var3.get(var4)));
                StrategyLoader var11 = new StrategyLoader();

                try {
                    if (this.computerNames != null) {
                        ((Player)var2.get(var5)).setStrategy(new ComputerStrategy());
                        ((Player)var2.get(var5)).getStrategy().setPlayer((Player)var2.get(var5));
                    } else {
                        Object var9 = var11.loadClass(var7).newInstance();
                        ((Player)var2.get(var5)).setStrategy((Strategy)var9);
                        ((Player)var2.get(var5)).getStrategy().setPlayer((Player)var2.get(var5));
                    }
                } catch (Exception var10) {
                    System.out.println("ERROR:  Strategy for " + var7 + " not loaded due to " + var10);
                    System.exit(1);
                }

                ++var5;
                ++var4;
            }
        }

        Collections.shuffle(var2);
        if (var2.size() == 4) {
            this.mapGaps = 8;
        }

        return var2;
    }

    private void layoutTopBorder() {
        this.topPane = new Container();
        this.mainPane.add(this.topPane, "First");
        this.topPane.setLayout(new FlowLayout());
        JLabel var1 = new JLabel("");
        var1.setPreferredSize(new Dimension(0, 75));
        this.topPane.add(var1, "First");
        JLabel[] var2 = new JLabel[6];

        int var3;
        for(var3 = 0; var3 < 6; ++var3) {
            var2[var3] = new JLabel(new ImageIcon("images/face" + (var3 + 1) + "s.png"));
            this.topPane.add(var2[var3], "First");
        }

        this.titleLabel = new JLabel(" Bones Battle ");
        this.titleLabel.setFont(Font.decode("Serif-36"));
        this.titleLabel.setHorizontalAlignment(0);
        this.topPane.add(this.titleLabel, "First");

        for(var3 = 5; var3 >= 0; --var3) {
            var2[var3] = new JLabel(new ImageIcon("images/face" + (var3 + 1) + "s.png"));
            this.topPane.add(var2[var3], "First");
        }

    }

    private void layoutSideBorders() {
        JLabel var1 = new JLabel("");
        JLabel var2 = new JLabel("");
        this.mainPane.add(var1, "Before");
        this.mainPane.add(var2, "After");
    }

    private void layoutBottomBorder() {
        Container var1 = new Container();
        this.mainPane.add(var1, "Last");
        var1.setLayout(new BorderLayout());
        Container var2 = new Container();
        var1.add(var2, "First");
        var2.setLayout(new FlowLayout());
        Container var3 = new Container();
        var1.add(var3, "Last");
        var3.setLayout(new FlowLayout());
        this.statusLabel = new JLabel[this.opponent.size()];

        int var4;
        String var5;
        for(var4 = 0; var4 < this.opponent.size(); ++var4) {
            var5 = this.buildStatusString(var4);
            if (((Player)this.opponent.get(var4)).getName().equals("Human")) {
                this.statusLabel[var4] = new JLabel(" ♥♥ " + var5 + " ");
            } else {
                this.statusLabel[var4] = new JLabel(" ██ " + var5 + " ");
            }

            this.statusLabel[var4].setFont(STAT_LABEL_FONT);
            this.statusLabel[var4].setForeground(((Player)this.opponent.get(var4)).getColor());
            var2.add(this.statusLabel[var4]);
        }

        this.quitButton = new JButton("Quit");
        this.quitButton.addActionListener(this);
        var3.add(this.quitButton);
        var3.add(new JLabel("  "));
        this.nameLabel = new JLabel[this.opponent.size()];

        for(var4 = 0; var4 < this.opponent.size(); ++var4) {
            var5 = ((Player)this.opponent.get(var4)).getName();
            if (var5.length() > 8) {
                var5 = var5.substring(0, 8);
            }

            while(8 - var5.length() >= 2) {
                var5 = " " + var5 + " ";
            }

            if (var5.length() == 7) {
                var5 = " " + var5;
            }

            this.nameLabel[var4] = new JLabel("   " + var5 + " ");
            this.nameLabel[var4].setFont(STAT_LABEL_FONT);
            this.nameLabel[var4].setForeground(((Player)this.opponent.get(var4)).getColor());
            var3.add(this.nameLabel[var4]);
        }

        var3.add(new JLabel("  "));
        this.nextButton = new JButton("Next");
        this.nextButton.addActionListener(this);
        this.nextButton.setEnabled(false);
        var3.add(this.nextButton);
    }

    private String buildStatusString(int var1) {
        String var3 = Integer.toString(this.board.countTerritories((Player)this.opponent.get(var1)));
        String var2;
        if (var3.length() < 2) {
            var2 = " " + var3 + " (";
        } else {
            var2 = var3 + " (";
        }

        String var4 = Integer.toString(this.board.countConnected((Player)this.opponent.get(var1)));
        if (var4.length() < 2) {
            var2 = var2 + " " + var4 + ")";
        } else {
            var2 = var2 + var4 + ")";
        }

        return var2;
    }

    private void displayMap() {
        this.centerPane = new Container();
        this.mainPane.add(this.centerPane, "Center");
        this.centerPane.setLayout(new GridLayout(this.board.ROWS, this.board.COLUMNS));
        this.chart = new JButton[this.board.ROWS][this.board.COLUMNS];

        for(int var1 = 0; var1 < this.board.ROWS; ++var1) {
            for(int var2 = 0; var2 < this.board.COLUMNS; ++var2) {
                Territory var3 = this.board.getTerritory(var1, var2);
                this.chart[var1][var2] = new JButton(Integer.toString(var3.getDice()));
                this.chart[var1][var2].setPreferredSize(new Dimension(80, 80));
                if (var3.getOwner() == null) {
                    this.chart[var1][var2].setText("");
                    this.chart[var1][var2].setContentAreaFilled(false);
                    this.chart[var1][var2].setBorderPainted(false);
                    this.chart[var1][var2].setRolloverEnabled(false);
                } else {
                    this.chart[var1][var2].addActionListener(this);
                    this.chart[var1][var2].setFont(QTY_BUTTON_FONT);
                    this.chart[var1][var2].setFocusPainted(false);
                    this.chart[var1][var2].setBackground(var3.getOwner().getColor());
                    this.chart[var1][var2].setForeground(QTY_BUTTONTEXT_COLOR);
                }

                this.centerPane.add(this.chart[var1][var2]);
            }
        }

    }

    public void updateStatLabels() {
        for(int var1 = 0; var1 < this.opponent.size(); ++var1) {
            String var2 = this.buildStatusString(var1);
            if (((Player)this.opponent.get(var1)).getName().equals("Human")) {
                this.statusLabel[var1].setText(" ♥♥ " + var2 + " ");
            } else {
                this.statusLabel[var1].setText(" ██ " + var2 + " ");
            }

            if (this.board.countTerritories((Player)this.opponent.get(var1)) == 0) {
                this.statusLabel[var1].setForeground(ELIMINATION_COLOR);
            }
        }

    }

    private void awardDice(Player var1) {
        int var2 = this.board.countConnected(var1);
        int var3 = this.board.MAXDICE * this.board.countTerritories(var1) - this.board.countDice(var1);
        ArrayList var4;
        if (var2 >= var3) {
            var4 = this.board.getPropertyOf(var1);
            Iterator var5 = var4.iterator();

            while(var5.hasNext()) {
                Territory var6 = (Territory)var5.next();
                var6.setDice(this.board.MAXDICE);
                this.chart[var6.getRow()][var6.getCol()].setText(Integer.toString(var6.getDice()));
            }
        } else {
            var4 = this.board.getPropertyOf(var1);
            int var9 = var2;
            if (var2 > var3) {
                var9 = var3;
            }

            for(int var10 = 0; var10 < var9; ++var10) {
                Territory var7;
                do {
                    int var8 = (int)(Math.random() * (double)var4.size());
                    var7 = (Territory)var4.get(var8);
                } while(var7.getDice() >= this.board.MAXDICE);

                var7.setDice(var7.getDice() + 1);
                this.chart[var7.getRow()][var7.getCol()].setText(Integer.toString(var7.getDice()));
            }
        }

    }

    private String getDirections() {
        return "\n  In this RISK-like dice game, you control the green territories while the computer plays the\n  remaining territories.  Each territory is labeled with a quantity of six-sided dice, ranging\n  from 1 through 8.  Initially, each player occupies six randomly selected territories\n  and is given 18 dice, one per territory plus a dozen scattered randomly.  A turn consists of\n  0 or more attacks, each launched from a territory  with 2 or more dice toward an adjacent\n  enemy territory.  To launch an attack, click on a suitable green territory, then on an\n  adjacent enemy territory.  The computer will (fairly!) roll and sum your territory's dice and,\n  separately, the enemy territory's dice.  If the total of your dice exceeds that of the enemy,\n  you win the enemy's territory, and all but one of your territory's dice are moved to it.  If not,\n  you retain the attacking territory but just one die.  When you are done attacking, click `Next'.\n  A quantity of dice equal to the number of territories in your largest territory cluster will be\n  randomly added to your territories, and play continues.  The winner is the player who\n  captures allof the territories.  You can see each player's territory and connected cluster\n  totals at the bottom of the game window.\n\n  When you are ready to play, click 'Start!', below.\n\n  Written by Lester I. McCann as a framework for CSc 345 assignments at the University of\n  Arizona.  Based on Dice Wars by Taro Ito (http://www.gamedesign.jp/).";
    }

    static {
        QTY_BUTTONTEXT_COLOR = Color.YELLOW;
        SUM_BUTTONTEXT_WIN_COLOR = Color.WHITE;
        SUM_BUTTONTEXT_LOSS_COLOR = Color.MAGENTA;
        ELIMINATION_COLOR = Color.GRAY;
    }
}