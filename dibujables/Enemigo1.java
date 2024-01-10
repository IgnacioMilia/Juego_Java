package dibujables;

import java.awt.geom.Point2D;
import entorno.Herramientas;
import juego.DisenioDeNivel;

public class Enemigo1 extends Colisionable {
	protected double velocidad = 3;
	protected Point2D.Double direccion = new Point2D.Double(4, 1);
	protected double disparadCada = 2200;
	protected double disparadoAl = System.currentTimeMillis();
	private boolean huyendo;
	
	public Enemigo1(double x, double y) {
		super(x, y);
		categorias.add(Categoria.ENEMIGO);
		this.duracionDeMuerte = 200;
	}

	// Movimiento del personaje
	public void moved() {
		// Corrije velocidad para que sea la misma hacia todas las direcciones
		double magnitud = Math.sqrt(direccion.x * direccion.x + direccion.y * direccion.y);
		double turbo = 1;
		this.setX(getX() + velocidad * turbo * direccion.x / magnitud);
		this.setY(getY() + velocidad * turbo * direccion.y / magnitud);
		// Rebota en los bordes
		if (this.getBordeIzquierdo() <= 0) {
			direccion.x = direccion.x > 0 ? direccion.x : -direccion.x;
		} else {
			if (this.getBordeDerecho() >= 800) {
				direccion.x = direccion.x < 0 ? direccion.x : -direccion.x;
			} else {
				if (this.getBordeInferior() >= 300) {
					direccion.y = direccion.y > 0 ? -direccion.y : direccion.y;
				} else {
					if (this.getBordeSuperior() <= 0) {
						direccion.y = direccion.y < 0 ? -direccion.y : direccion.y;
					}
				}
			}
		}
	}

	// Si no se está ejecutando su animación de muerte se mueve y luego dispara si es posible
	public void actualizad() {
		// Si está muriendo espera a que termine su animación y no ejecuta ninguna otra acción
		if(estaMuriendo()) {
			if(terminoLaAnimacionDeMuerte()) {
				morid();
			}
			return;
		}
		// Si está huyendo no ejecuta otra acción
		if(huyendo) {
			huid();
			return;
		}
		moved();
		disparad();
	}

	// Dispara
	public void disparad() {
		if (disparadoAl + disparadCada < System.currentTimeMillis()) {
			new Proyectil(x, y + 60, Categoria.ENEMIGO);
			Herramientas.play("recursos/sonidos/disparos_alienigenas.wav");
			disparadoAl = System.currentTimeMillis();
		}
	}

	// Cambia 180° su dirección
	public void cambiarDireccion() {
		direccion.x *= -1;
		direccion.y *= -1;
	}

	// Muere al colisionar con algo que contenga la categoría JUGADOR
	@Override
	public void onColision(Colisionable c) {
		if (c.getCategorias().contains(Categoria.JUGADOR)) {
			setColisionado(true);
			this.colisionesDeshabilitadas = true;
			muertoAl = System.currentTimeMillis();
			this.setSprite(Herramientas.cargarImagen("recursos/moco.png"));
			this.escala = 1.2;
			DisenioDeNivel.contarKill();
			Herramientas.play("recursos/sonidos/explosion_verde.wav");
			return;
		}
	}

	// Mantiene distancia de los demás personajes
	@Override
	public void onAcercamiento(Colisionable c, double distancia) {
		if (c instanceof Enemigo1 || c instanceof Enemigo2 || c.getCategorias().contains(Categoria.OBSTACULO)) {
			setColisionado(true);
			if (distancia < 20) {
				cambiarDireccion();
			}
			Dibujable d = (Dibujable) c;
			Point2D.Double p = Colisionable.alejarse(x, y, d.x, d.y);
			esquivar(p.x, p.y);
		}
	}

	// Para que se mueva en dirección dx, dy más rápido que lo normal
	public void esquivar(double dx, double dy) {
		double magnitud = Math.sqrt(dx * dx + dy * dy);
		double turbo = 1.5;
		x +=  velocidad * dx * turbo / magnitud;
		y -= velocidad * dy * turbo / magnitud;
	}

	// Para que huyan hacia los costados
	public void huid() {
		if(getBordeIzquierdo() > 800 || getBordeDerecho() < 0) {
			morid();
		}
		x += x < 400 ? -6 : 6;
		y += 2;
		huyendo = true;
	}
}