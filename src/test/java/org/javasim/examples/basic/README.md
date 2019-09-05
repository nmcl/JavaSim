This is an example of a simulation written using the simulation package. The example is taken from the book by Isi Mitrani (Simulation Techniques for Discrete Event Systems p22).

The simulation is of a service which attempts to execute as many requests for jobs as possible. The job requests are queued until the service can deal with them. However, the service is prone to failures, and so jobs started will be delayed until the service has been reactivated.

The classes provided include:

Arrivals - This class controls the rate at which Jobs arrive at
	   the service (Machine)

Breaks - This class controls the availability of the Machine by
	 "killing" it and restarting it at intervals drawn from
	 a Uniform distribution.

Job - This class represents the jobs which the Machine must process.

Machine - This is the Machine on which the service resides. It obtains
	  jobs from the job queue for the service and then attempts to
	  execute them. The machine can fail and so the response time for
	  jobs is not guaranteed to be the same.

MachineShop - This is the main part of the simulation which starts the
	      various processes (Scheduler, Arrivals, Machine, Job)
	      involved. It also prints out statistics for the response time
	      for the jobs.

Queue - This represents the queue which Jobs are placed on prior to being
	used by the Machine (service).

Main - This is the body of the program which initializes the threads package
       prior to the simulation starting.
