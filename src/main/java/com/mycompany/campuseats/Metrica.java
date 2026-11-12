package com.mycompany.campuseats;

import java.util.List;

public interface Metrica {
    String nombre();
    String calcular(List<LineaPedido> datos);
}
