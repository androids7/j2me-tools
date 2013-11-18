/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.tools.media;

/**
 *
 * @author hugocampos
 */
import javax.microedition.lcdui.*;
import javax.microedition.media.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.media.control.VideoControl;
import javax.microedition.midlet.MIDletStateChangeException;

public class Video extends Form implements CommandListener, PlayerListener {

    MIDlet midlet;
    private Display display;
    private Command play, stop, exit;
    private Player player;

    public Video(MIDlet midlet, Display display) {
        super("");
        this.midlet = midlet;
        this.display = Display.getDisplay(midlet);
        play = new Command("Play", Command.OK, 0);
        stop = new Command("Stop", Command.STOP, 0);
        exit = new Command("Exit", Command.EXIT, 0);
        addCommand(play);
        addCommand(stop);
        addCommand(exit);
        setCommandListener(this);
    }

      public void commandAction(Command c, Displayable d) {
        if (c == play) {
            try {
                playVideo();
            } catch (Exception e) {
            }
        } else if (c == stop) {
            player.close();
        } else if (c == exit) {
            if (player != null) {
                player.close();
            }

//            midlet.destroyApp(false);            
        }
    }
      
    public void playerUpdate(Player player, String event, Object eventData) {
        if (event.equals(PlayerListener.STARTED) && new Long(0L).equals((Long) eventData)) {
            VideoControl vc = null;
            if ((vc = (VideoControl) player.getControl("VideoControl")) != null) {
                Item videoDisp = (Item) vc.initDisplayMode(vc.USE_GUI_PRIMITIVE, null);
                append(videoDisp);
            }
            display.setCurrent(this);
        } else if (event.equals(PlayerListener.CLOSED)) {
            deleteAll();
        }
    }

    public void playVideo() {
        try {
            player = Manager.createPlayer(getClass().getResourceAsStream("/res/filename.mpg"), "video/mpeg");
            player.addPlayerListener(this);
            player.setLoopCount(-1);
            player.prefetch();
            player.realize();
            player.start();
        } catch (Exception e) {
        }
    }
}
