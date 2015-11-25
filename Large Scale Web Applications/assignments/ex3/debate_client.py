from grpc.beta import implementations
from grpc import framework
import debate_pb2


def run(choice, input_values, timeout=10):
    channel = implementations.insecure_channel('localhost', 50051)
    stub = debate_pb2.beta_create_Candidate_stub(channel)
    try:
        if choice == 'answer':
            response = stub.Answer(debate_pb2.AnswerRequest(
                question=input_values['question'], timeout=timeout), timeout)
            return response.answer
        elif choice == 'elaborate':
            response = stub.Elaborate(debate_pb2.ElaborateRequest(
                topic=input_values['topic'], blah_run=input_values['blah_run']), timeout)
            return response.answer
    except framework.interfaces.face.face.ExpirationError:
        return 'timeout'
