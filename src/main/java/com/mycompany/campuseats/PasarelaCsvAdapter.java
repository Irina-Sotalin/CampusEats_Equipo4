package com.mycompany.campuseats;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PasarelaCsvAdapter implements FuenteDatos<Pago> {
    private static final DateTimeFormatter FORMATO =
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public List<Pago> leer(String ruta) {
        List<Pago> pagos = new ArrayList<>();
        try {
            List<String> filas = Files.readAllLines(Path.of(ruta));
            for (int i = 1; i < filas.size(); i++) {
                if (filas.get(i).isBlank()) continue;
                String[] p = filas.get(i).split(";", -1);
                BigDecimal monto = new BigDecimal(p[1].trim().replace(",", "."));
                LocalDateTime fecha = LocalDateTime.parse(p[5].trim(), FORMATO);
                pagos.add(new Pago(p[0].trim(), fecha, monto, p[2].trim().toUpperCase()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error leyendo pagos_pasarela.csv: " + e.getMessage(), e);
        }
        return pagos;
    }
}
