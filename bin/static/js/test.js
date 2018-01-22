/**
 * 
 */
function zxc(packData){
 	data = packData[0];
 	if (data.x > data.y){
 		packData[3]="x";
 	}else{
 		if (data.x < data.y){
 			packData[3]="y";
 	 	}else{
 	 		packData[3]="xy";
 	 	}
 	}
 	data = JSON.stringify(packData);
	returnData(data);
	if (stopWorker==0){
		getNewData();
    }
    return ;
}
