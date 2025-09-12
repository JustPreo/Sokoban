/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com.Niveles;

import com.sokoban.com.Base.*;
import com.sokoban.com.levelEditor.Mapas.MapaNivel1;

/**
 *
 * @author user
 */
public class Lvl1 extends JuegoBase {
    //La base para poder crear un nivel es haciendole override a unos metodos que pondre de primeros con @override

    @Override
    public void conseguirCantCajas() {
        cantidadC = MapaNivel1.CANTIDAD_CAJAS;
        //En este metodo hay que definir cantidad de cajas
    }

    protected void configurarNivel() {
        // Definir mapa específico
        jugadorX = MapaNivel1.JUGADOR_POS[0]; //x jogador
        jugadorY = MapaNivel1.JUGADOR_POS[1];// y jogador
        // 0 = suelo, 1 = pared |2 = objetivo |3 = cajas | 4 = player
        cambiarMapa(MapaNivel1.MAPA);
        //Posicion de cajas
        cajasPos = MapaNivel1.CAJAS_POS;

        //Posiciones de objetivos
        objetivosPos = MapaNivel1.OBJETIVOS_POS;
        
        //Tienen que existir la misma cantidad de cajas que objetivos
        
        
        // Posición inicial del jugador
        
        // Columnas y filas
        COLUMNAS = MapaNivel1.columnas; //
        FILAS = MapaNivel1.filas; //
        //Entonces cuando vayas a tocar algo de esto tambien modificas el cambiarMapa
        xyInicial(jugadorX,jugadorY);
    }

    @Override
    protected void xyInicial(int x, int y) {//Para que cada mapa tenga su x , y inicial propio
        jugadorXInicial = x;
        jugadorYInicial = y;
        
    }
    
    protected int obtenerNumeroNivel() {
        return 1;
    }


}
