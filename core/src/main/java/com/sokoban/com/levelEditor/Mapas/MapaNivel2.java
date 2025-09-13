package com.sokoban.com.levelEditor.Mapas;

public class MapaNivel2 {
    public static final int filas = 11;
    public static final int columnas = 17;

    public static final int[][] MAPA = {
        {1, 1, 1, 1, 1, 1, 1, 1, 9, 9, 9, 9, 9, 9, 9, 9, 9},
        {1, 2, 2, 2, 2, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 2, 2, 2, 0, 0, 0, 0, 3, 0, 0, 3, 0, 0, 0, 1},
        {1, 2, 2, 2, 2, 0, 0, 1, 1, 0, 3, 0, 0, 3, 0, 0, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 3, 0, 1, 0, 1, 1, 1},
        {9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 3, 0, 3, 0, 1, 9, 9},
        {9, 9, 9, 9, 9, 9, 9, 9, 1, 0, 3, 0, 0, 3, 1, 9, 9},
        {9, 9, 9, 9, 9, 9, 9, 9, 1, 0, 3, 1, 3, 0, 1, 1, 9},
        {9, 9, 9, 9, 9, 9, 9, 9, 1, 0, 0, 0, 0, 0, 4, 1, 9},
        {9, 9, 9, 9, 9, 9, 9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 9},
        {9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9},
    };

    public static final int[][] CAJAS_POS = { {9, 2}, {12, 2}, {10, 3}, {13, 3}, {10, 4}, {10, 5}, {12, 5}, {10, 6}, {13, 6}, {10, 7}, {12, 7} };
    public static final int[][] OBJETIVOS_POS = { {1, 1}, {2, 1}, {3, 1}, {4, 1}, {2, 2}, {3, 2}, {4, 2}, {1, 3}, {2, 3}, {3, 3}, {4, 3} };
    public static final int[] JUGADOR_POS = {14, 8};
    public static final int CANTIDAD_CAJAS = 11;

}
