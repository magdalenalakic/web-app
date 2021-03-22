package beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Oglas implements Comparable{
	private String id;
	private String naziv;
	private String cijena;
	private String opis;
	private int lajkovi = 0;
	private int dislajkovi = 0;
	private String slika;
	private String datumPostavljanja;
	private String datumIsticanja;
	private boolean dodatUkorpu; // u realizaciji je
	private boolean uRealizaciji;
	private boolean dostavljen;
	private String grad;
	private String kategKojojPripada;
	private boolean izbrisan;
	private String prodavac;
	private int kolikoPutaLajk = 0;
	private int omiljen = 0;

	public Oglas() {
	}

	public Oglas(String id, String naziv, String cijena, String opis, int lajkovi, int dislajkovi, String slika,
			String datumPostavljanja, String datumIsticanja, String grad) {
		this.id = id;
		this.naziv = naziv;
		this.cijena = cijena;
		this.opis = opis;
		this.lajkovi = lajkovi;
		this.dislajkovi = dislajkovi;
		this.slika = slika;
		this.datumPostavljanja = (new SimpleDateFormat("dd-MM-yyyy")).format(new Date());
		this.datumIsticanja = datumIsticanja;
		this.grad = grad;
	}

	public Oglas(String naziv2, String opis2) {
		this.naziv = naziv2;
		this.opis = opis2;
	}

	public Oglas(String id, String naziv, String cijena, String opis, int lajkovi, int dislajkovi, String slika,
			String datumPostavljanja, String datumIsticanja, boolean uRealizaciji, boolean dostavljen, String grad,
			String kategKojojPripada, boolean izbrisan, boolean dodatUkorpu) {
		this.id = id;
		this.naziv = naziv;
		this.cijena = cijena;
		this.opis = opis;
		this.lajkovi = lajkovi;
		this.dislajkovi = dislajkovi;
		this.slika = slika;
		this.datumPostavljanja = (new SimpleDateFormat("dd-MM-yyyy")).format(new Date());
		String date[] = datumIsticanja.split("-");
		this.datumIsticanja = date[2] + "-" + date[1] + "-" + date[0];
		this.uRealizaciji = uRealizaciji;
		this.dostavljen = dostavljen;
		this.grad = grad;
		this.kategKojojPripada = kategKojojPripada;
		this.izbrisan = izbrisan;
	}

	@Override
	public String toString() {
		return "Oglas [id=" + id + ", naziv=" + naziv + ", cijena=" + cijena + ", opis=" + opis + ", lajkovi=" + lajkovi
				+ ", dislajkovi=" + dislajkovi + ", slika=" + slika + ", datumPostavljanja=" + datumPostavljanja
				+ ", datumIsticanja=" + datumIsticanja + ", uRealizaciji=" + uRealizaciji + ", dostavljen=" + dostavljen
				+ ", grad=" + grad + ", kategKojojPripada=" + kategKojojPripada + ", izbrisan=" + izbrisan
				+ ", prodavac=" + prodavac + ", kolikoPutaLajk=" + kolikoPutaLajk + ", omiljen=" + omiljen + "]";
	}

	public boolean isDodatUkorpu() {
		return dodatUkorpu;
	}

	public void setDodatUkorpu(boolean dodatUkorpu) {
		this.dodatUkorpu = dodatUkorpu;
	}

	public String getNaziv() {
		return this.naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public String getCijena() {
		return this.cijena;
	}

	public void setCijena(String cijena) {
		this.cijena = cijena;
	}

	public String getOpis() {
		return this.opis;
	}

	public void setOpis(String opis) {
		this.opis = opis;
	}

	public int getLajkovi() {
		return this.lajkovi;
	}

	public void setLajkovi(int lajkovi) {
		this.lajkovi = lajkovi;
	}

	public int getDislajkovi() {
		return this.dislajkovi;
	}

	public void setDislajkovi(int dislajkovi) {
		this.dislajkovi = dislajkovi;
	}

	public String getSlika() {
		return this.slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public String getDatumPostavljanja() {
		return this.datumPostavljanja;
	}

	public void setDatumPostavljanja(String datumPostavljanja) {
		this.datumPostavljanja = datumPostavljanja;
	}

	public String getDatumIsticanja() {
		return this.datumIsticanja;
	}

	public void setDatumIsticanja(String datumIsticanja) {
		this.datumIsticanja = datumIsticanja;
	}

	public String getGrad() {
		return this.grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getKategKojojPripada() {
		return this.kategKojojPripada;
	}

	public void setKategKojojPripada(String kategKojojPripada) {
		this.kategKojojPripada = kategKojojPripada;
	}

	public boolean isIzbrisan() {
		return this.izbrisan;
	}

	public void setIzbrisan(boolean izbrisan) {
		this.izbrisan = izbrisan;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProdavac() {
		return prodavac;
	}

	public void setProdavac(String prodavac) {
		this.prodavac = prodavac;
	}

	public boolean isuRealizaciji() {
		return this.uRealizaciji;
	}

	public void setuRealizaciji(boolean uRealizaciji) {
		this.uRealizaciji = uRealizaciji;
	}

	public boolean isDostavljen() {
		return this.dostavljen;
	}

	public void setDostavljen(boolean dostavljen) {
		this.dostavljen = dostavljen;
	}

	public int getKolikoPutaLajk() {
		return this.kolikoPutaLajk;
	}

	public void setKolikoPutaLajk(int kolikoPutaLajk) {
		this.kolikoPutaLajk = kolikoPutaLajk;
	}

	public int getOmiljen() {
		return this.omiljen;
	}

	public void setOmiljen(int omiljen) {
		this.omiljen = omiljen;
	}

	@Override
	public int compareTo(Object o) {
		int comp = ((Oglas) o).getOmiljen();
		return comp-this.getOmiljen();
	}
}
