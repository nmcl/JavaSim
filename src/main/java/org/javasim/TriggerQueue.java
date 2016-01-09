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

package org.javasim;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * A TriggerQueue is used to hold simulation process instances.
 */
 
public class TriggerQueue
{
    /**
     * Create a new instance.
     */
     
    public TriggerQueue()
    {
        head = new LinkedList<SimulationEntity>();
    }

    /**
     * If there is anything left on the queue when this goes out-of-scope then
     * we will place them back on to the simulation queue, rather than doing
     * nothing - this is the safest thing to do. THIS SHOULD NOT HAPPEN THOUGH!*
     */

    public void finalize ()
    {
        if (head != null)
        {
            if (head.size() > 0)
            {
                System.out
                        .println("TriggerQueue destructor called with non-zero list!");

                try
                {
                    triggerAll();
                }
                catch (NoSuchElementException e)
                {
                }
            }
        }
    }

    /**
     * Remove the first entry from the queue, possibly trigger it and then reactivate it
     * at the current simulation time.
     * 
     * @param setTrigger indicate whether or not to also trigger the removed process.
     * @throws NoSuchElementException thrown if the queue is empty.
     */
     
    public synchronized void triggerFirst (boolean setTrigger)
            throws NoSuchElementException
    {
        if (head.size() == 0)
            throw new NoSuchElementException();

        SimulationEntity removed = this.remove();

        if (setTrigger)
            removed.trigger();

        try
        {
            removed.reactivateAt(SimulationProcess.currentTime());
        }
        catch (RestartException e)
        {
        }
        catch (SimulationException e)
        {
        }
    }

    /**
     * Remove the first instance from the queue, trigger it and activate it at
     * the current time.
     * 
     * @throws NoSuchElementException thrown if the queue is empty.
     */
     
    public synchronized void triggerFirst () throws NoSuchElementException
    {
        triggerFirst(true);
    }

    /**
     * Remove all simulation processes from the queue and trigger them.
     * 
     * @throws NoSuchElementException thrown if the queue is empty.
     */
     
    public synchronized void triggerAll () throws NoSuchElementException
    {
        long currentNumber = head.size();

        if (currentNumber == 0)
            throw new NoSuchElementException();

        for (int i = 0; i < currentNumber; i++)
            triggerFirst();
    }

    protected synchronized void insert (SimulationEntity toAdd)
            throws SimulationException
    {
        if (toAdd.isWaiting())
            throw new SimulationException("Entity already waiting on event.");

        head.add(toAdd);
    }

    protected synchronized SimulationEntity remove ()
            throws NoSuchElementException
    {
        return head.removeFirst();
    }

    private LinkedList<SimulationEntity> head;
}
