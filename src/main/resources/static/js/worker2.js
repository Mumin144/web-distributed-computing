/**
 * 
 */
//importScripts('scripts.js');
var workerId;
var stopWorker = 0;
var task;
var taskData;
var script = '';
var inputData = {'id':-1,'data':null};
function getNewData(){
	postMessage({'id': workerId, 'msg': "getPackage"});
}
function returnData(dat){	
	task.data=dat;
	postMessage({'id': workerId, 'msg': "returnData", 'data': task});
	task="";
	taskData="";
}
function checkStart(){
	//returnData(json);
	/*if (script == json['skrypt']&& inputData['id']==json['idZadanie'] ){				
				return eval(json['zadanie'])(json, inputData['data']);	//możliwe bez użycia eval(niezbyt bezpieczne)
	}*/
	if (script == taskData[1] &&  inputData.id==task['taskId']){				
		return eval(taskData[2])(taskData[0] , inputData.data);	//możliwe bez użycia eval(niezbyt bezpieczne)
	}
	return null;
}
self.addEventListener('message', function(e) {
  var msg = e.data;
  switch (msg.cmd) {
		case 'setId':
			workerId = msg.msg;
			break;
    	case 'start':
            stopWorker = 0;
			postMessage({'id': workerId, 'msg': "getPackage"});
			break;
		case 'getPackage':			
			task = msg.msg;
			taskData = JSON.parse(task.data);			
			if(taskData[1]!= script){
				script = taskData[1];				
				importScripts(script);
			}
			if(inputData.id!=taskData['taskId']){
				postMessage({'id':workerId, 'msg': "getInputData", 'taskId':task['taskId']});
			}
			checkStart();
			break;
      	case 'getInputData':      		
      		inputData = {'id':msg.taskId ,'data':msg.data};

          	checkStart();
          	break;
    	case 'stop':
            stopWorker = 1;
			//close(); // Terminates the worker.
      		break;
    	default:
			postMessage('Unknown command: ' + msg.msg);
  };	
}, false);