package services;

import beans.Kategorija;
import beans.Oglas;
import beans.Poruka;
import beans.User;
import dao.KategorijaDAO;
import dao.OglasDAO;
import dao.PorukaDAO;
import dao.RecenzijaDAO;
import dao.UserDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/oglas")
@Produces({ "application/json" })
public class OglasService {
	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		if (this.ctx.getAttribute("oglasDAO") == null) {
			String contextPath = this.ctx.getRealPath("");
			this.ctx.setAttribute("oglasDAO", new OglasDAO(contextPath));
		}
		if (this.ctx.getAttribute("userDAO") == null) {
			String contextPath = this.ctx.getRealPath("");
			this.ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
		if (this.ctx.getAttribute("recenzijaDAO") == null) {
			String contextPath = this.ctx.getRealPath("");
			this.ctx.setAttribute("recenzijaDAO", new RecenzijaDAO(contextPath));
		}
		if (this.ctx.getAttribute("porukaDAO") == null) {
			String contextPath = this.ctx.getRealPath("");
			this.ctx.setAttribute("porukaDAO", new PorukaDAO(contextPath));
		}
	}

	@GET
	@Path("/getOglase")
	@Produces({ "application/json" })
	public Collection<Oglas> oglasi(@Context HttpServletRequest request) {
		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		ArrayList<Oglas> listaOgl = new ArrayList<>();
		try {
			for (Oglas oglas : o.getOglasi().values()) {
				if (!oglas.isDodatUkorpu()) {
					if (!oglas.isIzbrisan())
						listaOgl.add(oglas);
				}
			}
		} catch (Exception exception) {
		}
		return listaOgl;
	}

	@GET
	@Path("/getTop")
	@Produces({ "application/json" })
	public Collection<Oglas> getTop(@Context HttpServletRequest request) {
		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		ArrayList<Oglas> listaOgl = new ArrayList<Oglas>();
		ArrayList<Oglas> ret = new ArrayList<Oglas>();
		for (Oglas oglas : o.getOglasi().values()) {
			if (!oglas.isDodatUkorpu() && !oglas.isIzbrisan() && !oglas.isDostavljen() && !oglas.isuRealizaciji()) {
				listaOgl.add(oglas);
			}
		}
		Collections.sort(listaOgl);
		int i = 0;
		for (Oglas ogl : listaOgl) {
			if (i == 10) {
				break;
			}
			ret.add(ogl);
			i++;
		}

		return ret;
	}

	@GET
	@Path("/getSveOglase/admin")
	@Produces({ "application/json" })
	public Collection<Oglas> getOglasiAdmin(@Context HttpServletRequest request) {
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User u = (User) request.getSession().getAttribute("ulogovan");
		if (u == null) {
			return null;
		}
		User user = (User) userDAO.getUsers().get(u.getUsername());
		if (user == null) {
			return null;
		}
		if (!u.getUloga().equals("administrator")) {
			return null;
		}

		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		ArrayList<Oglas> listaOgl = new ArrayList<>();
		try {
			for (Oglas oglas : o.getOglasi().values()) {
				if (!oglas.isIzbrisan()) {
					listaOgl.add(oglas);
				}
			}
		} catch (Exception exception) {
		}
		return listaOgl;
	}

	@GET
	@Path("/getOglaseMoje/{userUsername}")
	@Produces({ "application/json" })
	public Collection<Oglas> oglasi(@PathParam("userUsername") String userUsername,
			@Context HttpServletRequest request) {
		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		ArrayList<Oglas> listaOgl = new ArrayList<>();
		try {
			for (Oglas oglas : o.getOglasi().values()) {
				if (!oglas.isIzbrisan() && oglas.getProdavac().equals(userUsername)) {
					listaOgl.add(oglas);
				}
			}
		} catch (Exception exception) {
		}
		return listaOgl;
	}

	@GET
	@Path("/preuzmiOglaseKateg/{nazivKategorija}")
	@Produces({ "application/json" })
	public Collection<Oglas> oglasiKateg(@PathParam("nazivKategorija") String nazivKategorija,
			@Context HttpServletRequest request) {
		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		User u = (User) request.getSession().getAttribute("ulogovan");

		ArrayList<Oglas> listaOglKategorija = new ArrayList<>();

		for (Oglas oglas : o.getOglasi().values()) {
			if (oglas.getKategKojojPripada().equals(nazivKategorija) && !oglas.isIzbrisan() && !oglas.isDodatUkorpu()
					&& !oglas.isDostavljen()) {
				if (u == null) {
					listaOglKategorija.add(oglas);
				} else if (u.getUloga().equals("kupac") || u.getUloga().equals("administrator")) {
					listaOglKategorija.add(oglas);
				} else if (u.getUloga().equals("prodavac")) {
					if (oglas.getProdavac().equals(u.getUsername())) {
						listaOglKategorija.add(oglas);
					}
				}
			}
		}

		return listaOglKategorija;
	}

