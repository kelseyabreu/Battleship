package com.ywdac.battleship.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES11;

public class ModelPrimitiveLine 
	extends Model
{
	private FloatBuffer m_VertexBuffer;
	private float m_Color[ ] = { 1.f, 1.f, 1.f, 1.f };
	private float first[ ]  = new float[ 3 ];
	private float second[ ] = new float[ 3 ];
	private float m_Width = 1.f;
	
	public ModelPrimitiveLine( float xFirst, float yFirst, float zFirst, float xSecond, float ySecond, float zSecond )
	{
		first[ 0 ] = xFirst;
		first[ 1 ] = yFirst;
		first[ 2 ] = zFirst;
		
		second[ 0 ] = xSecond;
		second[ 1 ] = ySecond;
		second[ 2 ] = zSecond;
		
		calculateVertices( );
	}
	
	public void setColor( float r, float g, float b, float a )
	{
		m_Color[ 0 ] = r;
		m_Color[ 1 ] = g;
		m_Color[ 2 ] = b;
		m_Color[ 3 ] = a;
	}
	
	public void setColor( float color[ ] )
	{
		if( color.length != 4 )
			return;
		
		m_Color = color;
	}
	
	public void setWidth( float w )
	{
		m_Width = w;
	}
	
	private void calculateVertices( )
	{
		float tempVerts[ ] = new float[ 6 ];
		
		tempVerts[ 0 ] = first[ 0 ];
		tempVerts[ 1 ] = first[ 1 ];
		tempVerts[ 2 ] = first[ 2 ];
		tempVerts[ 3 ] = second[ 0 ];
		tempVerts[ 4 ] = second[ 1 ];
		tempVerts[ 5 ] = second[ 2 ];
		
		ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect( tempVerts.length * 4 );
		vertexByteBuffer.order( ByteOrder.nativeOrder( ) );
		
		m_VertexBuffer = vertexByteBuffer.asFloatBuffer( );
		m_VertexBuffer.put( tempVerts );
		m_VertexBuffer.position( 0 );
	}

	@Override
	public void render( ) 
	{
		if( m_Width > 0 && m_Width != 1.f )
			GLES11.glLineWidth( m_Width );
		
		GLES11.glEnableClientState( GLES11.GL_VERTEX_ARRAY );
		GLES11.glDisable( GLES11.GL_TEXTURE_2D );
		GLES11.glColor4f( m_Color[ 0 ], m_Color[ 1 ], m_Color[ 2 ], m_Color[ 3 ] );
		
		GLES11.glVertexPointer( 3, GLES11.GL_FLOAT, 0, m_VertexBuffer );
		GLES11.glDrawArrays( GLES11.GL_LINE_STRIP, 0, 2 );
		GLES11.glDisableClientState( GLES11.GL_VERTEX_ARRAY );
		
		GLES11.glEnable( GLES11.GL_TEXTURE_2D );
		GLES11.glColor4f( 1.f, 1.f, 1.f, 1.f );
		
		if( m_Width > 0 && m_Width != 1.f )
			GLES11.glLineWidth( 1.f );
	}
	
	
}
