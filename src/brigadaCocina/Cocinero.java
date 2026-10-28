package brigadaCocina;

public class Cocinero implements Runnable {
	private String plato;
	private int pasos;
	public Cocinero(String plato,int pasos) {
		this.plato=plato;
		this.pasos=pasos;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String nombre_cocinero=Thread.currentThread().getName();
		for (int i = 1;i<pasos;i++) {
			System.out.println(nombre_cocinero+": paso "+i+"/"+ pasos + " de " + plato );
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println(nombre_cocinero + ": interrumpido");
			}
		}
		System.out.println(nombre_cocinero + ": plato terminado (" + plato + ")");
	}
}
