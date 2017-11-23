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

/**
 * Classic semaphores can "accumulate" more resources than the starting
 * value. The ceiling parameter is used to indicate whether or not the
 * resource count should ever go beyond the initial value - the default
 * is that it should.
 */

public class Semaphore
{
    /**
     * The result of various operations:
     * DONE - succeeded.
     * NOTDONE - failed.
     * WOULD_BLOCK - the operation would block the thread.
     * 
     * Note that for some operations only DONE and NOTDONE
     * could be returned.
     */
     
    public enum Outcome { DONE, NOTDONE, WOULD_BLOCK };
    
    /**
     * Create a new mutex (resources = 1).
     */

    public Semaphore()
    {
        numberWaiting = 0;
        numberOfResources = 1;
        currentResources = 1;
        waitingList = new TriggerQueue();
    }

    /**
     * Create a new semaphore (resources = number).
     * 
     * @param number the number of resources.
     */

    public Semaphore(long number)
    {
        numberWaiting = 0;
        numberOfResources = number;
        currentResources = number;
        waitingList = new TriggerQueue();
    }

    public void finalize ()
    {
        if (numberWaiting != 0)
            System.out
                    .println("Warning: semaphore being removed with clients waiting.");
    }

    /**
     * Number of entities blocked on the semaphore.
     * 
     * @return the number of entities blocked.
     */

    public long numberWaiting ()
    {
        return numberWaiting;
    }

    /**
     * Try to acquire the semaphore. Caller will be blocked if there are no free
     * resources.
     * 
     * @param toWait the entity that will be blocked.
     * @return an indication of the outcome (DONE, NOTDONE)
     * @throws RestartException if a reset occurs while an entity is blocked.
     */

    public Outcome get (SimulationEntity toWait)
            throws RestartException
    {
        if (currentResources > 0)
            currentResources--;
        else
        {
            numberWaiting++;

            try
            {
                waitingList.insert(toWait);
            }
            catch (SimulationException e)
            {
            }
	    
            toWait.cancel();
        }

        return Outcome.DONE;
    }

    /**
     * Only acquire the semaphore if it would not block the caller.
     * 
     * @param toWait the entity to block.
     * @return the outcome (DONE, NOTDONE or WOULD_BLOCK)
     */

    public Outcome tryGet (SimulationEntity toWait)
            throws RestartException
    {
        if (currentResources == 0)
            return Outcome.WOULD_BLOCK;
        else
            return get(toWait);
    }

    /**
     * Release the semaphore. No check is made to ensure the caller has
     * previously acquired the semaphore.
     * 
     * @return the outcome (DONE or NOTDONE)
     */

    public Outcome release ()
    {
        // if there are things waiting they get triggered right here
        // and recomsume the resource that would have been freed otherwise
        // by this release call
        if (numberWaiting > 0)
        {
            numberWaiting--;

            waitingList.triggerFirst(true);

            return Outcome.DONE;
        }
        else
        {
            // There is nothing waiting so we can free up a resource
            currentResources++;

            return Outcome.DONE;
        }
    }

    private TriggerQueue waitingList;

    private long numberWaiting;

    private long numberOfResources;

    private long currentResources;
}
