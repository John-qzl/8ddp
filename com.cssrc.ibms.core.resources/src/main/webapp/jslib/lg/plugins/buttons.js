(function ($)
{
    $.fn.buttons = function ()
    {
        return this.each(function ()
        {
        	
            if (this.manager) return;
          
            var g = {};
            this.manager = true;
            g.button = $(this);
            
            if (!g.button.hasClass("ib-btn")) g.button.addClass("ib-btn");
            
            g.button.append('<div class="ib-btn-l"></div><div class="ib-btn-r"></div>');  
            
            g.button.hover(function ()
            {
                $(this).addClass("ib-btn-over");
            }, function ()
            {
                $(this).removeClass("ib-btn-over");
            });
        });
    };

})(jQuery);