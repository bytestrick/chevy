package chevy.control;

import chevy.Game;
import chevy.view.Window;

import javax.swing.JButton;

public class MenuController {
    public MenuController(Window window, JButton play, JButton playerCycleNext,
                          JButton playerCyclePrev) {
        play.addActionListener(actionEvent -> {
            Game.startGameLoop();
            window.setScene(Window.Scene.PLAYING);
        });

        playerCycleNext.addActionListener(actionEvent -> { });

        playerCyclePrev.addActionListener(actionEvent -> { });
    }
}