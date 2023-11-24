package com.example.pasarela.Models.Utils;

public class numeroAtexto {
    private static final String[] UNIDADES = {"", "una", "dos", "tres", "cuatro", "cinco", "seis", "siete", "ocho", "nueve"};
    private static final String[] DIEZ_A_QUINCE = {"", "once", "doce", "trece", "catorce", "quince"};
    private static final String[] DECENAS = {"", "diez", "veinte", "treinta", "cuarenta", "cincuenta", "sesenta", "setenta", "ochenta", "noventa"};
    private static final String[] CENTENAS = {"", "ciento", "doscientas", "trescientas", "cuatrocientas", "quinientas", "seiscientas", "setecientas", "ochocientas", "novecientas"};


    public static String convertirNumeroATexto(int numero) {
        if (numero < 0 || numero > 2000) {
            return "Número fuera de rango (0-2000)";
        }

        if (numero == 0) {
            return "cero";
        }

        return convertirParte(numero);
    }

    private static String convertirParte(int parte) {
        if (parte <= 9) {
            return UNIDADES[parte];
        } else if (parte <= 15) {
            return DIEZ_A_QUINCE[parte - 10];
        } else if (parte <= 99) {
            int unidad = parte % 10;
            int decena = parte / 10;
            if (unidad == 0) {
                return DECENAS[decena];
            } else {
                return DECENAS[decena] + " y " + UNIDADES[unidad];
            }
        } else if (parte <= 199) {
            return "ciento " + convertirParte(parte % 100);
        } else if (parte <= 999) {
            int centena = parte / 100;
            int resto = parte % 100;
            if (resto == 0) {
                return CENTENAS[centena];
            } else {
                return CENTENAS[centena] + " " + convertirParte(resto);
            }
        } else {
            int millar = parte / 1000;
            int resto = parte % 1000;
            if (resto == 0) {
                return UNIDADES[millar] + " mil";
            } else {
                return UNIDADES[millar] + " mil " + convertirParte(resto);
            }
        }
    }
}
