# GeneticAlgorithm1
First implementation of a genetic algorithm.

This project will implement a genetic algorithm that will try to find a good solution to a 2D packing problem 

[given a domain and a set of points with some constraints, find a placing that comply with all the constraints and the added distance from one point to all the other is maximised]

Points could be seen as disks, and we do not want to have overlapping disks, moreover we want the disks to be as far from each other as possible.

Versione funzionante. Per ora i punti hanno tutti raggio uguale, impostato manualmente all'avvio del programma. Tutte le funzioni sono comunque in grado di lavorare su punti con diversi raggi. 
Va a tal riguardo modificata l'inizializzazione del problema, in particolare l'EvolutionEngine : questo dovrà ricevere una serie di valori del tipo [(#piante, valore raggio), (#piante, valore raggio), ... (#piante, valore raggio)]
sarà allora in grado di definire la dimensione dell'individuo (ora data come input da tastiera) sommando tutti i valori #piante e dovrà generare una prima popolazione dove ogni pianta ha il suo relativo raggio.
Cio si puo semplicemente fare, andando a chiamare in un doppio ciclo for (ciclo esterno : tante volte quante sono le coppie (#piante, raggio) ; ciclo interno dato da #piante) una funzione che crea un punto con il dato raggio.

Tutte le altre funzioni sono gia implementate per funzionare con punti con raggi diversi.
