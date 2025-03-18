package com.example.testfx;

public class DiscDur extends Component {
    private int capacitat;
    private String tipus;

    public DiscDur(int id, String nom, String marca, double preu, int stock, int capacitat, String tipus) {
        super(id, nom, marca, preu, stock);
        this.capacitat = capacitat;
        this.tipus = tipus;
    }

    public int getCapacitat() { return capacitat; }
    public String getTipus() { return tipus; }
}
