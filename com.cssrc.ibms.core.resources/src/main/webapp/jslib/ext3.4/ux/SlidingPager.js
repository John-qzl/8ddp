/*
 * Ext JS Library 3.4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
Ext.ux.SlidingPager=Ext.extend(Object,{init:function(b){var a=b.items.indexOf(b.inputItem);Ext.each(b.items.getRange(a-2,a+2),function(d){d.hide();});var c=new Ext.Slider({width:114,minValue:1,maxValue:1,plugins:new Ext.slider.Tip({getText:function(d){return String.format("Page <b>{0}</b> of <b>{1}</b>",d.value,d.slider.maxValue);}}),listeners:{changecomplete:function(e,d){b.changePage(d);}}});b.insert(a+1,c);b.on({change:function(d,e){c.setMaxValue(e.pages);c.setValue(e.activePage);}});}});