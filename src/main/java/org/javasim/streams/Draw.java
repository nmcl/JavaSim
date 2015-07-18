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
 * Return true or false with probability given when constructed. Uses a
 * UniformStream.
 */

public class Draw
{
    /**
     * Probability of true is 'p'.
     */

    public Draw(double p)
    {
        s = new UniformStream(0, 1);
        prob = p;
    }

    /**
     * Probability 'p'. Ignore the first 'StreamSelect' values before starting
     * to return values.
     */

    public Draw(double p, int StreamSelect)
    {
        s = new UniformStream(0, 1, StreamSelect);
        prob = p;
    }

    /**
     * Probability 'p'. Ignore the first 'StreamSelect' values before starting
     * to return values. The seeds to the UniformStream are 'MGSeed' and
     * 'LGSeed'.
     */

    public Draw(double p, int StreamSelect, long MGSeed, long LCGSeed)
    {
        s = new UniformStream(0, 1, StreamSelect, MGSeed, LCGSeed);
        prob = p;
    }

    /**
     * @return true with specified probability.
     */

    public boolean getBoolean () throws IOException
    {
        if (s.getNumber() >= prob)
            return true;
        else
            return false;
    }
    
    private UniformStream s;

    private double prob;
}
