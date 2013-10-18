/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.j2me.utils.io;

//import com.sun.midp.publickeystore.WebPublicKeyStore;
import java.io.IOException;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.HttpsConnection;

// Import GZIPInputStream class
import com.tinyline.util.GZIPInputStream;
import java.util.Timer;
import java.util.TimerTask;
import javax.microedition.io.SocketConnection;
import javax.microedition.pki.CertificateException;

/**
 *
 * @author willian
 */
public class HttpConnector extends Thread {
    private StringBuffer hostConnector;
    private StringBuffer operationConnector;
    private StringBuffer parametersConnector;
    private int responseCode = -1;
    private StringBuffer responseMessage;
    private boolean content = false;
    private boolean sendData = false;
    private boolean open = false;
    private boolean failHTTPS = false;
    private boolean failHTTP = false;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private DataOutputStream dataOutputStream = null;
    public HttpsConnection https = null;
    public HttpConnection http = null;
    public SocketConnection socket = null;
    private String requestMethod = "";
    private boolean waiting = true;
    private boolean secureConnection = false;
    private boolean close = true;

    private StringBuffer URL;

    private int timeOut = 3;
    private ConnectionTimer connectionTimer;
    private Timer timerTimeOut;

    protected String credentials = null;

    public HttpConnector() {
        this(null);
    }

    public HttpConnector( String hostConnector ) {
        this.hostConnector = new StringBuffer( hostConnector );
        this.operationConnector = new StringBuffer();
        this.parametersConnector = new StringBuffer();
    }

    public void run() {
        this.responseMessage = new StringBuffer();

        if (this.hostConnector != null && this.hostConnector.length() > 0) {
            this.close = false;
            URL = new StringBuffer();

            if (!secureConnection) {
                if (!this.hostConnector.toString().substring(0, 7).equals("http://")) {
                    URL.append("http://");
                }
            } else {
                if (!this.hostConnector.toString().substring(0, 8).equals("https://")) {
                    URL.append("https://");
                }
            }
            URL.append(this.hostConnector.toString());

            if (!URL.toString().substring(URL.length() - 1).equals("/")) {
                URL.append("/");
            }

            URL.append(this.operationConnector.toString());

            if (secureConnection) {
                openHTTPS();
            } else {
                openHTTP();
            }
        }else{
            this.responseCode = 0;
            this.content = false;
            this.failHTTP = true;
            this.failHTTPS = true;
            this.responseMessage.append("Endere\u00E7o do Servidor inv\u00E1lido!");
            this.waiting = false;
        }
    }

    public void appendParametersConnector( String parametersConnector ) {
        if ( this.parametersConnector.length() == 0 && ( requestMethod.equals( HttpsConnection.GET ) || requestMethod.equals( HttpConnection.GET ) ) ) {
            this.parametersConnector.append( "?" );
        }

        if ( this.parametersConnector.length() > 0 && ( requestMethod.equals( HttpsConnection.POST ) || requestMethod.equals( HttpConnection.POST ) ) ) {
            this.parametersConnector.append( "&" );
        }
        
        this.parametersConnector.append(parametersConnector);
    }

    private void startConnectionTimeOut(){
        if(timerTimeOut == null){
            connectionTimer = new ConnectionTimer(this);
        }

        if(timerTimeOut == null){
            timerTimeOut = new Timer();
        }

        timerTimeOut.schedule(connectionTimer, timeOut * 60000);
    }

