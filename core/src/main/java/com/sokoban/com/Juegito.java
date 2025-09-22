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
public class Juegito extends Game { //Este es el main para que abra el juegito

    @Override
    public void create() {
        //setScreen(new SlideshowTutorial(this, Idiomas.getInstance()));
        
                setScreen(new ThanosRoom());
        //setScreen(new Escena(this));
        SoundManager.playMusic("lobby", true, 0.5f);//0.5f es el volumen
    }

    public void render() {
        super.render();
        SoundManager.update();    
    }
}
