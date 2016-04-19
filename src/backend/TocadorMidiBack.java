package backend;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import fachada.Fachada;

public class TocadorMidiBack {

	public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    static final int FORMULA_DE_COMPASSO = 0x58;
    
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};
	
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
			sequenciador.getTempoInBPM();
			sequenciador.getTickPosition();
			
			Sequence sequence = Fachada.obterInstancia().getSequencia();
			Track[] tracks = sequence.getTracks();
			
			for(Track track : tracks){
				mostraDados(tracks[1]);
			}
			
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

	private void mostraDados(Track track){
		for (int i=0; i < track.size(); i++) { 
            MidiEvent event = track.get(i);
            System.out.print("@" + event.getTick() + " ");
            MidiMessage message = event.getMessage();
            if (message instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) message;
                System.out.print("Channel: " + sm.getChannel() + " ");
                if (sm.getCommand() == NOTE_ON) {
                    int key = sm.getData1();
                    int octave = (key / 12)-1;
                    int note = key % 12;
                    String noteName = NOTE_NAMES[note];
                    int velocity = sm.getData2();
                    System.out.println("Note on, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                } else if (sm.getCommand() == NOTE_OFF) {
                    int key = sm.getData1();
                    int octave = (key / 12)-1;
                    int note = key % 12;
                    String noteName = NOTE_NAMES[note];
                    int velocity = sm.getData2();
                    System.out.println("Note off, " + noteName + octave + " key=" + key + " velocity: " + velocity);
                } else {
                    System.out.println("Command:" + sm.getCommand());
                }
            } else if (message instanceof MetaMessage){
            	if(((MetaMessage)message).getType()==FORMULA_DE_COMPASSO){
            		MetaMessage metaMessage = (MetaMessage) message;
            		byte[] data = metaMessage.getData();
                    int p = data[0];
                    int q = data[1];
                    System.out.println("Formula de compasso\n\tp: " + p + "\tq:" + q);
            	}
            } else {
                System.out.println("Other message: " + message.getClass());
            }
        }
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
