package backend;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class TocadorMidiBack {

	private Sequencer sequenciador;
	private Sequence sequencia;
	private Receiver receptor = null;
	
	private long inicio = 0;
	private int volumeAtual = 75;
	private boolean soando = false;

	public void abrir() {		
		try {
			if (sequenciador != null && sequenciador.isRunning()) {
				sequenciador.stop();
				sequenciador.close();
				sequenciador = null;
			}			
		} catch (Throwable e1) {
			System.out.println("Erro em carregaArquivoMidi: " + e1.toString());
		}
	}

	public void tocar(File arqmidi, long inicio) {
		try {
			
			sequencia = MidiSystem.getSequence(arqmidi);
			sequenciador = MidiSystem.getSequencer();

			sequenciador.setSequence(sequencia);
			sequenciador.open();			
			sequenciador.start();

			receptor = sequenciador.getTransmitters().iterator().next().getReceiver();
			sequenciador.getTransmitter().setReceiver(receptor);

			long duracao = sequencia.getMicrosecondLength() / 1000000;
			
			sequenciador.setMicrosecondPosition(inicio);

			if (sequenciador.isRunning()) {
				duracao = sequenciador.getMicrosecondLength();
				soando = true;
			} else {
				soando = false;
				sequenciador.stop();
				sequenciador.close();
				inicio = 0L;
				duracao = 0;
			}

		} catch (MidiUnavailableException e1) {
			System.out.println(e1 + " : Dispositivo midi nao disponivel.");
		} catch (InvalidMidiDataException e2) {
			System.out.println(e2 + " : Erro nos dados midi.");
		} catch (IOException e3) {
			System.out.println(e3 + " : O arquivo midi nao foi encontrado.");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void pausar() {
		soando = false;
		sequenciador.stop();
	}
	
	public void parar() {
		soando = false;
		sequenciador.stop();
		sequenciador.close();
		sequenciador = null;
		inicio = 0L;
	}

	public Sequencer getSequenciador() {
		return sequenciador;
	}

	public void setSequenciador(Sequencer sequenciador) {
		this.sequenciador = sequenciador;
	}

	public Sequence getSequencia() {
		return sequencia;
	}

	public void setSequencia(Sequence sequencia) {
		this.sequencia = sequencia;
	}

	public Receiver getReceptor() {
		return receptor;
	}

	public void setReceptor(Receiver receptor) {
		this.receptor = receptor;
	}

	public long getInicio() {
		return inicio;
	}

	public void setInicio(long inicio) {
		this.inicio = inicio;
	}

	public int getVolumeAtual() {
		return volumeAtual;
	}

	public void setVolumeAtual(int volumeAtual) {
		this.volumeAtual = volumeAtual;
	}

	public boolean isSoando() {
		return soando;
	}

	public void setSoando(boolean soando) {
		this.soando = soando;
	}
	
}
