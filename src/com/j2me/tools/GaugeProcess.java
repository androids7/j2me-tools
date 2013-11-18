/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.j2me.tools;

import javax.microedition.lcdui.Gauge;

/**
 *
 * @author willian
 */
public class GaugeProcess extends Gauge {
    private GaugeUpdater updater;
    private Thread animate;
    private Gauge gauge = this;
    private boolean run = false;

    public GaugeProcess(String label, boolean interactive, int maxValue, int initialValue) {
        super(label, interactive, maxValue, initialValue);
    }

    public void startAnimate(){
        gauge.setValue(0);

        if(updater == null || animate == null){
            updater = new GaugeUpdater();
            animate = new Thread(updater);
            animate.start();
        }

        run = true;
    }

    public void stopAnimate(){
        if(animate != null){
            updater = null;
            animate = null;
            gauge.setValue(0);
        }

        run = false;
    }

    public boolean isAnimate(){
        return run;
    }

    private class GaugeUpdater implements Runnable {
        boolean right = true;
        public void run() {
            try {
                while (run) {
                    try{
                        Thread.sleep(1);
                    }catch(InterruptedException iex){
                        iex.printStackTrace();
                    }
                    
                    if (right) {
                        gauge.setValue(gauge.getValue() + 1);
                        if (gauge.getValue() == gauge.getMaxValue()) {
                            right = false;
                        }
                    } else {
                        gauge.setValue(gauge.getValue() - 1);
                        if (gauge.getValue() == 0) {
                            right = true;
                        }
                    }
                }
            } catch (Exception Error) {
                throw new RuntimeException(Error.getMessage());
            }
        }
    }
}
