package dibujables;

import java.awt.Image;
import java.io.File;
import java.util.Random;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import entorno.Entorno;
import entorno.Herramientas;
import juego.Juego;

public class Nave extends Colisionable {
    private int ancho;
    private int alto;
    private double velocidad;
    private Image sprite;
    private ProyectilPj proyectil;
    private Clip sonidoDisparo;
    private Clip sonidoExplosion;
    public int vidas;
    public long vidasRestadasAl;
    
    //Constructor de la nave
    public Nave(double x, double y, int ancho, int alto, double velocidad) {
        super(x, y, ancho, alto);
        categorias.add(Categoria.JUGADOR);
        this.ancho = ancho;
        this.alto = alto;
        this.escala = 0.8;
        this.velocidad = velocidad;
        this.vidas = 3;
        this.sprite = Herramientas.cargarImagen("recursos/nave.png").getScaledInstance(this.ancho, this.alto, Image.SCALE_DEFAULT);
        this.sonidoDisparo = cargarSonido("src/recursos/sonidos/disparos.wav");
        this.sonidoExplosion = cargarSonido("src/recursos/sonidos/explosion.wav");
    }

    // Carga un archivo de sonido y devuelve el Clip correspondiente
    private Clip cargarSonido(String ruta) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(ruta));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void actualizad() {
    	// Para que se deje de actualizar al estar reproduciéndose su animación de muerte
        if (estaMuriendo()) {
            if (terminoLaAnimacionDeMuerte()) {
                morir();
            }
            return;
        }
        
        // Para que titile al recibir daño y vuelva a la normalidad luego de un segundo
        if(categorias.contains(Categoria.INVULNERABLE)) {
        	if(System.currentTimeMillis() - vidasRestadasAl > 1000) {
        		categorias.remove(Categoria.INVULNERABLE);
        		this.visible = true;
        	}
        	else {
        		this.visible = new Random().nextInt(10) > 2;
        	}
        }

        Entorno entorno = Juego.entorno;
        mover();
        if (Juego.entorno.sePresiono(entorno.TECLA_ESPACIO)) {
            disparar();
        }
    }

    @Override
    public void dibujad() {
    	// Dibuja la nave en el entorno
        Entorno entorno = Juego.entorno;
        entorno.dibujarImagen(this.sprite, this.getX(), this.getY(), rotacion, escala);
    }
    
    // Mueve la nave según las teclas presionadas
    public void mover() {
        Entorno entorno = Juego.entorno;
        if ((entorno.estaPresionada(entorno.TECLA_DERECHA) || entorno.estaPresionada('D')) && this.getX() + this.ancho / 2 + this.velocidad < entorno.ancho() + 1) {
            this.setX(this.getX() + this.velocidad);
        }
        if ((entorno.estaPresionada(entorno.TECLA_IZQUIERDA) || entorno.estaPresionada('A')) && this.getX() - this.ancho / 2 - this.velocidad > -1) {
            this.setX(this.getX() - this.velocidad);
        }
        moverProyectil();
    }

    // Dispara un proyectil desde la nave
    public void disparar() {
        int anchoProyectil = 10;
        int altoProyectil = 25;
        int velocidadProyectil = 5;
        // Crea un proyectil si no hay uno en movimiento
        if (this.proyectil == null) {
            double proyectilX = this.getX();
            double proyectilY = this.getY() - (this.alto / 2) - (altoProyectil / 2);
            this.proyectil = new ProyectilPj(proyectilX, proyectilY, anchoProyectil, altoProyectil, velocidadProyectil);
            reproducirSonido(sonidoDisparo);
        }
    }

    private void reproducirSonido(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // Mueve el proyectil de la nave y verifica si se ha salido de la pantalla
    public void moverProyectil() {
        if (this.proyectil != null) {
            this.proyectil.mover();
            if (this.proyectil.getY() < 0) {
                this.proyectil = null;
            }
        }
    }

    @Override
    public void onColision(Colisionable c) {
        if (!c.categorias.contains(Categoria.JUGADOR) && !c.categorias.contains(Categoria.ITEM)) {
            this.colisionado = true;
            // Si este objeto no es "invulnerable" y el colisionador no es "blando"
            if (!this.categorias.contains(Categoria.INVULNERABLE) && !c.categorias.contains(Categoria.BLANDO)) {
                restarVida();
            }
        }
    }

    public void morid() {
    	morir();
    }
   
    private void morir() {
        // Marca el juego como perdido si no quedan vidas
        Juego.perdido = true;
    }

    // Resta una vida a la nave y verifica si se han agotado las vidas
    public void restarVida() {
    	// sólo resta vidas cada 1500 milisegundos
        if(System.currentTimeMillis() - vidasRestadasAl > 1500) {
        	vidasRestadasAl = System.currentTimeMillis();
        	vidas--;
        	if(vidas > 0)
        		Herramientas.play("recursos/sonidos/psh.wav");
        }
        if (Juego.miNave.vidas <= 0) {
        	this.sprite = Herramientas.cargarImagen("recursos/Enemigo2_explosion.png");
        	reproducirSonido(sonidoExplosion);
        	muertoAl = System.currentTimeMillis();
        }
        else {
        	categorias.add(Categoria.INVULNERABLE);
        }
    }
    
    // Getters y setters

    public double getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public double getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    @Override
    public void onAcercamiento(Colisionable b, double distancia) {

    }
}
