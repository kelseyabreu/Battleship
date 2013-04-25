package com.ywdac.battleship.events;

import com.ywdac.battleship.events.Events.EVENT;
import com.ywdac.battleship.logic.*;

public class Event_ShipSunk 
	extends BaseEvent
{
	private Ship m_Ship = null;
	private String m_Board;
	
	public Event_ShipSunk( Ship sunkShip, String board )
	{
		this.m_Type          = EVENT.SHIP_SUNK;
		this.m_PriorityLevel = BaseEvent.PRIORITY.NORMAL;
		this.m_Descriptor    = "";
		this.m_Timestamp     = System.currentTimeMillis( );
		
		m_Ship = sunkShip;
		m_Board = board;
	}
	
	public Ship getShip( )
	{
		return m_Ship;
	}
	
	public String getBoard( )
	{
		return m_Board;
	}
}
