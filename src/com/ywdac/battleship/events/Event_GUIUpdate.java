package com.ywdac.battleship.events;

import com.ywdac.battleship.events.Events.EVENT;

public class Event_GUIUpdate 
	extends BaseEvent
{
	private String m_Text;
	
	public Event_GUIUpdate( String text )
	{
		this.m_Descriptor    = "";
		this.m_PriorityLevel = PRIORITY.NORMAL;
		this.m_Timestamp     = System.currentTimeMillis( );
		this.m_Type          = EVENT.GUI_UPDATE;
		
		m_Text = text;
	}
	
	public String getText( )
	{
		return m_Text;
	}
}
