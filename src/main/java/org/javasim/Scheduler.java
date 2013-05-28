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

/*
 * Copyright (C) 1996, 1997, 1998,
 *
 * Department of Computing Science,
 * The University,
 * Newcastle upon Tyne,
 * UK.
 *
 * $Id: Scheduler.java,v 1.3 1998/12/07 08:28:10 nmcl Exp $
 */

package org.javasim;

import java.util.NoSuchElementException;

import org.javasim.internal.SimulationProcessIterator;
import org.javasim.internal.SimulationProcessList;

/**
 * This is the scheduler: the heart of the simulation system.
 * 
 * Note: unlike in SIMULA, an active process is removed from the simulation
 * queue prior to being activated.
 * 
 * @author marklittle
 *
 */
public class Scheduler extends Thread
{
    /**
      * Get the current simulation time.
      * 
      * @return the current simulation time.
      */
    
    public static double currentTime ()
    {
	return Scheduler.SimulatedTime;
    }

    /**
     * This routine resets the simulation time to zero and removes all
     * entries from the scheduler queue (as their times may no longer
     * be valid). Whatever operation caused the processes to become
     * suspended will raise the RestartSimulation exception, which the
     * application should catch. It should then perform any work necessary
     * to put the process back in a state ready for restarting the simulation
     * before calling Cancel on the process.
     * 
     * @throws SimulationException if an error occurs.
     */
    
    public static synchronized void reset () throws SimulationException
    {
	boolean finished = false;
	SimulationProcess tmp = SimulationProcess.current();
	
	Scheduler._simulationReset = true;

	// set resetting process to idle

	Scheduler.unschedule(tmp); // remove from queue
	tmp.deactivate();

	do
	{
	    try
	    {
		tmp = Scheduler.ReadyQueue.remove();
	    }
	    catch (NoSuchElementException e)
	    {
		finished = true;
	    }
	    
	} while (!finished);
	
	finished = false;

	SimulationProcessIterator iter = new SimulationProcessIterator(SimulationProcess.allProcesses);
	
	do
	{
	    try
	    {
		tmp = iter.get();

		/*
		 * Every process must be in Suspend, so we call Resume
		 * and get each one to check whether the simulation is
		 * restarting. If it is, it raises an exception and waits
		 * for the user to cancel the process after setting it
		 * to become ready to restart.
		 */
		
		tmp.resumeProcess();

		/*
		 * Wait for this process to become idle again.
		 */
		
		while (!tmp.idle())
		    Thread.yield();
	    }
	    catch (NullPointerException e)
	    {
		finished = true;
	    }
		
	} while (!finished);

	Scheduler.SimulatedTime = 0.0;
	Scheduler._simulationReset = false;

	SimulationProcess.Current = null;
    }

    /**
     * Is the simulation undergoing a reset? Processes should call this
     * method to determine whether the simulation is being reset. If it
     * is, then they should act accordingly.
     * 
     * @return <code>true</code> if the simulation is being reset, <code>false</code> otherwise.
     */
    
    public static synchronized boolean simulationReset ()
    {
	return Scheduler._simulationReset;
    }
    
    /**
     * Stop the simulation. Processes should call this
     * method to determine whether the simulation is being stopped. If it
     * is, then they should act accordingly.
     */
    
    public static synchronized void stopSimulation ()
    {
	Scheduler.schedulerRunning = false;
    }

    /**
     * Start the simulation either from the start or from where it was
     * previously stopped.
     */
    
    public static synchronized void startSimulation ()
    {
	Scheduler.schedulerRunning = true;
    }

    /**
     * Has the simulation started?
     * 
     * @return <code>true</code> if the simulation is running, <code>false</code>
     * otherwise.
     */
    
    protected static synchronized boolean simulationStarted ()
    {
	return Scheduler.schedulerRunning;
    }

    private Scheduler ()
    {
    }

    /**
     * It is possible that the currently active process may remove itself
     * from the simulation queue. In which case we don't want to suspend the
     * process since it needs to continue to run. The return value indicates
     * whether or not to call suspend on the currently active process.
     */
    
    static synchronized boolean schedule () throws SimulationException
    {
	if (Scheduler.simulationStarted())
	{
	    SimulationProcess p = SimulationProcess.current();
	    
	    try
	    {
		SimulationProcess.Current = Scheduler.ReadyQueue.remove();
	    }
	    catch (NoSuchElementException e)
	    {
		System.out.println("Simulation queue empty - terminating.");
		System.exit(0);
	    }

	    if (SimulationProcess.Current.evtime() < 0)
		throw new SimulationException("Invalid SimulationProcess wakeup time.");
	    else
		Scheduler.SimulatedTime = SimulationProcess.Current.evtime();

	    if (p != SimulationProcess.Current)
	    {
		SimulationProcess.Current.resumeProcess();
		
		return true;
	    }
	    else
		return false;
	}
	else
	    throw new SimulationException("Simulation not started.");
    }

    static synchronized void unschedule (SimulationProcess p)
    {
	try
	{
	    Scheduler.ReadyQueue.remove(p); // remove from queue
	}
	catch (NoSuchElementException e)
	{
	}

	p.deactivate();
    }

    static SimulationProcessList getQueue ()
    {
	synchronized (theScheduler)
	{
	    return ReadyQueue;
	}
    }

    static double getSimulationTime ()
    {
	synchronized (theScheduler)
	{
	    return SimulatedTime;
	}
    }
    
    private static double SimulatedTime = 0.0;
    private static SimulationProcessList ReadyQueue = new SimulationProcessList();
    
    private static boolean schedulerRunning = false;
    private static boolean _simulationReset = false;

    static Scheduler theScheduler = new Scheduler();   
}
