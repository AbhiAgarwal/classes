import time
import debate_pb2

_ONE_DAY_IN_SECONDS = 60 * 60 * 24


class Candidate(debate_pb2.BetaCandidateServicer):

    def Answer(self, request, context):
        print 'x'
        print request
        print request.question
        return debate_pb2.AnswerReply(message='Hello, %s!' % request.question)

    def Elaborate(self, request, context):
        print request
        print request.question
        return debate_pb2.ElaborateReply(message='Hello, %s!' % request.question)


def serve():
    server = debate_pb2.beta_create_Candidate_server(Candidate())
    server.add_insecure_port('[::]:50051')
    server.start()
    try:
        while True:
            time.sleep(_ONE_DAY_IN_SECONDS)
    except KeyboardInterrupt:
        server.stop(0)

if __name__ == '__main__':
    serve()
