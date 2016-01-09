/*
 * Copyright 1990-2010, Mark Little, University of Newcastle upon Tyne
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
 * (C) 1990-2010,
 */

package org.javasim.simset;

/*
 * This class defines the elements of the linked lists within SIMSET.
 */

// Thanks to Jim Bean for converting the C++SIM classes

/**
 * @deprecated  As of release 2.1 use Java's own linked lists.
 */

public class Link {

     public Link () {
        this.next = null;
        this.prev = null;
        this.theList = null;
        }

     /**
      * @return the successor to this list element.
      */
      
    synchronized public Link suc () {
        return next;
    }

     /**
      * @return the predecessor to this list element.
      */
      
    synchronized public Link pred () {
        return prev;
    }

     /**
      * @return this element in the list and remove it from the list.
      */
      
    synchronized public Link out () {
        RemoveElement ();
        return this;
    }

     /**
      * Add this entrt to the list.
      * 
      * @param list the list to add this element.
      */
      
    synchronized public void inTo (Head list) {
        if (list != null) {

            list.addLast (this);
            theList = list;
            return;

        }
    }

     /**
      * Add this list element to the same list as the element provided. Make
      * sure elements are only on one list.
      * 
      * @param toPrecede the element to add before in its list. If null, or the element
      * is not in a list, then remove this element from any list it may be in to
      * match.
      */
      
    synchronized public void precede (Link toPrecede) {
        if ((toPrecede == null) || ( ! toPrecede.inList()))

            RemoveElement();

        else {
            if (inList())
                RemoveElement();
            
            toPrecede.addBefore(this);
        }
    };

     /**
      * Add this list element to the same list as the element provided. Make
      * sure elements are only on one list.
      * 
      * @param toFollow the element to add after in its list. If null, or the element
      * is not in a list, then remove this element from any list it may be in to
      * match.
      */
      
    synchronized public void follow (Link toFollow) {
        if ((toFollow == null) || ( ! toFollow.inList()))

            RemoveElement();

        else {
            if (inList())
                RemoveElement();
            
            toFollow.addAfter(this);
        }
    }

     /**
      * Add this element to the head of a list.
      * 
      * @param list the list to use.
      */
      
    synchronized public void follow (Head list) {
        if (list != null)
            list.addFirst(this);
    }

     /**
      * @return <code>true</code> if this element is in a list, <code>false</code> otherwise.
      */
      
    synchronized public boolean inList () {
        return (boolean) (theList != null);
    }

    private void RemoveElement () {

        // can't have prev and next if we are not on a list
        if (theList == null)
            return;

        if (prev != null)
            prev.next = next;

        if (next != null)
            next.prev = prev;

        if (theList.first == this)
            theList.first = next;

        if (theList.last == this)
            theList.last = prev;

        theList = null;
        prev = null;
        next = null;
    }

    private void addAfter  (Link toAdd) {
        toAdd.prev = this;
        toAdd.theList = theList;

        if (next == null)
            next = toAdd;

        else {
            next.prev = toAdd;
            toAdd.next = next;
            next = toAdd;
        }

        if (theList.last == this)
            theList.last = toAdd;
    }

    private void addBefore (Link toAdd) {
        toAdd.theList = theList;
        toAdd.next = this;

        if (prev == null)
            prev = toAdd;

        else {
            prev.next = toAdd;
            toAdd.prev = prev;
            prev = toAdd;
        }

        if (theList.first == this)
            theList.first = toAdd;
    }

    protected Link next;
    protected Link prev;
    protected Head theList = new Head();
}
