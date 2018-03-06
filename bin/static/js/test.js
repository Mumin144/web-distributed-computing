/**
 * 
 */
function zxc(packData){
 	var data = packData[0];
 	if (data.x > data.y){
 		packData[3]="x";
 	}else{
 		if (data.x < data.y){
 			packData[3]="y";
 	 	}else{
 	 		packData[3]="xy";
 	 	}
 	}
 	var response = {"q": data, "a":packData[3]};
 	//data = JSON.stringify(response);
	returnData(response);
	if (stopWorker==0){
		getNewData();
    }
    return ;
}
