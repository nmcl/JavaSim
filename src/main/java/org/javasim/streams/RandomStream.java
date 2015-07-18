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
 * The class RandomStream is the base class from which the other distribution
 * classes are derived. It uses a linear congruential generator based on the
 * algorithm from "Algorithms", R. Sedgewick, Addison-Wesley, Reading MA, 1983
 * pp. 36-38. The results of the LC generator are shuffled with a multiplicative
 * generator as suggested by Maclaren and Marsaglia (See Knuth Vol2,
 * Seminumerical Algorithms). The multiplicative generator is courtesy I.
 * Mitrani 1992, private correspondence: Y[i+1] = Y[i] * 5^5 mod 2^26, period is
 * 2^24, initial seed must be odd
 */

public abstract class RandomStream
{
    /**
     * @return In derived classes this method returns the value obtained by the stream.
     * It must be redefined by the deriving class.
     */

    public abstract double getNumber () throws IOException, ArithmeticException;

    /**
     * @return a chi-square error measure on the uniform distribution function.
     */

    public final double error ()
    {
        long r = 100;
        long N = 100 * r;
        long f[] = new long[100];
        int i;

        for (i = 0; i < r; i++)
            f[i] = 0;
        for (i = 0; i < N; i++)
            f[(int) (uniform() * r)]++;
        long t = 0;
        for (i = 0; i < r; i++)
            t += f[i] * f[i];
        double rt = (double) r * t;
        double rtN = rt / (double) N - (double) N;
        return 1.0 - (rtN / r);
    }

    protected RandomStream()
    {
      if (series == null)
      {
        series = new double[128];

        mSeed = 772531;
        lSeed = 1878892440;

        for (int i = 0; i < RandomStream.sizeOfSeries
                / RandomStream.sizeOfDouble; i++)
            series[i] = mgen();
      }
    }

    protected RandomStream(long MGSeed, long LCGSeed)
    {
        series = new double[128];

        // Clean up input parameters

        if ((MGSeed & 1) == 0)
            MGSeed--;
        if (MGSeed < 0)
            MGSeed = -MGSeed;
        if (LCGSeed < 0)
            LCGSeed = -LCGSeed;

        // Initialise state

        mSeed = MGSeed;
        lSeed = LCGSeed;

        for (int i = 0; i < RandomStream.sizeOfSeries
                / RandomStream.sizeOfDouble; i++)
            series[i] = mgen();
    }

    protected final double uniform ()
    {
        // A linear congruential generator based on the algorithm from
        // "Algorithms", R. Sedgewick, Addison-Wesley, Reading MA, 1983.
        // pp. 36-38.

        long m = 100000000;
        long b = 31415821;
        long m1 = 10000;

        // Do the multiplication in pieces to avoid overflow

        long p0 = lSeed % m1, p1 = lSeed / m1, q0 = b % m1, q1 = b / m1;

        lSeed = (((((p0 * q1 + p1 * q0) % m1) * m1 + p0 * q0) % m) + 1) % m;

        // The results of the LC generator are shuffled with
        // the multiplicative generator as suggested by
        // Maclaren and Marsaglia (See Knuth Vol2, Seminumerical Algorithms)

        long choose = lSeed
                % (RandomStream.sizeOfSeries / RandomStream.sizeOfDouble);

        double result = series[(int) choose];
        series[(int) choose] = mgen();

        return result;
    }

    private double mgen ()
    {
        // A multiplicative generator, courtesy I. Mitrani 1992,
        // private correspondence
        // Y[i+1] = Y[i] * 5^5 mod 2^26
        // period is 2^24, initial seed must be odd

        long two2the26th = 67108864; // 2**26

        mSeed = (mSeed * 25) % two2the26th;
        mSeed = (mSeed * 25) % two2the26th;
        mSeed = (mSeed * 5) % two2the26th;

        return (double) mSeed / (double) two2the26th;
    }

    private static long mSeed = 0;

    private static long lSeed = 0;

    private static double[] series = null;

    /*
     * We do this so that we can have the same results when running on most Unix
     * boxes with C++. It doesn't make any difference to the randomness of a
     * distribution.
     */

    static private final long sizeOfSeries = 1024;

    static private final long sizeOfDouble = 8;
}
