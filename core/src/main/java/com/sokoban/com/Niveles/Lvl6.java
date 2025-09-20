/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Niveles;

import com.sokoban.com.Base.JuegoBase;
import com.sokoban.com.levelEditor.MapaNivel6;

/**
 *
 * @author user
 */
public class Lvl6 extends JuegoBase{
     @Override
    public void conseguirCantCajas() {
        cantidadC = MapaNivel6.CANTIDAD_CAJAS;
    }

    @Override
    protected void configurarNivel() {
        jugadorX = MapaNivel6.JUGADOR_POS[0];
        jugadorY = MapaNivel6.JUGADOR_POS[1];
        cambiarMapa(MapaNivel6.MAPA);
        cajasPos = MapaNivel6.CAJAS_POS;
        objetivosPos = MapaNivel6.OBJETIVOS_POS;
        COLUMNAS = MapaNivel6.columnas; //
        FILAS = MapaNivel6.filas; //
        xyInicial(jugadorX, jugadorY);
    }

    @Override
    protected void xyInicial(int x, int y) {
        jugadorXInicial = x;
        jugadorYInicial = y;
    }

    @Override
    protected int obtenerNumeroNivel() {
        return 6;
    }
    
}
