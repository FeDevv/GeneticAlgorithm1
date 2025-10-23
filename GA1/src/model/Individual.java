package model;

import java.util.List;

public class Individual {
    private List<Point> chromosomes;
    private double fitness;

    //costruttore parziale per inizializzare l'individuo
    public Individual(List<Point> chromosomes) {
        this.chromosomes = chromosomes;
        this.fitness = Double.NEGATIVE_INFINITY;
    }

    //costruttore parziale per aggiungere fitness
    public Individual(double fitness) { this.fitness = fitness; }

    //getter per prendere i cromosomi
    public List<Point> getChromosomes() { return this.chromosomes; }
    //getter per prendere la fitness
    public double getFitness() { return this.fitness; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Individuo\n");
        for (Point p : chromosomes) {
            sb.append(p.toString()).append("\n");
        }
        return sb.toString();
    }

}
