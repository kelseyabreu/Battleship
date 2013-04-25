package com.ywdac.battleship.renderer;

/**
 * Generic commonly-used mathematical operations not offered by Android and/or OpenGL
 * 
 * @author ssell
 */
public class MathOps 
{
	//--------------------------------------------------------------------------------------
	// Generic Ops and Variables
	
	public static float PI = 3.141592f;
	
	public static float degreesToRadians( float deg )
	{
		return deg * ( PI / 180.f );
	}
	
	public static float radiansToDegrees( float rad )
	{
		return rad * ( 180.f / PI );
	}
	
	//--------------------------------------------------------------------------------------
	// Matrix Ops
	
	enum MATRIX_ATTR
	{
		X_AXIS,
		Y_AXIS,
		Z_AXIS,
		POSITION
	}
	
	/**
	 * Copies matrix a into matrix b.
	 * 
	 * @param a
	 * @param b
	 */
	public static void copyMatrix( float[ ] a, float[ ] b )
	{
		if( a.length != b.length )
			return;
		
		for( int i = 0; i < a.length; i++ )
			b[ i ] = a[ i ];
	}
	
	/**
	 * Extracts the specified attribute from the provided 3x3, column-major, matrix.
	 * 
	 * @param matrix
	 * @param attr
	 * @return A 3-component array
	 */
	public static float[ ] extractFromMatrix_3x3( float matrix_orig[ ], MATRIX_ATTR attr )
	{
		if( matrix_orig.length != 9 )
			return matrix_orig;
		
		float matrix[ ] = new float[ 9 ];
		copyMatrix( matrix_orig, matrix );
		
		float vec[ ]    = new float[ 3 ];
		
		switch( attr )
		{
		case X_AXIS:
			vec[ 0 ] = matrix[ 0 ];
			vec[ 1 ] = matrix[ 1 ];
			vec[ 2 ] = matrix[ 2 ];
			break;
		
		case Y_AXIS:
			vec[ 0 ] = matrix[ 3 ];
			vec[ 1 ] = matrix[ 4 ];
			vec[ 2 ] = matrix[ 5 ];
			break;
			
		case Z_AXIS:
			vec[ 0 ] = matrix[ 6 ];
			vec[ 1 ] = matrix[ 7 ];
			vec[ 2 ] = matrix[ 8 ];
			break;
			
		case POSITION:
		default:
			vec[ 0 ] = 0.f;
			vec[ 1 ] = 0.f;
			vec[ 2 ] = 0.f;
			break;
		}
		
		return vec;
	}
	
	/**
	 * Extracts the specified attribute from the provided 4x4, column-major, matrix.
	 * 
	 * @param matrix
	 * @param attr
	 * @return A 3-component array
	 */
	public static float[ ] extractFromMatrix_4x4( float matrix_orig[ ], MATRIX_ATTR attr )
	{
		if( matrix_orig.length != 16 )
			return matrix_orig;
		
		float matrix[ ] = new float[ 16 ];
		copyMatrix( matrix_orig, matrix );
		
		float vec[ ] = new float[ 3 ];
		
		switch( attr )
		{
		case X_AXIS:
			vec[ 0 ] = matrix[ 0 ];
			vec[ 1 ] = matrix[ 1 ];
			vec[ 2 ] = matrix[ 2 ];
			break;
		
		case Y_AXIS:
			vec[ 0 ] = matrix[ 4 ];
			vec[ 1 ] = matrix[ 5 ];
			vec[ 2 ] = matrix[ 6 ];
			break;
			
		case Z_AXIS:
			vec[ 0 ] = matrix[ 8 ];
			vec[ 1 ] = matrix[ 9 ];
			vec[ 2 ] = matrix[ 10 ];
			break;
			
		case POSITION:
			vec[ 0 ] = matrix[ 12 ];
			vec[ 1 ] = matrix[ 13 ];
			vec[ 2 ] = matrix[ 14 ];
			break;
			
		default:
			vec[ 0 ] = 0.f;
			vec[ 1 ] = 0.f;
			vec[ 2 ] = 0.f;
			break;
		}
		
		return vec;
	}
	
