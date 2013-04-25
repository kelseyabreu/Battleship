package com.ywdac.battleship.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ywdac.battleship.events.Events.EVENT;
import com.ywdac.battleship.logic.GameState;

public class EventManager 
{
	static enum QUEUE_TYPE
	{
		FIFO,
		PRIORITY
	}
	
	private static EventManager m_Instance = null;
	private QUEUE_TYPE m_QueueType = QUEUE_TYPE.PRIORITY;
	private int m_ActiveQueue = 0;
	private boolean m_DidNotFinish = false;
	
	private ArrayList< BaseEvent > m_Queue0 = new ArrayList< BaseEvent >( );
	private ArrayList< BaseEvent > m_Queue1 = new ArrayList< BaseEvent >( );
	
	private Map< Events.EVENT, List< EventListener > > m_Map = new HashMap< Events.EVENT, List< EventListener > >( );
	
	//--------------------------------------------------------------------------------------
	
	protected EventManager( )
	{
		
	}
	
	public static EventManager get( )
	{
		if( m_Instance == null )
			m_Instance = new EventManager( );
		
		return m_Instance;
	}
	
	public void setQueueType( QUEUE_TYPE type )
	{
		m_QueueType = type;
	}
	
	//--------------------------------------------------------------------------------------
	
	/**
	 * Adds a new EventListener/Event pair to the Manager.
	 * 
	 * @param listener
	 * @param event
	 * @return TRUE if pair added, FALSE if pair already existed
	 */
	public boolean addListener( EventListener listener, EVENT event )
	{
		List< EventListener > list = m_Map.get( event );
		
		if( list == null )
		{
			m_Map.put( event, new ArrayList< EventListener >( ) );
			list = m_Map.get( event );
		}
		
		// See if the Listener/Event pair already exists
		for( int i = 0; i < list.size( ); i++ )
		{
			if( list.get( i ).equals( listener ) )
				return false;
		}
		
		// Pair does not already exist. Add listener to list in the proper
		// location based on its PROCESS
		int i = 0;
		
		//for( ; i < list.size( ); i++ )
		//{
		//	if( list.get( i ).getProcessTime( ).compareTo( listener.getProcessTime( ) ) <= 0 )
		//		break;
		//}
		
		//list.add( i, listener );
		list.add( listener );
		
		return true;
	}
	
