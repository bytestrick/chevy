package chevy;

import chevy.utils.Log;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
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
        Log.info(getClass() + ": inizializzazione");
        final Effect[] e = Effect.values();
        for (int i = 0; i < e.length; ++i) {
            effects[i] = loadClip(e[i].toString().toLowerCase());
        }
        final Song[] s = Song.values();
        for (int i = 0; i < s.length; ++i) {
            songs[i] = loadClip(s[i].toString().toLowerCase());
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

    private static void applyGain(Clip clip, final float percentage) {
        assert clip != null;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        final float beg = Math.abs(gainControl.getMinimum()), end = Math.abs(gainControl.getMaximum());
        gainControl.setValue((beg + end) * percentage - beg);
    }

    private Clip loadClip(final String prefix) {
        try {
            URL path = getClass().getResource("/assets/sound/" + prefix + ".wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(Objects.requireNonNull(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            return clip;
        } catch (NullPointerException | IOException | UnsupportedAudioFileException e) {
            Log.error(getClass() + ": caricamento traccia fallito: " + prefix + " (" + e.getMessage() + ")");
            System.exit(1);
        } catch (LineUnavailableException e) {
            Log.error(getClass() + ": " + prefix + ": " + e.getMessage());
            final DataLine.Info dataLineInfo;
            try {
                dataLineInfo = (DataLine.Info) AudioSystem.getClip().getLineInfo();
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            }
            Log.info("I tipi di DataLine supportati su questa macchina sono:");
            Arrays.stream(dataLineInfo.getFormats()).forEach(format -> Log.info(format.toString()));
            System.exit(1);
        }
        return null;
    }

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

    public void startMusic() {
        if (musicWorkerRunning) {
            Log.warn("Il thread per la musica è già attivo.");
            return;
        }
        musicWorkerRunning = true;
        musicPlaying = true;
        Thread.ofPlatform().name("Music Worker").start(() -> {
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
                        i = (i + 1) % songs.length;
                        songs[i].setFramePosition(0);
                        Log.info(getClass() + ": canzone successiva");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            Log.info(Thread.currentThread().getName() + " termina");
        });
    }

    public void cancelMusic() {
        synchronized (musicMutex) {
            musicWorkerRunning = false;
            musicPlaying = false;
            musicMutex.notify();
        }
    }

    public void resumeMusic() {
        synchronized (musicMutex) {
            if (musicPlaying) {
                Log.warn(getClass() + ": la musica è già in riproduzione.");
            } else {
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
            } else {
                Log.warn(getClass() + ": la musica è già in pausa.");
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