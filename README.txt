This is a pool of disk connections, or in other words a wrapper around 
the regular java disk io api (File, Directory exc) so we can keep track
of how many connections to the file system are being used in a jvm.