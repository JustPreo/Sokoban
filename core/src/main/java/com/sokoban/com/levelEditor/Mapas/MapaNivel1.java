package com.sokoban.com.levelEditor.Mapas;

public class MapaNivel1 {
    public static final int filas = 11;
    public static final int columnas = 14;

    public static final int[][] MAPA = {
        {9, 9, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {9, 9, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 1},
        {9, 9, 1, 0, 3, 0, 0, 3, 0, 3, 0, 3, 0, 1},
        {1, 1, 1, 1, 1, 1, 0, 1, 1, 3, 0, 3, 0, 1},
        {1, 2, 2, 0, 0, 1, 0, 1, 0, 0, 3, 0, 1, 1},
        {1, 2, 2, 0, 0, 0, 0, 4, 0, 1, 1, 0, 0, 1},
        {1, 2, 2, 0, 0, 1, 3, 1, 1, 1, 1, 0, 0, 1},
        {1, 2, 2, 0, 0, 1, 0, 3, 0, 0, 3, 0, 0, 1},
        {1, 2, 2, 0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 9, 9},
        {9, 9, 9, 9, 9, 9, 9 ,9, 9, 9, 9, 9, 9, 9}//Esto es lo unico que modifico manual porque no se como hacerlo en codigo XD
    };

    public static final int[][] CAJAS_POS = { {4, 2}, {7, 2}, {9, 2}, {11, 2}, {9, 3}, {11, 3}, {10, 4}, {6, 6}, {7, 7}, {10, 7} };
    public static final int[][] OBJETIVOS_POS = { {1, 4}, {2, 4}, {1, 5}, {2, 5}, {1, 6}, {2, 6}, {1, 7}, {2, 7}, {1, 8}, {2, 8} };
    public static final int[] JUGADOR_POS = {7, 5};
    public static final int CANTIDAD_CAJAS = 10;

}
