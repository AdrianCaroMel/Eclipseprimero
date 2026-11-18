package panel;

// los datos del vuelo: sin synchronized, quien los protege es ControlPanel
public class Panel {

	public static final int PREVISTA = 600;

	private int retraso = 0;
	private int salida = PREVISTA;

	public int getRetraso() {
		return retraso;
	}

	public void setRetraso(int retraso) {
		this.retraso = retraso;
	}

	public int getSalida() {
		return salida;
	}

	public void setSalida(int salida) {
		this.salida = salida;
	}

	// pasa minutos desde las 00:00 a texto: 615 -> "10:15"
	public static String hora(int minutos) {
		int h = minutos / 60;
		int m = minutos % 60;
		String texto = h + ":";
		if (m < 10) {
			texto = texto + "0";
		}
		return texto + m;
	}
}