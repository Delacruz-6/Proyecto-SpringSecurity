package realEstate.salesianos.triana.dam.realEstate.users.models;

public enum UserRole {

    ADMIN("GESTOR"), PROPIETARIO("GESTOR"), GESTOR("GESTOR");

    private String valor;

    private UserRole (String valor) {this.valor=valor;}
    public String getValor(){return valor;}
    public void setValor(String valor) { this.valor=valor;}
}
