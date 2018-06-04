package sort;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Contenedor extends JPanel{

  private JLabel etiqueta;
  int[] numeros;

  public Contenedor(String archivo, int framerate, String metodo){
    setLayout(new BorderLayout());
    etiqueta = new JLabel(new ImageIcon(createImage(archivo)));
    add(etiqueta);
    JButton botonOrdenar = new JButton("Ordenar");
    add(botonOrdenar, BorderLayout.SOUTH);
    botonOrdenar.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e){
        BufferedImage imagen = (BufferedImage) ((ImageIcon) etiqueta.getIcon()).getImage();
        new UpdateWorker(numeros, etiqueta, archivo, imagen, framerate, metodo).execute();
        botonOrdenar.setEnabled(false);
      }
    });
  }

  public BufferedImage createImage(String archivo){
    BufferedImage imagen = null;
    try{
      imagen = ImageIO.read(new File("resource/"+archivo));
      ataqueHackerman(imagen);
      Graphics2D g = imagen.createGraphics();
      g.dispose();
    }catch(Exception e){
      System.err.println("(-)\tAsegurate de estar en el directorio 'src'");
      System.err.println("\ty de haber escrito bien el nombre de imagen (la cual debe estar en la carpeta resource)");
    }
    return imagen;
  }

  public void ataqueHackerman(BufferedImage imagen){
    int length = imagen.getHeight()*imagen.getWidth();
    numeros = new int[length];
    for(int i = 0; i < numeros.length; i++)
    numeros[i] = i;
    Random r = new Random();
    for(int i = 0; i < length; i++){
      int j = r.nextInt(length);
      swapImagen(imagen, i, j);
    }
  }
  
  public void swapImagen(BufferedImage imagen, int i, int j){
    int colI = i%imagen.getWidth();
    int renI = i/imagen.getWidth();
    int colJ = j%imagen.getWidth();
    int renJ = j/imagen.getWidth();
    int aux = imagen.getRGB(colI, renI);
    imagen.setRGB(colI, renI, imagen.getRGB(colJ, renJ));
    imagen.setRGB(colJ, renJ, aux);
    aux = numeros[i];
    numeros[i] = numeros[j];
    numeros[j] = aux;
  }

}
