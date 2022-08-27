import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.JTextField;

public class Menu extends JFrame implements MouseListener {

    // this strings will hold the value in JTextField named player1NicknameField
    // and player2NicknameField
    private String player1Nickname;
    private String player2Nickname;

    private JTextField player1NicknameField;
    private JTextField player2NicknameField;

    private ImageIcon buttonIcon;
    private JButton start;

    private Menu() throws FontFormatException, IOException {

        // this jpanel is modified to have a gradient background color
        // holds all the components
        JPanel mainPanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // changeable color of the gradient effect
                Color color1 = Color.decode("#c0c0aa");
                Color color2 = Color.decode("#1cefff");

                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

            }

        };

        mainPanel.setPreferredSize(new Dimension(400, 450));
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.blue);

        // setting the font for game name "Jack 'N Poy"
        File font_file = new File("src/fonts/crackman.ttf");
        Font font = Font.createFont(Font.TRUETYPE_FONT, font_file);
        Font crackman = font.deriveFont(50f);

        JLabel gameName = new JLabel("Jack 'N Poy", JLabel.CENTER);
        gameName.setBounds(0, 20, 400, 40);
        gameName.setFont(crackman);
        gameName.setForeground(Color.BLACK);

        // setting the font for "Player 1" and "Player 2" labels
        File font_file2 = new File("src/fonts/GameShark.otf");
        Font font2 = Font.createFont(Font.TRUETYPE_FONT, font_file2);
        Font gameshark = font2.deriveFont(40f);

        JLabel player1Label = new JLabel("Player 1", JLabel.CENTER);
        player1Label.setFont(gameshark);
        player1Label.setBounds(100, 120, 200, 30);
        player1Label.setForeground(Color.BLACK);

        player1NicknameField = new JTextField();
        player1NicknameField.setBounds(100, 170, 200, 30);
        player1NicknameField.setFont(new Font("Arial", Font.BOLD, 20));
        player1NicknameField.setHorizontalAlignment(JTextField.CENTER);
        player1NicknameField.setOpaque(false);
        player1NicknameField.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.black));

        // ---------------------------------------------------------------

        JLabel player2Label = new JLabel("Player 2", JLabel.CENTER);
        player2Label.setFont(gameshark);
        player2Label.setBounds(100, 250, 200, 30);
        player2Label.setForeground(Color.BLACK);

        player2NicknameField = new JTextField();
        player2NicknameField.setBounds(100, 300, 200, 30);
        player2NicknameField.setFont(new Font("Arial", Font.BOLD, 20));
        player2NicknameField.setHorizontalAlignment(JTextField.CENTER);
        player2NicknameField.setOpaque(false);
        player2NicknameField.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.black));

        buttonIcon = new ImageIcon("src/icons/start.png");

        start = new JButton();
        start.setIcon(buttonIcon);
        start.setBounds(100, 370, 200, 40);
        start.setBackground(new Color(0, 0, 0, 0));
        start.setFocusable(false);
        start.setOpaque(false);
        start.setBorder(null);
        start.setContentAreaFilled(false);
        start.addMouseListener(this);

        this.add(mainPanel);
        mainPanel.add(gameName);
        mainPanel.add(player1Label);
        mainPanel.add(player1NicknameField);
        mainPanel.add(player2Label);
        mainPanel.add(player2NicknameField);
        mainPanel.add(start);

        this.setTitle("Jack 'N Poy");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

    private void disposeFrame() {

        this.dispose();
    }

    private void startSoundEffects() {

        // setting up the sound effects for the start button
        try {

            File file1 = new File("src/audio/gameStart.wav");
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

    public static void main(String[] args) throws FontFormatException, IOException {

        new Menu();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

        // will change the appearance of the start button when pressed
        if (e.getSource() == start) {

            buttonIcon = new ImageIcon("src/icons/startClicked.png");
            start.setIcon(buttonIcon);

        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

        // the code below will be executed when the start button is "clicked"
        if (e.getSource() == start) {

            startSoundEffects();

            buttonIcon = new ImageIcon("src/icons/startGlow.png");
            start.setIcon(buttonIcon);

            disposeFrame();

            player1Nickname = player1NicknameField.getText();
            player2Nickname = player2NicknameField.getText();

            GameFrame gf = new GameFrame(player1Nickname, player2Nickname);

        }

    }

    @Override
    public void mouseEntered(MouseEvent e) {

        // will change the appearance of the start button when hovered
        if (e.getSource() == start) {

            buttonIcon = new ImageIcon("src/icons/startGlow.png");
            start.setIcon(buttonIcon);

        }

    }

    @Override
    public void mouseExited(MouseEvent e) {

        // will change the appearance of the start button when mouse exited
        if (e.getSource() == start) {

            buttonIcon = new ImageIcon("src/icons/start.png");
            start.setIcon(buttonIcon);

        }

    }

}
