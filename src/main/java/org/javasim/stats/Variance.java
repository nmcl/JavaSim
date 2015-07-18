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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Used to obtain the variance of the samples given.
 */

public class Variance extends Mean
{
    public Variance()
    {
        reset();
    }

    /**
     * @param Add to the instance, updating the variance.
     */

    public void setValue (double value) throws IllegalArgumentException
    {
        _sqr += value * value;
        super.setValue(value);
    }

    /**
     * Zero the statistics.
     */

    public void reset ()
    {
        _sqr = 0.0;
        super.reset();
    }

    /**
     * @return the variance.
     */

    public double variance ()
    {
        if (_Number > 1)
            return ((_sqr - ((_Sum * _Sum) / _Number)) / (_Number - 1));
        else
            return 0.0;
    }

    /**
     * @return the standard deviation of the samples.
     */

    public double stdDev ()
    {
        if (_Number == 0 || variance() <= 0)
            return 0.0;
        else
            return Math.sqrt(variance());
    }

    /**
     * @return the confidence.
     * 
     * @param value the confidence range should be between 0 and 0.9999
     */

    public double confidence (double value) throws IllegalArgumentException
    {
        if ((value > 1) || (value < 0))
                throw new IllegalArgumentException();
        
        return mean() + (1+value)*stdDev();
    }

    /**
     * Prints out the statistics information.
     */

    public void print ()
    {
        System.out.println("Variance          : " + variance());
        System.out.println("Standard Deviation: " + stdDev());

        super.print();
    }

    /**
     * Save the state of the histogram to the file named 'fileName'.
     * 
     * @param fileName the file to use.
     * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
     */

    public boolean saveState (String fileName) throws IOException
    {
        FileOutputStream f = new FileOutputStream(fileName);
        DataOutputStream oFile = new DataOutputStream(f);

        boolean res = saveState(oFile);

        f.close();

        return res;
    }

    /**
     * Save the state of the histogram to the stream 'oFile'.
     * 
     * @param oFile the stream to use.
     * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
     */

    public boolean saveState (DataOutputStream oFile) throws IOException
    {
        oFile.writeDouble(_sqr);
        return super.saveState(oFile);
    }

    /**
     * Restore the histogram state from the file 'fileName'.
     * 
     * @param fileName the file to use.
     * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
     */

    public boolean restoreState (String fileName) throws FileNotFoundException,
            IOException
    {
        FileInputStream f = new FileInputStream(fileName);
        DataInputStream iFile = new DataInputStream(f);

        boolean res = restoreState(iFile);

        f.close();

        return res;
    }

    /**
     * Restore the histogram state from the stream 'iFile'.
     * 
     * @param iFile the stream to use.
     * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
     */

    public boolean restoreState (DataInputStream iFile) throws IOException
    {
        _sqr = iFile.readDouble();

        return super.restoreState(iFile);
    }

    private double _sqr;
}
