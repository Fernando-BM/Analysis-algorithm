package sort;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

public class UpdateWorker extends SwingWorker<BufferedImage, BufferedImage>{

  private BufferedImage referencia;
  private JLabel target;
  private BufferedImage copia;
  private int[] arreglo;
  private int framerate;
  private int n;
  private String metodo;
  private int iteracion;


  public UpdateWorker(int[] numeros, JLabel target, String archivo, BufferedImage master, int speed, String algoritmo){
    this.arreglo = numeros;
    this.target = target;
    try{
      referencia = ImageIO.read(new File("resource/"+archivo));
      copia = master;
      n = copia.getHeight()*copia.getWidth();
    }catch(Exception e){
      System.err.println(":c Esto no deberia ocurrir");
    }
    framerate = speed; // Indica cada cuantas iteraciones se actualizara la imagen
    metodo = algoritmo;
    iteracion = 0;
  }

  public BufferedImage updateImage(){
    Graphics2D g = copia.createGraphics();
    g.drawImage(copia, 0, 0, null);
    g.dispose();
    return copia;
  }

  @Override
  protected void process(List<BufferedImage> chunks){
    target.setIcon(new ImageIcon(chunks.get(chunks.size() - 1)));
  }

  public void update(){
    for(int i = 0; i < n; i++){
      int indiceDeOriginal = arreglo[i];
      int colOriginal = indiceDeOriginal%copia.getWidth();
      int renOriginal = indiceDeOriginal/copia.getWidth();
      int colI = i%copia.getWidth();
      int renI = i/copia.getWidth();
      copia.setRGB(colI, renI, referencia.getRGB(colOriginal, renOriginal));
    }
    publish(updateImage());
  }

  @Override
  protected BufferedImage doInBackground() throws Exception{
    if(metodo.equals("bubble"))
    bubbleSort();
    if(metodo.equals("exchange"))
    exchangeSort();
    if(metodo.equals("insertion"))
    insertionSort();
    if(metodo.equals("selection"))
    selectionSort();
    if(metodo.equals("merge"))
    mergeSort();
    update();
    return null;
  }

  private void bubbleSort(){
    for(int i = 0; i < n-1; i++){
      for(int j = 0; j < n-i-1; j++){
        if(arreglo[j] < arreglo[j+1])
        swap(j, j+1);
      }
      if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
      iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones
    }
  }

  private void exchangeSort(){
    for ( int i = 0; i < n-1; i ++ ){
         for (int j = i + 1; j < n; j ++ ){
              if( arreglo[ i ] > arreglo[ j ] ) {
              swap(i, j);}
         }
         if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
         iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones
     }
}

  private void insertionSort(){

    int  auxVal;
    for (int i = 1; i <n; i++){
      auxVal = arreglo[i];
      int j;
      j = i;
      while (j > 0 && arreglo [j - 1] > auxVal) {
        arreglo[j] = arreglo[j - 1];
        j--;
      }
      arreglo[j] = auxVal;
      if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
      iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones


    }

  }

  private void selectionSort(){
    for (int i=0; i < n-1; i++) {
      for (int j=i+1; j < n; j++) {
          if (arreglo[i] > arreglo[j]) {
          swap(i, j);
         }
      }
      if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
      iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones
    }
}

private void doMergeSort(int lowerIndex, int higherIndex) {

     if (lowerIndex < higherIndex) {
         int middle = lowerIndex + (higherIndex - lowerIndex) / 2;
         //Lado izquierdo del arreglo
         doMergeSort(lowerIndex, middle);
         // Lado izquierdo del arreglo
         doMergeSort(middle + 1, higherIndex);
         //Mezcla a ambas partes
         mergeParts(lowerIndex, middle, higherIndex);
     }

 }

 private void mergeParts(int lowerIndex, int middle, int higherIndex) {
        int[] tempMergArr;
        tempMergArr = new int[n];;

        for (int i = lowerIndex; i <= higherIndex; i++) {
            tempMergArr[i] = arreglo[i];
        }
        int i = lowerIndex;
        int j = middle + 1;
        int k = lowerIndex;
        while (i <= middle && j <= higherIndex) {
            if (tempMergArr[i] <= tempMergArr[j]) {
                arreglo[k] = tempMergArr[i];
                i++;
            } else {
                arreglo[k] = tempMergArr[j];
                j++;
            }
            k++;

        }
        while (i <= middle) {
            arreglo[k] = tempMergArr[i];
            k++;
            i++;
            if(iteracion%framerate == 0) update(); // Actualizamos la interfaz grafica solo si han pasado el numero de iteraciones deseadas
            iteracion = (iteracion+1)%framerate; // Aumentamos el numero de iteraciones
        }

    }
  private void mergeSort(){
    doMergeSort(0, n-1);

  }

  public void swap(int i, int j){
    int aux = arreglo[i];
    arreglo[i] = arreglo[j];
    arreglo[j] = aux;
  }

}
