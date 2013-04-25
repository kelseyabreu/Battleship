package com.ywdac.battleship.events;

import com.ywdac.battleship.events.Events.EVENT;

public class Event_SceneLoaded 
	extends BaseEvent
{
	private String m_Scene = null;
	
	public Event_SceneLoaded( String scene )
	{
		this.m_Descriptor    = "";
		this.m_PriorityLevel = PRIORITY.CRITICAL;
		this.m_Timestamp     = System.currentTimeMillis( );
		this.m_Type          = EVENT.SCENE_LOADED;
		
		m_Scene = scene;
	}
	
	public String getScene( )
	{
		return m_Scene;
	}
}
