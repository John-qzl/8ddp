function waitForStyles(){for(var a=0;a<document.styleSheets.length;a++){if(/googleapis/.test(document.styleSheets[a].href)){return document.body.className+=" droid";}}setTimeout(waitForStyles,100);}setTimeout(function(){if(/AppleWebKit/.test(navigator.userAgent)&&/iP[oa]d|iPhone/.test(navigator.userAgent)){return;}var a=document.createElement("LINK");a.type="text/css";a.rel="stylesheet";a.href="http://fonts.googleapis.com/css?family=Droid+Sans|Droid+Sans:bold";document.documentElement.getElementsByTagName("HEAD")[0].appendChild(a);waitForStyles();},20);