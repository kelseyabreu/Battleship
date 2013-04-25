package com.ywdac.battleship.renderer;

import java.nio.FloatBuffer;
import android.opengl.GLES11;

public class GridNode 
	extends SceneNode
{
	private FloatBuffer m_VertexBuffer;
	private float m_Color[ ] = { 1.f, 1.f, 1.f, 1.f };
	
	private ModelPrimitiveLine m_Rows[ ];
	private ModelPrimitiveLine m_Cols[ ];
	
	private float m_Width = 1.f;
	
	public GridNode( String id, RENDER_TYPE type ) 
	{
		super(id, type);
		
		m_Rows = new ModelPrimitiveLine[ 11 ];
		m_Cols = new ModelPrimitiveLine[ 11 ];
		
		for( int i = 0; i < 11; i++ )
		{
			m_Rows[ i ] = new ModelPrimitiveLine( -25.f, ( -25.f + ( i * 5 ) ), 0.f, 25.f, ( -25.f + ( i * 5 ) ), 0.f );
			m_Rows[ i ].setColor( m_Color );
			
			m_Cols[ i ] = new ModelPrimitiveLine( ( -25.f + ( i * 5 ) ), 25.f, 0.f, ( -25.f + ( i * 5 ) ), -25.f, 0.f );
			m_Cols[ i ].setColor( m_Color );
		}
	}

	@Override
	public void draw( ) 
	{
		GLES11.glPushMatrix( );
		GLES11.glMultMatrixf( m_Frame.getMatrix( false ), 0 );
		
		if( m_Width != 1.f )
			GLES11.glLineWidth( m_Width );
		
		for( int i = 0; i < 11; i++ )
		{
			m_Rows[ i ].render( );
			m_Cols[ i ].render( );
		}
		
		if( m_Width != 1.f )
			GLES11.glLineWidth( 1.f );
		
		GLES11.glPopMatrix( );
	}
	
	public void setWidth( float w )
	{
		m_Width = w;
	}
	
	public void setColor( float r, float g, float b, float a )
	{
		m_Color[ 0 ] = r;
		m_Color[ 1 ] = g;
		m_Color[ 2 ] = b;
		m_Color[ 3 ] = a;
	}
}
