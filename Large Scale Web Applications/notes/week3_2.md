## Apache vs Nginx (Mini-tech-talk)

### Apache

- Process driven
    - Spawns new process for each connection
- Nginx uses the Reactor pattern. 
    - It's single-threaded (but can fork several processes to utilize multiple cores). The main event loop waits for the OS to signal a readiness event - e.g. that data is available to read from a socket, at which point it is read into a buffer and processed. 
    - The single thread can very efficiently serve tens of thousands of simultaneous connections (the thread-per-connection model would fail at this because of the huge context-switching overhead, as well as the large memory consumption, as each thread needs its own stack).
- Good for data processing/calculation based requirements.

### Nginx

- Good for I/O dense requirements.
- Has better [throughput](https://en.wikipedia.org/wiki/Throughput) when content is static.
- [How To Optimize Nginx Configuration](https://www.digitalocean.com/community/tutorials/how-to-optimize-nginx-configuration)
- [Design pattern](http://stackoverflow.com/questions/3436808/how-does-nginx-handle-http-requests)

### Django running with a server on the front

- Gunicorn
- Apache
- Apache + mod_wsgi
- [Gunicorn and nginx together](http://stackoverflow.com/questions/20163233/deploying-django-project-with-gunicorn-and-nginx/20529091#20529091)

### Linux Kernel 

- You can be blocking. Linux Kernel has methods to tell you when your file is ready to read or write or your process is ready.

#### nginx and gunicorn

- nginx configuration files (/etc/nginx/sites-enabled/ and /etc/nginx/nginx.conf)
- gunicorn configuration files (/etc/init/gunicorn.conf and /etc/gunicorn.d/) 


## General notes

- TCP (Transmission control protocol): 
- TCP/IP. TCP is a level above IP.
- Open a new TCP connection with the server at an IP/Port.
- TCP connection takes 3 handshakes to establish.
    - http://www.cisco.com/web/about/ac123/ac147/images/ipj/ipj_9-4/94_syn_fig1_lg.jpg
