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
 * Returns a number from a normal distribution with the given mean and standard
 * deviation.
 */

public class NormalStream extends RandomStream
{
    /**
     * Create stream with mean 'm' and standard deviation 'sd'.
     */

    public NormalStream(double m, double sd)
    {
        super();

        mean = m;
        standardDeviation = sd;
        z = 0.0;
    }

    /**
     * Create stream with mean 'm' and standard deviation 'sd'. Skip the first
     * 'StreamSelect' values.
     */

    public NormalStream(double m, double sd, int StreamSelect)
    {
        super();

        mean = m;
        standardDeviation = sd;
        z = 0.0;

        for (int ss = 0; ss < StreamSelect * 1000; ss++)
            uniform();
    }

    /**
     * Create stream with mean 'm' and standard deviation 'sd'. Skip the first
     * 'StreamSelect' values. Pass seeds 'MGSeed' and 'LCGSeed' to the base
     * class.
     */

    public NormalStream(double m, double sd, int StreamSelect, long MGSeed,
            long LCGSeed)
    {
        super(MGSeed, LCGSeed);

        mean = m;
        standardDeviation = sd;
        z = 0.0;

        for (int ss = 0; ss < StreamSelect * 1000; ss++)
            uniform();
    }

    /**
     * Use the polar method, due to Box, Muller and
     * Marsaglia.Taken from Seminumerical Algorithms, Knuth, Addison-Wesley,
     * p.117.
     * 
     * @return a stream number.
     */

    public double getNumber () throws IOException, ArithmeticException
    {
        // Use the polar method, due to Box, Muller and Marsaglia
        // Taken from Seminumerical Algorithms, Knuth, Addison-Wesley, p.117

        double X2;

        if (z != 0.0)
        {
            X2 = z;
            z = 0.0;
        }
        else
        {
            double S, v1, v2;
            do
            {
                v1 = 2.0 * uniform() - 1.0;
                v2 = 2.0 * uniform() - 1.0;
                S = v1 * v1 + v2 * v2;
            }
            while (S >= 1.0);

            S = Math.sqrt((-2.0 * Math.log(S)) / S);
            X2 = v1 * S;
            z = v2 * S;
        }

        return mean + X2 * standardDeviation;
    }

    private double mean;

    private double standardDeviation;

    private double z;
}
