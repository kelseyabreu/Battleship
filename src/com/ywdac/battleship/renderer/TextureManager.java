package com.ywdac.battleship.renderer;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.ywdac.battleship.utilities.Logger;
import com.ywdac.battleship.utilities.Logger.CHANNEL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES11;
import android.opengl.GLUtils;

/**
 * Manages the textures in use by the application. Global singleton.
 * 
 * @author ssell
 *
 */
public class TextureManager 
{
	private static TextureManager m_Instance = null;
	private static Context m_Context;
	private static HashMap< Integer, Texture > m_Textures = new HashMap< Integer, Texture >( );
		
	//--------------------------------------------------------------------------------------
	
	private TextureManager( )
	{
		
	}
	
	public static TextureManager get( )
	{
		if( m_Instance == null )
			m_Instance = new TextureManager( );
		
		return m_Instance;
	}
	
	public void initialize( Context context )
	{
		m_Context = context;
	}
	
	/**
	 * Destroys all textures at program end.
	 */
	public void destroy( )
	{
		Iterator< Entry<Integer, Texture > > iter = m_Textures.entrySet( ).iterator( );
		
		while( iter.hasNext( ) )
		{
			deleteTexture( iter.next( ).getKey( ) );
		}
	}
	
	//--------------------------------------------------------------------------------------
	
	/**
	 * Loads the specified texture. Texture must be present in /res/drawable-mdpi and be a square bitmap.\n\n
	 * List of available textures can be found under R.drawable.*
	 * 
	 * @param drawable
	 * @return
	 */
	public boolean loadTexture( int drawable )
	{
		// Make sure we do not already have this texture.
		if( m_Textures.containsKey( drawable ) )
		{
			// We already have a texute in the hash table for this drawable.
			// See if it needs to be reloaded into memory. If not, exit with true.
			Texture t = m_Textures.get( drawable );
			
			if( t.m_GLID.get( 0 ) == 0 )
				reloadTexture( t );		// Texture is not in memory. Reload it.
			
			return true;
		}
		
		Texture texture = new Texture( );
		
		Bitmap bitmap = BitmapFactory.decodeResource( m_Context.getResources( ), drawable );
		
		GLES11.glGenTextures( 1, texture.m_GLID );
		GLES11.glBindTexture( GLES11.GL_TEXTURE_2D, texture.m_GLID.get( 0 ) );		// Make active texture for upcoming settings
		
		GLES11.glTexParameterf( GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_NEAREST );
		GLES11.glTexParameterf( GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_LINEAR );
		
		GLUtils.texImage2D( GLES11.GL_TEXTURE_2D, 0, bitmap, 0 );
		
		// clean up
		bitmap.recycle( );
		
		if( texture.m_GLID.get( 0 ) == 0 )
		{
			Logger.get( ).write( "Failed to load texture with ID [" + drawable + "]!", CHANNEL.ERROR_NON_CRITICAL );
			return false;
		}
		
		texture.m_ResourceID = drawable;
		m_Textures.put( drawable, texture );
		
		return true;
	}
	
	/**
	 * Unloads the specified texture from memory.\n
	 * If texture is requested via getTexture, it will be reloaded automatically.
	 * 
	 * @param id
	 */
	public void unloadTexture( int id )
	{
		//Texture texture = m_Textures.get( id );
		
		//if( texture == null || texture.m_GLID.get( 0 ) == 0 )
		//	return;
		
		//GLES11.glDeleteTextures( 1, texture.m_GLID );
	}
	
	/**
	 * Deletes a texture completely from the manager.
	 * 
	 * @param id
	 */
	public void deleteTexture( int id )
	{
		//Texture texture = m_Textures.get( id );
		
		//if( texture == null )
		//	return;
		
		//if( texture.m_GLID.get( 0 ) != 0 )
		//	unloadTexture( id );
		
		//m_Textures.remove( texture.m_ResourceID );
	}
	
	/**
	 * Searches for the texture with the associated ID. 
	 * If found, returns the OpenGL handle. Else returns 0.
	 * 
	 * @param id
	 * @return
	 */
	public int getTexture( int id )
	{
		Texture texture = m_Textures.get( id );
		
		if( texture == null ) 
			return 0;
		else
		{
			if( texture.m_GLID.get( 0 ) == 0 )
				reloadTexture( texture );
			
			return texture.m_GLID.get( 0 );
		}
	}
	
	/**
	 * Reloads a texture that has been unloaded previously.
	 * Same thing as loadTexture but does not create a new Texture object.
	 * 
	 * @param texture
	 */
	private void reloadTexture( Texture texture )
	{
		Bitmap bitmap = BitmapFactory.decodeResource( m_Context.getResources( ), texture.m_ResourceID );
		
		GLES11.glGenTextures( 1, texture.m_GLID );
		GLES11.glBindTexture( GLES11.GL_TEXTURE_2D, texture.m_GLID.get( 0 ) );		// Make active texture for upcoming settings
		
		GLES11.glTexParameterf( GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_NEAREST );
		GLES11.glTexParameterf( GLES11.GL_TEXTURE_2D, GLES11.GL_TEXTURE_MIN_FILTER, GLES11.GL_LINEAR );
		
		GLUtils.texImage2D( GLES11.GL_TEXTURE_2D, 0, bitmap, 0 );
		
		// clean up
		bitmap.recycle( );
	}

	public void deleteAllTextures( ) 
	{
		// TODO Auto-generated method stub
		
	}
}

class Texture
{
	public IntBuffer m_GLID = IntBuffer.allocate( 1 );
	public int m_ResourceID = 0;
}
