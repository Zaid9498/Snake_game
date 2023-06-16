import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    int B_HT = 400;
    int B_WIDTH = 400;
    int MaxDots = 1600;
    int DotSize = 10;
    int Dots;
    int x[] = new int[MaxDots];
    int y[] = new int[MaxDots];
    int apple_x;
    int apple_y;
    //images
    Image body, head, apple;
    Timer timer;
    int DELAY = 150;
    boolean leftDirection = true;
    boolean rightDirection = false;
    boolean upDirection = false;
    boolean downDirection = false;

    boolean inGame = true;

    Board() {
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HT));
        setBackground(Color.BLACK);
        intitGame();
        loadIimages();
    }

    //initialize of game
    public void intitGame() {
        Dots = 3;
        //initialize of snake's position
        x[0] = 250;
        y[0] = 250;
        for (int i = 1; i < Dots; i++) {
            x[i] = x[0] + DotSize * i;
            y[i] = y[0];
        }
        // initialize of apples position
//        apple_x=150;
//        apple_y=150;
        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    //load images from resources folder to image object
    public void loadIimages() {
        ImageIcon bodyIcon = new ImageIcon("src/resources/dot.png");
        body = bodyIcon.getImage();
        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();
        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();
    }

    //draw images of snakes & apples position
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    public void doDrawing(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < Dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[0], y[0], this);
                } else
                    g.drawImage(body, x[i], y[i], this);
            }
        } else {
            gameOver(g);
            timer.stop();
        }
    }
    //initialize apples position randomly
    public void locateApple() {
        apple_x = ((int) (Math.random() * 39)) * DotSize;
        apple_y = ((int) (Math.random() * 39)) * DotSize;
    }
     // check collision with border & body.
    public void checkCollision(){
        // collision with body
        for(int i=1;i<Dots;i++){
            if(i>4 && x[0]==x[i] && y[0]==y[i]){
                inGame=false;
            }
        }
        // collision with border
        if(x[0]<0){
            inGame=false;
        }
        if(x[0]>=B_WIDTH){
            inGame=false;
        }
        if(y[0]<0){
            inGame=false;
        }
        if(y[0]>=B_HT){
            inGame=false;
        }
    }
    public void gameOver(Graphics g){
        String msg="Game over";
        int score=(Dots-3)*100;
        String scoremsg="Score:"+Integer.toString(score);
        Font small=new Font("Helvetica",Font.BOLD,14);
        FontMetrics fontMetrics=getFontMetrics(small);

        g.setColor(Color.RED);
        g.setFont(small);
        g.drawString(msg,(B_WIDTH-fontMetrics.stringWidth(msg))/2, B_HT/4);
        g.drawString(scoremsg,(B_WIDTH-fontMetrics.stringWidth(scoremsg))/2,3*(B_HT/4));

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    public void move() {
        for (int i = Dots - 1; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (leftDirection) {
            x[0] -= DotSize;
        }
        if (rightDirection) {
            x[0] += DotSize;
        }
        if (upDirection) {
            y[0] -= DotSize;
        }
        if (downDirection) {
            y[0] += DotSize;
        }
    }
    //snake eat apple
    public void checkApple(){
       if(apple_x==x[0] && apple_y==y[0]){
           Dots++;
           locateApple();
       }
    }

    //key controlling
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent keyEvent) {
            int key =keyEvent.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !rightDirection) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (key == KeyEvent.VK_RIGHT && !leftDirection) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if (key == KeyEvent.VK_UP && !downDirection) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if (key == KeyEvent.VK_DOWN && !upDirection) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}