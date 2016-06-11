from flask import Flask, jsonify
from flask import abort
from flask import make_response
from flask import request
from flask import render_template
import logging
from Model.tasks import *
from Model.ShowHtmlTable import showHtmlTable
from flask import url_for
from flask.ext.httpauth import HTTPBasicAuth
auth = HTTPBasicAuth()
import uuid
from Model.heroes import *

#Testing with curl:
#curl -i -u miguel:python -H "Content-Type: application/json"  http://localhost:5000/todo/api/v1.0/tasks
#curl -i -u miguel:python  -H "Content-Type: application/json" -X POST -d "{""title"":""Read a book"", ""description"":""Read a description""}" http://localhost:5000/todo/api/v1.0/tasks
#curl -i -u miguel:python  -H "Content-Type: application/json" -X PUT -d "{""done"":true}" http://localhost:5000/todo/api/v1.0/tasks/2
#curl -i -u miguel:python  -H "Content-Type: application/json" -X DELETE  http://localhost:5000/todo/api/v1.0/tasks/2

#curl -i -u miguel:python -H "Content-Type: application/json"  http://localhost:5000/todo/api/v1.0/jobs
#curl -i -u miguel:python  -H "Content-Type: application/json" -X POST  http://localhost:5000/todo/api/v1.0/createjob

logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s (%(threadName)-2s) %(message)s',
                    )

#__name__="__main__"
app = Flask(__name__)



@auth.get_password
def get_password(username):
    if username == 'dexter':
        return 'python'
    return None

@auth.error_handler
def unauthorized():
    return make_response(jsonify({'error': 'Unauthorized access'}), 401)

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

@app.route('/todo/api/v1.0/tasks/<task_id>', methods=['PUT'])
def update_task(task_id):
    task = filter(lambda t: t['id'] == task_id, tasks)
    task = list(task)
    if len(task) == 0:
        abort(404)
    if not request.json:
        abort(400)
    if 'title' in request.json and type(request.json['title']) != unicode:
        abort(400)
    if 'description' in request.json and type(request.json['description']) is not unicode:
        abort(400)
    if 'done' in request.json and type(request.json['done']) is not bool:
        abort(400)
    task[0]['title'] = request.json.get('title', task[0]['title'])
    task[0]['description'] = request.json.get('description', task[0]['description'])
    task[0]['done'] = request.json.get('done', task[0]['done'])
    cond = task[0]['cond']
    with cond:
        cond.notifyAll()
    
    return jsonify({'task': taskToJson (task[0]) })

@app.route('/todo/api/v1.0/tasks/<task_id>', methods=['DELETE'])
def delete_task(task_id):
    task = filter(lambda t: t['id'] == task_id, tasks)
    task = list (task)
    if len(task) == 0:
        abort(404)
    tasks.remove(task[0])
    return jsonify({'result': True})

@app.route('/todo/api/v1.0/tasks', methods=['POST'])
def create_task():
    print (request.json)
    if not request.json or not 'title' in request.json:
      abort(400)
    task = {
        'id': tasks[-1]['id'] + 1,
        #'id': str(uuid.uuid1()),
        'title': request.json['title'],
        'description': request.json.get('description', ""),
        'done': False
    }
    tasks.append(task)
    return jsonify({'task': taskToJson (task) }), 201

@app.route('/todo/api/v1.0/htmltasks', methods=['GET'])
#@auth.login_required
def get_htmltasks():
    temp = map (make_public_task, tasks) ;
    temp = list (temp) #only needed for Python 3, map output is iterator but not a list
    
    #htmltable=showHtmlTable(temp)
    #return htmltable.__html__()
    return render_template('showTable.html',rows=temp)
    
@app.route('/todo/api/v1.0/tasks/<task_id>', methods=['GET'])
#@auth.login_required
def get_task(task_id):
    task = filter(lambda t: t['id'] ==task_id, tasks)
    #task = [elem for elem in tasks if elem['id']==task_id]
    #task=tasks[1]
    task = list(task) # required by Python 3
    if len(task) == 0:
        abort(404)
    return jsonify({'task': taskToJson (task) })

@app.route('/todo/api/v1.0/tasks', methods=['GET'])
#@auth.login_required
def get_tasks():
    #return jsonify({'tasks': tasks})
    temp = map (make_public_task, tasks) ;
    temp = list (temp) #only needed for Python 3, map output is iterator but not a list
    return jsonify( {'tasks': temp }  )

@app.route('/todo/api/v1.0/jobs', methods=['GET'])
def getJobs():
    js = make_public_job(JobDict)
    return jsonify( {'jobs': js }  )

@app.route('/todo/api/v1.0/createjob', methods=['POST'])
def createJobs():
    j = Job()
    j.postJob()
    return jsonify( {'job': JobDict[j.Id].toJson() }  )
    #return jsonify( {'job': "asdf" }  )
    #return jsonify( {'job': JobDict[j.Id].toJson() }  )
@app.route('/todo/api/v1.0/finishjob', methods=['PUT'])
def finishJobs():
    j = Job()
    j.postJob()
    
    return jsonify( {'job': JobDict[j.Id].toJson() }  )
def make_public_job(jobDict):
    new_Jobs={}
    for job_tuple in JobDict.items():
        print (job_tuple)
        newjob=job_tuple[1].toJson()
        new_Jobs[str(job_tuple[0])]=newjob
    return new_Jobs

def make_public_task(task):
    new_task = {}
    for field in task:
        if field == 'id':
            new_task['uri'] = url_for('get_task', task_id=task['id'], _external=True)
            new_task['id'] = task[field]
        else:
            new_task[field] = task[field]
    return new_task

import json
# get this object
from flask import Response
@app.route('/heroes', methods=['GET'])
def get_heros():
    #return jsonify({'tasks': tasks})
    #return jsonify( {'result': heroes}  )
    return Response(json.dumps(heroes),  mimetype='application/json')

@app.route('/heroMain', methods=['GET'])
def get_htmlheroMain():
    return render_template('index.html')

if __name__ == '__main__':
    app.run(debug=True,host='0.0.0.0',port=8080)
