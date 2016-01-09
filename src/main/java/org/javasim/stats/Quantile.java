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

package org.javasim.stats;

/**
 * Provides a means of obtaining the p-quantile of a distribution, i.e., the
 * value below which p-percent of the distribution lies.
 */

public class Quantile extends PrecisionHistogram
{
    /**
     * Create with 95% probability.
     */

    public Quantile()
    {
        qProb = 0.95;
    }

    /**
     * Create with the specified probability. If the probability it greater than
     * 100% (1.0) or less than or equal to 0% then throw an exception.
     * 
     * @param q the probability to use for this instance.
     */

    public Quantile(double q) throws IllegalArgumentException
    {
        qProb = q;

        if ((q <= 0.0) || (q > 1.0))
            throw new IllegalArgumentException("Quantile::Quantile ( " + q
                    + " ) : bad value.");
    }

    /**
     * @return the p-quantile.
     */

    public double getValue ()
    {
        double pSamples = numberOfSamples() * qProb;
        long nEntries = 0;
        Bucket ptr = Head, trail = null;

        while ((nEntries < pSamples) && (ptr != null))
        {
            nEntries += ptr.size();
            trail = ptr;
            ptr = ptr.cdr();
        }

        return trail.name();
    }

    /**
     * @return the p-quantile percentage.
     */

    public double range ()
    {
        return qProb;
    }

    /**
     * Print out the quantile information.
     */

    public void print ()
    {
        System.out.println("Quantile precentage : " + qProb);
        System.out.println("Value below which percentage occurs "
                + this.getValue());
        super.print();
    }

    private double qProb;
}
