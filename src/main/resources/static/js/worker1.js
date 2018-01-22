/**
 * 
 */
//importScripts('scripts.js');
var workerId;
var stopWorker = 0;
var json ;
var script = '';
var inputData = {'id':-1,'data':null};
function getNewData(){
	postMessage({'id': workerId, 'msg': "getPackage"});
}
function returnData(dat){
	postMessage({'id': workerId, 'msg': "returnData", 'data': dat});
}
function checkStart(){
	if (script == json['skrypt']&& inputData['id']==json['idZadanie'] ){
				return eval(json['zadanie'])(json, inputData['data']);	//możliwe bez użycia eval(niezbyt bezpieczne)
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
			json = JSON.parse(msg.msg);
			json = msg.msg;
			if(json['skrypt']!= script){
				script = json['skrypt'];				
				importScripts(json['skrypt']);
			}
			if(inputData.id!=json['idZadanie']){
				postMessage({'id':workerId, 'msg': "getInputData", 'data':{'taskId':json['idZadanie'],'path':json['sciezka_dane_wejsciowe']}});
			}
			checkStart();
			break;
      	case 'getInputData':
      		if(json['skrypt']!= script){
				script = json['skrypt'];				
				importScripts(json['skrypt'])
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