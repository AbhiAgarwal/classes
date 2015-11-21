from grpc.beta import implementations
import debate_pb2

_TIMEOUT_SECONDS = 10


def run():
    channel = implementations.insecure_channel('localhost', 50051)
    stub = debate_pb2.beta_create_Candidate_stub(channel)
    response = stub.Answer(debate_pb2.AnswerRequest(), _TIMEOUT_SECONDS)
    print "Greeter client received: " + response.answer

if __name__ == '__main__':
    run()