	/**
	 * Sets the specified attribute in the provided 3x3, column-major, matrix.
	 * 
	 * @param matrix
	 * @param attr
	 * @param x
	 * @param y
	 * @param z
	 * @return The modified matrix.
	 */
	public static float[ ] setMatrixColumn_3x3( float matrix_orig[ ], MATRIX_ATTR attr, float x, float y, float z )
	{
		if( matrix_orig.length != 9 )
			return matrix_orig;
		
		float matrix[ ] = new float[ 9 ];
		copyMatrix( matrix_orig, matrix );
		
		switch( attr )
		{
		case X_AXIS:
			matrix[ 0 ] = x;
			matrix[ 1 ] = y;
			matrix[ 2 ] = z;
			break;
			
		case Y_AXIS:
			matrix[ 3 ] = x;
			matrix[ 4 ] = y;
			matrix[ 5 ] = z;
			break;
			
		case Z_AXIS:
			matrix[ 6 ] = x;
			matrix[ 7 ] = y;
			matrix[ 8 ] = z;
			break;
			
		case POSITION:
		default:
			break;
		}
		
		return matrix;
	}
	
	/**
	 * Sets the specified attribute in the provided 4x4, column-major, matrix.
	 * 
	 * @param matrix
	 * @param attr
	 * @param x
	 * @param y
	 * @param z 
	 * @param w
	 * @return The modified matrix.
	 */
	public static float[ ] setMatrixColumn_4x4( float matrix_orig[ ], MATRIX_ATTR attr, float x, float y, float z, float w )
	{
		if( matrix_orig.length != 16 )
			return matrix_orig;
		
		float matrix[ ] = new float[ 16 ];
		copyMatrix( matrix_orig, matrix );
		
		switch( attr )
		{
		case X_AXIS:
			matrix[ 0 ] = x;
			matrix[ 1 ] = y;
			matrix[ 2 ] = z;
			matrix[ 3 ] = w;
			break;
			
		case Y_AXIS:
			matrix[ 4 ] = x;
			matrix[ 5 ] = y;
			matrix[ 6 ] = z;
			matrix[ 7 ] = w;
			break;
			
		case Z_AXIS:
			matrix[ 8 ]  = x;
			matrix[ 9 ]  = y;
			matrix[ 10 ] = z;
			matrix[ 11 ] = w;
			break;
			
		case POSITION:
			matrix[ 12 ] = x;
			matrix[ 13 ] = y;
			matrix[ 14 ] = z;
			matrix[ 15 ] = w;
			break;
			
		default:
			break;
		}
		
		return matrix;
	}
	
	/**
	 * Sets the specified attribute in the provided 4x4, column-major, matrix.<br>
	 * This version excludes the rarely used 'w' parameter.
	 * 
	 * @param matrix
	 * @param attr
	 * @param x
	 * @param y
	 * @param z 
	 * @return The modified matrix.
	 */
	public static float[ ] setMatrixColumn_4x4( float matrix_orig[ ], MATRIX_ATTR attr, float x, float y, float z )
	{
		if( matrix_orig.length != 16 )
			return matrix_orig;
		
		float matrix[ ] = new float[ 16 ];
		copyMatrix( matrix_orig, matrix );
		
		switch( attr )
		{
		case X_AXIS:
			matrix[ 0 ] = x;
			matrix[ 1 ] = y;
			matrix[ 2 ] = z;
			break;
			
		case Y_AXIS:
			matrix[ 4 ] = x;
			matrix[ 5 ] = y;
			matrix[ 6 ] = z;
			break;
			
		case Z_AXIS:
			matrix[ 8 ]  = x;
			matrix[ 9 ]  = y;
			matrix[ 10 ] = z;
			break;
			
		case POSITION:
			matrix[ 12 ] = x;
			matrix[ 13 ] = y;
			matrix[ 14 ] = z;
			break;
			
		default:
			break;
		}
		
		return matrix;
	}
	
	/**
	 * Loads the identity matrix into the provided 3x3, column-major, matrix.
	 * 
	 * @param matrix
	 * @return The modified matrix.
	 */
	public static float[ ] getIdentity_3x3( )
	{
		float matrix[ ] = new float[ 9 ];
		
		matrix[ 0 ] = 1.f;
		matrix[ 1 ] = 0.f;
		matrix[ 2 ] = 0.f;
		
		matrix[ 3 ] = 0.f;
		matrix[ 4 ] = 1.f;
		matrix[ 5 ] = 0.f;
		
		matrix[ 6 ] = 0.f;
		matrix[ 7 ] = 0.f;
		matrix[ 8 ] = 1.f;
		
		return matrix;
	}
	
