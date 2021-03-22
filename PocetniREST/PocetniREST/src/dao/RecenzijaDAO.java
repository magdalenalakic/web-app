package dao;

import beans.Kategorija;
import beans.Recenzija;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class RecenzijaDAO {
	private HashMap<String, Recenzija> recenzije = new HashMap<>();
	private String contextPath;

	public RecenzijaDAO() {
	}

	public RecenzijaDAO(String contextPath) {
		this.contextPath = contextPath;
		loadRec(contextPath);
	}

	private void loadRec(String contextPath) {
		String path = contextPath + File.separator + "recenzije.json";
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
				Recenzija rec = mapper.convertValue(par.getValue(), Recenzija.class);
				recenzije.put(rec.getId(), rec);
			}

		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String stringCats = objectMapper.writeValueAsString(recenzije);
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

	public void saveRecenzije() {
		String path = contextPath+ File.separator + "recenzije.json";
		File f = new File(path);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String stringCats = objectMapper.writeValueAsString(recenzije);
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

	public HashMap<String, Recenzija> getRecenzije() {
		return this.recenzije;
	}

	public void setRecenzije(HashMap<String, Recenzija> recenzije) {
		this.recenzije = recenzije;
	}

	public void dodajRecenziju(Recenzija novaRec) {
		this.recenzije.put(novaRec.getId(), novaRec);

		saveRecenzije();
	}
}
