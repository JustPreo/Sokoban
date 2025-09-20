/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Niveles;

import com.sokoban.com.Base.JuegoBase;
import com.sokoban.com.levelEditor.MapaNivel7;

/**
 *
 * @author user
 */
public class Lvl7 extends JuegoBase {
     @Override
    public void conseguirCantCajas() {
        cantidadC = MapaNivel7.CANTIDAD_CAJAS;
    }

    @Override
    protected void configurarNivel() {
        jugadorX = MapaNivel7.JUGADOR_POS[0]; 
        jugadorY = MapaNivel7.JUGADOR_POS[1];
        cambiarMapa(MapaNivel7.MAPA);
        cajasPos = MapaNivel7.CAJAS_POS;
        objetivosPos = MapaNivel7.OBJETIVOS_POS;
        COLUMNAS = MapaNivel7.columnas; //
        FILAS = MapaNivel7.filas; //
        xyInicial(jugadorX,jugadorY);
    }

    @Override
    protected void xyInicial(int x, int y) {
      jugadorXInicial = x;
        jugadorYInicial = y;
    }

    @Override
    protected int obtenerNumeroNivel() {
       return 7;
    }    
    
}
