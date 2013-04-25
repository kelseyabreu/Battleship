package com.ywdac.battleship.renderer;

import android.opengl.GLES11;

public class Camera 
	extends SceneNode
{
	public Camera( String id, RENDER_TYPE type ) 
	{
		super( id, type );
		// TODO Auto-generated constructor stub
	}

	@Override
	public void draw( ) 
	{
		// TODO Auto-generated method stub
		
	}
	
	public void lookRight( float delta )
	{
		m_Frame.rotateLocal( delta, 0.f, 1.f, 0.f );
	}
	
	public void lookUp( float delta )
	{
		m_Frame.rotateLocal( delta, 1.f, 0.f, 0.f );
	}
	
	public void lookAt( SceneNode node )
	{
		float pos[ ] = node.getPosition( );
		
		m_Frame.lookAt( pos[ 0 ], pos[ 1 ], pos[ 2 ] );
		
		float up[ ] = m_Frame.getUp( );
		
		up[ 0 ] = -up[ 0 ];
		up[ 1 ] = -up[ 1 ];
		up[ 2 ] = -up[ 2 ];
		
		m_Frame.setUp( up );
	}
	
	public void lookAt( float x, float y, float z )
	{
		m_Frame.lookAt( x, y, z );
		
		float up[ ] = m_Frame.getUp( );
		
		up[ 0 ] = -up[ 0 ];
		up[ 1 ] = -up[ 1 ];
		up[ 2 ] = -up[ 2 ];
		
		m_Frame.setUp( up );
	}
	
	public void push( boolean rotation_only )
	{
		GLES11.glPushMatrix( );
		GLES11.glMultMatrixf( m_Frame.getCameraMatrix( rotation_only ), 0 );
	}
	
	public void pop( )
	{
		GLES11.glPopMatrix( );
	}
	
}
