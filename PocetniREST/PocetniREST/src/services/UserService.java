package services;

import beans.Oglas;
import beans.User;
import dao.OglasDAO;
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
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class UserService {
	@Context
	ServletContext ctx;

	@PostConstruct
	public void init() {
		if (this.ctx.getAttribute("userDAO") == null) {
			String contextPath = this.ctx.getRealPath("");
			this.ctx.setAttribute("userDAO", new UserDAO(contextPath));
		}
	}

	@POST
	@Path("/prijava")
	@Produces({ "application/json" })
	public User login(User user, @Context HttpServletRequest request) {
		UserDAO userDao = (UserDAO) this.ctx.getAttribute("userDAO");
		User loggedUser = userDao.find(user.getUsername(), user.getPassword());
		if (loggedUser == null)
			return null;
		request.getSession().setAttribute("ulogovan", loggedUser);
		User u = (User) request.getSession().getAttribute("ulogovan");
		return loggedUser;
	}

	@GET
	@Path("/odjava")
	@Consumes({ "application/json" })
	public void logout(@Context HttpServletRequest request) {
		request.getSession().invalidate();
	}

	@POST
	@Path("/registracija")
	@Produces({ "application/json" })
	public User registerUser(User user, @Context HttpServletRequest request) {
		UserDAO users = (UserDAO) this.ctx.getAttribute("userDAO");
		User ret = users.registerUser(user.getUsername(), user.getPassword(), user.getIme(), user.getPrezime(),
				user.getTel(), user.getGrad(), user.getEmail());
		if (ret == null)
			return null;
		request.getSession().setAttribute("ulogovan", ret);
		return ret;
	}

	@GET
	@Path("/ucitajKorisnike")
	@Produces({ "application/json" })
	public Collection<User> useri(@Context HttpServletRequest request) {
		UserDAO u = (UserDAO) this.ctx.getAttribute("userDAO");
		ArrayList<User> listaUsera = new ArrayList<>();
		try {
			for (User user : u.getUsers().values())
				listaUsera.add(user);
			System.out.println("+++++++++++ISPIS USERA U SERVICEE +++++++++++++++++");
			for (User user : listaUsera)
				System.out.println(user);
			System.out.println("++++////////////////////////++++++++++++++++++++++++++++++++");
		} catch (Exception exception) {
		}
		return listaUsera;
	}

	@POST
	@Path("/izmjeniUlogu/{userUlUs}")
	@Produces({ "application/json" })
	public User IzmjeniUsere(@PathParam("userUlUs") String userUlUs, @Context HttpServletRequest request) {
		System.out.println(
				"%%%%%%%%%%%%%%%%%%%%%%%%%%$$$$$$$$$$$$$$$$$$$$$$$$$$$$%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println(userUlUs);
		if (userUlUs.contains("%20"))
			userUlUs.replace("%20", " ");
		String[] parts = userUlUs.split(",");
		String ulogaUsera = parts[0];
		System.out.println(ulogaUsera);
		String usernameUsera = parts[1];
		System.out.println(usernameUsera);
		System.out.println("Izmjena uloge " + ulogaUsera + " " + usernameUsera);
		UserDAO users = (UserDAO) this.ctx.getAttribute("userDAO");
		User ret = (User) users.getUsers().get(usernameUsera);
		ret.setUloga(ulogaUsera);
		users.saveUsers();
		System.out.println(ret);
		return ret;
	}

	@POST
	@Path("/pretragaK/{ime}/{grad}")
	@Produces({ "application/json" })
	public Collection<User> pretragaK(@PathParam("ime") String ime, @PathParam("grad") String grad) {
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		ArrayList<User> listaUsera = new ArrayList<>();
		if (ime.contains("%20"))
			ime.replace("%20", " ");
		if (grad.contains("%20"))
			grad.replace("%20", " ");

		for (User user : userDAO.getUsers().values()) {
			if (user.getIme().toUpperCase().contains(ime.toUpperCase())
					&& user.getGrad().toUpperCase().contains(grad.toUpperCase()) && !ime.equals("")
					&& !grad.equals("")) {
				listaUsera.add(user);
			}
		}

		return listaUsera;
	}

	@POST
	@Path("/pretragaO/{naziv}/{minCijena}/{maxCijena}/{minOcjena}/{maxOcjena}/{grad}")
	@Produces({ "application/json" })
	public Collection<Oglas> pretragaO(@PathParam("naziv") String naziv, @PathParam("minCijena") float minCijena,
			@PathParam("maxCijena") float maxCijena, @PathParam("minOcjena") int minOcjena,
			@PathParam("maxOcjena") int maxOcjena, @PathParam("grad") String grad) {
		OglasDAO oglasDAO = (OglasDAO) this.ctx.getAttribute("oglasDAO");
		ArrayList<Oglas> listaOglasa = new ArrayList<>();

		if (naziv.contains("%20"))
			naziv.replace("%20", " ");
		if (grad.contains("%20"))
			grad.replace("%20", " ");

		for (Oglas o : oglasDAO.getOglasi().values()) {
			int cijena = Integer.parseInt(o.getCijena());
			int pozOcjena = o.getLajkovi();
			if (o.getNaziv().toUpperCase().contains(naziv.toUpperCase()) && pozOcjena >= minOcjena
					&& pozOcjena <= maxOcjena && cijena >= minCijena && cijena <= maxCijena
					&& o.getGrad().contains(grad)) {

				listaOglasa.add(o);
				continue;
			}
		}
		return listaOglasa;
	}

	@GET
	@Path("/ucitajProdavca/{usernameUser}")
	@Produces({ "application/json" })
	public User ucitajProdavca(@PathParam("usernameUser") String usernameUser) {
		System.out.println("Ucitaj prodavca za dodavanje rec ili ocjene!!!");
		System.out.println(usernameUser);
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User user = (User) userDAO.getUsers().get(usernameUser);
		return user;
	}

	@GET
	@Path("/lajkujProdavca/{usernameP}/{usernameUser}")
	@Produces({ "application/json" })
	public Response lajkujProdavca(@PathParam("usernameP") String usernameP,
			@PathParam("usernameUser") String usernameUser, @Context HttpServletRequest request) {
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User userKupac = (User) userDAO.getUsers().get(usernameUser);
		User userProdavac = (User) userDAO.getUsers().get(usernameP);
		for (String s : userKupac.getListaOcjenjenihProdavaca()) {
			if (s.equals(usernameP)) {
				return Response.status(400).build();
			}
		}
		int lajk = userProdavac.getProdavacLajk();
		lajk++;
		userProdavac.setProdavacLajk(lajk);
		userKupac.getListaOcjenjenihProdavaca().add(userProdavac.getUsername());
		userDAO.saveUsers();
		return Response.status(200).build();
	}

	@GET
	@Path("/dislajkujProdavca/{usernameP}/{usernameUser}")
	@Produces({ "application/json" })
	public Response dislajkujProdavca(@PathParam("usernameP") String usernameP,
			@PathParam("usernameUser") String usernameUser, @Context HttpServletRequest request) {
		UserDAO userDAO = (UserDAO) this.ctx.getAttribute("userDAO");
		User userKupac = (User) userDAO.getUsers().get(usernameUser);
		User userProdavac = (User) userDAO.getUsers().get(usernameP);
		for (String s : userKupac.getListaOcjenjenihProdavaca()) {
			if (s.equals(usernameP)) {
				System.out.println("Prodavca ste vec ocjenjen!!!!");
				return Response.status(400).build();
			}
		}

		int dislajk = userProdavac.getProdavacDislajk();
		dislajk++;
		userProdavac.setProdavacDislajk(dislajk);
		userKupac.getListaOcjenjenihProdavaca().add(userProdavac.getUsername());
		userDAO.saveUsers();
		return Response.status(200).build();
	}
}