	/**
	 * Loads the identity matrix into the provided 4x4, column-major, matrix.
	 * 
	 * @param matrix
	 * @return The modified matrix.
	 */
	public static float[ ] getIdentity_4x4( )
	{
		float matrix[ ] = new float[ 16 ];
		
		matrix[ 0 ]  = 1.f;
		matrix[ 1 ]  = 0.f;
		matrix[ 2 ]  = 0.f;
		matrix[ 3 ]  = 0.f;
		
		matrix[ 4 ]  = 0.f;
		matrix[ 5 ]  = 1.f;
		matrix[ 6 ]  = 0.f;
		matrix[ 7 ]  = 0.f;
		
		matrix[ 8 ]  = 0.f;
		matrix[ 9 ]  = 0.f;
		matrix[ 10 ] = 1.f;
		matrix[ 11 ] = 0.f;
		
		matrix[ 12 ] = 0.f;
		matrix[ 13 ] = 0.f;
		matrix[ 14 ] = 0.f;
		matrix[ 15 ] = 1.f;
		
		return matrix;
	}
	
	/**
	 * Creates a rotation matrix based off of the provided angle (in degrees) and axes.
	 * 
	 * @param angle
	 * @param x
	 * @param y
	 * @param z
	 * @return 3x3, column-major matrix (float[16])
	 */
	public static float[ ] createRotationMatrix_3x3( float angle, float x, float y, float z )
	{
		float matrix[ ] = getIdentity_3x3( );
		float magnitude, sine, cosine;
		
		angle = degreesToRadians( angle );
		
		sine      = ( float )Math.sin( ( double )angle );
		cosine    = ( float )Math.cos( ( double )angle );
		magnitude = ( float )Math.sqrt( ( double )( x * x + y * y + z * z ) );
		
		if( magnitude == 0.0f )
			return matrix;
		
		x /= magnitude;
		y /= magnitude;
		z /= magnitude;
		
		float xx = x * x;
		float yy = y * y;
		float zz = z * z;
		float xy = x * y;
		float yz = y * z;
		float zx = z * x;
		float xs = x * sine;
		float ys = y * sine;
		float zs = z * sine;
		float one_minus_cosine = 1.f - cosine;
		
		matrix[ 0 ] = ( one_minus_cosine * xx ) + cosine;
		matrix[ 1 ] = ( one_minus_cosine * xy ) - zs;
		matrix[ 2 ] = ( one_minus_cosine * zx ) + ys;
		
		matrix[ 3 ] = ( one_minus_cosine * xy ) + zs;
		matrix[ 4 ] = ( one_minus_cosine * yy ) + cosine;
		matrix[ 5 ] = ( one_minus_cosine * yz ) - xs;
		
		matrix[ 6 ] = ( one_minus_cosine * zx ) - ys;
		matrix[ 7 ] = ( one_minus_cosine * yz ) + xs;
		matrix[ 8 ] = ( one_minus_cosine * zz ) + cosine;
		
		return matrix;
	}
	
	/**
	 * Creates a rotation matrix based off of the provided angle (in degrees) and axes.
	 * 
	 * @param angle
	 * @param x
	 * @param y
	 * @param z
	 * @return 3x3, column-major matrix (float[16])
	 */
	public static float[ ] createRotationMatrix_4x4( float angle, float x, float y, float z )
	{
		float matrix[ ] = getIdentity_4x4( );
		float magnitude, sine, cosine;
		
		angle = degreesToRadians( angle );
		
		sine      = ( float )Math.sin( ( double )angle );
		cosine    = ( float )Math.cos( ( double )angle );
		magnitude = ( float )Math.sqrt( ( double )( x * x + y * y + z * z ) );
		
		if( magnitude == 0.0f )
			return matrix;
		
		x /= magnitude;
		y /= magnitude;
		z /= magnitude;
		
		float xx = x * x;
		float yy = y * y;
		float zz = z * z;
		float xy = x * y;
		float yz = y * z;
		float zx = z * x;
		float xs = x * sine;
		float ys = y * sine;
		float zs = z * sine;
		float one_minus_cosine = 1.f - cosine;
		
		matrix[ 0 ]  = ( one_minus_cosine * xx ) + cosine;
		matrix[ 1 ]  = ( one_minus_cosine * xy ) - zs;
		matrix[ 2 ]  = ( one_minus_cosine * zx ) + ys;
		//matrix[3] already set
		
		matrix[ 4 ]  = ( one_minus_cosine * xy ) + zs;
		matrix[ 5 ]  = ( one_minus_cosine * yy ) + cosine;
		matrix[ 6 ]  = ( one_minus_cosine * yz ) - xs;
		//matrix[7] already set
		
		matrix[ 8 ]  = ( one_minus_cosine * zx ) - ys;
		matrix[ 9 ]  = ( one_minus_cosine * yz ) + xs;
		matrix[ 10 ] = ( one_minus_cosine * zz ) + cosine;
		//matrix[11] already set
		
		//matrix[12] already set
		//matrix[13] already set
		//matrix[14] already set
		//matrix[15] already set
		
		return matrix;
	}
	