    private void stopConnectionTimeOut(){
        if(timerTimeOut != null){
            try{
                timerTimeOut.cancel();
                timerTimeOut = null;

                connectionTimer.cancel();
                connectionTimer = null;
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void openHTTP() {
        try {
            this.open = true;

            //--------------------------------------
            // Create the connection HTTP
            //--------------------------------------
            http = ( HttpConnection ) javax.microedition.io.Connector.open( URL.toString() );
            //--------------------------------------

            //--------------------------------------
            // Set Request http Method
            //--------------------------------------
            if ( !this.requestMethod.equals( "" ) ) {
                http.setRequestMethod( this.requestMethod );
            } else {
                http.setRequestMethod( HttpConnection.POST );
            }
            //--------------------------------------

            http.setRequestProperty
            ("Content-Type", "application/x-www-form-urlencoded");

            http.setRequestProperty
            ("Content-Length", String.valueOf( this.parametersConnector.toString().length() ) );

            if ( isSendData() && http.getRequestMethod().equals( HttpConnection.POST ) ) {
                // Getting the output stream may flush the headers
                writeRequest(http, this.parametersConnector.toString());
            }

            readResponse(http);
        } catch (CertificateException e) {
            this.responseMessage.append( "Falha na conex\u00E3o com o Servidor("+e.getMessage()+")!\r\n" );
            this.failHTTP = true;
            System.out.println( "Connection: openHttp: URL: " + URL + "\nException: " + e.getMessage() );
            e.printStackTrace();
        } catch (IOException e) {
            this.responseMessage.append( "Falha na conex\u00E3o com o Servidor("+e.getMessage()+")!\r\n" );
            this.failHTTP = true;
            System.out.println( "Connection: openHttp: URL: " + URL + "\nException: " + e.getMessage() );
            e.printStackTrace();
        } catch (SecurityException e) {
            // user permission was denied
            this.responseMessage.append( "Falha na conex\u00E3o com o Servidor("+e.getMessage()+")!\r\n" );
            this.failHTTP = true;
            System.out.println( "Connection: openHttp: URL: " + URL + "\nException: " + e.getMessage() );
            e.printStackTrace();
        } catch (Exception e) {
            this.responseMessage.append( "Falha na conex\u00E3o com o Servidor("+e.getMessage()+")!\r\n" );
            this.failHTTP = true;
            System.out.println( "Connection: openHttp: URL: " + URL + "\nException: " + e.getMessage() );
            e.printStackTrace();
        } finally {
            this.waiting = false;
        }
    }

    private void openHTTPS() {
        try {
            this.open = true;

            //--------------------------------------
            // Create the connection HTTP
            //--------------------------------------
            https = ( HttpsConnection ) javax.microedition.io.Connector.open( URL.toString() );
            //--------------------------------------

            //--------------------------------------
            // Set Request http Method
            //--------------------------------------
            if ( !this.requestMethod.equals( "" ) ) {
                https.setRequestMethod( this.requestMethod );
            } else {
                https.setRequestMethod( HttpsConnection.POST );
            }
            //--------------------------------------

            https.setRequestProperty
            ("Content-Type", "application/x-www-form-urlencoded");

            https.setRequestProperty
            ("Content-Length", String.valueOf( this.parametersConnector.toString().length() ) );

            if ( isSendData() && https.getRequestMethod().equals( HttpsConnection.POST ) ) {
                // Getting the output stream may flush the headers
                writeRequest(https, this.parametersConnector.toString());
            }

            readResponse(https);
        } catch (CertificateException e) {
            this.responseMessage.append( "Falha na conex\u00E3o com o Servidor("+e.getMessage()+")!\r\n" );
            this.failHTTPS = true;
            System.out.println( "Connection: openHttp: URL: " + URL + "\nException: " + e.getMessage() );
            e.printStackTrace();
        } catch (IOException e) {
            this.responseMessage.append( "Falha na conex\u00E3o com o Servidor("+e.getMessage()+")!\r\n" );
            this.failHTTPS = true;
            System.out.println( "Connection: openHttp: URL: " + URL + "\nException: " + e.getMessage() );
            e.printStackTrace();
        } catch (SecurityException e) {
            // user permission was denied
            this.responseMessage.append( "Falha na conex\u00E3o com o Servidor("+e.getMessage()+")!\r\n" );
            this.failHTTPS = true;
            System.out.println( "Connection: openHttp: URL: " + URL + "\nException: " + e.getMessage() );
            e.printStackTrace();
        } catch (Exception e) {
            this.responseMessage.append( "Falha na conex\u00E3o com o Servidor("+e.getMessage()+")!\r\n" );
            this.failHTTP = true;
            System.out.println( "Connection: openHttp: URL: " + URL + "\nException: " + e.getMessage() );
            e.printStackTrace();
        } finally {
            this.waiting = false;
        }
    }

    private void writeRequest(HttpConnection conn,
            String request)
            throws IOException {
        OutputStream out = null;
        try {
            // Getting the output stream may flush the headers
            out = conn.openOutputStream();
            int requestLength = request.length();
            for (int i = 0; i < requestLength; ++i) {
                out.write(request.charAt(i));
            }

            this.responseCode = conn.getResponseCode();
            this.responseMessage.append(conn.getResponseMessage());
            this.responseMessage.append("\r\n");

        } catch (CertificateException e) {
            e.printStackTrace();
            /*
            WebPublicKeyStore webPK = new WebPublicKeyStore(conn.openInputStream());

            webPK.loadCertificateAuthorities();
             */
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // already closing, just ignore
                }
            }
        }
    }

    private void readResponse(HttpConnection conn) throws IOException {
        try {
            startConnectionTimeOut();

            String encoding = conn.getEncoding();
            if (encoding != null && encoding.equals("gzip")) {
                System.out.println("Connection: openHttp: URL: " + URL + "\ngetEncoding: true");
                this.inputStream = new GZIPInputStream(conn.openInputStream());
            } else {
                System.out.println("Connection: openHttp: URL: " + URL + "\ngetEncoding: false");
                this.inputStream = conn.openInputStream();
            }

            this.responseCode = conn.getResponseCode();

            System.gc();
            System.gc();

            //se a requisição http não foi realizada com sucesso, retorna falso
            System.out.println("Connection: openhttp: URL: " + URL + "\n");
            System.out.println("Connection: openhttp: PARAMETERS: " + this.parametersConnector.toString() + "\n");
            if (conn.getResponseCode() != HttpConnection.HTTP_OK) {
                this.responseMessage = new StringBuffer("Falha na conex\u00E3o com o Servidor!");
                switch(conn.getResponseCode()){
                    case HttpConnection.HTTP_BAD_REQUEST:
                        this.responseMessage.append("\r\nSolicita\u00E7\u00E3o (request) incompriens\u00EDvel , protocolo inesistente , tipo incompat\u00EDvel.");
                        break;
                    case HttpConnection.HTTP_NOT_FOUND:
                        this.responseMessage.append("\r\nN\u00E3o foi encontrada a URL solicitada.");
                        break;
                    case HttpConnection.HTTP_BAD_METHOD:
                        this.responseMessage.append("\r\nO servidor n\u00E3o suporta o m\u00E9todo solicitado.");
                        break;
                    case HttpConnection.HTTP_INTERNAL_ERROR:
                        this.responseMessage.append("\r\nErro desconhecido da natureza do servidor.");
                        break;
                    case HttpConnection.HTTP_UNAVAILABLE:
                        this.responseMessage.append("\r\nCapacidade m\u00E1xima do servidor alcan\u00E7ada.");
                        break;
                    default:
                        this.responseMessage.append("\r\nC\u00F3digo do erro: " + conn.getResponseCode());
                        this.responseMessage.append("\r\nDescri\u00E7\u00E3o do erro: " + conn.getResponseMessage());
                        break;
                }

                if (secureConnection) {
                    this.failHTTPS = true;
                } else {
                    this.failHTTP = true;
                }
            } else {
                this.content = true;
                this.responseMessage.append("OK");
            }

            System.out.println("Connection: ResponseMessage: " + this.responseMessage.toString() + "\n");
            System.out.println("Connection: openHttp: URL: " + URL + "\nNo Exception: ");
        } catch (CertificateException e) {
            e.printStackTrace();
            /*
            WebPublicKeyStore webPK = new WebPublicKeyStore(conn.openInputStream());

            webPK.loadCertificateAuthorities();
             */
        } catch(Exception e){
            if (secureConnection) {
                this.failHTTPS = true;
            } else {
                this.failHTTP = true;
            }
            
            this.responseMessage = new StringBuffer("Falha na conex\u00E3o com o Servidor!");
            this.responseMessage.append("\r\nC\u00F3digo do erro: " + conn.getResponseCode());
            this.responseMessage.append("\r\nDescri\u00E7\u00E3o do erro: " + conn.getResponseMessage());
        }

        stopConnectionTimeOut();
    }

    private boolean getData(){
        return getData(false);
    }

    private boolean getData(boolean secureConnection){
        return false;
    }

    private boolean sendData() throws IOException{
        return sendData(false);
    }

    private boolean sendData(boolean secureConnection) throws IOException{
        boolean statusSendData = false;
        int countAttemps = 0;
        try{
            System.out.println( "openOutputStream: Enviando dados para a autenticacao" );

            if(!secureConnection){
                outputStream = http.openOutputStream();
                outputStream.write(parametersConnector.toString().getBytes());
                outputStream.flush();
                outputStream.close();

                // now get the response
                responseCode = http.getResponseCode();
                responseMessage.append( http.getResponseMessage() );
                this.responseMessage.append("\r\n");
                System.out.println("Connection: " + responseCode + "\r\nMessage: " + responseMessage.toString());

                System.gc();
                System.gc();

                if (responseCode != http.HTTP_OK) {
                    this.responseMessage.append( "Falha na conex\u00E3o com o Servidor ao enviar DADOS!" );
                    this.responseMessage.append( "\r\nC\u00F3digo do erro: " + http.getResponseCode() + " - " + http.getResponseMessage() );

                    this.responseMessage = new StringBuffer("Falha na conex\u00E3o com o Servidor!");
                    switch(responseCode){
                        case HttpConnection.HTTP_BAD_REQUEST:
                            this.responseMessage.append("\r\nSolicita\u00E7\u00E3o (request) incompriens\u00EDvel , protocolo inesistente , tipo incompat\u00EDvel.");
                            break;
                        case HttpConnection.HTTP_NOT_FOUND:
                            this.responseMessage.append("\r\nN\u00E3o foi encontrada a URL solicitada.");
                            break;
                        case HttpConnection.HTTP_BAD_METHOD:
                            this.responseMessage.append("\r\nO servidor n\u00E3o suporta o m\u00E9todo solicitado.");
                            break;
                        case HttpConnection.HTTP_INTERNAL_ERROR:
                            this.responseMessage.append("\r\nErro desconhecido da natureza do servidor.");
                            break;
                        case HttpConnection.HTTP_UNAVAILABLE:
                            this.responseMessage.append("\r\nCapacidade m\u00E1xima do servidor alcan\u00E7ada.");
                            break;
                        default:
                            this.responseMessage.append("\r\nC\u00F3digo do erro: " + responseCode);
                            this.responseMessage.append("\r\nDescri\u00E7\u00E3o do erro: " + http.getResponseMessage());
                            break;
                    }
                }else{
                    statusSendData = true;
                }
            }else{
                statusSendData = true;
            }
            countAttemps++;
        }catch(Exception ex){
            if (secureConnection) {
                this.failHTTPS = true;
            } else {
                this.failHTTP = true;
            }

            this.responseMessage = new StringBuffer("Falha na conex\u00E3o com o Servidor!");
            this.responseMessage.append("\r\nC\u00F3digo do erro: " + http.getResponseCode());
            this.responseMessage.append("\r\nDescri\u00E7\u00E3o do erro: " + http.getResponseMessage());
        }finally{
            return statusSendData;
        }
    }

    public void close() {
        System.out.println( "Fechando conex\u00E3o.\n" );
        try {
            if ( this.inputStream != null ) {
                try {
                    this.inputStream.close();
                } catch ( NullPointerException npx ) {
                    npx.printStackTrace();
                }
            }

            if ( this.dataOutputStream != null ) {
                try {
                    this.dataOutputStream.close();
                } catch ( NullPointerException npx ) {
                    npx.printStackTrace();
                }
            }

            if ( this.outputStream != null ) {
                try {
                    this.outputStream.close();
                } catch ( NullPointerException npx ) {
                    npx.printStackTrace();
                }
            }

            if ( this.https != null ) {
                try {
                    this.https.close();
                } catch ( NullPointerException npx ) {
                    npx.printStackTrace();
                }
            }

            if ( this.http != null ) {
                try {
                    this.http.close();
                } catch ( NullPointerException npx ) {
                    npx.printStackTrace();
                }
            }
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } finally {
            this.open = false;
            this.close = true;
        }
    }

    public boolean isClose() {
        return close;
    }

    public boolean isContent() {
        return content;
    }

    public boolean isFailHTTP() {
        return failHTTP;
    }

    public boolean isFailHTTPS() {
        return failHTTPS;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isSendData() {
        return sendData;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return this.responseMessage.toString();
    }

    public StringBuffer getParametersConnector() {
        return parametersConnector;
    }

    public String getUrlConnector() {
        return this.hostConnector.toString();
    }

    public boolean isWaiting() {
        return waiting;
    }

    public void setSendData( boolean sendData ) {
        this.sendData = sendData;
    }

    public void setDataOutputStream( DataOutputStream dataOutputStream ) {
        this.dataOutputStream = dataOutputStream;
    }

    public void setOperationConnector( String operationConnector ) {
        this.operationConnector.append( operationConnector );
    }

    public void setParametersConnector( String parametersConnector ) {
        this.parametersConnector = new StringBuffer( "?" );
        this.parametersConnector.append( parametersConnector );
    }

    public void setRequestMethod( String requestMethod ) {
        this.requestMethod = requestMethod;
    }

    public void setSecureConnection( boolean secureConnection ) {
        this.secureConnection = secureConnection;
    }

    public void setUrlConnector( String urlConnector ) {
        this.hostConnector = new StringBuffer();
        this.hostConnector.append( urlConnector );
    }

    private class ConnectionTimer extends TimerTask{
        private HttpConnector httpConnector = null;

        public ConnectionTimer(HttpConnector httpConnector){
            this.httpConnector = httpConnector;
        }

        public void run(){
            this.httpConnector.close();

            //throw new Exception("N\u00E3o foi poss\u00EDvel atender a requisi\u00E7\u00E3o! A sua sess\u00E3o foi interrompida (time out).");
        }
    }

    //Contantes dos cabe�alhos HTTP.
    //Usado apenas em requisi��es do tipo POST.
    public static String CAB_CONTENT_TYPE = "Content-Type";
    public static String CAB_CONTENT_TYPE_FORM_URLENCODED = "application/" +
        "x-www-form-urlencoded";    //Usado apenas em requisi��es do tipo POST.
    public static String CAB_CONTENT_LENGTH = "Content-Length";
    public static String CAB_USER_AGENT = "User-Agent";
    public static String CAB_USER_AGENT_PADRAO = "Mozilla/5.0 (Windows; U; " +
        "Windows NT 5.1; pt-BR; rv:1.8.1.7) Gecko/20070914 Firefox/2.0.0.7";
    public static String CAB_ACCEPT = "Accept";
    public static String CAB_ACCEPT_PADRAO = "application/xml," +
        "application/xhtml+xml,text/html;text/xml;q=0.9,text/plain;q=0.8," +
        "image/png,image/gif,image/jpeg,image/jpg,*/*;q=0.5";
    public static String CAB_ACCEPT_LANGUAGE = "Accept-Language";
    public static String CAB_ACCEPT_LANGUAGE_PADRAO = "pt-br,pt;q=0.8,en-us;" +
        "q=0.5,en;q=0.3";
    public static String CAB_ACCEPT_CHARSET = "Accept-Charset";
    public static String CAB_ACCEPT_CHARSET_PADRAO = "ISO-8859-1,utf-8;q=0.7," +
        "*;q=0.7";
    public static String CAB_CACHE_CONTROL = "Cache-Control";
    public static String CAB_CACHE_CONTROL_PADRAO = "no-transform";
    public static String CAB_CONNECTION = "Connection";
    public static String CAB_CONNECTION_PADRAO = "close";
    /**
     * Padr�o de cabe�alhos HTTP usados nas conex�es.
     * Voc� pode acrescentar outros se desejar.
     */
    public static String[][] CABECALHOS_PADRAO = {
        { CAB_USER_AGENT, CAB_USER_AGENT_PADRAO },
        { CAB_ACCEPT, CAB_ACCEPT_PADRAO },
        { CAB_ACCEPT_LANGUAGE, CAB_ACCEPT_LANGUAGE_PADRAO },
        { CAB_ACCEPT_CHARSET, CAB_ACCEPT_CHARSET_PADRAO },
        { CAB_CACHE_CONTROL, CAB_CACHE_CONTROL_PADRAO },
        { CAB_CONNECTION, CAB_CONNECTION_PADRAO }
    };

    /**
     * Tipo de conte�do enviado numa requisi��o POST. O padr�o �
     * application/x-www-form-urlencoded.
     */
    public String contentType = CAB_CONTENT_TYPE_FORM_URLENCODED;
}