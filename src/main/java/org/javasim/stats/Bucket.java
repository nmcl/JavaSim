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

public class Bucket
{
    /**
     * Create bucket with name 'n' and number of entries 'initEntries'.
     */

    public Bucket(double n, long initEntries)
    {
        numberOfEntries = initEntries;
        name = n;
        next = null;
    }

    /**
     * Create bucket with name 'n' and 1 entry.
     */

    public Bucket(double n)
    {
        numberOfEntries = 1;
        name = n;
        next = null;
    }

    /**
     * Copy constructor.
     */

    public Bucket(Bucket b)
    {
        numberOfEntries = b.size();
        name = b.name();
        next = null;
    }

    /**
     * @return whether or not the name of the bucket equal to 'value'?
     */

    public boolean equals (double value)
    {
        if (name == value)
            return true;
        else
            return false;
    }

    /**
     * @return whether or not the name of the bucket greater than 'value'?
     */

    public boolean greaterThan (double value)
    {
        if (name > value)
            return true;
        else
            return false;
    }

    /**
     * @return whether or not the name of the bucket greater than or equal to 'value'?
     */

    public boolean greaterThanOrEqual (double value)
    {
        if (name >= value)
            return true;
        else
            return false;
    }

    /**
     * @return whether or not the name of the bucket less than 'value'?
     */

    public boolean lessThan (double value)
    {
        if (name < value)
            return true;
        else
            return false;
    }

    /**
     * @return whether or not the name of the bucket less than or equal to 'value'?
     */

    public boolean lessThanOrEqual (double value)
    {
        if (name <= value)
            return true;
        else
            return false;
    }

    /**
     * @return the name of the bucket.
     */

    public double name ()
    {
        return name;
    }

    /**
     * Increment the number of entries by 'value'.
     * 
     * @param value the increment.
     */

    public void incrementSize (long value)
    {
        numberOfEntries += value;
    }

    /**
     * Set the number of entries to 'value'.
     * 
     * @param value the size of the bucket.
     */

    public void size (long value)
    {
        numberOfEntries = value;
    }

    /**
     * @return the number of entries.
     */

    public long size ()
    {
        return numberOfEntries;
    }

    /**
     * @return the next bucket.
     */

    public Bucket cdr ()
    {
        return next;
    }

    /**
     * Set the next bucket.
     * 
     * @param n the next bucket.
     */

    public void setCdr (Bucket n)
    {
        next = n;
    }

    private long numberOfEntries;

    private double name;

    private Bucket next;
}
