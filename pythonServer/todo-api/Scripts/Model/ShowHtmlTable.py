from flask_table import Table, Col

from Model.tasks import tasks
#Declare the table
class TaskTable(Table):
    id=Col('id')
    title=Col('title')


def showHtmlTable(mytask):
    table=TaskTable(mytask)
    return table