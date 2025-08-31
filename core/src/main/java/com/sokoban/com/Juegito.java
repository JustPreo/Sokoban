/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com;

import com.badlogic.gdx.Game;
import com.sokoban.com.Base.IntentoDeMenu.MenuScreen;

/**
 *
 * @author user
 */
public class Juegito extends Game{
    @Override
    public void create() {
        setScreen(new MenuScreen()); 
    }
}
    
    
