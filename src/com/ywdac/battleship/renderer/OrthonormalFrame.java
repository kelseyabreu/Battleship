package com.ywdac.battleship.renderer;

public class OrthonormalFrame 
{
	private float m_Position[ ] = new float[ 3 ];
	private float m_Forward[ ] = new float[ 3 ];
	private float m_Upward[ ] = new float[ 3 ];
	
	public OrthonormalFrame( )
	{
		m_Position[ 0 ] = 0.f;
		m_Position[ 1 ] = 0.f;
		m_Position[ 2 ] = 0.f;
		
		m_Forward[ 0 ] = 0.f;
		m_Forward[ 1 ] = 0.f;
		m_Forward[ 2 ] = 1.f;
		
		m_Upward[ 0 ] = 0.f;
		m_Upward[ 1 ] = 1.f;
		m_Upward[ 2 ] = 0.f;
	}
	
	/**
	 * Returns the matrix version of this orthonormal frame.
	 * 
	 * @param rotation_only true if using on skybox, false generally otherwise.
	 * @return
	 */
	public float[ ] getMatrix( boolean rotation_only )
	{
		float matrix[ ]  = new float[ 16 ];
		float xVector[ ] = new float[ 3 ];
		
		xVector = MathOps.cross( m_Upward, m_Forward );
		
		matrix = MathOps.getIdentity_4x4( );
		matrix = MathOps.setMatrixColumn_4x4( matrix, MathOps.MATRIX_ATTR.X_AXIS, xVector[ 0 ], xVector[ 1 ], xVector[ 2 ] );
		matrix = MathOps.setMatrixColumn_4x4( matrix, MathOps.MATRIX_ATTR.Y_AXIS, m_Upward[ 0 ], m_Upward[ 1 ], m_Upward[ 2 ] );
		matrix = MathOps.setMatrixColumn_4x4( matrix, MathOps.MATRIX_ATTR.Z_AXIS, m_Forward[ 0 ], m_Forward[ 1 ], m_Forward[ 2 ] );
		
		if( rotation_only )
			return matrix;
		
		matrix = MathOps.setMatrixColumn_4x4( matrix, MathOps.MATRIX_ATTR.POSITION, m_Position[ 0 ], m_Position[ 1 ], m_Position[ 2 ] );
		
		return matrix;
	}
	
