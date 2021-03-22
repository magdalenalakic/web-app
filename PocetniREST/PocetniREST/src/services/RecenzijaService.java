package services;

import beans.Oglas;
import beans.Poruka;
import beans.Recenzija;
import beans.User;
import dao.OglasDAO;
import dao.PorukaDAO;
import dao.RecenzijaDAO;
import dao.UserDAO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/recenzija")
@Produces({ "application/json" })
public class RecenzijaService {
	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		if (this.ctx.getAttribute("recenzijaDAO") == null) {
			String contextPath = this.ctx.getRealPath("");
			this.ctx.setAttribute("recenzijaDAO", new RecenzijaDAO(contextPath));
		}
		if (this.ctx.getAttribute("porukaDAO") == null) {
			String contextPath = this.ctx.getRealPath("");
			this.ctx.setAttribute("porukaDAO", new PorukaDAO(contextPath));
		}
		if (this.ctx.getAttribute("userDAO") == null) {
			String contextPath = this.ctx.getRealPath("");
			this.ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
		if (this.ctx.getAttribute("oglasDAO") == null) {
			String contextPath = this.ctx.getRealPath("");
			this.ctx.setAttribute("oglasDAO", new OglasDAO(contextPath));
		}
	}

	@GET
	@Path("/ucitajOgRecenzija/{ogNaziv}")
	@Produces({ "application/json" })
	public Oglas ucitajOgRecenzija(@PathParam("ogNaziv") String ogNaziv, @Context HttpServletRequest request) {
		OglasDAO oglasi = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		Oglas ret = (Oglas) oglasi.getOglasi().get(ogNaziv);
		return ret;
	}

	@POST
	@Path("/dodajRecenziju/{idOglas}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response dodajRecenziju(@PathParam("idOglas") String idOglas, Recenzija recenzija,
			@Context HttpServletRequest request) {
		UserDAO uDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User userRecenzent = (User) uDAO.getUsers().get(recenzija.getRecenzent());
		for (String s : userRecenzent.getListaRecenzijaOglas()) {
			if (s.equals(idOglas)) {
				System.out.println("Vec ste ostavili recenziju na oglas!");
				return Response.status(400).build();
			}
		}
		RecenzijaDAO recDAO = (RecenzijaDAO) this.ctx.getAttribute("recenzijaDAO");
		recenzija.setId(String.valueOf(recDAO.getRecenzije().size() + 1));
		recenzija.setIzbrisan(false);
		userRecenzent.getListaRecenzijaOglas().add(recenzija.getId());

		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		Oglas oglas = o.getOglasi().get(recenzija.getIdOglas());
		oglas.setDodatUkorpu(false);
		oglas.setDostavljen(false);
		oglas.setuRealizaciji(false);

		PorukaDAO porDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		String date = (new SimpleDateFormat("dd-MM-yyyy hh:mm:ss")).format(new Date());
		Poruka poruka = new Poruka(oglas.getNaziv(), "admin", "Automatizovana poruka",
				"Ostavljena je recenzija na vas oglas", date, "administrator", oglas.getId());
		poruka.setPrimalac(oglas.getProdavac());
		poruka.setId(porDAO.getPoruke().size() + 1);
		porDAO.getPoruke().put(poruka.getId(), poruka);

		recDAO.dodajRecenziju(recenzija);
		uDAO.saveUsers();
		o.saveOglase();
		porDAO.savePor();
		return Response.status(200).build();
	}

	@GET
	@Path("/getRecenzije/{nazivOglasa}")
	@Produces({ "application/json" })
	public Collection<Recenzija> getRecenzije(@PathParam("nazivOglasa") String nazivOglasa,
			@Context HttpServletRequest request) {
		System.out.println(nazivOglasa);
		RecenzijaDAO o = (RecenzijaDAO) this.ctx.getAttribute("recenzijaDAO");
		ArrayList<Recenzija> listaRecenzijaZaOglas = new ArrayList<>();
		try {
			for (Recenzija rec : o.getRecenzije().values()) {
				if (rec.getOglas().equals(nazivOglasa)) {
					if (!rec.isIzbrisan()) {
						listaRecenzijaZaOglas.add(rec);
						continue;
					}
					System.out.println("Obrisanu kategoriju necemo ispisati, to je: " + rec.getNaslovRecenzije()
							+ "--------------------------------");
				}
			}
			System.out.println("+++++++++++ISPIS RECENZIJA  +++++++++++++++++");
			for (Recenzija rec : listaRecenzijaZaOglas)
				System.out.println(rec);
			System.out.println("************************************************************************************");
		} catch (Exception exception) {
		}
		return listaRecenzijaZaOglas;
	}

	@GET
	@Path("/ucitajRecIzmjena/{rid}")
	@Produces({ "application/json" })
	public Recenzija ucitajRecIzmjena(@PathParam("rid") String rid, @Context HttpServletRequest request) {
		RecenzijaDAO recenzije = (RecenzijaDAO) this.ctx.getAttribute("recenzijaDAO");
		Recenzija ret = (Recenzija) recenzije.getRecenzije().get(rid);
		return ret;
	}

