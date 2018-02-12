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

import net.imglib2.RealLocalizable;
import net.imglib2.roi.BoundaryType;

/**
 * A {@link SuperEllipsoid} which contains <b>all</b> edge points. It is defined
 * by a center, semi-axis lengths, and an exponent.
 *
 * @author Alison Walter
 * @author Robert Haase, Scientific Computing Facility, MPI-CBG,
 *         rhaase@mpi-cbg.de
 */
public class ClosedSuperEllipsoid extends AbstractSuperEllipsoid
{

	/**
	 * Creates an n-d super ellipsoid, where n is determined by the length of
	 * the smaller array
	 *
	 * @param center
	 *            position of the superellipsoid in space, given in pixel
	 *            coordinates
	 * @param semiAxisLengths
	 *            array containing n elements representing half values of
	 *            width/height/depth/...
	 * @param exponent
	 *            exponent of the superellipsoid
	 */
	public ClosedSuperEllipsoid( final double[] center, final double[] semiAxisLengths, final double exponent )
	{
		super( center, semiAxisLengths, exponent );
	}

	@Override
	public boolean test( final RealLocalizable l )
	{
		return distancePowered( l ) <= 1.0;
	}

	@Override
	public BoundaryType boundaryType()
	{
		return BoundaryType.CLOSED;
	}
}
