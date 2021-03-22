package dao;

import beans.Kategorija;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class KategorijaDAO {
	private HashMap<String, Kategorija> kategorije = new HashMap<>();
	private String contextPath;

	public KategorijaDAO() {
	}

	public KategorijaDAO(String contextPath) {
		this.contextPath = contextPath;
		loadKategorije(contextPath);
	}

	private void loadKategorije(String contextPath2) {
		String path = contextPath + File.separator + "kategorije.json";
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
				Kategorija kat = mapper.convertValue(par.getValue(), Kategorija.class);
				kategorije.put(kat.getNaziv(), kat);
			}

		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String stringCats = objectMapper.writeValueAsString(kategorije);
				fileWriter.write(stringCats);

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

	public void saveKategorije() {
		String path = contextPath + File.separator + "kategorije.json";
		File f = new File(path);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String stringCats = objectMapper.writeValueAsString(kategorije);
			fileWriter.write(stringCats);
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

	public HashMap<String, Kategorija> getKategorije() {
		return this.kategorije;
	}

	public void setKategorije(HashMap<String, Kategorija> kategorije) {
		this.kategorije = kategorije;
	}

	public void dodajKateg(Kategorija novaKategorija) {
		this.kategorije.put(novaKategorija.getNaziv(), novaKategorija);
		saveKategorije();
	}
}
