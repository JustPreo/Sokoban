/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Niveles;

import com.sokoban.com.Base.JuegoBase;
import com.sokoban.com.levelEditor.MapaNivel2;

/**
 *
 * @author user
 */
public class Lvl2 extends JuegoBase {

    @Override
    public void conseguirCantCajas() {
        cantidadC = MapaNivel2.CANTIDAD_CAJAS;
    }

    @Override
    protected void configurarNivel() {
        jugadorX = MapaNivel2.JUGADOR_POS[0]; 
        jugadorY = MapaNivel2.JUGADOR_POS[1];
        cambiarMapa(MapaNivel2.MAPA);
        cajasPos = MapaNivel2.CAJAS_POS;
        objetivosPos = MapaNivel2.OBJETIVOS_POS;
        COLUMNAS = MapaNivel2.columnas; //
        FILAS = MapaNivel2.filas; //
        xyInicial(jugadorX,jugadorY);
    }

    @Override
    protected void xyInicial(int x, int y) {
      jugadorXInicial = x;
        jugadorYInicial = y;
    }

    @Override
    protected int obtenerNumeroNivel() {
       return 2;
    }
    
}
