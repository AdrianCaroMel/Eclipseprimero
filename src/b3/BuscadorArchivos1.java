package b3;

import java.io.*;
import java.util.*;

public class BuscadorArchivos1 {
	
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		System.out.print("Dame una carpeta: ");
		String carpeta = sc.nextLine().trim();
		
		System.out.print("Ahora una extension: ");
		String extension = sc.nextLine().trim().toLowerCase();
		
		if(extension.startsWith(".")) {
			extension = extension.substring(1);
		}
		
	    if (extension.isEmpty()) {
	        System.out.println("La extensión es obligatoria");
	        return; 
	    }
	    
	    ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", "dir", "/b", carpeta);
	    
	    BufferedReader brOutput = null;
	    BufferedReader brError = null;
	    
	    try {
	    	Process p = pb.start();
	    	int total = 0;
	    	
	    	brOutput = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    	brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
	    	
	    	String linea;
	    	while ((linea = brError.readLine()) != null) {
	    		System.out.println("ERROR: " + linea);
	    	}
	    	
	    	while ((linea = brOutput.readLine()) != null) {
	    		if (linea.toLowerCase().endsWith("." + extension)) {
	    			total++;
	    			System.out.println(total + ". " + linea);
	    		}
	    	}
	    	
	    	int codigoSalida = p.waitFor();
	    	
	    	if (total == 0 && codigoSalida == 0) {
	    		System.out.println("No hay archivos ." + extension);
	    	}
	    	
	    	System.out.println("Total: " + total + " | Código de salida: " + codigoSalida);
	    	
	    } catch (IOException e) {
	    	System.out.print("ERROR: " + e);
	    } catch (InterruptedException i) {
	    	System.out.print("ERROR: " + i);
	    } finally {
	    	try {
	    		if (brOutput != null) brOutput.close();
	    	} catch (IOException e) {
	    		System.out.print("ERROR al cerrar output: " + e);
	    	}
	    	try {
	    		if (brError != null) brError.close();
	    	} catch (IOException e) {
	    		System.out.print("ERROR al cerrar error: " + e);
	    	}
	    }
	}
}
