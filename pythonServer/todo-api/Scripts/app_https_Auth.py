from flask import Flask, jsonify
from flask import abort
from flask import make_response
from flask import request
import logging
from flask import url_for
from flask.ext.httpauth import HTTPBasicAuth
from Model.tasks import tasks

auth = HTTPBasicAuth()

#Testing with curl:
#Parameter with -k : skip the certificate verification
#curl -k -i -u miguel:python -H "Content-Type: application/json"  https://localhost:443/todo/api/v1.0/tasks
#curl -k -i -u miguel:python -H "Content-Type: application/json" -X POST -d "{""title"":""Read a book"", ""description"":""Read a description""}" https://localhost:443/todo/api/v1.0/tasks
#curl -k -i -u miguel:python -i -H "Content-Type: application/json" -X PUT -d "{""done"":true}" https://localhost:443/todo/api/v1.0/tasks/2
#curl -k -i -u miguel:python -i -H "Content-Type: application/json" -X DELETE  https://localhost:443/todo/api/v1.0/tasks/2


logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s (%(threadName)-2s) %(message)s',
                    )

#__name__="__main__"
app = Flask(__name__)

#tasks = [
#    {
#        'id': 1,
#        'title': u'Buy groceries',
#        'description': u'Milk, Cheese, Pizza, Fruit, Tylenol', 
#        'done': False
#    },
#    {
#        'id': 2,
#        'title': u'Learn Python',
#        'description': u'Need to find a good Python tutorial on the web', 
#        'done': False
#    }
#]

@auth.get_password
def get_password(username):
    if username == 'miguel':
        return 'python'
    return None

@auth.error_handler
def unauthorized():
    return make_response(jsonify({'error': 'Unauthorized access'}), 401)

@app.errorhandler(404)
def not_found(error):
    return make_response(jsonify({'error': 'Not found'}), 404)

@app.route('/todo/api/v1.0/tasks/<int:task_id>', methods=['PUT'])
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
    return jsonify({'task': task[0]})

@app.route('/todo/api/v1.0/tasks/<int:task_id>', methods=['DELETE'])
def delete_task(task_id):
    task = filter(lambda t: t['id'] == task_id, tasks)
    task = list (task)
    if len(task) == 0:
        abort(404)
    tasks.remove(task[0])
    return jsonify({'result': True})

@app.route('/todo/api/v1.0/tasks', methods=['POST'])
def create_task():
    print ("Running");
    print ("Running with:",request);
    if not request.json or not 'title' in request.json:
        abort(400)
    task = {
        'id': tasks[-1]['id'] + 1,
        'title': request.json['title'],
        'description': request.json.get('description', ""),
        'done': False
    }
    tasks.append(task)
    return jsonify({'task': task}), 201

@app.route('/todo/api/v1.0/tasks/<int:task_id>', methods=['GET'])
@auth.login_required
def get_task(task_id):
    task = filter(lambda t: t['id'] ==task_id, tasks)
    #task = [elem for elem in tasks if elem['id']==task_id]
    #task=tasks[1]
    task = list(task) # required by Python 3
    if len(task) == 0:
        abort(404)
    return jsonify({'task': task})

@app.route('/todo/api/v1.0/tasks', methods=['GET'])
@auth.login_required
def get_tasks():
    #return jsonify({'tasks': tasks})
    temp = map (make_public_task, tasks) ;
    temp = list (temp) #only needed for Python 3, map output is iterator but not a list
    return jsonify( {'tasks': temp }  )

def make_public_task(task):
    new_task = {}
    for field in task:
        if field == 'id':
            new_task['uri'] = url_for('get_task', task_id=task['id'], _external=True)
        else:
            new_task[field] = task[field]
    return new_task

sslinfo = ('C:/Download/lab/todo-api/flask/server.crt','C:/Download/lab/todo-api/flask/server.key')

if __name__ == '__main__':
    app.run(debug=True,port=443, ssl_context=('C:/Download/lab/todo-api/flask/server.crt','C:/Download/lab/todo-api/flask/server.key') )
