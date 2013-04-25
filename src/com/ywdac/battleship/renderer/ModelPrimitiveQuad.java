package com.ywdac.battleship.renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.ywdac.battleship.utilities.Logger;
import com.ywdac.battleship.utilities.Logger.CHANNEL;

import android.opengl.GLES11;

/**
 * Basic quad. Should not be used aside from debugging and initial testing.
 * 
 * @author ssell
 *
 */
public class ModelPrimitiveQuad 
	extends Model
{
	private FloatBuffer m_VertexBuffer;
	private FloatBuffer m_TextureBuffer;
	private int m_Width  = 0;
	private int m_Height = 0;
	
	private float m_Color[ ] = { 1.f, 1.f, 1.f, 1.f };
	private int m_TextureID = 0;
	private int m_GLTexture = 0;
	
	public ModelPrimitiveQuad( int width, int height )
	{
		m_Width  = width;
		m_Height = height;
		
		calculateVertices( );
		calculateTextureCoordinates( );
	}
	
	public void setWidth( int width )
	{
		m_Width = width;
		calculateVertices( );
	}
	
	public void setHeight( int height )
	{
		m_Height = height;
		calculateVertices( );
	}
	
	public void setDimensions( int width, int height )
	{
		m_Width  = width;
		m_Height = height;
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
	
	public void setTexture( int id )
	{
		m_TextureID = id;
		
		m_TextureIDs = new int[ 1 ];
		m_GLIDs      = new int[ 1 ];
		
		m_TextureIDs[ 0 ] = m_TextureID;
		m_GLIDs[ 0 ]      = m_GLTexture;
	}
	
	public int getWidth( )
	{
		return m_Width;
	}
	
	public int getHeight( )
	{
		return m_Height;
	}
	
	public float[ ] getColor( )
	{
		return m_Color;
	}
	
	public int getTexture( )
	{
		return m_TextureID;
	}
	
	@Override
	public void render( ) 
	{
		GLES11.glEnableClientState( GLES11.GL_VERTEX_ARRAY );

		if( m_TextureID != 0 )
		{
			if( m_GLTexture == 0 )
			{
				m_GLTexture = TextureManager.get( ).getTexture( m_TextureID );
				m_GLIDs[ 0 ] = m_GLTexture;
				
				if( m_GLTexture == 0 )
					Logger.get( ).write( "Missing texture [" + m_TextureID + "] on ModelPrimitiveQuad", CHANNEL.ERROR_NON_CRITICAL );
				
			}
			
			GLES11.glEnableClientState( GLES11.GL_TEXTURE_COORD_ARRAY );
			GLES11.glBindTexture( GLES11.GL_TEXTURE_2D, m_GLTexture );
			GLES11.glTexCoordPointer( 2, GLES11.GL_FLOAT, 0, m_TextureBuffer );
		}
		else
		{	
			// Typically Models will be displayed as textured and not as solid colors.
			// Solid color models will only exist due to testing and/or errors and so
			// the natural behavior of this OpenGL implementation will be to enable texturing.
			// But you can not have solid color while texturing is enabled, so temporarily
			// switch states. This will eventually be handled by the future SceneTree.
			
			GLES11.glDisable( GLES11.GL_TEXTURE_2D );
			GLES11.glColor4f( m_Color[ 0 ], m_Color[ 1 ], m_Color[ 2 ], m_Color[ 3 ] );
		}
		
		GLES11.glVertexPointer( 3, GLES11.GL_FLOAT, 0, m_VertexBuffer );
		GLES11.glDrawArrays( GLES11.GL_TRIANGLE_STRIP, 0, 4 );
		GLES11.glDisableClientState( GLES11.GL_VERTEX_ARRAY );
		
		if( m_TextureID != 0 )
			GLES11.glDisableClientState( GLES11.GL_TEXTURE_COORD_ARRAY );
		else
		{
			GLES11.glEnable( GLES11.GL_TEXTURE_2D );
			GLES11.glColor4f( 1.f, 1.f, 1.f, 1.f );
		}
		
	}
	
	private void calculateVertices( )
	{
		float tempVerts[ ] = new float[ 12 ];
		
		tempVerts[ 0 ] = 0.f; 
		tempVerts[ 1 ] = 0.f; 
		tempVerts[ 2 ] = 0.f;
		
		tempVerts[ 3 ] = 0.f; 
		tempVerts[ 4 ] = m_Height; 
		tempVerts[ 5 ] = 0.f;
		
		tempVerts[ 6 ] = m_Width;
		tempVerts[ 7 ] = 0.f;
		tempVerts[ 8 ] = 0.f;
		
		tempVerts[ 9 ]  = m_Width;
		tempVerts[ 10 ] = m_Height;
		tempVerts[ 11 ] = 0.f;
		
		// float has 4 bytes, so allocate enough for each vertex component
		ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect( tempVerts.length * 4 );
		vertexByteBuffer.order( ByteOrder.nativeOrder( ) );
		
		m_VertexBuffer = vertexByteBuffer.asFloatBuffer( );
		m_VertexBuffer.put( tempVerts );
		m_VertexBuffer.position( 0 );
	}
	
	private void calculateTextureCoordinates( )
	{
		float coords[ ] = {
				0.f, 1.f,
				0.f, 0.f,
				1.f, 1.f,
				1.f, 0.f };
		
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect( coords.length * 4 );
		byteBuffer.order( ByteOrder.nativeOrder( ) );
		
		m_TextureBuffer = byteBuffer.asFloatBuffer( );
		m_TextureBuffer.put( coords );
		m_TextureBuffer.position( 0 );
	}
}
