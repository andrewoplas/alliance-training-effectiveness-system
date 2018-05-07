$(document).ready(function() {
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
	
	var counter = 0;
	$('.btn-add-item').on('click', function(){
		var nestableList = '<li class="dd-item dd3-item" data-id="' + counter + '">' +
	        '<div class="dd-handle dd3-handle"></div>' +
	        '<div class="dd3-content" id="id-' + counter + '"> New Item</div>' +
	        '<button type="button" class="btn-remove-item btn btn-danger btn-outline btn-circle">'+
	        	'<i class="mdi mdi-close"></i></button>' +
        	'</li>';
		
		$('.dd-list:first').append(nestableList);
		$('#id-' + counter).editable();
		
		counter++;
	})
	
	$(document).on('click', '.btn-remove-item', function(){
		var parent = $(this).parents('li');
			parent.addClass('zoomOut animated');
			setTimeout(function(){
				parent.remove();
			}, 500);
	});
	
	$("#nice").on('click', function(){
		var item = $('#nestable2').nestable('serialize');
		
		$.each(item, function(index, value){
			value['content'] = $('[data-id=' + value.id + ']').find('.dd3-content').first().text();
			if ('children' in value){
				addContent(value.children);
			}
		});
		
		console.log(item);
	});
	
	function addContent(items){
		$.each(items, function(index, value){
			value['content'] = $('[data-id=' + value.id + ']').find('.dd3-content').first().text();
			if ('children' in value){
				addContent(value.children);
			}
		});
	}
	
	//turn to inline mode
	$.fn.editable.defaults.mode = 'popup';
});