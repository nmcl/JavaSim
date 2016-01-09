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

/**
 * A simple histogram with a set number of buckets.
 */

public class SimpleHistogram extends PrecisionHistogram
{
    /**
     * Create with 'nbuckets' evenly distributed over the range 'min' to 'max'.
     */

    public SimpleHistogram(double min, double max, long nbuckets)
    {
        if (min < max)
        {
            minIndex = min;
            maxIndex = max;
        }
        else
        {
            minIndex = max;
            maxIndex = min;
        }

        if (nbuckets > 0)
            numberBuckets = nbuckets;
        else
            nbuckets = 1;

        width = (max - min) / numberBuckets;
        reset();
    }

    /**
     * Create a number of buckets with width 'w' evenly distributed over the
     * range 'min' to 'max'.
     */

    public SimpleHistogram(double min, double max, double w)
    {
        if (min < max)
        {
            minIndex = min;
            maxIndex = max;
        }
        else
        {
            minIndex = max;
            maxIndex = min;
        }

        if (w > 0)
            width = w;
        else
            width = 2.0;

        numberBuckets = (long) ((max - min) / width);

        if ((max - min) / width - numberBuckets > 0)
            numberBuckets++;

        reset();
    }

    /**
     * @param value add to the histogram. If it is outside the range of the histogram
     * then raise an exception, otherwise find the appropriate bucket and
     * increment it.
     */

    public void setValue (double value) throws IllegalArgumentException
    {
        if ((value < minIndex) || (value > maxIndex))
            throw new IllegalArgumentException("Value " + value
                    + " is beyond histogram range [ " + minIndex + ", "
                    + maxIndex + " ]");

        for (Bucket ptr = Head; ptr != null; ptr = ptr.cdr())
        {
            double bucketValue = ptr.name();

            if ((value == bucketValue) || (value <= bucketValue + width))
            {
                super.setValue(ptr.name());
                return;
            }
        }

        // shouldn't get here!!

        throw new IllegalArgumentException("Something went wrong with "
                + value);
    }

    /**
     * Empty the histogram. Always keep the number of buckets that
     * were originally specified though.
     */

    public void reset ()
    {
        double value = minIndex;

        super.reset();

        // pre-create buckets with given width

        for (int i = 0; i < numberBuckets; value += width, i++)
            create(value);
    }

    /**
     * Get the number of entries in bucket 'name'.
     * 
     * @param name the id of the bucket.
     * @return the number of entries in the bucket.
     */

    public double sizeByName (double name) throws IllegalArgumentException
    {
        if ((name < minIndex) || (name > maxIndex))
            throw new IllegalArgumentException("Argument out of range.");

        for (Bucket ptr = Head; ptr != null; ptr = ptr.cdr())
        {
            double bucketValue = ptr.name();

            if ((name == bucketValue) || (name <= bucketValue + width))
                return ptr.size();
        }

        throw new IllegalArgumentException("Name " + name + " out of range.");
    }

    /**
     * @return the width of each bucket.
     */

    public double Width ()
    {
        return width;
    }
    
    /**
     * Print out information about the histogram.
     */

    public void print ()
    {
        System.out.println("SimpleHistogram Data:");
        System.out.println("Maximum index range  : " + maxIndex);
        System.out.println("Minimum index range  : " + minIndex);
        System.out.println("Number of buckets    : " + numberBuckets);
        System.out.println("Width of each bucket : " + width);

        super.print();
    }

    /**
     * Save the state of the histogram to the file named 'fileName'.
     * 
     * @param fileName the file to use.
     * @return <code>true</code> if it succeeded, <code>false</code> otherwise.
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
     * @return <code>true</code> if it succeeded, <code>false</code> otherwise.
     */

    public boolean saveState (DataOutputStream oFile) throws IOException
    {
        oFile.writeDouble(minIndex);
        oFile.writeDouble(maxIndex);
        oFile.writeDouble(width);
        oFile.writeLong(numberBuckets);

        return super.saveState(oFile);
    }

    /**
     * Restore the histogram state from the file 'fileName'.
     * 
     * @param fileName the file to use.
     * @return <code>true</code> if it succeeded, <code>false</code> otherwise.
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
     * @return <code>true</code> if it succeeded, <code>false</code> otherwise.
     */

    public boolean restoreState (DataInputStream iFile) throws IOException
    {
        minIndex = iFile.readDouble();
        maxIndex = iFile.readDouble();
        width = iFile.readDouble();
        numberBuckets = iFile.readLong();

        return super.restoreState(iFile);
    }

    private double minIndex;

    private double maxIndex;

    private double width;

    private long numberBuckets;
}
