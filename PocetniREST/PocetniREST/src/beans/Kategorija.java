package beans;

import java.util.ArrayList;

public class Kategorija {
	private String naziv;
	private String opis;
	private boolean izbrisan;
	private ArrayList<String> listaOglasa = new ArrayList<>();

	public Kategorija() {
	}

	public Kategorija(String naziv, String opis) {
		this.naziv = naziv;
		this.opis = opis;
	}

	public Kategorija(String naziv, String opis, ArrayList<String> listaOglasa) {
		this.naziv = naziv;
		this.opis = opis;
		this.listaOglasa = listaOglasa;
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = 31 * result + ((this.naziv == null) ? 0 : this.naziv.hashCode());
		result = 31 * result + ((this.opis == null) ? 0 : this.opis.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Kategorija other = (Kategorija) obj;
		if (this.naziv == null) {
			if (other.naziv != null)
				return false;
		} else if (!this.naziv.equals(other.naziv)) {
			return false;
		}
		if (this.opis == null) {
			if (other.opis != null)
				return false;
		} else if (!this.opis.equals(other.opis)) {
			return false;
		}
		return true;
	}

	public String getNaziv() {
		return this.naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getOpis() {
		return this.opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public ArrayList<String> getListaOglasa() {
		return this.listaOglasa;
	}

	public void setListaOglasa(ArrayList<String> listaOglasa) {
		this.listaOglasa = listaOglasa;
	}

	public String toString() {
		return "Kategorija [naziv=" + this.naziv + ", opis=" + this.opis + "]";
	}

	public boolean isIzbrisan() {
		return this.izbrisan;
	}

	public void setIzbrisan(boolean izbrisan) {
		this.izbrisan = izbrisan;
	}
}