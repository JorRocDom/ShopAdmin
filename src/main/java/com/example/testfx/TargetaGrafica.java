package com.example.testfx;

public class TargetaGrafica extends Component {
    private int memoriaVRAM;
    private double frequencia;
    private int consum;

    public TargetaGrafica(int id, String nom, String marca, double preu, int stock, int memoriaVRAM, double frequencia, int consum) {
        super(id, nom, marca, preu, stock);
        this.memoriaVRAM = memoriaVRAM;
        this.frequencia = frequencia;
        this.consum = consum;
    }

    public int getMemoriaVRAM() { return memoriaVRAM; }
    public double getFrequencia() { return frequencia; }
    public int getConsum() { return consum; }
}
