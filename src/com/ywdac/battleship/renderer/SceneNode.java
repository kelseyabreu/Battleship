package com.ywdac.battleship.renderer;

import android.opengl.GLES11;

//------------------------------------------------------------------------------------------

/**
 * The marriage between an Orthonormal Frame and Model.
 * 
 * Orthonormal Frames store the position and orientation.
 * Models store the physical aspects to be rendered.
 * 
 * @author ssell
 *
 */
public class SceneNode 
{
	public static enum RENDER_TYPE
	{
		NONE,
		SOLID_COLOR,
		SOLID_TEXTURE,
		TRANSPARENT_COLOR,
		TRANSPARENT_TEXTURE,
		SKY_BOX
	}
	
	protected OrthonormalFrame m_Frame = new OrthonormalFrame( );
	protected Model            m_Model = null;
	protected String           m_ID    = null;
	protected RENDER_TYPE      m_Type  = RENDER_TYPE.NONE;
	
	//--------------------------------------------------------------------------------------
	
	/**
	 * 
	 * @param id
	 * @param type
	 */
	public SceneNode( String id, RENDER_TYPE type )
	{
		m_ID   = id;
		m_Type = type;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getID( )
	{
		return m_ID;
	}
	
	/**
	 * 
	 * @return
	 */
	public RENDER_TYPE getRenderType( )
	{
		return m_Type;
	}
	
	/**
	 * 
	 * @param type
	 */
	public void setRenderType( RENDER_TYPE type )
	{
		m_Type = type;
	}
	
	public void attachModel( Model model )
	{
		m_Model = model;
	}
	
	/**
	 * 
	 * @return
	 */
	public Model getModel( )
	{
		return m_Model;
	}
	
	/**
	 * 
	 */
	public void draw( )
	{
		GLES11.glPushMatrix( );
		GLES11.glMultMatrixf( m_Frame.getMatrix( false ), 0 );
		
		m_Model.render( );
		
		GLES11.glPopMatrix( );
	}
	
	/**
	 * Translate (move) the node by the specified vector (x,y,z).
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void translate( float x, float y, float z )
	{
		m_Frame.moveForward( -z );	// -z since moving forward is along the negative z-axis, and is opposite of what will be intended by translate call.
		m_Frame.moveUp( y );
		m_Frame.moveRight( x );
	}
	
	/**
	 * Translate (move) the node by the specified vector (x,y,z).
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void translate( float[ ] trans )
	{
		if( trans.length != 3 )
			return;
		
		m_Frame.translateInLocal( trans[ 0 ], trans[ 1 ], trans[ 2 ] );
	}
	
	/**
	 * 
	 * @param delta
	 */
	public void moveForward( float delta )
	{
		m_Frame.moveForward( delta );
	}
	
	/**
	 * 
	 * @param delta
	 */
	public void moveRight( float delta )
	{
		m_Frame.moveRight( delta );
	}
	
	/**
	 * 
	 * @param delta
	 */
	public void moveUp( float delta )
	{
		m_Frame.moveUp( delta );
	}
	
	/**
	 * Rotate the node by an angle in degrees around the axis (x,y,z).
	 * 
	 * @param angle
	 * @param x
	 * @param y
	 * @param z
	 */
	public void rotate( float angle, float x, float y, float z )
	{
		m_Frame.rotateLocal( angle, x, y, z );
	}
	
	/**
	 * Scale the node along the axes.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void scale( float x, float y, float z )
	{
	
	}
	
	public float[ ] getPosition( )
	{
		return m_Frame.getPosition( );
	}

	public void setPosition( float x, float y, float z )
	{
		m_Frame.setPosition( x, y, -z );
	}
}