	@POST
	@Path("/dodajOglas/{usernameUser}")
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Oglas dodavanjeNovogOglasa(@PathParam("usernameUser") String usernameUser, Oglas oglas,
			@Context HttpServletRequest request) {
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User user = (User) userDAO.getUsers().get(usernameUser);
		OglasDAO ogDAO = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		String slika = oglas.getSlika();
		while (slika.contains("\\")) {
			String[] parts = slika.split("\\\\");
			slika.trim();
			slika = parts[parts.length - 1];
		}
		oglas.setId(String.valueOf(ogDAO.getOglasi().size() + 1));
		oglas.setLajkovi(0);
		oglas.setDislajkovi(0);
		oglas.setIzbrisan(false);
		oglas.setDodatUkorpu(false);
		oglas.setDostavljen(false);
		oglas.setuRealizaciji(false);
		oglas.setSlika(slika);
		Oglas noviOglas = new Oglas(oglas.getId(), oglas.getNaziv(), oglas.getCijena(), oglas.getOpis(),
				oglas.getLajkovi(), oglas.getDislajkovi(), oglas.getSlika(), oglas.getDatumPostavljanja(),
				oglas.getDatumIsticanja(), oglas.isuRealizaciji(), oglas.isDostavljen(), oglas.getGrad(),
				oglas.getKategKojojPripada(), oglas.isIzbrisan(), oglas.isDodatUkorpu());
		noviOglas.setProdavac(user.getUsername());
		noviOglas.setIzbrisan(false);
		KategorijaDAO katDAO = (KategorijaDAO) this.ctx.getAttribute("kategorijaDAO");
		katDAO.getKategorije().get(noviOglas.getKategKojojPripada()).getListaOglasa().add(noviOglas.getId());
		ogDAO.dodajOglas(noviOglas);
		katDAO.saveKategorije();
		return noviOglas;
	}

	@GET
	@Path("/detaljiOglas/{idOglas}")
	@Produces({ "application/json" })
	public Oglas detaljiOglas(@PathParam("idOglas") String idOglas, @Context HttpServletRequest request) {
		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		Oglas detaljanOglas = o.getOglasi().get(idOglas);

		return detaljanOglas;
	}

	@DELETE
	@Path("/brisanjeOg/{idOglas}")
	@Produces({ "application/json" })
	public Collection<Oglas> brisanjeOg(@PathParam("idOglas") String idOglas, @Context HttpServletRequest request) {
		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		Oglas oglas = o.getOglasi().get(idOglas);

		if (oglas == null) {
			return null;
		}

		if (!oglas.isDodatUkorpu() && !oglas.isuRealizaciji()) {
			oglas.setIzbrisan(true);
		}

		o.saveOglase();

		Collection<Oglas> retOglas = new ArrayList<Oglas>();
		for (Oglas ogs : o.getOglasi().values()) {
			if (!ogs.isIzbrisan()) {
				retOglas.add(ogs);
			}
		}

		return retOglas;
	}

	@GET
	@Path("/ucitajOgIzmjena/{idOglas}")
	@Produces({ "application/json" })
	public Oglas ucitajOgIzmjena(@PathParam("idOglas") String idOglas, @Context HttpServletRequest request) {
		OglasDAO oglasi = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		Oglas ret = (Oglas) oglasi.getOglasi().get(idOglas);
		return ret;
	}

