from debate_client import run
import sys

if __name__ == '__main__':
    if len(sys.argv) is 1:
        print 'python moderator.py <answer|elaborate> "question" [timeout] [blah_run...]'
        print 'Examples:'
        print '\tpython moderator.py answer "why should you win" 25'
        print '\tpython moderator.py elaborate taxes 1'
        print '\tpython moderator.py elaborate "foreign policy" 2 3'
    else:
        choice = sys.argv[1]
        if choice == 'answer':
            if len(sys.argv) >= 4:
                question = sys.argv[2]
                timeout = sys.argv[3]
                try:
                    timeout = int(timeout)
                except ValueError:
                    raise ValueError('Timeout is not a number')
                print run('answer', {
                    'question': question,
                }, timeout)
            else:
                print 'Missing question or timeout'
        elif choice == 'elaborate':
            if len(sys.argv) >= 3:
                topic = sys.argv[2]
                blah_run = 0
                if len(sys.argv) > 3:
                    blah_run = sys.argv[3:]
                    blah_run = [int(i) for i in blah_run]
                print run('elaborate', {
                    'topic': topic,
                    'blah_run': blah_run,
                }, 25)
            else:
                print 'Missing topic or blah_run'
        else:
            print 'Choose either answer or elaborate'
