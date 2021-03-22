package beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Poruka {
	private int id;
	private String nazivOglasa;
	private String ulogaPosiljaoca;
	private String idOglasa;
	private String primalac;
	private String posiljalac;
	private String naslov;
	private String sadrzaj;
	private String datumIvrijemePor;
	private boolean obrisan;

	public Poruka() {
		Date d = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		this.datumIvrijemePor = dateFormat.format(d);
		this.obrisan = false;

	}

	public Poruka(String nazivOglasa, String posiljalac, String naslov, String sadrzaj, String datumIvrijemePor,
			String ulogaPosiljaoca, String idOglasa) {
		this.nazivOglasa = nazivOglasa;
		this.posiljalac = posiljalac;
		this.naslov = naslov;
		this.sadrzaj = sadrzaj;
		this.datumIvrijemePor = datumIvrijemePor;
		this.ulogaPosiljaoca = ulogaPosiljaoca;
		this.idOglasa = idOglasa;
	}

	public String toString() {
		return "Poruka [id=" + this.id + ",nazivOglasa=" + this.nazivOglasa + ", primalac=" + this.primalac
				+ ", posiljalac=" + this.posiljalac + ", naslov=" + this.naslov + ", sadrzaj=" + this.sadrzaj
				+ ", datumIvrijemePor=" + this.datumIvrijemePor + ",  obrisan=" + this.obrisan + "]";
	}

	public String getNazivOglasa() {
		return this.nazivOglasa;
	}

	public String getUlogaPosiljaoca() {
		return ulogaPosiljaoca;
	}

	public void setUlogaPosiljaoca(String ulogaPosiljaoca) {
		this.ulogaPosiljaoca = ulogaPosiljaoca;
	}

	public void setNazivOglasa(String nazivOglasa) {
		this.nazivOglasa = nazivOglasa;
	}

	public String getPosiljalac() {
		return this.posiljalac;
	}

	public void setPosiljalac(String posiljalac) {
		this.posiljalac = posiljalac;
	}

	public String getNaslov() {
		return this.naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	public String getSadrzaj() {
		return this.sadrzaj;
	}

	public void setSadrzaj(String sadrzaj) {
		this.sadrzaj = sadrzaj;
	}

	public String getIdOglasa() {
		return idOglasa;
	}

	public void setIdOglasa(String idOglasa) {
		this.idOglasa = idOglasa;
	}

	public String getDatumIvrijemePor() {
		return this.datumIvrijemePor;
	}

	public void setDatumIvrijemePor(String datumIvrijemePor) {
		this.datumIvrijemePor = datumIvrijemePor;
	}

	public String getPrimalac() {
		return this.primalac;
	}

	public void setPrimalac(String primalac) {
		this.primalac = primalac;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isObrisan() {
		return this.obrisan;
	}

	public void setObrisan(boolean obrisan) {
		this.obrisan = obrisan;
	}

}