	@PUT
	@Path("/izmjeniOglas/{idOglas}")
	@Produces({ "application/json" })
	public Oglas izmjeniOglas(@PathParam("idOglas") String idOglas, Oglas oglas, @Context HttpServletRequest request) {
		OglasDAO oglasi = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		Oglas ret = (Oglas) oglasi.getOglasi().get(idOglas);
		ret.setNaziv(oglas.getNaziv());
		ret.setCijena(oglas.getCijena());
		String date[] = oglas.getDatumIsticanja().split("-");
		ret.setDatumIsticanja(date[2] + '-' + date[1] + '-' + date[0]);
		ret.setGrad(oglas.getGrad());
		ret.setOpis(oglas.getOpis());
		ret.setSlika(oglas.getSlika());
		if (!ret.getKategKojojPripada().equals(oglas.getKategKojojPripada())) {
			KategorijaDAO katDAO = (KategorijaDAO) this.ctx.getAttribute("kategorijaDAO");

			Kategorija staraKat = katDAO.getKategorije().get(ret.getKategKojojPripada());
			Kategorija novaKat = katDAO.getKategorije().get(oglas.getKategKojojPripada());

			ret.setKategKojojPripada(oglas.getKategKojojPripada());
			staraKat.getListaOglasa().remove(idOglas);
			novaKat.getListaOglasa().add(idOglas);

			katDAO.saveKategorije();
		}
		PorukaDAO porDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		String datum = (new SimpleDateFormat("dd-MM-yyyy hh:mm:ss")).format(new Date());
		Poruka poruka = new Poruka(oglas.getNaziv(), "admin", "Automatizovana poruka", "Izmjenjena je vas oglas", datum,
				"administrator", ret.getId());
		poruka.setPrimalac(ret.getProdavac());
		poruka.setId(porDAO.getPoruke().size() + 1);
		porDAO.getPoruke().put(poruka.getId(), poruka);

		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");

		if (ret.isDodatUkorpu()) {
			for (User usr : userDAO.getUsers().values()) {
				boolean flag = false;
				if (usr.getUloga().equals("kupac")) {
					for (String str : usr.getListaPorucenihOglasa()) {
						if (str.equals(ret.getId())) {
							flag = true;
							datum = (new SimpleDateFormat("dd-MM-yyyy hh:mm:ss")).format(new Date());
							poruka = new Poruka(oglas.getNaziv(), "admin", "Automatizovana poruka",
									"Izmjenjena je oglas koji ste porucili", datum, "administrator", ret.getId());
							poruka.setPrimalac(usr.getUsername());
							poruka.setId(porDAO.getPoruke().size() + 1);
							porDAO.getPoruke().put(poruka.getId(), poruka);
							break;
						}
					}

				}
				if (flag) {
					break;
				}
			}
		}

		if (ret.isDostavljen()) {
			for (User usr : userDAO.getUsers().values()) {
				boolean flag = false;
				if (usr.getUloga().equals("kupac")) {
					for (Oglas ogl : usr.getListaDostavljenih()) {
						if (ogl.getId().equals(ret.getId())) {
							flag = true;
							datum = (new SimpleDateFormat("dd-MM-yyyy hh:mm:ss")).format(new Date());
							poruka = new Poruka(oglas.getNaziv(), "admin", "Automatizovana poruka",
									"Izmjenjena je oglas koji vam je dostavljen", datum, "administrator", ret.getId());
							poruka.setPrimalac(usr.getUsername());
							poruka.setId(porDAO.getPoruke().size() + 1);
							porDAO.getPoruke().put(poruka.getId(), poruka);
							break;
						}
					}

				}
				if (flag) {
					break;
				}
			}
		}

		porDAO.savePor();
		oglasi.saveOglase();
		return ret;
	}

	@GET
	@Path("/getKateg")
	@Produces({ "application/json" })
	public Collection<Kategorija> kategorija(@Context HttpServletRequest request) {
		KategorijaDAO k = (KategorijaDAO) this.ctx.getAttribute("kategorijaDAO");
		ArrayList<Kategorija> listaKat = new ArrayList<>();
		try {
			for (Kategorija kategorija : k.getKategorije().values()) {
				if (!kategorija.isIzbrisan()) {
					listaKat.add(kategorija);
				}
			}
		} catch (Exception exception) {
		}
		return listaKat;
	}

	@GET
	@Path("/dodajUkorpu/{nazivOglasaUser}")
	@Produces({ "application/json" })
	public Collection<Oglas> dodajUkorpu(@PathParam("nazivOglasaUser") String nazivOglasaUser,
			@Context HttpServletRequest request) {
		String[] dio = nazivOglasaUser.split(",");
		String nazivOglasa = dio[0];
		String userUsername = dio[1];

		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		UserDAO u = (UserDAO) this.ctx.getAttribute("userDAO");
		User user = (User) u.getUsers().get(userUsername);
		ArrayList<Oglas> listaOgl = new ArrayList<>();
		ArrayList<Oglas> ret = new ArrayList<Oglas>();

		for (Oglas oglas : o.getOglasi().values()) {
			if (oglas.isDodatUkorpu() || oglas.isIzbrisan() || oglas.isDostavljen())
				continue;
			if (oglas.getNaziv().equals(nazivOglasa)) {
				oglas.setDodatUkorpu(true);
				oglas.setuRealizaciji(true);
				o.saveOglase();
				user.getListaPorucenihOglasa().add(oglas.getId());
				u.saveUsers();
				continue;
			}
			listaOgl.add(oglas);
		}

		Collections.sort(listaOgl);
		int i = 0;
		for (Oglas ogl : listaOgl) {
			if (i == 10) {
				break;
			}
			ret.add(ogl);
			i++;
		}

		return ret;
	}

