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

import org.javasim.stats.Quantile;
import org.javasim.stats.Variance;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuantileUnitTest
{
    @Test
    public void test () throws Exception
    {
        Quantile q;
        
        try
        {
            q = new Quantile(1.1);
            
            fail();
        }
        catch (final Exception ex)
        {
        }
        
        try
        {
            q = new Quantile(-1.1);
            
            fail();
        }
        catch (final Exception ex)
        {
        }
        
        q = new Quantile();
        
        assertTrue(q.range() == 0.95);
        
        for (int i = 0; i < 100; i++)
            q.setValue(i);
        
        assertTrue(q.getValue() == 94.0);
    }
}
