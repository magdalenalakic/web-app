package services;

import beans.Kategorija;
import beans.Oglas;
import dao.KategorijaDAO;
import dao.OglasDAO;

import java.util.ArrayList;
import java.util.Collection;
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

@Path("/kategorija")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class KategorijaService {
	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		if (this.ctx.getAttribute("kategorijaDAO") == null) {
			String contextPath = this.ctx.getRealPath("");
			this.ctx.setAttribute("kategorijaDAO", new KategorijaDAO(contextPath));
		}
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

	@POST
	@Path("/dodajNovuKat")
	@Produces({ "application/json" })
	public Kategorija dodavanjeNoveKategorije(Kategorija kategorija, @Context HttpServletRequest request) {
		KategorijaDAO katDAO = (KategorijaDAO) this.ctx.getAttribute("kategorijaDAO");
		Kategorija novaKategorija = new Kategorija(kategorija.getNaziv(), kategorija.getOpis());
		novaKategorija.setIzbrisan(false);
		katDAO.dodajKateg(novaKategorija);
		return novaKategorija;
	}

	@DELETE
	@Path("/brisanjeKateg/{nazivKategorija}")
	@Produces({ "application/json" })
	public Collection<Kategorija> brisanjeKateg(@PathParam("nazivKategorija") String nazivKategorija,
			@Context HttpServletRequest request) {
		KategorijaDAO k = (KategorijaDAO) this.ctx.getAttribute("kategorijaDAO");
		ArrayList<Kategorija> listaKategorija = new ArrayList<>();
		try {
			for (Kategorija kategorija : k.getKategorije().values()) {
				if (kategorija.isIzbrisan())
					continue;
				if (kategorija.getNaziv().equals(nazivKategorija)) {
					kategorija.setIzbrisan(true);
					continue;
				}
				listaKategorija.add(kategorija);
			}
		} catch (Exception exception) {
		}
		return listaKategorija;
	}

	@GET
	@Path("/ucitajKatIzmjena/{kategNaziv}")
	@Produces({ "application/json" })
	public Kategorija izmjenaKateg(@PathParam("kategNaziv") String kategNaziv, @Context HttpServletRequest request) {
		KategorijaDAO kategorije = (KategorijaDAO) this.ctx.getAttribute("kategorijaDAO");
		Kategorija ret = (Kategorija) kategorije.getKategorije().get(kategNaziv);
		return ret;
	}

	@PUT
	@Path("/izmjeniKat/{kategorija}")
	@Produces({ "application/json" })
	public Kategorija izmjeniKateg(@PathParam("kategorija") String kategorija, @Context HttpServletRequest request) {
		if (kategorija.contains("%20"))
			kategorija.replace("%20", " ");
		String[] parts = kategorija.split(",");
		String naziv = parts[0];
		String opis = parts[1];
		String staraKateg = parts[2];
		KategorijaDAO kategorije = (KategorijaDAO) this.ctx.getAttribute("kategorijaDAO");
		Kategorija ret = (Kategorija) kategorije.getKategorije().get(staraKateg);
		kategorije.getKategorije().remove(staraKateg);
		ret.setNaziv(naziv);
		ret.setOpis(opis);
		kategorije.getKategorije().put(ret.getNaziv(), ret);
		kategorije.saveKategorije();
		OglasDAO o = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		Collection<Oglas> oglasi = o.getOglasi().values();
		for (Oglas og : oglasi) {
			if (og.getKategKojojPripada().equals(staraKateg)) {
				og.setKategKojojPripada(naziv);
			}
		}
		o.saveOglase();
		return ret;
	}
}
