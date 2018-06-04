package sort;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class Sort{

  public Sort(String archivo, int framerate, String metodo){
    EventQueue.invokeLater(new Runnable(){
      @Override
      public void run(){
        try{
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          JFrame frame = new JFrame("Ordenamientos");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setLayout(new BorderLayout());
          frame.add(new Contenedor(archivo, framerate, metodo));
          frame.pack();
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
        }catch(Exception e){
          System.out.println("\t:(");
        }
      }
    });
  }

}
