package model;

import java.time.LocalDate;

public class Movimento {
	
	private LocalDate data;
	private String tipo;
	private double denaroOperazione;
	private double saldoFinale;
	
	public Movimento(LocalDate data, String tipo, double denaroOperazione, double saldoFinale) {
		this.data = data;
		this.tipo = tipo;
		this.denaroOperazione = denaroOperazione;
		this.saldoFinale = saldoFinale;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public double getDenaro() {
		return denaroOperazione;
	}

	public void setDenaro(double denaroOperazione) {
		this.denaroOperazione = denaroOperazione;
	}
	
	public double getSaldoFinale() {
		return saldoFinale;
	}

	public void setSaldoFinale(double saldoFinale) {
		this.saldoFinale = saldoFinale;
	}
	

}
