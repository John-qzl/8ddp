	var initHeight=700;
	var planData=null;
	var currentToday=new Date();
	var pageDate=new Date();
	var lastOpt;
	var lastOptCss="";
	var calendarDialog;
	var currentSelectDate="";
	var selectDate;
	function initToday(){
		var todayY=currentToday.getFullYear();//本年
		var todayM=currentToday.getMonth()+1;//本月
        var todayD = currentToday.getDate();//本日
        var todayWD=currentToday.getDay();//本周几
        $("#currentDay").html(todayD > 9 ? todayD : '0' + todayD);
		$("#showDateII").html(todayY + "年" + (todayM > 9 ? todayM : "0" + todayM) + "月" + (todayD > 9 ? todayD : "0" + todayD) + '日');
 		$("#currentWeekDay").html(getWeekDay(todayWD));
	}
    //实现日历
    function calendar(showObj) {
    	clearData();
        var year = pageDate.getFullYear();      //选中年
        var month = pageDate.getMonth() + 1;    //选中月
        var day = pageDate.getDate();           //选中日
		var todayY=currentToday.getFullYear();//本年
		var todayM=currentToday.getMonth()+1;//本月
		var todayD=currentToday.getDate();//今天
		
		var fstart;//当月日历第一天
		var fend;//当月日历最后一天
		var todayStr=todayY+"-"+(todayM>9?todayM:"0"+todayM)+"-"+(todayD>9?todayD:"0"+todayD);
		selectDate=year+"-"+(month>9?month:"0"+month)+"-"+(day>9?day:"0"+day);
 		if("7"=="7"||"7"=="9"){
 			$("#showDate").html(year+"年"+(month>9?month:"0"+month)+"月");
 			
 		}else{
 			$("#showDate").html(year+"/"+(month>9?month:"0"+month));
 			
 		}
 		//计算出vStart,vEnd具体时间
 		//选中月第一天是星期几（距星期日离开的天数）
        var startDay = new Date(year, month - 1, 1).getDay();
 		var firstdate = new Date(year, month-1, 1);//选中月第一天
 		var lastMonth = new Date(year, month-2, 1);//选中月上个月第一天
		var nextMonth = new Date(year, month, 1);//选中月下月第一天
		
		var lastStr = lastMonth.getFullYear()+"-"+((lastMonth.getMonth() + 1)>9?(lastMonth.getMonth() + 1):"0"+(lastMonth.getMonth() + 1)); 
		var currentStr=year+"-"+(month>9?month:"0"+month);
 		var nextStr = nextMonth.getFullYear()+"-"+((nextMonth.getMonth() + 1)>9?(nextMonth.getMonth() + 1):"0"+(nextMonth.getMonth() + 1));   
 		var lastMothStart = DateAdd("d", -startDay, firstdate).getDate();//日期第一天
 		var lastMothend = DateAdd("d", -1, firstdate).getDate();//上月的最后一天

        //本月有多少天(即最后一天的getDate()，但是最后一天不知道，我们可以用“上个月的0来表示本月的最后一天”)
        var nDays = new Date(year, month, 0).getDate();
        
        //插入日历显示第一天
        fstart =new Date(year,month-2,lastMothStart);
        //开始画日历
        var numRow = 0;  //记录行的个数，到达7的时候创建tr
        var totalRow=1;
        var i;        //日期
        var html = '<tr><th class="title">周日</th>'+
			        '<th class="title">周一</th>'+
			        '<th class="title">周二</th>'+
			        '<th class="title">周三</th>'+
        			'<th class="title">周四</th>'+
        			'<th class="title">周五</th>'+
        			'<th class="title">周六</th></tr>';
        //第一行
        html += '<tr>';
        for (i = lastMothStart; startDay!=0&&i<=lastMothend; i++) {
            html += '<td  id="'+lastStr+'-'+i+'" onclick="prev(this)" data="">';
            html+='<div class="notSelectMonthDay " title="'+lastStr+'-'+i+'">';
            html+=i;
            html+='</div></td>';
            numRow++;
        }
        for (var j = 1; j <= nDays; j++) {
            if (year==todayY&&month==todayM&&j == todayD) {
                html += '<td id="'+currentStr+'-'+(j>9?j:'0'+j)+'" onclick="clickDate(this)" data="" >';  
                html += '<div class="currentCalendar" title="'+currentStr+'-'+(j>9?j:"0"+j)+'">';
            }
            else {
                html += '<td id="'+currentStr+'-'+(j>9?j:'0'+j)+'" onclick="clickDate(this)" data="">';
                html += '<div title="'+currentStr+'-'+(j>9?j:"0"+j)+'">';
            }
            html += j; 
            html += '</div></td>';
            numRow++;
            if (numRow == 7) {  //如果已经到一行（一周）了，重新创建tr
                numRow = 0;
                totalRow++;
                html += '</tr><tr>';
            }
        }
		//补充后一个月
        if(numRow>0){
        	for(var j=1;j<=7;j++){
	        	html += '<td  id="'+nextStr+'-0'+j+'" onclick="next(this)" data="">';
	        	html+='<div class="notSelectMonthDay " title="'+nextStr+'-0'+j+'">'+j+'</div></td>';
	            numRow++;
	        	if (numRow == 7) {  //如果已经到一行（一周）了，重新创建tr
	                numRow = 0;
	                html += '</tr>';
	                fend = new Date(year,month,j);//塞入最后一天日期
	            }
        	}
        }
        
        $('#LDay').html(html);
        
        initHeight=parseInt($('#LDay').height())+80+40+10+15;
        if(window.name&&window.name!=''){
        	window.parent.document.getElementsByName(window.name)[0].height=initHeight;
    	}
        //标记选中日期
        if(showObj!='undefined'&&showObj!=undefined){
        	$('div[title="'+showObj+'"]').addClass("currentSelect");
        }else{
        	if(selectDate!=todayStr){
	        	$('div[title="'+selectDate+'"]').addClass("currentSelect");
        	}
        }
        
        
        setTimeout(function(){getAgendaData(fstart,fend)}, 300);
			
   		
    }
    
    //清除备注信息
    function clearData(){
    	if(window.name&&window.name!=''){
    		parent.document.getElementsByName(window.name)[0].height=initHeight;
    	}
    	$('#planDataEventchd').html("");
    

    }

    //点击日期
    function clickDate(obj){
 		showData(obj);
 	}
 	
    //展示备注信息
 	function showData(obj){
 		if(window.name&&window.name!=''){
	    	parent.document.getElementsByName(window.name)[0].height=initHeight;
	    }
 		if(lastOpt==undefined||lastOpt=='undefined'){

    	}else{
    	   $(lastOpt).children('div').eq(0).addClass(lastOptCss);
    	}
    	
    	$('div').removeClass("currentSelect");
    	var divObj=$(obj).children('div').eq(0);
    	
    	lastOpt=obj;
    	lastOptCss=$(divObj).attr("class");
    	$(divObj).removeClass(lastOptCss);
    	$(divObj).addClass("currentSelect");
    	currentSelectDate=$(divObj).attr("title");
    	clearData();
 		var data=$(obj).attr("data");
 		if(data=='') return false;
 		var datas;
 		if (typeof (data) == "string") {
            datas=data.split(",");
        }

        if (typeof (data) == "object") {
            datas = data;
        }

 		var html='';
 		var cnt = 0;
 		for(var key in datas){
 			if(isNaN(key)) continue;
 			if(key == 0){
 				html+='<div class="dataEvent nth-of-typeEeven" id="'+planData[datas[key]][1]+'" onclick="clickData(\''+datas[key]+'\',this)" title="'+planData[datas[key]][2]+'\n'+planData[datas[key]][1]+'">';
 			}else {
 				html+='<div class="dataEvent" id="'+planData[datas[key]][1]+'" onclick="clickData(\''+datas[key]+'\',this)" title="'+planData[datas[key]][2]+'\n'+planData[datas[key]][1]+'">';
 			}
		    html+='<div class="dataEvent2" ><div class="dataEvent2_1">'+planData[datas[key]][3]+'&nbsp;&nbsp;'+planData[datas[key]][4]+'</div></div>';
		    html+='<div class="dataEvent3">'+planData[datas[key]][1]+'</div>';
		    html+='</div>';
		    cnt = cnt + 1;
 		}
 		if(html=='') html="暂无日程安排";
		
 		$('#planDataEventchd').html(html);
		
 	}
 	
 	
 	//点击数据 进入日程详细
 	function clickData(id,obj){
 		$("div.dataEvent").removeClass("nth-of-typeEeven");
 		$(obj).addClass("nth-of-typeEeven");
 		top['index']._OpenWindow({
 	 		 url: __ctx + "/oa/calendar/agenda/detail.do?agendaId="+id,
 	         title: "日程详细",
 	         width: 650, 
 	         height: 800,
 	         callback: function() {
 	        	 alert("回调成功1");
 	         }
 	 	});
 	}
 	
  //添加日程
  function doAdd(){
  	var date=new Date();
 	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	var hours = date.getHours();
	var minutes = date.getMinutes();
           
	var selectDate=year+"-"+(month>9?month:"0"+month)+"-"+(day>9?day:"0"+day);
	if(currentSelectDate!=''){
		selectDate=currentSelectDate;
	}
	var beginTime=(hours>9?hours:"0"+hours)+":"+(minutes>9?minutes:"0"+minutes);
	
	top['index']._OpenWindow({
 		 url: __ctx + "/oa/calendar/agenda/edit.do",
         title: "日程编辑",
         width: 600, 
         height: 500,
         callback: function() {
        	 location.href=location.href;
         }
 	});
	
  }
  
        
 function getWeekDay(day){
 	var weekDay="";
    if(day==0){
  		weekDay="星期日";
    }else if(day==1){
    	weekDay="星期一";
    }else if(day==2){
    	weekDay="星期二";
    }else if(day==3){
    	weekDay="星期三";
    }else if(day==4){
    	weekDay="星期四";
    }else if(day==5){
    	weekDay="星期五";
    }else if(day==6){
    	weekDay="星期六";
    }
 	return weekDay;
 }
 
 //下月按钮
 function next(obj){
 	pageDate.setDate(1);//设置本月第一天
 	pageDate.setMonth(pageDate.getMonth() + 1);
 	var idv=$(obj).attr("id");
 	if(idv!='prevbtn'&&idv!='nextbtn'){
 		calendar(idv);
 	}else{
 		calendar();
 	}
 }
 
 //上月按钮
 function prev(obj){
 	pageDate.setDate(1);//设置本月第一天
 	 pageDate.setMonth(pageDate.getMonth() - 1);
 	var idv=$(obj).attr("id");
 	if(idv!='prevbtn'&&idv!='nextbtn'){
 		calendar(idv);
 	}else{
 		calendar();
 	}
 	
 }
 
 //根据星期和月获取指定日期
 function DateAdd(interval, number, idate) {
 	   var date=new Date(idate.getFullYear(),idate.getMonth(),idate.getDate());
       number = parseInt(number);
       switch (interval) {
           case "y": date.setFullYear(date.getFullYear() + number); break;
           case "m": date.setMonth(date.getMonth() + number); break;
           case "d": date.setDate(date.getDate() + number); break;
           case "w": date.setDate(date.getDate() + 7 * number); break;
           case "h": date.setHours(date.getHours() + number); break;
           case "n": date.setMinutes(date.getMinutes() + number); break;
           case "s": date.setSeconds(date.getSeconds() + number); break;
           case "l": date.setMilliseconds(date.getMilliseconds() + number); break;
       }
       return date;
   }
 
 
 // 实时获取当月日程数据
 function getAgendaData(fstart,fend){
	 fstart= DateUtil.dateToStr("yyyy-MM-dd HH:mm:ss",fstart);
	 fend= DateUtil.dateToStr("yyyy-MM-dd HH:mm:ss",fend);
		$.ajax({
			url:__ctx+"/oa/calendar/agenda/show.do",
			dataType: 'json',
			data:{"startTime":fstart,"endTime":fend},
			success: function(data){
				var data = data;
				//添加含有日志的天样式
				var sd=$(".currentSelect").attr("title");
			 	if(sd==undefined||selectDate=='undefined'){
			 		sd=$(".currentCalendar").attr("title");
			 	}
				var datas=data.dateevents;
				planData=data.events;
				for(var key in datas){
					$('#'+key).children('div').eq(0).addClass("hashPlanDiv"); 
					$('#'+key).attr("data",datas[key]);
					if(key==sd){
						showData($('#'+sd));
					}
				}
				
			}
		});
}
 
 $(function(){
		initToday();
		calendar();
		
		if($("#planDataEvent").height(140).rollbar!=undefined){
			$("#planDataEvent").height(140).rollbar({
			   		sliderSize: '20%',
		 			scroll:'vertical', 
		 			pathPadding : 12,
		 			zIndex:120
				});
		}
 });