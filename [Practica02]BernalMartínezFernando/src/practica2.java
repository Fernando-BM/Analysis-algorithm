import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.ArrayList;

public class practica2 {


  private static void uso() {
      System.err.println("El uso es: java -jar practica2.jar <Nombre Archivo>");
      System.exit(1);
  }

  public static void Guarda(ArrayList<Integer> prolist){
    Random rnd = new Random();
    int aleatorio= (int)(rnd.nextDouble() *100);
    try{
      File archivo=new File("Mi_listaOrdenada.txt");
      FileWriter escribir=new FileWriter(archivo,true);
      escribir.write("\n");
      escribir.write("\n");
      for(int n: prolist){
        escribir.write(n+" ");
      }
      escribir.close();
    }catch(Exception e){
      System.out.printf("No pude escribir la lista ordenada en el archivo \"%s\".\n");
      System.exit(1);

    }
  }

  public static ArrayList<Integer> CreaArreglo(String archivo){
      ArrayList<Integer> lista= new ArrayList<Integer>();
      String cadena;
      try {

        FileReader f = new FileReader(archivo);
        BufferedReader b = new BufferedReader(f);

        while((cadena = b.readLine())!=null) {
          String[] parts = cadena.split(" ");
          for(String s: parts){
            int j=Integer.parseInt(s);
            lista.add(j);
          }
        }
      b.close();
      }catch (IOException ioe) {
        System.out.printf("No pude cargar del archivo \"%s\".\n",
        archivo);
        System.exit(1);
      }
      return lista;
  }


  public static Integer busquedaLineal(ArrayList<Integer> lista_busqueda, int k){
    for(int i=0;i<= lista_busqueda.size()-1; i++ ){
      if(lista_busqueda.get(i)==k)
        return i;
    }
    return -1;
  }

  public static Integer busquedaBinaria(ArrayList<Integer> lista_busqueda, int k, int min, int max){

    int prom=(int)(min+max)/2;
    if((min==max && lista_busqueda.get(prom)!=k) || (lista_busqueda.get(max)<k) || (lista_busqueda.get(min)>k))
      return -1;
    if(lista_busqueda.get(prom)==k)
      return prom;
    else{
        if(lista_busqueda.get(prom)>k){
          return busquedaBinaria(lista_busqueda,k,min,prom-1);
        }else{
        return busquedaBinaria(lista_busqueda,k,prom+1, max);
        }
    }
  }

  public static Integer min(int a, int b){
    if(a>b)
      return b;
    return a;
  }

  public static Integer busquedaExponencial(ArrayList<Integer> lista_busqueda,int k, int longitud){
    if(lista_busqueda.size()==0)
      return -1;
    int limite=1;
    while(limite<lista_busqueda.size() && lista_busqueda.get(limite)<k){
      limite= limite*2;
    }
    return busquedaBinaria(lista_busqueda,k, limite/2, min(limite,lista_busqueda.size()-1));
  }

  public static Integer busquedaInterpolacion(ArrayList<Integer> lista_busqueda, int k){
    int valor=k;
    int izq=0;
    int der=lista_busqueda.size()-1;
    int presunto=0;

    while(lista_busqueda.get(der)>=valor && lista_busqueda.get(izq)<valor){
      presunto=((valor-lista_busqueda.get(izq))/(lista_busqueda.get(der)-lista_busqueda.get(izq))*(der-izq))+izq;
      if (valor>lista_busqueda.get(presunto))
        izq=presunto+1;
      else
        if(valor<lista_busqueda.get(presunto))
          der=presunto-1;
        else
          izq=presunto;
    }

    if (lista_busqueda.get(izq)==valor)
      return izq;
    else
      return -1;

  }

  public static void ElegirAlgoritmo(ArrayList<Integer> g, int number, int tam, String s){
    switch(s){
      case "lineal":
        System.out.println("La lista es:");
        System.out.println(g);
        System.out.println("Por Busqueda lineal ");
        System.out.println("El indice del elemento es: "+busquedaLineal(g,number));
        break;
      case "binaria":
        System.out.println("La lista es:");
        System.out.println(g);
        System.out.println("Por Busqueda binaria ");
        System.out.println("El indice del elemento es: "+busquedaBinaria(g,number,0,tam-1));
        break;
      case "exponencial":
        System.out.println("La lista es:");
        System.out.println(g);
        System.out.println("Por Busqueda exponencial ");
        System.out.println("El indice del elemento es: "+busquedaExponencial(g,number,tam));
        break;
      case "interpolacion":
        System.out.println("La lista es:");
        System.out.println(g);
        System.out.println("Por Busqueda interpolacion ");
        System.out.println("El indice del elemento es: "+busquedaInterpolacion(g,number));
        break;
      default:
        System.out.println("La opcion del algoritmo es invalida, vuelve a intentarlo");

    }

  }

/*
*
*EL metodo todosSusDigitosSonDistintos analiza cada numero menor a numero e indica
*si el numero no tiene numero repetidos en cada iteracion
*@param numero es el numero que se requiere para que sea nuestro punto de partida
*@return boolean que indica si todos os digitos del numero son distintos*/
  private static boolean todosSusDigitosSonDistintos(int numero) {
     int numMask = 0;
     int numDigits = (int) Math.ceil(Math.log10(numero+1));
     for (int digitIdx = 0; digitIdx < numDigits; digitIdx++) {
         int curDigit = (int)(numero / Math.pow(10,digitIdx)) % 10;
         int digitMask = (int)Math.pow(2, curDigit);
         if ((numMask & digitMask) > 0) return false;
         numMask = numMask | digitMask;
     }
     return true;
 }


 /*
 *
 *EL metodo ListaOrdenanda en cada indice de la lista va a;adiendo los elementos
 *que no tienen elementos con digitos en comun
 *@param n la longitud de la lista
 *@return Arraylist lista con todos los elementos que no tienen digitos en comun*/


 public static ArrayList<Integer> ListaOrdenanda(int n){
   ArrayList<Integer> prolist= new ArrayList<Integer>();
   for (int i=1; i<n; i++) {
         if (todosSusDigitosSonDistintos(i)) {
             prolist.add(i);
            }
  }
  return prolist;
}

/*
*
*EL metodo ListaPar itera la lista e ilimina a los elementos con indice impar
*@return ArrayList regresa la lista coon los elementos de indice par*/


public static ArrayList<Integer> ListaSinPar(ArrayList<Integer> prolist){
  for(int i=0; i<prolist.size(); i+=2){
    prolist.remove(i);
  }
  return prolist;
}




  public static void main(String[] args) {

    if (args.length != 3)
          uso();

    String nombreArchivo = args[0];
    Integer number=Integer.parseInt(args[1]);
    String algoritmo=args[2];

    ArrayList<Integer> g= new ArrayList<Integer>();
    g = CreaArreglo(nombreArchivo);


    Random rnd = new Random();
    int aleatorio= (int)(rnd.nextDouble() *1000 + 100);
    ArrayList<Integer> prolist= new ArrayList<Integer>();
    prolist = ListaSinPar(ListaOrdenanda(aleatorio));
    Guarda(prolist);


    int tam= g.size();
    ElegirAlgoritmo(g,number, tam,algoritmo);

  }
}
