import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
public class FlappyBird extends JPanel implements ActionListener,KeyListener{
    int boardwidth=360;
    int boardHeight=640;
    
    Image backgroundImg;
    Image birdImg;
    Image toppipeImg;
    Image bottompipeImg;
     int birdx = boardwidth/8;
     int birdy=boardHeight/2;
     int birdwidth=34;
     int birdheight=24;
    class Bird{
        int x=birdx;
        int y=birdy;
        int width=birdwidth;
        int height=birdheight;
        Image img;
        Bird(Image img)
        {
            this.img=img;
        }
    }
    int pipex=boardwidth;
    int pipey=0;
    int pipewidth=64;
    int pipeheight=512;
    class Pipe{
         int x=pipex;
         int y=pipey;
         int width=pipewidth;
         int height=pipeheight;
         Image img;
         boolean passed = false;

         Pipe(Image img)
         {
            this.img=img;
         }

    }
    Bird bird;
    int velocityY=0;
    int gravity=1;
    Timer gameloop;
    Timer placePipeTimer;
   int  velocityX=-4;
   ArrayList<Pipe> pipes = new ArrayList<>();
double score = 0;
 
   boolean  gameover = false;
   Random random = new Random();
    FlappyBird()
    {
        setPreferredSize(new Dimension(boardwidth,boardHeight));
        setFocusable(true);
        addKeyListener(this);
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        toppipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottompipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        bird = new Bird(birdImg);
        placePipeTimer = new Timer(1500,new ActionListener() {
           @Override
            public void actionPerformed(ActionEvent e)
            {
            placePipes();
            }
        });
        placePipeTimer.start();
        gameloop = new Timer(1000/60,this);
        gameloop.start();

    }

    public void placePipes()
    {   
        int randomPipeY=(int)(pipey - pipeheight/4 - Math.random()*(pipeheight/2));
        int openingSpace = boardHeight/4;
        Pipe topPipe = new Pipe(toppipeImg);
        topPipe.y=randomPipeY;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(bottompipeImg);
        bottomPipe.y=topPipe.y + pipeheight + openingSpace;
        pipes.add(bottomPipe);


    }
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g)
    {
        g.drawImage(backgroundImg, 0, 0, boardwidth,boardHeight,null);
        g.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);

        for(int i=0;i<pipes.size();i++)
        {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height,null);

        }

        // score
         g.setColor(Color.PINK);
         g.setFont(new Font("Arial",Font.PLAIN,32));
         if(gameover)
         {
            g.drawString("GameOver: "+ String.valueOf((int)score), 10, 35);
         }
         else
         {
            g.drawString(String.valueOf((int)score), 10, 35);
         }
    }
    public void move()
    {   
        velocityY+=gravity;
        bird.y+=velocityY;
        bird.y = Math.max(bird.y,0);
       
        //pipes
        for(int i=0;i<pipes.size();i++)
        {
            Pipe pipe = pipes.get(i);
            pipe.x+=velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width)
            {
              pipe.passed=true;
              score+=0.5;
            }
            if(collison(bird,pipe))
            {
                gameover = true;
            }
        }

        if(bird.y > boardHeight)
        {
            gameover = true;
        }
       

    }
    public boolean collison(Bird a,Pipe b)
    {
        return (a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y);
       
    }
    @Override
    public void actionPerformed(ActionEvent e) {
      move();
      repaint();
     if(gameover)
     {
        placePipeTimer.stop();
        gameloop.stop();
     }
    }
    @Override
    public void keyTyped(KeyEvent e) {
      
    }
    @Override
    public void keyPressed(KeyEvent e) {
      
    }
    @Override
    public void keyReleased(KeyEvent e) {
      if(e.getKeyCode() == KeyEvent.VK_SPACE)
      {
         velocityY=-9;   
         if(gameover)
         {
           bird.y=birdy;
           velocityY=0; 
           pipes.clear();
           score = 0;
           gameover = false;
           gameloop.start();
           placePipeTimer.start();
         }                                             
      }
    }
    
}
