/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Tutorialez;

import com.sokoban.com.Base.JuegoBase;

/**
 *
 * @author user
 */
public class Tutorial extends JuegoBase {

    @Override
    public void conseguirCantCajas() {
        cantidadC = 6;

    }

    @Override
    protected void configurarNivel() {
        // Definir mapa específico
        jugadorX = 11; //x jogador
        jugadorY = 2;// y jogador
        // 0 = suelo, 1 = pared |2 = objetivo |3 = cajas | 4 = player
        cambiarMapa(new int[][]{ //Crea un mapa nuevo
           //0   1   2   3   4   5   6   7   8   9  10  11  12  13  14  15  16  17  18 
            {9 , 9 , 9 , 9 , 1 , 1 , 1 , 1 , 1 , 1 , 1 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 },//0
            {9 , 9 , 9 , 9 , 1 , 0 , 0 , 0 , 0 , 0 , 1 , 1 , 1 , 1 , 1 , 1 , 1 , 1 , 1 },//1
            {1 , 1 , 1 , 1 , 1 , 0 , 1 , 1 , 1 , 0 , 1 , 0 , 1 , 1 , 0 , 0 , 2 , 2 , 1 },//2
            {1 , 0 , 3 , 0 , 0 , 3 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 0 , 2 , 2 , 1 },//3
            {1 , 0 , 0 , 0 , 1 , 0 , 1 , 1 , 0 , 1 , 1 , 1 , 1 , 1 , 0 , 0 , 2 , 2 , 1 },//4
            {1 , 1 , 1 , 0 , 1 , 0 , 1 , 1 , 0 , 1 , 9 , 9 , 9 , 1 , 1 , 1 , 1 , 1 , 1 },//5
            {9 , 9 , 1 , 0 , 0 , 3 , 0 , 3 , 0 , 1 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 },//6
            {9 , 9 , 1 , 1 , 1 , 0 , 0 , 3 , 1 , 1 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 },//7
            {9 , 9 , 9 , 9 , 1 , 3 , 0 , 0 , 1 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 },//8
            {9 , 9 , 9 , 9 , 1 , 0 , 0 , 0 , 1 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 },//9
            {9 , 9 , 9 , 9 , 1 , 1 , 1 , 1 , 1 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 },//10
            {9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 , 9 } //11

        });
        //Posicion de cajas
        cajasPos = new int[][]{{2,3},{5,3},{5,6},{7,6},{7,7},{5,8}};//Cajas son las 3

        //Posiciones de objetivos
        objetivosPos = new int[][]{{16,2},{16,3},{16,4},{17,2},{17,3},{17,4}};//Objetivos son los 2

        //Tienen que existir la misma cantidad de cajas que objetivos
        // Posición inicial del jugador
        // Columnas y filas
        COLUMNAS = 19;
        FILAS = 12;
        //Entonces cuando vayas a tocar algo de esto tambien modificas el cambiarMapa
        xyInicial(jugadorX, jugadorY);

    }

    @Override
    protected void xyInicial(int x, int y) {
        jugadorXInicial = x;
        jugadorYInicial = y;

    }

}
