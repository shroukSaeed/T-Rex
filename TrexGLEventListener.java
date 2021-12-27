/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TRex;

import com.sun.opengl.util.j2d.TextRenderer;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author Shrouk
 */
public class TrexGLEventListener extends AnimListener {

    int animationIndex = 0;
    int animationIndex1 = 2;
    int animationIx = 11;
    int maxWidth = 100;
    int maxHeight = 100;
    float sclv1 = 0.5f;
    float sclv2 = 0.5f;
    float sclv3 = 0.5f;
    int x = 0, y = 20;
    private static final int X_MIN = 0;
    private static final int X_MAX = 120;
    private static final int Y_MIN = 0;
    private static final int Y_MAX = 120;
//     double rotate = 0;
//    int x = maxWidth/2, y = maxHeight/2;
//    private final int NUMBER_OF_DIRECTIONS = 4;
    private final int MAX_STEPS = 30;

    private double trexXPosition;
    private double trexYPosition;
//    private double trexyPosition = -40;
    private double trexX2Position;
    private double trexX3Position;

    private double trexX3Position1;
    private double trexX2Position1;
    private double trexXPosition1;

    boolean birdcheck = true;
    int timer = 100;
    int timerP = 0;
    int score = 0;
    int scorePause = 0;

    boolean isGameOver = false;
    boolean isWinner = false;
    boolean isPause = false;
    boolean isResume = false;
    boolean isEx = false;
    
    boolean down = false;
    private int YBird=35;

    private int direction;
    private int count = 0;

    TextRenderer tr = new TextRenderer(Font.decode("PLAIN"));

    String textureNames[] = {"walk1.png", "walk2.png", "duck1.png", "duck2.png", "hit.png", "idle.png",
        "cocus1b.png", "cocus1c.png", "cocus2s.png", "cocus3.png", "cocus3s.png",
        "bird1.png", "bird2.png", "cloud.png", "live.png", "paus.png", "land.png"};
    TextureReader.Texture texture[] = new TextureReader.Texture[textureNames.length];
    int textures[] = new int[textureNames.length];

    @Override
    public void init(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);    //This Will Clear The Background Color To Black
        gl.glEnable(GL.GL_TEXTURE_2D);  // Enable Texture Mapping
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glGenTextures(textureNames.length, textures, 0);
        gl.glOrtho(X_MIN, X_MAX, Y_MIN, Y_MAX, -1.0, 1.0);
        trexXPosition = 21;
        trexYPosition = 0;
        trexX2Position = 90;
        trexX3Position = 50;

//        steps = 1 + (int) (Math.random() * MAX_STEPS);