	/**
	 * Removes a EventListener/Event pair from the Manager.
	 * 
	 * @param listener
	 * @param event
	 * @return TRUE if pairing removed, FALSE if it never existed.
	 */
	public boolean removeListener( EventListener listener, EVENT event )
	{
		List< EventListener > list = m_Map.get( event );
		
		if( list != null )
		{
			if( list.contains( listener ) )
			{
				m_Map.get( event ).remove( listener );
				
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Instantly propagates the event. Generally should not be used and queue(...) should be employed instead.
	 * 
	 * @param event
	 * @return FALSE if there were no listeners registered with the event.
	 */
	public boolean trigger( BaseEvent event )
	{
		// First make sure that there are listeners out there that care about it.
		List< EventListener > listeners = m_Map.get( event.getType( ) );
		
		if( listeners == null )
			return false;
		
		for( int i = 0; i < listeners.size( ); i++ )
			listeners.get( i ).handleEvent( event );
		
		return true;
	}
	
	/**
	 * Enqueues an event.
	 * 
	 * Event is placed on queue based on the queue type (FIFO or Priority) and
	 * its PRIORITY level.
	 * 
	 * @param event
	 */
	public void queue( BaseEvent event )
	{
		/*
		 * Two types of queues : FIFO and Priority.
		 * 
		 * If FIFO then just add straight into the active queue. 
		 * If priority place ahead/behind other events as necessary.
		 */
		
		if( m_QueueType == QUEUE_TYPE.FIFO )
		{
			if( m_ActiveQueue == 0 )
				m_Queue0.add( event );
			else
				m_Queue1.add( event );
		}
		else
		{
			ArrayList< BaseEvent > queue = ( m_ActiveQueue == 0 ? m_Queue0 : m_Queue1 );
			
			if( queue.size( ) == 0 )
				queue.add( event );
			else
			{
				// Higher the priority, the lower in the queue it goes (closer to index 0).
				int i = 0;
				
				for( ; i < queue.size( ); i++ )
				{
					if( queue.get( i ).getPriorityLevel( ).compareTo( event.getPriorityLevel( ) ) <= 0 )
						break;
				}
				
				queue.add( i, event );
			}
		}
	}
	
	/**
	 * Aborts specified event.
	 * 
	 * @param event
	 * @param abort_all_of_type If TRUE, will remove all events of type. If FALSE will remove first encountered event of type.
	 */
	public void abort( BaseEvent event, boolean abort_all_of_type )
	{
		ArrayList< BaseEvent > queue = ( m_ActiveQueue == 0 ? m_Queue0 : m_Queue1 );
		
		if( abort_all_of_type )
		{
			queue.remove( event );
		}
		else
		{
			for( int i = 0; i < queue.size( ); i++ )
			{
				if( queue.get( i ).equals( event ) )
				{
					queue.remove( i );
					break;
				}
			}
		}
	}
	
	public void abortEverything( )
	{
		m_Queue0.clear( );
		m_Queue1.clear( );
		m_Map.clear( );
	}
	
	//--------------------------------------------------------------------------------------
	
	/**
	 * Processes enqueued events.
	 * 
	 * @param time_allotted
	 * @return
	 */
	public boolean tick( long time_allotted )
	{	
		long deadline = System.currentTimeMillis( ) + time_allotted;
		
		if( m_DidNotFinish )
		{
			// Start off with finishing the old queue if possible
			processQueue( ( m_ActiveQueue == 0 ? 1 : 0 ), time_allotted );
			
			// Make sure we finished
			if( ( m_ActiveQueue == 0 ? m_Queue1 : m_Queue0 ).size( ) != 0 )
				return false;
			
			// Do we have time to start processing the new queue?
			if( System.currentTimeMillis( ) >= deadline )
			{
				// Are there even any events to process?
				if( ( m_ActiveQueue == 0 ? m_Queue0 : m_Queue1 ).size( ) == 0 )
				{
					m_DidNotFinish = false;
					return true; 	// No more event to process
				}
				
				// Out of time.
				m_ActiveQueue = ( m_ActiveQueue == 0 ? 1 : 0 );
				
				// We will start off the next tick with the new queue
				return false;
			}
			
			m_DidNotFinish = false;
		}
			
		// Swap queues to prevent infinite events
		m_ActiveQueue = ( m_ActiveQueue == 0 ? 1 : 0 );
			
		processQueue( ( m_ActiveQueue == 0 ? 1 : 0 ), ( deadline - System.currentTimeMillis( ) ) );
			
		// Did processQueue complete the active queue?
		if( m_DidNotFinish )
			return false;
		else
			return true;
	}
	
	private void processQueue( int qNumber, long time )
	{
		List< BaseEvent > queue = ( qNumber == 0 ? m_Queue0 : m_Queue1 );
		long deadline = System.currentTimeMillis( ) + time;
		
		while( queue.size( ) > 0 )
		{
			List< EventListener > listeners = m_Map.get( queue.get( 0 ).getType( ) );
			
			if( listeners != null )
			{
				if( queue.get( 0 ).getType( ) != EVENT.GAME_ANOTHER )
				{
					for( int j = 0; j < listeners.size( ); j++ )
					{
						if( listeners.get( j ).handleEvent( queue.get( 0 ) ) )
						{
							// Event was consumed by listener. 
							break;
						}
					}
				}
				else
				{
					GameState.get( ).endGame( );
					GameState.get( ).startGame( );
					
					return;
				}
			}
			
			// Remove the event
			if( !queue.isEmpty( ) )
				queue.remove( 0 );
			
			// Do we have time to process next event?
			if( System.currentTimeMillis( ) > deadline )
			{
				// Ran out of time
				m_DidNotFinish = true;
				return;
			}
		}
	}
}
