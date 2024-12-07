
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Flow;

import javax.swing.*;
import javax.swing.border.Border;


public class WhackAMole {
    int boardWidth = 600;
    int boardHeight = 650;
    int score =0;

    JFrame frame = new JFrame("Mario: Whack A Mole");
    JPanel textPanel = new JPanel();
    JLabel textLabel = new JLabel();
    JPanel boardPanel = new JPanel();
    JPanel menuPanel = new JPanel();
    JPanel highscorePanel = new JPanel();
    JLabel highscoreLabel = new JLabel();
    JPanel resetPanel = new JPanel();    
    JButton resetButton = new JButton();

    JButton[] board = new JButton[9];
    ImageIcon plantIcon;
    ImageIcon moleIcon;

    JButton currentMoleTile;
    JButton currrentPlantTile;
    
    Random random = new Random();
    Timer setMolTimer;
    Timer setPlantTimer;
    Border border = BorderFactory.createLineBorder(Color.BLACK, 2);

    private static final String FILE_NAME = "D:\\Projects\\Whack-A-Mole\\highscore.txt";

    public WhackAMole() {
        int highscore = getHighScore();

        //Setting Frame
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //Text Panel
        textLabel.setFont(new Font("Times New Roman",Font.PLAIN,30));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Score : 0");
        textLabel.setForeground(Color.WHITE);
        textLabel.setBackground(Color.DARK_GRAY);
        textLabel.setOpaque(true);
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        textPanel.setPreferredSize(new Dimension(330,50));

        //Highscore Panel
        highscoreLabel.setFont(new Font("Times New Roman",Font.ROMAN_BASELINE,22));
        highscoreLabel.setText("Highscore : "  + Integer.toString(highscore));   
        highscoreLabel.setForeground(Color.WHITE);     
        highscorePanel.setLayout(new BorderLayout());
        highscorePanel.add(highscoreLabel);
        highscorePanel.setBackground(Color.DARK_GRAY);
        highscorePanel.setPreferredSize(new Dimension(170,50));

        //Reset Panel
        resetButton.setText("Reset");
        resetButton.setFocusPainted(false);
        resetButton.setFont(new Font("Times New Roman",Font.BOLD,20));
        resetButton.setFocusable(true);
        resetPanel.setLayout(new BorderLayout());
        resetPanel.add(resetButton);
        resetPanel.setPreferredSize(new Dimension(100,40));
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event)
            {
                textLabel.setText("Score : 0");
                score = 0;
                for(int i=0;i<9 ;i++){
                    board[i].setEnabled(true);
                }
                setMolTimer.start();
                setPlantTimer.start();
            }
        });

        JPanel subMenuPanel = new JPanel();
        subMenuPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        subMenuPanel.add(highscorePanel);
        subMenuPanel.add(resetPanel);
        subMenuPanel.setBackground(Color.DARK_GRAY);

        menuPanel.setLayout(new BorderLayout());
        menuPanel.add(textPanel,BorderLayout.CENTER);
        menuPanel.add(subMenuPanel,BorderLayout.EAST);
        menuPanel.setBackground(Color.DARK_GRAY);

        frame.add(menuPanel,BorderLayout.NORTH);
        
        boardPanel.setLayout(new GridLayout(3,3));
        frame.add(boardPanel);
        // boardPanel.setBackground(Color.black);

        // piranhaIcon = new ImageIcon(getClass().getResource("./piranha.png"));
        Image plantImg = new ImageIcon(getClass().getResource("./piranha.png")).getImage();
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        Image moleImg = new ImageIcon(getClass().getResource("./monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        //Game Logic
        for(int i=0;i<9;i++){
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false);
            
            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    JButton tile = (JButton) e.getSource();

                    if(tile== currentMoleTile){
                        score+= 10;
                        textLabel.setText("Score : " + Integer.toString(score));
                    }else if(tile == currrentPlantTile){
                        textLabel.setText("Game Over : " + Integer.toString(score));
                        setMolTimer.stop();
                        setPlantTimer.stop();
                        for(int i=0;i<9 ;i++){
                            board[i].setEnabled(false);
                        }
                        if(score > highscore){
                            highscoreLabel.setText("HighScore : " + Integer.toString(score));
                            saveHighScore(score);
                        } 
                    }
                    else{
                        score-=5;
                        textLabel.setText("Score : " + Integer.toString(score));
                    }
                }
            });
        }
        
        //Setting Mole Position
        setMolTimer = new Timer(800, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(currentMoleTile != null){
                    currentMoleTile.setIcon(null);
                    currentMoleTile = null;
                }

                int num = random.nextInt(9);  //0-8
                JButton tile = board[num];

                if(currrentPlantTile == tile) return;

                currentMoleTile = tile;
                currentMoleTile.setIcon(moleIcon);
            }
        });

        //Setting Plant position
        setPlantTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(currrentPlantTile != null){
                    currrentPlantTile.setIcon(null);
                    currrentPlantTile = null;
                }

                int num = random.nextInt(9);  

                JButton tile = board[num];

                if(currentMoleTile == tile) return;

                currrentPlantTile = tile;
                currrentPlantTile.setIcon(plantIcon);
            }
        });
        
        setMolTimer.start();
        setPlantTimer.start();
        frame.setVisible(true);
    }

    private static int getHighScore() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            // Create the file if it does not exist
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating file: " + e.getMessage());
            }
            return 0; // No previous scores, return 0
        }

        // Read the high score from the file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line); // Return the existing high score
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading high score: " + e.getMessage());
        }
        
        return 0; // Default to 0 if there's an issue
    }

    // Method to save the new high score to the file
    private static void saveHighScore(int score) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write(String.valueOf(score)); // Write the new high score to the file
        } catch (IOException e) {
            System.err.println("Error saving high score: " + e.getMessage());
        }
    }

    
}
