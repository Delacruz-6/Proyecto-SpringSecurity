# Real Estate V.2
## _Proyecto 2ª DAM (Acceso a datos / Programación de servicios y procesos )_

[![N|Solid](logo.png)](https://triana.salesianos.edu/colegio//nsolid)




>## Funcionalidad
  Crear una aplicación de la gestión y visualización de los datos referentes a un conjunto de inmobiliarias y sus asociacines.
  Creando peticiones (usando API REST) desde las entidades, asociaciones y aplicando cambios en la forma de mostrar su estructura a través de Dtos para luego mostrar y modificar esos datos aplicando seguridad con Spring Security
  

>##Instrucciones de arranque
  Para ejecutar esta aplicación tras clonar el repositorio, debes ejecutar en la consola de tu IDE, "spring-boot:run", con la configuración de Maven.


>## Estructura de paquetes
| Paquete | URL |
| ------ | ------ |
| Controllers | [RealEstate/Controllers](https://github.com/miguelcamposedu/g4-realstate-backend/tree/master/realEstate/src/main/java/realEstate/salesianos/triana/dam/realEstate/controllers) |
| DTOs | [RealEstate/DTOs](https://github.com/Delacruz-6/Proyecto-SpringSecurity/tree/main/realEstate/src/main/java/realEstate/salesianos/triana/dam/realEstate/dtos) |
| Models | [RealEstate/Models](https://github.com/Delacruz-6/Proyecto-SpringSecurity/tree/main/realEstate/src/main/java/realEstate/salesianos/triana/dam/realEstate/models) |
| Repositories | [RealEstate/Repositories](https://github.com/Delacruz-6/Proyecto-SpringSecurity/tree/main/realEstate/src/main/java/realEstate/salesianos/triana/dam/realEstate/repositories)
| Services | [RealEstate/Services](https://github.com/Delacruz-6/Proyecto-SpringSecurity/tree/main/realEstate/src/main/java/realEstate/salesianos/triana/dam/realEstate/services)
| Util | [RealEstate/Utils](https://github.com/Delacruz-6/Proyecto-SpringSecurity/tree/main/realEstate/src/main/java/realEstate/salesianos/triana/dam/realEstate/util)
| Config | [RealEstate/Config](https://github.com/Delacruz-6/Proyecto-SpringSecurity/tree/main/realEstate/src/main/java/realEstate/salesianos/triana/dam/realEstate/config)
| Security | [RealEstate/Security](https://github.com/Delacruz-6/Proyecto-SpringSecurity/tree/main/realEstate/src/main/java/realEstate/salesianos/triana/dam/realEstate/Security)

>## Entidades
  Contamos con 5 entidades que son:
  - Inmobiliaria
  - Vivienda
  - Usuario
  - Interesa

  
>## Asociaciones
## ManytoMany con atributos extra ( Vivienda -> Interesa <- Usuario )

#### Vivienda
```sh
@Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vivienda")
    private List<Interesa> intereses = new ArrayList<>();
```
#### Usuario (interesado)
```sh
@Builder.Default
    @OneToMany(mappedBy = "interesado")
    private List<Interesa> intereses = new ArrayList<>();
```
#### Interesa
```sh
@Builder.Default
    @EmbeddedId
    private InteresaPK id = new InteresaPK();
```
#### InteresaPK
La clase interesaPK es la clave compuesta y a su vez foranea, que através de la anotacion @Embeddable y mappedBy conseguimos rescatar los identificadores de las tablas de la asociación requeridas
```sh
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InteresaPK implements Serializable {

    private Long interesado_id;

    private Long vivienda_id;

}
```
## ManyToOne bidireccional (Inmobiliaria -> Vivienda)
#### Inmobiliaria
```sh
 @OneToMany(mappedBy = "inmobiliaria", fetch = FetchType.LAZY)
    private List<Vivienda> viviendas = new ArrayList<>();   
```
#### Vivienda
```sh
@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inmobiliaria_id", foreignKey = @ForeignKey(name = "FK_VIVIENDA_INMOBILIARIA"))
    private Inmobiliaria inmobiliaria;
```

## ManyToOne bidireccional (Propietario -> Vivienda)
#### Usuario (propietario)
```sh
@OneToMany(mappedBy = "propietario")
    private List<Vivienda> viviendas = new ArrayList<>();
```
#### Vivienda
```sh
@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "propietario_id", foreignKey = @ForeignKey(name = "FK_VIVIENDA_PROPIETARIO"))
    private Propietario propietario;
```

```
