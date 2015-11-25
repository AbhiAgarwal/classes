# README.txt contains instructions how to build and run your code.

# TODO: document how to invoke protoc in order to generate the stubs in your
# language of choice.

protoc --python_out=. --grpc_out=. --plugin=protoc-gen-grpc=`which grpc_python_plugin` consultation.proto
protoc --python_out=. --grpc_out=. --plugin=protoc-gen-grpc=`which grpc_python_plugin` debate.proto

# TODO: document how to build your server and client code (if applicable)
# YOUR INSTRUCTIONS GO HERE

Not required

# TODO: document how run your server (on localhost)
# YOUR INSTRUCTIONS GO HERE

python debate_server.py
python moderator.py <commands>
