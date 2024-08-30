package chevy.service;

import chevy.utils.Load;
import chevy.utils.Log;
import chevy.utils.Utils;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Sound {
    private static final Clip[] effects = new Clip[Effect.values().length];
    private static final Clip[] songs = new Clip[Song.values().length];
    private static final Clip[] loops = new Clip[]{Load.clip("loop0"), Load.clip("loop1")};
    private static Sound instance = null;
    private static Clip currentPlayingLoop;
    private static boolean musicPaused = false;
    private static boolean musicRunning = false;
    private static float effectGainPercent = .8f; // valore predefinito volume effetti
    private static float musicGainPercent = .7f; // valore predefinito volume musica
    private static Clip previousSong;
    private final Object musicMutex = new Object();

    private Sound() {
        final Effect[] e = Effect.values();
        for (int i = 0; i < e.length; ++i) {
            effects[i] = Load.clip(e[i].toString().toLowerCase());
        }
        final Song[] s = Song.values();
        for (int i = 0; i < s.length; ++i) {
            songs[i] = Load.clip(s[i].toString().toLowerCase());
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
     * @param clip       a cui applicare il gain
     * @param percentage valore decimale compreso tra 0 e 1, rappresenta il volume
     */
    private static void applyGain(Clip clip, final float percentage) {
        if (clip != null) {
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            final float beg = Math.abs(gainControl.getMinimum()), end = Math.abs(gainControl.getMaximum());
            gainControl.setValue((beg + end) * percentage - beg);
        }
    }


    public static void startMenuMusic() {
        if (Arrays.stream(loops).allMatch(Objects::nonNull)) {
            currentPlayingLoop = loops[Utils.random.nextInt(loops.length)];
            applyGain(currentPlayingLoop, musicGainPercent);
            currentPlayingLoop.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public static void stopMenuMusic() {
        if (currentPlayingLoop != null) {
            currentPlayingLoop.stop();
        }
    }

    /**
     * Riproduce un effetto
     * Se l'effetto è già in riproduzione viene solamente fatto ripartire.
     *
     * @param effect da riprodurre
     */
    public void play(Effect effect) {
        Clip clip = effects[effect.ordinal()];
        if (clip != null) {
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
    }

    /**
     * Imposta il volume della musica
     *
     * @param percentage valore decimale compreso tra 0 e 1, rappresenta il volume
     */
    public void setMusicVolume(final float percentage) {
        if (percentage >= 0 && percentage <= 1) {
            musicGainPercent = percentage;
            for (Clip loop : loops) {
                applyGain(loop, percentage);
            }
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
            effectGainPercent = percentage;
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
        if (Arrays.stream(songs).allMatch(Objects::nonNull)) {
            stopMusic();
            musicRunning = true;
            Thread.ofPlatform().start(() -> {
                Log.info("Thread per la musica avviato");
                while (musicRunning) {
                    // Scegli la canzone successiva escludendo la precedente.
                    final Clip finalPreviousSong = previousSong;
                    final List<Clip> choices = Stream.of(songs).filter(clip -> !clip.equals(finalPreviousSong)).toList();
                    final Clip song = choices.get(Utils.random.nextInt(choices.size()));
                    song.setFramePosition(0);
                    previousSong = song;
                    final long len = song.getMicrosecondLength();
                    synchronized (musicMutex) { // Acquisisci il monitor di musicMutex
                        while (song.getMicrosecondPosition() < len) {
                            if (musicRunning) {
                                try {
                                    if (musicPaused) {
                                        song.stop();
                                        musicMutex.wait(); // Aspetta una chiamata di resumeMusic()
                                    } else {
                                        song.start();
                                        musicMutex.wait((len - song.getMicrosecondPosition()) / 1000);
                                    }
                                } catch (InterruptedException e) {
                                    break;
                                }
                            } else {
                                song.stop();
                                Log.info("Thread per la musica terminato");
                                return;
                            }
                        }
                    }
                }
            });
        }
    }

    public void stopMusic() {
        if (musicRunning) {
            synchronized (musicMutex) {
                musicRunning = false;
                musicPaused = false;
                musicMutex.notify();
            }
            Thread t = Thread.currentThread();
            try {
                synchronized (t) {
                    t.wait(10); // Dai tempo al thread di uscire.
                }
            } catch (InterruptedException ignored) { }
        }
    }

    public void resumeMusic() {
        synchronized (musicMutex) {
            if (musicPaused) {
                musicPaused = false;
                musicMutex.notify();
            }
        }
    }

    public void pauseMusic() {
        synchronized (musicMutex) {
            if (!musicPaused) {
                musicPaused = true;
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