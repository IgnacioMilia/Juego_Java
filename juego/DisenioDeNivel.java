package juego;

import dibujables.Asteroide;
import dibujables.Dibujable;
import dibujables.Enemigo1;
import dibujables.Enemigo2;
import dibujables.Enemigo3;
import dibujables.Nave;
import entorno.Herramientas;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;

public class DisenioDeNivel {
	static Random random = new Random();
    private static boolean jefeLlamado;
    public static long tiempoInicial = System.currentTimeMillis();
    public static int contadorEnemigos = 0;
    public static int puntaje = 0;
    private static int puntajeObjetivo = 10000;
    // Máximo de enemigos, tiempo entre su aparición y tiempo en el que aparecieron
    private static int maxEnemigo1 = 3;
    private static int enemigo1Cada = 2000;
    private static long enemigo1AparecidoAl;
    private static int maxEnemigo2 = 1;
    private static int enemigo2Cada = 6000;
    private static long enemigo2AparecidoAl;
    private static int maxAsteroides= 4;
    private static int asteroideCada = 1000;
    private static long asteroideAparecidoAl;
    // imagenes
    private static Image moneda = Herramientas.cargarImagen("recursos/moneda.png").getScaledInstance(20,20,0);
    private static Image corazon = Herramientas.cargarImagen("recursos/vida.png").getScaledInstance(20,20,0);
    private static Image craneo = Herramientas.cargarImagen("recursos/craneo.png").getScaledInstance(20,20,0);
    private static Image reloj = Herramientas.cargarImagen("recursos/reloj.png").getScaledInstance(20,20,0);

    public static void iniciarJuego() {
        Juego.juegoIniciado = true;
        Juego.perdido = false;
    }

    public static void reiniciarJuego() {
    	Juego.miNave.vidas = 3;
        Dibujable.eliminarTodos();
        Juego.juegoIniciado = false;          
        Juego.miNave = new Nave(400, 540, 100, 80, 10); // Crea una nueva instancia de la nave
        jefeLlamado = false;
        tiempoInicial = System.currentTimeMillis();
        puntaje = 0;
        contadorEnemigos = 0;
    }
    
    public static void contarKill() {
        contadorEnemigos++;
        sumarPuntaje(500); //Suma 500 puntos por enemigo asesinado
        if((System.currentTimeMillis() - tiempoInicial)/1000 > 10) {
        	sumarPuntaje(1000); //Si pasaron mas de 10 segundos suma 1000 puntos extra por enemigo asesinado
        }
        if(contadorEnemigos > 3) {
        	sumarPuntaje(250); //Si se eliminó mas de 3 enemigos suma 250 puntos extra por enemigo asesinado
        }
    }
    
    public static void sumarPuntaje(int cantidad) {
        puntaje += cantidad;
    }
    
	public static void tac() {
	    long tiempoActual = System.currentTimeMillis();
	    long tiempoTranscurrido = tiempoActual - tiempoInicial;

	    if (Juego.perdido || Juego.ganado) {
            reiniciarJuego();
            iniciarJuego();
	    }
	    
		Dibujable.actualizarTodo(Juego.entorno);
		
    	// Dibujar contador de enemigos
    	Juego.entorno.cambiarFont("MONOSPACED", 20, Color.WHITE);
    	Juego.entorno.dibujarImagen(craneo, 20, Juego.entorno.alto() -17, 0);
    	Juego.entorno.escribirTexto(""+contadorEnemigos, 38, Juego.entorno.alto() - 10);
		// Dibujar vidas
//    	Juego.entorno.escribirTexto("Vidas: " + Juego.miNave.vidas, Juego.entorno.ancho() / 2 - 20, Juego.entorno.alto() - 10);
    	if(Juego.miNave.vidas >= 1)
        	Juego.entorno.dibujarImagen(corazon, Juego.entorno.ancho() / 2 - 25, Juego.entorno.alto() -17, 0);
    	if(Juego.miNave.vidas >= 2)
    		Juego.entorno.dibujarImagen(corazon, Juego.entorno.ancho() / 2 - 0, Juego.entorno.alto() -17, 0);
    	if(Juego.miNave.vidas >= 3)
    		Juego.entorno.dibujarImagen(corazon, Juego.entorno.ancho() / 2 + 25, Juego.entorno.alto() -17, 0);
    	// Dibujar puntaje
    	Juego.entorno.dibujarImagen(moneda, Juego.entorno.ancho() -145, Juego.entorno.alto() -17, 0);
    	Juego.entorno.escribirTexto(""+ puntaje +"/"+ puntajeObjetivo, Juego.entorno.ancho() -133, Juego.entorno.alto() - 10);
    	// Dibujar tiempo
    	Juego.entorno.dibujarImagen(reloj, 15, 15, 0);
    	Juego.entorno.escribirTexto("" + tiempoTranscurrido/1000, 28, 22);
    	
		// Si se cumplen las condiciones para llegar al jefe final o si ya fue llamado, se evita que salgan más enemigos
		if(puntaje >= puntajeObjetivo || jefeLlamado) {
//			Juego.ganado = true; if(true) return;	// Descomentar esta linea para saltearse el jefe
			if(!jefeLlamado) {
				Sonido.detenerSonido();
		    	llamadJefe();
			}
			return;
		}
		
		// Si hay menos instancias de cierto <tipo> que max<Tipo> y pasó más tiempo que <tipo>Cada, se crea una nueva instancia
		if (Dibujable.getCantidadDeInstanciasDe("Asteroide") < maxAsteroides && tiempoActual - asteroideAparecidoAl > asteroideCada) {
			asteroideAparecidoAl = tiempoActual;
			new Asteroide(random.nextInt(800), -50);
		}
		if (Dibujable.getCantidadDeInstanciasDe("Enemigo1") < maxEnemigo1 && tiempoActual - enemigo1AparecidoAl > enemigo1Cada) {
			enemigo1AparecidoAl = tiempoActual;
			new Enemigo1(random.nextInt(800), 22);
		}
		if (Dibujable.getCantidadDeInstanciasDe("Enemigo2") < maxEnemigo2 && tiempoActual - enemigo2AparecidoAl > enemigo2Cada) {
			enemigo2AparecidoAl = tiempoActual;
			new Enemigo2(random.nextInt(700) + 50, -75, Juego.miNave);
		}
	}
	
	// Se instania al jefe y se limpia la pantalla de Enemigo1's
    public static void llamadJefe() {
    	jefeLlamado = true;
    	for(Dibujable d : Dibujable.getInstancias()) {
    		if(d instanceof Enemigo1) {
    			((Enemigo1)d).huid();
    		}
    	}
    	new Enemigo3(400, 550);
    }
}

