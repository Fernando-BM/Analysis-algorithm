package mx.unam.ciencias.edd;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
/**
 * Práctica 9: Trayectoria mínima y algoritmo de Dijkstra.
 */
public class practica6{




    private static String cadena;
    private static String v;


    private static void uso() {
        System.err.println("El uso es: java -jar practica6.jar <Nombre Archivo>");
        System.exit(1);
    }

    public static Grafica<String> CreaGrafica(String archivo){
      Grafica<String> g = new Grafica<String>();
      String cadena;

    try {

      FileReader f = new FileReader(archivo);
      BufferedReader b = new BufferedReader(f);

      String primeraLinea=b.readLine();
      String[] partsAux = primeraLinea.split(", |,");
      v = partsAux[0];

      /*ZONE TRIKI*/


      for(String s: partsAux){
        g.agrega(s);
      }

      /*for(String ss: partsAux){
        VerticeGrafica<String>  verticeLista = g.vertice(ss);
        ELista2.agrega(verticeLista);

      }Habilitar par inconexas*/


      while((cadena = b.readLine())!=null) {
        String[] parts = cadena.split(", |,");
        if(cadena.length()<=7){
            String part1 = parts[0];
            String part2 = parts[1];
            String part3 = parts[2];
            int p3=Integer.valueOf(part3);
            g.conecta(part1,part2,p3);
            System.out.println(part1+" , "+part2
            +" , "+part3);
        }


      }
      b.close();
    }catch (IOException ioe) {
      System.out.printf("No pude cargar del archivo \"%s\".\n",
      archivo);
      System.exit(1);
    }
      return g;
  }




  public static void Guarda(String a, String b){

    try{
      File archivo=new File("Grafica.txt");
      FileWriter escribir=new FileWriter(archivo,true);
      escribir.write(a+"\n");
      escribir.write(b+"\n");
      escribir.close();
    }catch(Exception e){
      System.out.printf("No pude el archivo \"%s\".\n");
      System.exit(1);

    }
  }



    public static void main(String[] args) {

            if (args.length != 1)
                  uso();

              String nombreArchivo = args[0];
              Grafica<String> g = new Grafica<String>();
              g = CreaGrafica(nombreArchivo);

              /*Crear una lista de vertices la caul tendra todos los vertices de G original
              y se llena*/
              Lista<String> vertices_Original= new Lista<String>(); //Lita con todos los vertices de G
              for(String s: g){
                vertices_Original.agrega(s);
              }
              System.out.println(vertices_Original.toString());

              System.out.println("exito");


        for(String vertice: g){

        /* Dijkstra */
        Lista<VerticeGrafica<String>> dijkstra = g.dijkstra(v, vertice);
        Lista<String> aux= new Lista<String>();
        Lista<Lista<String>> aux2= new Lista<Lista<String>>();
        if(v.equals(vertice))
        System.out.println("");
        else{


          String j="Dijkstra de "+v+" a "+vertice+" es: " ;
          String s="";
          for (VerticeGrafica<String> ver : dijkstra)
              s += ver.getElemento() + ", ";
              Guarda(j,s);
              System.out.println(j);
              System.out.println(s);



        }
        }

    }
}
