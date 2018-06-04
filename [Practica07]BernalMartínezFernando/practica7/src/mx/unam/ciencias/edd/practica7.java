package mx.unam.ciencias.edd;

import java.io.BufferedReader;
import java.util.concurrent.ThreadLocalRandom;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Random;

/**
 * Práctica 7: Clan.
 */
public class practica7 {

    private static String cadena;

    private static String v;

  /*  private static Lista<VerticeGrafica<String>> ELista2 = new Lista<VerticeGrafica<String>>();*/




    private static void uso() {
        System.err.println("El uso es: java -jar practica7.jar clan <Nombre Archivo> <numero k>");
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



      for(String s: partsAux){
        g.agrega(s);
      }


      while((cadena = b.readLine())!=null) {
        String[] parts = cadena.split(", |,");
        if(cadena.length()<=5){
            String part1 = parts[0];
            String part2 = parts[1];
            g.conecta(part1,part2);

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


    @SuppressWarnings("unchecked")
    public static Lista<VerticeGrafica<String>> MinRecursivo(Lista<VerticeGrafica<String>> original, VerticeGrafica<String> vertice, Grafica f){

      Lista<VerticeGrafica<String>> copiat= new Lista<VerticeGrafica<String>>();

      for(VerticeGrafica<String> elem: original){

        if (f.sonVecinos( vertice.getElemento(), elem.getElemento()) == false){
          copiat.agrega(elem);
        }
      }

      return copiat;

    }



    public static   Lista<String>  DFSvertices(Grafica<String> g){

              g.paraCadaVertice((v) -> v.setColor(Color.ROJO));
              Pila<VerticeGrafica<String>> p = new Pila<VerticeGrafica<String>>();
              Lista<String> ELista = new Lista<String>();
              VerticeGrafica<String> vertice = g.vertice(v);
              vertice.setColor(Color.NEGRO);
              p.mete(vertice);
              while (!p.esVacia()) {
                  vertice = p.saca();
                  ELista.agrega(vertice.getElemento());
                  for (VerticeGrafica<String> vecino : vertice.vecinos()) {
                      if (vecino.getColor() == Color.ROJO) {
                          vecino.setColor(Color.NEGRO);
                          p.mete(vecino);

                      }
                  }
              }
              return  ELista;
    }




    public static   Grafica<String>  eliminaVertices(Grafica<String> g, Lista<String> vertices){
      for(String s:vertices){
        g.elimina(s);
      }
      return g;
    }



    //*Vamos a seleccionar los vertices a eliminar de la grafica*/
    public static   Lista<String>  eligeVetices(Lista<String> vertices, int longitudG, int nd){
      Lista<String> verticesSeleccionados = new Lista<String>();

      int numeroAeliminar=longitudG-nd;
      // Conjunto de números ya usados
      Set<Integer> alreadyUsedNumbers = new HashSet<>();

      Random random = new Random();
      // Vamos a generar k números aleatorios sin repetición
      while (alreadyUsedNumbers.size()<numeroAeliminar) {
        // Instanciamos la clase Random


         // Número aleatorio entre 0 y el numero de vertices , excluido el 40.
         int randomNumber = random.nextInt(longitudG);

         // Si no lo hemos usado ya, lo usamos y lo metemos en el conjunto de usados.
         if (!alreadyUsedNumbers.contains(randomNumber)){
            verticesSeleccionados.agrega(vertices.get(randomNumber));
            alreadyUsedNumbers.add(randomNumber);

          }
    }

    return verticesSeleccionados;
  }

  /*Verificar grado de cada arista*/
    public static boolean verificarClan(Grafica<String> n, int grado){
      boolean esClan=false;
      VerticeGrafica<String> vertice;
      for (String  v : n) {

        vertice = n.vertice(v);

        if(vertice.getGrado() == grado){

          esClan=true;
        }else{

          esClan=false;
        }

      }

      return esClan;

    }










    public static void main(String[] args) {




      if (args.length != 3)
            uso();

        String algoritmo=args[0];
        String nombreArchivo = args[1];
        int k=Integer.parseInt(args[2]);
        Grafica<String> g = new Grafica<String>();
        g = CreaGrafica(nombreArchivo);

        /*Crear una lista de vertices la caul tendra todos los vertices de G original
        y se llena*/
        Lista<String> vertices_Original= new Lista<String>(); //Lita con todos los vertices de G
        int longitudG=0; //Numero de vertices de la grafica
        for(String s: g){
          vertices_Original.agrega(s);
          longitudG++;
        }


        /*Verificar que la K que ingresan sea menor al tama;o de la grafica*/
        if (k>longitudG) {
          System.out.println("\nUn clan debe ser de tamanio menor a la grafica original\n");
          System.exit(1);
        }

        System.out.println("\n Estos son les vertices originales de G1: "+vertices_Original.toString());



        /*Lista vertices tendra los vertice del arbol de forma aleatoria generado por DFS*
          */
        Lista<String> vertices= new Lista<String>();
        vertices=DFSvertices(g);

        /*nd_choice nos ayuda a saber el tama;o del ejemplar a escoger*/
        int nd_choice = ThreadLocalRandom.current().nextInt(k, longitudG + 1);
        System.out.println("\nEl nd_choice aleatorio fue: "+nd_choice+"\n");

        /*Se crea la grafica ejemplar para analizar si cumple los req de un clan*/
        Lista<String> vertices_a_ELiminar= new Lista<String>(); //Lita con todos los vertices del ejemplar
        vertices_a_ELiminar=eligeVetices(vertices,longitudG,nd_choice );



        /*Aqui se crea la grafica ejemplar*/
        Grafica<String> ejemplar = new Grafica<String>();
        ejemplar=eliminaVertices(g,vertices_a_ELiminar);


        String formatGrafica="El ejemplar tiene los vertices: ";
        for (String v : ejemplar ) {
          formatGrafica+= v+ ", ";
        }
        System.out.println(formatGrafica);


        /*Verificar que ejemplar es un clan*/
        boolean esClan=verificarClan(ejemplar,nd_choice-1);

        if (esClan==true) {
          System.out.println("\n******************Clan encontrado**************************\n");

        }else{

          System.out.println("\n---------------Clan no encontrado-------------------------\n");
        }


    }
}
