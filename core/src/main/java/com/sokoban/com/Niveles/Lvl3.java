/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Niveles;

import com.sokoban.com.Base.JuegoBase;
import com.sokoban.com.levelEditor.Mapas.MapaNivel3;

/**
 *
 * @author user
 */
public class Lvl3 extends JuegoBase {
@Override
    public void conseguirCantCajas() {
        cantidadC = MapaNivel3.CANTIDAD_CAJAS;
    }

    @Override
    protected void configurarNivel() {
        jugadorX = MapaNivel3.JUGADOR_POS[0]; 
        jugadorY = MapaNivel3.JUGADOR_POS[1];
        cambiarMapa(MapaNivel3.MAPA);
        cajasPos = MapaNivel3.CAJAS_POS;
        objetivosPos = MapaNivel3.OBJETIVOS_POS;
        COLUMNAS = MapaNivel3.columnas; //
        FILAS = MapaNivel3.filas; //
        xyInicial(jugadorX,jugadorY);
    }

    @Override
    protected void xyInicial(int x, int y) {
      jugadorXInicial = x;
        jugadorYInicial = y;
    }

    @Override
    protected int obtenerNumeroNivel() {
       return 3;
    }    
}
