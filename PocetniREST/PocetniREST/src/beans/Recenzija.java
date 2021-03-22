package beans;

public class Recenzija {

	private String id;
	private String idOglas;
	private String oglas;
	private String recenzent;
	private String naslovRecenzije;
	private String sadrzajRecenzijel;
	private String slika;
	private boolean oglasTacan;
	private boolean ispostovanDogovor;
	private boolean izbrisan;

	public Recenzija() {
	}

	public Recenzija(String idOglas, String oglas, String recenzent, String naslovRecenzije, String sadrzajRecenzijel,
			boolean oglasTacan, boolean ispostovanDogovor) {
		this.idOglas = idOglas;
		this.oglas = oglas;
		this.recenzent = recenzent;
		this.naslovRecenzije = naslovRecenzije;
		this.sadrzajRecenzijel = sadrzajRecenzijel;
		this.oglasTacan = oglasTacan;
		this.ispostovanDogovor = ispostovanDogovor;
	}

	public String toString() {
		return "Recenzija [oglas=" + this.oglas + ", recenzent=" + this.recenzent + ", naslovRecenzije="
				+ this.naslovRecenzije + ", sadrzajRecenzijel=" + this.sadrzajRecenzijel + ", slika=" + this.slika
				+ ", oglasTacan=" + this.oglasTacan + ", ispostovanDogovor=" + this.ispostovanDogovor + "]";
	}

	public String getIdOglas() {
		return idOglas;
	}

	public void setIdOglas(String idOglas) {
		this.idOglas = idOglas;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOglas() {
		return this.oglas;
	}

	public void setOglas(String oglas) {
		this.oglas = oglas;
	}

	public String getRecenzent() {
		return this.recenzent;
	}

	public void setRecenzent(String recenzent) {
		this.recenzent = recenzent;
	}

	public String getNaslovRecenzije() {
		return this.naslovRecenzije;
	}

	public void setNaslovRecenzije(String naslovRecenzije) {
		this.naslovRecenzije = naslovRecenzije;
	}

	public String getSadrzajRecenzijel() {
		return this.sadrzajRecenzijel;
	}

	public void setSadrzajRecenzijel(String sadrzajRecenzijel) {
		this.sadrzajRecenzijel = sadrzajRecenzijel;
	}

	public String getSlika() {
		return this.slika;
	}

	public void setSlika(String slika) {
		this.slika = slika;
	}

	public boolean isOglasTacan() {
		return this.oglasTacan;
	}

	public void setOglasTacan(boolean oglasTacan) {
		this.oglasTacan = oglasTacan;
	}

	public boolean isIspostovanDogovor() {
		return this.ispostovanDogovor;
	}

	public void setIspostovanDogovor(boolean ispostovanDogovor) {
		this.ispostovanDogovor = ispostovanDogovor;
	}

	public boolean isIzbrisan() {
		return this.izbrisan;
	}

	public void setIzbrisan(boolean izbrisan) {
		this.izbrisan = izbrisan;
	}
}
