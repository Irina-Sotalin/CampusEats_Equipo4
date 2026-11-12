package com.mycompany.campuseats;

import java.util.List;

public interface Filtro {
    List<LineaPedido> aplicar(List<LineaPedido> lineas);
    int getElementosDescartados();
    String getNombre();
}
