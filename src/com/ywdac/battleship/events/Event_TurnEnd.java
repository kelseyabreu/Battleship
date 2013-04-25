package com.ywdac.battleship.events;

import com.ywdac.battleship.events.Events.EVENT;

public class Event_TurnEnd
	extends BaseEvent
{
	public Event_TurnEnd( )
	{
		this.m_Descriptor    = "";
		this.m_PriorityLevel = PRIORITY.HIGH;
		this.m_Timestamp     = System.currentTimeMillis( );
		this.m_Type          = EVENT.TURN_END;
	}
}
