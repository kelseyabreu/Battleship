package com.ywdac.battleship.renderer;

import com.ywdac.battleship.R;
import com.ywdac.battleship.events.*;
import com.ywdac.battleship.events.Events.EVENT;
import com.ywdac.battleship.logic.*;
import com.ywdac.battleship.renderer.SceneNode.RENDER_TYPE;
import com.ywdac.battleship.utilities.RNG;

import android.opengl.GLES11;

public class BattleshipScene 
	extends Scene
{
	private enum SCENE_STATE
	{
		PLAYER_CREATE,
		IDLE,
		MOVING_CAMERA_TO_PLAYER,
		IDLE_ON_PLAYER,
		MOVING_CAMERA_TO_OPPONENT,
		IDLE_ON_OPPONENT,
		WAITING_ON_FIRE
	};
	
	private SkyboxNode m_Skybox;
	private GridNode m_GridPlayer;
	private GridNode m_GridOpponent;
	private Camera m_Camera;
	private SceneNode m_Center;

	private ShipNode m_SetUpCarrier;
	private ShipNode m_SetUpBattleship;
	private ShipNode m_SetUpDestroyer;
	private ShipNode m_SetUpSubmarine;
	private ShipNode m_SetUpCruiser;
	private ShipNode m_Selected;
	private SceneNode m_RotateArrow;
	
	private SCENE_STATE m_State = SCENE_STATE.IDLE;
	
	public BattleshipScene( String id ) 
	{
		super( id );
		
		m_Tree = new BattleshipSceneTree( );
		
		EventManager.get( ).addListener( this, EVENT.TURN_BEGIN );
		EventManager.get( ).addListener( this, EVENT.USER_IO );
		EventManager.get( ).addListener( this, EVENT.FIRE_RESPONSE );
		EventManager.get( ).addListener( this, EVENT.SHIP_SUNK );
		EventManager.get( ).addListener( this, EVENT.SHIP_ADD );
	}

	@Override
	public void load( ) 
	{
		m_Skybox       = new SkyboxNode( "test_sky", RENDER_TYPE.SOLID_TEXTURE, R.drawable.skybox );
		m_GridPlayer   = new GridNode( "player_grid", RENDER_TYPE.SOLID_COLOR );
		m_GridOpponent = new GridNode( "opponent_grid", RENDER_TYPE.SOLID_COLOR );
		m_Camera       = new Camera( "camera", RENDER_TYPE.NONE );
		m_Center       = new SceneNode( "center_decor", RENDER_TYPE.TRANSPARENT_TEXTURE );
	
		m_GridPlayer.moveRight( -60.f );
		m_GridPlayer.setColor( 0.f, 1.f, 0.f, 1.f );
		m_GridOpponent.moveRight( 60.f );
		m_GridOpponent.setColor( 1.f, 0.f, 0.f, 1.f );
		
		m_Camera.setPosition( -60.f, 0.f, 100.f );
		m_Camera.lookAt( m_GridPlayer );
		
		m_Center.setPosition( -25.f, -25.f, 0.f );
		
		ModelPrimitiveQuad centerModel = new ModelPrimitiveQuad( 50, 50 );
		
		// Get random center
		RNG rng = new RNG( System.currentTimeMillis( ) );
		int choice = ( int )rng.next( 3 );
		
		switch( choice )
		{
		case 0:
			centerModel.setTexture( R.drawable.center_earth );
			break;
		case 1:
			centerModel.setTexture( R.drawable.center_moon );
			break;
		case 2:
			centerModel.setTexture( R.drawable.center_mars );
			break;
		}
		
		m_Center.attachModel( centerModel );
		
		loadTiles( );

		m_Tree.addNode( m_Skybox );
		m_Tree.addNode( m_GridPlayer );
		m_Tree.addNode( m_GridOpponent );
		m_Tree.addNode( m_Camera );
		m_Tree.addNode( m_Center );
		
		m_Tree.load( );
		
		EventManager.get( ).queue( new Event_TurnEnd( ) );		
	}

	@Override
	public void unload( ) 
	{
		m_Tree.unload( );
	}
	
	public void update( )
	{
		switch( m_State )
		{
		case MOVING_CAMERA_TO_PLAYER:
		{
			if( m_Camera.getPosition( )[ 0 ] != m_GridPlayer.getPosition( )[ 0 ] )
			{
				long time = System.currentTimeMillis( );
				while( System.currentTimeMillis( ) - time < 5 );
				m_Camera.moveRight( -2.f );
			}
			else
			{
				m_State = SCENE_STATE.IDLE_ON_PLAYER;
				EventManager.get( ).queue( new Event_TurnEnd( ) );
			}
			
			break;
		}
		
		case MOVING_CAMERA_TO_OPPONENT:
		{
			if( m_Camera.getPosition( )[ 0 ] != m_GridOpponent.getPosition( )[ 0 ] )
			{
				long time = System.currentTimeMillis( );
				while( System.currentTimeMillis( ) - time < 5 );
				m_Camera.moveRight( 2.f );
			}
			else
			{
				m_State = SCENE_STATE.IDLE_ON_OPPONENT;
			}
			
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void draw( )
	{
		GLES11.glPushMatrix( );
		GLES11.glLoadIdentity( );
		
		m_Tree.draw( );
		
		GLES11.glPopMatrix( );
	}
	
	private void loadTiles( )
	{
		float color = 0.f;
		
		for( int y = 0; y < 10; y++ )
		{
			for( int x = 0; x < 10; x++ )
			{
				
				TileNode tile = new TileNode( 
						x + ":" + y + ":player",		// String ID
						RENDER_TYPE.TRANSPARENT_COLOR, 	// Render class
						x, 								// Board X [0-9]
						y, 								// Board Y [0-9]
						"player", 						// Board String ID ('player' or 'opponent')
						color,							// Color ID for picking : R value
						0.5f,							// Color ID for picking : G value
						0.5f );							// Color ID for picking : B value
				
				m_Tree.addNode( tile );
				
				color += 0.01f;
			}
		}
		
		color = 0.f;
		
		for( int y = 0; y < 10; y++ )
		{
			for( int x = 0; x < 10; x++ )
			{
				
				TileNode tile = new TileNode( 
						x + ":" + y + ":opponent",		// String ID
						RENDER_TYPE.TRANSPARENT_COLOR, 	// Render class
						x, 								// Board X [0-9]
						y, 								// Board Y [0-9]
						"opponent", 					// Board String ID ('player' or 'opponent')
						0.5f,							// Color ID for picking : R value
						color,							// Color ID for picking : G value
						0.5f );							// Color ID for picking : B value
				
				m_Tree.addNode( tile );
				
				color += 0.01f;
			}
		}
	}
	
	private void handlePicking( float x, float y )
	{
		/**
		 * This is really stupid picking but not enough time currently
		 * to implement ray or color picking, plus the android opengl 
		 * wrapper makes it a lot harder than it would normally be.
		 */
		
		/**
		 * The grid ranges on X-Axis from: 31 to 448
		 * The grid ranges on Y-Axis from: 246 to 663
		 */
		
		float cellSize = 417 / 10.f;
		
		float xRel = x - 31;
		float yRel = y - 246;
		
		int cellX = ( int )( xRel / cellSize );
		int cellY = ( int )( yRel / cellSize );
		
		if( cellX < 0 || cellX > 9 || cellY < 0 || cellY > 9 )
			return;
		
		String coords = "";
		
		switch( cellX )
		{
		case 0:
			coords = "A" + cellY;
			break;
		case 1:
			coords = "B" + cellY;
			break;
		case 2:
			coords = "C" + cellY;
			break;
		case 3:
			coords = "D" + cellY;
			break;
		case 4:
			coords = "E" + cellY;
			break;
		case 5:
			coords = "F" + cellY;
			break;
		case 6:
			coords = "G" + cellY;
			break;
		case 7:
			coords = "H" + cellY;
			break;
		case 8:
			coords = "I" + cellY;
			break;
		case 9:
			coords = "J" + cellY;
			break;
		}
		
		EventManager.get( ).queue( new Event_FireUpon( coords ) );
		m_State = SCENE_STATE.WAITING_ON_FIRE;
	}

	@Override
	public boolean handleEvent( BaseEvent e ) 
	{
		if( e instanceof Event_TurnBegin )
		{
			/**
			 * We must pan the camera to the board belonging to that player
			 */
			
			Event_TurnBegin event = ( Event_TurnBegin )e;
			
			if( event.getWho( ).toLowerCase( ).compareTo( "player" ) == 0 )
			{
				// Are we already focused on player grid?
				float camera_pos[ ] = m_Camera.getPosition( );
				float grid_pos[ ]   = m_GridOpponent.getPosition( );
				
				if( camera_pos[ 0 ] != grid_pos[ 0 ] )
				{
					m_State = SCENE_STATE.MOVING_CAMERA_TO_OPPONENT;
				}
			}
			else
			{
				// Are we already focused on opponent grid?
				float camera_pos[ ] = m_Camera.getPosition( );
				float grid_pos[ ]   = m_GridPlayer.getPosition( );
				
				if( camera_pos[ 0 ] != grid_pos[ 0 ] )
				{
					m_State = SCENE_STATE.MOVING_CAMERA_TO_PLAYER;
				}
			}
			
			// DELETE ME
			/*Board board = GameState.get( ).getBoard( "opponent" );
			
			for( int y = 0; y < 10; y++ )
			{
				for( int x = 0; x < 10; x++ )
				{
					String tileID;
					
					if( board.m_Tiles[ x ][ y ].isOccupied( ) )
					{
						tileID = x + ":" + y + ":opponent";
						TileNode node = ( TileNode )m_Tree.getNode( tileID );
						node.setColor( 0.f, 0.7f, 0.7f );
					}
				}
			}*/
		}
		else if( e instanceof Event_UserIO )
		{
			Event_UserIO event = ( Event_UserIO )e;
			
			if( m_State == SCENE_STATE.IDLE_ON_OPPONENT )
			{
				handlePicking( event.getX( ), event.getY( ) );
			}
			else if( m_State == SCENE_STATE.PLAYER_CREATE )
			{
				specialPick( event.getX( ), event.getY( ) );
			}
		}
		else if( e instanceof Event_FireResponse )
		{
			Event_FireResponse event = ( Event_FireResponse )e;
			int response = event.getResponse( );
			
			// Make sure it was a hit or miss
			if( response == -1 )
			{
				if( m_State == SCENE_STATE.WAITING_ON_FIRE )
					m_State = SCENE_STATE.IDLE_ON_OPPONENT;
				
				return false;
			}
			
			//----------------------------------------
			// Get the coordinates / board
			
			int coord[ ] = event.getCoords( );
			String tileID = "";
			
			if( m_State == SCENE_STATE.WAITING_ON_FIRE )
				tileID = coord[ 0 ] + ":" + coord[ 1 ] + ":opponent";
			else if( m_State == SCENE_STATE.IDLE_ON_PLAYER )
				tileID = coord[ 0 ] + ":" + coord[ 1 ] + ":player";
				
			//----------------------------------------
			
			TileNode node = ( TileNode )m_Tree.getNode( tileID );
			
			if( node != null )
			{
				// Hit
				if( response != 0 )
				{
					node.hit( true );
					
					if( m_State == SCENE_STATE.WAITING_ON_FIRE )
						m_State = SCENE_STATE.IDLE_ON_OPPONENT;
					else if( m_State == SCENE_STATE.IDLE_ON_PLAYER )
						m_State = SCENE_STATE.IDLE_ON_PLAYER;
				}
				else	// Miss
				{
					node.hit( false );
					
					EventManager.get( ).queue( new Event_TurnEnd( ) );
				}
			}	
		}
		else if( e instanceof Event_ShipSunk )
		{
			Event_ShipSunk event = ( Event_ShipSunk )e;
			
			Ship ship = event.getShip( );
			String shipType = "";
			
			if( ship instanceof Carrier )
			{
				shipType = "carrier";
			}
			else if( ship instanceof Destroyer )
			{
				shipType = "destroyer";
			}
			else if( ship instanceof Battleship )
			{
				shipType = "battleship";
			}
			else if( ship instanceof Cruiser )
			{
				shipType = "cruiser";
			}
			else
			{
				shipType = "submarine";
			}
			
			//------------------------------------------------------------------------------
			
			if( ship.getBoard( ).compareToIgnoreCase( "opponent" ) == 0 )
			{
				Tile tiles[ ] = ship.m_OccupyTiles;
				TileNode nodes[ ] = new TileNode[ tiles.length ];
				
				for( int i = 0; i < nodes.length; i++ )
				{
					if( tiles[ i ] != null )
					{
						nodes[ i ] = ( TileNode )m_Tree.getNode( ( tiles[ i ].m_X + ":" + tiles[ i ].m_Y + ":" + ship.getBoard( ) ) );
					}
					else
					{
						break;
					}
				}
				
				m_Tree.addNode( new ShipNode( shipType, RENDER_TYPE.TRANSPARENT_TEXTURE, nodes, ship.getBoard( ) ) );
			}
			else
			{
				ShipNode shipNode = null;
				
				BattleshipSceneTree tree = ( BattleshipSceneTree )m_Tree;
				shipNode = tree.getShipNode( shipType );
				
				if( shipNode != null )
				{
					shipNode.sink( );
				}
			}
		}
		else if( e instanceof Event_ShipAdd )
		{
			Event_ShipAdd event = ( Event_ShipAdd )e;
			
			Ship ship = event.getShip( );
			
			Tile tiles[ ] = ship.m_OccupyTiles;
			TileNode nodes[ ] = new TileNode[ tiles.length ];
				
			for( int i = 0; i < nodes.length; i++ )
			{
				if( tiles[ i ] != null )
				{
					nodes[ i ] = ( TileNode )m_Tree.getNode( ( tiles[ i ].m_X + ":" + tiles[ i ].m_Y + ":" + event.getShip( ).getBoard( ) ) );
				}
				else
				{
					break;
				}
			}
				
			String shipType;
				
			if( ship instanceof Carrier )
			{
				shipType = "carrier";
			}
			else if( ship instanceof Destroyer )
			{
				shipType = "destroyer";
			}
			else if( ship instanceof Battleship )
			{
				shipType = "battleship";
			}
			else if( ship instanceof Cruiser )
			{
				shipType = "cruiser";
			}
			else
			{
				shipType = "submarine";
			}
				
			m_Tree.addNode( new ShipNode( shipType, RENDER_TYPE.TRANSPARENT_TEXTURE, nodes, event.getShip( ).getBoard( ) ) );
		}
		
		return false;
	}
	
	public void prepareForSetUp( )
	{
		m_SetUpCarrier    = new ShipNode( "carrier", RENDER_TYPE.TRANSPARENT_TEXTURE, null, "player" );
		m_SetUpBattleship = new ShipNode( "battleship", RENDER_TYPE.TRANSPARENT_TEXTURE, null, "player" );
		m_SetUpDestroyer  = new ShipNode( "destroyer", RENDER_TYPE.TRANSPARENT_TEXTURE, null, "player" );
		m_SetUpSubmarine  = new ShipNode( "submarine", RENDER_TYPE.TRANSPARENT_TEXTURE, null, "player" );
		m_SetUpCruiser    = new ShipNode( "cruiser", RENDER_TYPE.TRANSPARENT_TEXTURE, null, "player" );
		m_RotateArrow     = new SceneNode( "rotateArrow", RENDER_TYPE.TRANSPARENT_TEXTURE );
		
		ShipNode shadowCarrier    = new ShipNode( "carrier", RENDER_TYPE.TRANSPARENT_TEXTURE, null, "player" );
		ShipNode shadowBattleship = new ShipNode( "battleship", RENDER_TYPE.TRANSPARENT_TEXTURE, null, "player" );
		ShipNode shadowDestroyer  = new ShipNode( "destroyer", RENDER_TYPE.TRANSPARENT_TEXTURE, null, "player" );
		ShipNode shadowSubmarine  = new ShipNode( "submarine", RENDER_TYPE.TRANSPARENT_TEXTURE, null, "player" );
		ShipNode shadowCruiser    = new ShipNode( "cruiser", RENDER_TYPE.TRANSPARENT_TEXTURE, null, "player" );
		
		SceneNode playButton  = new SceneNode( "playButton", RENDER_TYPE.TRANSPARENT_TEXTURE );
		
		shadowCarrier.moveForward( 0.5f );
		shadowBattleship.moveForward( 0.5f );
		shadowDestroyer.moveForward( 0.5f );
		shadowSubmarine.moveForward( 0.5f );
		shadowCruiser.moveForward( 0.5f );
		
		shadowCarrier.sink( );
		shadowBattleship.sink( );
		shadowDestroyer.sink( );
		shadowSubmarine.sink( );
		shadowCruiser.sink( );
		
		ModelPrimitiveQuad rotateModel = new ModelPrimitiveQuad( 6, 6 );
		rotateModel.setTexture( R.drawable.rotate );
		m_RotateArrow.attachModel( rotateModel );
		m_RotateArrow.setPosition( -85.f, 27.f, 0.01f );
		
		ModelPrimitiveQuad playModel = new ModelPrimitiveQuad( 10, 5 );
		playModel.setTexture( R.drawable.play_button );
		playButton.attachModel( playModel );
		playButton.setPosition( -45.f, 27.f, 0.01f );
		
		m_Tree.addNode( m_SetUpCarrier );
		m_Tree.addNode( m_SetUpBattleship );
		m_Tree.addNode( m_SetUpDestroyer );
		m_Tree.addNode( m_SetUpSubmarine );
		m_Tree.addNode( m_SetUpCruiser );
		m_Tree.addNode( m_RotateArrow );
		m_Tree.addNode( playButton );
		
		m_Tree.addNode( shadowCarrier );
		m_Tree.addNode( shadowBattleship );
		m_Tree.addNode( shadowDestroyer );
		m_Tree.addNode( shadowSubmarine );
		m_Tree.addNode( shadowCruiser );
		
		m_State = SCENE_STATE.PLAYER_CREATE;
	}
	
	public void cleanUpSetUp( )
	{
		m_Tree.deleteNode( "" );
	}
	
	public void populateLogicBoard( )
	{
		Board board = GameState.get( ).getBoard( "player" );
		int x, y;
		
		//----------------------------------------------------------------------------------
		// Carrier
		
		x = m_SetUpCarrier.getCells( )[ 0 ];
		y = m_SetUpCarrier.getCells( )[ 1 ];
		
		if( !m_SetUpCarrier.isRotated( ) )
		{
			// Straight across
			for( int i = 0; i < 5; i++ )
			{
				board.m_Tiles[ x + i ][ y ].m_OccupiedBy = board.m_Carrier;
				board.m_Carrier.m_OccupyTiles[ i ] = board.m_Tiles[ x + i ][ y ];
			}
		}
		else
		{
			for( int i = 0; i < 5; i++ )
			{
				board.m_Tiles[ x ][ y - i ].m_OccupiedBy = board.m_Carrier;
				board.m_Carrier.m_OccupyTiles[ i ] = board.m_Tiles[ x ][ y - i ];
			}
		}
		
		//----------------------------------------------------------------------------------
		// Battleship
		
		x = m_SetUpBattleship.getCells( )[ 0 ];
		y = m_SetUpBattleship.getCells( )[ 1 ];
				
		if( !m_SetUpBattleship.isRotated( ) )
		{
			// Straight across
			for( int i = 0; i < 4; i++ )
			{
				board.m_Tiles[ x + i ][ y ].m_OccupiedBy = board.m_Battleship;
				board.m_Battleship.m_OccupyTiles[ i ] = board.m_Tiles[ x + i ][ y ];
			}
		}
		else
		{
			for( int i = 0; i < 4; i++ )
			{
				board.m_Tiles[ x ][ y - i ].m_OccupiedBy = board.m_Battleship;
				board.m_Battleship.m_OccupyTiles[ i ] = board.m_Tiles[ x ][ y - i ];
			}
		}
		
		//----------------------------------------------------------------------------------
		// Destroyer
				
		x = m_SetUpDestroyer.getCells( )[ 0 ];
		y = m_SetUpDestroyer.getCells( )[ 1 ];
				
		if( !m_SetUpDestroyer.isRotated( ) )
		{
			// Straight across
			for( int i = 0; i < 3; i++ )
			{
				board.m_Tiles[ x + i ][ y ].m_OccupiedBy = board.m_Destroyer;
				board.m_Destroyer.m_OccupyTiles[ i ] = board.m_Tiles[ x + i ][ y ];
			}
		}
		else
		{
			for( int i = 0; i < 3; i++ )
			{
				board.m_Tiles[ x ][ y - i ].m_OccupiedBy = board.m_Destroyer;
				board.m_Destroyer.m_OccupyTiles[ i ] = board.m_Tiles[ x ][ y - i ];
			}
		}
		
		//----------------------------------------------------------------------------------
		// Submarine
						
		x = m_SetUpSubmarine.getCells( )[ 0 ];
		y = m_SetUpSubmarine.getCells( )[ 1 ];
						
		if( !m_SetUpSubmarine.isRotated( ) )
		{
			// Straight across
			for( int i = 0; i < 3; i++ )
			{
				board.m_Tiles[ x + i ][ y ].m_OccupiedBy = board.m_Submarine;
				board.m_Submarine.m_OccupyTiles[ i ] = board.m_Tiles[ x + i ][ y ];
			}
		}
		else
		{
			for( int i = 0; i < 3; i++ )
			{
				board.m_Tiles[ x ][ y - i ].m_OccupiedBy = board.m_Submarine;
				board.m_Submarine.m_OccupyTiles[ i ] = board.m_Tiles[ x ][ y - i ];
			}
		}
		
		//----------------------------------------------------------------------------------
		// Cruiser
								
		x = m_SetUpCruiser.getCells( )[ 0 ];
		y = m_SetUpCruiser.getCells( )[ 1 ];
							
		if( !m_SetUpCruiser.isRotated( ) )
		{
			// Straight across
			for( int i = 0; i < 2; i++ )
			{
				board.m_Tiles[ x + i ][ y ].m_OccupiedBy = board.m_Cruiser;
				board.m_Cruiser.m_OccupyTiles[ i ] = board.m_Tiles[ x + i ][ y ];
			}
		}
		else
		{
			for( int i = 0; i < 2; i++ )
			{
				board.m_Tiles[ x ][ y - i ].m_OccupiedBy = board.m_Cruiser;
				board.m_Cruiser.m_OccupyTiles[ i ] = board.m_Tiles[ x ][ y - i ];
			}
		}
		
		EventManager.get( ).queue( new Event_ShipAdd( board.m_Carrier ) );
		EventManager.get( ).queue( new Event_ShipAdd( board.m_Battleship ) );
		EventManager.get( ).queue( new Event_ShipAdd( board.m_Submarine ) );
		EventManager.get( ).queue( new Event_ShipAdd( board.m_Cruiser ) );
		EventManager.get( ).queue( new Event_ShipAdd( board.m_Destroyer ) );
	}
	
	private void specialPick( float x, float y )
	{
		if( ( y > 690 && y < 710 ) )
		{
			// Picking from top row
			
			if( ( x > 10 && x < 210 ) )
			{
				// Carrier 
				if( m_Selected != null && m_Selected != m_SetUpCarrier )
					m_Selected.toggleSelect( );
				
				m_SetUpCarrier.toggleSelect( );
				m_Selected = m_SetUpCarrier;
			}
			else if( x > 289 && x < 480 )
			{
				// Battleship
				if( m_Selected != null && m_Selected != m_SetUpBattleship )
					m_Selected.toggleSelect( );
				
				m_SetUpBattleship.toggleSelect( );
				m_Selected = m_SetUpBattleship;
			}
		}
		else if( y > 715 )
		{
			if( x > 10 && x < 130 )
			{
				// Submarine
				if( m_Selected != null && m_Selected != m_SetUpSubmarine )
					m_Selected.toggleSelect( );
				
				m_SetUpSubmarine.toggleSelect( );
				m_Selected = m_SetUpSubmarine;
			}
			else if( x > 150 && x < 300 )
			{
				// Destroyer
				if( m_Selected != null && m_Selected != m_SetUpDestroyer )
					m_Selected.toggleSelect( );
				
				m_SetUpDestroyer.toggleSelect( );
				m_Selected = m_SetUpDestroyer;
			}
			else if( x > 320 )
			{
				// Cruiser
				if( m_Selected != null && m_Selected != m_SetUpCruiser )
					m_Selected.toggleSelect( );
				
				m_SetUpCruiser.toggleSelect( );
				m_Selected = m_SetUpCruiser;
			}
		}
		else
		{
			// Check if board bounds
			float cellSize = 417 / 10.f;
			
			float xRel = x - 31;
			float yRel = y - 246;
			
			int cellX = ( int )( xRel / cellSize );
			int cellY = ( int )( yRel / cellSize );
			
			if( m_Selected == null )
				return;
			
			if( cellX < 0 || cellX > 9 || cellY < 0 || cellY > 9 )
			{
				// Rotate button?
				if( y > 150 && y < 220 )
				{
					if( x > 0 && x < 60 )
					{
						// Rotate pressed
						if( m_Selected != null )
						{
							if( m_Selected.isSet( ) == false )
								return;
							
							cellX = m_Selected.getCells( )[ 0 ];
							cellY = m_Selected.getCells( )[ 1 ];
							
							if( m_Selected.getID( ).compareToIgnoreCase( "carrier" ) == 0 )
							{
								if( cellX > 5 && m_Selected.isRotated( ) )
									return;
								else if( cellY < 4 && !m_Selected.isRotated( ) )
									return;
							}
							else if( m_Selected.getID( ).compareToIgnoreCase( "battleship" ) == 0 )
							{
								if( cellX > 6 && m_Selected.isRotated( ) )
									return;
								else if( cellY < 3 && !m_Selected.isRotated( ) )
									return;
							}
							else if( m_Selected.getID( ).compareToIgnoreCase( "destroyer" ) == 0 || m_Selected.getID( ).compareToIgnoreCase( "submarine" ) == 0 )
							{
								if( cellX > 7 && m_Selected.isRotated( ) )
									return;
								else if( cellY < 2 && !m_Selected.isRotated( ) )
									return;
							}
							else
							{
								if( cellX > 8 && m_Selected.isRotated( ) )
									return;
								else if( cellY < 1 && !m_Selected.isRotated( ) )
									return;
							}
							
							m_Selected.rotate( );
						}
					}
					else if( x > 330 )
					{
						// Play button
						if( m_SetUpCarrier.isSet( ) && m_SetUpBattleship.isSet( ) && m_SetUpDestroyer.isSet( ) && m_SetUpSubmarine.isSet( ) && m_SetUpCarrier.isSet( ) )
						{
							populateLogicBoard( );
							cleanUpSetUp( );
							m_State = SCENE_STATE.IDLE;
							EventManager.get( ).queue( new Event_TurnEnd( ) );
						}
					}
				}
				
				return;
			}
			
			if( m_Selected.getID( ).compareToIgnoreCase( "carrier" ) == 0 )
			{
				if( cellX > 5 && !m_Selected.isRotated( ) )
					return;
				else if( cellY < 4 && m_Selected.isRotated( ) )
					return;
			}
			else if( m_Selected.getID( ).compareToIgnoreCase( "battleship" ) == 0 )
			{
				if( cellX > 6 && !m_Selected.isRotated( ) )
					return;
				else if( cellY < 3 && m_Selected.isRotated( ) )
					return;
			}
			else if( m_Selected.getID( ).compareToIgnoreCase( "destroyer" ) == 0 || m_Selected.getID( ).compareToIgnoreCase( "submarine" ) == 0 )
			{
				if( cellX > 7 && !m_Selected.isRotated( ) )
					return;
				else if( cellY < 2 && m_Selected.isRotated( ) )
					return;
			}
			else
			{
				if( cellX > 8 && !m_Selected.isRotated( ) )
					return;
				else if( cellY < 1 && m_Selected.isRotated( ) )
					return;
			}
			
			if( m_Selected != null )
			{
				m_Selected.setPosition( ( m_Selected.isRotated( ) ? -80.f : -85.f ) + ( 5 * cellX ), -30.f + ( ( 10.f - cellY ) * 5.f ), 0.1f );
				m_Selected.set( );
				m_Selected.setCells( cellX, cellY );
			}
		}
	}

}
