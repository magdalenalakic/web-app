package dao;

import beans.Oglas;
import beans.User;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

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

public class OglasDAO {
	private HashMap<String, Oglas> oglasi = new HashMap<>();

	private String contextPath;

	public OglasDAO() {
	}

	public OglasDAO(String contextPath) {
		this.contextPath = contextPath;
		loadOglase(contextPath);
	}

	private void loadOglase(String contextPath2) {
		String path = contextPath + File.separator + "oglasi2.json";
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
				Oglas oglas = mapper.convertValue(par.getValue(), Oglas.class);
				oglasi.put(oglas.getId(), oglas);
			}

		} catch (FileNotFoundException fnfe) {
			try {
				file.createNewFile();
				fileWriter = new FileWriter(file);
				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
				objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
				String stringAds = objectMapper.writeValueAsString(oglasi);
				fileWriter.write(stringAds);

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

	public void saveOglase() {
		String path = contextPath  + File.separator + "oglasi2.json";
		File f = new File(path);
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(f);
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			objectMapper.getFactory().configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
			String stringAds = objectMapper.writeValueAsString(oglasi);
			fileWriter.write(stringAds);
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

	public HashMap<String, Oglas> getOglasi() {
		return this.oglasi;
	}

	public void setOglasi(HashMap<String, Oglas> oglasi) {
		this.oglasi = oglasi;
	}

	public void dodajOglas(Oglas noviOglas) {
		this.oglasi.put(noviOglas.getId(), noviOglas);
		saveOglase();
	}
}
