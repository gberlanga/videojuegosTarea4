/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tarea4;

import java.applet.Applet;
import java.applet.AudioClip;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
/**
 *
 * @author gberlanga
 */
public class Tarea4 extends JFrame implements Runnable, KeyListener {
        public class Puntaje {
       
        private String nombre;
        private int puntaje;
       
        public Puntaje() {                            
                nombre = "";
                puntaje = 0;
        }
       
        public Puntaje(String nombre, int puntaje) {                      
                this.nombre = nombre;
                this.puntaje = puntaje;
        }
       
        public void setNombre(String nombre) {                              
                this.nombre = nombre;
        }                  
        public void setPuntaje(int puntaje) {                
                this.puntaje = puntaje;
        }
 
        public String getNombre() {          
                return nombre;
        }              
       
        public int getPuntaje() {          
                return puntaje;
        }
       
        public String toString(){                  
                return "" + getPuntaje() + "," + getNombre();
        }
}
        
         private final int iMAXANCHO = 10; // maximo numero de personajes por ancho
    private final int iMAXALTO = 8;  // maxuimo numero de personajes por alto
    private Base basPrincipal;         // Objeto principal      
    private Base basGameOver;           //imagen de gameover
    private LinkedList<Base> lklChimpy; //lista de chimpys
    private LinkedList<Base> lklDiddy;  // lista de diddys
    // se definen boleaanas para cada direccion 
    private boolean bIzq;
    private boolean bDer;
    private boolean bArri;
    private boolean bAbaj;
    private boolean bPause;         //boolean que prende si esta en pausa
    private boolean bGameOver;   // booleana que prende cuando termina el juego
    private int iVidas;           // EL numero de vidas
    private int iPuntos;            // el numero de puntos
    private int iAzar;           // se usa para almacenar numeros aleatorios
    private int iPosX;          // se almacena alguna posicion en x
    private int iPosY;          // se almacena alguna posicion en Y
    private int iSpeed;         // velocidad de chimpy y diddy
    
    /* objetos para manejar el buffer del Applet y este no parpadee */
    private Image    imaImagenApplet;   // Imagen a proyectar en Applet	
    private Graphics graGraficaApplet;  // Objeto grafico de la Imagen
    private AudioClip adcSonidoChimpy;   // Objeto sonido de Chimpy
    private AudioClip adcSonidoDiddy;    // Objeto sonido de Diddy

    private Vector vec;    // Objeto vector para agregar el puntaje.
    private String nombreArchivo;    //Nombre del archivo.
    private String[] arr;    //Arreglo del archivo divido.
    private long tiempoActual;  //Tiempo de control de la animación
    
