package juego;

import dibujables.Nave;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;
import java.awt.Image;

public class Juego extends InterfaceJuego {
    public static Juego juego;
    public static Entorno entorno;
    boolean bossInicializado = false;
    long startAt;
    public static Nave miNave;
    public static boolean perdido;
    public static boolean ganado;
    private Menu menu;
    public static boolean juegoIniciado;
    private Image fondo;
    private boolean enMenuPrincipal = true;

    Juego() {
        Juego.entorno = new Entorno(this, "Lost Galaxian - Grupo 1 - v1", 800, 600);
        this.fondo = Herramientas.cargarImagen("recursos/fondojuego.jpg").getScaledInstance(entorno.ancho(), entorno.alto(), Image.SCALE_DEFAULT);
        Juego.miNave = new Nave(400, 540, 100, 80, 10);
        menu = new Menu(entorno);
        Juego.entorno.iniciar();
        Sonido.cargarSonido("src/recursos/sonidos/suspenso.wav"); // Cargar el sonido
        Sonido.reproducirSonido(); // Reproducir el sonido de fondo
    }
    

    public void tick() {
        if (!juegoIniciado) {
            entorno.dibujarImagen(fondo, entorno.ancho() / 2, entorno.alto() / 2, 0);
            menu.dibujar();
            if (entorno.sePresiono(entorno.TECLA_ENTER)) {
                DisenioDeNivel.iniciarJuego();
 //               enMenuPrincipal = false; // Sale del menú principal al iniciar el juego
            }
        } else if (perdido) {
            menu.GAME_OVER();
            if (entorno.sePresiono(entorno.TECLA_ENTER)) {
                DisenioDeNivel.reiniciarJuego();
 //               enMenuPrincipal = true; // Vuelve al menú principal al reiniciar el juego
                Sonido.reproducirSonido(); // Vuelve a reproducir el sonido de fondo
            } else if (entorno.sePresiono(entorno.TECLA_ESPACIO)) {
                DisenioDeNivel.reiniciarJuego();
                DisenioDeNivel.iniciarJuego();
//                enMenuPrincipal = false; // Sale del menú principal al reiniciar y comenzar el juego
                Sonido.reproducirSonido(); // Vuelve a reproducir el sonido de fondo
            }
        } else if (ganado) {
            menu.WIN();
            if (entorno.sePresiono(entorno.TECLA_ENTER) /*&& !enMenuPrincipal*/) {
                DisenioDeNivel.reiniciarJuego();
 //               enMenuPrincipal = true; // Vuelve al menú principal al ganar el juego y presionar Enter
                ganado = false;
                Sonido.reproducirSonido(); // Vuelve a reproducir el sonido de fondo
            }
            
        } else {
            entorno.dibujarImagen(fondo, entorno.ancho() / 2, entorno.alto() / 2, 0);
            DisenioDeNivel.tac();
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Juego.juego = new Juego();
    }
}
