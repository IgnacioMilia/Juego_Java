package dibujables;

import java.awt.Image;

// Subclase básica de Dibujable para poder mostrar en pantalla objetos simples con imagen variable
public class DibujableBasico extends Dibujable{
	
	public DibujableBasico(Image sprite, double x, double y, double ancho, double alto) {
		super(x, y, ancho, alto);
		this.sprite = sprite.getScaledInstance((int)ancho, (int)alto, Image.SCALE_DEFAULT);
	}
	
	@Override
	public void actualizad() {
	}
}
