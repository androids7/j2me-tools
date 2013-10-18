/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.j2me.utils;

/**
 *
 * @author willian
 */
import javax.microedition.lcdui.*;
import java.util.*;

/**
 *
 * @author willian
 */
public class WaitScreen extends CustomItem {

    private int mCount,  mMaximum;
    private int mInterval;
    private int mWidth,  mHeight,  mX,  mY,  mRadius;
    private String mMessage;
    private String mPercente;
    private Image imgBotton;
    private boolean animateScreen = false;

    public WaitScreen(String title) {
        super(title);
        mCount = 0;
        mMaximum = 36;
        mInterval = 100;

        TimerTask task = new TimerTask() {

            public void run() {
                mCount = (mCount + 1) % mMaximum;
                repaint();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 0, mInterval);
    }

    public void setString(String string) {
        this.mMessage = string;
        repaint();
    }

    public void setStringPercente(String stringPercente) {
        this.mPercente = stringPercente;
    }

    public void paint(Graphics g, int iWidth, int iHeight) {
        mWidth = iWidth;
        mHeight = iHeight;
        int halfWidth = mWidth / 2;
        int halfHeight = mHeight / 2;

        mRadius = Math.min(halfWidth, halfHeight);
        mX = halfWidth - mRadius / 2;
        mY = halfHeight - mRadius / 2;

        int theta = -(mCount * 360 / mMaximum);

        //g.setColor( 255, 255, 255 );
        //g.fillRect( 0, 0, mWidth, mHeight );
        g.setColor(0, 0, 0);
        if (animateScreen) {
            g.drawArc(mX, mY, mRadius, mRadius, 0, 360);
            g.fillArc(mX, mY, mRadius, mRadius, theta + 90, 90);
            g.fillArc(mX, mY, mRadius, mRadius, theta + 270, 90);
        }

        /*if (mPercente != null) {
        g.setColor( 0, 0, 0 );
        g.drawArc( mX+(mRadius/3), mY+(mRadius/3), mRadius/3, mRadius/3, 0, 360 );
        g.fillArc( mX+(mRadius/3), mY+(mRadius/3), mRadius/3, mRadius/3, 0, 360 );
        }*/

        if (mMessage != null) {
            if (mPercente != null) {
                if (animateScreen) {
                    g.drawString(mMessage, mWidth / 2, mHeight, Graphics.BOTTOM | Graphics.HCENTER);
                } else {
                    g.drawString(mMessage, mWidth / 2, mHeight, Graphics.BOTTOM | Graphics.HCENTER);
                }
            }else{
                if (animateScreen) {
                    g.drawString(mMessage, mWidth / 2, mHeight - (mHeight / 8), Graphics.BOTTOM | Graphics.HCENTER);
                } else {
                    g.drawString(mMessage, mWidth / 2, mHeight / 2, Graphics.BOTTOM | Graphics.HCENTER);
                }
            }
        }

        if (mPercente != null) {
            //g.setColor(255, 255, 255);
            g.setColor(0, 0, 0);
            Font font = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_LARGE);
            g.setFont(font);
            //g.drawString(mPercente, mWidth / 2, (mHeight / 2) + (mHeight / 25), Graphics.BOTTOM | Graphics.HCENTER);
            if (animateScreen) {
                g.drawString(mPercente, mWidth / 2, mHeight - (mHeight / 8), Graphics.BOTTOM | Graphics.HCENTER);
            } else {
                g.drawString(mPercente, mWidth / 2, mHeight / 2, Graphics.BOTTOM | Graphics.HCENTER);
            }
        }

        if (imgBotton != null) {
            g.drawImage(imgBotton, 0, mHeight - imgBotton.getHeight(), Graphics.BOTTOM | Graphics.RIGHT);
        }
    }

    public int getMinContentWidth() {
        return 80;
    }

    public int getMinContentHeight() {
        return 80;
    }

    public int getPrefContentWidth(int width) {
        return getMinContentWidth();
    }

    public int getPrefContentHeight(int height) {
        return getMinContentHeight();
    }

    public void setMinContentWidth(int width) {
        mWidth = width;
    }

    public void setMinContentHeight(int height) {
        mHeight = height;
    }

    public void setImageBotton(Image img) {
        imgBotton = img;
    }

    public boolean isAnimateScreen() {
        return animateScreen;
    }

    public void setAnimateScreen(boolean animateScreen) {
        this.animateScreen = animateScreen;
    }

    /*
     * Color
    Red

    Green

    Blue

    Hexadecimal

    Black	0	0	0	#000000
    White	255	255	255	#FFFFFF
    Red	255	0	0	#FF0000
    Green	0	192	0	#00C000
    Blue	0	0	255	#0000FF
    Yellow	255	255	0	#FFFF00
     */
}