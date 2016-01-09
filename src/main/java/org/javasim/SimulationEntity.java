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

package org.javasim;

public class SimulationEntity extends SimulationProcess
{
    public void finalize ()
    {
        super.finalize();
    }

    /**
     * Interrupt the given process (which *must* be in Wait or WaitFor), and
     * resume it. If immediate resumption is required then this process will be
     * suspended (placed back on to the scheduler queue for "immediate"
     * resumption when the interrupted process has finished).
     * 
     * @param toInterrupt the process to interrupt.
     * @param immediate specify whether immediate resumption is required.
     * @throws SimulationException if there is a problem.
     * @throws RestartException if the simulation has been restarted.
     */

    public void interrupt (SimulationEntity toInterrupt, boolean immediate)
            throws SimulationException, RestartException
    {
        if (toInterrupt.terminated())
            throw new SimulationException("Entity already terminated.");

        if (!toInterrupt._waiting)
            throw new SimulationException("Entity not waiting.");

        toInterrupt._interrupted = true;

        // remove from queue for "immediate" activation

        Scheduler.unschedule(toInterrupt); // remove from queue and prepare to
                                            // suspend

        // will take over when this process is suspended

        toInterrupt.reactivateAt(SimulationProcess.currentTime(), true);

        /*
         * Put "this" on to queue and suspend so that interrupted process can
         * run.
         */

        if (immediate)
            reactivateAt(SimulationProcess.currentTime());
    }

    /**
     * Trigger this instance.
     */
     
    public final void trigger ()
    {
        _triggered = true;
        _waiting = false;
    }

    /**
     * @return whether or not this instance is currently waiting.
     */
     
    public final boolean isWaiting ()
    {
        return _waiting;
    }
    
    /**
     * Must wake up any waiting process before we "die". Currently only a single
     * process can wait on this condition, but this may change to a list later.
     */

    public void terminate ()
    {
        /*
         * Resume waiting process before this one "dies".
         */

        if (_isWaitedOnBy != null)
        {
            // remove from queue for "immediate" activation

            try
            {
                _isWaitedOnBy.cancel();
                _isWaitedOnBy.reactivateAt(SimulationProcess.currentTime(), true);
            }
            catch (RestartException e)
            {
            }
            catch (SimulationException e)
            {
            }

            _isWaitedOnBy = null;
        }

        super.terminate();
    }

    protected SimulationEntity()
    {
        super();

        _isWaitedOnBy = null;
        _interrupted = _triggered = _waiting = false;
    }

    /**
     * Wait for specified period of time. If this process is interrupted then
     * the InterruptedException is thrown.
     * 
     * @param waitTime the time to wait.
     * @throws SimulationException thrown if an error occurs.
     * @throws RestartException thrown if the simulation has been restarted.
     * @throws InterruptedException thrown if this instance has been interrupted.
     */

    protected void timedWait (double waitTime) throws SimulationException,
            RestartException, InterruptedException
    {
        _waiting = true;

        try
        {
            hold(waitTime);
        }
        catch (SimulationException e)
        {
            throw new SimulationException("Invalid entity.");
        }

        _waiting = false;

        if (_interrupted)
        {
            _interrupted = false;
            throw new InterruptedException();
        }
    }

    /**
     * Suspends the current process until the process in the parameter has been
     * terminated. If the calling process is interrupted before the 'controller'
     * is terminated, then the InterruptedException is thrown. If the boolean
     * parameter is true then the controller is reactivated immediately.
     * 
     * @param controller the process upon whose termination this instance will be resumed.
     * @param reAct indicate whether or not the controlling process should be activated now.
     * @throws SimulationException thrown if an error occurs.
     * @throws RestartException thrown if the simulation has been restarted.
     * @throws InterruptedException thrown if this instance has been interrupted.
     */

    protected void waitFor (SimulationEntity controller, boolean reAct)
            throws SimulationException, RestartException, InterruptedException
    {
        if (controller == this) // can't wait on self!
            throw new SimulationException("WaitFor cannot wait on self.");

        controller._isWaitedOnBy = this; // resume when controller terminates

        // make sure this is ready to run

        try
        {
            if (reAct)
                controller.reactivateAt(SimulationProcess.currentTime(), true);
        }
        catch (SimulationException e)
        {
        }

        _waiting = true;

        // we don't go back on to queue as controller will wake us

        cancel();

        _waiting = _interrupted = false;

        // if we have been successful then terminated = true

        if (!controller.terminated())
            throw new InterruptedException();
    }

    /**
     * Suspends the current process until the process in the parameter has been
     * terminated. If the calling process is interrupted before the 'controller'
     * is terminated, then the InterruptedException is thrown. The controller
     * will not be reactivated immediately.
     * 
     * @param controller the process upon whose termination this instance will be resumed.
     * @throws SimulationException thrown if an error occurs.
     * @throws RestartException thrown if the simulation has been restarted.
     * @throws InterruptedException thrown if this instance has been interrupted.
     */

    protected void waitFor (SimulationEntity controller)
            throws SimulationException, RestartException, InterruptedException
    {
        waitFor(controller, false);
    }

    /**
     * The calling process is placed onto the trigger queue and should only be
     * restarted pending some application specific event which uses the trigger
     * queue. The InterruptedException is thrown if the caller is interrupted
     * rather than being triggered.
     * 
     * @param _queue the queue to place this process.
     * @throws SimulationException thrown if an error occurs.
     * @throws RestartException thrown if the simulation has been restarted.
     * @throws InterruptedException thrown if this instance has been interrupted.
     */

    protected void waitForTrigger (TriggerQueue _queue)
            throws SimulationException, RestartException, InterruptedException
    {
        _queue.insert(this);

        _interrupted = false;
        _waiting = true;

        cancel(); // remove from queue and suspend

        // indicate whether this was triggered successfully or interrupted

        if (_triggered)
            _triggered = false;
        else
            throw new InterruptedException();
    }

    /**
     * Currently, a process which is waiting on a semaphore cannot be
     * interrupted - its wait status is not set.
     */

    protected void waitForSemaphore (Semaphore _sem) throws RestartException
    {
        _sem.get(this);
    }

    private SimulationEntity _isWaitedOnBy;

    private boolean _interrupted;

    private boolean _triggered;

    private boolean _waiting;
}
