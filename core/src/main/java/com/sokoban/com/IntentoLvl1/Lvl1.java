/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.IntentoLvl1;

import com.sokoban.com.Base.*;
import com.sokoban.com.Base.IntentoDeMenu.MenuScreen;

/**
 *
 * @author user
 */
public class Lvl1 extends JuegoBase {
    //La base para poder crear un nivel es haciendole override a unos metodos que pondre de primeros con @override

    @Override
    public void conseguirCantCajas() {
        switch (MenuScreen.dificultad) {
            case 1:
                cantidadC = 1;
                break;
            case 2:
                cantidadC = 1;
                break;
            case 3:
                cantidadC = 1;
                break;
        }
    }

    protected void configurarNivel() {
        // Definir mapa específico
        // 0 = suelo, 1 = pared |2 = objetivo |3 = cajas | 4
        cambiarMapa(new int[][]{ //Crea un mapa nuevo
            {1, 1, 1, 1, 1, 1, 1, 1, 1},//0
            {1, 2, 0, 0, 0, 0, 0, 0, 1},//1
            {1, 0, 0, 0, 0, 0, 0, 0, 1},//2
            {1, 0, 0, 0, 0, 0, 0, 0, 1},//3
            {1, 0, 0, 0, 0, 0, 0, 0, 1},//4
            {1, 0, 0, 0, 0, 0, 3, 0, 1},//5
            {1, 0, 0, 0, 0, 0, 0, 0, 1},//6
            {1, 1, 1, 1, 1, 1, 1, 1, 1} //7
           //0  1  2  3  4  5  6  7  8
        });
        //Posicion de cajas
        cajasPos = new int[][]{{2, 2}};//Cajas son las 3

        //Posiciones de objetivos
        objetivosPos = new int[][]{{6, 5}};//Objetivos son los 2
        
        //Tienen que existir la misma cantidad de cajas que objetivos
        
        
        // Posición inicial del jugador
        jugadorX = 2; //x jogador
        jugadorY = 4;// y jogador
        // Columnas y filas
        COLUMNAS = 9; //Lo cambie a 9
        FILAS = 8; //Tomar en cuenta que esto es completamente manual
        //Entonces cuando vayas a tocar algo de esto tambien modificas el cambiarMapa
    }

}
