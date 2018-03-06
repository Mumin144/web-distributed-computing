/**
 * 
 */
class WorkerController{
    constructor(displayId,autoStart){
        if (typeof(Worker) !== "undefined") {  // sprawdzenie wspieralności webworker
            this.threads = navigator.hardwareConcurrency; // ilość wątków
            this.displayId = displayId; // id elementu wyświetlającego
            this.workerJs = '/js/worker2.js';
            //this.workerJs = 'js/worker1.js';
            this.workers = [];
            this.queue =[];
            this.queueInputData=[];
            this.inputDataBusy=0;
            this.assigments =[];
            this.inputData=[];
            for (let i=0; i<this.threads; i++){
                this.workers.push(new Worker(this.workerJs));
                $('#'+this.displayId).append(
                    "<div id='workerDisplay"+i+"'>Wątek "+i+" "+
                    "<i class='fa fa-cog ' ></i><br>"+
                    "Policzono: <i id='packageCounter"+i+"'>0</i><br>"+
                    "<button class='btn-success' id='worker"+i+"Start'>Start</button>"+
                    "<button class='btn-warning' id='worker"+i+"Stop'>Stop</button>"+
                    "<button class='btn-danger' id='worker"+i+"Kill'>Kill</button>"+
                    "</div>");
                $('#'+this.displayId).on('click','#worker'+i+'Start', function(){
                    ctrl.startWorker(i);
                })
                $('#'+this.displayId).on('click','#worker'+i+'Stop', function(){
                    ctrl.stopWorker(i);
                })
                $('#'+this.displayId).on('click','#worker'+i+'Kill', function(){
                    ctrl.killWorker(i);
                })
                this.workers[i].addEventListener('message', function(e) {//TO DO
                    ctrl.eventListener(e.data);
                }, false);
                this.workers[i].postMessage({'cmd': 'setId', 'msg': i});
            }
            $('#'+this.displayId).append(
                "<div id='allDisplay'>Całość<br>"+
                "Policzono: <i id='packageCounter'>0</i><br>"+
                "<button class='btn-success' id='allStart'>Start</button>"+
                "<button class='btn-warning' id='allStop'>Stop</button>"+
                "<button class='btn-danger' id='allKill'>Kill</button>"+
                "</div>");
            $('#'+this.displayId).on('click','#allStart', function(){
                ctrl.startWorkerAll();
            })
            $('#'+this.displayId).on('click','#allStop', function(){
                ctrl.stopWorkerAll();
            })
            $('#'+this.displayId).on('click','#allKill', function(){
                ctrl.killWorkerAll();
            })
            $(window).on('unload', function(){
            	ctrl.killWorkerAll();
            });
            if(autoStart){
                this.startWorkerAll();
            }
        }
        else {
            document.getElementById(displayId).value = "przeglądarka nie jest wspierana";
        }
    }
    startWorker(id){
        this.workers[id].postMessage({'cmd': 'start', 'msg': ''});
    }
    startWorkerAll(){
        for (let i=0; i<this.workers.length; i++){
            if (this.workers[i]!==null){
                this.startWorker(i);
            }
        }
    }
    stopWorker(id){ // zakończ po wykonaniu zadania
        this.workers[id].postMessage({'cmd': 'stop', 'msg': ''});
        this.dequeueTask(id);
    }
    stopWorkerAll(id){ // zakończ po wykonaniu zadania
        for (let i=0; i<this.workers.length; i++){
            if (this.workers[i]!==null){
                this.stopWorker(i);
            }
        }
    }
    killWorker(id){
        this.workers[id].terminate();
        if (this.assigments[id]!= null){
            this.cancelTask(id);
        }
        $('#workerDisplay'+id+' button').remove();
        $('#workerDisplay'+id+' .fa-cog:first').removeClass('fa-spin');
        this.workers[id]=null;
        this.dequeueTask(id);
    }
    killWorkerAll(){ // zakończ po wykonaniu zadania
        for (let i=0; i<this.workers.length; i++){
            if (this.workers[i]!==null){
                this.killWorker(i);
            }
        }
    }
    enqueueTask(id){//kolejkowanie zadanań do pobrania
        if (this.queue.length ===0)
        {
            this.queue.push(id);
            this.getTask();
        }
        else{
            this.queue.push(id);
        }
    }
    dequeueTask(id){
        let index = this.queue.indexOf(id);
        if(index!==-1){
            this.queue.splice(index,1);
        }
    }
    getTask(){
        if(this.queue.length>0){
        	this.getTaskBusy=1;
			var id = ctrl.queue.pop();
			$.ajax({
		   		  type: "GET",
		   		  url: "/wdc/getPackage",
		   		  success: function(data){	 
		   			//ctrl.assigments[id] = data['id'];
		   			ctrl.assigments[id] = data;
                    ctrl.toggleCog(id);
                    ctrl.workers[id].postMessage({'cmd': 'getPackage', 'msg': data});
                    console.log("send to task "+data.id);
                    if (ctrl.queue.length !==0)
                    {
                        ctrl.getTask();
                    }                    
		   		  },	 
		   		  error: function(){
		   			ctrl.queue.push(id);
		   			setTimeout(function(){ctrl.getTask()}, 10000);
		   		  }
	   		});
        }
    }
    cancelTask(id){
    	$.ajax({
	   		  type: "POST",
	   		  url: "/wdc/cancelPackage",
	   		  data: JSON.stringify(ctrl.assigment[id]),
	   		  dateType: "json",
	   	      contentType: "application/json"  				
 		});        
        this.assigments[id]=null;
    }
    enqueueInputData(id,taskId){
        if(this.inputData[taskId]=== undefined) {
            if (this.inputDataBusy === 0) {
                this.queueInputData.push({'id': id, 'taskId': taskId});
                this.getInputData();
            } else {
                this.queueInputData.push({'id': id, 'taskId': taskId});
            }
        }else{
            this.sendInputData(id,taskId);
        }
    }
    getInputData(){
        this.inputDataBusy=1;
        let obj = this.queueInputData.pop();
        if(this.inputData[obj.taskId]===undefined){
        	$.ajax({
  	   		  type: "GET",
  	   		  url: "/wdc/getInputData?id="+obj.taskId,
  	   		  success: function(data){
  	   			  data = JSON.parse(data);
  	   			  ctrl.inputData[obj.taskId]=data;
  	   			  ctrl.sendInputData(obj.id,obj.taskId);
  	   			  if (ctrl.queueInputData.length!==0){
                    ctrl.getInputData();
  	   			  }else{
                    this.inputDataBusy=0;
  	   			  }
  	   		  }
        	});        	
        }
        else {
            this.sendInputData(obj.id,obj.taskId);
            if (ctrl.queueInputData.length!==0){
                ctrl.getInputData();
            }else{
                this.inputDataBusy=0;
            }
        }
    }
    sendInputData(id,taskId){
        ctrl.workers[id].postMessage({'cmd':'getInputData', 'data':ctrl.inputData[taskId], 'taskId':taskId})
    }
    toggleCog(id){
        $('#workerDisplay'+id+' .fa-cog:first').toggleClass('fa-spin');
    }
    addToCounter(id){
        let counter =+ $('#packageCounter'+id).text();
        $('#packageCounter'+id).html(counter+1);
        counter =+ $('#packageCounter').text();
        $('#packageCounter').html(counter+1);
    }
    returnPack(pack){
    	console.log("return id "+pack.id);
    	$.ajax({
  		  type: "POST",
  		  url: "/wdc/returnPackage",
  		  data: JSON.stringify(pack),
	   		  dateType: "json",
	   	      contentType: "application/json"  		 
 		});
    }
    eventListener(e)
    {
        switch(e.msg){
            case 'getPackage':
                this.enqueueTask(e.id);
                break;
            case 'returnData':
                this.addToCounter(e.id);
                this.toggleCog(e.id);
                this.returnPack(e.data);
                /*$(document).ready(function(){
                    $.post("db1.php/?fn=wynik&json="+e.data, function(){
                        ctrl.assigments[e.id]= null;
                    });
                });*/
                break;
            case 'getInputData':
                this.enqueueInputData(e.id,e.taskId);
                break;
            default:
                console.log(e.id + e.msg);
        }
    }
}