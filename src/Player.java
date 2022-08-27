import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Image;

public class Player extends JPanel {

    // player attributes
    private String nickname;
    private int score = 0;
    private int move = 0;

    /**
     * RK = rock key
     * PK = paper key
     * SK = scissors key
     */

    public JLabel playerMove;

    private ImageIcon rockIcon = new ImageIcon(
            new ImageIcon("src/icons/rock.png").getImage()
                    .getScaledInstance(40, 40, Image.SCALE_SMOOTH));

    private ImageIcon paperIcon = new ImageIcon(
            new ImageIcon("src/icons/paper.png").getImage()
                    .getScaledInstance(40, 40, Image.SCALE_SMOOTH));

    private ImageIcon scissorIcon = new ImageIcon(
            new ImageIcon("src/icons/scissor.png").getImage()
                    .getScaledInstance(40, 40, Image.SCALE_SMOOTH));

    private ImageIcon noMoveIcon = new ImageIcon("src/icons/noMove.png");

    public Player(String nickname, char cRK, char cPK, char cSK) {

        setNickname(nickname);

        this.setPreferredSize(new Dimension(270, 350));
        this.setLayout(null);
        this.setBorder(BorderFactory.createLineBorder(Color.black, 3));

        JLabel playerNickname = new JLabel(nickname, JLabel.CENTER);
        playerNickname.setBounds(0, 10, 270, 40);
        playerNickname.setFont(new Font("Arial", Font.BOLD, 25));

        playerMove = new JLabel("", JLabel.CENTER);
        playerMove.setBounds(60, 60, 150, 150);
        playerMove.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        playerMove.setFont(new Font("Arial", Font.BOLD, 100));
        // playerMove.setBackground(new Color(255, 0, 0));
        playerMove.setOpaque(true);
        playerMove.setIcon(noMoveIcon);

        JLabel rock = new JLabel("", JLabel.CENTER);
        rock.setBounds(20, 260, 50, 50);
        rock.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        rock.setFont(new Font("Arial", Font.BOLD, 25));
        rock.setIcon(rockIcon);

        JLabel rockKey = new JLabel("" + cRK, JLabel.CENTER);
        rockKey.setBounds(20, 305, 50, 50);
        rockKey.setFont(new Font("Arial", Font.PLAIN, 20));

        JLabel paper = new JLabel("", JLabel.CENTER);
        paper.setBounds(110, 260, 50, 50);
        paper.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        paper.setFont(new Font("Arial", Font.BOLD, 25));
        paper.setIcon(paperIcon);

        JLabel paperKey = new JLabel("" + cPK, JLabel.CENTER);
        paperKey.setBounds(110, 305, 50, 50);
        paperKey.setFont(new Font("Arial", Font.PLAIN, 20));

        JLabel scissors = new JLabel("", JLabel.CENTER);
        scissors.setBounds(200, 260, 50, 50);
        scissors.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        scissors.setFont(new Font("Arial", Font.BOLD, 25));
        scissors.setIcon(scissorIcon);

        JLabel scissorsKey = new JLabel("" + cSK, JLabel.CENTER);
        scissorsKey.setBounds(200, 305, 50, 50);
        scissorsKey.setFont(new Font("Arial", Font.PLAIN, 20));

        this.add(playerNickname);
        this.add(playerMove);
        this.add(rock);
        this.add(paper);
        this.add(scissors);
        this.add(rockKey);
        this.add(paperKey);
        this.add(scissorsKey);

    }

    // setters
    public void setNickname(String nickname) {

        this.nickname = nickname;

    }

    public void setScore(int score) {

        this.score = score;

    }

    public void setMove(int move) {

        this.move = move;

    }

    // getters

    public String getNickname() {

        return this.nickname;
    }

    public int getScore() {

        return this.score;
    }

    public int getMove() {

        return this.move;
    }

}
