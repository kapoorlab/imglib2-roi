/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2017 Tobias Pietzsch, Stephan Preibisch, Stephan Saalfeld,
 * John Bogovic, Albert Cardona, Barry DeZonia, Christian Dietz, Jan Funke,
 * Aivar Grislis, Jonathan Hale, Grant Harris, Stefan Helfrich, Mark Hiner,
 * Martin Horn, Steffen Jaensch, Lee Kamentsky, Larry Lindsey, Melissa Linkert,
 * Mark Longair, Brian Northan, Nick Perry, Curtis Rueden, Johannes Schindelin,
 * Jean-Yves Tinevez and Michael Zinsmaier.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

package net.imglib2.roi.geom.real;

import java.util.ArrayList;
import java.util.List;

import net.imglib2.AbstractRealInterval;
import net.imglib2.RealLocalizable;
import net.imglib2.RealPoint;
import net.imglib2.roi.geom.GeomMaths;
import net.imglib2.roi.util.AbstractRealMaskPoint;

/**
 * A polyline, which can be embedded in n-dimensional space.
 *
 * @author Alison Walter
 */
public class DefaultPolyline extends AbstractRealInterval implements Polyline< RealPoint >
{
	private final List< double[] > vertices;

	/**
	 * Creates a polyline with the specified vertices. The dimensionality of the
	 * space is determined by the dimensionality of the first vertex. If a given
	 * vertex has fewer dimensions then an exception will be thrown. However, if
	 * the given vertex has more dimensions it will be truncated.
	 *
	 * @param vertices
	 *            Vertices which define the polyline in the desired order.
	 */
	public DefaultPolyline( final List< ? extends RealLocalizable > vertices )
	{
		super( GeomMaths.getBoundsReal( vertices ) );
		this.vertices = new ArrayList<>( vertices.size() );

		for ( int i = 0; i < vertices.size(); i++ )
		{
			final double[] p = new double[ n ];
			for ( int d = 0; d < n; d++ )
			{
				p[ d ] = vertices.get( i ).getDoublePosition( d );
			}
			this.vertices.add( p );
		}
	}

	@Override
	public boolean test( final RealLocalizable l )
	{
		for ( int i = 1; i < vertices.size(); i++ )
		{
			final double[] ptOne = vertices.get( i - 1 );
			final double[] ptTwo = vertices.get( i );
			final boolean testLineContains = GeomMaths.lineContains( ptOne, ptTwo, l, n );
			if ( testLineContains )
				return true;
		}
		return false;
	}

	/**
	 * Returns the vertex at the specified position. The vertices are in the
	 * same order as when they were passed to the constructor, unless vertices
	 * have been added/removed.
	 */
	@Override
	public RealPoint vertex( final int pos )
	{
		return new PolylineVertex( vertices.get( pos ) );
	}

	@Override
	public int numVertices()
	{
		return vertices.size();
	}

	@Override
	public void addVertex( final int index, final RealLocalizable vertex )
	{
		if ( vertex.numDimensions() < n )
			throw new IllegalArgumentException( "Vertex must have at least" + n + " dimensions" );
		final double[] p = new double[ n ];
		for ( int d = 0; d < n; d++ )
			p[ d ] = vertex.getDoublePosition( d );
		vertices.add( index, p );
		updateMinMax();
	}

	@Override
	public void removeVertex( final int index )
	{
		vertices.remove( index );
		updateMinMax();
	}

	@Override
	public boolean equals( final Object obj )
	{
		if ( !( obj instanceof Polyline ) )
			return false;

		final Polyline< ? > p = ( Polyline< ? > ) obj;
		if ( numVertices() != p.numVertices() || boundaryType() != p.boundaryType() || n != p.numDimensions() )
			return false;

		for ( int i = 0; i < numVertices(); i++ )
		{
			for ( int d = 0; d < n; d++ )
			{
				if ( vertices.get( i )[ d ] != p.vertex( i ).getDoublePosition( d ) )
					return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int result = 777;

		int t = 11;
		for ( int i = 0; i < numVertices(); i++ )
		{
			for ( int d = 0; d < n; d++ )
				result += t * ( vertices.get( i )[ d ] * vertices.get( i )[ d ] );
			t += 3;
		}

		return result;
	}

	// -- Helper methods --

	private void updateMinMax()
	{
		for ( int d = 0; d < n; d++ )
		{
			double minD = vertices.get( 0 )[ d ];
			double maxD = vertices.get( 0 )[ d ];
			for ( int i = 1; i < numVertices(); i++ )
			{
				if ( vertices.get( i )[ d ] < minD )
					minD = vertices.get( i )[ d ];
				if ( vertices.get( i )[ d ] > maxD )
					maxD = vertices.get( i )[ d ];
			}
			min[ d ] = minD;
			max[ d ] = maxD;
		}
	}

	// -- Helper classes --

	private class PolylineVertex extends AbstractRealMaskPoint
	{
		public PolylineVertex( final double[] pos )
		{
			super( pos );
		}

		@Override
		public void updateBounds()
		{
			updateMinMax();
		}

	}
}