	/**
	 * Inverts the provided 4x4 matrix.
	 * 
	 * @param matrix_orig
	 * @return
	 */
	public static float[ ] invertMatrix_4x4f( float[ ] matrix_orig )
	{
		if( matrix_orig.length != 16 )
			return matrix_orig;
		
		float matrix[ ] = new float[ 16 ];
		copyMatrix( matrix_orig, matrix );
		
		float ret[ ] = getIdentity_4x4( );
		
		int i, j;
		float det, detij;
		
		det = 0.f;
		
		// Calculate the 4x4 determinant
		for( i = 0; i < 4; i++ )
		{
			det += ( ( i % 2 == 1 )  ? ( -matrix[ i ] * DetIJ( matrix, 0, i ) ) : 
                   (  matrix[ i ] * DetIJ( matrix, 0, i ) ) );
		}
		
		// Calculate inverse

		for( i = 0; i < 4; i++ )
		{
			for( j = 0; j < 4; j++ )
			{
				detij = DetIJ( matrix, j, i );
				ret[ ( i * 4 ) + j ] = ( ( i + j ) % 2 == 1 ) ? ( -detij * det ) : ( detij * det );
			}
		}
		
		return matrix;
	}
	
	public static float[ ] createTranslateMatrix_4x4( float x, float y, float z )
	{
		float matrix[ ] = getIdentity_4x4( );
		
		matrix[ 12 ] = x;
		matrix[ 13 ] = y;
		matrix[ 14 ] = z;
		
		return matrix;
	}
	
	private static float DetIJ( float m[ ], int i, int j )
	{
		int x, y, ii, jj;
		float ret;
		float mat[ ][ ] = new float[ 3 ][ 3 ];

		x = 0;

		for( ii = 0; ii < 4; ii++ )
		{
			if( ii == i )
				continue;

			y = 0;

			for( jj = 0; jj < 4; jj++ )
			{
				if( jj == j )
					continue;

				mat[ x ][ y ] = m[ ( ii * 4 ) + jj ];
				y++;
			}

			x++;
		}

		ret  = mat[ 0 ][ 0 ] * ( mat[ 1 ][ 1 ] * mat[ 2 ][ 2 ] - mat[ 2 ][ 1 ] * mat[ 1 ][ 2 ] );
		ret -= mat[ 0 ][ 1 ] * ( mat[ 1 ][ 0 ] * mat[ 2 ][ 2 ] - mat[ 2 ][ 0 ] * mat[ 1 ][ 2 ] );
		ret += mat[ 0 ][ 2 ] * ( mat[ 1 ][ 0 ] * mat[ 2 ][ 1 ] - mat[ 2 ][ 0 ] * mat[ 1 ][ 1 ] );

		return ret;
	}
	
	//--------------------------------------------------------------------------------------
	// Vector Ops
	
