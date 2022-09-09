//4*4拼图位置随机打乱
var place = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16];
var middle;
//jQuery使用$(function(){}) ,js使用window.onload = function(){}
$(function(){
	sui();
});
function sui() {
    for(var i = 0;i<30;i++){
        for(var j = 0;j<place.length;j++){
            if(place[j] == 16){
                if(j == 0 || j == 3 || j == 12 || j == 15){
                    var rad = parseInt(Math.random()*2);
                    if(rad == 0){
                        if(j == 0 || j == 12){
                            changen(j,"右");
                        }else if(j == 3 || j == 15){
                            changen(j,"左");
                        }
                    }else{
                        if(j == 0 || j == 3){
                            changen(j,"下");
                        }else if(j == 12 || j == 15){
                            changen(j,"上");
                        }
                    }
                }else if(j == 1 || j == 2 || j == 4 || j == 8 || j == 7 || j == 11 || j == 13 || j == 14){
                    var rad = parseInt(Math.random()*3);
                    if(rad == 0){
                        if(j == 1 || j == 2 || j == 13 || j == 14){
                            changen(j,"左");
                        }else if(j == 4 || j == 8 || j == 7 || j == 11){
                            changen(j,"上");
                        }
                    }else if(rad == 1){
                        if(j == 1 || j == 2 || j == 13 || j == 14){
                            changen(j,"右");
                        }else if(j == 4 || j == 8 || j == 7 || j == 11){
                            changen(j,"下");
                        }
                    }else{
                        if(j == 1 || j == 2){
                            changen(j,"下");
                        }else if(j == 4 || j == 8){
                            changen(j,"右");
                        }else if(j == 7 || j == 11){
                            changen(j,"左");
                        }else if(j == 13 || j == 14){
                            changen(j,"上");
                        }
                    }
                }else{
                    var rad = parseInt(Math.random()*4);
                    if(rad == 0){
                        changen(j,"上");
                    }else if(rad == 1){
                        changen(j,"下");
                    }else if(rad == 2){
                        changen(j,"左");
                    }else if(rad == 3){
                        changen(j,"右");
                    }
                }
            }
        }
    }
}

function changen(j,wei){
	if(wei == "上"){
		middle = place[j-4];
		place[j-4] = place[j];
		place[j] = middle;
	}else if(wei == "下"){
		middle = place[j+4];
		place[j+4] = place[j];
		place[j] = middle;
	}else if(wei == "左"){
		middle = place[j-1];
		place[j-1] = place[j];
		place[j] = middle;
	}else if(wei == "右"){
		middle = place[j+1];
		place[j+1] = place[j];
		place[j] = middle;
	}
}

function huan(wei) {
    var zhi1 = $("#"+(parseInt(wei)-1)).attr("val");//左面值
	if (parseInt(zhi1) == 15) {
		if (parseInt(wei) != 1 && parseInt(wei) != 5 && parseInt(wei) != 9 && parseInt(wei) != 13) {
			var zhong = $("#"+parseInt(wei)).attr("val");
            $("#"+parseInt(wei)).attr("src","../image/pintu/bai.png");
            $("#"+parseInt(wei)).attr("val","15");
            $("#"+(parseInt(wei)-1)).attr("src",urlPath+name+zhong+".jpg");
            $("#"+(parseInt(wei)-1)).attr("val",zhong);

		}
	}
    var zhi2 = $("#"+(parseInt(wei)-4)).attr("val");//上面值
    if (parseInt(zhi2) == 15) {
        if (parseInt(wei) != 1 && parseInt(wei) != 2 && parseInt(wei) != 3 && parseInt(wei) != 4) {
            var zhong = $("#"+parseInt(wei)).attr("val");
            $("#"+parseInt(wei)).attr("src","../image/pintu/bai.png");
            $("#"+parseInt(wei)).attr("val","15");
            $("#"+(parseInt(wei)-4)).attr("src",urlPath+name+zhong+".jpg");
            $("#"+(parseInt(wei)-4)).attr("val",zhong);

        }
    }
    var zhi3 = $("#"+(parseInt(wei)+1)).attr("val");//右面值
    if (parseInt(zhi3) == 15) {
        if (parseInt(wei) != 4 && parseInt(wei) != 8 && parseInt(wei) != 12 && parseInt(wei) != 16) {
            var zhong = $("#"+parseInt(wei)).attr("val");
            $("#"+parseInt(wei)).attr("src","../image/pintu/bai.png");
            $("#"+parseInt(wei)).attr("val","15");
            $("#"+(parseInt(wei)+1)).attr("src",urlPath+name+zhong+".jpg");
            $("#"+(parseInt(wei)+1)).attr("val",zhong);

        }
    }
    var zhi4 = $("#"+(parseInt(wei)+4)).attr("val");//下面值
    if (parseInt(zhi4) == 15) {
        if (parseInt(wei) != 13 && parseInt(wei) != 14 && parseInt(wei) != 15 && parseInt(wei) != 16) {
            var zhong = $("#"+parseInt(wei)).attr("val");
            $("#"+parseInt(wei)).attr("src","../image/pintu/bai.png");
            $("#"+parseInt(wei)).attr("val","15");
            $("#"+(parseInt(wei)+4)).attr("src",urlPath+name+zhong+".jpg");
            $("#"+(parseInt(wei)+4)).attr("val",zhong);

        }
    }

}