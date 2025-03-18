package com.example.testfx;

public class Processador extends Component {
    private double frequencia;
    private int nuclis;
    private String socket;

    public Processador(int id, String nom, String marca, double preu, int stock, double frequencia, int nuclis, String socket) {
        super(id, nom, marca, preu, stock);
        this.frequencia = frequencia;
        this.nuclis = nuclis;
        this.socket = socket;
    }

    public double getFrequencia() { return frequencia; }
    public int getNuclis() { return nuclis; }
    public String getSocket() { return socket; }
}
