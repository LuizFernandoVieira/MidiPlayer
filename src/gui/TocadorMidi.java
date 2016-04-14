package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import constantes.Constantes;

public class TocadorMidi extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private Botoes botoes;

	private Sequencer sequenciador;
	private Sequence sequencia;
	private Receiver receptor = null;
	private long inicio = 0;

	private int volumeAtual = 75;
	private JSlider sliderVolume;
	private JProgressBar sliderProgressoInstante;

	private Container painel;
	private boolean soando = false;

	public TocadorMidi() {
		CustomizadorInterface.customiza();
		
		botoes = new Botoes();
		
		constroiSliders();
		constroiPaineis();
			
		adicionaActionListeners();	
		adicionaChangeListener();

		setSize(Constantes.LARGURA, Constantes.ALTURA);
		setLocation(Constantes.POSX, Constantes.POSY);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
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
		try {
			if (sequenciador != null && sequenciador.isRunning()) {
				sequenciador.stop();
				sequenciador.close();
				sequenciador = null;
			}
			Sequence sequencianova = MidiSystem.getSequence(arqseqnovo);
			double duracao = sequencianova.getMicrosecondLength() / 1000000.0d;

			botoes.getBotaoMostradorArquivo().setText("Arquivo: \"" + arqseqnovo.getName() + "\"");
			botoes.getBotaoMostradorDuracao().setText("\nDura\u00e7\u00e3o:" + formataInstante(duracao));

			botoes.getBotaoTocar().setEnabled(true);
			botoes.getBotaoPausar().setEnabled(false);
			botoes.getBotaoParar().setEnabled(false);
		} catch (Throwable e1) {
			System.out.println("Erro em carregaArquivoMidi: " + e1.toString());
		}
	}
	
	public void tocar(String caminho, long inicio) {
		try {
			File arqmidi = new File(caminho);
			sequencia = MidiSystem.getSequence(arqmidi);
			sequenciador = MidiSystem.getSequencer();

			sequenciador.setSequence(sequencia);
			sequenciador.open();
			retardo(500);
			sequenciador.start();

			receptor = sequenciador.getTransmitters().iterator().next().getReceiver();
			sequenciador.getTransmitter().setReceiver(receptor);

			botoes.getBotaoMostradorArquivo().setText("Arquivo: \"" + arqmidi.getName() + "\"");

			long duracao = sequencia.getMicrosecondLength() / 1000000;
			botoes.getBotaoMostradorDuracao().setText("\nDura\u00e7\u00e3o:" + formataInstante(duracao));
			botoes.getBotaoMostradorInstante().setText(formataInstante(0));

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

			botoes.getBotaoAbrir().setEnabled(false);
			botoes.getBotaoTocar().setEnabled(false);
			botoes.getBotaoPausar().setEnabled(true);
			botoes.getBotaoParar().setEnabled(true);

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
	
	void retardo(int miliseg) {
		try {
			Thread.sleep(miliseg);
		} catch (InterruptedException e) {
		}
	}
        
	public void pausar() {
		soando = false;
		sequenciador.stop();

		botoes.getBotaoAbrir().setEnabled(false);
		botoes.getBotaoTocar().setEnabled(true);
		botoes.getBotaoPausar().setEnabled(false);
		botoes.getBotaoParar().setEnabled(false);
	}

	public void parar() {
		soando = false;
		sequenciador.stop();
		sequenciador.close();
		sequenciador = null;
		inicio = 0L;

		botoes.getBotaoAbrir().setEnabled(true);
		botoes.getBotaoTocar().setEnabled(true);
		botoes.getBotaoPausar().setEnabled(false);
		botoes.getBotaoParar().setEnabled(false);

		sliderProgressoInstante.setValue(0);
		botoes.getBotaoMostradorInstante().setText(formataInstante(0));
	}

	@Override
	public void run() {
		double dur;
		double t;
		int pos = 0;

		while (true) {
			if (soando) {
				dur = sequenciador.getMicrosecondLength() / 1000000;
				t = sequenciador.getMicrosecondPosition() / 1000000;
				pos = (int) ((t * 100) / dur);
				try {
					sliderProgressoInstante.setValue(pos);
					botoes.getBotaoMostradorInstante().setText(formataInstante(t));
					retardo(1000);
					if (t >= dur) {
						sliderProgressoInstante.setValue(0);
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
	
	private void constroiSliders() {		
		sliderProgressoInstante = new JProgressBar();
		sliderProgressoInstante.setPreferredSize(new Dimension(200, 20));
		sliderProgressoInstante.setFocusable(false);
		
		sliderVolume = new JSlider(JSlider.HORIZONTAL, 0, 127, volumeAtual);
		sliderVolume.setPreferredSize(new Dimension(150, 20));
		sliderVolume.setFocusable(false);
	}
	
	private void constroiPaineis() {
		painel = getContentPane();
		JPanel painelOperacoes = new JPanel();
		
		JPanel p1 = new JPanel();
		JPanel p2 = new JPanel();
		JPanel p3 = new JPanel();
		JPanel p4 = new JPanel();
		JPanel p5 = new JPanel();
		JPanel p6 = new JPanel();

		painelOperacoes.setLayout(new GridLayout(3, 0));
		painel.setLayout(new GridLayout(7, 0));
		
		p1.add(botoes.getBotaoMostradorCaminho());
		p2.add(botoes.getBotaoAbrir());
		p2.add(botoes.getBotaoTocar());
		p2.add(botoes.getBotaoPausar());
		p2.add(botoes.getBotaoParar());
		p3.add(botoes.getBotaoMostradorArquivo());
		p4.add(botoes.getBotaoMostradorDuracao());
		p5.add(sliderProgressoInstante);
		p5.add(botoes.getBotaoMostradorInstante());
		p6.add(new JLabel("Volume: "));
		p6.add(sliderVolume);
		p6.add(botoes.getBotaoMostradorValorVolume());
		
//		painelOperacoes.add(p2);
//		painelOperacoes.add(p3);
//		painelOperacoes.add(p4);
		
		painel.add(p1);
		painel.add(p2);
		painel.add(p3);
		painel.add(p4);
		painel.add(p5);
		painel.add(p6);
		
//		painel.add(painelOperacoes);
	}
	
	private void adicionaActionListeners() {
		botoes.getBotaoAbrir().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrir();
			}
		});

		botoes.getBotaoTocar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tocar(botoes.getBotaoMostradorCaminho().getText(), inicio);
			}
		});

		botoes.getBotaoPausar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inicio = sequenciador.getMicrosecondPosition();
				pausar();
			}
		});

		botoes.getBotaoParar().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parar();
			}
		});
	}

	private void adicionaChangeListener() {
		sliderVolume.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int valor = (int) source.getValue();

					ShortMessage mensagemDeVolume = new ShortMessage();
					for (int i = 0; i < 16; i++) {
						try {
							mensagemDeVolume.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, valor);
							receptor.send(mensagemDeVolume, -1);
						} catch (InvalidMidiDataException e1) {
						}
					}
					volumeAtual = valor;
					botoes.getBotaoMostradorValorVolume().setText("" + (volumeAtual * 100) / 127 + "%");
				}
			}
		});
	}
	
}
