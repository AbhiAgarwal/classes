import time
import sys
import BaseHTTPServer
from string import Template

HOST_NAME = '0.0.0.0'
FILE_PATH = ''
URL = 'Unknown'

HTML = """
<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi?autoload={'modules':[{'name':'visualization','version':'1','packages':['timeline']}]}"></script>
    <!--Load the AJAX API-->
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript" src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <script type="text/javascript">
    
    // Load the Visualization API and the annotationchart package.
    google.load('visualization', '1', {'packages':['timeline']});
      
    // Set a callback to run when the Google Visualization API is loaded.
    google.setOnLoadCallback(drawChart);
      
    function drawChart() {
      var jsonData = $$.ajax({
          url: "getData",
          dataType:"json",
          async: false
          }).responseText;
          
      var evalledData = eval("("+jsonData+")");
      // Create our data table out of JSON data loaded from server.
      var data = new google.visualization.DataTable(evalledData);

      // Instantiate and draw our chart, passing in some options. 
      var chart = new google.visualization.Timeline(document.getElementById('chart_div'));
      chart.draw(data);
    }

    </script>
  </head>
  <body>
    <h1>Uptime and HTTP codes for server: $url</h1>
    <div id='chart_div' style='width: 900px; height: 500px;'></div>
  </body>
</html>
"""

def intervals_from_file(FILE_PATH):
  uptime_data = []
  last_code = None
  start_time = None
  last_time = None
  with open(FILE_PATH, 'r') as uptime_file:
    for line in uptime_file.readlines():
      if line.startswith('#'):
        continue
      if line.startswith('URL='):
        continue
      cur_time_sec, code = line.strip().split(',')
      if code == '-1':
        code = 'Down'
      cur_time = str(int(cur_time_sec) * 1000)
      if not start_time:
        start_time = cur_time
      if last_code and code != last_code:
        uptime_data.append([start_time, last_time, last_code])
        start_time = last_time
      last_time = cur_time
      last_code = code
  # Handle last code case.
  if start_time:
    uptime_data.append([start_time, last_time, last_code])
  return uptime_data

JSON_TEMPLATE = """
{
  cols: [{label: 'HTTP Code', type: 'string'},
         {label: 'Start', type: 'date'},
         {label: 'End', type: 'date'}
  ],
  rows: [$rows
  ]     
}
"""

def intervals_to_json(intervals):
  row_str = ''
  for start, end, code in intervals:
    row_str += "\n    {c:[{v: '%s'}, {v: new Date(%s)}, {v: new Date(%s)}]}," \
            % (code, start, end)
  s = Template(JSON_TEMPLATE)
  t = s.substitute(rows=row_str)
  return t

def send_data(s):
  uptime_intervals = intervals_from_file(FILE_PATH)
  uptime_json = intervals_to_json(uptime_intervals)
  s.wfile.write(uptime_json)

class MyHandler(BaseHTTPServer.BaseHTTPRequestHandler):
  def do_HEAD(s):
    s.send_response(200)
    s.send_header("Content-type", "text/html")
    s.end_headers()

  def do_GET(s):
    """Respond to a GET request."""
    s.send_response(200)
    s.send_header("Content-type", "text/html")
    s.end_headers()
    if s.path == "/getData":
      send_data(s)
    else:
      t = Template(HTML)
      s.wfile.write(t.substitute(url=URL))

def main(argv):
  if len(argv) != 3:
    print("Usage %s <port> <samples_file>" % argv[0])
    sys.exit(-1)
  port = int(argv[1])
  global FILE_PATH
  FILE_PATH = argv[2]
  global URL
  with open(FILE_PATH, 'r') as uptime_file:
    for line in uptime_file.readlines():
      if line.startswith('URL='):
        _, URL = line.split('=')
  server_class = BaseHTTPServer.HTTPServer
  httpd = server_class((HOST_NAME, port), MyHandler)
  print time.asctime(), "Server Starts - %s:%s" % (HOST_NAME, port)
  try:
    httpd.serve_forever()
  except KeyboardInterrupt:
    pass
  httpd.server_close()
  print time.asctime(), "Server Stops - %s:%s" % (HOST_NAME, port)

if __name__ == '__main__':
  main(sys.argv)
