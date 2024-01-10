package dibujables;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public abstract class Colisionable extends Dibujable {
	// Lista de todas las instancias de sus subclases
	protected static ArrayList<Colisionable> instancias = new ArrayList<Colisionable>();
	// margen que determina a partir de que distancia llamar a onAcercamiento()
	protected static double margen = 15;
	// para que la superficie de colisión sea más pequeña que el ancho
	protected double radioDeColision = 1;

	// Se agrega a la lista
	public Colisionable(double x, double y) {
		super(x, y);
		instancias.add(this);
	}

	// Da la opción de especificar tamaño
	public Colisionable(double x, double y, double ancho, double alto) {
		super(x, y, ancho, alto);
		instancias.add(this);
	}

	// Comprueba si hay colisiones
	public static void actualizarTodo() {
		Colisionable.checkAll(instancias);
	}

	// Actualiza su lista de instancias conservando las coincidencias con la de Dibujable luego de que haya actualizado la suya
	public static void actualizarListaDeInstancias(ArrayList<Dibujable> dibujables) {
		instancias.retainAll(dibujables);
	}

	// Métodos abstractos que serán llamados al producirse una colisión o acercamiento
	public abstract void onAcercamiento(Colisionable b, double distancia);

	public abstract void onColision(Colisionable c);
	
	// Revisa si dos colisionables están lo suficientemente cerca para que se produzca una colision y si es así llama a los métodos correspondientes
	public static void check(Colisionable a, Colisionable b) {
		if(a.colisionesDeshabilitadas || b.colisionesDeshabilitadas) {
			return;
		}
		double distancia = a.getPosicion().distance(b.getPosicion()); // DA VALORES ELEVADÍSIMOS
		distancia = Math.sqrt((b.getX() - a.getX()) * (b.getX() - a.getX()) + (b.getY() - a.getY()) * (b.getY() - a.getY()));
		double radio1 = a.getRadio() * a.escala * a.radioDeColision;
		double radio2 = b.getRadio() * b.escala * b.radioDeColision;
		distancia = distancia - radio1 - radio2;
		if (distancia < margen) {
			a.onAcercamiento(b, distancia);
			b.onAcercamiento(a, distancia);
			if (distancia < 0) {
				a.onColision(b);
				b.onColision(a);
			}
		}
	}

	// Envía toda la lista de colisionables recibida a check()
	public static void checkAll(ArrayList<Colisionable> colisionables) {
		@SuppressWarnings("unchecked")
		ArrayList<Colisionable> notCheckeds = (ArrayList<Colisionable>) colisionables.clone();
		for (Colisionable c1 : colisionables) {
			c1.setColisionado(false);
			notCheckeds.remove(c1);
			for (Colisionable c2 : notCheckeds) {
				Colisionable.check(c1, c2);
			}
		}
	}
	
	// Obtiene vector que se aleja del objetivo
	public static Point2D.Double alejarse(double x1, double y1, double x2, double y2) {
		return acercarse(x2, y2, x1, y1);
	}
	
	// Obtiene vector que conduce al objetivo
	public static Point2D.Double acercarse(double x1, double y1, double x2, double y2) {
		double dx = x2 - x1;
		double dy = y2 - y1;
		double magnitud = 1; //Math.sqrt(dx * dx + dy * dy);
		return new Point2D.Double(dx / magnitud, dy / magnitud);
	}
	
	// getters y setters
	public static ArrayList<Colisionable> getColisionables() {
		return instancias;
	}
	
	// actualizar contador de instancias de las subclases
	public static int getCantidadDeInstanciasDe(String className) {
		int cnt = 0;
		for(Colisionable d : Colisionable.instancias) {
			if(d.simpleClassName.equals(className)) {
				cnt++;
			}
		}
		return cnt;
	}
}