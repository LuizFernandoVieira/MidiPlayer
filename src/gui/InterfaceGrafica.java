package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import constantes.Constantes;
import fachada.Fachada;

public class InterfaceGrafica extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private static Fachada fachada;
	private boolean deveTocar;
	
	private Botoes botoes;
	private Sliders sliders;
	private Paineis paineis;

	public InterfaceGrafica() {		
		fachada = Fachada.obterInstancia();
		
		CustomizadorInterface.customiza();
		
		botoes = new Botoes();
		sliders = new Sliders();
		paineis = new Paineis(this, botoes, sliders);
	
		adicionaActionListeners();	
		adicionaChangeListener();

		setSize(Constantes.LARGURA, Constantes.ALTURA);
		setLocation(Constantes.POSX, Constantes.POSY);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	void retardo(int miliseg) {
		try {
			Thread.sleep(miliseg);
		} catch (InterruptedException e) {
		}
	}

	@Override
	public void run() {
		double dur;
		double t;
		int pos = 0;

		while (true) {
			if (Fachada.obterInstancia().isSoando()) {
				Sequencer sequencer = Fachada.obterInstancia().getSequenciador();
				dur = sequencer.getMicrosecondLength() / 1000000;
				t = sequencer.getMicrosecondPosition() / 1000000;
				pos = (int) ((t * 100) / dur);
				try {
					sliders.getSliderProgressoInstante().setValue(pos);
					botoes.getBotaoMostradorInstante().setText(formataInstante(t));
					botoes.getBotaoMostradorTick().setText("" + sequencer.getTickPosition());
					botoes.getBotaoMostradorTempo().setText("" + sequencer.getTempoInBPM());
					
					
					retardo(1000);
					if (t >= dur) {
						sliders.getSliderProgressoInstante().setValue(0);
						botoes.getBotaoMostradorInstante().setText(formataInstante(0));

						botoes.getBotaoAbrir().setEnabled(true);
						botoes.getBotaoTocar().setEnabled(true);
						botoes.getBotaoTocar().setEnabled(false);
						botoes.getBotaoParar().setEnabled(false);
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}

			else {
				try {
					retardo(1000);
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}

	}    
		
    public String formataInstante(double t1) {
    		String inicio    = "";

    		double h1  = (int)(t1/3600.0);
    		double m1  = (int)((t1 - 3600*h1)/60);
    		double s1  = (t1 - (3600*h1 +60*m1));

    		double h1r  = t1/3600.0;
    		double m1r  = (t1 - 3600*h1)/60.0f;
    		double s1r  = (t1 - (3600*h1 +60*m1));

		String sh1 = "";
		String sm1 = "";
		String ss1 = "";

		if (h1 == 0) {
			sh1 = "00";
		} else if (h1 < 10) {
			sh1 = "0" + reformata(h1, 0);
		} else if (h1 < 100) {
			sh1 = "" + reformata(h1, 0);
		} else {
			sh1 = "" + reformata(h1, 0);
		}

		if (m1 == 0) {
			sm1 = "00";
		} else if (m1 < 10) {
			sm1 = "0" + reformata(m1, 0);
		} else if (m1 < 60) {
			sm1 = "" + reformata(m1, 0);
		}

		if (s1 == 0) {
			ss1 = "00";
		} else if (s1 < 10) {
			ss1 = "0" + reformata(s1r, 2);
		} else if (s1 < 60) {
			ss1 = reformata(s1r, 2);
		}

		return inicio = "\n" + "   " + sh1 + "h " + sm1 + "m " + ss1 + "s";
    }
    
    public String reformata(double x, int casas) { 
    		DecimalFormat df = new DecimalFormat() ;
    		df.setGroupingUsed(false);
    		df.setMaximumFractionDigits(casas);
    		return df.format(x);
    }

	private void adicionaActionListeners() {
		botoes.getBotaoAbrir().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrir();
				Fachada.obterInstancia().abrir();
			}
		});

		botoes.getBotaoTocar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File arqmidi = new File(botoes.getBotaoMostradorCaminho().getText());
				tocar(arqmidi, Fachada.obterInstancia().getInicio());
				Fachada.obterInstancia().tocar(arqmidi, Fachada.obterInstancia().getInicio());
			}
		});

		botoes.getBotaoPausar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				pausar();
				Fachada.obterInstancia().setInicio(Fachada.obterInstancia().
						getSequenciador().getMicrosecondPosition());
				Fachada.obterInstancia().pausar();
			}
		});

		botoes.getBotaoParar().addActionListener(new ActionListener() {			
			public void actionPerformed(ActionEvent e) {
				parar();
				Fachada.obterInstancia().parar();
			}
		});
	}

	private void adicionaChangeListener() {
		sliders.getSliderVolume().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int valor = (int) source.getValue();

					ShortMessage mensagemDeVolume = new ShortMessage();
					for (int i = 0; i < 16; i++) {
						try {
							mensagemDeVolume.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, valor);
							Receiver receptor = Fachada.obterInstancia().getReceptor();
							if (receptor != null){
								receptor.send(mensagemDeVolume, -1);
							}
						} catch (InvalidMidiDataException e1) {
						}
					}
					Fachada.obterInstancia().setVolumeAtual(valor);
					botoes.setVolumeAtual(valor);
					sliders.setVolumeAtual(valor);
					botoes.getBotaoMostradorValorVolume().setText("" + (Fachada.obterInstancia().
							getVolumeAtual() * 100) / 127 + "%");
				}
			}
		});
	}
	
	public void abrir() {
		JFileChooser selecao = new JFileChooser(".");
		selecao.setFileSelectionMode(JFileChooser.FILES_ONLY);
		selecao.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if (!f.isFile())
					return true;
				String name = f.getName().toLowerCase();
				if (name.endsWith(".mid"))
					return true;
				if (name.endsWith(".midi"))
					return true;
				return false;
			}

			public String getDescription() {
				return "Arquivo Midi (*.mid,*.midi)";
			}
		});
		selecao.showOpenDialog(this);

		botoes.getBotaoMostradorCaminho().setText(selecao.getSelectedFile().toString());
		File arqseqnovo = selecao.getSelectedFile();
		
		Sequence sequencianova;
		try {
			sequencianova = MidiSystem.getSequence(arqseqnovo);
			double duracao = sequencianova.getMicrosecondLength() / 1000000.0d;
			
			botoes.getBotaoMostradorArquivo().setText("Arquivo: \"" + arqseqnovo.getName() + "\"");
			botoes.getBotaoMostradorDuracao().setText("\nDura\u00e7\u00e3o:" + formataInstante(duracao));
		} catch (InvalidMidiDataException | IOException e) {
			e.printStackTrace();
		}

		botoes.getBotaoTocar().setEnabled(true);
		botoes.getBotaoPausar().setEnabled(false);
		botoes.getBotaoParar().setEnabled(false);
	}
	
	public void tocar(File arqmidi, long inicio) {
		retardo(500);
		
		botoes.getBotaoMostradorArquivo().setText("Arquivo: \"" + arqmidi.getName() + "\"");
		try {
			botoes.getBotaoMostradorDuracao().setText("\nDura\u00e7\u00e3o:" + formataInstante(
				(MidiSystem.getSequence(arqmidi).getMicrosecondLength()) / 1000000)
			);
		} catch (InvalidMidiDataException | IOException e) {
			e.printStackTrace();
		}
		botoes.getBotaoMostradorInstante().setText(formataInstante(0));
		
		botoes.getBotaoAbrir().setEnabled(false);
		botoes.getBotaoTocar().setEnabled(false);
		botoes.getBotaoPausar().setEnabled(true);
		botoes.getBotaoParar().setEnabled(true);
	}
	
	public void pausar() {
		botoes.getBotaoAbrir().setEnabled(false);
		botoes.getBotaoTocar().setEnabled(true);
		botoes.getBotaoPausar().setEnabled(false);
		botoes.getBotaoParar().setEnabled(false);
	}
	
	public void parar() {
		botoes.getBotaoAbrir().setEnabled(true);
		botoes.getBotaoTocar().setEnabled(true);
		botoes.getBotaoPausar().setEnabled(false);
		botoes.getBotaoParar().setEnabled(false);

		sliders.getSliderProgressoInstante().setValue(0);
		botoes.getBotaoMostradorInstante().setText(formataInstante(0));
	}
	
}
