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
public class Simulation
{
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
	Simulation._simulationReset = true;

	try
	{
	    Scheduler.reset();
	}
	finally
	{
	    Simulation._simulationReset = false;
	}
    }

    /**
     * Is the simulation undergoing a reset? Processes should call this
     * method to determine whether the simulation is being reset. If it
     * is, then they should act accordingly.
     * 
     * @return <code>true</code> if the simulation is being reset, <code>false</code> otherwise.
     */
    
    public static synchronized boolean isReset ()
    {
	return Simulation._simulationReset;
    }
    
    /**
     * Stop the simulation. Processes should call this
     * method to determine whether the simulation is being stopped. If it
     * is, then they should act accordingly.
     */
    
    public static synchronized void stop ()
    {
	Simulation.schedulerRunning = false;
    }

    /**
     * Start the simulation either from the start or from where it was
     * previously stopped.
     */
    
    public static synchronized void start ()
    {
	Simulation.schedulerRunning = true;
    }

    /**
     * Has the simulation started?
     * 
     * @return <code>true</code> if the simulation is running, <code>false</code>
     * otherwise.
     */
    
    protected static synchronized boolean isStarted ()
    {
	return Simulation.schedulerRunning;
    }
    
    private static boolean schedulerRunning = false;
    private static boolean _simulationReset = false; 
}
