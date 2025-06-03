package algoritmos;

import java.util.Random;

public class QuickSortSecuencial {

    public static void main(String[] args) {
        int n = 1_000_000; //Tamaño del arreglo
        int[] array = new int[n];

        // Generar valores aleatorios para llenar el arreglo
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            array[i] = rand.nextInt(); // enteros aleatorios (pueden ser positivos y negativos)
        }

        // Medir tiempo de ejecución del quicksort
        long tiempoInicio = System.currentTimeMillis();

        quicksortSecuencial(array, 0, array.length - 1);

        long tiempoFinal = System.currentTimeMillis();

        System.out.println("Tiempo para ordenar " + n + " elementos: " + (tiempoFinal - tiempoInicio) + " ms");

        // verificar que el arreglo está ordenado
        if (isSorted(array)) {
            System.out.println("El arreglo está ordenado correctamente.");
        } else {
            System.out.println("El arreglo NO está ordenado.");
        }
    }

    // Método para ordenar el arreglo con quicksort
    public static void quicksortSecuencial(int[] array, int primero, int ultimo) {
        int i = primero;
        int j = ultimo;
        // Seleccionamos un pivote usando la técnica de mediana de tres
        int medio = (primero + ultimo) / 2;
        int a = array[primero];
        int b = array[medio];
        int c = array[ultimo];
        int pivote = medianaDeTres(a, b, c);

       
        while (i < j) {
            // Avanzar i mientras el valor sea menor que el pivote
            while (array[i] < pivote) {
                i++;
            }
            // Retroceder j mientras el valor sea mayor que el pivote
            while (array[j] > pivote) {
                j--;
            }
            // Intercambiar si los índices no se han cruzado
            if (i <= j) {
            	// Si los índices i y j aún no se cruzaron, significa que:
                // - L[i] es mayor o igual que el pivote (está mal ubicado en la mitad izquierda)
                // - L[j] es menor o igual que el pivote (está mal ubicado en la mitad derecha)
                // Por lo tanto, los intercambiamos para llevar cada valor a su mitad correspondiente.
                int temp = array[j];
                array[j] = array[i];
                array[i] = temp;
                
                //Avanzamos ambos índices para seguir revisando los siguientes elementos.
                i++;
                j--;
            }
        }

     // Si todavía hay elementos en la mitad izquierda,
     // llamamos recursivamente para ordenar esa parte.
        if (primero < j) {
            quicksortSecuencial(array, primero, j);
        }
     // Si todavía hay elementos en la mitad derecha,
     // llamamos recursivamente para ordenar esa parte también.
        if (ultimo > i) {
            quicksortSecuencial(array, i, ultimo);
        }
    }
    
 // Devuelve la mediana de tres valores (mejor selección de pivote que el primero o el último elemento)
    private static int medianaDeTres(int a, int b, int c) {
        if ((a >= b && a <= c) || (a <= b && a >= c)) return a;
        else if ((b >= a && b <= c) || (b <= a && b >= c)) return b;
        else return c;
    }
    
    public static void sort(int[] array) {
        quicksortSecuencial(array, 0, array.length - 1);
    }


    // Método para verificar que el arreglo esté ordenado
    public static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) {
                return false;
            }
        }
        return true;
    }
}
