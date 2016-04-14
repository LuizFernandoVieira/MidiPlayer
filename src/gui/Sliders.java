package gui;

import java.awt.Dimension;

import javax.swing.JProgressBar;
import javax.swing.JSlider;

public class Sliders {

	private JSlider sliderVolume;
	private JProgressBar sliderProgressoInstante;
	
	private int volumeAtual = 75;
	
	public Sliders() {
		constroiSliders();
	}
	
	private void constroiSliders() {	
		constroiSliderProgressoInstante();
		constroiSliderVolume();
	}
	
	private void constroiSliderProgressoInstante() {
		sliderProgressoInstante = new JProgressBar();
		sliderProgressoInstante.setPreferredSize(new Dimension(200, 20));
		sliderProgressoInstante.setFocusable(false);	
	}
	
	private void constroiSliderVolume() {
		sliderVolume = new JSlider(JSlider.HORIZONTAL, 0, 127, volumeAtual);
		sliderVolume.setPreferredSize(new Dimension(150, 20));
		sliderVolume.setFocusable(false);
	}

	public JSlider getSliderVolume() {
		return sliderVolume;
	}

	public void setSliderVolume(JSlider sliderVolume) {
		this.sliderVolume = sliderVolume;
	}

	public JProgressBar getSliderProgressoInstante() {
		return sliderProgressoInstante;
	}

	public void setSliderProgressoInstante(JProgressBar sliderProgressoInstante) {
		this.sliderProgressoInstante = sliderProgressoInstante;
	}

	public int getVolumeAtual() {
		return volumeAtual;
	}

	public void setVolumeAtual(int volumeAtual) {
		this.volumeAtual = volumeAtual;
	}
	
}
