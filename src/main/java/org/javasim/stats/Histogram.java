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
 * This histogram class maintains a fixed number of buckets. When the number of
 * buckets required to maintain all of the values given is about to be exceeded
 * a merge operation is performed. This takes a pair of buckets and merges their
 * values according to the policy selected when the histogram was created. The
 * policies are: (1) ACCUMULATE - this creates a new bucket with the same name
 * as the largest of the buckets, and it has the sum of the two old bucket
 * entries as its entry number. (2) MEAN - this creates a new bucket with the
 * name as the mean of the two old buckets, and it has the sum of the two old
 * bucket entries as its entry number. (3) MAX - this creates a new bucket with
 * the name as the largest of the buckets, and it has the same number of
 * entries. (4) MIN - this creates a new bucket with the name as the smallest of
 * the two buckets, and it has the same number of entries.
 */

public class Histogram extends PrecisionHistogram
{   
    public static final int ACCUMULATE = 0;

    public static final int MEAN = 1;

    public static final int MAX = 2;

    public static final int MIN = 3;
    
    /**
     * Create with maximum index 'maxIndex' and specified 'mergeChoice', which
     * will be used if the buckets must be merged.
     */

    public Histogram(long maxIndex, int mergeChoice)
    {
        if (maxIndex > 1)
            maxSize = maxIndex;
        else
            maxSize = 2;

        merge = mergeChoice;
    }

    /**
     * Create with maximum index 'maxIndex'. Merge choice is MEAN.
     */

    public Histogram(long maxIndex)
    {
        if (maxIndex > 1)
            maxSize = maxIndex;
        else
            maxSize = 2;

        merge = Histogram.MEAN;
    }

    /**
     * Add 'value' to the histogram. If a bucket already exists for this then it
     * is incremented, otherwise a new bucket will be created. This may require
     * the existing buckets to be merged to make room.
     * 
     * @param value the number to use.
     */

    public void setValue (double value) throws IllegalArgumentException
    {
        if ((numberOfBuckets() == maxSize) && (!isPresent(value)))
        {
            try
            {
                mergeBuckets();
            }
            catch (StatisticsException e)
            {
            }
        }

        super.setValue(value);
    }

    /**
     * Save the state of the histogram to the file named 'fileName'.
     * 
     * @param fileName the name of the file to use to save the state.
     * @return <code>true</code> if the state was written, <code>false</code> otherwise.
     */

    public boolean saveState (String fileName) throws IOException
    {
        FileOutputStream f = new FileOutputStream(fileName);
        DataOutputStream iFile = new DataOutputStream(f);

        boolean res = saveState(iFile);

        f.close();

        return res;
    }

    /**
     * Save the state of the histogram to the stream 'oFile'.
     * 
     * @param oFile the name of the DataOutputStream to use to save the state.
     * @return <code>true</code> if the state was written, <code>false</code> otherwise.
     */

    public boolean saveState (DataOutputStream oFile) throws IOException
    {
        oFile.writeLong(maxSize);
        oFile.writeInt(merge);

        return super.saveState(oFile);
    }

    /**
     * Restore the histogram state from the file 'fileName'.
     * 
     * @param fileName the name of the file to use to read the state.
     * @return <code>true</code> if the state was read, <code>false</code> otherwise.
     */

    public boolean restoreState (String fileName) throws FileNotFoundException,
            IOException
    {
        FileInputStream f = new FileInputStream(fileName);
        DataInputStream oFile = new DataInputStream(f);

        boolean res = restoreState(oFile);

        f.close();

        return res;
    }

    /**
     * Restore the histogram state from the stream 'iFile'.
     * 
     * @param iFile the name of the DataInputStream to use to read the state.
     * @return <code>true</code> if the state was read, <code>false</code> otherwise.
     */

    public boolean restoreState (DataInputStream iFile) throws IOException
    {
        maxSize = iFile.readLong();
        merge = iFile.readInt();

        return super.restoreState(iFile);
    }

    /**
     * Print the contents of the histogram.
     */

    public void print ()
    {
        System.out.println("Maximum number of buckets " + maxSize);
        System.out.print("Merge choice is ");

        switch (merge)
        {
        case Histogram.ACCUMULATE:
            System.out.println("ACCUMULATE");
            break;
        case Histogram.MEAN:
            System.out.println("MEAN");
            break;
        case Histogram.MAX:
            System.out.println("MAX");
            break;
        case Histogram.MIN:
            System.out.println("MIN");
            break;
        }

        super.print();
    }

    /**
     * Merge the existing buckets according to the merge criteria specified when
     * the histogram was created.
     */

    protected void mergeBuckets () throws StatisticsException
    {
        Bucket newHead = null, ptr = null;
        Bucket index = Head;
        long newLength = 0;

        index = super.Head;

        while (index != null)
        {
            Bucket newElement = null;

            // merge buckets in pairs

            if (index.cdr() != null)
            {
                newElement = new Bucket(compositeName(index, index.cdr()));
                newElement.size(compositeSize(index, index.cdr()));

                // move on to next pair of buckets

                index = (index.cdr()).cdr();
            }
            else
                newElement = new Bucket(index);

            newLength++;
            if (newHead != null)
                ptr.setCdr(newElement);
            else
                newHead = newElement;

            ptr = newElement;
        }

        index = super.Head;
        ptr = index;

        while (index != null)
        {
            ptr = index.cdr();
            index = ptr;
        }

        super.Head = newHead;
        super.length = newLength;
    }

    private double compositeName (Bucket a, Bucket b)
    {
        switch (merge)
        {
        case ACCUMULATE:
        case MAX:
            return b.name();
        case MEAN:
            return ((a.name() * a.size() + b.name() * b.size()) / (a.size() + b
                    .size()));
        case MIN:
            return a.name();
        default:
            break;
        }

        return 0.0;
    }

    private long compositeSize (Bucket a, Bucket b) throws StatisticsException
    {
        switch (merge)
        {
        case ACCUMULATE:
            return (a.size() + b.size());
        case MEAN:
            return (a.size() + b.size());
        case MAX:
            return b.size();
        case MIN:
            return a.size();
        default:
            break;
        }

        // shouldn't get here!

        throw new StatisticsException("compositeSize switch error.");
    }

    protected long maxSize;

    protected int merge;
}
