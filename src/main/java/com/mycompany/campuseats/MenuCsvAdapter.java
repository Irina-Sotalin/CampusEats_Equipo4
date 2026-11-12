package com.mycompany.campuseats;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MenuCsvAdapter implements FuenteDatos<Plato> {
    @Override
    public List<Plato> leer(String ruta) {
        List<Plato> platos = new ArrayList<>();
        try {
            List<String> filas = Files.readAllLines(Path.of(ruta));
            for (int i = 1; i < filas.size(); i++) {
                if (filas.get(i).isBlank()) continue;
                String[] p = filas.get(i).split(",", -1);
                platos.add(new Plato(
                    p[0].trim(), p[1].trim(), p[2].trim(),
                    new BigDecimal(p[3].trim()), p[4].trim().toUpperCase()
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo menu_admin.csv: " + e.getMessage(), e);
        }
        return platos;
    }
}
