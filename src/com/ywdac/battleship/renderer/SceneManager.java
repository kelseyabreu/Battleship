package com.ywdac.battleship.renderer;

import java.util.ArrayList;

import com.ywdac.battleship.events.BaseEvent;
import com.ywdac.battleship.events.EventListener;
import com.ywdac.battleship.events.EventManager;
import com.ywdac.battleship.events.Event_SceneLoad;
import com.ywdac.battleship.events.Event_SceneLoaded;
import com.ywdac.battleship.events.Events.EVENT;

/**
 * A simple class that keeps track and manages all scenes within the game.
 * 
 * @author ssell
 *
 */
public class SceneManager
	extends EventListener
{
	private static SceneManager m_Instance = null;
	private Scene m_Active = null;
	
	private ArrayList< Scene > m_Scenes = new ArrayList< Scene >( );
	
	protected SceneManager( )
	{
		
	}
	
	public static SceneManager get( )
	{
		if( m_Instance == null )
			m_Instance = new SceneManager( );
		
		return m_Instance;
	}
	
	public void initialize( )
	{
		EventManager.get( ).addListener( this, EVENT.SCENE_LOAD );
	}
	
	public void addScene( Scene scene )
	{
		m_Scenes.add( scene );
	}
	
	public void deleteScene( Scene scene )
	{
		scene.unload( );
		m_Scenes.remove( scene );
	}
	
	public Scene getActiveScene( )
	{
		return m_Active;
	}
	
	public Scene getScene( String id )
	{
		for( int i = 0; i < m_Scenes.size( ); i++ )
			if( m_Scenes.get( i ).getID( ).compareTo( id ) == 0 )
				return m_Scenes.get( i );
		
		return null;
	}

	@Override
	public boolean handleEvent( BaseEvent e ) 
	{
		if( e instanceof Event_SceneLoad )
		{
			Event_SceneLoad event = ( Event_SceneLoad )e;
			Scene scene = getScene( event.getScene( ) );
			
			if( scene == null )
				return false;
			
			if ( m_Active != null )
				m_Active.unload( );
			
			m_Active = scene;
			m_Active.load( );
			
			EventManager.get( ).queue( new Event_SceneLoaded( m_Active.getID( ) ) );
		}
		
		return false;
	}

	public void deleteScene( String string ) 
	{
		for( int i = 0; i < m_Scenes.size( ); i++ )
		{
			if( m_Scenes.get( i ).getID( ).compareToIgnoreCase( string ) == 0 )
			{
				deleteScene( m_Scenes.get( i ) );
				break;
			}
		}
	}
	
	public void deleteAllScenes( )
	{
		while( m_Scenes.isEmpty( ) == false )
			deleteScene( m_Scenes.get( 0 ) );
		
		m_Active = null;
	}
}
