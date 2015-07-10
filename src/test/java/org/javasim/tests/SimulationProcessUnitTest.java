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

package org.javasim.tests;

import org.javasim.RestartException;
import org.javasim.Simulation;
import org.javasim.SimulationException;
import org.javasim.SimulationProcess;
import org.javasim.streams.ExponentialStream;
import org.junit.Test;

import static org.junit.Assert.*;

class Dummy extends SimulationProcess
{
    public Dummy (double mean)
    {
        InterArrivalTime = new ExponentialStream(mean);
    }

    public void run ()
    {
        try
        {
            hold(InterArrivalTime.getNumber());
        }
        catch (final Exception ex)
        {
        }
    }

    private ExponentialStream InterArrivalTime;
}

class Runner extends SimulationProcess
{
    public Runner ()
    {
    }

    public void run ()
    {
        try
        {
            Dummy A = new Dummy(8);

            A.activateDelay(2000);
            
            Simulation.start();

            hold(1000);

            Simulation.stop();

            A.terminate();

            SimulationProcess.mainResume();
        }
        catch (final Exception e)
        {
        }
    }

    public void await ()
    {
        this.resumeProcess();
        SimulationProcess.mainSuspend();
    }
}


public class SimulationProcessUnitTest
{
    @Test
    public void test () throws Exception
    {
        Runner proc = new Runner();

        proc.await();
        
        assertTrue(proc.time() > 0.0);
        assertTrue(proc.nextEv() != null);
        assertTrue(proc.evtime() == 1000.0);
        
        assertFalse(proc.idle());
        assertFalse(proc.passivated());
        assertFalse(proc.terminated());
        
        assertTrue(SimulationProcess.current() != null);
    }
}
