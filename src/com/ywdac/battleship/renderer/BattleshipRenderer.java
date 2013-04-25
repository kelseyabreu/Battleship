package com.ywdac.battleship.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.ywdac.battleship.events.*;

import android.opengl.*;

//------------------------------------------------------------------------------------------

public class BattleshipRenderer
	implements GLSurfaceView.Renderer
{
	public static int m_Width = 0, m_Height = 0;
	
	//--------------------------------------------------------------------------------------
	
	public void onSurfaceCreated( GL10 gl, EGLConfig config )
	{
		gl.glEnable( GLES11.GL_TEXTURE_2D );
		gl.glClearColor( 0.f, 0.f, 0.f, 1.f );
		gl.glClearDepthf( 1.f );
		gl.glEnable( GLES11.GL_DEPTH_TEST );
		gl.glDepthFunc( GLES11.GL_LEQUAL );
		gl.glHint( GLES11.GL_PERSPECTIVE_CORRECTION_HINT, GLES11.GL_NICEST );
		gl.glEnable( GL10.GL_BLEND );
	    gl.glBlendFunc( GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA );		
	}
	
	public void update( )
	{
		EventManager.get( ).tick( 1000 );
		
		Scene scene = SceneManager.get( ).getActiveScene( );
		
		if( scene != null )
			scene.update( );
	}
	
	public void onDrawFrame( GL10 gl )
	{
		update( );
		
		GLES11.glClear( GLES11.GL_COLOR_BUFFER_BIT | GLES11.GL_DEPTH_BUFFER_BIT );
		
		Scene scene = SceneManager.get( ).getActiveScene( );
		
		if( scene != null )
			scene.draw( );
	}
	
	public void onSurfaceChanged( GL10 gl, int width, int height )
	{
		m_Width  = width;
		m_Height = height;
		
		gl.glViewport( 0, 0, width, height );
		
		gl.glMatrixMode( GLES11.GL_PROJECTION );
		gl.glLoadIdentity( );
		GLU.gluPerspective( gl, 45.f, ( float )width / ( float )height, 0.1f, 10000.f );
		
		gl.glMatrixMode( GLES11.GL_MODELVIEW );
		gl.glLoadIdentity( );
		
		DeviceInfo.setScreenWidth( width );
		DeviceInfo.setScreenHeight( height );
		DeviceInfo.setNear( 0.1f );
		DeviceInfo.setFar( 10000.f );
		DeviceInfo.setGL( gl );
	}
}
