/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TRex;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.media.opengl.GLCanvas;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Trex2 extends JFrame  {
 Animator animator;
  private GLCanvas glcanvas;
  private TrexGLEventListener listener = new TrexGLEventListener();

  public static void main(String[] args) {
      InputStream music;
        try{
            
            music=new FileInputStream(new File("src\\TRex\\mixkit.WAV"));
            AudioStream audios=new AudioStream(music);  
            AudioPlayer.player.start(audios);
          
        }catch(Exception e){
          JOptionPane.showMessageDialog(null , e.getLocalizedMessage());  
        }
    new Trex2().animator.start();
  }

  public Trex2() {
    super("Trex test Application");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    glcanvas = new GLCanvas();
    glcanvas.addGLEventListener(listener);
    glcanvas.addKeyListener(listener);
    getContentPane().add(glcanvas, BorderLayout.CENTER);
    animator = new FPSAnimator(10);
    animator.add(glcanvas);
        

    
    setSize(900, 600);
    setLocationRelativeTo(this);
    setVisible(true);
     setFocusable(true);
        glcanvas.requestFocus();
      }

}
