/*
 * FileConnector.java
 *
 * � MobilePeople, 2003-2005
 * Confidential and proprietary.
 */

package com.j2me.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

/**
 *
 * @author willian
 */
public class FileConnector {

    private final String FILE_SEPARATOR = ( System.getProperty( "file.separator" ) != null ) ? System.getProperty( "file.separator" ) : "/";
    private String path = "file:///store/home/user/";
    private String fileName;
    private String[] content;
    private boolean exists = false;
    // Checks whether the API is available
    private boolean APIAvailable = System.getProperty( "microedition.io.file.FileConnection.version" ) != null;
    private Vector rootsList = new Vector();
    private FileConnection fileConnection = null;

    /**
     * 
     * @param fileName
     */
    public FileConnector( String fileName ) {
        new FileConnector( null, fileName );
    }

    /**
     * 
     * @param path
     * @param fileName
     */
    public FileConnector( String path, String fileName ) {
        loadRoots();

        if ( path != null ) {
            this.path = path;
        }
        this.fileName = fileName;

        try {
            System.out.println("Path File: "+this.path + fileName);
            fileConnection = ( FileConnection ) javax.microedition.io.Connector.open( this.path + fileName, javax.microedition.io.Connector.READ );
            if ( !fileConnection.exists() ) {
                exists = false;
                System.out.println("No exist: "+this.path + fileName);
            } else {
                exists = true;
                System.out.println("exist: "+this.path + fileName);
                //fileConnection.delete();
                fileConnection.close();
            }
        } catch ( IOException ioE ) {
            ioE.printStackTrace();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } finally {
            try {
                if ( fileConnection != null ) {
                    fileConnection.close();
                }
            } catch ( Exception ex ) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 
     * @return
     */
    public boolean exists() {
        return exists;
    }

    /**
     * 
     * @return 
     */
    public boolean readData() {
        boolean read = false;
        InputStream inputStream = null;
        try {
            fileConnection = ( FileConnection ) javax.microedition.io.Connector.open( this.path + fileName, javax.microedition.io.Connector.READ );
            if ( !fileConnection.exists() ) {
                System.out.println( "\nArquivo n�o encontrado." );
            } else {
                read = true;
                System.out.println( "\nArquivo encontrado." );

                inputStream = fileConnection.openInputStream();
                byte byteArray[] = new byte[ 1024 ];
                int length = inputStream.read( byteArray, 0, 1024 );

                String hostServer = null;
                boolean hostServerSSL = false;
                if ( length > 0 ) {
                    String conteudo = new String( byteArray, 0, length );

                    int indexSeparator = 0;
                    String param = "";
                    String result = "";

                    while ( conteudo.indexOf( "\r\n", indexSeparator ) >= 0 ) {

                        param = conteudo.substring( indexSeparator, conteudo.indexOf( "=", indexSeparator ) );

                        result = conteudo.substring( indexSeparator, conteudo.indexOf( "\r\n", indexSeparator ) );

                        System.out.println( "\nParam: " + param );
                        System.out.println( "\nResult: " + result );

                        if ( param.equals( "HOST_SERVER" ) ) {
                            hostServer = result;
                        } else if ( param.equals( "SSL" ) ) {
                            if ( result.equals( "0" ) ) {
                                hostServerSSL = false;
                            } else if ( result.equals( "1" ) ) {
                                hostServerSSL = true;
                            }
                        }

                        indexSeparator = conteudo.indexOf( "\r\n", indexSeparator ) + 1;
                    }
                    if ( indexSeparator < conteudo.length() ) {
                        param = conteudo.substring( indexSeparator, conteudo.indexOf( "=", indexSeparator ) );

                        result = conteudo.substring( indexSeparator );

                        System.out.println( "\nParam: " + param );
                        System.out.println( "\nResult: " + result );

                        if ( param.equals( "HOST_SERVER" ) ) {
                            hostServer = result;
                        } else if ( param.equals( "SSL" ) ) {
                            if ( result.equals( "false" ) ) {
                                hostServerSSL = false;
                            } else if ( result.equals( "true" ) ) {
                                hostServerSSL = true;
                            }
                        }
                    }
                } else {
                    hostServer = "";
                    hostServerSSL = false;
                }

                this.content = new String[] { hostServer, String.valueOf( hostServerSSL ) };
            }
        } catch ( IOException ioE ) {
            ioE.printStackTrace();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } finally {
            try {
                if ( inputStream != null ) {
                    inputStream.close();
                }

                if ( fileConnection != null ) {
                    //fileConnection.delete();
                    fileConnection.close();
                }
            } catch ( Exception ex ) {
                ex.printStackTrace();
            }
        }
        return read;
    }

    /**
     * 
     * @param hostServer
     * @param hostServerChannelSSL
     */
    public void writeData( String hostServer, boolean hostServerChannelSSL ) {
        writeData( hostServer, hostServerChannelSSL, false );
    }

    /**
     * 
     * @param hostServer
     * @param hostServerChannelSSL
     * @param truncateData
     */
    public void writeData( String hostServer, boolean hostServerChannelSSL, boolean truncateData ) {
        OutputStream outputStream = null;
        StringBuffer dataOutputStream = new StringBuffer();
        try {
            fileConnection = ( FileConnection ) javax.microedition.io.Connector.open( this.path + fileName, javax.microedition.io.Connector.READ_WRITE );
            if ( !fileConnection.exists() ) {
                System.out.println( "\nArquivo n�o encontrado." );
                fileConnection.create();
                System.out.println( "\nArquivo criado." );
            } else {
                System.out.println( "\nArquivo encontrado." );
                
                if ( truncateData ) {
                    fileConnection.truncate( 0 );
                }
            }

            dataOutputStream.append( "HOST_SERVER=" + hostServer );
            dataOutputStream.append( "\r\n" );
            dataOutputStream.append( "SSL=" + String.valueOf( hostServerChannelSSL ) );
            dataOutputStream.append( "\r\n" );

            outputStream = fileConnection.openOutputStream();

            outputStream.write( dataOutputStream.toString().getBytes() );

            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch ( IOException ioE ) {
            ioE.printStackTrace();
        } catch ( Exception ex ) {
            ex.printStackTrace();
        } finally {
            try {
                if ( outputStream != null ) {
                    outputStream.close();
                }

                if ( fileConnection != null ) {
                    fileConnection.close();
                }
            } catch ( Exception ex ) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 
     * @param param
     * @param value
     */
    public void writeData( String param, String value ) {
        OutputStream outputStream = null;
        StringBuffer dataOutputStream = new StringBuffer();
        int lengthReadFile = 0;
        boolean fileExists = false;
        try {
            fileConnection = ( FileConnection ) javax.microedition.io.Connector.open( this.path + fileName, javax.microedition.io.Connector.READ_WRITE );
            if ( !fileConnection.exists() ) {
                System.out.println( "\nArquivo n�o encontrado." );
                fileConnection.create();
                System.out.println( "\nArquivo criado." );
            } else {
                //fileConnection.delete();
                System.out.println( "\nArquivo encontrado." );
                InputStream inputStream = fileConnection.openInputStream();
                fileExists = true;
                byte byteArray[] = new byte[ 1024 ];
                lengthReadFile = inputStream.read( byteArray, 0, 1024 );

                if ( lengthReadFile > 0 ) {
                    dataOutputStream.append( new String( byteArray, 0, lengthReadFile ) );
                }
                    
                fileConnection.truncate( 1 );
            }

            if( value == null ){
                value = "";
            }

            if(fileExists){
                if ( lengthReadFile > 0 ) {
                    int indexParam = dataOutputStream.toString().indexOf( param );
                    int lastIndexParam = -1;
                    
                    if( indexParam >= 0 ){
                        System.out.println("Conteudo:\r\n"+dataOutputStream.toString());
                        String fileContent = null;
                        String fileLastContent = null;
                        
                        System.out.println("Index Param:\r\n"+indexParam);
                        
                        lastIndexParam = dataOutputStream.toString().indexOf( "=", indexParam );
                        System.out.println("Last Param:\r\n"+lastIndexParam);
                        
                        fileContent = dataOutputStream.toString().substring( 0, lastIndexParam+1 );
                        
                        lastIndexParam = dataOutputStream.toString().indexOf( "\r\n", lastIndexParam );
                        System.out.println("Last Param:\r\n"+lastIndexParam);
                        fileLastContent = dataOutputStream.toString().substring( lastIndexParam+1 );
                        /*
                        if( dataOutputStream.toString().length() > lastIndexParam ){
                            fileLastContent = dataOutputStream.toString().substring( lastIndexParam+1 );
                        }else{
                            fileLastContent = "";
                        }*/
                        
                        System.out.println("PreConteudo:\r\n"+fileContent);
                        System.out.println("Value:\r\n"+value);
                        System.out.println("PosConteudo:\r\n"+fileLastContent);
                        
                        dataOutputStream = new StringBuffer();
                        dataOutputStream.append( fileContent );
                        dataOutputStream.append( value );
                        if( !fileLastContent.trim().equals("") ){
                            dataOutputStream.append( fileLastContent );
                        }else{
                            dataOutputStream.append( "\r\n" );
                        }
                        System.out.println("Novo Conteudo:\r\n"+dataOutputStream.toString());
                    }else{
                        dataOutputStream.append( param );
                        dataOutputStream.append( "=" );
                        dataOutputStream.append( value );
                        dataOutputStream.append( "\r\n" );
                    }
                }else{
                    dataOutputStream.append( param );
                    dataOutputStream.append( "=" );
                    dataOutputStream.append( value );
                    dataOutputStream.append( "\r\n" );
                }
            }else{
                dataOutputStream.append( param );
                dataOutputStream.append( "=" );
                dataOutputStream.append( value );
                dataOutputStream.append( "\r\n" );
            }

            outputStream = fileConnection.openOutputStream();

            outputStream.write( dataOutputStream.toString().getBytes() );

            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch ( IOException ioE ) {
            System.out.println("Falha ao escrever Parametro no arquivo.");
            System.out.println("IOException"+ioE);
            System.out.println("Error: "+ioE.getMessage());
            ioE.printStackTrace();
        } catch ( Exception ex ) {
            System.out.println("Falha ao escrever Parametro no arquivo.");
            System.out.println("Exception"+ex);
            System.out.println("Error: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if ( outputStream != null ) {
                    outputStream.close();
                }

                if ( fileConnection != null ) {
                    //fileConnection.delete();
                    fileConnection.close();
                }
            } catch ( Exception ex ) {
                ex.printStackTrace();
            }
        }
    }

    public String getValue(String param){
        String value = "";
        
        StringBuffer dataOutputStream = new StringBuffer();
        int lengthReadFile = 0;
        boolean fileExists = false;
        try {
            fileConnection = ( FileConnection ) javax.microedition.io.Connector.open( this.path + fileName, javax.microedition.io.Connector.READ_WRITE );
            if ( !fileConnection.exists() ) {
                System.out.println( "\nArquivo n�o encontrado." );
            } else {
                InputStream inputStream = fileConnection.openInputStream();
                fileExists = true;
                byte byteArray[] = new byte[ 1024 ];
                lengthReadFile = inputStream.read( byteArray, 0, 1024 );

                if ( lengthReadFile > 0 ) {
                    dataOutputStream.append( new String( byteArray, 0, lengthReadFile ) );
                }
            }

            if(fileExists){
                if ( lengthReadFile > 0 ) {
                    int indexParam = dataOutputStream.toString().indexOf( param );
                    int lastIndexParam = -1;
                    
                    if( indexParam >= 0 ){
                        lastIndexParam = dataOutputStream.toString().indexOf( "=", indexParam );
                        value = dataOutputStream.toString().substring( lastIndexParam+1, dataOutputStream.toString().indexOf( "\r\n", lastIndexParam )+1 );
                    }
                }
            }
        } catch ( IOException ioE ) {
            System.out.println("Falha ao escrever Parametro no arquivo.");
            System.out.println("IOException"+ioE);
            System.out.println("Error: "+ioE.getMessage());
            ioE.printStackTrace();
        } catch ( Exception ex ) {
            System.out.println("Falha ao escrever Parametro no arquivo.");
            System.out.println("Exception"+ex);
            System.out.println("Error: "+ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if ( fileConnection != null ) {
                    fileConnection.close();
                }
            } catch ( Exception ex ) {
                ex.printStackTrace();
            }
        }
        
        return value.trim();
    }

    /**
     * 
     * @return
     */
    public String[] getContent() {
        return content;
    }

    private void loadRoots() {
        System.out.println("FileSystemRegistry loadRoots");
        if ( !rootsList.isEmpty() ) {
            rootsList.removeAllElements();
        }
        try {
            Enumeration roots = FileSystemRegistry.listRoots();
            while ( roots.hasMoreElements() ) {
                String root = FILE_SEPARATOR + ( String ) roots.nextElement();

                rootsList.addElement( root );
                System.out.println("FileSystemRegistry root: "+root);
            }
        } catch ( Throwable e ) {
            e.printStackTrace();
        }
    }

    public boolean isAPIAvailable() {
        return APIAvailable;
    }

    public String getPath() {
        return path;
    }
    
    public void close(){
        if(fileConnection != null){
            try{
                fileConnection.close();
            }catch(java.io.IOException ex){
                ex.printStackTrace();
            }
            fileConnection = null;
        }
    }
}
