package com.sokoban.com;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {

    private static HashMap<String, Music> musicMap = new HashMap<>();
    private static Sound caminarSound;
    private static Sound pou;

    private static Music currentMusic = null;
    private static float targetVolume = 0.5f;  // Volumen deseado , MUSICA
    private static float targetVolumeEffects = 0.5f;//Volumen deseado , EFECTOS
    private static float fadeSpeed = 0.02f;  // Velocidad de fade
    private static float volumenAntesDePausar = targetVolume;

    private static float controlVolumen = 0.5f;

    static {
        musicMap.put("lobby", Gdx.audio.newMusic(Gdx.files.internal("Musica/Simplicity.WAV")));
        musicMap.put("nivel", Gdx.audio.newMusic(Gdx.files.internal("Musica/shrek.WAV")));

        pou = Gdx.audio.newSound(Gdx.files.internal("Musica/POU.WAV"));
        caminarSound = Gdx.audio.newSound(Gdx.files.internal("Musica/walk.WAV"));
    }

    public static void playMusic(String name, boolean loop, float volume) {
        Music music = musicMap.get(name);
        if (music == null) {
            return;
        }

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
        if (currentMusic == null) {
            return;
        }

        float vol = currentMusic.getVolume();
        if (vol < controlVolumen) {
            vol += fadeSpeed;
            if (vol > controlVolumen) {
                vol = controlVolumen;
            }
            currentMusic.setVolume(vol);
        } else if (vol > controlVolumen) {
            vol -= fadeSpeed;
            if (vol < controlVolumen) {
                vol = controlVolumen;
            }
            currentMusic.setVolume(vol);
        }
    }

    public static void pauseMusic() {
        volumenAntesDePausar = currentMusic.getVolume();
        controlVolumen = 0f; // baja volumen con fade
    }

    public static void resumeMusic() {
        if (currentMusic != null) {
            controlVolumen = volumenAntesDePausar; // restaura el volumen real antes de pausar
            if (!currentMusic.isPlaying()) {
                currentMusic.play();
            }
        }
    }

    public static void playCaminar(float volume) {//por si acaso
        if (caminarSound != null) {
            caminarSound.play(targetVolumeEffects);
        }
    }

    public static void playPou(float volume) {//el float volume es por si acaso
        if (pou != null) {
            pou.play(targetVolumeEffects);
        }
    }

    public static void stopMusic() {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
    }

    public static void dispose() {
        for (Music m : musicMap.values()) {
            m.dispose();
        }
        if (caminarSound != null) {
            caminarSound.dispose();
        }
    }

    private static void setVolume(float volumen) {
        targetVolume = volumen;
    }

    private static void setVolumeEffects(float volumen) {
        targetVolumeEffects = volumen;
    }

    // MÉTODOS AGREGADOS PARA PANTALLA SETTINGS YA TENGO MUCHO SUEÑO LOL
    // Métodos para controlar volumen de música (compatibles con PantallaSettings que cree para el setting)
    public static void setMusicVolume(float volume) {
        setVolume(volume); // usa el método existente
        controlVolumen = targetVolume; // esto actualiza el control inmediatamente
    }

    public static float getMusicVolume() {
        return targetVolume;
    }

    // Métodos para controlar volumen de efectos (compatibles con PantallaSettings yet again)
    public static void setSoundVolume(float volume) {
        setVolumeEffects(volume); // same as the last
    }

    public static float getSoundVolume() {
        return targetVolumeEffects;
    }

    // nada mas para verificar si se esta reproduciendo
    public static boolean isMusicPlaying() {
        return currentMusic != null && currentMusic.isPlaying();
    }
}
