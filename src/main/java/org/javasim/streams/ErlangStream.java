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
 * Returns a number from an Erlang distribution with the given mean and standard
 * deviation.
 */

public class ErlangStream extends RandomStream
{
    /**
     * Create a stream with mean 'm' and standard deviation 'sd'.
     */

    public ErlangStream(double m, double sd)
    {
        super();

        Mean = m;
        StandardDeviation = sd;

        double z = Mean / StandardDeviation;
        k = (long) (z * z);
    }

    /**
     * Create a stream with mean 'm' and standard deviation 'sd'. Ignore the
     * first 'StreamSelect' values before starting to return values.
     */

    public ErlangStream(double m, double sd, int StreamSelect)
    {
        super();

        Mean = m;
        StandardDeviation = sd;

        double z = Mean / StandardDeviation;
        k = (long) (z * z);
        for (int ss = 0; ss < StreamSelect * 1000; ss++)
            uniform();
    }

    /**
     * Create a stream with mean 'm' and standard deviation 'sd'. Ignore the
     * first 'StreamSelect' values before starting to return values. The seeds
     * to the RandomStream are 'MGSeed' and 'LGSeed'.
     */

    public ErlangStream(double m, double sd, int StreamSelect, long MGSeed,
            long LCGSeed)
    {
        super(MGSeed, LCGSeed);

        Mean = m;
        StandardDeviation = sd;

        double z = Mean / StandardDeviation;
        k = (long) (z * z);
        for (int ss = 0; ss < StreamSelect * 1000; ss++)
            uniform();
    }

    /**
     * @return a stream number.
     */

    public double getNumber () throws IOException, ArithmeticException
    {
        double z = 1.0;
        for (int i = 0; i < k; i++)
            z *= uniform();

        return -(Mean / k) * Math.log(z);
    }

    private double Mean;

    private double StandardDeviation;

    private long k;
}
