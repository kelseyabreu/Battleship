package com.ywdac.battleship.renderer;

import android.opengl.GLES11;

public class SkyboxNode 
	extends SceneNode
{
	public SkyboxNode( String id, RENDER_TYPE type, int texture ) 
	{
		super( id, type );
		
		ModelPrimitiveQuad model = new ModelPrimitiveQuad( 200, 100 );
		model.setTexture( texture );
		
		m_Model = model;
		
		translate( -100.f, -50.f, -2.f );
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
