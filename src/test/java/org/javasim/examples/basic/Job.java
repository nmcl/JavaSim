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

package org.javasim.examples.basic;

import org.javasim.RestartException;
import org.javasim.Scheduler;
import org.javasim.SimulationException;

public class Job
{
    public Job()
    {
        boolean empty = false;

        ResponseTime = 0.0;
        ArrivalTime = Scheduler.currentTime();

        empty = MachineShop.JobQ.isEmpty();
        MachineShop.JobQ.enqueue(this);
        MachineShop.TotalJobs++;

        if (empty && !MachineShop.M.processing()
                && MachineShop.M.isOperational())
        {
            try
            {
                MachineShop.M.activate();
            }
            catch (SimulationException e)
            {
            }
            catch (RestartException e)
            {
            }
        }
    }

    public void finished ()
    {
        ResponseTime = Scheduler.currentTime() - ArrivalTime;
        MachineShop.TotalResponseTime += ResponseTime;
    }

    private double ResponseTime;

    private double ArrivalTime;
}
