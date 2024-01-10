package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Color;
import java.awt.Image;

public class Menu {
    private Entorno entorno;
    private Image fondoMenu;
    private Image gameOver;
    private Image fondo;
    private static Image win;
    
    public Menu(Entorno entorno) {
        this.entorno = entorno;
        this.fondoMenu = Herramientas.cargarImagen("recursos/fondomenu.jpg").getScaledInstance(entorno.ancho(), entorno.alto(), Image.SCALE_DEFAULT);
        this.gameOver = Herramientas.cargarImagen("recursos/gameover.jpg").getScaledInstance(entorno.ancho(), entorno.alto(), Image.SCALE_DEFAULT);
        this.fondo = Herramientas.cargarImagen("recursos/fondojuego.jpg").getScaledInstance(entorno.ancho(), entorno.alto(), Image.SCALE_DEFAULT);
        Menu.win = Herramientas.cargarImagen("recursos/win.png").getScaledInstance(entorno.ancho()-100, entorno.alto()-100, Image.SCALE_DEFAULT);
    }
    
    //Dibuja el menu principal
    public void dibujar() {
    	entorno.dibujarImagen(fondoMenu, entorno.ancho() / 2, entorno.alto() / 2, 0);
        entorno.cambiarFont("Comic Sans MS", 70, Color.CYAN);
        entorno.escribirTexto("Lost Galaxian", entorno.ancho() / 2 - 220, entorno.alto() / 2 -20);
        entorno.cambiarFont("Century Gothic", 30, Color.PINK);
        entorno.escribirTexto("Presiona ENTER para iniciar", entorno.ancho() / 2 - 200, entorno.alto() / 2 +80);
        entorno.cambiarFont("Century Gothic", 12, Color.WHITE);
        entorno.escribirTexto("Presionar 'C' para ver créditos.", entorno.ancho() / 2 - 100, entorno.alto() -20);
        if(entorno.estaPresionada('C')) {
        	CREDITOS();
        }
    }
    
    //Dibuja el menu que aparece al perder el juego
    public void GAME_OVER() {
    	entorno.dibujarImagen(gameOver, entorno.ancho() / 2, entorno.alto() / 2, 0);
        entorno.cambiarFont("Century Gothic", 30, Color.WHITE);
        entorno.escribirTexto("Presiona ESPACIO para reintentar", entorno.ancho() / 2 - 300, entorno.alto() / 2 +120);
        entorno.escribirTexto("Presiona ENTER para volver al menú", entorno.ancho() / 2 - 300, entorno.alto() / 2 +160);
        entorno.escribirTexto("Presiona SUPR para salir", entorno.ancho() / 2 - 300, entorno.alto() / 2 +200);
        if (entorno.sePresiono(entorno.TECLA_DELETE)) {
            System.exit(0);
        }
    }
    
    //Dibuja el menu que aparece al ganar la partida
    public void WIN() {
    	entorno.dibujarImagen(fondo, entorno.ancho() / 2, entorno.alto() / 2, 0);
    	entorno.dibujarImagen(win, entorno.ancho() / 2, entorno.alto() / 2, 0);
        entorno.cambiarFont("Century Gothic", 30, Color.WHITE);
        entorno.escribirTexto("Presiona ENTER para volver al menú principal", entorno.ancho() / 2 - 300, entorno.alto() / 2 +200);
    }
    
    //Dibuja los creditos que aparecen al presionar 'C' en el menu principal
    public void CREDITOS() {
    	entorno.dibujarRectangulo(entorno.ancho() / 2, entorno.alto() / 2, entorno.ancho(), entorno.alto(), 0, Color.BLACK);
        entorno.cambiarFont("Comic Sans MS", 15, Color.WHITE);
        entorno.escribirTexto("Imágenes cortesía de PNGWING.com", entorno.ancho() / 2 - 220, entorno.alto() / 2 -40);
        entorno.escribirTexto("Sonidos cortesía de ZAPSPLAT.com", entorno.ancho() / 2 - 220, entorno.alto() / 2 -20);
        entorno.escribirTexto("convertio.co", entorno.ancho() / 2 - 220, entorno.alto() / 2 +20);
        entorno.escribirTexto("Audacity", entorno.ancho() / 2 - 220, entorno.alto() / 2 +40);
    }
}