        for (int i = 0; i < textureNames.length; i++) {
            try {

                texture[i] = TRex.TextureReader.readTexture(assetsFolderName + "//" + textureNames[i], true);
                gl.glBindTexture(GL.GL_TEXTURE_2D, textures[i]);

//                mipmapsFromPNG(gl, new GLU(), texture[i]);
                new GLU().gluBuild2DMipmaps(
                        GL.GL_TEXTURE_2D,
                        GL.GL_RGBA, // Internal Texel Format,
                        texture[i].getWidth(), texture[i].getHeight(),
                        GL.GL_RGBA, // External format from image,
                        GL.GL_UNSIGNED_BYTE,
                        texture[i].getPixels() // Imagedata
                );
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void display(GLAutoDrawable gld) {
        GL gl = gld.getGL();
        gl.glClear(GL.GL_COLOR_BUFFER_BIT);       //Clear The Screen And The Depth Buffer
        gl.glLoadIdentity();

        updateLandPosition();
        updateCacutsPosition();
        updatePirdPosition();

        if (timer > 0 && !isGameOver && !isWinner && !isPause) {
            timer--;
            score++;
        } else if(timer==0 && !isGameOver ) {
           isWinner = true ;
        }
       

//        handleKeyPress();
        animationIndex = animationIndex % 2;
        animationIndex1 = animationIndex1 % 4;

        DrawLive1(gl, 0, 0, 14, sclv1);
        DrawLive2(gl, 5, 0, 14, sclv2);
        DrawLive3(gl, 10, 0, 14, sclv3);

        if (birdcheck == true) {
            animationIx = 11;
            birdcheck = false;
        } else if (birdcheck == false) {
            animationIx = 12;
            birdcheck = true;
        }

        if (!isGameOver && !isWinner && !isPause) {
//            && !isWinner
            if (down) {
                DrawSprite(gl, x, y, animationIndex1++, 1.4f);
//            down = false;
            } else if (!down) {

                DrawSprite(gl, x, y, animationIndex++, 1.4f);
            }

            DrawPird(gl,trexX3Position,YBird , animationIx++, 1.4f);
            DrawLand(gl, trexXPosition, trexYPosition - 13, 2f);
            DrawClouds(gl, trexXPosition, 50, 13, 1.3f);
            DrawClouds(gl, trexX2Position, 50, 13, 1);
            DrawCocus(gl, trexXPosition, 21, 7, 1.4f);
            DrawCocus(gl, trexX2Position, 21, 9, 1.4f);
            DrawCocus(gl, trexX2Position + 110, 21, 7, 1.4f);
            trexX2Position1 = trexX2Position;
            trexXPosition1 = trexXPosition;
            trexX3Position1 = trexX3Position;
        } else if((isGameOver && !isWinner && !isPause) || ( isWinner && !isGameOver && !isPause) ){

            DrawPird(gl, trexX3Position1, YBird, 11, 1.4f);
            DrawLand(gl, trexXPosition1, trexYPosition - 13, 2f);
            DrawClouds(gl, trexXPosition1, 50, 13, 1.3f);
            DrawClouds(gl, trexX2Position1, 50, 13, 1);
            DrawCocus(gl, trexX2Position1, 21, 9, 1.4f);
            DrawSprite(gl, x, y, 4, 1.4f);
        } 
        
        else if(isPause && !isWinner && !isGameOver) {
            DrawPird(gl, trexX3Position1, YBird, 11, 1.4f);
            DrawLand(gl, trexXPosition1, trexYPosition - 13, 2f);
            DrawClouds(gl, trexXPosition1, 50, 13, 1.3f);
            DrawClouds(gl, trexX2Position1, 50, 13, 1);
            DrawCocus(gl, trexX2Position1, 21, 9, 1.4f);
            scorePause = score;
            timerP = timer;
            DrawSprite(gl, x, y, 4, 1.4f);
            Pause();
        }
        
        else if(isResume && !isPause) {
            DrawPird(gl,trexX3Position,YBird , animationIx++, 1.4f);
            DrawLand(gl, trexXPosition, trexYPosition - 13, 2f);
            DrawClouds(gl, trexXPosition, 50, 13, 1.3f);
            DrawClouds(gl, trexX2Position, 50, 13, 1);
            DrawCocus(gl, trexXPosition, 21, 7, 1.4f);
            DrawCocus(gl, trexX2Position, 21, 9, 1.4f);
            DrawCocus(gl, trexX2Position + 110, 21, 7, 1.4f);
            score = scorePause;
            timer = timerP;
        }

        DrawScoreAndTimer();

        if (count == 0 && (trexX2Position == x || trexXPosition == x ||  (YBird == y &&trexX3Position==x)) && y <= 21 && !isWinner
                && !isPause) {
            sclv1 = 0;

            try {
                InputStream music;
                music = new FileInputStream(new File("src\\TRex\\Mario 2 - Die.WAV"));
                AudioStream audios = new AudioStream(music);
                AudioPlayer.player.start(audios);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }
            count++;
        } else if (count == 1 && (trexX2Position == x || trexXPosition == x || (YBird == y &&trexX3Position==x)) && y <= 21 && !isWinner 
                 && !isPause) {
            sclv2 = 0;
            InputStream music;
            try {

                music = new FileInputStream(new File("src\\TRex\\Mario 2 - Die.WAV"));
                AudioStream audios = new AudioStream(music);
                AudioPlayer.player.start(audios);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }
            count++;
        } else if (count == 2 && (trexX2Position == x || trexXPosition == x ||  (YBird == y && trexX3Position==x)) && y <= 21 && !isWinner
                && !isPause) {

            sclv3 = 0;
            try {
                InputStream music;

                music = new FileInputStream(new File("src\\TRex\\Mario 2 - Die.WAV"));
                AudioStream audios = new AudioStream(music);
                AudioPlayer.player.start(audios);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
            }

            isGameOver = true;
            GameOver();
        }
        
        else if(isWinner && !isGameOver){
            isGameOver = false;
            Winner();
        }

    }

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
    }

    @Override
    public void displayChanged(GLAutoDrawable glad, boolean bln, boolean bln1) {
    }

    @Override
    public void keyPressed(final KeyEvent e) {

        int keyCode = e.getKeyCode();
        keyBits.set(keyCode);
        if (keyCode == KeyEvent.VK_UP) {
            if (y < maxHeight -65) {
                try {
                    y += 15;
                    InputStream music;
                    music=new FileInputStream(new File("src\\TRex\\Mario 1 - Jump.WAV"));
                    AudioStream audios=new AudioStream(music);
                    AudioPlayer.player.start(audios);
                }
                catch (FileNotFoundException ex) {
                    Logger.getLogger(TrexGLEventListener.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(TrexGLEventListener.class.getName()).log(Level.SEVERE, null, ex);
                }           
            }
            }
        if (keyCode == KeyEvent.VK_DOWN) {
             if(!isGameOver){
                 down=true;
             }
         }
        if (keyCode == KeyEvent.VK_P) {
           isPause = true;
//           Pause();
        }
        
        if (keyCode == KeyEvent.VK_R) {
           isPause = false;
           isResume = true;
        }
        if (keyCode == KeyEvent.VK_ESCAPE) {
           
            NewJFrame eng = new NewJFrame();
//            
            eng.setVisible(true);
        }
        
    }

    @Override
    public void keyReleased(final KeyEvent event) {
        int keyCode = event.getKeyCode();
        keyBits.clear(keyCode);
        if (keyCode == KeyEvent.VK_UP) {
            y -= 15;

        } if (keyCode == KeyEvent.VK_DOWN) {
             if(!isGameOver){
                 down=false;
             }
    }
    }

    @Override
    public void keyTyped(final KeyEvent event) {
        // don't care  
    }

    public boolean isKeyPressed(final int keyCode) {
        return keyBits.get(keyCode);
    }


    public void DrawSprite(GL gl, double x, double y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawPird(GL gl, double x, double y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);
        // Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawLand(GL gl, double x, double y, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[16]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.2, y / (maxHeight / 2.0) - 0.2, 0);
        gl.glScaled(2 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawClouds(GL gl, double x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawCocus(GL gl, double x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawLive1(GL gl, double x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawLive2(GL gl, double x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawLive3(GL gl, double x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[index]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }

    public void DrawPause(GL gl, double x, int y, int index, float scale) {
        gl.glEnable(GL.GL_BLEND);
        gl.glBindTexture(GL.GL_TEXTURE_2D, textures[15]);	// Turn Blending On

        gl.glPushMatrix();
        gl.glTranslated(x / (maxWidth / 2.0) - 0.9, y / (maxHeight / 2.0) - 0.9, 0);
        gl.glScaled(0.1 * scale, 0.1 * scale, 1);
        //System.out.println(x +" " + y);
        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(1.0f, 1.0f, -1.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
        gl.glPopMatrix();

        gl.glDisable(GL.GL_BLEND);
    }


    public BitSet keyBits = new BitSet(256);

    private void updateLandPosition() {
        if (direction == 0) { // left
            trexXPosition -= 10;

            if (trexXPosition < X_MIN) {
                trexXPosition = X_MAX;
            }

        }
    }

    private void updateCacutsPosition() {
        if (direction == 0) { // left
            trexX2Position -= 10;

            if (trexX2Position < X_MIN) {
                trexX2Position = X_MAX;
            }

        }
    }

    private void updatePirdPosition() {
        if (direction == 0) { // left
            trexX3Position -= 10;

            if (trexX3Position < X_MIN) {
                trexX3Position = X_MAX;
            }

        }
    }


    public void DrawScoreAndTimer() {
        if(!isPause) {
            tr.beginRendering(300, 300);
            tr.setColor(Color.BLACK);
            tr.draw("Score : " + score, 8, 280);
            tr.draw("Timer : " + timer, 8, 260);
            tr.draw("Enter P to pause", 200, 280);
            tr.setColor(Color.WHITE);
            tr.endRendering();
        }
        
        else {
            tr.beginRendering(300, 300);
            tr.setColor(Color.BLUE);
            tr.draw("Score : " + scorePause, 8, 280);
            tr.draw("Timer : " + timerP, 8, 260);
            tr.draw("Enter R to resume", 200, 280);
            tr.setColor(Color.WHITE);
            tr.endRendering();
        }

    }

    public void GameOver() {

        tr.beginRendering(300, 300);
        tr.setColor(Color.BLUE);
        tr.draw("Game Over ", 80, 230);
        tr.draw("Your score : " + score, 80, 200);
        tr.setColor(Color.WHITE);
        tr.endRendering();

        isGameOver = true;
    }

    public void Winner() {

        tr.beginRendering(300, 300);
        tr.setColor(Color.BLUE);
        tr.draw("Winner", 80, 230);
        tr.draw("Your score : " + score, 80, 200);
        tr.setColor(Color.WHITE);
        tr.endRendering();

        isWinner = true;
    }
    
    public void Pause() {

        tr.beginRendering(300, 300);
        tr.setColor(Color.BLUE);
        tr.draw("Pause ", 80, 230);
        tr.setColor(Color.WHITE);
        tr.endRendering();

        isResume = false;
        isPause = true;
    }
}
        

