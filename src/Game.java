import bagel.*;
import java.lang.Math;
import bagel.util.Point;
import static bagel.Window.close;

public class Game extends AbstractGame {
    private final Image house = new Image("res/images/bg2.png");
    private final Image player = new Image("res/images/player.png");
    private final Image apple = new Image("res/images/cake.png");
    private final Font myName = new Font("res/Font/BillionDreams_PERSONAL.ttf",52);
    private final Font myScore = new Font("res/Font/Hysteria.ttf",45);
    private final String  title1 = "Rainko With a Cake";
    private final Point titleLocation1 = new Point(20, 50);
    private final String  title2 = "--Designed By Alvin";
    private final Point titleLocation2 = new Point(60, 100);


    private final int damage = 10, heal = 5;
    private final double gravity = 0.6, groundLevel = 624;
    private final double moveDamping = 0.99, bumpDamping = 0.87, jumpAcceleration = 2, radius = 35;
    private final double minimumBounceSpeed = 5;

    private double x, y;
    private double xSpeed, ySpeed;
    private int health, score;
    private boolean isGameOver;

    private  double initialScrollPosition = 0;

    double xApple = Window.getWidth() * Math.random();
    double yApple = groundLevel * Math.random();

    public Game() {
        reset();
    }

    public void reset(){

        this.x = 220;
        this.y = groundLevel;
        this.xSpeed = 0;
        this.ySpeed = 0;
        this.health = 100;
        this.score = 0;
        this.isGameOver = false;
    }

    public void gameOver(){
        health=0;
        isGameOver = true;
        Font GG = new Font("res/Font/Hysteria.ttf",85);
        GG.drawString("Game Over!", Window.getWidth()/2.0-160, Window.getHeight()/2.0-200);
        GG = new Font("res/Font/Hysteria.ttf",40);
        GG.drawString("Press ENTER to play again...", Window.getWidth()/2.0-160, Window.getHeight()/2.0-160);
        GG.drawString("              ESC to exit...", Window.getWidth()/2.0-160, Window.getHeight()/2.0-120);
        //System.out.println("Score: " + score);
    }

    /**
     * Entry point for Bagel game
     *
     * Explore the capabilities of Bagel: https://people.eng.unimelb.edu.au/mcmurtrye/bagel-doc/
     */
    public static void main(String[] args) {
        // Create new instance of game and run it
        Game game = new Game();
        game.run();
    }

    /**
     * Updates the game state approximately 60 times a second, potentially reading from input.
     * @param input The input instance which provides access to keyboard/mouse state information.
     */
    @Override
    protected void update(Input input) {
        double walkAcceleration = 0.2 + 0.1 * Math.random();

        if(!isGameOver) {
            if (input.isDown(Keys.LEFT) || input.isDown(Keys.A)) {
                xSpeed -= walkAcceleration;
            }
            if (input.isDown(Keys.RIGHT) || input.isDown(Keys.D)) {
                xSpeed += walkAcceleration;
            }
            if (input.wasPressed(MouseButtons.LEFT) || input.wasPressed(Keys.UP) || input.wasPressed(Keys.W) || input.wasPressed(Keys.SPACE)) {
                ySpeed -= 5 * jumpAcceleration + 1 * Math.random();
            }

            if (input.wasPressed(Keys.ESCAPE)) {
                close();
            }



        }else{
            if (input.isDown(Keys.ENTER)) {
                reset();
                // reset
            }
            if (input.wasPressed(Keys.ESCAPE)) {
                close();
            }
        }
        x += xSpeed;
        y += ySpeed;
        ySpeed += gravity;
        xSpeed *= moveDamping;

        if (y < 0) {
            y = 0;
            ySpeed = 0;
            health -= damage;
        } // SKY

        if (y > groundLevel) {
            y = groundLevel;
            ySpeed *= -bumpDamping;
            if (Math.abs(ySpeed) < minimumBounceSpeed) {
                ySpeed = 0;
            } else {
                health -= (damage-5);
            }
        } //GROUND


        if (x < 0) {
            xSpeed *= -bumpDamping;
        } // LEFT

        if (x > Window.getWidth()) {
            xSpeed *= -bumpDamping;
        } // RIGHT
        if (Math.abs(x - xApple) < radius && Math.abs(y - yApple) < radius && (!isGameOver)) {
            // Collision
            xApple = Window.getWidth() * Math.random();
            yApple = groundLevel * Math.random();
            score += 10;
            health += heal;
        }
        
        if (initialScrollPosition == Window.getWidth()){initialScrollPosition=0;}
        DrawOptions loopImage = new DrawOptions();
        loopImage.setSection(initialScrollPosition, 0, Window.getWidth() - initialScrollPosition, Window.getHeight());
        house.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0, loopImage);

        DrawOptions loopImage2 = new DrawOptions();
        loopImage2.setSection(0, 0, initialScrollPosition, Window.getHeight());
        house.draw(1.5*Window.getWidth() - initialScrollPosition, Window.getHeight() / 2.0, loopImage2);

        initialScrollPosition+=1;
        DrawOptions bigger = new DrawOptions();
        bigger.setScale(1.3,1.3);

        DrawOptions scale = new DrawOptions();
        scale.setScale(0.2,0.2);
        apple.draw(xApple, yApple, scale);

        
        scale.setScale(1.3,1.3);
        player.draw(x, y, scale);
        //player.draw(x, y, color);

        
        myName.drawString(title1, titleLocation1.x, titleLocation1.y);
        myName.drawString(title2, titleLocation2.x, titleLocation2.y);
        

        if (health > 100) {health = 100;}
        if (health <= 0) {
            gameOver();

        }
        myScore.drawString("Health: " + health +"\nScore: " + score,850, 50);

    }
}
