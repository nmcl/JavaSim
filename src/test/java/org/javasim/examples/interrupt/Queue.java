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

import java.util.NoSuchElementException;

public class Queue
{
    public Queue()
    {
        head = null;
        length = 0;
    }

    public boolean isEmpty ()
    {
        if (length == 0)
            return true;
        else
            return false;
    }

    public long queueSize ()
    {
        return length;
    }

    public Job dequeue () throws NoSuchElementException
    {
        if (isEmpty())
            throw new NoSuchElementException();

        List ptr = head;
        head = head.next;

        length--;

        return ptr.work;
    }

    public void enqueue (Job toadd)
    {
        if (toadd == null)
            return;

        List ptr = head;

        if (isEmpty())
        {
            head = new List();
            ptr = head;
        }
        else
        {
            while (ptr.next != null)
                ptr = ptr.next;

            ptr.next = new List();
            ptr = ptr.next;
        }

        ptr.next = null;
        ptr.work = toadd;
        length++;
    }

    private List head;

    private long length;
}

/* This is the queue on which Jobs are placed before they are used. */

class List
{
    public List()
    {
        work = null;
        next = null;
    }

    public Job work;

    public List next;
}