	public float[ ] getCameraMatrix( boolean rotation_only )
	{
		float matrix[ ]  = new float[ 16 ];
		float xVector[ ] = new float[ 3 ];
		float zVector[ ] = new float[ 3 ];
		
		zVector[ 0 ] = -m_Forward[ 0 ];
		zVector[ 1 ] = -m_Forward[ 1 ];
		zVector[ 2 ] = -m_Forward[ 2 ];
		
		xVector = MathOps.cross( zVector, m_Upward  );
		
		matrix = MathOps.getIdentity_4x4( );
		
		//--------------------------------------------
		
		matrix[ 0 ] = xVector[ 0 ];
		matrix[ 1 ] = xVector[ 1 ];
		matrix[ 2 ] = xVector[ 2 ];
		
		matrix[ 4 ] = m_Upward[ 0 ];
		matrix[ 5 ] = m_Upward[ 1 ];
		matrix[ 6 ] = m_Upward[ 2 ];
		
		matrix[ 8 ]  = zVector[ 0 ];
		matrix[ 9 ]  = zVector[ 1 ];
		matrix[ 10 ] = zVector[ 2 ];
		
		if( rotation_only )
			return matrix;
		
		float transMatrix[ ] = new float[ 16 ];
		transMatrix = MathOps.createTranslateMatrix_4x4( -m_Position[ 0 ], -m_Position[ 1 ], -m_Position[ 2 ] );
		
		matrix = MathOps.multiplyMatrix_4x4( matrix, transMatrix );
		
		return matrix;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setPosition( float x, float y, float z )
	{
		m_Position[ 0 ] = x;
		m_Position[ 1 ] = y;
		m_Position[ 2 ] = z;
	}
	
	/**
	 * Sets the x-component of the position vector.
	 * 
	 * @param x
	 */
	public void setX( float x )
	{
		m_Position[ 0 ] = x;
	}
	
	/**
	 * Sets the y-component of the position vector.
	 * 
	 * @param y
	 */
	public void setY( float y )
	{
		m_Position[ 1 ] = y;
	}
	
	/**
	 * Sets the z-component of the position vector.
	 * 
	 * @param z
	 */
	public void setZ( float z )
	{
		m_Position[ 2 ] = z;
	}
	
	/**
	 * Returns the position vector.
	 * 
	 * @return
	 */
	public float[ ] getPosition( )
	{
		return m_Position;
	}
	
	/**
	 * Returns the x-component of the position vector.
	 * 
	 * @return
	 */
	public float getX( )
	{
		return m_Position[ 0 ];
	}
	
	/**
	 * Returns the y-component of the position vector.
	 * 
	 * @return
	 */
	public float getY( )
	{
		return m_Position[ 1 ];
	}
	
	/**
	 * Returns the z-component of the position vector.
	 * 
	 * @return
	 */
	public float getZ( )
	{
		return m_Position[ 2 ];
	}
	
	/**
	 * Sets the forward vector (z-axis)
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setForward( float x, float y, float z )
	{
		m_Forward[ 0 ] = x;
		m_Forward[ 1 ] = y;
		m_Forward[ 2 ] = z;
	}
	
	/**
	 * Sets the forward vector (z-axis)
	 * 
	 * @param vec
	 */
	public void setForward( float vec[ ] )
	{
		if( vec.length != 3 )
			return;
		
		m_Forward = vec;
	}
	
	/**
	 * Returns the forward vector (z-axis)
	 * 
	 * @return
	 */
	public float[ ] getForward( )
	{
		return m_Forward;
	}
	
	/**
	 * Sets the upward vector (y-axis)
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setUp( float x, float y, float z )
	{
		m_Upward[ 0 ] = x;
		m_Upward[ 1 ] = y;
		m_Upward[ 2 ] = z;
	}
	
	/**
	 * Sets the upward vector (y-axis)
	 * 
	 * @param vec
	 */
	public void setUp( float vec[ ] )
	{
		if( vec.length != 3 )
			return;
		
		m_Upward = vec;
	}
	
	/**
	 * Gets the upward vector (y-axis)
	 * @return
	 */
	public float[ ] getUp( )
	{
		return m_Upward;
	}
	
	/**
	 * 
	 * @return
	 */
	public float[ ] getXAxis( )
	{
		return MathOps.cross( m_Upward, m_Forward );
	}

	/**
	 * 
	 * @return
	 */
	public float[ ] getYAxis( )
	{
		return m_Upward;
	}

	/**
	 * 
	 * @return
	 */
	public float[ ] getZAxis( )
	{
		return m_Forward;
	}
	
	/**
	 * Translates the frame in world coordinates.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void translateInWorld( float x, float y, float z ) 
	{
		m_Position[ 0 ] += x;
		m_Position[ 1 ] += y;
		m_Position[ 2 ] += z;
	}
	
	/**
	 * Translates the frame in local coordinates.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public void translateInLocal( float x, float y, float z )
	{
		moveRight( x );
		moveUp( y );
		moveForward( z );
	}
	
	/**
	 * Moves the frame 'forward' from the local perspective.<br>
	 * AKA along the local Z-axis.
	 * 
	 * @param delta
	 */
	public void moveForward( float delta )
	{
		m_Position[ 0 ] += m_Forward[ 0 ] * delta;
		m_Position[ 1 ] += m_Forward[ 1 ] * delta;
		m_Position[ 2 ] += m_Forward[ 2 ] * delta;
	}
	
	/**
	 * Moves the frame to the 'right' from the local perspective.<br>
	 * AKA along the local X-axis.
	 * 
	 * @param delta
	 */
	public void moveRight( float delta )
	{
		float right[ ] = new float[ 3 ];
		right = MathOps.cross( m_Upward, m_Forward );
		
		m_Position[ 0 ] += right[ 0 ] * delta;
		m_Position[ 1 ] += right[ 1 ] * delta;
		m_Position[ 2 ] += right[ 2 ] * delta;
	}
	
	/**
	 * Moves the frame 'upward' from the local perspective.<br>
	 * AKA along the local Y-axis.
	 * 
	 * @param delta
	 */
	public void moveUp( float delta )
	{
		m_Position[ 0 ] += m_Upward[ 0 ] * delta;
		m_Position[ 1 ] += m_Upward[ 1 ] * delta;
		m_Position[ 2 ] += m_Upward[ 2 ] * delta;
	}
	
	/**
	 * Rotates the frame around the world axes by the given angle.
	 * 
	 * @param angle The angle to rotate by in DEGREES.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void rotateWorld( float angle, float x, float y, float z )
	{
		float rot[ ] = MathOps.createRotationMatrix_4x4( angle, x, y, z );
		float vec[ ] = new float[ 3 ];
		
		// Transform the y-axis
		vec[ 0 ] = rot[ 0 ] * m_Upward[ 0 ] + rot[ 4 ] * m_Upward[ 1 ] + rot[ 8 ]  * m_Upward[ 2 ];
		vec[ 1 ] = rot[ 1 ] * m_Upward[ 0 ] + rot[ 5 ] * m_Upward[ 1 ] + rot[ 9 ]  * m_Upward[ 2 ];
		vec[ 2 ] = rot[ 2 ] * m_Upward[ 0 ] + rot[ 6 ] * m_Upward[ 1 ] + rot[ 10 ] * m_Upward[ 2 ];
		
		m_Upward[ 0 ] = vec[ 0 ];
		m_Upward[ 1 ] = vec[ 1 ];
		m_Upward[ 2 ] = vec[ 2 ];
		
		// Transform the z-axis
		vec[ 0 ] = rot[ 0 ] * m_Forward[ 0 ] + rot[ 4 ] * m_Forward[ 1 ] + rot[ 8 ]  * m_Forward[ 2 ];
		vec[ 1 ] = rot[ 1 ] * m_Forward[ 0 ] + rot[ 5 ] * m_Forward[ 1 ] + rot[ 9 ]  * m_Forward[ 2 ];
		vec[ 2 ] = rot[ 2 ] * m_Forward[ 0 ] + rot[ 6 ] * m_Forward[ 1 ] + rot[ 10 ] * m_Forward[ 2 ];
		
		m_Forward[ 0 ] = vec[ 0 ];
		m_Forward[ 1 ] = vec[ 1 ];
		m_Forward[ 2 ] = vec[ 2 ];
	}
	
	/**
	 * Rotates the frame around the local axes by the given angle.
	 * 
	 * @param angle The angle to rotate by in DEGREES.
	 * @param x
	 * @param y
	 * @param z
	 */
	public void rotateLocal( float angle, float x, float y, float z )
	{
		float l2w[ ] = localToWorld( x, y, z, true );
		
		rotateWorld( angle, l2w[ 0 ], l2w[ 1 ], l2w[ 2 ] );
	}
	
	/**
	 * Convert local coordinate system to world.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param rotation_only
	 * @return
	 */
	public float[ ] localToWorld( float x, float y, float z, boolean rotation_only )
	{
		float rotMat[ ] = getMatrix( true );
		float world[ ]  = new float[ 3 ];
		
		world[ 0 ] = rotMat[ 0 ] * x + rotMat[ 4 ] * y + rotMat[ 8 ]  * z;
		world[ 1 ] = rotMat[ 1 ] * x + rotMat[ 5 ] * y + rotMat[ 9 ]  * z;
		world[ 2 ] = rotMat[ 2 ] * x + rotMat[ 6 ] * y + rotMat[ 10 ] * z;
		
		if( !rotation_only )
		{
			world[ 0 ] += m_Position[ 0 ];
			world[ 1 ] += m_Position[ 1 ];
			world[ 2 ] += m_Position[ 2 ];
		}
		
		return world;
	}
	
	/**
	 * Convert world coordinate system to local.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public float[ ] worldToLocal( float x, float y, float z )
	{
		float new_world[ ] = new float[ 3 ];
		float local[ ]     = new float[ 3 ];
		
		new_world[ 0 ] = x - m_Position[ 0 ];
		new_world[ 1 ] = y - m_Position[ 1 ];
		new_world[ 2 ] = z - m_Position[ 2 ];
		
		float rotMat[ ] = getMatrix( true );
		float invMat[ ] = MathOps.invertMatrix_4x4f( rotMat );
		
		local[ 0 ] = invMat[ 0 ] * new_world[ 0 ] + invMat[ 4 ] * new_world[ 1 ] + invMat[ 8 ]  * new_world[ 2 ];
		local[ 1 ] = invMat[ 1 ] * new_world[ 0 ] + invMat[ 5 ] * new_world[ 1 ] + invMat[ 9 ]  * new_world[ 2 ];
		local[ 2 ] = invMat[ 2 ] * new_world[ 0 ] + invMat[ 6 ] * new_world[ 1 ] + invMat[ 10 ] * new_world[ 2 ];
		
		return local;
	}
	
	/**
	 * Transforms a point by the frame matrix.
	 * 
	 * @param point_orig
	 * @return
	 */
	public float[ ] transformPoint( float point_orig[ ] )
	{
		if( point_orig.length != 3 )
			return point_orig;
		
		float point[ ] = new float[ 3 ];
		MathOps.copyMatrix( point_orig, point );
		
		float matrix[ ] = getMatrix( false );
		
		point[ 0 ] = matrix[ 0 ] * point_orig[ 0 ] + matrix[ 4 ] * point_orig[ 1 ] + matrix[ 8 ]  * point_orig[ 2 ] + matrix[ 12 ];
		point[ 1 ] = matrix[ 1 ] * point_orig[ 0 ] + matrix[ 5 ] * point_orig[ 1 ] + matrix[ 9 ]  * point_orig[ 2 ] + matrix[ 13 ];
		point[ 2 ] = matrix[ 2 ] * point_orig[ 0 ] + matrix[ 6 ] * point_orig[ 1 ] + matrix[ 10 ] * point_orig[ 2 ] + matrix[ 14 ];
		
		return point;
		
	}
	
	/**
	 * Rotates a vector by the frame matrix.
	 * 
	 * @param vector_orig
	 * @return
	 */
	public float[ ] rotateVector( float vector_orig[ ] )
	{
		if( vector_orig.length != 3 )
			return vector_orig;
		
		float vector[ ] = new float[ 3 ];
		MathOps.copyMatrix( vector_orig, vector );
		
		float matrix[ ] = getMatrix( true );
		
		vector[ 0 ] = matrix[ 0 ] * vector_orig[ 0 ] + matrix[ 4 ] * vector_orig[ 1 ] + matrix[ 8 ]  * vector_orig[ 2 ];
		vector[ 1 ] = matrix[ 1 ] * vector_orig[ 0 ] + matrix[ 5 ] * vector_orig[ 1 ] + matrix[ 9 ]  * vector_orig[ 2 ];
		vector[ 2 ] = matrix[ 2 ] * vector_orig[ 0 ] + matrix[ 6 ] * vector_orig[ 1 ] + matrix[ 10 ] * vector_orig[ 2 ];
		
		return vector;
	}
	
	public void lookAt( float x, float y, float z )
	{
		float direction[ ] = new float[ 3 ];

		direction[ 0 ] = x - m_Position[ 0 ];
		direction[ 1 ] = y - m_Position[ 1 ];
		direction[ 2 ] = z - m_Position[ 2 ];
		
		direction = MathOps.normalize_3f( direction );
		
		m_Upward[ 0 ] = 0.f;
		m_Upward[ 1 ] = 1.f;
		m_Upward[ 2 ] = 0.f;
		
		float right[ ] = MathOps.cross( direction, m_Upward );
		right = MathOps.normalize_3f( right );
		
		m_Upward  = MathOps.cross( direction, right );
		m_Forward = direction;
	}
	
}
