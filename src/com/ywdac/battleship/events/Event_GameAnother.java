package com.ywdac.battleship.events;

import com.ywdac.battleship.events.Events.EVENT;

public class Event_GameAnother 
	extends BaseEvent
{
	public Event_GameAnother( )
	{
		this.m_Descriptor    = "";
		this.m_PriorityLevel = PRIORITY.HIGH;
		this.m_Timestamp     = System.currentTimeMillis( );
		this.m_Type          = EVENT.GAME_ANOTHER;
	}
}
