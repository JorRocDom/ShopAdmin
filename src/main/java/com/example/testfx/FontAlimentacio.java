package com.example.testfx;

public class FontAlimentacio extends Component {
    private int potencia;          // Potencia en vatios (W)
    private String certificacion;  // Certificación 80 Plus (Bronze, Silver, Gold, Platinum, Titanium)
    private boolean modular;       // Indica si es modular o no

    public FontAlimentacio(int id, String nom, String marca, double preu, int stock, int potencia, String certificacion, boolean modular) {
        super(id, nom, marca, preu, stock);
        this.potencia = potencia;
        this.certificacion = certificacion;
        this.modular = modular;
    }

    public int getPotencia() { return potencia; }
    public String getCertificacion() { return certificacion; }
    public boolean isModular() { return modular; }
}

