import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameFrame extends JFrame implements KeyListener {

    /**
     * 
     * final int NOT_SET = 0; -> DEFAULT MOVE VALUE OF PLAYERS
     * Rock, Paper, and Scissor is already self explanatory
     * 
     * 
     * P1 = player 1
     * RK = rock key
     * PK = paper key
     * SK = scissors key
     * 
     * 
     */

    private final int NOT_SET = 0;
    private final int ROCK = 1;
    private final int PAPER = 2;
    private final int SCISSORS = 3;

    private Player player1;
    private Player player2;
    private JLabel timer;
    private JLabel player1Score;
    private JLabel player2Score;
    private Thread match;
    private JButton nextRound;
    private JPanel resultPanel;
    private JLabel resultText;

    public GameFrame(String P1NN, String P2NN) {

        JPanel mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(800, 500));
        mainPanel.setLayout(null);

        // username limit is 13 chars

        player1 = new Player(P1NN, 'Q', 'W', 'E');
        player1.setBounds(10, 115, 270, 350);

        player2 = new Player(P2NN, '1', '2', '3');
        player2.setBounds(520, 115, 270, 350);

        player1Score = new JLabel(String.valueOf(player1.getScore()), JLabel.CENTER);
        player1Score.setBounds(320, 390, 80, 39);
        player1Score.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        player1Score.setFont(new Font("Arial", Font.BOLD, 20));
        player1Score.setBackground(new Color(255, 255, 0));
        player1Score.setOpaque(true);

        player2Score = new JLabel(String.valueOf(player2.getScore()), JLabel.CENTER);
        player2Score.setBounds(400, 390, 80, 39);
        player2Score.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        player2Score.setFont(new Font("Arial", Font.BOLD, 20));
        player2Score.setBackground(new Color(255, 255, 0));
        player2Score.setOpaque(true);

        JLabel scoreboard = new JLabel("score", JLabel.CENTER);
        scoreboard.setBounds(360, 435, 80, 30);
        scoreboard.setFont(new Font("Consolas", Font.BOLD, 25));

        JLabel result = new JLabel("RESULT", JLabel.CENTER);
        result.setBounds(350, 130, 100, 30);
        result.setFont(new Font("Consolas", Font.BOLD, 25));

        resultPanel = new JPanel();
        result.setLayout(new GridBagLayout());
        resultPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        resultPanel.setBounds(325, 170, 150, 150);

        resultText = new JLabel("", JLabel.CENTER);

        resultPanel.add(resultText);

        JLabel player1Tag = new JLabel("Player 1", JLabel.CENTER);
        player1Tag.setBounds(85, 80, 120, 30);
        player1Tag.setFont(new Font("Consolas", Font.BOLD, 25));

        JLabel player2Tag = new JLabel("Player 2", JLabel.CENTER);
        player2Tag.setBounds(600, 80, 120, 30);
        player2Tag.setFont(new Font("Consolas", Font.BOLD, 25));

        timer = new JLabel("3", JLabel.CENTER);
        timer.setBounds(375, 20, 50, 50);
        timer.setFont(new Font("Calibri", Font.BOLD, 40));

        nextRound = new JButton("Next Round");
        nextRound.setBounds(20, 20, 150, 40);
        nextRound.setVisible(false);
        nextRound.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                synchronized (match) {

                    match.notifyAll();

                }

                nextRound.setVisible(false);
                resultText.setIcon(null);

                resetPlayerMove(player1);
                resetPlayerMove(player2);

                focusFrame();

            }

        });

        this.add(mainPanel);
        mainPanel.add(player1);
        mainPanel.add(player2);
        mainPanel.add(player1Score);
        mainPanel.add(player2Score);
        mainPanel.add(scoreboard);
        mainPanel.add(result);
        mainPanel.add(resultPanel);
        mainPanel.add(player1Tag);
        mainPanel.add(player2Tag);
        mainPanel.add(timer);
        mainPanel.add(nextRound);

        this.addKeyListener(this);
        this.setTitle("Jack 'N Poy");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    private void focusFrame() {

        this.requestFocus();
    }

    private void startRound() {

        if (player1.getMove() != 0 && player2.getMove() != 0) {

            match = new Thread(new Runnable() {

                @Override
                public void run() {

                    try {

                        Thread.sleep(1000);
                        timer.setText("3");

                        countDownSound();

                        Thread.sleep(1000);
                        timer.setText("2");
                        Thread.sleep(1000);
                        timer.setText("1");
                        Thread.sleep(1000);
                        timer.setText("0");

                        int winner = checkWinner(player1, player2);
                        updateScoreboard(player1, player2);

                        displayMove(player1);
                        displayMove(player2);

                        showWinner(winner);

                        nextRound.setVisible(true);

                        waitThread();

                        player1.setMove(0);
                        player2.setMove(0);

                        timer.setText("3");

                    } catch (InterruptedException e) {

                        e.printStackTrace();
                    }

                }

            });

            match.start();

        }

    };

    private void waitThread() throws InterruptedException {

        synchronized (match) {

            match.wait();

        }
    }

    private void playerMoved(Player player, int move) {

        if (player.getMove() == 0) {

            playerMovedSound();

            player.setMove(move);

            ImageIcon playerMoved = new ImageIcon("src/icons/playerMoved.png");
            player.playerMove.setIcon(playerMoved);

            startRound();

        }

    }

    private int checkWinner(Player player1, Player player2) {

        if (player1.getMove() == 1 && player2.getMove() == 2) {

            player2.setScore(player2.getScore() + 1);
            return 2;

        } else if (player1.getMove() == 1 && player2.getMove() == 3) {

            player1.setScore(player1.getScore() + 1);
            return 1;

        } else if (player1.getMove() == 2 && player2.getMove() == 1) {

            player1.setScore(player1.getScore() + 1);
            return 1;

        } else if (player1.getMove() == 2 && player2.getMove() == 3) {

            player2.setScore(player2.getScore() + 1);
            return 2;

        } else if (player1.getMove() == 3 && player2.getMove() == 1) {

            player2.setScore(player2.getScore() + 1);
            return 2;

        } else if (player1.getMove() == 3 && player2.getMove() == 2) {

            player1.setScore(player1.getScore() + 1);
            return 1;

        } else {

            return 3;
        }

    }

    private void updateScoreboard(Player player1, Player player2) {

        if (player1.getScore() > player2.getScore()) {

            player1Score.setBackground(new Color(0, 250, 0));

            player1Score.setText(String.valueOf(player1.getScore()));
            player2Score.setText(String.valueOf(player2.getScore()));

        } else if (player1.getScore() < player2.getScore()) {

            player2Score.setBackground(new Color(0, 250, 0));

            player1Score.setText(String.valueOf(player1.getScore()));
            player2Score.setText(String.valueOf(player2.getScore()));

        } else {

            player1Score.setBackground(new Color(255, 255, 0));
            player1Score.setText(String.valueOf(player1.getScore()));

            player2Score.setBackground(new Color(255, 255, 0));
            player2Score.setText(String.valueOf(player2.getScore()));
        }

    }

    private void displayMove(Player player) {

        player.playerMove.setBackground(null);
        player.playerMove.setText("");

        if (player.getMove() == 1) {

            ImageIcon rockIcon = new ImageIcon(
                    new ImageIcon("src/icons/rock.png").getImage()
                            .getScaledInstance(140, 140, Image.SCALE_SMOOTH));

            player.playerMove.setIcon(rockIcon);

        } else if (player.getMove() == 2) {

            ImageIcon paperIcon = new ImageIcon(
                    new ImageIcon("src/icons/paper.png").getImage()
                            .getScaledInstance(140, 140, Image.SCALE_SMOOTH));

            player.playerMove.setIcon(paperIcon);

        } else if (player.getMove() == 3) {

            ImageIcon scissorIcon = new ImageIcon(
                    new ImageIcon("src/icons/scissor.png").getImage()
                            .getScaledInstance(140, 140, Image.SCALE_SMOOTH));

            player.playerMove.setIcon(scissorIcon);

        }

    }

    private void showWinner(int winner) {

        if (winner == 1) {

            ImageIcon img = new ImageIcon("src/icons/player1win.png");
            resultText.setIcon(img);

        } else if (winner == 2) {

            ImageIcon img = new ImageIcon("src/icons/player2win.png");
            resultText.setIcon(img);

        } else {

            ImageIcon img = new ImageIcon("src/icons/draw.png");
            resultText.setIcon(img);

        }

    }

    private void resetPlayerMove(Player player) {

        ImageIcon noMoveIcon = new ImageIcon("src/icons/noMove.png");

        player.playerMove.setIcon(noMoveIcon);

    }

    private void countDownSound() {

        // setting up the sound effects for the start button
        try {

            File file1 = new File("src/audio/countDown.wav");
            AudioInputStream countDown;

            countDown = AudioSystem.getAudioInputStream(file1);

            Clip clip = AudioSystem.getClip();
            clip.open(countDown);
            clip.stop();
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

            e.printStackTrace();
        }

    }

    private void playerMovedSound() {

        // setting up the sound effects for the start button
        try {

            File file1 = new File("src/audio/clicked.wav");
            AudioInputStream startButton;

            startButton = AudioSystem.getAudioInputStream(file1);

            Clip clip = AudioSystem.getClip();
            clip.open(startButton);
            clip.stop();
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {

            e.printStackTrace();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

            case 81:

                playerMoved(player1, ROCK);

                break;

            case 87:

                playerMoved(player1, PAPER);

                break;

            case 69:

                playerMoved(player1, SCISSORS);

                break;

            case 97:

                playerMoved(player2, ROCK);

                break;

            case 98:

                playerMoved(player2, PAPER);

                break;

            case 99:

                playerMoved(player2, SCISSORS);

                break;

            case 13:

                nextRound.doClick();

                break;

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
