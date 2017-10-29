package br.com.ericknery.fileorganizer;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import br.com.ericknery.fileorganizer.exception.ArquivoInvalidoException;
import br.com.ericknery.fileorganizer.exception.ArquivoNaoEncontradoException;

public class Watchdog implements Runnable {

	WatchService watcher;
	WatchKey key;
	
	@Override
	public void run() {

		try {
			
			configurarListener();
			
		} catch (IOException | ArquivoNaoEncontradoException | ArquivoInvalidoException e) {
			System.err.println("Excecao do tipo " + e.getMessage() + " encontrada.");
			System.err.println("Detalhamento:");
			e.printStackTrace();
		}


		WatchKey key = null;
		for (;;) {
			
			try {
				System.out.println("Monitoring key");
				key = watcher.take();
				
			} catch (InterruptedException e) {
				
				System.err.println("Excecao do tipo " + e.getMessage() + " encontrada.");
				System.err.println("Detalhamento:");
				e.printStackTrace();
				
			}
			
			for(WatchEvent<?> event: key.pollEvents()) {
				
				System.out.println("Encontrado evento, iniciando tratamento");
				
				WatchEvent.Kind<?> kind = event.kind();
				
				/*
				 * Nao estamos rastreando eventos OVERFLOW, porem eles podem ser gerados sempre
				 * Ou seja, caso um evento destes seja capturado, devemos ignorar
				 */
				if( kind == OVERFLOW)
				{
					System.out.println("Evento do tipo Overflow encontrado");
					continue;
				}
				
				if(event.kind() == ENTRY_CREATE)
				{
					System.out.println("Event tipe = ENTRY_CREATE");
				}
				
				if(event.kind() == ENTRY_MODIFY)
				{
					System.out.println("Event tipe = ENTRY_MODIFY");
				}
				
				WatchEvent<Path> ev = (WatchEvent<Path>)event;
				Path filename = ev.context();
				
				System.out.println("Encontrado um arquivo no caminho "+filename.toAbsolutePath());
			}
			
		    boolean valid = key.reset();
		    if (!valid) {
		        break;
		    }
		}
	}

	public void configurarListener() throws IOException, ArquivoNaoEncontradoException, ArquivoInvalidoException {
		watcher = FileSystems.getDefault().newWatchService();

		String inputPath = "C:\\Users\\user\\Desktop\\PastaEntrada";
		String OutputPath = "C:\\Users\\user\\Desktop\\PastaSaida";

		File dir = new File("C:\\Users\\user\\Desktop\\PastaEntrada");

		if (!dir.exists()) {
			throw new ArquivoNaoEncontradoException("Arquivo nao encontrado no no caminho [" + inputPath + "]");
		}
		if (!dir.isDirectory()) {
			throw new ArquivoInvalidoException("Caminho [" + inputPath + "] especificado deve ser um diretorio");
		}

//		key = dir.toPath().register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
		dir.toPath().register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
	}
}
