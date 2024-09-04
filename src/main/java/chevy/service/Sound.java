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

/**
 * Effetti sonori e musica del gioco
 */
public final class Sound {
    private static final Clip[] effects = new Clip[Effect.values().length];
    private static final Clip[] songs = new Clip[Song.values().length];
    private static final Clip[] loops = new Clip[]{Load.clip("loop0"), Load.clip("loop1")};
    private static final Object mutex = new Object();
    /** Valore corrente per il volume degli effetti sonori, in percentuale */
    public static float effectGainPercentage = .8f;
    /** Valore corrente per il volume della musica, in percentuale */
    public static float musicGainPercentage = .7f;
    private static Clip currentLoop;
    private static boolean musicPaused = false;
    private static boolean musicRunning = false;
    private static Clip previousSong;

    static {
        final Effect[] e = Effect.values();
        for (int i = 0; i < e.length; ++i) {
            effects[i] = Load.clip(e[i].toString().toLowerCase());
        }
        final Song[] s = Song.values();
        for (int i = 0; i < s.length; ++i) {
            songs[i] = Load.clip(s[i].toString().toLowerCase());
        }
        setMusicVolume(musicGainPercentage);
        setEffectsVolume(effectGainPercentage);
    }

    /**
     * Mappa l'intervallo decimale tra 0 e 1 al dominio del gain della {@link Clip} specificata
     *
     * @param clip       a cui applicare il gain
     * @param percentage valore decimale compreso tra 0 e 1, rappresenta il volume
     */
    private static void applyGain(Clip clip, final float percentage) {
        if (clip != null) {
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            final float beg = Math.abs(gainControl.getMinimum()), end =
                    Math.abs(gainControl.getMaximum());
            gainControl.setValue((beg + end) * percentage - beg);
        }
    }

    /**
     * Avvia la musica di gioco
     */
    public static void startMenuMusic() {
        if (Arrays.stream(loops).allMatch(Objects::nonNull)) {
            currentLoop = loops[Utils.random.nextInt(loops.length)];
            applyGain(currentLoop, musicGainPercentage);
            currentLoop.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public static void stopMenuMusic() {
        if (currentLoop != null) {
            currentLoop.stop();
        }
    }

    /**
     * Riproduce un effetto
     * Se l'effetto è già in riproduzione viene solamente fatto ripartire.
     *
     * @param effect da riprodurre
     */
    public static void play(Effect effect) {
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
    public static void setMusicVolume(final float percentage) {
        if (percentage >= 0 && percentage <= 1) {
            musicGainPercentage = percentage;
            for (Clip loop : loops) {
                applyGain(loop, percentage);
            }
            for (Song song : Song.values()) {
                applyGain(songs[song.ordinal()], percentage);
            }
        } else {
            Log.warn(Sound.class + ": il volume non può essere impostato al " + (percentage * 100) + "%");
        }
    }

    /**
     * Imposta il volume degli effetti
     *
     * @param percentage valore decimale compreso tra 0 e 1, rappresenta il volume
     */
    public static void setEffectsVolume(final float percentage) {
        if (percentage >= 0 && percentage <= 1) {
            effectGainPercentage = percentage;
            for (Effect effect : Effect.values()) {
                applyGain(effects[effect.ordinal()], percentage);
            }
        } else {
            Log.warn(Sound.class + ": il volume non può essere impostato al " + (percentage * 100) + "%");
        }
    }

    /**
     * Crea un thread che si occupa di riprodurre la musica in sequenza. Quando una clip termina
     * la riproduzione fa
     * partire la prossima.
     */
    public static void startMusic() {
        if (Arrays.stream(songs).allMatch(Objects::nonNull)) {
            stopMusic();
            musicRunning = true;
            Thread.ofPlatform().start(() -> {
                Log.info("Thread per la musica avviato");
                while (musicRunning) {
                    // Scegli la canzone successiva escludendo la precedente.
                    final Clip finalPreviousSong = previousSong;
                    final List<Clip> choices =
                            Stream.of(songs).filter(clip -> !clip.equals(finalPreviousSong)).toList();
                    final Clip song = choices.get(Utils.random.nextInt(choices.size()));
                    song.setFramePosition(0);
                    previousSong = song;
                    final long len = song.getMicrosecondLength();
                    synchronized (mutex) { // Acquisisci il monitor di musicMutex
                        while (song.getMicrosecondPosition() < len) {
                            if (musicRunning) {
                                try {
                                    if (musicPaused) {
                                        song.stop();
                                        mutex.wait(); // Aspetta una chiamata di resumeMusic()
                                    } else {
                                        song.start();
                                        mutex.wait((len - song.getMicrosecondPosition()) / 1000);
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

    public static void stopMusic() {
        if (musicRunning) {
            synchronized (mutex) {
                musicRunning = false;
                musicPaused = false;
                mutex.notify();
            }
            Thread t = Thread.currentThread();
            try {
                synchronized (t) {
                    t.wait(10); // Dai tempo al thread di uscire.
                }
            } catch (InterruptedException ignored) {}
        }
    }

    public static void resumeMusic() {
        synchronized (mutex) {
            if (musicPaused) {
                musicPaused = false;
                mutex.notify();
            }
        }
    }

    public static void pauseMusic() {
        synchronized (mutex) {
            if (!musicPaused) {
                musicPaused = true;
                mutex.notify();
            }
        }
    }

    public enum Effect {
        HUMAN_DEATH, ARROW_SWOOSH, WIN, CLAW_HIT, PUNCH, BLADE_SLASH, SPIKE, ARCHER_FOOTSTEPS,
        NINJA_FOOTSTEPS, KNIGHT_FOOTSTEPS, ZOMBIE_BITE, ZOMBIE_CHOCKING, ZOMBIE_HIT, SKELETON_HIT,
        SKELETON_DISASSEMBLED, SKELETON_ATTACK, SLIDE, SLIME_HIT, SLIME_DEATH, ROBOTIC_INSECT,
        BEETLE_ATTACK, BEETLE_DEATH, WRAITH_ATTACK, WRAITH_HIT, WRAITH_DEATH, MUD, COIN,
        KEY_EQUIPPED, POWER_UP_UI, HEALTH_POTION, LOST, PLAY_BUTTON, BUTTON, STOP, UNLOCK_CHARACTER
    }

    private enum Song {
        BG1, BG2, BG3
    }
}