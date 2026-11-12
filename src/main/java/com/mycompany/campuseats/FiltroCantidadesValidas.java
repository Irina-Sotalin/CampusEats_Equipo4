package com.mycompany.campuseats;

import java.util.ArrayList;
import java.util.List;

public class FiltroCantidadesValidas implements Filtro {
    private int descartados;

    @Override
    public List<LineaPedido> aplicar(List<LineaPedido> lineas) {
        List<LineaPedido> resultado = new ArrayList<>();
        descartados = 0;
        for (LineaPedido linea : lineas) {
            if (linea.cantidad() > 0) resultado.add(linea);
            else descartados++;
        }
        return resultado;
    }

    @Override
    public int getElementosDescartados() { return descartados; }

    @Override
    public String getNombre() { return "Cantidades inválidas"; }
}
