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

import org.javasim.SimulationProcess;

public class SimulationProcessIterator
{
    public SimulationProcessIterator(SimulationProcessList L)
    {
        ptr = L.Head;
    }

    public final synchronized SimulationProcess get ()
    {
        if (ptr != null)
        {
            SimulationProcessCons p = ptr;
            ptr = ptr.cdr();
            return p.car();
        }

        return null;
    }

    private SimulationProcessCons ptr;

};

class SimulationProcessCons
{

    public SimulationProcessCons(SimulationProcess p, SimulationProcessCons n)
    {
        Proc = p;
        Next = n;
    }

    public final SimulationProcess car ()
    {
        return Proc;
    }

    public final SimulationProcessCons cdr ()
    {
        return Next;
    }

    public final void setfCdr (SimulationProcessCons n)
    {
        Next = n;
    }

    private SimulationProcess Proc;

    private SimulationProcessCons Next;
}
