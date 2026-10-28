package brigadaCocina;

public class BrigadaCocina {
	public static void main (String[]args) {
		System.out.println("Abre la cocina del hilo: " + Thread.currentThread().getName());
		Thread ana = new Thread(new Cocinero("Paella",4), "Ana");
		Thread luis = new Thread(new Cocinero("Gazpacho",3), "Luis");
		Thread marta = new Thread(new Cocinero("Tarta",5), "Marta");
		
		ana.start();
		
		try {
			ana.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		luis.start();
		
		try {
			luis.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		marta.start();
		
		try {
			marta.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	System.out.println("El estado de Ana al final ha sido: " + ana.getState());
	System.out.println("El estado de Luis al final ha sido: " + luis.getState());
	System.out.println("El estado de Marta al final ha sido: " + marta.getState());
	System.out.println("La jefa de cocina cierra el dia");
	}

}
