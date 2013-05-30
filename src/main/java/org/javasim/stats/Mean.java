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

import java.io.*;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Mean
{
    public Mean()
    {
        reset();
    }

    /**
     * Add 'value', incrementing the number of samples, the sum, mean, etc.
     */

    public void setValue (double value) throws IllegalArgumentException
    {
        if (value > _Max)
            _Max = value;
        if (value < _Min)
            _Min = value;
        _Sum += value;
        _Number++;
        _Mean = (double) (_Sum / _Number);
    }

    /**
     * Reset the object.
     */

    public void reset ()
    {
        _Max = Float.MIN_VALUE;
        _Min = Float.MAX_VALUE;
        _Sum = _Mean = 0.0;
        _Number = 0;
    }

    /**
     * Returns the number of samples.
     */

    public int numberOfSamples ()
    {
        return _Number;
    }

    /**
     * Returns the minimum value given.
     */

    public double min ()
    {
        return _Min;
    }

    /**
     * Returns the maximum value given.
     */

    public double max ()
    {
        return _Max;
    }

    /**
     * Returns the sum of all values.
     */

    public double sum ()
    {
        return _Sum;
    }

    /**
     * Returns the mean value.
     */

    public double mean ()
    {
        return _Mean;
    }

    /**
     * Save the state of the histogram to the file named 'fileName'.
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
     */

    public boolean saveState (DataOutputStream oFile) throws IOException
    {
        oFile.writeDouble(_Max);
        oFile.writeDouble(_Min);
        oFile.writeDouble(_Sum);
        oFile.writeDouble(_Mean);
        oFile.writeInt(_Number);

        return true;
    }

    /**
     * Restore the histogram state from the file 'fileName'.
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
     */

    public boolean restoreState (DataInputStream iFile) throws IOException
    {
        _Max = iFile.readDouble();
        _Min = iFile.readDouble();
        _Sum = iFile.readDouble();
        _Mean = iFile.readDouble();
        _Number = iFile.readInt();

        return true;
    }

    /**
     * Print out the statistics compiled on the data given.
     */

    public void print ()
    {
        System.out.println("Number of samples : " + numberOfSamples());
        System.out.println("Minimum           : " + min());
        System.out.println("Maximum           : " + max());
        System.out.println("Sum               : " + sum());
        System.out.println("Mean              : " + mean());
    }

    protected double _Max;

    protected double _Min;

    protected double _Sum;

    protected double _Mean;

    protected int _Number;
}
