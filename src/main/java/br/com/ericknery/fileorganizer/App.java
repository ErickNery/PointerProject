package br.com.ericknery.fileorganizer;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import org.apache.commons.digester3.Digester;

public class App {
	
	public static void main(String Args[]) {

		// Limite para evitar algum lock
		Calendar tempoLimite = GregorianCalendar.getInstance();
		tempoLimite.add(Calendar.MINUTE, 1);

		ExecutorService executor = Executors.newSingleThreadExecutor();
		executor.execute(new Watchdog());
		
		if (System.currentTimeMillis() > tempoLimite.getTimeInMillis()) {
			System.out.println("Tempo limite alcancado!");
			System.out.println("Saindo do programa");
			
			System.exit(0);
		}
	}
}
