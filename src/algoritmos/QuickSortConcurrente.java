package algoritmos;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.RejectedExecutionException;

public class QuickSortConcurrente {

    // Umbral mínimo para aplicar concurrencia: si la partición es menor que esto, se hace de forma secuencial
    private static final int UMBRAL_CONCURRENTE = 50_000;

    // Número de hilos disponibles en el sistema (núcleos del procesador)
    private static final int NUM_HILOS = Runtime.getRuntime().availableProcessors();

    // Pool de hilos con cantidad fija, para ejecutar tareas concurrentes
    private static final ExecutorService ejecutor = Executors.newFixedThreadPool(NUM_HILOS);

    public static void main(String[] args) throws InterruptedException {
        int n = 5_000_000; // Tamaño del arreglo a ordenar
        int[] array = new int[n];

        // Rellenamos el arreglo con números aleatorios
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            array[i] = rand.nextInt();
        }

        // Medimos el tiempo de inicio
        long tiempoInicio = System.currentTimeMillis();

        // Llamada inicial al algoritmo quicksort concurrente
        quicksortConcurrente(array, 0, array.length - 1);

        // Cerramos el pool de hilos y esperamos a que terminen todas las tareas
        ejecutor.shutdown();
        ejecutor.awaitTermination(1, TimeUnit.HOURS);

        // Medimos el tiempo de finalización
        long tiempoFin = System.currentTimeMillis();

        // Imprimimos el tiempo total de ejecución
        System.out.println("Tiempo para ordenar " + n + " elementos (concurrente): " + (tiempoFin - tiempoInicio) + " ms");

        // Verificamos si el arreglo quedó correctamente ordenado
        if (estaOrdenado(array)) {
            System.out.println("El arreglo está ordenado correctamente.");
        } else {
            System.out.println("El arreglo NO está ordenado.");
        }
    }

    // Método principal del algoritmo Quicksort con soporte para concurrencia
    public static void quicksortConcurrente(int[] array, int primero, int ultimo) {
        if (primero >= ultimo) {
            return; // Caso base: subarreglo vacío o con un solo elemento
        }

        int izquierda = primero;
        int derecha = ultimo;

        // Seleccionamos un pivote usando la técnica de mediana de tres
        int medio = (primero + ultimo) / 2;
        int a = array[primero];
        int b = array[medio];
        int c = array[ultimo];
        int pivote = medianaDeTres(a, b, c);

        // Particionamos el arreglo alrededor del pivote
        while (izquierda <= derecha) {
            while (array[izquierda] < pivote) {
                izquierda++;
            }
            while (array[derecha] > pivote) {
                derecha--;
            }
            if (izquierda <= derecha) {
                // Intercambiamos elementos que están en el lado incorrecto
                int temp = array[izquierda];
                array[izquierda] = array[derecha];
                array[derecha] = temp;
                izquierda++;
                derecha--;
            }
        }

        // Procesamos la sublista izquierda (antes del pivote)
        if (derecha - primero > UMBRAL_CONCURRENTE) {
            // Si la sublista es suficientemente grande, lanzamos una tarea concurrente
            final int izquierdaPrimero = primero;
            final int izquierdaUltimo = derecha;
            try {
                ejecutor.execute(() -> quicksortConcurrente(array, izquierdaPrimero, izquierdaUltimo));
            } catch (RejectedExecutionException e) {
                // Si el ejecutor está saturado, lo hacemos secuencialmente
                quicksortConcurrente(array, izquierdaPrimero, izquierdaUltimo);
            }
        } else if (primero < derecha) {
            // Si la sublista es pequeña, se ordena secuencialmente
            quicksortConcurrente(array, primero, derecha);
        }

        // Procesamos la sublista derecha (después del pivote)
        if (ultimo - izquierda > UMBRAL_CONCURRENTE) {
            // Si la sublista es suficientemente grande, usamos concurrencia
            final int derechaPrimero = izquierda;
            final int derechaUltimo = ultimo;
            try {
                ejecutor.execute(() -> quicksortConcurrente(array, derechaPrimero, derechaUltimo));
            } catch (RejectedExecutionException e) {
                quicksortConcurrente(array, derechaPrimero, derechaUltimo);
            }
        } else if (izquierda < ultimo) {
            // Ordenamiento secuencial si la partición es pequeña
            quicksortConcurrente(array, izquierda, ultimo);
        }
    }

    // Devuelve la mediana de tres valores (mejor selección de pivote que el primero o el último elemento)
    private static int medianaDeTres(int a, int b, int c) {
        if ((a >= b && a <= c) || (a <= b && a >= c)) return a;
        else if ((b >= a && b <= c) || (b <= a && b >= c)) return b;
        else return c;
    }

    // Método auxiliar para ordenar el arreglo completo (por si se usa desde otro lugar)
    public static void sort(int[] arreglo) throws InterruptedException {
        quicksortConcurrente(arreglo, 0, arreglo.length - 1);
        ejecutor.shutdown();
        ejecutor.awaitTermination(1, TimeUnit.HOURS);
    }

    // Verifica que el arreglo esté ordenado de forma ascendente
    public static boolean estaOrdenado(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) {
                return false; // Encontramos un elemento desordenado
            }
        }
        return true;
    }
}