    public Tarea4(){
        init();
        start();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void start () {
        //Declaras un hilo
        Thread th = new Thread (this);
        //empieza el hilo
        th.start();
    }
    
    public void init(){
        nombreArchivo = "Puntaje.txt";
        vec = new Vector();
        setSize(800,500);
        
        //se inicializan todas las booelanas en falso
        bIzq = false;
        bDer = false;
        bArri = false;
        bAbaj = false;
        // se inicializan las vidas y los puntos
        iAzar = (int) (Math.random() * 3 + 4);
        iVidas = iAzar;
        iPuntos = 0;
        // se inicializan las posiciones X y Y
        iPosX = 0;
        iPosY = 0;
        // se define la velocidad inicial
        iSpeed = 2;
        //se crean las listas de diddys y chimpys
        lklChimpy = new LinkedList();
        lklDiddy = new LinkedList();
             
	URL urlImagenPrincipal = this.getClass().getResource("juanito.gif");
                
        // se crea el objeto para principal 
	basPrincipal = new Base(0, 0, getWidth() / iMAXANCHO,
                getHeight() / iMAXALTO,
                Toolkit.getDefaultToolkit().getImage(urlImagenPrincipal));

        // se posiciona a principal  en el centro 
        basPrincipal.setX(getWidth()/2);
        basPrincipal.setY(getHeight()/2);
        
        // defino la imagen de chimpy
	URL urlImagenChimpy = this.getClass().getResource("chimpy.gif");
        
        //se crea el grupo de #Azar chimpys
        iAzar = (int) (Math.random() * 4 + 5);
        for (int iI = 0; iI < iAzar; iI++) {
            // se crea el objeto para chimpy
            Base basChimpy = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                    getHeight() / iMAXALTO,
                    Toolkit.getDefaultToolkit().getImage(urlImagenChimpy));
            
            resetRight(basChimpy);
            
            lklChimpy.add(basChimpy);
        }
        
        // defino la imagen de diddy
	URL urlImagenDiddy = this.getClass().getResource("diddy.gif");
        
        //se crea el grupo de #Azar diddys
        iAzar = (int) (Math.random() * 4 + 5);
        for (int iI = 0; iI < iAzar; iI++) {
            // se crea el objeto para diddy
            Base basDiddy = new Base(iPosX,iPosY, getWidth() / iMAXANCHO,
                    getHeight() / iMAXALTO,
                    Toolkit.getDefaultToolkit().getImage(urlImagenDiddy));
            
            resetLeft(basDiddy);
            
            lklDiddy.add(basDiddy);

        }
        // la imagen para game Over
        URL urlImagenGameOver = this.getClass().getResource("gameover.gif");
        
        // se crea el objeto para gameOver
        basGameOver = new Base(iPosX,iPosY, getWidth(), getHeight(),
                    Toolkit.getDefaultToolkit().getImage(urlImagenGameOver));
        
        basGameOver.setX(getWidth()/2 - basGameOver.getAncho()/2);
        basGameOver.setY(getHeight()/2 - basGameOver.getAlto()/2);
        
        //sonido para cuando choque con diddy
        URL urlSonidoDiddy = this.getClass().getResource("monkey2.wav");
        adcSonidoDiddy = Applet.newAudioClip (urlSonidoDiddy);
        
        //sonido para cuando choque con chimpy
        URL urlSonidoChimpy = this.getClass().getResource("monkey1.wav");
        adcSonidoChimpy = Applet.newAudioClip(urlSonidoChimpy);
        
        // se agrega KeyListener porque si no no funciona
        addKeyListener(this);
    }
    
