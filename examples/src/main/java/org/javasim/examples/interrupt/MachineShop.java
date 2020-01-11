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

package org.javasim.examples.interrupt;

import org.javasim.RestartException;
import org.javasim.Scheduler;
import org.javasim.Simulation;
import org.javasim.SimulationEntity;
import org.javasim.SimulationException;
import org.javasim.SimulationProcess;

public class MachineShop extends SimulationEntity
{
    public MachineShop()
    {
    }

    public void run ()
    {
        try
        {
            Signaller s = new Signaller(1000);
            Arrivals A = new Arrivals(2);
            MachineShop.cpu = new Processor(10);
            Job J = new Job(false);

            MachineShop.cpu.activate();
            A.activate();
            s.activate();

            Simulation.start();

            waitFor(cpu);

            System.out.println("Total jobs processed " + ProcessedJobs);
            System.out.println("Total signals processed " + SignalledJobs);

            Simulation.stop();

            MachineShop.cpu.terminate();
            A.terminate();
            s.terminate();

            SimulationProcess.mainResume();
        }
        catch (SimulationException e)
        {
        }
        catch (InterruptedException e)
        {
        }
        catch (RestartException e)
        {
        }
    }

    public void await ()
    {
        this.resumeProcess();
        SimulationProcess.mainSuspend();
    }

    public static Processor cpu = null;

    public static Queue JobQ = new Queue();

    public static Queue SignalQ = new Queue();

    public static long ProcessedJobs = 0;

    public static long SignalledJobs = 0;

    public static double TotalResponseTime = 0.0;

    public static double MachineActiveTime = 0.0;
}
