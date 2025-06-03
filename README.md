# QuickSort - Programación Concurrente
Si se corren las clases QuickSortSecuencial o QuickSortConcurrente se ejecutan por determinado con un arreglo de 1 millón de elementos.
La variable UMBRAL_CONCURRENTE tiene de valor determinado 50.000; si una partición tiene 50.000 elementos o menos, se corre de manera secuencial.

Para testear ambos algoritmos secuenciales y concurrentes, con arreglos ordenados y desordenados, se puede utilizar QuickSortBenchmark que se encuentra en el package de benchmark.
