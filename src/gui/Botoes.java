package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.JButton;

import constantes.Constantes;

public class Botoes {
	
	private JButton botaoAbrir;
	private JButton botaoTocar;
	private JButton botaoPausar;
	private JButton botaoParar;

	private JButton botaoMostradorCaminho;
	private JButton botaoMostradorArquivo;
	private JButton botaoMostradorDuracao;
	private JButton botaoMostradorInstante;
	private JButton botaoMostradorValorVolume;
	
	private Color corOPR = new Color(180, 220, 220);
	private Color corARQ = new Color(230, 230, 228);
	
	private int volumeAtual = 75;
	
	public Botoes() {
		constroiBotoes();
	}

	private void constroiBotoes() {
		constroiBotaoAbrir();
		constroiBotaoTocar();
		constroiBotaoPausar();
		constroiBotaoParar();
		constroiBotaoMostradorCaminho();
		constroiBotaoMostradorArquivo();
		constroiBotaoMostradorDuracao();
		constroiBotaoMostradorInstante();
		constroiBotaoValorVolume();
	}

	private void constroiBotaoAbrir() {
		botaoAbrir  = constroiBotao("Abrir", 18);
		botaoAbrir.setBackground(corOPR);
		botaoAbrir.setEnabled(true);
	}
	
	private void constroiBotaoTocar() {
		botaoTocar  = constroiBotao("\u25b6", 18);
		botaoTocar.setBackground(corOPR);
		botaoTocar.setEnabled(false);
	}
	
	private void constroiBotaoPausar() {
		botaoPausar = constroiBotao("\u25ae\u25ae", 18);
		botaoPausar.setBackground(corOPR);
		botaoPausar.setEnabled(false);
	}
	
	private void constroiBotaoParar() {		
		botaoParar  = constroiBotao("\u25fc", 18);
		botaoParar.setBackground(corOPR);
		botaoParar.setEnabled(false);
	}
	
	private void constroiBotaoMostradorCaminho() {
		botaoMostradorCaminho    = constroiBotao(" DIR: " + Constantes.DIRETORIO, 18);
		botaoMostradorCaminho.setBackground(corARQ);
	}
	
	private void constroiBotaoMostradorArquivo() {
		botaoMostradorArquivo     = constroiBotao(" Arquivo: ", 18);
		botaoMostradorArquivo.setBackground(corARQ);
	}
	
	private void constroiBotaoMostradorDuracao() {
		botaoMostradorDuracao     = constroiBotao(" Dura\u00e7\u00e3o: ", 18);
		botaoMostradorDuracao.setBackground(corARQ);
	}
	
	private void constroiBotaoMostradorInstante() {
		botaoMostradorInstante    = constroiBotao(" ", 18);
		botaoMostradorInstante.setBackground(corARQ);
	}
	
	private void constroiBotaoValorVolume() {
		botaoMostradorValorVolume = constroiBotao(" ", 18);
		botaoMostradorValorVolume.setBackground(corARQ);
		botaoMostradorValorVolume.setText("" + (volumeAtual * 100) / 127 + "%");	
	}
	
	@SuppressWarnings("unused")
	private JButton constroiBotao(String legenda) {
		JButton botao = new JButton(legenda);
		botao.setMargin(new Insets(2, 2, 2, 2));
		botao.setFocusable(false);
		botao.setFont(botao.getFont().deriveFont(Font.PLAIN));
		return botao;
	}

	private JButton constroiBotao(String legenda, float tamanhoFonte) {
		JButton botao = new JButton(legenda);
		botao.setMargin(new Insets(2, 2, 2, 2));
		botao.setFocusable(false);
		botao.setFont(botao.getFont().deriveFont(Font.PLAIN));
		botao.setFont(botao.getFont().deriveFont(tamanhoFonte));
		return botao;
	}
	
	public JButton getBotaoAbrir() {
		return botaoAbrir;
	}

	public void setBotaoAbrir(JButton botaoAbrir) {
		this.botaoAbrir = botaoAbrir;
	}

	public JButton getBotaoTocar() {
		return botaoTocar;
	}

	public void setBotaoTocar(JButton botaoTocar) {
		this.botaoTocar = botaoTocar;
	}

	public JButton getBotaoPausar() {
		return botaoPausar;
	}

	public void setBotaoPausar(JButton botaoPausar) {
		this.botaoPausar = botaoPausar;
	}

	public JButton getBotaoParar() {
		return botaoParar;
	}

	public void setBotaoParar(JButton botaoParar) {
		this.botaoParar = botaoParar;
	}

	public JButton getBotaoMostradorCaminho() {
		return botaoMostradorCaminho;
	}

	public void setBotaoMostradorCaminho(JButton botaoMostradorCaminho) {
		this.botaoMostradorCaminho = botaoMostradorCaminho;
	}

	public JButton getBotaoMostradorArquivo() {
		return botaoMostradorArquivo;
	}

	public void setBotaoMostradorArquivo(JButton botaoMostradorArquivo) {
		this.botaoMostradorArquivo = botaoMostradorArquivo;
	}

	public JButton getBotaoMostradorDuracao() {
		return botaoMostradorDuracao;
	}

	public void setBotaoMostradorDuracao(JButton botaoMostradorDuracao) {
		this.botaoMostradorDuracao = botaoMostradorDuracao;
	}

	public JButton getBotaoMostradorInstante() {
		return botaoMostradorInstante;
	}

	public void setBotaoMostradorInstante(JButton botaoMostradorInstante) {
		this.botaoMostradorInstante = botaoMostradorInstante;
	}

	public JButton getBotaoMostradorValorVolume() {
		return botaoMostradorValorVolume;
	}

	public void setBotaoMostradorValorVolume(JButton botaoMostradorValorVolume) {
		this.botaoMostradorValorVolume = botaoMostradorValorVolume;
	}
	
}
