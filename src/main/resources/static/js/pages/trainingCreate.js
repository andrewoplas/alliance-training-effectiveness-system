$(document).ready(function() {
	var trainingPlanObject = {};
	
	// Nestable
	var updateOutput = function(e) {
	    var list = e.length ? e : $(e.target),
	        output = list.data('output');
	    if (window.JSON) {
	        output.val(window.JSON.stringify(list.nestable('serialize'))); //, null, 2));
	    } else {
	        output.val('JSON browser support required for this demo.');
	    }
	};
	$('#nestable2').nestable({
	    group: 1
	}).on('change', updateOutput);
	updateOutput($('#nestable2').data('output', $('#nestable2-output')));
	$('#nestable2').on('click', function(e) {
	    var target = $(e.target),
	        action = target.data('action');
	    if (action === 'expand-all') {
	        $('.dd').nestable('expandAll');
	    }
	    if (action === 'collapse-all') {
	        $('.dd').nestable('collapseAll');
	    }
	});
	
	// Add item to nestable
	var counter = 1;
	$('.btn-add-item').on('click', function(){
		var nestableList = '<li class="dd-item dd3-item" data-id="' + counter + '">' +
	        '<div class="dd-handle dd3-handle"></div>' +
	        '<div class="dd3-content" id="id-' + counter + '">New Item</div>' +
	        '<button type="button" class="btn-remove-item btn btn-danger btn-outline btn-circle">'+
	        	'<i class="mdi mdi-close"></i></button>' +
        	'</li>';
		
		$('.dd-list:first').append(nestableList);
		$('#id-' + counter).editable();
		
		counter++;
		
		if(!$('.help-block-outline').hasClass('hide')) {
			$('.help-block-outline').addClass('hide');
		}
	})
	
	// Initialize editable
	$('#id-0').editable();
	
	
	// Remove item to nestable
	$(document).on('click', '.btn-remove-item', function(){
		var parent = $(this).parent();
			parent.addClass('zoomOut animated');
			setTimeout(function(){
				parent.remove();
			}, 500);
	});
	
	
	//turn to inline mode
	$.fn.editable.defaults.mode = 'popup';
	
	// Validation First Fieldset
	$('.first-fieldset .validate-empty, .second-fieldset .validate-empty').blur(function(){
		if($.trim($(this).val()).length == 0) {
			$(this).parents('.form-group').addClass('has-error');
			$(this).parents('.form-group').find('.help-block').removeClass('hide');
		} else {
			$(this).parents('.form-group').removeClass('has-error');
			$(this).parents('.form-group').find('.help-block').addClass('hide');
		}
	})	
	
	// Multi-select (Semantic) Configuration
	$('.multi-select').dropdown({
	    onAdd: function(value, text, $selectedItem) {
	    	$('.multi-select .menu').find('[data-value='+ value +']').hide();
	    	
	    	$(this).parents('.panel').find('.help-block').addClass('hide');
	    },
	    onRemove: function(value, text, $selectedItem) {
	    	$('.multi-select .menu').find('[data-value='+ value +']').show();
	    },
	    onLabelRemove: function(e) {
	    	$(this).tooltip('destroy');
	    	
            return !0
        },
	    onLabelCreate: function(t, n) {
	    	var value = $(this).attr('data-value');
	    	var option = $('.multi-select select').find('[value='+value+']');
	    	var email = option.attr('data-email');
	    	
	    	$(this).attr('data-toggle', 'tooltip');
	    	$(this).attr('data-placement', 'top');
	    	$(this).attr('title', email);
	    	$(this).tooltip();
	    	
	    	return $(this);
        },
	  });
	
	// Initialize tooltip
	$('[data-toggle=tooltip]').tooltip();
	
	// Gather Summary Information
	$('#third-step').on('click', function(){
		// First Fieldset
		var title = $('#training').val();
		var description = $('#description').val();
		var calendar = $('#calendar').fullCalendar('clientEvents');
		calendar.sort(function(a, b){return a.start - b.start});
		
		// Second Fieldset				
		var item = $('#nestable2').nestable('serialize');
		$.each(item, function(index, value){
			value['content'] = $('[data-id=' + value.id + ']').find('.dd3-content').first().text();
			if ('children' in value){
				addContent(value.children);
			}
		});
		
		// Third Fieldset
		var supervisors = [], facilitators = [], participants  = [];				
		$.each($('#supervisor-select').dropdown('get activeItem'), function(index, value){
			supervisors.push($(value).text());
		});
		
		$.each($('#facilitator-select').dropdown('get activeItem'), function(index, value){
			facilitators.push($(value).text());
		});
		
		$.each($('#participant-select').dropdown('get activeItem'), function(index, value){
			participants.push($(value).text());
		});
		
		
		// Set it!
		$('[data-value=training]').text(title);
		$('[data-value=description]').text(description);
		
		var scheduleHTML = "";
		var dateArr = [];
		$.each(calendar, function(index, item) {
			scheduleHTML += 
				'<tr>' +
					'<td>'+ (index+1) +'</td>' +
					'<td>'+ item.start.format('MMM D, YYYY') +'</td>' +
					'<td>'+ item.start.format('h:mm A') +'</td>' +
					'<td>'+ item.end.format('h:mm A') +'</td>' +
					'<td>'+ item.start.format('dddd') +'</td>' +
				'</tr>';
			
			dateArr.push({
				date: item.start.format('YYYY-MM-DD'), 
				startTime: item.start.format('HH:mm:00'), 
				endTime: item.end.format('HH:mm:00'), 
				className: item.className[0]
			});
		});
		
		
		$('#schedule-table tbody').html(scheduleHTML);
		if (!$.fn.dataTable.isDataTable('#schedule-table')) {
			$('#schedule-table').DataTable({
		        "paging":   false,
		        "info":     false,
		        "searching": false
		    });
		}
		
		var courseOutlineHTML = $('<ol id="parent-outline"></ol>');
		$.each(item, function(index, value){
			courseOutlineHTML.append('<li>' + value.content + '</li>')
			if ('children' in value){
				courseOutlineHTML.find('li:last').append(addContentHTML(value.children));
			}
		});
		$('[data-value=courseOutline]').html(courseOutlineHTML);
		
		var supervisorHTML = "";
		for(var i=0; i<supervisors.length; i++) {
			supervisorHTML += 
				'<span class="m-b-10">' +
				'<i class="mdi mdi-account-box-outline m-r-5"></i>' +
					supervisors[i] +
				'</span><br/>';
		}
		
		var facilitatorHTML = "";
		for(var i=0; i<facilitators.length; i++) {
			facilitatorHTML += 
				'<span class="m-b-10">' +
				'<i class="mdi mdi-account-box-outline m-r-5"></i>' +
					facilitators[i] +
				'</span><br/>';
		}
		
		var participantHTML = "<ol class='p-l-15'>";
		for(var i=0; i<participants.length; i++) {
			participantHTML += 
				'<li class="m-b-10">' +
					participants[i] +
				'</li>';
		}
		participantHTML += "</ol>";
		
		$('[data-value=supervisors]').html(supervisorHTML);
		$('[data-value=facilitators]').html(facilitatorHTML);
		$('[data-value=participants]').html(participantHTML);	
		
		
		// Setup the Object
		trainingPlanObject['title'] = title;
		trainingPlanObject['description'] = description;
		trainingPlanObject['calendar'] = dateArr;
		trainingPlanObject['courseOutline'] = JSON.stringify(item);
		trainingPlanObject['supervisors'] = $('#supervisor-select').dropdown('get value');
		trainingPlanObject['facilitators'] = $('#facilitator-select').dropdown('get value');
		trainingPlanObject['participants'] = $('#participant-select').dropdown('get value');
	});
	
	function addContent(items){
		$.each(items, function(index, value){
			value['content'] = $('[data-id=' + value.id + ']').find('.dd3-content').first().text();
			if ('children' in value){
				addContent(value.children);
			}
		});
	}
	
	function addContentHTML(items){
		var parent = $("<ol></ol>");
		$.each(items, function(index, value){
			parent.append('<li>' + value.content + '</li>');
			if ('children' in value){
				parent.find('li:last').append(addContentHTML(value.children));
			}
		});
		
		return parent;
	}
	
	$(".submit").on('click', function(){
		$("#ajax-process").modal('show');
		
		$.ajax({
			url: "/ates/training/create",
			type: 'POST',
			contentType : 'application/json; charset=utf-8',
			dataType : 'json',
			data: JSON.stringify(trainingPlanObject),
			success: function(data, textStatus, jqXHR) {
				$("#ajax-process").modal('hide');
                 if(data == true) {
                	 swal({   
                         title: "Success!",
                         type: "success",
                         text: "We have successfully created the training plan",
                         confirmButtonColor: "#1e88e5",
                         showConfirmButton: true,
                	 }, function(isConfirm){   
                         if (isConfirm) {     
                        	 window.location.href = "/ates/training/list";   
                         } 
                     });               	 
                 } else {
                	 showErrorAlert();
                 }
            },
            error: function(jqXHR, status, error) {
            	showErrorAlert();
            }
         });
	});
	
	
});