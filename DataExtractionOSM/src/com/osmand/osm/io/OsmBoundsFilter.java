package com.osmand.osm.io;

import com.osmand.osm.Entity;
import com.osmand.osm.Node;
import com.osmand.osm.Relation;
import com.osmand.osm.Way;
import com.osmand.osm.Entity.EntityId;

public class OsmBoundsFilter implements IOsmStorageFilter {
	
	private final double lonEnd;
	private final double latDown;
	private final double latUp;
	private final double lonStart;

	public OsmBoundsFilter(double latStart, double lonStart, double latEnd, double lonEnd){
		this.latUp = latStart;
		this.lonStart = lonStart;
		this.latDown = latEnd;
		this.lonEnd = lonEnd;
		
	}

	@Override
	public boolean acceptEntityToLoad(OsmBaseStorage storage, Entity entity) {
		if(entity instanceof Node){
			double lon = ((Node) entity).getLongitude();
			double lat = ((Node) entity).getLatitude();
			if (latDown <= lat && lat <= latUp && lonStart <= lon && lon <= lonEnd) {
				return true;
			}
			return false;
		}
		// filter if one of the instance exists
		// IMPORTANT : The main assumption is that order is preserved in osm file (first are node, way, relation)!!!
		if(entity instanceof Way){
			for(Long l : ((Way) entity).getNodeIds()){
				if(storage.getRegisteredEntities().containsKey(l)){
					return true;
				}
			}
			return false;
		}
		if(entity instanceof Relation){
			for(EntityId l : ((Relation) entity).getMemberIds()){
				if(storage.getRegisteredEntities().containsKey(l)){
					return true;
				}
			}
			return false;
		}
		return false;
	}

}
