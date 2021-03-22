package beans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class User {
	private String username;
	private String password;
	private String ime;
	private String prezime;
	private String uloga;
	private int tel;
	private String grad;
	private String email;
	private String datumRegistracije;
	private int prodavacLajk = 0;
	private int prodavacDislajk = 0;
	private int prijava = 0;

	private ArrayList<String> listaPorucenihOglasa = new ArrayList<>();
	private ArrayList<String> listaOmiljenihOglasa = new ArrayList<>();
	private ArrayList<Integer> listaPoruka = new ArrayList<>();
	private ArrayList<Oglas> listaDostavljenih = new ArrayList<>();
	private ArrayList<String> listaOcjenjenihOglasa = new ArrayList<>();
	private ArrayList<String> listaOcjenjenihProdavaca = new ArrayList<>();
	private ArrayList<String> listaRecenzijaOglas = new ArrayList<>();
	private ArrayList<String> listaRecenzijaProdavac = new ArrayList<>();

	public User() {
	}

	public User(String username, String uloga) {
		this.username = username;
		this.uloga = uloga;
	}

	public User(String username, String password, String uloga, String ime, String prezime, int telefon, String grad,
			String email) {
		this.username = username;
		this.password = password;
		this.ime = ime;
		this.prezime = prezime;
		this.uloga = uloga;
		this.tel = telefon;
		this.grad = grad;
		this.email = email;
		this.datumRegistracije = (new SimpleDateFormat("dd-MM-yyyy")).format(new Date());
	}

	public ArrayList<String> getListaPorucenihOglasa() {
		return listaPorucenihOglasa;
	}

	public void setListaPorucenihOglasa(ArrayList<String> listaPorucenihOglasa) {
		this.listaPorucenihOglasa = listaPorucenihOglasa;
	}

	public ArrayList<String> getListaOmiljenihOglasa() {
		return listaOmiljenihOglasa;
	}

	public void setListaOmiljenihOglasa(ArrayList<String> listaOmiljenihOglasa) {
		this.listaOmiljenihOglasa = listaOmiljenihOglasa;
	}

	public ArrayList<String> getListaOcjenjenihOglasa() {
		return listaOcjenjenihOglasa;
	}

	public void setListaOcjenjenihOglasa(ArrayList<String> listaOcjenjenihOglasa) {
		this.listaOcjenjenihOglasa = listaOcjenjenihOglasa;
	}

	public ArrayList<String> getListaOcjenjenihProdavaca() {
		return listaOcjenjenihProdavaca;
	}

	public void setListaOcjenjenihProdavaca(ArrayList<String> listaOcjenjenihProdavaca) {
		this.listaOcjenjenihProdavaca = listaOcjenjenihProdavaca;
	}

	public String getUsername() {
		return this.username;
	}

	public String toString() {
		return "User [username=" + this.username + ", password=" + this.password + ", prodavacLajk ="
				+ this.prodavacLajk + ", prodavacLajk =" + this.prodavacDislajk + ", ime=" + this.ime + ", prezime="
				+ this.prezime + ", uloga=" + this.uloga + ", tel=" + this.tel + ", grad=" + this.grad + ", email="
				+ this.email + ", datumRegistracije=" + this.datumRegistracije + ", listaPorucenihOglasa="
				+ this.listaPorucenihOglasa + ", listaOmiljenihOglasa=" + this.listaOmiljenihOglasa + ", listaPoruka="
				+ this.listaPoruka + ", listaDostavljenih=" + this.listaDostavljenih + "]";
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIme() {
		return this.ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return this.prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getUloga() {
		return this.uloga;
	}

	public void setUloga(String uloga) {
		this.uloga = uloga;
	}

	public int getTel() {
		return this.tel;
	}

	public int setTel(int tel) {
		return this.tel = tel;
	}

	public String getGrad() {
		return this.grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String mail) {
		this.email = mail;
	}

	public String getDatumRegistracije() {
		return this.datumRegistracije;
	}

	public void setDatumRegistracije(String datumRegistracije) {
		this.datumRegistracije = datumRegistracije;
	}

	public ArrayList<Integer> getListaPoruka() {
		return this.listaPoruka;
	}

	public void setListaPoruka(ArrayList<Integer> listaPoruka) {
		this.listaPoruka = listaPoruka;
	}

	public ArrayList<Oglas> getListaDostavljenih() {
		return this.listaDostavljenih;
	}

	public void setListaDostavljenih(ArrayList<Oglas> listaDostavljenih) {
		this.listaDostavljenih = listaDostavljenih;
	}

	public int getProdavacLajk() {
		return this.prodavacLajk;
	}

	public void setProdavacLajk(int prodavacLajk) {
		this.prodavacLajk = prodavacLajk;
	}

	public int getProdavacDislajk() {
		return this.prodavacDislajk;
	}

	public void setProdavacDislajk(int prodavacDislajk) {
		this.prodavacDislajk = prodavacDislajk;
	}

	public ArrayList<String> getListaRecenzijaOglas() {
		return this.listaRecenzijaOglas;
	}

	public void setListaRecenzijaOglas(ArrayList<String> listaRecenzijaOglas) {
		this.listaRecenzijaOglas = listaRecenzijaOglas;
	}

	public ArrayList<String> getListaRecenzijaProdavac() {
		return this.listaRecenzijaProdavac;
	}

	public void setListaRecenzijaProdavac(ArrayList<String> listaRecenzijaProdavac) {
		this.listaRecenzijaProdavac = listaRecenzijaProdavac;
	}

	public int getPrijava() {
		return prijava;
	}

	public void setPrijava(int prijava) {
		this.prijava = prijava;
	}

}
