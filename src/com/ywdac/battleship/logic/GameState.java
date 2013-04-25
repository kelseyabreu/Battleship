package com.ywdac.battleship.logic;

import com.ywdac.battleship.ai.AIUnit;
import com.ywdac.battleship.events.*;
import com.ywdac.battleship.events.Events.EVENT;
import com.ywdac.battleship.renderer.BattleshipScene;
import com.ywdac.battleship.renderer.GameOverScene;
import com.ywdac.battleship.renderer.SceneManager;
import com.ywdac.battleship.renderer.TextureManager;

public class GameState
	extends EventListener
{
	public enum STATE
	{
		NONE,
		PLAYER_SETUP,
		OPPONENT_SETUP,
		PLAYER_TURN,
		OPPONENT_TURN,
		PREPPING_FOR_AI,
		GAME_OVER
	};
	
	private static GameState m_Instance = null;
	
	private STATE m_State;
	private Board m_BoardPlayer;
	private Board m_BoardOpponent;
	private AIUnit m_AIPlayer;
	
	private int m_ShipCountPlayer;
	private int m_ShipCountOpponent;
	
	protected GameState( )
	{ 
	}
	
	public static GameState get( )
	{
		if( m_Instance == null )
			m_Instance = new GameState( );
		
		return m_Instance;
	}
	
	public Board getBoard( String id )
	{
		if( id.compareToIgnoreCase( "player" ) == 0 )
			return m_BoardPlayer;
		else
			return m_BoardOpponent;
	}
	
	public STATE getState( )
	{
		return m_State;
	}
	
	public void startGame( )
	{
		EventManager.get( ).addListener( this, EVENT.TURN_END );
		EventManager.get( ).addListener( this, EVENT.GAME_END );
		EventManager.get( ).addListener( this, EVENT.FIRE_UPON );
		EventManager.get( ).addListener( this, EVENT.SHIP_SUNK );
		EventManager.get( ).addListener( this, EVENT.GAME_ANOTHER );
		
		m_State             = STATE.NONE;
		m_BoardPlayer       = new Board( "player" );
		m_BoardOpponent     = new Board( "opponent" );	
		m_AIPlayer          = new AIUnit( );
		m_ShipCountPlayer   = 5;
		m_ShipCountOpponent = 5;
		
		SceneManager.get( ).initialize( );
		SceneManager.get( ).addScene( new BattleshipScene( "game_scene" ) );
		SceneManager.get( ).addScene( new GameOverScene( "game_over_win" ) );
		SceneManager.get( ).addScene( new GameOverScene( "game_over_loss" ) );
		
		EventManager.get( ).queue( new Event_SceneLoad( "game_scene" ) );
	}
	
	public void endGame( )
	{
		EventManager.get( ).abortEverything( );
		SceneManager.get( ).deleteAllScenes( );
		//TextureManager.get( ).destroy( );
	}

	@Override
	public boolean handleEvent( BaseEvent e ) 
	{
		// Also do crude pauses. If had more time I would
		// implement a non-stupid timing system.
		
		if( e instanceof Event_TurnEnd )
		{
			switch( m_State )
			{
			case NONE:
			{
				m_State = STATE.PLAYER_SETUP;
				BattleshipScene scene = ( BattleshipScene )SceneManager.get( ).getActiveScene( );
				
				scene.prepareForSetUp( );
				//m_BoardPlayer.BOARD_TEST_PLACEMENT( );
				break;
			}
			case PLAYER_SETUP:
				m_State = STATE.OPPONENT_SETUP;
				m_AIPlayer.SetShipLocations( );
				//m_BoardOpponent.BOARD_TEST_PLACEMENT( );
				break;
			case OPPONENT_SETUP:
				m_State = STATE.PLAYER_TURN;
				EventManager.get( ).queue( new Event_TurnBegin( "player" ) );
				break;
			case PLAYER_TURN:
				m_State = STATE.PREPPING_FOR_AI;
				EventManager.get( ).trigger( new Event_TurnBegin( "opponent" ) );
				break;
			case PREPPING_FOR_AI:
				m_State = STATE.OPPONENT_TURN;
				m_AIPlayer.TakeTurn( );
				break;
			case OPPONENT_TURN:
			{
				long time = System.currentTimeMillis( );
				while( System.currentTimeMillis( ) - time < 1000 );
				
				EventManager.get( ).queue( new Event_TurnBegin( "player" ) );
				m_State = STATE.PLAYER_TURN;
				break;
			}
			}
		}
		else if( e instanceof Event_FireUpon )
		{
			Event_FireUpon event = ( Event_FireUpon )e;
			
			Board active;
			
			if( m_State == STATE.PLAYER_TURN )
				active = m_BoardOpponent;
			else
				active = m_BoardPlayer;
			
			String coords = event.getCoords( );
			
			int x, y;
			
			x = coords.charAt( 0 ) - 'A';
			y = Integer.parseInt( coords.substring( 1 ) );
			
			EventManager.get( ).queue( new Event_FireResponse( active.fireUpon( x, y ), x, y ) );
		}
		else if( e instanceof Event_ShipSunk )
		{
			Event_ShipSunk event = ( Event_ShipSunk )e;
			
			if( event.getBoard( ).compareToIgnoreCase( "player" ) == 0 )
			{
				m_ShipCountPlayer--;
			}
			else
			{
				m_ShipCountOpponent--;
			}
			
			if( m_ShipCountPlayer == 0 )
			{
				EventManager.get( ).queue( new Event_SceneLoad( "game_over_loss" ) );
				m_State = STATE.GAME_OVER;
				
				return true;
			}
			else if( m_ShipCountOpponent == 0 )
			{
				EventManager.get( ).queue( new Event_SceneLoad( "game_over_win" ) );
				m_State = STATE.GAME_OVER;
				
				return true;
			}
		}
		else if( e instanceof Event_GameAnother )
		{
			//endGame( );
			//startGame( );
			//m_State = STATE.NONE;
		}
		
		return false;
	}
}
