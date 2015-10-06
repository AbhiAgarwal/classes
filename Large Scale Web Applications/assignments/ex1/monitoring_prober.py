'''
A prober is a simple application that runs on a client machine and issues
periodic requests to a production server to check whether it is up and
operational. A prober allows you to check the end-to-end service of a Web
service.

The prober must be invoked with the following two command lines arguments:
    - <hostname>: any hostname format that can be interpreted by a browser. For example www.facebook.com
    - <samples_file>: a path to a file that will be created and will contain the probing samples for this invocation. One line per sample.
'''

import sys, time, httplib, threading

def get_status_from_url(host_name):
    # Gets response status from the host_name.
    # If it's not responsive for 30 seconds then it will return -1.
    try:
        connection = httplib.HTTPConnection(host_name, 80, timeout=30)
        connection.request('GET', '/')
        response = connection.getresponse()
        return response.status
    except:
        return -1

def get_time_and_status(host_name):
    # Returns current time and the response from the URL
    current_time = str(int(time.time()))
    url_response = str(get_status_from_url(host_name))
    return [current_time, url_response]

def monitor(host_name, record):
    monitor_thread = threading.Timer(30, monitor, [host_name, record])
    monitor_thread.daemon = True
    monitor_thread.start()
    # Gets the current time and status and appends it to the record file
    to_print = ','.join(get_time_and_status(host_name))
    print to_print
    record.write(to_print + '\n')
    record.flush()

def prober(host_name, sample_file):
    # Opens the file to record
    record = open(sample_file, 'w')
    record.write('URL=' + host_name + '\n')
    # Begins monitoring
    monitor(host_name, record)
    # Run program until interrupt
    try:
        while True:
            time.sleep(0.1)
    except KeyboardInterrupt:
        record.close()

if __name__ == '__main__':
    print 'Prober'
    if len(sys.argv) is 3 and sys.argv[1] and sys.argv[2]:
        host_name = sys.argv[1]
        sample_file = sys.argv[2]
        print host_name, sample_file
        prober(host_name, sample_file)
    else:
        print 'Arguments:'
        print '\tHostname: Any hostname format that can be interpreted by a browser. For example www.facebook.com.'
        print '\tSample File path: A path to a file that will be created and will contain the probing samples for this invocation. One line per sample.'