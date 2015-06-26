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

import org.javasim.stats.Bucket;
import org.junit.Test;

import static org.junit.Assert.*;

public class BucketUnitTest
{
    @Test
    public void test () throws Exception
    {
        Bucket b1 = new Bucket(20.0);
        Bucket b2 = new Bucket(20.0, 1000);
        
        assertEquals(b2.size(), (long) 1000);
        
        assertTrue(b1.equals(b2.name()));
        
        Bucket b3 = new Bucket(b1);
        
        assertTrue(b3.equals(b1.name()));
        
        Bucket b4 = new Bucket(40.0);
        
        assertTrue(b4.greaterThan(b2.name()));
        assertTrue(b4.greaterThanOrEqual(b4.name()));
        
        assertTrue(b1.lessThan(b4.name()));
        assertTrue(b1.lessThanOrEqual(b1.name()));
    }
}
