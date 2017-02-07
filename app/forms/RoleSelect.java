package forms;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import lib.BootstrapSelectKeyValue;
import models.SecurityRole;

public class RoleSelect {
	
	private SecurityRole role;
	private EntityManager em;
	
    public RoleSelect(EntityManager em){
    	this.em = em;
	}
	
	public RoleSelect(EntityManager em, SecurityRole role){
		this.em = em;
		this.role = role;
	}
	
	public List<lib.BootstrapSelectKeyValue> populateVenueSelect()
	{
		Query query = em.createQuery("SELECT e FROM SecurityRole e");
		@SuppressWarnings("unchecked")
		List<SecurityRole> roleDB = query.getResultList();

		List<lib.BootstrapSelectKeyValue> roles = new ArrayList<lib.BootstrapSelectKeyValue>();

		for (SecurityRole role : roleDB) {
			
			BootstrapSelectKeyValue keyVal = null;
			if(role == this.role)
			{
				keyVal = new BootstrapSelectKeyValue(role.getId().toString(), role.getName(), true);
				//venues.put(keyVal, true);
			}
			else
			{
				keyVal = new BootstrapSelectKeyValue(role.getId().toString(), role.getName(), false);
				//venues.put(keyVal, false);
			}
			
			roles.add(keyVal);
		}
		
		return roles;
	}
	
	public List<lib.BootstrapSelectKeyValue> updateVenueSelect(List<Integer> rolesSelected, List<lib.BootstrapSelectKeyValue> roles){
		
		for(Integer i = 0; i < roles.size(); i++)
		{		
			lib.BootstrapSelectKeyValue role = roles.get(i);
			
			for(Integer roleId: rolesSelected)
			{
				if(role.getId().equals(roleId.toString()))
				{
					role.setSelected(true);
					break;
				}
			}			
			
			roles.set(i, role);
		}
		
		return roles;
	}
}
