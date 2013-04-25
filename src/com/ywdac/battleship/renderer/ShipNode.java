package com.ywdac.battleship.renderer;

import android.opengl.GLES11;

import com.ywdac.battleship.R;

/**
 * Base class for all ship nodes. 
 * Might be retooled to be the only ship node.
 * 
 * I dont really know. Just need a node to
 * work with the BattleshipSceneTree for now.
 * 
 * @author ssell
 *
 */
public class ShipNode 
	extends SceneNode
{
	public String m_Board;
	private boolean m_Sunk     = false;
	private boolean m_Selected = false;
	private boolean m_Rotated  = false;
	private boolean m_IsSet    = false;
	
	private int m_CellX = 0;
	private int m_CellY = 0;
	
	public ShipNode( String id, RENDER_TYPE type, TileNode occupies[ ], String board ) 
	{
		super( id, type );
		
		m_Board = board;
		
		// Last minute board setup code.........
		if( occupies == null )
		{
			playerSetup( id );
			return;
		}
		
		// Get width, height, and position
		
		float smallX = 999;
		float bigX   = -999;
		float smallY = 999;
		float bigY   = -999;
		
		int smallestX = 0;
		int smallestY = 0;
		
		for( int i = 0; i < occupies.length; i++ )
		{
			float tX = occupies[ i ].getPosition( )[ 0 ];
			float tY = occupies[ i ].getPosition( )[ 1 ];
			
			if( tX < smallX )
			{
				smallX = tX;
				smallestX = i;
			}
			
			if( tX > bigX )
				bigX   = tX;
			
			if( tY < smallY )
			{
				smallY = tY;
				smallestY = i;
			}
			
			if( tY > bigY )
				bigY = tY;
		}
		
		float width  = bigX - smallX + 5.f;
		float height = bigY - smallY + 5.f;
		
		if( width > height )
		{
			// Horizontal Ship
			m_Frame.setPosition( occupies[ smallestX ].getPosition( )[ 0 ], occupies[ smallestX ].getPosition( )[ 1 ], 0.1f );
		}
		else
		{
			// Vertical Ship
			m_Frame.setPosition( occupies[ smallestY ].getPosition( )[ 0 ], occupies[ smallestY ].getPosition( )[ 1 ], 0.1f );
			
		}
		
		ModelPrimitiveQuad model = new ModelPrimitiveQuad( ( int )( width > height ? width : height ), ( int )( width > height ? height : width ) );
		
		if( board.compareToIgnoreCase( "player" ) == 0 )
		{
			if( id.compareToIgnoreCase( "carrier" ) == 0 )
			{
				model.setTexture( R.drawable.player_carrier );
			}
			else if( id.compareToIgnoreCase( "battleship" ) == 0 )
			{
				model.setTexture( R.drawable.player_battleship );
			}
			else if( id.compareToIgnoreCase( "destroyer" ) == 0 )
			{
				model.setTexture( R.drawable.player_destroyer );
			}
			else if( id.compareToIgnoreCase( "submarine" ) == 0 )
			{
				model.setTexture( R.drawable.player_submarine );
			}
			else if( id.compareToIgnoreCase( "cruiser" ) == 0 )
			{
				model.setTexture( R.drawable.player_cruiser );
			}
		}
		else
		{
			model.setTexture( R.drawable.worst_saucer_ever );
			this.sink( );
		}
		
		if( width < height )
		{
			this.rotate( 270.f, 0.f, 0.f, 1.f );
			this.moveUp( -5.f );
		}
		
		m_Model = model;
	}
	
	public void draw( )
	{
		GLES11.glPushMatrix( );
		GLES11.glMultMatrixf( m_Frame.getMatrix( false ), 0 );
		
		if( m_Sunk )
		{
			GLES11.glColor4f( 0.3f, 0.3f, 0.3f, 0.5f );
		}
		else if( m_Selected )
		{
			GLES11.glColor4f( 0.3f, 0.8f, 0.3f, 0.5f );
		}
		
		m_Model.render( );
		
		if( m_Sunk || m_Selected )
		{
			GLES11.glColor4f( 1.f, 1.f, 1.f, 1.f );
		}
		
		GLES11.glPopMatrix( );
	}
	
	public void sink( )
	{
		m_Sunk = true;
	}
	
	private void playerSetup( String id )
	{
		ModelPrimitiveQuad model;
		
		if( id.compareToIgnoreCase( "carrier" ) == 0 )
		{
			model = new ModelPrimitiveQuad( 25, 5 );
			model.setTexture( R.drawable.player_carrier );
			m_Model = model;
			
			this.setPosition( -86.f, -32.f, 0.f );
		}
		else if( id.compareToIgnoreCase( "battleship" ) == 0 )
		{
			model = new ModelPrimitiveQuad( 20, 5 );
			model.setTexture( R.drawable.player_battleship );
			m_Model = model;
			
			this.setPosition( -55.f, -32.f, 0.f );
		}
		else if( id.compareToIgnoreCase( "destroyer" ) == 0 )
		{
			model = new ModelPrimitiveQuad( 15, 5 );
			model.setTexture( R.drawable.player_destroyer );
			m_Model = model;
			
			this.setPosition( -65.f, -40.f, 0.f );
		}
		else if( id.compareToIgnoreCase( "submarine" ) == 0 )
		{
			model = new ModelPrimitiveQuad( 15, 5 );
			model.setTexture( R.drawable.player_submarine );
			m_Model = model;
			
			this.setPosition( -85.f, -40.f, 0.f );
		}
		else if( id.compareToIgnoreCase( "cruiser" ) == 0 )
		{
			model = new ModelPrimitiveQuad( 10, 5 );
			model.setTexture( R.drawable.player_cruiser );
			m_Model = model;
			
			this.setPosition( -45.f, -40.f, 0.f );
		}
	}
	
	public void toggleSelect( )
	{
		if( m_Selected == false )
		{
			m_Selected = true;
		}
		else
		{
			m_Selected = false;
		}
	}
	
	public void rotate( )
	{
		if( m_Rotated )
		{
			this.moveUp( 5.f );
			this.rotate( -270.f, 0.f, 0.f, 1.f );
			m_Rotated = false;
		}
		else
		{
			this.rotate( 270.f, 0.f, 0.f, 1.f );
			this.moveUp( -5.f );
			m_Rotated = true;
		}
	}
	
	public boolean isRotated( )
	{
		return m_Rotated;
	}
	
	public void set( )
	{
		m_IsSet = true;
	}
	
	public boolean isSet( )
	{
		return m_IsSet;
	}
	
	public void setCells( int x, int y )
	{
		m_CellX = x;
		m_CellY = y;
	}
	
	public int[ ] getCells( )
	{
		int cells[ ] = { m_CellX, m_CellY };
		return cells;
	}
}
