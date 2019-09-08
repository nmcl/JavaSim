This is a basic example of the new interrupt mechanisms added in version 1.5.

A Processor object has two message queue, one for general messages and one for interrupts (signals). The processor will wait for a set period of time and will then inspect its general message queue for work to be done. This will be performed and then it will go back to waiting. A Signal object will periodically wake up and send a signal to the processor and place a message in to its signal queue.
