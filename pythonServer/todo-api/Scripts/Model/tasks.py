import logging
import uuid
import threading
tasks = [
    {
        'id': 1,
        'title': u'Buy groceries',
        'description': u'Milk, Cheese, Pizza, Fruit, Tylenol', 
        'done': False
    },
    {
        'id': 2,
        'title': u'Learn Python',
        'description': u'Need to find a good Python tutorial on the web', 
        'done': False
    }
]

def taskToJson(task):
    return {
                'id':task['id'],
                'title':task['title'],
                'description':task['description'],
                'done':task['done']
            }

JobDict = {}

class Job:
    
    def __init__(self):
        self.Id=str(uuid.uuid1())
        self.Finish=False
        self.mytasks=[]
        self.MonitorThread=None
        self.finishCond=threading.Condition() #Trigger event when job finish
        
    def toJson(self):
        return {
                'Id':self.Id,
                'Finish':self.Finish,
                'tasks':self.mytasks
            }
        
    def postJob(self):
        JobDict[self.Id] = self
        taskid=str(uuid.uuid1())
        task = {
        'id': taskid,
        'title': 'Job1',
        'description': 'description1',
        'done': False,
        'cond':threading.Condition()
        }
        tasks.append(task)
        self.mytasks.append(taskid)
        taskid=str(uuid.uuid1())
        task = {
        'id': taskid,
        'title': 'Job2',
        'description': 'description2',
        'done': False,
        'cond':threading.Condition()
        }
        tasks.append(task)
        self.mytasks.append(taskid)
        self.MonitorThread = threading.Thread(name=self.Id, target=self.waitForTaskFinishThread, args=())
        self.MonitorThread.start()
        
    def finishJob(self):
        self.Finish=True

    
    def waitForTaskFinishThread(self):
        print(self.Id + ":Waiting for tasks finish")
        for task_id in self.mytasks:
            task = filter(lambda t: t['id'] ==task_id, tasks)
            cond=task[0]['cond']
            with cond:
                cond.wait()
                print(task[0]['id'] + "finished running")
        print (self.Id+":Finished running")
        self.finishJob()
        #All tasks finish
        #Trigger the job finish event
        with self.finishCond:
            self.finishCond.notifyAll()
            
