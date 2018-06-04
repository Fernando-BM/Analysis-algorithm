import math
import sys

#def que sirve para la lectura del archivo
def lectura(archivo):
    'Lectura de un archivo'
    lista=[]
    archivo= open(archivo,"r")
    for linea in archivo:
        linea2=linea.split(" ")
        for elem in linea2:
            if(elem is not "\n"):
                entero=int(elem)
                lista.append(entero)
    return lista


#def counting_sort, ordenar un lista, solo de numero positivos.
def counting_sort(array, maxval):
    n = len(array)
    m = maxval + 1
    cuenta = [0] * m
    print("El conteo de cada numero es:")

    # inicializar el array con 0
    for a in array:
        cuenta[a] += 1
        print cuenta
    print(cuenta)

    # cuenta occurences
    i = 0
    for a in range(m):
        for c in range(cuenta[a]): #'cuenta[a]' copias de'a'
            array[i] = a
            i += 1
    print("La lista ordenada es:")
    return array


def radix_sort(array):
    len_array = len(array)
    modulo = 10
    div = 1
    while True:
        # arreglo vacio, [[] for i in range(10)]
        list_aux = [[], [], [], [], [], [], [], [], [], []]
        for valor in array:
            least_digit = valor % modulo
            least_digit /= div
            list_aux[least_digit].append(valor)
        modulo = modulo * 10
        div = div * 10


        if len(list_aux[0]) == len_array:
            return list_aux[0]

        print list_aux
        array = []
        rd_list_append = array.append
        for x in list_aux:
            for y in x:
                rd_list_append(y)




tam = 10

def sort(array, bucketSize=tam):
  if len(array) == 0:
    return array

  # Determina el valor maximo y el vaor minimo
  minValue = array[0]
  maxValue = array[0]
  for i in range(1, len(array)):
    if array[i] < minValue:
      minValue = array[i]
    elif array[i] > maxValue:
      maxValue = array[i]

  # Inicializar bucket
  bucketCount = int(math.floor((maxValue - minValue) / bucketSize) + 1)
  buckets = []
  for i in range(0, bucketCount):
    buckets.append([])

  # Distribuir las entradasen el bucket
  for i in range(0, len(array)):
    buckets[int(math.floor((array[i] - minValue) / bucketSize))].append(array[i])

  # ordenar cada bucket
  array = []
  for i in range(0, len(buckets)):
    radix_sort(buckets[i])
    for j in range(0, len(buckets[i])):
      array.append(buckets[i][j])

  print("La lista ordenada es:")
  return array



#def para encontrar el maximo elemento
def Max(list):
    if len(list) == 1:
        return list[0]
    else:
        m = Max(list[1:])
        return m if m > list[0] else list[0]


#def para e menu de opciones
def elige(opcion,array,x):
    if(opcion=="counting"):
        print (counting_sort(array,x))
    elif(opcion=="radix"):
        print (radix_sort(array))
    elif(opcion=="bucket"):
        print (sort(array))
    else:
        print("opcion invalida")




#main de la clase
a=sys.argv
archivo= lectura(a[1])
algoritmo=a[2]
maxi=Max(archivo)
elige(algoritmo,archivo,maxi)
