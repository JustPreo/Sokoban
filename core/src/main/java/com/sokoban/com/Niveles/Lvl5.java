/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Niveles;

import com.sokoban.com.Base.JuegoBase;
import com.sokoban.com.levelEditor.MapaNivel5;

/**
 *
 * @author user
 */
public class Lvl5 extends JuegoBase {
    @Override
    public void conseguirCantCajas() {
        cantidadC = MapaNivel5.CANTIDAD_CAJAS;
    }

    @Override
    protected void configurarNivel() {
        jugadorX = MapaNivel5.JUGADOR_POS[0]; 
        jugadorY = MapaNivel5.JUGADOR_POS[1];
        cambiarMapa(MapaNivel5.MAPA);
        cajasPos = MapaNivel5.CAJAS_POS;
        objetivosPos = MapaNivel5.OBJETIVOS_POS;
        COLUMNAS = MapaNivel5.columnas; //
        FILAS = MapaNivel5.filas; //
        xyInicial(jugadorX,jugadorY);
    }

    @Override
    protected void xyInicial(int x, int y) {
      jugadorXInicial = x;
        jugadorYInicial = y;
    }

    @Override
    protected int obtenerNumeroNivel() {
       return 5;
    }    
    
}
