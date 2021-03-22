package services;

import beans.Oglas;
import beans.Poruka;
import beans.User;
import dao.OglasDAO;
import dao.PorukaDAO;
import dao.UserDAO;
import java.util.ArrayList;
import java.util.Collection;
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

@Path("/poruka")
@Produces({ "application/json" })
public class PorukaService {
	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		if (this.ctx.getAttribute("porukaDAO") == null) {
			String contextPath = this.ctx.getRealPath("");
			this.ctx.setAttribute("porukaDAO", new PorukaDAO(contextPath));
		}
	}

	@POST
	@Path("/posaljiPorukuKP/{userPP}")
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Poruka posaljiPorukuKP(@PathParam("userPP") String userPP, Poruka poruka,
			@Context HttpServletRequest request) {

		if (userPP.contains("%20"))
			userPP.replace("%20", " ");
		String[] parts = userPP.split(",");
		String posiljalacUsername = parts[0];

		PorukaDAO porDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		OglasDAO oglasDAO = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		Oglas oglas = oglasDAO.getOglasi().get(poruka.getNazivOglasa());
		poruka.setNazivOglasa(oglas.getNaziv());

		User posiljalacUser = (User) userDAO.getUsers().get(parts[0]);
		String primalacUsername;
		if (parts.length == 1) {
			primalacUsername = oglas.getProdavac();
		} else {
			primalacUsername = parts[1];
		}

		User primalacUser = (User) userDAO.getUsers().get(primalacUsername);
		int id = porDAO.getPoruke().size();
		id++;
		poruka.setId(id);
		poruka.setIdOglasa(oglas.getId());
		poruka.setUlogaPosiljaoca(posiljalacUser.getUloga());
		poruka.setPosiljalac(posiljalacUser.getUsername());
		poruka.setPrimalac(primalacUser.getUsername());
		porDAO.getPoruke().put(Integer.valueOf(id), poruka);

		porDAO.savePor();
		return poruka;
	}

	@GET
	@Path("/prikaziPoruke/{userUsername}")
	@Produces({ "application/json" })
	public Collection<Poruka> prikaziPoruke(@PathParam("userUsername") String userUsername,
			@Context HttpServletRequest request) {
		PorukaDAO porukaDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User user = (User) userDAO.getUsers().get(userUsername);
		ArrayList<Poruka> listaPoruka = new ArrayList<>();
		for (Poruka p : porukaDAO.getPoruke().values()) {
			if (!p.isObrisan()) {
				if (p.getPosiljalac().equals(userUsername)) {
					listaPoruka.add(p);
					continue;
				}
				if (userUsername.equals(p.getPrimalac())) {
					listaPoruka.add(p);
				}
			}
		}
		return listaPoruka;
	}

	@POST
	@Path("/posaljiPorukuA/{userUsernamePos}")
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Poruka posaljiPorukuAdminu(@PathParam("userUsernamePos") String userUsernamePos, Poruka p) {
		PorukaDAO porukaDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		OglasDAO oglasDAO = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User posiljalac = (User) userDAO.getUsers().get(userUsernamePos);
		Oglas oglas = oglasDAO.getOglasi().get(p.getNazivOglasa());

		for (User u : userDAO.getUsers().values()) {
			if (u.getUloga().equals("administrator")) {
				p.setPrimalac(u.getUsername());
				p.setNazivOglasa("");
				p.setPosiljalac(posiljalac.getUsername());
				p.setUlogaPosiljaoca("prodavac");
				int id = porukaDAO.getPoruke().size();
				id++;
				p.setId(id);
				porukaDAO.getPoruke().put(Integer.valueOf(id), p);
				porukaDAO.savePor();
				return p;
			}
		}
		return null;
	}

	@POST
	@Path("/posaljiPorukuKorisniku/{primalaccUsername}")
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Poruka posaljiPorukuKorisniku(@PathParam("primalaccUsername") String primalaccUsername, Poruka p) {
		PorukaDAO porukaDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		OglasDAO oglasDAO = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User primalac = (User) userDAO.getUsers().get(primalaccUsername);
		User user = new User();
		for (User u : userDAO.getUsers().values()) {
			if (u.getUloga().equals("administrator")) {
				user = u;
				p.setUlogaPosiljaoca("administrator");
				p.setPrimalac(primalac.getUsername());
				p.setPosiljalac(user.getUsername());
				int id = porukaDAO.getPoruke().size();
				id++;
				p.setId(id);
				porukaDAO.getPoruke().put(Integer.valueOf(id), p);
				porukaDAO.savePor();
				return p;
			}
		}
		return null;
	}

	@GET
	@Path("/getUserePrimaoce")
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Collection<User> getUserePrimaoce(@Context HttpServletRequest request) {
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		ArrayList<User> listaPrimaoca = new ArrayList<>();
		for (User u : userDAO.getUsers().values()) {
			if (u.getUloga().equals("administrator"))
				continue;
			listaPrimaoca.add(u);
		}
		return listaPrimaoca;
	}

	@GET
	@Path("/getAdminPrimaoce")
	@Produces({ "application/json" })
	@Consumes({ "application/json" })
	public Collection<User> getAdminPrimaoce(@Context HttpServletRequest request) {
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		ArrayList<User> listaPrimaoca = new ArrayList<>();
		for (User u : userDAO.getUsers().values()) {
			if (u.getUloga().equals("administrator")) {
				listaPrimaoca.add(u);
			}
		}
		return listaPrimaoca;
	}

	@GET
	@Path("/brisanjePor/{por}")
	@Produces({ "application/json" })
	public Collection<Poruka> brisanjePor(@PathParam("por") String por, @Context HttpServletRequest request) {
		String[] parts = por.split(",");
		String idPor = parts[0];
		String ulogovanUser = parts[1];
		int idPoruke = Integer.parseInt(idPor);
		PorukaDAO porukaDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		ArrayList<Poruka> listaPoruka = new ArrayList<>();
		try {
			for (Poruka poruka : porukaDAO.getPoruke().values()) {
				if (poruka.isObrisan())
					continue;
				if (poruka.getId() == idPoruke) {
					poruka.setObrisan(true);
					continue;
				}
				listaPoruka.add(poruka);
			}

		} catch (Exception exception) {
		}
		porukaDAO.savePor();
		return listaPoruka;
	}

	@GET
	@Path("/ucitajPorIzmjena/{idPor}")
	@Produces({ "application/json" })
	public Poruka ucitajPorIzmjena(@PathParam("idPor") String idPor, @Context HttpServletRequest request) {
		int id = Integer.parseInt(idPor);
		PorukaDAO porukaDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		Poruka ret = (Poruka) porukaDAO.getPoruke().get(Integer.valueOf(id));
		return ret;
	}

	@POST
	@Path("/izmjeniPorukuKP/{idPor}")
	@Produces({ "application/json" })
	public Poruka izmjeniPorukuKP(@PathParam("idPor") String idPor, Poruka poruka,
			@Context HttpServletRequest request) {
		int id = Integer.parseInt(idPor);
		PorukaDAO porukaDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		Poruka staraPor = (Poruka) porukaDAO.getPoruke().get(Integer.valueOf(id));
		staraPor.setNaslov(poruka.getNaslov());
		staraPor.setSadrzaj(poruka.getSadrzaj());
		porukaDAO.savePor();
		return staraPor;
	}

	@POST
	@Path("/izmjeniPorukuA/{idPor}")
	@Produces({ "application/json" })
	public Poruka izmjeniPorukuA(@PathParam("idPor") String idPor, Poruka poruka, @Context HttpServletRequest request) {
		int id = Integer.parseInt(idPor);
		PorukaDAO porukaDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		Poruka staraPor = (Poruka) porukaDAO.getPoruke().get(Integer.valueOf(id));
		staraPor.setNaslov(poruka.getNaslov());
		staraPor.setSadrzaj(poruka.getSadrzaj());
		porukaDAO.savePor();
		return staraPor;
	}

	@POST
	@Path("/izmjeniPorukuKorisniku/{idPor}")
	@Produces({ "application/json" })
	public Poruka izmjeniPorukuKorisniku(@PathParam("idPor") String idPor, Poruka poruka,
			@Context HttpServletRequest request) {
		int id = Integer.parseInt(idPor);
		PorukaDAO porukaDAO = (PorukaDAO) this.ctx.getAttribute("porukaDAO");
		Poruka staraPor = (Poruka) porukaDAO.getPoruke().get(Integer.valueOf(id));
		staraPor.setNaslov(poruka.getNaslov());
		staraPor.setSadrzaj(poruka.getSadrzaj());
		porukaDAO.savePor();
		return staraPor;
	}
}
