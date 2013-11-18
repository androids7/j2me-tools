/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.j2me.tools.timer;

import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;

/**
 *
 * @author willian
 */
public class ExpiresLogin extends TimerTask{
    private static Timer _timer;
    private static CommandListener _commandListener;
    private static Display _display;
    private static Displayable _displayable;
    private static Alert _alert;
    private static Hashtable _previousDisplayables;
    private static Form _frmWaitScreen;

    //private static Displayable _displayableKeyHashTable;

    private static boolean _loginExpired = false;

    private static int _timeOutUser;
    private static String _timeOutUserMessage;
    private static String _timeOutUserTitle;

    /**
     * Valida o período do Usuário Logado, caso ele fique um X ("getTimeOutUser()") tempo sem nenhuma ação será pedido novamente o Login do Usuário
     * @param commandListener
     * @param display Dispositivo de entrada da Aplicação
     * @param displayable Tela para o Usuário efetuar o Login
     */
    public ExpiresLogin(CommandListener commandListener, Display display, Displayable displayable){
        ExpiresLogin._commandListener = commandListener;
        ExpiresLogin._display = display;
        ExpiresLogin._displayable = displayable;
    }

    /**
     * Iniciar tarefa
     */
    public void run() {
        System.out.println("Sess\u00E3o de Login expirada");
        if(ExpiresLogin._display.getCurrent() != _frmWaitScreen){
            ExpiresLogin._loginExpired = true;

            ExpiresLogin._displayable.setCommandListener(ExpiresLogin._commandListener);

            ExpiresLogin._previousDisplayables.put(ExpiresLogin._displayable
                                                 , ExpiresLogin._display.getCurrent());

            ExpiresLogin._display.setCurrent(getAlert(), ExpiresLogin._displayable);
        }else{
            System.out.println("N\u00E3o foi poss\u00EDvel expirar a sess\u00E3o do usu\u00E1rio, pois existe um processamento em andamento.");
            startValidation();
        }
    }

    /**
     * Cancelar validação e o contador da Sessão de Login do Usuário
     */
    public void stopValidation(){
        try{
            ExpiresLogin._timer.cancel();
            ExpiresLogin._timer = null;
            this.cancel();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    /**
     * Iniciar validação e o contador da Sessão de Login do Usuário
     */
    public void startValidation(){
        ExpiresLogin._loginExpired = false;
        if(getTimeOutUser() > 0){
            if(ExpiresLogin._timer != null) {
                try{
                    ExpiresLogin._timer.cancel();
                    ExpiresLogin._timer = null;
                    //this.cancel();
                }catch(Exception ex){
                    System.out.println("Error ao encerrar o controle de Sess\u00E3o do Usu\u00E1rio.\r\n"+ex.getMessage());
                }
            }

            if(ExpiresLogin._timer == null) {
                ExpiresLogin._timer = new Timer();
            }

            ExpiresLogin._timer.schedule(this, getTimeOutUser() * 60000);
            //System.out.println("Iniciando Sess\u00E3o de Login");
        }else{
            if(ExpiresLogin._timer != null){
                ExpiresLogin._timer.cancel();
                ExpiresLogin._timer = null;
            }
        }
    }

    private Alert getAlert(){
        if(getTimeOutUserTitle() != null
        && getTimeOutUserMessage() != null){
            if(ExpiresLogin._alert == null){
                ExpiresLogin._alert = new Alert(null, null, null, AlertType.INFO);
                ExpiresLogin._alert.setTimeout(Alert.FOREVER);
            }

            ExpiresLogin._alert.setTitle(getTimeOutUserTitle());
            ExpiresLogin._alert.setString(getTimeOutUserMessage());
        }else{
            ExpiresLogin._alert = null;
        }

        return ExpiresLogin._alert;
    }

    /**
     * Retorna o valor para Time Out do Login do Usuário
     * @return Valor de expiração do Login do Usuário
     */
    private static int getTimeOutUser() {
        return ExpiresLogin._timeOutUser;
    }

    /**
     * Retorna a mensagem de aviso de expiração do Login do Usuário
     * @return Valor de expiração do Login do Usuário
     */
    private static String getTimeOutUserMessage() {
        return ExpiresLogin._timeOutUserMessage;
    }

    /**
     * Retorna o título da mensagem de aviso de expiração do Login do Usuário
     * @return Valor de expiração do Login do Usuário
     */
    private static String getTimeOutUserTitle() {
        return ExpiresLogin._timeOutUserTitle;
    }

    public static void setTimeOutUser(int timeOutUser) {
        ExpiresLogin._timeOutUser = timeOutUser;
    }

    public static void setTimeOutUserMessage(String timeOutUserMessage) {
        ExpiresLogin._timeOutUserMessage = timeOutUserMessage;
    }

    public static void setTimeOutUserTitle(String timeOutUserTitle) {
        ExpiresLogin._timeOutUserTitle = timeOutUserTitle;
    }

    public static Displayable getDisplayable() {
        return _displayable;
    }

    public static void setPreviousDisplayables(Hashtable _previousDisplayables) {
        ExpiresLogin._previousDisplayables = _previousDisplayables;
    }

//    public static void setDisplayableKeyHashTable(Displayable _displayableKeyHashTable) {
//        ExpiresLogin._displayableKeyHashTable = _displayableKeyHashTable;
//    }

    /**
     * Retora 'True' caso o Login esteja expirado, e retorna 'False' caso o Login ainda não esteje expirado
     * @return
     */
    public static boolean isLoginExpired() {
        return _loginExpired;
    }

    public static void setWaitScreen(Form _frmWaitScreen) {
        ExpiresLogin._frmWaitScreen = _frmWaitScreen;
    }
}
