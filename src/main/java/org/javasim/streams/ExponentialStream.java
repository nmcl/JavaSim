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
 * Returns a number from an exponential distribution with the given mean.
 */

public class ExponentialStream extends RandomStream
{
    /**
     * Create stream with mean 'm'.
     */

    public ExponentialStream(double m)
    {
        super();

        Mean = m;
    }

    /**
     * Create stream with mean 'm'. Skip the first 'StreamSelect' stream values.
     */

    public ExponentialStream(double m, int StreamSelect)
    {
        super();

        Mean = m;

        for (int i = 0; i < StreamSelect * 1000; i++)
            uniform();
    }

    /**
     * Create stream with mean 'm'. Skip the first 'StreamSelect' stream values.
     * Pass seeds 'MGSeed' and 'LCGSeed' to the base class.
     */

    public ExponentialStream(double m, int StreamSelect, long MGSeed,
            long LCGSeed)
    {
        super(MGSeed, LCGSeed);

        Mean = m;

        for (int i = 0; i < StreamSelect * 1000; i++)
            uniform();
    }

    /**
     * @return stream number.
     */

    public double getNumber () throws IOException, ArithmeticException
    {
        return -Mean * Math.log(uniform());
    }

    private double Mean;
}
