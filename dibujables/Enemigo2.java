package dibujables;

import java.awt.geom.Point2D;
import java.io.File;
import entorno.Herramientas;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

public class Enemigo2 extends Colisionable {
	protected double velocidad = 5;
	protected Point2D.Double direccion = new Point2D.Double(0, 1);
	private long murioAl;
	private long duracionDeExplosion = 100;
	protected double rotacion;
	private Dibujable target;
	private Clip sonidoExplosion;

	public Enemigo2(double x, double y, Dibujable target) {
		super(x, y);
		categorias.add(Categoria.ENEMIGO);
		this.rotacion = Herramientas.radianes(140);
		ancho = 30;
		alto = 80;
		this.escala = 0.8;
		this.x = target.x;
		this.target = target;
		this.sonidoExplosion = cargarSonido("src/recursos/sonidos/explosion.wav");
	}
	
	@Override
	public void dibujad() {
		super.dibujad(this.rotacion, this.escala);
	}
	
	public void moved() {
		perseguid(target);
		if (this.getBordeInferior() >= 580) {
			morid();
			return;
		}
		double magnitud = Math.sqrt(direccion.x * direccion.x + direccion.y * direccion.y);
		double turbo = 1;
		this.setX(getX() + velocidad * turbo * direccion.x / magnitud);
		this.setY(getY() + velocidad * turbo * direccion.y / magnitud);
	}

	// Ajusta su dirección horizontal para acercarse a la posición actual de target
	public void perseguid(Dibujable target) {
		if (target == null) {
			return;
		}
		direccion.x = target.getPosicion().x > x ? 0.4 : -0.4;
	}

	public void actualizad() {
		moved();
	}

	public void cambiarDireccion() {
		direccion.x *= -1;
	}

	public void esquivar(double dx, double dy) {
		double magnitud = Math.sqrt(dx * dx + dy * dy);
		double turbo = 1.7;
		x = x + velocidad * dx * turbo / magnitud;
		y = y + velocidad * dy * turbo / magnitud;
	}

	@Override
	public void onColision(Colisionable c) {
		if(c instanceof Nave) {
			morid();
		}
		if(c instanceof ProyectilPj) {
			Herramientas.play("recursos/sonidos/clac.wav");
		}
	}

	@Override
	public void onAcercamiento(Colisionable c, double distancia) {}

	public void morid() {
		if (murioAl != 0) {
			this.ancho = 40;
			// Si murió hace más de duracionDeExplosion milisegundos
			if (System.currentTimeMillis() - murioAl > duracionDeExplosion) {
				super.morid();
			}
			return;
		}
		this.sprite = Herramientas.cargarImagen("recursos/Enemigo2_explosion.png"); //.getScaledInstance(this.ancho, this.alto, Image.SCALE_DEFAULT);
		this.reproducirSonido(sonidoExplosion);
		ancho = alto = getSprite().getWidth(null) - 10;
		direccion.y = 0;
		murioAl = System.currentTimeMillis();
		return;
	}

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

	private void reproducirSonido(Clip clip) {
		if (clip != null) {
			clip.setFramePosition(0);
			clip.start();
		}
	}

}