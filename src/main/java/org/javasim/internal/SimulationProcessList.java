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

package org.javasim.internal;

import java.util.NoSuchElementException;

import org.javasim.SimulationProcess;

public class SimulationProcessList
{
    public SimulationProcessList()
    {
        Head = null;
    }

    public synchronized void insert (SimulationProcess p)
    {
        insert(p, false);
    }

    public synchronized void insert (SimulationProcess p, boolean prior)
    {
        // If list is empty, insert at head

        if (Head == null)
        {
            Head = new SimulationProcessCons(p, null);
            return;
        }

        // Try to insert before (if there is anything scheduled later)

        SimulationProcessIterator iter = new SimulationProcessIterator(this);
        SimulationProcess prev = null;

        for (SimulationProcess q = iter.get(); q != null; prev = q, q = iter
                .get())
        {
            if (prior)
            {
                if (q.evtime() >= p.evtime())
                {
                    insertBefore(p, q);
                    return;
                }
            }
            else
            {
                if (q.evtime() > p.evtime())
                {
                    insertBefore(p, q);
                    return;
                }
            }
        }

        // Got to insert at the end (currently pointed at by 'prev')

        insertAfter(p, prev);
    }

    public synchronized boolean insertBefore (SimulationProcess ToInsert,
            SimulationProcess Before)
    {
        for (SimulationProcessCons prev = null, p = Head; p != null; prev = p, p = p
                .cdr())
        {
            if (p.car() == Before)
            {
                SimulationProcessCons newcons = new SimulationProcessCons(
                        ToInsert, p);
                if (prev != null)
                    prev.setfCdr(newcons);
                else
                    Head = newcons;

                return true;
            }
        }

        return false;
    }

    public synchronized boolean insertAfter (SimulationProcess ToInsert,
            SimulationProcess After)
    {
        for (SimulationProcessCons p = Head; p != null; p = p.cdr())
            if (p.car() == After)
            {
                SimulationProcessCons newcons = new SimulationProcessCons(
                        ToInsert, p.cdr());
                p.setfCdr(newcons);
                return true;
            }

        return false;
    }

    public synchronized SimulationProcess remove (SimulationProcess element)
            throws NoSuchElementException
    {
        // Take care of boundary condition - empty list

        if (Head == null)
            throw new NoSuchElementException();

        SimulationProcess p = null;

        for (SimulationProcessCons prev = null, ptr = Head; ptr != null; prev = ptr, ptr = ptr
                .cdr())
        {
            if (ptr.car() == element)
            {
                SimulationProcessCons oldcons = ptr;

                // unlink the cons cell for the element we're removing

                if (prev != null)
                    prev.setfCdr(ptr.cdr());
                else
                    Head = ptr.cdr();

                // return the pointer to the process
                p = ptr.car();

                return p;
            }
        }

        throw new NoSuchElementException();
    }

    public synchronized SimulationProcess remove ()
            throws NoSuchElementException
    {
        // Change unspecified element to "remove head of list" request

        if (Head != null)
            return (remove(Head.car()));
        else
            throw new NoSuchElementException();
    }

    public synchronized SimulationProcess getNext (SimulationProcess current)
            throws NoSuchElementException
    {
        // take care of boundary condition - empty list.

        if ((Head == null) || (current == null))
            throw new NoSuchElementException();

        for (SimulationProcessCons ptr = Head; ptr != null; ptr = ptr.cdr())
        {
            if (ptr.car() == current)
            {
                if (ptr.cdr() == null)
                    return null;
                else
                    return ptr.cdr().car();
            }
            else // terminate search - past the point current could be
            if (ptr.car().evtime() > current.evtime())
                break;
        }

        /*
         * If we get here then we have not found current on the list which can
         * only mean that it is currently active.
         */

        return Head.car();
    }

    public void print ()
    {
        SimulationProcessIterator iter = new SimulationProcessIterator(this);
        SimulationProcess prev = null;

        for (SimulationProcess q = iter.get(); q != null; prev = q, q = iter
                .get())
        {
            System.out.println(q.evtime());
        }
    }

    // package?

    protected SimulationProcessCons Head;
}
