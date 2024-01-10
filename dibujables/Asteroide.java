package dibujables;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;
import entorno.Herramientas;
import juego.Juego;

public class Asteroide extends Colisionable {
	private double velocidad;

	public Asteroide(double x, double y) {
		super(x, y);
		this.categorias.add(Categoria.OBSTACULO);
		this.dx = new Random().nextBoolean() ? 1 : -1;
		this.dy = new Random().nextDouble(3)+2;
		this.velocidad = new Random().nextDouble(0.5) +0.4;
		this.escala = 0.8;
//		this.radioDeColision = 0.8;
	}
	public Asteroide(double x, double y, double dx, double dy) {
		this(x, y);
		this.dx = dx;
		this.dy = dy;
	}
	public Asteroide(double x, double y, int ancho, int alto) {
		super(x, y);
		sprite = sprite.getScaledInstance(ancho, alto, Image.SCALE_DEFAULT);
		this.ancho = ancho;
		this.alto = alto;
	}
	
	public Asteroide(double x, double y, int ancho, int alto, double dx, double dy) {
		super(x, y, ancho, alto);
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public void onAcercamiento(Colisionable b, double distancia) {
	}

	@Override
	public void onColision(Colisionable c) {
		if (c instanceof Asteroide) {
			if (getX() < c.getX()) {
				dx = dx > 0 ? -dx : dx;
			}
			if (getX() > c.getX()) {
				dx = dx < 0 ? -dx : dx;
			}
		}
		if (c instanceof Nave) {
			if (getX() < c.getX()) {
				dx = dx > 0 ? -dx : dx;
			}
			if (getX() > c.getX()) {
				dx = dx < 0 ? -dx : dx;
			}
		}
		if ( c instanceof ProyectilPj) {
			Herramientas.play("recursos/sonidos/fomp.wav");
			for(int i = 0; i < 30; i++) {
				Juego.entorno.dibujarCirculo(x + new Random().nextInt(30)-15, y + alto/2 + new Random().nextInt(30)-15, 3, Color.WHITE);
			}
		}
	}

	@Override
	public void actualizad() {
		x += dx * velocidad;
		y += dy * velocidad;
		if (x < 0 + ancho / 2) {
			dx = dx < 0 ? -dx : dx;
		}
		if (x > 800 - ancho / 2) {
			dx = dx > 0 ? -dx : dx;
		}
	}

}
