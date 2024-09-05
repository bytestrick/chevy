package chevy.service;

import chevy.utils.Load;
import chevy.utils.Log;
import chevy.utils.Utils;
import chevy.view.GamePanel;
import chevy.view.Window;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
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
    /** Valore corrente per il volume degli effetti sonori, in percentuale */
    public static float effectGainPercentage = .8f;
    /** Valore corrente per il volume della musica, in percentuale */
    public static float musicGainPercentage = .7f;
    private static Clip currentLoop;
    private static Clip currentSong;
    private static LineListener currentSongLineListener;
    private static boolean musicPaused;

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
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            final float beg = Math.abs(gain.getMinimum()), end = Math.abs(gain.getMaximum());
            gain.setValue((beg + end) * percentage - beg);
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
            Arrays.stream(loops).forEach(loop -> applyGain(loop, musicGainPercentage));
            Arrays.stream(songs).forEach(song -> applyGain(song, musicGainPercentage));
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
            Arrays.stream(effects).forEach(effect -> applyGain(effect, effectGainPercentage));
        } else {
            Log.warn(Sound.class + ": il volume non può essere impostato al " + (percentage * 100) + "%");
        }
    }

    /**
     * Avvia la musica di gioco
     *
     * @param newSong se {@code true} fa in modo che parta una nuova canzone diversa dalla
     *                precedente, altrimenti fa partire la canzone precedente dal punto in cui è
     *                stata interrotta
     */
    public static void startMusic(boolean newSong) {
        if (Stream.of(songs).allMatch(Objects::nonNull) && (currentSong == null || newSong)) {
            List<Clip> choices =
                    Stream.of(songs).filter(clip -> !clip.equals(currentSong)).toList();
            currentSong = choices.get(Utils.random.nextInt(choices.size()));
            currentSong.setFramePosition(0);
            Log.info("Nuova canzone scelta");
        }
        musicPaused = false;
        currentSongLineListener = Sound::handleSongFinished;
        currentSong.addLineListener(currentSongLineListener);
        currentSong.start();
    }

    /**
     * Quando la clip corrente riceve un segnale di STOP, e si determina che sia dovuto alla fine
     * della riproduzione, fa partire un'altra canzone
     */
    private static void handleSongFinished(LineEvent event) {
        if (!musicPaused && event.getType() == LineEvent.Type.STOP
                && Window.isQuitDialogNotActive() && GamePanel.isPauseDialogNotActive()) {
            Log.info("La canzone è finita");
            currentSong.removeLineListener(currentSongLineListener);
            startMusic(true);
        }
    }

    /**
     * Mette in pausa la musica di gioco
     */
    public static void stopMusic() {
        if (currentSong != null) {
            musicPaused = true;
            currentSong.stop();
            Log.info("Musica di gioco stoppata");
        }
    }

    /**
     * Avvia la musica del menù
     *
     * @param newSong se cambiare canzone o meno
     */
    public static void startLoop(boolean newSong) {
        if (currentLoop == null || newSong) {
            currentLoop = loops[Utils.random.nextInt(loops.length)];
        }
        if (currentLoop != null) {
            currentLoop.loop(Clip.LOOP_CONTINUOUSLY);
            Log.info("Musica del menù avvivata");
        }
    }

    public static void stopLoop() {
        if (currentLoop != null) {
            currentLoop.stop();
            Log.info("Musica del menù stoppata");
        }
    }

    public enum Effect {
        HUMAN_DEATH, ARROW_SWOOSH, WIN, CLAW_HIT, PUNCH, BLADE_SLASH, SPIKE, ARCHER_FOOTSTEPS,
        NINJA_FOOTSTEPS, KNIGHT_FOOTSTEPS, ZOMBIE_BITE, ZOMBIE_CHOCKING, ZOMBIE_HIT, SKELETON_HIT,
        SKELETON_DISASSEMBLED, SKELETON_ATTACK, SLIDE, SLIME_HIT, SLIME_DEATH, ROBOTIC_INSECT,
        BEETLE_ATTACK, BEETLE_DEATH, WRAITH_ATTACK, WRAITH_HIT, WRAITH_DEATH, MUD, COIN,
        KEY_EQUIPPED, POWER_UP_UI, HEALTH_POTION, LOST, PLAY_BUTTON, BUTTON, STOP, UNLOCK_CHARACTER
    }

    private enum Song {BG1, BG2, BG3}
}