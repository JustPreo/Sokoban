/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Niveles;

import com.sokoban.com.Base.JuegoBase;
import com.sokoban.com.levelEditor.MapaNivel4;

/**
 *
 * @author user
 */
public class Lvl4 extends JuegoBase {
    @Override
    public void conseguirCantCajas() {
        cantidadC = MapaNivel4.CANTIDAD_CAJAS;
    }

    @Override
    protected void configurarNivel() {
        jugadorX = MapaNivel4.JUGADOR_POS[0]; 
        jugadorY = MapaNivel4.JUGADOR_POS[1];
        cambiarMapa(MapaNivel4.MAPA);
        cajasPos = MapaNivel4.CAJAS_POS;
        objetivosPos = MapaNivel4.OBJETIVOS_POS;
        COLUMNAS = MapaNivel4.columnas; //
        FILAS = MapaNivel4.filas; //
        xyInicial(jugadorX,jugadorY);
    }

    @Override
    protected void xyInicial(int x, int y) {
      jugadorXInicial = x;
        jugadorYInicial = y;
    }

    @Override
    protected int obtenerNumeroNivel() {
       return 4;
    }    
    
}
