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

import org.javasim.stats.Variance;
import org.junit.Test;

import static org.junit.Assert.*;

public class VarianceUnitTest
{
    @Test
    public void test () throws Exception
    {
        Variance vn = new Variance();
        
        vn.setValue(10.0);
        vn.setValue(20.0);
        
        double v = vn.variance();
        
        assertTrue(v > 0.0);
        assertTrue(vn.stdDev() == Math.sqrt(vn.variance()));
        
        vn.saveState("target/variance.temp");
        
        vn.reset();
        
        assertTrue(vn.variance() == 0.0);
        
        vn.restoreState("target/variance.temp");
        
        assertTrue(v == vn.variance());
    }
}
