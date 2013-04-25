package com.ywdac.battleship.renderer;

import android.opengl.GLES11;

/**
 * Base class for all tile nodes. 
 * Might be retooled to be the only tile node.
 * 
 * I dont really know. Just need a node to
 * work with the BattleshipSceneTree for now.
 * 
 * @author ssell
 *
 */
public class TileNode 
	extends SceneNode
{
	private int m_X, m_Y;
	private String m_Board;
	
	private float m_RID;
	private float m_GID;
	private float m_BID;
	
	public TileNode( String id, RENDER_TYPE type, int x, int y, String board, float rID, float gID, float bID ) 
	{
		super( id, type );
		
		m_X = x;
		m_Y = y;
		m_Board = board;
		
		m_RID = rID;
		m_GID = gID;
		m_BID = bID;
		
		ModelPrimitiveQuad model = new ModelPrimitiveQuad( 5, 5 );
		model.setColor( 0.f, 0.f, 0.f, 0.f );
		
		m_Model = model;
		
		if( board == "player" )
		{
			this.moveRight( -85.f );
			this.moveUp( 20.f );
		}
		else
		{
			this.moveRight( 35.f );
			this.moveUp( 20.f );
		}
		
		this.moveRight( x * 5 );
		this.moveUp( -( y * 5 ) );
	}
	
	public void hit( boolean successful )
	{
		ModelPrimitiveQuad model = ( ModelPrimitiveQuad )m_Model;
		
		if( successful )
			model.setColor( 0.7f, 0.f, 0.f, 0.01f );
		else
			model.setColor( 0.7f, 0.7f, 0.7f, 0.01f );
		
		m_Model = model;
	}
	
	public void setColor( float r, float g, float b )
	{
		ModelPrimitiveQuad model = ( ModelPrimitiveQuad )m_Model;
		model.setColor( r, g, b, 0.01f );
		
		m_Model = model;
	}
	
	public int getX( )
	{
		return m_X;
	}
	
	public int getY( )
	{
		return m_Y;
	}
	
	public String getBoard( )
	{
		return m_Board;
	}
	
	public boolean isID( float r, float g, float b )
	{
		if( m_RID == r && m_GID == g && m_BID == b )
			return true;
		
		return false;
	}
	
	@Override
	public void draw( )
	{
		GLES11.glPushMatrix( );
		GLES11.glMultMatrixf( m_Frame.getMatrix( false ), 0 );
		
		m_Model.render( );
		
		GLES11.glPopMatrix( );
	}
}
