package com.example.testfx;

public class Component {
    private int id;
    private String nom;
    private String marca;
    private double preu;
    private int stock;

    public Component(int id, String nom, String marca, double preu, int stock) {
        this.id = id;
        this.nom = nom;
        this.marca = marca;
        this.preu = preu;
        this.stock = stock;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void mostrarInformacio() {
        System.out.println(nom + " - " + marca + " | Precio: " + preu + "€ | Stock: " + stock);
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getMarca() { return marca; }
    public Double getPreu() { return preu; }
    public int getStock() { return stock; }

    public void setNom(String nom) { this.nom = nom; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setPreu(Double preu) { this.preu = preu; }
    public void setStock(int stock) { this.stock = stock; }
}
