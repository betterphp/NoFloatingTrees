package uk.co.jacekk.bukkit.NoFloatingTrees;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

public class NoFloatingTreesConfig {
	
	private YamlConfiguration config;
	private HashMap<String, Object> configDefaults = new HashMap<String, Object>();
	
	public NoFloatingTreesConfig(File configFile){
		this.config = new YamlConfiguration();
		
		this.configDefaults.put("remove-freq", 30);
		this.configDefaults.put("use-logblock", true);
		
		if (configFile.exists() == false){
			for (String key : this.configDefaults.keySet()){
				this.config.set(key, this.configDefaults.get(key));
			}
			
			try {
				this.config.save(configFile);
			} catch (IOException e){
				e.printStackTrace();
			}
		}else{
			try {
				this.config.load(configFile);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public boolean getBoolean(String key){
		if (this.configDefaults.containsKey(key) == false){
			return false;
		}
		
		return this.config.getBoolean(key, (Boolean) this.configDefaults.get(key));
	}
	
	public int getInt(String key){
		if (this.configDefaults.containsKey(key) == false){
			return 0;
		}
		
		return this.config.getInt(key, (Integer) this.configDefaults.get(key));
	}
	
}
