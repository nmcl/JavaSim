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

package org.javasim.tests.examples.streams;

import org.javasim.stats.Histogram;
import org.javasim.streams.TriangularStream;
import org.javasim.examples.streams.*;

import org.junit.Test;

public class TriangularExampleStreamTest {
  @Test
  public void test() {
    try {

      TriangularStream triangular = new TriangularStream(0, 20, 7);

      Histogram hist = new Histogram(25);

      for (int i = 0; i < 10000; i++) {
        int value = (int) Math.round(triangular.getNumber());

        System.out.println(" " + value);

        hist.setValue(value);
      }

      System.out.println("RandomStream error: " + triangular.error());

      hist.print();

    } catch (final Throwable ex) {
    }
  }
}
