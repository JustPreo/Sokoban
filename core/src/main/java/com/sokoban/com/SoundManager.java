/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sokoban.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author user
 */
public class SoundManager {

    private static Music NivelesMusica = Gdx.audio.newMusic(Gdx.files.internal("Musica/shrek.WAV"));
    private static Music LobbyMusica = Gdx.audio.newMusic(Gdx.files.internal("Musica/shrek.WAV"));
    private static Music current = null;
    private static Sound sonidoCaminar = Gdx.audio.newSound(Gdx.files.internal("Musica/walk.WAV"));

    public static void setearMusica(int numero) {
        //1 = lobby | 2 = Niveles
        if (current != null && current.isPlaying())
        {
        current.stop();
        }
        if (numero == 1) {
            current = LobbyMusica;
        } else {
            current = NivelesMusica;
        }
        playCurrent();
    }

    private static void playCurrent() {
        if (current != null) {
            current.setLooping(true);
            current.play();
            current.setVolume(0);
        }
    }
    
    public static void playCaminar()
    {
        //sonidoCaminar.stop();
        sonidoCaminar.play();
    }

}
