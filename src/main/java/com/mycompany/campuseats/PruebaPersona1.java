package com.mycompany.campuseats;

import java.util.List;

public class PruebaPersona1 {
    public static void main(String[] args) {
        System.out.println("PRUEBA DE LECTURA DE ARCHIVOS (PERSONA 1)");

        try {
            // 1. Probar Menú CSV
            System.out.println("\nLeyendo Menu");
            FuenteDatos<?> fuenteMenu = FabricaFuentes.crear("menu_csv");
            List<?> menu = fuenteMenu.leer("datos/menu_admin.csv");
            System.out.println("Exito: Se leyeron " + menu.size() + " platos.");
            System.out.println("Ejemplo del primero: " + menu.get(0));

            // 2. Probar Pasarela CSV
            System.out.println("\nLeyendo Pagos...");
            FuenteDatos<?> fuentePagos = FabricaFuentes.crear("pasarela_csv");
            List<?> pagos = fuentePagos.leer("datos/pagos_pasarela.csv");
            System.out.println("Exito: Se leyeron " + pagos.size() + " pagos.");
            System.out.println("Ejemplo del primero: " + pagos.get(0));

            // 3. Probar Pedidos JSON
            System.out.println("\nLeyendo Pedidos App...");
            FuenteDatos<?> fuentePedidos = FabricaFuentes.crear("app_json");
            List<?> pedidos = fuentePedidos.leer("datos/pedidos_app.json");
            System.out.println("Exito: Se leyeron " + pedidos.size() + " lineas de pedido.");
            System.out.println("Ejemplo de la primera linea: " + pedidos.get(0));

        } catch (Exception e) {
            System.err.println("Error durante la lectura: " + e.getMessage());
            e.printStackTrace();
        }
    }
}