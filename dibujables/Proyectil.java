package dibujables;

public class Proyectil extends Colisionable {
	protected double velocidad = 8;
	protected Categoria tirador;
	
	public Proyectil(double x, double y, Categoria tirador) {
		super(x, y, 10, 25);
		categorias.add(Categoria.PROYECTIL);
		this.tirador = tirador;
	}

	@Override
	public void actualizad() {
		y += velocidad;
	}

	@Override
	public void onColision(Colisionable c) {
		if (!c.categorias.contains(this.tirador) && !c.categorias.contains(Categoria.PROYECTIL)) {
//			morid();
		}
	}

	@Override
	public void onAcercamiento(Colisionable c, double distancia) {
	}
}
