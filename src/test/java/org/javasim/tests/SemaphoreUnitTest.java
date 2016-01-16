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
import org.javasim.Semaphore;
import org.javasim.SimulationEntity;
import org.javasim.streams.ExponentialStream;
import org.junit.Test;

import static org.junit.Assert.*;

class DummyEntity extends SimulationEntity
{
    public DummyEntity (double mean)
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


public class SemaphoreUnitTest
{
    @Test
    public void test () throws Exception
    {
	Semaphore sem = new Semaphore(2);
	DummyEntity e1 = new DummyEntity(10);
	DummyEntity e2 = new DummyEntity(20);
	DummyEntity e3 = new DummyEntity(30);

	assertTrue(sem.numberWaiting() == 0);

	Semaphore.Outcome result = sem.get(e1);

	assertTrue(result == Semaphore.Outcome.DONE);

	result = sem.get(e2);

	assertTrue(result == Semaphore.Outcome.DONE);

	result = sem.tryGet(e3);

	assertTrue(result == Semaphore.Outcome.WOULD_BLOCK);

	result = sem.get(e3);

	assertTrue(result == Semaphore.Outcome.DONE);
	assertTrue(sem.numberWaiting() == 1);
    }
}
