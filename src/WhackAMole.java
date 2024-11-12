
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;


public class WhackAMole {
    int boardWidth = 600;
    int boardHeight = 650;
    int score =0;

    JFrame frame = new JFrame("Mario: Whack A Mole");
    JPanel textPanel = new JPanel();
    JLabel textLabel = new JLabel();
    JPanel boardPanel = new JPanel();

    JButton[] board = new JButton[9];
    ImageIcon plantIcon;
    ImageIcon moleIcon;

    JButton currentMoleTile;
    JButton currrentPlantTile;
    
    Random random = new Random();
    Timer setMolTimer;
    Timer setPlantTimer;

    public WhackAMole() {
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial",Font.PLAIN,50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Score : 0");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel,BorderLayout.NORTH);
        
        boardPanel.setLayout(new GridLayout(3,3));
        frame.add(boardPanel);
        // boardPanel.setBackground(Color.black);

        // piranhaIcon = new ImageIcon(getClass().getResource("./piranha.png"));
        Image plantImg = new ImageIcon(getClass().getResource("./piranha.png")).getImage();
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        Image moleImg = new ImageIcon(getClass().getResource("./monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
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
                    }
                    else{
                        score-=5;
                        textLabel.setText("Score : " + Integer.toString(score));
                    }
                }
            });
        }

        setMolTimer = new Timer(500, new ActionListener() {
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

    
}
