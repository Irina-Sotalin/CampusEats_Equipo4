package com.mycompany.campuseats;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FiltroPedidosDuplicados implements Filtro {
    private int descartados;

    @Override
    public List<LineaPedido> aplicar(List<LineaPedido> lineas) {
        List<LineaPedido> resultado = new ArrayList<>();
        java.util.Map<String, Integer> primeraAparicion = new java.util.HashMap<>();
        descartados = 0;

        /* Identifica qué aparición del pedido debe conservarse. */
        for (LineaPedido linea : lineas) {
            primeraAparicion.putIfAbsent(linea.idPedido(), linea.indicePedidoFuente());
        }

        /* Conserva todas las líneas de la primera aparición y elimina las demás. */
        for (LineaPedido linea : lineas) {
            if (primeraAparicion.get(linea.idPedido()) == linea.indicePedidoFuente()) {
                resultado.add(linea);
            } else {
                descartados++;
            }
        }
        return resultado;
    }

    @Override
    public int getElementosDescartados() { return descartados; }

    @Override
    public String getNombre() { return "Pedidos duplicados"; }
}
