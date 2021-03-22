package dao;

import beans.Oglas;
import beans.User;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class UserDAO {
	private HashMap<String, User> users = new HashMap<>();

	private String contextPath;

	public UserDAO() {
	}

	public UserDAO(String contextPath) {
		this.contextPath = contextPath;
		loadUsers(contextPath);
	}

	public User registerUser(String username, String password, String ime, String prezime, int telefon, String grad,
			String email) {
		User user = new User(username, password, "kupac", ime, prezime, telefon, grad, email);
		
		boolean postoji = false;
		for (User korisnik : this.users.values()) {
			if (korisnik.getUsername().equals(username))
				postoji = true;
		}
		if (!postoji) {
			this.users.put(user.getUsername(), user);
			user.setUloga("kupac");
			saveUsers();
			return user;
		}
		return null;
	}

	private void loadUsers(String contextPath) {
		String path = contextPath + File.separator + "users.json";
		FileWriter fileWriter = null;
		BufferedReader in = null;
		File file = null;
		try {
			file = new File(path);
			in = new BufferedReader(new FileReader(file));

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.setVisibilityChecker(
					VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
			TypeFactory factory = TypeFactory.defaultInstance();
			MapType type = factory.constructMapType(HashMap.class, String.class, Object.class);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			@SuppressWarnings("unchecked")
			HashMap<String, Object> data = (HashMap<String, Object>) objectMapper.readValue(file, type);
			for (Map.Entry<String, Object> par : data.entrySet()) {
				ObjectMapper mapper = new ObjectMapper();
				String jsonInString = (String) par.toString();
				User user = mapper.convertValue(par.getValue(), User.class);
				users.put(user.getUsername(), user);
			}

		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String stringUsers = objectMapper.writeValueAsString(users);
				fileWriter.write(stringUsers);

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileWriter != null) {
					try {
						fileWriter.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void saveUsers() {
		String path = contextPath + File.separator + "users.json";
		File f = new File(path);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String stringUsers = objectMapper.writeValueAsString(users);
			fileWriter.write(stringUsers);
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileWriter != null) {
				try {
					fileWriter.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String toString() {
		return "UserDAO [users=" + this.users + "]";
	}

	public HashMap<String, User> getUsers() {
		return this.users;
	}

	public void setUsers(HashMap<String, User> users) {
		this.users = users;
	}

	public User find(String username, String password) {
		for (User user : this.users.values()) {
			if (username.equals(user.getUsername())) {
				if (password.equals(user.getPassword()))
					return user;
				return null;
			}
		}
		return null;
	}

	public Collection<User> findAll() {
		return this.users.values();
	}

	public User findUser(String username) {
		return this.users.containsKey(username) ? this.users.get(username) : null;
	}

	public User update(String username, User user) {
		String uloga = user.getUloga();
		User pronadjen = findUser(username);
		pronadjen.setUloga(uloga);
		saveUsers();
		return pronadjen;
	}
}
