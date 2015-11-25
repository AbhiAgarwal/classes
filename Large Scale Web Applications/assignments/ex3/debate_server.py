from grpc.beta import implementations
from grpc import framework

import time
import debate_pb2
import consultation_pb2
import random

_ONE_DAY_IN_SECONDS = 60 * 60 * 24


def coin_flip():
    return 1 if random.random() < 0.2 else 0


def get_retort(question, timeout):
    channel = implementations.insecure_channel('54.88.18.92', 50051)
    stub = consultation_pb2.beta_create_CampaignManager_stub(channel)
    try:
        response = stub.Retort(consultation_pb2.RetortRequest(
            original_question=question), timeout + 10)
        return response.retort
    except framework.interfaces.face.face.ExpirationError:
        return 'no comment'


class Candidate(debate_pb2.BetaCandidateServicer):

    def Answer(self, request, context):
        acceptable_question_starts = ['why', 'what', 'how', 'who', 'when']
        question = request.question.lower().split(' ')
        return_answer = ''
        if question[0] in acceptable_question_starts:
            question_sub = request.question.replace('You', 'I')
            question_sub = question_sub.replace('your', 'my')
            question_sub = question_sub.lower()
            retort = get_retort(request.question, request.timeout)
            return_answer = 'You asked me ' + question_sub + \
                ' but I want to say that ' + retort
            if retort == 'no comment':
                return_answer = retort
        else:
            answer_options = [
                'your 3 cent titanium tax goes too far',
                'your 3 cent titanium tax doesn\'t go too far enough'
            ]
            flip_value = coin_flip()
            return_answer = answer_options[flip_value]
        return debate_pb2.AnswerReply(answer=return_answer)

    def Elaborate(self, request, context):
        if len(request.blah_run) is 0:
            return debate_pb2.ElaborateReply(answer=request.topic.lower())
        else:
            return_string = ''
            if len(request.blah_run) is 1:
                return_string += ('blah ' *
                                  request.blah_run[0]) + request.topic.lower()
            else:
                for i in range(0, len(request.blah_run)):
                    if i is len(request.blah_run) - 1:
                        return_string += ('blah ' * request.blah_run[i])
                    else:
                        return_string += ('blah ' *
                                          request.blah_run[i]) + request.topic.lower() + ' '
            return debate_pb2.ElaborateReply(answer=return_string)


def serve():
    candidate_server = debate_pb2.beta_create_Candidate_server(
        Candidate())
    candidate_server.add_insecure_port('[::]:50051')
    candidate_server.start()

    try:
        while True:
            time.sleep(_ONE_DAY_IN_SECONDS)
    except KeyboardInterrupt:
        candidate_server.stop(0)

if __name__ == '__main__':
    serve()
