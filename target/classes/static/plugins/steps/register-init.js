$(function() {

	//jQuery time
	var current_fs, next_fs, previous_fs; //fieldsets
	var left, opacity, scale; //fieldset properties which we will animate
	var animating; //flag to prevent quick multi-click glitches
	
	$(".next").click(function(){
		if(animating) return false;
		
		var error = false;
		var hash = [];
		$('.first-fieldset .form-control').each(function(){
			if($.trim($(this).val()).length == 0) {
				$(this).parents('.form-group').addClass('has-error');
				$(this).parents('.form-group').find('.help-block-empty').removeClass('hide');
				hash.push($(this).attr('id'));
				error = true;
			} else if ($(this).parents('.form-group').hasClass('has-error')) {
				hash.push($(this).attr('id'));
				error = true;
			}
		});
		
		// First fieldset
		if($('#calendar').fullCalendar('clientEvents').length == 0 && $(this).attr('id') == 'first-step') {
			$('#calendar').parent().find('.help-block-schedule').removeClass('hide');
			hash.push('calendar');
			error = true;
		}
		
		// Second fieldset
		if($('#nestable2').nestable('serialize').length == 0 && $(this).attr('id') == 'second-step') {
			$('#nestable2').parents('fieldset').find('.help-block-outline').removeClass('hide');
			error = true;
		}
		
		// Third fieldset
		if($(this).attr('id') == 'third-step') {
			$('.multi-select').each(function(){
				if(($(this).dropdown('get value')) == null) {
					$(this).parents('.panel').find('.help-block').removeClass('hide');
					hash.push($(this).parents('.panel-collapse').attr('id'));
					error = true;
				}
			});
		}
		
		if(hash.length > 0) {
			var moreOffset = 100;
			if(hash[0] == 'calendar' || hash[0].indexOf('select') !== -1) moreOffset = 0;

			// animate
	       $('html, body').animate({
	           scrollTop: $('#' + hash[0]).offset().top - moreOffset
	       }, 300);
		}
		
		if(error){
			return false;
		}	
		
		animating = true;
		
		current_fs = $(this).parents('fieldset');
		next_fs = $(this).parents('fieldset').next();
		
		//activate next step on progressbar using the index of next_fs
		$("#eliteregister li").eq($("fieldset").index(next_fs)).addClass("active");
		
		//show the next fieldset
		next_fs.show(); 
		//hide the current fieldset with style
		current_fs.animate({opacity: 0}, {
			step: function(now, mx) {
				//as the opacity of current_fs reduces to 0 - stored in "now"
				//1. scale current_fs down to 80%
				scale = 1 - (1 - now) * 0.2;
				//2. bring next_fs from the right(50%)
				left = (now * 50)+"%";
				//3. increase opacity of next_fs to 1 as it moves in
				opacity = 1 - now;
				current_fs.css({'transform': 'scale('+scale+')'});
				next_fs.css({'transform': 'scale(1)'});
				next_fs.css({'left': left, 'opacity': opacity});
			}, 
			duration: 600, 
			complete: function(){
				current_fs.hide();
				animating = false;
			}, 
			//this comes from the custom easing plugin
			easing: 'easeInOutBack'
		});
	});
	
	$(".previous").click(function(){
		if(animating) return false;
		animating = true;
		
		current_fs = $(this).parents('fieldset');
		previous_fs = $(this).parents('fieldset').prev();
		
		//de-activate current step on progressbar
		$("#eliteregister li").eq($("fieldset").index(current_fs)).removeClass("active");
		
		//show the previous fieldset
		previous_fs.show(); 
		//hide the current fieldset with style
		current_fs.animate({opacity: 0}, {
			step: function(now, mx) {
				//as the opacity of current_fs reduces to 0 - stored in "now"
				//1. scale previous_fs from 80% to 100%
				scale = 0.8 + (1 - now) * 0.2;
				//2. take current_fs to the right(50%) - from 0%
				left = ((1-now) * 50)+"%";
				//3. increase opacity of previous_fs to 1 as it moves in
				opacity = 1 - now;
				current_fs.css({'left': left});
				previous_fs.css({'transform': 'scale('+scale+')', 'opacity': opacity});
			}, 
			duration: 800, 
			complete: function(){
				current_fs.hide();
				animating = false;
			}, 
			//this comes from the custom easing plugin
			easing: 'easeInOutBack'
		});
	});
	
	$(".fieldset-goto").click(function(){
		if(animating) return false;
		animating = true;
		
		var gotoFieldset = $(this).attr('fieldset');
		
		current_fs = $(this).parents('fieldset');
		previous_fs = $('.' + gotoFieldset + '-fieldset');
		
		//de-activate current step on progressbar
		$("#eliteregister li").eq($("fieldset").index(current_fs)).removeClass("active");
		var removeActive = false;
		$("#eliteregister li").each(function(){
			if(removeActive) {
				$(this).removeClass('active');
				return;
			}
			
			removeActive = $(this).hasClass(gotoFieldset + '-li');
		});
		
		//show the previous fieldset
		previous_fs.show(); 
		//hide the current fieldset with style
		current_fs.animate({opacity: 0}, {
			step: function(now, mx) {
				//as the opacity of current_fs reduces to 0 - stored in "now"
				//1. scale previous_fs from 80% to 100%
				scale = 0.8 + (1 - now) * 0.2;
				//2. take current_fs to the right(50%) - from 0%
				left = ((1-now) * 50)+"%";
				//3. increase opacity of previous_fs to 1 as it moves in
				opacity = 1 - now;
				current_fs.css({'left': left});
				previous_fs.css({'transform': 'scale('+scale+')', 'opacity': opacity});
			}, 
			duration: 800, 
			complete: function(){
				current_fs.hide();
				animating = false;
			}, 
			//this comes from the custom easing plugin
			easing: 'easeInOutBack'
		});
	});
});