	@POST
	@Path("/izmjeniRec/{recId}")
	@Consumes({ "application/json" })
	@Produces({ "application/json" })
	public Recenzija izmjeniRec(@PathParam("recId") String recId, Recenzija recenzija,
			@Context HttpServletRequest request) {

		RecenzijaDAO recenzije = (RecenzijaDAO) this.ctx.getAttribute("recenzijaDAO");
		Recenzija ret = (Recenzija) recenzije.getRecenzije().get(recId);
		ret.setOglas(recenzija.getOglas());
		ret.setRecenzent(recenzija.getRecenzent());
		ret.setNaslovRecenzije(recenzija.getNaslovRecenzije());
		ret.setSadrzajRecenzijel(recenzija.getSadrzajRecenzijel());
		ret.setOglasTacan(recenzija.isOglasTacan());
		ret.setIspostovanDogovor(recenzija.isIspostovanDogovor());

		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");

		Oglas oglas = (Oglas) o.getOglasi().get(ret.getIdOglas());
		PorukaDAO porDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		String date = (new SimpleDateFormat("dd-MM-yyyy hh:mm:ss")).format(new Date());
		Poruka poruka = new Poruka(oglas.getNaziv(), "admin", "Automatizovana poruka",
				"Izmjenjena je recenzija za vas oglas", date, "administrator", oglas.getId());
		poruka.setPrimalac(oglas.getProdavac());
		poruka.setId(porDAO.getPoruke().size() + 1);
		porDAO.getPoruke().put(poruka.getId(), poruka);

		porDAO.savePor();
		recenzije.saveRecenzije();
		return ret;
	}

	@GET
	@Path("/brisanjeRec/{nazivRec}")
	@Produces({ "application/json" })
	public Collection<Recenzija> brisanjeRec(@PathParam("nazivRec") String nazivRec,
			@Context HttpServletRequest request) {
		System.out.println(nazivRec);
		RecenzijaDAO recenzijaDAO = (RecenzijaDAO) this.ctx.getAttribute("recenzijaDAO");
		ArrayList<Recenzija> listaRec = new ArrayList<>();
		try {
			for (Recenzija recenzija : recenzijaDAO.getRecenzije().values()) {
				if (recenzija.isIzbrisan()) {
					System.out.println("---> Recenzija koja je izbrisana " + recenzija.getNaslovRecenzije());
					continue;
				}
				if (recenzija.getNaslovRecenzije().equals(nazivRec)) {
					System.out.println("---> Brisem ovu recenziju " + recenzija.getNaslovRecenzije());
					recenzija.setIzbrisan(true);
					continue;
				}
				System.out.println("***** Recenzija koja nije izbrisana" + recenzija.getNaslovRecenzije());
				listaRec.add(recenzija);
			}
			System.out.println("+++++++++++ISPIS RECENZIJA POSLIJE BRISANJA +++++++++++++++++");
			for (Recenzija r : listaRec)
				System.out.println(r);
			System.out.println("************************************************************************************");
		} catch (Exception exception) {
		}
		return listaRec;
	}

	@GET
	@Path("/provjeraRecenzijaProdavac/{usernameP}/{usernameUser}")
	@Produces({ "application/json" })
	public Response provjeraRecenzijaProdavac(@PathParam("usernameP") String usernameP,
			@PathParam("usernameUser") String usernameUser, @Context HttpServletRequest request) {
		if (usernameP.contains("%20"))
			usernameP.replace("%20", " ");
		System.out.println("************ " + usernameP);
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User userKupac = (User) userDAO.getUsers().get(usernameUser);
		User userProdavac = (User) userDAO.getUsers().get(usernameP);
		for (String s : userKupac.getListaRecenzijaProdavac()) {
			if (s.equals(usernameP)) {
				System.out.println("Dodali ste vec recenziju za prodavca");
				return Response.status(400).build();
			}
		}
		return Response.status(200).build();
	}

	@POST
	@Path("/dodajRecenzijuProdavac/{usernameP}")
	@Produces({ "application/json" })
	public Response dodajRecenzijuProdavac(@PathParam("usernameP") String usernameP, Recenzija novaRec,
			@Context HttpServletRequest request) {
		System.out.println(novaRec);
		UserDAO uDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User userRecenzent = (User) uDAO.getUsers().get(novaRec.getRecenzent());
		for (String s : userRecenzent.getListaRecenzijaProdavac()) {
			if (s.equals(usernameP)) {
				System.out.println("Vec ste ostavili recenziju na prodavca!");
				return Response.status(400).build();
			}
		}
		RecenzijaDAO recDAO = (RecenzijaDAO) this.ctx.getAttribute("recenzijaDAO");
		novaRec.setId(String.valueOf(recDAO.getRecenzije().size() + 1));
		novaRec.setOglas(usernameP);
		userRecenzent.getListaRecenzijaProdavac().add(usernameP);

		recDAO.dodajRecenziju(novaRec);
		return Response.status(200).build();
	}

	@GET
	@Path("/prikaziRecenzijeP/{usernameP}")
	@Produces({ "application/json" })
	public Collection<Recenzija> prikaziRecenzijeP(@PathParam("usernameP") String usernameP) {
		RecenzijaDAO recDAO = (RecenzijaDAO) this.ctx.getAttribute("recenzijaDAO");
		ArrayList<Recenzija> listaRecProdavca = new ArrayList<>();
		for (Recenzija r : recDAO.getRecenzije().values()) {
			if (r.getOglas().equals(usernameP))
				listaRecProdavca.add(r);
		}
		return listaRecProdavca;
	}
}
