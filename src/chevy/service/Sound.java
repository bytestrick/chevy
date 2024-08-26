package chevy.service;

import chevy.utils.Load;
import chevy.utils.Log;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.Random;

public class Sound {
    private static Sound instance = null;
    private final Random random = new Random();
    private final Clip[] effects = new Clip[Effect.values().length];
    private final Clip[] songs = new Clip[Song.values().length];
    private final Object musicMutex = new Object();
    private boolean musicPlaying = false;
    private boolean musicWorkerRunning = false;
    private float effectGainPercent = .8f; // valore predefinito volume effetti
    private float musicGainPercent = .4f; // valore predefinito volume musica

    private Sound() {
        final Effect[] e = Effect.values();
        for (int i = 0; i < e.length; ++i) {
            try (Clip clip = Load.clip(e[i].toString().toLowerCase())) {
                effects[i] = clip;
            }
        }
        final Song[] s = Song.values();
        for (int i = 0; i < s.length; ++i) {
            try (Clip clip = Load.clip(s[i].toString().toLowerCase())) {
                songs[i] = clip;
            }
        }
        setMusicVolume(musicGainPercent);
        setEffectsVolume(effectGainPercent);
    }

    public static Sound getInstance() {
        if (instance == null) {
            instance = new Sound();
        }
        return instance;
    }

    /**
     * Mappa l'intervallo decimale tra 0 e 1 al dominio del gain della clip specificata
     *
     * @param clip a cui applicare il gain
     * @param percentage valore decimale compreso tra 0 e 1, rappresenta il volume
     */
    private static void applyGain(Clip clip, final float percentage) {
        assert clip != null;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        final float beg = Math.abs(gainControl.getMinimum()), end = Math.abs(gainControl.getMaximum());
        gainControl.setValue((beg + end) * percentage - beg);
    }

    /**
     * Riproduce un effetto
     * Se l'effetto è già in riproduzione viene solamente fatto ripartire.
     *
     * @param effect da riprodurre
     */
    public void play(Effect effect) {
        Clip clip = effects[effect.ordinal()];
        if (clip.isActive() || clip.isRunning()) {
            clip.stop();
            clip.flush();
            clip.drain();
        }
        if (clip.getMicrosecondPosition() > 0) {
            clip.setMicrosecondPosition(0);
        }
        clip.start();
    }

    /**
     * Imposta il volume della musica
     *
     * @param percentage valore decimale compreso tra 0 e 1, rappresenta il volume
     */
    public void setMusicVolume(final float percentage) {
        if (percentage >= 0 && percentage <= 1) {
            this.musicGainPercent = percentage;
            for (Song song : Song.values()) {
                applyGain(songs[song.ordinal()], percentage);
            }
        } else {
            Log.warn(getClass() + ": il volume non può essere impostato al " + (percentage * 100) + "%");
        }
    }

    /**
     * Imposta il volume degli effetti
     *
     * @param percentage valore decimale compreso tra 0 e 1, rappresenta il volume
     */
    public void setEffectsVolume(final float percentage) {
        if (percentage >= 0 && percentage <= 1) {
            this.effectGainPercent = percentage;
            for (Effect effect : Effect.values()) {
                applyGain(effects[effect.ordinal()], percentage);
            }
        } else {
            Log.warn(getClass() + ": il volume non può essere impostato al " + (percentage * 100) + "%");
        }
    }

    /**
     * Crea un thread che si occupa di riprodurre la musica in sequenza. Quando una clip termina la riproduzione fa
     * partire la prossima.
     */
    public void startMusic() {
        if (musicWorkerRunning) {
            stopMusic();
        }
        musicWorkerRunning = true;
        musicPlaying = true;
        Thread.ofPlatform().start(() -> {
            Log.info("Thread per la musica avviato");
            random.setSeed(Thread.currentThread().threadId());
            int i = random.nextInt(songs.length);
            songs[i].setFramePosition(0);
            while (musicWorkerRunning) {
                synchronized (musicMutex) { // Acquisisci il monitor di musicMutex
                    try {
                        while (songs[i].getFramePosition() < songs[i].getFrameLength()) {
                            if (musicWorkerRunning) {
                                if (musicPlaying) {
                                    songs[i].start();
                                    musicMutex.wait((songs[i].getMicrosecondLength() - songs[i].getMicrosecondPosition()) / 1000);
                                } else {
                                    songs[i].stop();
                                    musicMutex.wait(); // Aspetta una chiamata di resumeMusic()
                                }
                            } else {
                                songs[i].stop();
                                break;
                            }
                        }
                        // FIXME: la prossima canzone deve essere scelta a Random
                        i = (i + 1) % songs.length;
                        songs[i].setFramePosition(0);
                        Log.info(getClass() + ": canzone successiva");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            Log.info("Thread per la musica terminato");
        });
    }

    public void stopMusic() {
        synchronized (musicMutex) {
            musicWorkerRunning = false;
            musicPlaying = false;
            musicMutex.notify();
        }
    }

    public void resumeMusic() {
        synchronized (musicMutex) {
            if (!musicPlaying) {
                musicPlaying = true;
                musicMutex.notify();
            }
        }
    }

    public void pauseMusic() {
        synchronized (musicMutex) {
            if (musicPlaying) {
                musicPlaying = false;
                musicMutex.notify();
            }
        }
    }

    public enum Effect {
        HUMAN_DEATH, ARROW_SWOOSH, WIN, CLAW_HIT, PUNCH, BLADE_SLASH, SPIKE, ARCHER_FOOTSTEPS, NINJA_FOOTSTEPS,
        KNIGHT_FOOTSTEPS, ZOMBIE_BITE, ZOMBIE_CHOCKING, ZOMBIE_HIT, SKELETON_HIT, SKELETON_DISASSEMBLED,
        SKELETON_ATTACK, SLIDE, SLIME_HIT, SLIME_DEATH, ROBOTIC_INSECT, BEETLE_ATTACK, BEETLE_DEATH, GHOST_ATTACK,
        GHOST_HIT, GHOST_DEATH, MUD, COIN, KEY_EQUIPPED, POWER_UP_UI, HEALTH_POTION
    }

    public enum Song {
        BG1, BG2, BG3
    }
}