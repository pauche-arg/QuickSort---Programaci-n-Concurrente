package benchmark;

import java.util.Arrays;
import java.util.Random;

import algoritmos.*;

public class QuickSortBenchmark {

    // Tamaños de los arrays a probar
    private static final int[] TAMANIO = {10000, 100000, 1000000, 10000000, 100000000};

    public static void main(String[] args) {
        // Matriz para guardar los tiempos de ejecución
        // [fila][0=Secuencial Ordenado, 1=Concurrente Ordenado, 2=Secuencial Desordenado, 3=Concurrente Desordenado]
        long[][] tiempos = new long[TAMANIO.length][4];

        for (int index = 0; index < TAMANIO.length; index++) {
            int size = TAMANIO[index];

            // Crear arrays para la prueba
            int[] randomArray = generarArrayRandom(size);  
            int[] sortedArray = generarArrayOrdenado(size); 

            System.out.println("\n== Tamaño de array: " + size + " ==");

            // Ejecutar cada versión del algoritmo y guardar los tiempos
            tiempos[index][0] = test("Secuencial (Ordenado)", sortedArray, QuickSortSecuencial::sort);
            tiempos[index][1] = test("Concurrente (Ordenado)", sortedArray, QuickSortConcurrente::sort);
            tiempos[index][2] = test("Secuencial (Desordenado)", randomArray, QuickSortSecuencial::sort);
            tiempos[index][3] = test("Concurrente (Desordenado)", randomArray, QuickSortConcurrente::sort);
        }

        // Imprimir tabla resumen con todos los tiempos
        imprimirTabla(tiempos);
    }

    // Genera un array de enteros aleatorios
    private static int[] generarArrayRandom(int size) {
        Random rand = new Random(1234); // Semilla fija para reproducibilidad
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt();
        }
        return array;
    }

    // Genera un array ordenado de manera ascendente
    private static int[] generarArrayOrdenado(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    // Ejecuta la función de ordenamiento, mide el tiempo y lo retorna
    private static long test(String etiqueta, int[] original, Sorter sorter) {
        int[] copy = Arrays.copyOf(original, original.length); // Copia para no modificar el original
        long start = System.currentTimeMillis(); // Tiempo de inicio
        try {
            sorter.sort(copy); // Ejecuta el algoritmo
        } catch (Exception e) {
            // Si ocurre una excepción, imprimir mensaje y retornar -1
            System.out.printf("%-30s : ERROR - %s%n", etiqueta, e.getMessage());
            return -1;
        }
        long end = System.currentTimeMillis(); // Tiempo de finalización
        long duracion = end - start;
        System.out.printf("%-30s : %4d ms%n", etiqueta, duracion);
        return duracion;
    }

    // Imprime la tabla resumen con los tiempos
    private static void imprimirTabla(long[][] tiempos) {
        System.out.println("\n+----------+-----------------------+------------------------+------------------------+-------------------------+");
        System.out.println("| Tamaño   | Secuencial (Ordenado) | Concurrente (Ordenado) | Secuencial (Desord.)   | Concurrente (Desord.)   |");
        System.out.println("+----------+-----------------------+------------------------+------------------------+-------------------------+");

        // Recorre los tamaños de array y muestra los tiempos correspondientes
        for (int i = 0; i < TAMANIO.length; i++) {
            System.out.printf("| %-8d | %-21s | %-22s | %-22s | %-23s |\n",
                    TAMANIO[i],
                    formato(tiempos[i][0]),
                    formato(tiempos[i][1]),
                    formato(tiempos[i][2]),
                    formato(tiempos[i][3]));
        }

        System.out.println("+----------+-----------------------+------------------------+------------------------+-------------------------+");
    }

    // Formatea los tiempos: si es invalido, muestra "ERROR"
    private static String formato(long tiempo) {
        return tiempo == -1 ? "ERROR" : tiempo + " ms";
    }

    // Interfaz funcional para usar referencias a métodos de ordenamiento
    interface Sorter {
        void sort(int[] array) throws Exception;
    }
}