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

package org.javasim.tests.stats;

import org.javasim.stats.Mean;
import org.junit.Test;

import static org.junit.Assert.*;

public class MeanUnitTest
{
    @Test
    public void test () throws Exception
    {
        final double MAX = 1000.0;
        final double MIN = 0.0;
        
        Mean mn = new Mean();
        
        mn.setValue(MAX);
        mn.setValue(MIN);
        
        assertTrue(mn.max() == MAX);
        assertTrue(mn.min() == MIN);
        assertEquals(mn.numberOfSamples(), 2);
        assertTrue(mn.sum() == MAX+MIN);
        assertTrue(mn.mean() == (MAX+MIN)/2);
        
        mn.saveState("target/mean.tmp");
        
        mn.reset();
        
        assertTrue(mn.mean() == 0.0);
        
        Mean theMean = new Mean();
        
        theMean.restoreState("target/mean.tmp");
        
        assertTrue(theMean.max() == MAX);
    }
}
