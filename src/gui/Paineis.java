package gui;

import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Paineis {
	
	private Container painel;
	
	public Paineis(JFrame jFrame, Botoes botoes, Sliders sliders) {
		constroiPaineis(jFrame, botoes, sliders);
	}
	
	private void constroiPaineis(JFrame jFrame, Botoes botoes, Sliders sliders) {
		painel = jFrame.getContentPane();
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
		p5.add(sliders.getSliderProgressoInstante());
		p5.add(botoes.getBotaoMostradorInstante());
		p6.add(new JLabel("Volume: "));
		p6.add(sliders.getSliderVolume());
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

}