	@GET
	@Path("/prikaziKorpu/{userUsername}")
	@Produces({ "application/json" })
	public Collection<Oglas> prikaziKorpu(@PathParam("userUsername") String userUsername,
			@Context HttpServletRequest request) {
		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User user = (User) userDAO.getUsers().get(userUsername);
		Collection<Oglas> listaSvihOglasa = new ArrayList<Oglas>();

		for (String og : user.getListaPorucenihOglasa()) {
			Oglas oglas = o.getOglasi().get(og);
			listaSvihOglasa.add(oglas);
		}

		return listaSvihOglasa;
	}

	@GET
	@Path("/kupacDostavljeni/{userUsername}")
	@Produces({ "application/json" })
	public Collection<Oglas> prikaziDostavljene(@PathParam("userUsername") String userUsername,
			@Context HttpServletRequest request) {
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User user = (User) userDAO.getUsers().get(userUsername);

		return user.getListaDostavljenih();
	}

	@GET
	@Path("/dodajUomilj/{nazivOglasaUser}")
	@Produces({ "application/json" })
	public Collection<Oglas> dodajUomilj(@PathParam("nazivOglasaUser") String nazivOglasaUser,
			@Context HttpServletRequest request) {
		String[] dio = nazivOglasaUser.split(",");
		String nazivOglasa = dio[0];
		String userUsername = dio[1];
		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		UserDAO u = (UserDAO) this.ctx.getAttribute("userDAO");
		User user = (User) u.getUsers().get(userUsername);
		ArrayList<Oglas> listaOgl = new ArrayList<>();
		ArrayList<Oglas> ret = new ArrayList<Oglas>();

		for (Oglas oglas : o.getOglasi().values()) {
			if (oglas.isDodatUkorpu() || oglas.isDostavljen() || oglas.isIzbrisan())
				continue;
			if (oglas.getNaziv().equals(nazivOglasa)) {
				int g = oglas.getOmiljen();
				g++;
				oglas.setOmiljen(g);
				o.saveOglase();
				user.getListaOmiljenihOglasa().add(oglas.getId());
				u.saveUsers();
			}
			listaOgl.add(oglas);
		}
		Collections.sort(listaOgl);
		int i = 0;
		for (Oglas ogl : listaOgl) {
			if (i == 10) {
				break;
			}
			ret.add(ogl);
			i++;
		}

		return ret;
	}

	@GET
	@Path("/prikaziOmiljene/{userUsername}")
	@Produces({ "application/json" })
	public Collection<Oglas> prikaziOmiljene(@PathParam("userUsername") String userUsername,
			@Context HttpServletRequest request) {
		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User user = (User) userDAO.getUsers().get(userUsername);
		Collection<Oglas> listaSvihOglasa = new ArrayList<Oglas>();

		for (String og : user.getListaOmiljenihOglasa()) {
			listaSvihOglasa.add(o.getOglasi().get(og));
		}
		return listaSvihOglasa;
	}

	@GET
	@Path("/dostavljeni/{idOglasa}/{usernameUser}")
	@Produces({ "application/json" })
	public Response dostavljeni(@PathParam("idOglasa") String idOglasa,
			@PathParam("usernameUser") String usernameUser) {

		OglasDAO oglasDAO = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		HashMap<String, Oglas> o = oglasDAO.getOglasi();
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User u = (User) userDAO.getUsers().get(usernameUser);
		Oglas foundOg = o.get(idOglasa);
		foundOg.setDostavljen(true);
		Oglas noviOg = new Oglas(foundOg.getId(), foundOg.getNaziv(), foundOg.getCijena(), foundOg.getOpis(),
				foundOg.getLajkovi(), foundOg.getDislajkovi(), foundOg.getSlika(), foundOg.getDatumPostavljanja(),
				foundOg.getDatumIsticanja(), foundOg.getGrad());
		u.getListaDostavljenih().add(noviOg);
		noviOg.setDostavljen(true);
		noviOg.setProdavac(foundOg.getProdavac());
		noviOg.setDodatUkorpu(foundOg.isDodatUkorpu());
		int i = 0;
		for (String idOg : u.getListaPorucenihOglasa()) {
			if (idOg.equals(foundOg.getId())) {
				break;
			}
			i++;
		}
		u.getListaPorucenihOglasa().remove(i);

		PorukaDAO porDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		String date = (new SimpleDateFormat("dd-MM-yyyy hh:mm:ss")).format(new Date());
		Poruka poruka = new Poruka(foundOg.getNaziv(), "admin", "Automatizovana poruka",
				"Vas oglas je dostavljen kupcu", date, "administrator", foundOg.getId());
		poruka.setPrimalac(foundOg.getProdavac());
		poruka.setId(porDAO.getPoruke().size() + 1);
		porDAO.getPoruke().put(poruka.getId(), poruka);

		porDAO.savePor();
		userDAO.saveUsers();
		oglasDAO.saveOglase();
		return Response.status(200).build();
	}

