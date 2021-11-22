package realEstate.salesianos.triana.dam.realEstate.models;

public enum Tipo {
    ALQUILER("ALQUILER"), VENTA("VENTA"), OBRA_NUEVA("OBRA_NUEVA");

    private String valor;

    private Tipo (String valor) {this.valor=valor;}
    public String getValor(){return valor;}
    public void setValor(String valor) { this.valor=valor;}
}


