package com.ywdac.battleship.events;

import com.ywdac.battleship.events.Events.EVENT;

public class Event_GameEnd 
	extends BaseEvent
{
	public Event_GameEnd( )
	{
		this.m_Descriptor    = "";
		this.m_PriorityLevel = PRIORITY.CRITICAL;
		this.m_Timestamp     = System.currentTimeMillis( );
		this.m_Type          = EVENT.GAME_END;
	}
}
