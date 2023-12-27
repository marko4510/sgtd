package com.example.pasarela.Models.Utils;

public class GenerarCodigo {
    
    public static String generarCodigo(String valorIncrementable) {
        // Definir la longitud máxima del código
        int longitudMaxima = 4;

        // Calcular cuántos ceros se deben agregar
        int cerosFaltantes = Math.max(0, longitudMaxima - valorIncrementable.length());

        // Construir el código con ceros a la izquierda
        StringBuilder codigo = new StringBuilder();
        for (int i = 0; i < cerosFaltantes; i++) {
            codigo.append('0');
        }
        codigo.append(valorIncrementable);

        return codigo.toString();
    }

 
}
