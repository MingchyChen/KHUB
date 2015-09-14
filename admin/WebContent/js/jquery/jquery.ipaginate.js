(function($) {
	$.fn.ipaginate = function(options) {
		var opts = $.extend({}, $.fn.ipaginate.defaults, options);
		
		return this.each(function() {
			$this = $(this);
			
			//產生Grid資料和分頁器
			$.fn.getPaginationData(opts.page, opts, $this);
		});		
	};
	
	$.fn.paginate.defaults = {
		page: 1,//要顯示第幾頁資料
		pageSize: 10,//每頁顯示幾筆資料
		url: null,//索取資料URL
		gridSelection: null,//放置grid資料tbody的選擇器
		gridColumns: null//grid欄位名稱
	};
	
	//依指定的URL跟伺服器取得總頁數以及分頁資料
	$.fn.getPaginationData = function(page, opts, selectedObj) {
		$.getJSON(opts.url, 
				{
					page: page,
					pageSize: opts.pageSize
				}, 
				function(jsonData) {
					if(jsonData) {
						$.fn.createGrid(jsonData, opts);//產生Grid區塊
						$.fn.createPagination(page, jsonData.pageCount, opts, selectedObj);//產生分頁器區塊
					}
				});
	}
	
	//使用jquery.paginate來產生分頁器區塊
	$.fn.createPagination = function(page, pageCount, opts, selectedObj) {
		//selectedObj.paginate({
		$("div#pagination").paginate({
			count 		: pageCount,
			start 		: page,
			display     : 10,
			border					: true,
			border_color			: '#BEF8B8',
			text_color  			: '#68BA64',
			background_color    	: '#E3F2E1',	
			border_hover_color		: '#68BA64',
			text_hover_color  		: 'black',
			background_hover_color	: '#CAE6C6',
			rotate      : false,
			images		: false,
			mouse		: 'press',
			onChange    : function(page){
				$.fn.getPaginationData(page, opts, selectedObj);//點擊分頁器後，重新產生Grid資料以及分頁器
            } 
		});
	}
	
	//產生Grid區塊
	$.fn.createGrid = function(jsonData, opts) {
		var insetViewData = "";
		var aaData = jsonData.aaData;
		$.each(aaData, function(index, items) {
	        	insetViewData += $.fn.createTR(items, opts);
	         });

		$(opts.gridSelection).html(insetViewData);
	}

	//產生Grid區塊內單行資料，不同的Grid會因為顯示的欄位名稱不同而有所調整
	$.fn.createTR = function(obj, opts){
		var tr = "<tr>";
		$.each(opts.gridColumns, function(i, columnName) {
			tr += "<td class=\""+ columnName +"\">" + obj[columnName] + "</td>";
			     	
		});
		tr += "</tr>";
		return tr;
	}
})(jQuery);