	/**
	 * Calculates and returns the cross product of 3-component vectors a and b.<br><br>
	 * 
	 * The cross product is a third vector perpendicular to the other two (a and b).<br>
	 * As an example: Z_AXIS cross Y_AXIS = X_AXIS
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static float[ ] cross( float a[ ], float b[ ] )
	{
		float c[ ] = new float[ 3 ];
		
		if( a.length != 3 || b.length != 3 )
		{
			c[ 0 ] = 0.f;
			c[ 1 ] = 0.f;
			c[ 2 ] = 0.f;
			
			return c;
		}
		
		c[ 0 ] = a[ 1 ] * b[ 2 ] - a[ 2 ] * b[ 1 ];
		c[ 1 ] = a[ 2 ] * b[ 0 ] - a[ 0 ] * b[ 2 ];
		c[ 2 ] = a[ 0 ] * b[ 1 ] - a[ 1 ] * b[ 0 ];
		
		return c;
	}
	
	/**
	 * Calculates and returns the dot product of 3-component vectors a and b.<br>
	 * The result is already converted to degrees and from the original cosine value.<br><br>
	 * 
	 * The dot product is the angle between two vectors.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static float dot( float a[ ], float b[ ] )
	{
		if( a.length != 3 || b.length != 3 )
			return 0.f;
		
		double result = a[ 0 ] * b[ 0 ] + a[ 1 ] * b[ 1 ] + a[ 2 ] * b[ 2 ];
		
		result = Math.acos( result );
		
		return radiansToDegrees( ( float )result );
	}
	
	/**
	 * Returns the product of the two provided 4x4 matrices.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static float[ ] multiplyMatrix_4x4( float[ ] a, float[ ] b )
	{
		float matrix[ ] = new float[ 16 ];
		
		if( a.length != 16 || b.length != 16 )
			return matrix;
		
		matrix[ 0 ]  = a[ 0 ] * b[ 0 ]  + a[ 4 ] * b[ 1 ]  + a[ 8 ]  * b[ 2 ]  + a[ 12 ] * b[ 3 ];
		matrix[ 4 ]  = a[ 0 ] * b[ 4 ]  + a[ 4 ] * b[ 5 ]  + a[ 8 ]  * b[ 6 ]  + a[ 12 ] * b[ 7 ];
		matrix[ 8 ]  = a[ 0 ] * b[ 8 ]  + a[ 4 ] * b[ 9 ]  + a[ 8 ]  * b[ 10 ] + a[ 12 ] * b[ 11 ];
		matrix[ 12 ] = a[ 0 ] * b[ 12 ] + a[ 4 ] * b[ 13 ] + a[ 8 ]  * b[ 14 ] + a[ 12 ] * b[ 15 ];

		matrix[ 1 ]  = a[ 1 ] * b[ 0 ]  + a[ 5 ] * b[ 1 ]  + a[ 9 ]  * b[ 2 ]  + a[ 13 ] * b[ 3 ];
		matrix[ 5 ]  = a[ 1 ] * b[ 4 ]  + a[ 5 ] * b[ 5 ]  + a[ 9 ]  * b[ 6 ]  + a[ 13 ] * b[ 7 ];
		matrix[ 9 ]  = a[ 1 ] * b[ 8 ]  + a[ 5 ] * b[ 9 ]  + a[ 9 ]  * b[ 10 ] + a[ 13 ] * b[ 11 ];
		matrix[ 13 ] = a[ 1 ] * b[ 12 ] + a[ 5 ] * b[ 13 ] + a[ 9 ]  * b[ 14 ] + a[ 13 ] * b[ 15 ];

		matrix[ 2 ]  = a[ 2 ] * b[ 0 ]  + a[ 6 ] * b[ 1 ]  + a[ 10 ] * b[ 2 ]  + a[ 14 ] * b[ 3 ];
		matrix[ 6 ]  = a[ 2 ] * b[ 4 ]  + a[ 6 ] * b[ 5 ]  + a[ 10 ] * b[ 6 ]  + a[ 14 ] * b[ 7 ];
		matrix[ 10 ] = a[ 2 ] * b[ 8 ]  + a[ 6 ] * b[ 9 ]  + a[ 10 ] * b[ 10 ] + a[ 14 ] * b[ 11 ];
		matrix[ 14 ] = a[ 2 ] * b[ 12 ] + a[ 6 ] * b[ 13 ] + a[ 10 ] * b[ 14 ] + a[ 14 ] * b[ 15 ];

		matrix[ 3 ]  = a[ 3 ] * b[ 0 ]  + a[ 7 ] * b[ 1 ]  + a[ 11 ] * b[ 2 ]  + a[ 15 ] * b[ 3 ];
		matrix[ 7 ]  = a[ 3 ] * b[ 4 ]  + a[ 7 ] * b[ 5 ]  + a[ 11 ] * b[ 6 ]  + a[ 15 ] * b[ 7 ];
		matrix[ 11 ] = a[ 3 ] * b[ 8 ]  + a[ 7 ] * b[ 9 ]  + a[ 11 ] * b[ 10 ] + a[ 15 ] * b[ 11 ];
		matrix[ 15 ] = a[ 3 ] * b[ 12 ] + a[ 7 ] * b[ 13 ] + a[ 11 ] * b[ 14 ] + a[ 15 ] * b[ 15 ];
		
		return matrix;
	}
	
	/**
	 * 
	 * @param vector
	 * @return
	 */
	public static float getVectorLength( float vector[ ] )
	{
		if( vector.length != 3 )
			return 0;
		
		return ( float )Math.sqrt( ( vector[ 0 ] * vector[ 0 ] ) +
				                   ( vector[ 1 ] * vector[ 1 ] ) +
				                   ( vector[ 2 ] * vector[ 2 ] ) );
	}
	
	/**
	 * 
	 * @param vector
	 * @return
	 */
	public static float[ ] normalize_3f( float vector[ ] )
	{
		float normalized[ ] = new float[ 3 ];
		
		if( vector.length != 3 )
			return normalized;
		
		float length = getVectorLength( vector );
		
		normalized[ 0 ] = vector[ 0 ] / length;
		normalized[ 1 ] = vector[ 1 ] / length;
		normalized[ 2 ] = vector[ 2 ] / length;
		
		return normalized;
	}
}
