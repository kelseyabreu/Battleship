package com.ywdac.battleship.events;

public abstract class EventListener 
{
	/**
	 * EventListeners need to let the EventManager know when they want to handle an event.
	 * An event with a lower PROCESS ( EARLY being the lowest and LAST being the highest)
	 * will receive an event before those with a higher PROCESS.
	 * 
	 * Vast majority of listeners will (and should) use the NORMAL setting while any
	 * listener that consumes an event (returns TRUE on handleEvent) should be set to LAST
	 * so that all listeners can process the event before it is consumed.
	 * 
	 * @author ssell
	 */
	static public enum PROCESS
	{
		EARLY,
		NORMAL,
		LATE,
		LAST
	}
	
	protected String m_Identifier;
	protected PROCESS m_ProcessTime;
	
	public String getIdentifier( )
	{
		return m_Identifier;
	}
	
	public PROCESS getProcessTime( )
	{
		return m_ProcessTime;
	}
	
	abstract public boolean handleEvent( BaseEvent e );
}
