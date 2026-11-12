package com.mycompany.campuseats;

import java.util.List;

public interface FuenteDatos<T> {
    List<T> leer(String ruta);
}
