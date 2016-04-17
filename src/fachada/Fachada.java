package fachada;

import java.io.File;

import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import backend.TocadorMidiBack;

public class Fachada {

	private static Fachada instancia;
	private TocadorMidiBack tocadorMidiBack;
	
	public static Fachada obterInstancia() {
		if(instancia == null) {
			instancia = new Fachada();
		}
		
		return instancia;
	}
	
	public Fachada() {
		tocadorMidiBack = new TocadorMidiBack();
	}
	
	public void abrir() {
		tocadorMidiBack.abrir();
	}
	
	public void tocar(File arqmidi, long inicio) {
		tocadorMidiBack.tocar(arqmidi, inicio);
	}
	
	public void pausar() {
		tocadorMidiBack.pausar();
	}
	
	public void parar() {
		tocadorMidiBack.parar();
	}
	
	public Sequencer getSequenciador() {
		return tocadorMidiBack.getSequenciador();
	}

	public void setSequenciador(Sequencer sequenciador) {
		this.tocadorMidiBack.setSequenciador(sequenciador);
	}

	public Sequence getSequencia() {
		return tocadorMidiBack.getSequencia();
	}

	public void setSequencia(Sequence sequencia) {
		this.tocadorMidiBack.setSequencia(sequencia);
	}

	public Receiver getReceptor() {
		return tocadorMidiBack.getReceptor();
	}

	public void setReceptor(Receiver receptor) {
		this.tocadorMidiBack.setReceptor(receptor);
	}

	public long getInicio() {
		return tocadorMidiBack.getInicio();
	}

	public void setInicio(long inicio) {
		this.tocadorMidiBack.setInicio(inicio);
	}

	public int getVolumeAtual() {
		return tocadorMidiBack.getVolumeAtual();
	}

	public void setVolumeAtual(int volumeAtual) {
		this.tocadorMidiBack.setVolumeAtual(volumeAtual);
	}

	public boolean isSoando() {
		return tocadorMidiBack.isSoando();
	}

	public void setSoando(boolean soando) {
		this.tocadorMidiBack.setSoando(soando);
	}
	
}
