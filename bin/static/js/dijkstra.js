function dijkstraGraf(taskData , inputData) {
    var time = Date.now();
    var n = inputData.length;//ilość wierzchołków
    var q = n;
    var temp = {'x': 0, 'y': 0, 'w': null};
    var d = [];//koszty dojscia
    var p = [];//poprzednicy
    var start = taskData.s; //- 1;
    var stop = taskData.f; //- 1;
    d[start] = 0;
    p[start] = start;
    var y;
    var x;
    for (y = 0; y < n; y++) {
        if ((temp['w'] === null && start !== y)) {
            temp = {'x': start, 'y': y, 'w': inputData[start][y]};
        } else {
            if (temp['w'] > inputData[start][y] && start !== y) {
                temp = {'x': start, 'y': y, 'w': inputData[start][y]};
            }
        }
    }
    d[temp['y']] = temp['w'];
    p[temp['y']] = start;
    temp = {'x': null, 'y': null, 'w': null};
    while (q != 0) {
        d.forEach(function (value, i) {
            for (y = 0; y < n; y++) {
                if ((temp['w'] === null && inputData[i][y] !== 0)) {
                    temp = {'x': i, 'y': y, 'w': inputData[i][y] + d[i]};
                } else {
                    if (temp['w'] > inputData[i][y] + d[i] && inputData[i][y] !== 0 && d[y] === undefined) {
                        temp = {'x': i, 'y': y, 'w': inputData[i][y] + d[i]};
                    }
                }
            }
        })
        d[temp['y']] = temp['w'];
        p[temp['y']] = temp['x'];
        temp = {'x': null, 'y': null, 'w': null};
        q--;
        if (d[stop] !== undefined) {
            break;
        }
    }
    let droga = [];
    temp = p[stop];
    droga.push(stop);
    while (temp !== start) {
        droga.unshift(temp);
        temp = p[temp];
    }
    droga.unshift(temp);
    let response ={
		"s": taskData.s,
    	"f": taskData.f,
    	"way": droga,
    	"sum": d[stop],
    	"time": Date.now() - time
    	};
    response = JSON.stringify(response);
    returnData(response);
    if (!stopWorker){
		getNewData();
    }
    return ;

}