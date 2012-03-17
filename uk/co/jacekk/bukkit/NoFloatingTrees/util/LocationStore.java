package uk.co.jacekk.bukkit.NoFloatingTrees.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

public class LocationStore {
	
	private ArrayList<LocationStorable> locations;
	private File storageFile;
	
	public LocationStore(File storageFile){
		this.locations = new ArrayList<LocationStorable>();
		this.storageFile = storageFile;
		
		if (this.storageFile.exists() == false){
			try{
				this.storageFile.createNewFile();
				
				ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(this.storageFile));
				
				stream.writeObject(this.locations);
				stream.flush();
				stream.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void load(){
		try{
			this.locations = (ArrayList<LocationStorable>) new ObjectInputStream(new FileInputStream(this.storageFile)).readObject();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void save(){
		try{
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(this.storageFile));
			
			stream.writeObject(this.locations);
			stream.flush();
			stream.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public boolean contains(Location location){
		for (LocationStorable storedLocation : this.locations){
			if (storedLocation.equals(location)){
				return true;
			}
		}
		
		return false;
	}
	
	public void add(Location location){
		this.locations.add(new LocationStorable(location));
	}
	
	public void remove(Location location){
		this.locations.remove(new LocationStorable(location));
	}
	
	public List<Location> getAll(){
		ArrayList<Location> locations = new ArrayList<Location>();
		
		for (LocationStorable storedLocation : this.locations){
			locations.add(storedLocation.getLocation());
		}
		
		return locations;
	}
	
}
