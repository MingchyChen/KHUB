/*
 * SimpleModal Confirm Modal Dialog
 * http://www.ericmmartin.com/projects/simplemodal/
 * http://code.google.com/p/simplemodal/
 *
 * Copyright (c) 2009 Eric Martin - http://ericmmartin.com
 *
 * Licensed under the MIT license:
 *   http://www.opensource.org/licenses/mit-license.php
 *
 * Revision: $Id: confirm.js 212 2009-09-03 05:33:44Z emartin24 $
 *
 */

function confirm2(message, callback) {
	$('#confirm').modal({
		closeHTML:"<a href='#' title='關閉' class='modal-close'>x</a>",
		position: ["30%",],
		overlayId:'confirm-overlay',
		containerId:'confirm-container', 
		onShow: function (dialog) {
			$('.message', dialog.data[0]).append(message);

			/*
			// if the user clicks "yes"
			$('.yes', dialog.data[0]).click(function () {
				// call the callback
				if ($.isFunction(callback)) {
					callback.apply();
				}
				// close the dialog
				$.modal.close();
			});*/
		}
	});
}
function confirm3(message, callback) {
	
	$('#confirm').modal({
		closeHTML:"<a href='#' title='關閉' class='modal-close'>x</a>",
		position: ["30%",],
		overlayId:'confirm-overlay',
		containerId:'confirm-container2', 
		autoPosition : true,
		onShow: function (dialog) {
			$('.message', dialog.data[0]).append(message);
		}
	});
}