    public void paint (Graphics graGrafico) {
        // Inicializan el DoubleBuffer
        if (imaImagenApplet == null){
                imaImagenApplet = createImage (this.getSize().width,
                        this.getSize().height);
                graGraficaApplet = imaImagenApplet.getGraphics ();
        }
 
        // Actualiza la imagen de fondo.
        URL urlImagenFondo = this.getClass().getResource("Ciudad.png");
        Image imaImagenFondo = Toolkit.getDefaultToolkit().getImage(urlImagenFondo);
         graGraficaApplet.drawImage(imaImagenFondo, 0, 0, getWidth(), getHeight(), this);
 
        // Actualiza el Foreground.
        graGraficaApplet.setColor (getForeground());
        paint1(graGraficaApplet);
 
        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenApplet, 0, 0, this);
    }
    
    public void paint1(Graphics graDibujo){
         // si la imagen ya se cargo
        if (basPrincipal != null && lklChimpy != null && lklDiddy != null) {
            //Dibuja la imagen de principal en el Applet
            basPrincipal.paint(graDibujo, this);
            //Dibuja las chimpys
            for (Base basChimpy : lklChimpy) {
                basChimpy.paint(graDibujo, this);
            }
            //Dibuja los diddys
            for (Base basDiddy : lklDiddy) {
                basDiddy.paint(graDibujo, this);
            }
            
            if (bGameOver) {
                basGameOver.paint(graDibujo, this);
            }
            
            Font stringFont = new Font( "Comic Sans MS", Font.PLAIN, 18 );
            graDibujo.setFont(stringFont);
            graDibujo.drawString("Vidas: " + iVidas, 5, 20);
            graDibujo.drawString("Puntos: " + iPuntos, 5, 38);
                      
        } // sino se ha cargado se dibuja un mensaje 
        else {
                //Da un mensaje mientras se carga el dibujo	
                graDibujo.drawString("No se cargo la imagen..", 20, 20);
        }
    }
    
     public void actualiza(){
        if (bArri) {
            basPrincipal.setY(basPrincipal.getY() - (getHeight()/iMAXALTO));
            bArri = false;
        }
        if (bAbaj) {
            basPrincipal.setY(basPrincipal.getY() + (getHeight()/iMAXALTO));
            bAbaj = false;
        }
        if (bDer) {
            basPrincipal.setX(basPrincipal.getX() + (getWidth()/iMAXANCHO));
            bDer = false;
        }
        if (bIzq) {
            basPrincipal.setX(basPrincipal.getX() - (getWidth()/iMAXANCHO));
            bIzq = false;
        }
        for (Base basChimpy : lklChimpy) {
            basChimpy.setX(basChimpy.getX() - iSpeed);
        }
        for (Base basDiddy : lklDiddy) {
            basDiddy.setX(basDiddy.getX() + iSpeed);
        }
        if (iVidas <= 0) {
            bGameOver = true;
        }
    }
    
     /**
     * checaColision
     * 
     * Metodo usado para checar la colision entre objetos
     * 
     */
    public void checaColision(){
        if (basPrincipal.getX() < 0) {
            basPrincipal.setX(basPrincipal.getX() + (getWidth()/iMAXANCHO));
        }
        if (basPrincipal.getX() > (getWidth() - basPrincipal.getAncho())) {
            basPrincipal.setX(basPrincipal.getX() - (getWidth()/iMAXANCHO));
        }
        if (basPrincipal.getY() < 0) {
            basPrincipal.setY(basPrincipal.getY() + (getHeight()/iMAXALTO));
        }
        if (basPrincipal.getY() > (getHeight() - basPrincipal.getAlto())) {
            basPrincipal.setY(basPrincipal.getY() - (getHeight()/iMAXALTO));
        }
        for (Base basChimpy : lklChimpy) {
            if (basChimpy.intersecta(basPrincipal)) {
                adcSonidoChimpy.play();
                iPuntos += 10;
                resetRight(basChimpy);
            }
            if (basChimpy.getX() < (0 - basChimpy.getAncho())) {
                resetRight(basChimpy);
            }
        }
        for (Base basDiddy : lklDiddy) {
            if (basDiddy.intersecta(basPrincipal)) {
                adcSonidoDiddy.play();
                iVidas -- ;
                iSpeed ++ ;
                resetLeft(basDiddy);
            }
            if (basDiddy.getX() > getWidth()) {
                resetLeft(basDiddy);
            }
        }
    }
	
    /**
     * Metodo resetRight
     * 
     * Metodo que reinicia la posicion de algo a la derecha
     * 
     * @param basParam es el <code>Objeto de la clase Base</code> reseteado
     */
    public void resetRight(Base basParam) {
        basParam.setX((int) (Math.random() * getWidth() * 2) + getWidth());
   //     basParam.setY((int) (Math.random() * (getHeight() - basParam.getAlto())));
        basParam.setY( (((int) (Math.random() * iMAXALTO)) * getHeight( )) /8 );
    }
    /**
     * Metodo resetLeft
     * 
     * Metodo que reinicia la posicion de algo a la izquierda
     * 
     * @param basParam es el <code>Objeto de la clase Base</code> reseteado
     */
    public void resetLeft(Base basParam) {
        basParam.setX((int) (Math.random() * getWidth() * -2));
 //       basParam.setY((int) (Math.random() * (getHeight() - basParam.getAlto())));
        basParam.setY( (((int) (Math.random() * iMAXALTO)) * getHeight( )) /8 );
    }
    
    public void run () {
        /* mientras dure el juego, se actualizan posiciones de jugadores
           se checa si hubo colisiones para desaparecer jugadores o corregir
           movimientos y se vuelve a pintar todo
        */
        while (!bGameOver) {
            if(!bPause) {
                actualiza();
                checaColision();
            }
            repaint();
            try {
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " +
                        iexError.toString());
            }
        }
        repaint();
                // pide el nombre de usuario
                String nombre = JOptionPane.showInputDialog("Cual es tu nombre?");
                JOptionPane.showMessageDialog(null,
                              "El puntaje de " + nombre + " es: " + iPuntos, "PUNTAJE",
                              JOptionPane.PLAIN_MESSAGE);
                try {
 
                      leeArchivo();    //lee el contenido del archivo
                      //Agrega el contenido del nuevo puntaje al vector.
                      vec.add(new Puntaje(nombre,iPuntos));
                      //Graba el vector en el archivo.
                      grabaArchivo();
                } catch(IOException e) {
 
                      System.out.println("Error en " + e.toString());
                }
    }
    
    public void leeArchivo() throws IOException {
                                                         
                BufferedReader fileIn;
                try {
                        fileIn = new BufferedReader(new FileReader(nombreArchivo));
                } catch (FileNotFoundException e){
                        File puntos = new File(nombreArchivo);
                        PrintWriter fileOut = new PrintWriter(puntos);
                        fileOut.println("100,demo");
                        fileOut.close();
                        fileIn = new BufferedReader(new FileReader(nombreArchivo));
                }
                String dato = fileIn.readLine();
                while(dato != null) {  
                                                       
                      arr = dato.split(",");
                      int num = (Integer.parseInt(arr[0]));
                      String nom = arr[1];
                      vec.add(new Puntaje(nom,num));
                      dato = fileIn.readLine();
                }
                fileIn.close();
        }    
     
    public void grabaArchivo() throws IOException {
                                                         
                PrintWriter fileOut = new PrintWriter(new FileWriter(nombreArchivo));
                for (int i = 0; i < vec.size(); i++) {
 
                    Puntaje x;
                    x = (Puntaje) vec.get(i);
                    fileOut.println(x.toString());
                }
                fileOut.close();
        }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Tarea4 t = new Tarea4();
        t.setVisible(true);
    }

    /**
     * keyPressed
     * 
     * Metodo sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al dejar presionada
     * alguna tecla.
     * 
     * @param keyEvent es el <code>KeyEvent</code> que se genera en al presionar.
     * 
     */
    public void keyPressed(KeyEvent keyEvent) {
       // se checa si esta en pausa
        if(keyEvent.getKeyCode() == KeyEvent.VK_P) {
            bPause = !bPause;
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
            bGameOver = true;
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_C) {
            try {
                leeArchivo();
            } catch (IOException ex) {
                Logger.getLogger(Tarea4.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_G) {
            try {
                grabaArchivo();
            } catch (IOException ex) {
                Logger.getLogger(Tarea4.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * keyTyped
     * 
     * Metodo sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar una 
     * tecla que no es de accion.
     * 
     * @param keyEvent es el <code>KeyEvent</code> que se genera en al presionar.
     * 
     */
    public void keyTyped(KeyEvent keyEvent){
    	// no hay codigo pero se debe escribir el metodo
    }
    
    /**
     * keyReleased
     * Metodo sobrescrito de la interface <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al soltar la tecla.
     * 
     * @param keyEvent es el <code>KeyEvent</code> que se genera en al soltar.
     * 
     */
    public void keyReleased(KeyEvent keyEvent){
    	// si presiono flecha para arriba
        if(keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            bArri = true;
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            bAbaj = true;
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            bIzq = true;
        }
        if(keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            bDer = true;
        }
    }
    
}
