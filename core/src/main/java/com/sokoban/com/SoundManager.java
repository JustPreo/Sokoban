package com.sokoban.com;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import java.util.HashMap;

public class SoundManager {

    private static HashMap<String, Music> musicMap = new HashMap<>();
    private static Sound caminarSound;

    private static Music currentMusic = null;
    private static float targetVolume = 1f;  // Volumen deseado , se puede cambiar a futuro con metodo de nad
    private static float fadeSpeed = 0.02f;  // Velocidad de fade

    static {
        musicMap.put("lobby", Gdx.audio.newMusic(Gdx.files.internal("Musica/Simplicity.WAV")));
        musicMap.put("nivel", Gdx.audio.newMusic(Gdx.files.internal("Musica/shrek.WAV")));

        caminarSound = Gdx.audio.newSound(Gdx.files.internal("Musica/walk.WAV"));
    }

    public static void playMusic(String name, boolean loop, float volume) {
        Music music = musicMap.get(name);
        if (music == null) return;

        if (currentMusic != null && currentMusic != music) {
            currentMusic.stop();
        }

        currentMusic = music;
        currentMusic.setLooping(loop);
        targetVolume = volume;
        currentMusic.setVolume(0f); 
        currentMusic.play();
    }

    // Llamar cada frame desde render()
    public static void update() {
        if (currentMusic == null) return;

        float vol = currentMusic.getVolume();
        if (vol < targetVolume) {
            vol += fadeSpeed;
            if (vol > targetVolume) vol = targetVolume;
            currentMusic.setVolume(vol);
        } else if (vol > targetVolume) {
            vol -= fadeSpeed;
            if (vol < targetVolume) vol = targetVolume;
            currentMusic.setVolume(vol);
        }
    }

    public static void pauseMusic() {
        targetVolume = 0f; // baja volumen con fade
    }

    public static void resumeMusic() {
        targetVolume = 1f; // sube volumen hasta 1
        if (!currentMusic.isPlaying()) currentMusic.play();
    }

    public static void playCaminar(float volume) {
        if (caminarSound != null) {
            caminarSound.play(volume);
        }
    }

    public static void stopMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
    }

    public static void dispose() {
        for (Music m : musicMap.values()) m.dispose();
        if (caminarSound != null) caminarSound.dispose();
    }
    
    public static void setVolume(float volumen)
    {
    targetVolume = volumen;
    }
}