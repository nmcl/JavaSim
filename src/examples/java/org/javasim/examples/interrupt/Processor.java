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

import java.io.IOException;

import org.javasim.RestartException;
import org.javasim.SimulationEntity;
import org.javasim.SimulationException;
import org.javasim.streams.ExponentialStream;

public class Processor extends SimulationEntity
{
    public Processor(double mean)
    {
        sTime = new ExponentialStream(mean);
    }

    public void run ()
    {
        Job j = null;

        while (!terminated())
        {
            try
            {
                try
                {
                    timedWait(sTime.getNumber());

                    if (!MachineShop.JobQ.isEmpty())
                    {
                        j = MachineShop.JobQ.dequeue();
                        MachineShop.ProcessedJobs++;
                    }
                }
                catch (InterruptedException e)
                {
                    if (MachineShop.SignalQ.isEmpty())
                        System.out
                                .println("Error - signal caught, but no message given!");
                    else
                    {
                        j = MachineShop.SignalQ.dequeue();
                        MachineShop.SignalledJobs++;
                    }
                }

                if (MachineShop.SignalledJobs == 2)
                    terminate();
            }
            catch (SimulationException e)
            {
            }
            catch (RestartException e)
            {
            }
            catch (IOException e)
            {
            }
        }
    }

    private ExponentialStream sTime;
}
