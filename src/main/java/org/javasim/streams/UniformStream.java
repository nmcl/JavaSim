/*
 * Copyright 1990-2008, Mark Little, University of Newcastle upon Tyne
 * and others contributors as indicated 
 * by the @authors tag. All rights reserved. 
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors. 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 * 
 * (C) 1990-2008,
 */

package org.javasim.streams;

import java.io.IOException;

/**
 * Returns a number drawn from a uniform distribution with the given lower and
 * upper bounds.
 */

public class UniformStream extends RandomStream
{
    /**
     * Create stream with low bound 'l' and high bound 'h'.
     */

    public UniformStream(double l, double h)
    {
        super();

        lo = l;
        hi = h;
        range = hi - lo;
    }

    /**
     * Create stream with low bound 'l' and high bound 'h'. Skip the first
     * 'StreamSelect' values before returning numbers from the stream.
     */

    public UniformStream(double l, double h, int StreamSelect)
    {
        super();

        lo = l;
        hi = h;
        range = hi - lo;

        for (int i = 0; i < StreamSelect * 1000; i++)
            uniform();
    }

    /**
     * Create stream with low bound 'l' and high bound 'h'. Skip the first
     * 'StreamSelect' values before returning numbers from the stream. Pass the
     * seeds 'MGSeed' and 'LCGSeed' to the base class.
     */

    public UniformStream(double l, double h, int StreamSelect, long MGSeed,
            long LCGSeed)
    {
        super(MGSeed, LCGSeed);

        lo = l;
        hi = h;
        range = hi - lo;

        for (int i = 0; i < StreamSelect * 1000; i++)
            uniform();
    }

    /**
     * @return a number from the stream.
     */

    public double getNumber () throws IOException, ArithmeticException
    {
        return lo + (range * uniform());
    }

    private double lo;

    private double hi;

    private double range;
}