	@POST
	@Path("/lajkuj/{idOglas}/{usernameUser}")
	@Produces({ "application/json" })
	public Response lajkujOglas(@PathParam("idOglas") String idOglas, @PathParam("usernameUser") String usernameUser,
			@Context HttpServletRequest request) {

		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User user = (User) userDAO.getUsers().get(usernameUser);
		Oglas oglas = (Oglas) o.getOglasi().get(idOglas);
		for (String s : user.getListaOcjenjenihOglasa()) {
			if (s.equals(idOglas)) {
				return Response.status(400).build();
			}
		}

		int lajk = oglas.getLajkovi();
		lajk++;
		oglas.setLajkovi(lajk);
		user.getListaOcjenjenihOglasa().add(oglas.getId());
		o.saveOglase();
		return Response.status(200).build();
	}

	@POST
	@Path("/dislajkuj/{idOglas}/{usernameUser}")
	@Produces({ "application/json" })
	public Response dislajkujOglas(@PathParam("idOglas") String idOglas, @PathParam("usernameUser") String usernameUser,
			@Context HttpServletRequest request) {

		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User user = (User) userDAO.getUsers().get(usernameUser);
		Oglas oglas = (Oglas) o.getOglasi().get(idOglas);
		for (String s : user.getListaOcjenjenihOglasa()) {
			if (s.equals(idOglas)) {
				return Response.status(400).build();
			}
		}
		int dislajk = oglas.getDislajkovi();
		dislajk++;
		oglas.setDislajkovi(dislajk);
		user.getListaOcjenjenihOglasa().add(oglas.getId());
		o.saveOglase();
		return Response.status(200).build();
	}

	@GET
	@Path("/filtriranjeStatus/{status}/{usernameUser}")
	@Consumes({ "application/json" })
	public Collection<Oglas> filtriranjeStatus(@PathParam("status") String status,
			@PathParam("usernameUser") String usernameUser) {
		OglasDAO oglasDAO = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User userProdavac = (User) userDAO.getUsers().get(usernameUser);
		ArrayList<Oglas> listaOgl = new ArrayList<>();
		String d = "dostavljeni";
		if (status.equals(d)) {
			for (Oglas o : oglasDAO.getOglasi().values()) {
				if (o.getProdavac().equals(usernameUser) && o.isDostavljen()) {
					listaOgl.add(o);
				}
			}
			return listaOgl;
		}
		if (status.equals("realizacija")) {
			for (Oglas o : oglasDAO.getOglasi().values()) {
				if (o.getProdavac().equals(usernameUser) && o.isDodatUkorpu() && !o.isDostavljen()) {
					listaOgl.add(o);
				}
			}
			return listaOgl;
		}
		if (status.equals("aktivni")) {
			for (Oglas o : oglasDAO.getOglasi().values()) {
				if (o.getProdavac().equals(usernameUser) && !o.isDodatUkorpu() && !o.isDostavljen()
						&& !o.isIzbrisan()) {
					listaOgl.add(o);
				}
			}
			return listaOgl;
		}
		for (Oglas o : oglasDAO.getOglasi().values()) {
			if (o.getProdavac().equals(usernameUser) && !o.isIzbrisan()) {
				listaOgl.add(o);
			}
		}
		return listaOgl;
	}

	@GET
	@Path("/najpopularniji")
	@Produces({ "application/json" })
	public Collection<Oglas> najpopularniji() {
		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		ArrayList<Oglas> listaOglasa = new ArrayList<>();
		for (Oglas oglas : o.getOglasi().values())
			listaOglasa.add(oglas);
		listaOglasa.sort((i, k) -> Integer.compare(k.getOmiljen(), i.getOmiljen()));
		System.out.println("/*/*/-*/*-/-*/-*/-/-*/-*/-");
		for (Oglas og : listaOglasa)
			System.out.println(String.valueOf(og.getNaziv()) + " " + og.getOmiljen());
		System.out.println("*//////////////******************/////////s");
		if (listaOglasa.size() >= 10)
			return listaOglasa.subList(0, 10);
		return listaOglasa;
	}
}
