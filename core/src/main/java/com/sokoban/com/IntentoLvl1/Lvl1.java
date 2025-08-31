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
                cantidadC = 2;
                break;
            case 3:
                cantidadC = 3;
                break;
        }
    }

    protected void configurarNivel() {
        // Definir mapa específico
        cambiarMapa(new int[][]{ //Crea un mapa nuevo
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        });
        //Posicion de cajas
        cajasPos = new int[][]{{2, 2}, {6, 3}, {2, 5}};

        //Posiciones de objetivos
        objetivosPos = new int[][]{{1, 1}, {7, 3}, {8, 6}};
        
        
        // Posición inicial del jugador
        jugadorX = 2; //x jogador
        jugadorY = 4;// y jogador
        // Columnas y filas
        COLUMNAS = 10; //
        FILAS = 8; //Tomar en cuenta que esto es completamente manual
        //Entonces cuando vayas a tocar algo de esto tambien modificas el cambiarMapa
    }

}
