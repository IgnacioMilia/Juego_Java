package dibujables;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class ProyectilPj extends Colisionable {
	private double x;
	private double y;
	private int anchoProyectil;
	private int altoProyectil;
	private int velocidadProyectil;
	private Image sprite;

	public ProyectilPj(double x, double y, int anchoProyectil, int altoProyectil, int velocidadProyectil) {
		super(x, y, anchoProyectil, altoProyectil);
		categorias.add(Categoria.JUGADOR);
		categorias.add(Categoria.PROYECTIL);
		this.x = x;
		this.y = y;
		this.anchoProyectil = anchoProyectil;
		this.altoProyectil = altoProyectil;
		this.velocidadProyectil = velocidadProyectil;
		this.sprite = Herramientas.cargarImagen("recursos/Proyectil2.png").getScaledInstance(this.anchoProyectil,this.altoProyectil, Image.SCALE_DEFAULT);
	}

	public void actualizad() {
		mover();
	}

	public void dibujad(Entorno entorno) {
		entorno.dibujarImagen(this.sprite, this.x, this.y, 0, this.escala);
	}

	public void mover() {
		this.y -= velocidadProyectil;
		if (this.y < -altoProyectil) {
			morid();
		}
	}

	// Getters y setters
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getAnchoProyectil() {
		return anchoProyectil;
	}

	public void setAnchoProyectil(int anchoProyectil) {
		this.anchoProyectil = anchoProyectil;
	}

	public int getAltoProyectil() {
		return altoProyectil;
	}

	public void setAltoProyectil(int altoProyectil) {
		this.altoProyectil = altoProyectil;
	}

	public int getVelocidadProyectil() {
		return velocidadProyectil;
	}

	public void setVelocidadProyectil(int velocidadProyectil) {
		this.velocidadProyectil = velocidadProyectil;
	}
	
	public Image getSprite() {
		return sprite;
	}

	@Override
	public void onAcercamiento(Colisionable b, double distancia) {
	}

	@Override
	public void onColision(Colisionable c) {
		if (!c.categorias.contains(Categoria.JUGADOR) && !c.categorias.contains(Categoria.PROYECTIL)) {
			morid();
		}
	}
}
